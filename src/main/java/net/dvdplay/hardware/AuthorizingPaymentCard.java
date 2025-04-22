package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.userop.MaxDiscsAllowedException;
import net.dvdplay.userop.ProcessPaymentDisabledCardException;
import net.dvdplay.userop.ProcessPaymentValidationException;
import net.dvdplay.userop.PromoCodeException;
import net.dvdplay.userop.RentOpException;

public class AuthorizingPaymentCard extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   static int verificationRetry = 0;

   public AuthorizingPaymentCard(ActionListener lAction, JComponent lComponent, String data) {
      component = lComponent;
      action = lAction;
   }

   public static void resetVerificationRetry() {
      verificationRetry = 0;
   }

   public void run() {
      try {
         AbstractHardwareThread.mRentOp.processPayment();
         verificationRetry = 0;
         action.actionPerformed(new ActionEvent(component, 1001, "PaymentCardApprovedScreen"));
      } catch (MaxDiscsAllowedException var7) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "MaxDiscsAllowed reached");
         action.actionPerformed(new ActionEvent(this, 1001, "MaximumDiscExceededScreen"));
      } catch (PromoCodeException var8) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var8.getMessage());
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Promocode not associated with this card");
         action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4012,CartTableScreen "));
      } catch (ProcessPaymentValidationException var9) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var9.getMessage());
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Payment card validation try " + (verificationRetry + 1) + " failed");
         verificationRetry++;
         if (verificationRetry < 3) {
            action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4036,ZipCodeScreen "));
         } else {
            verificationRetry = 0;
            action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4036,CartTableScreen "));
         }
      } catch (ProcessPaymentDisabledCardException var10) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var10.getMessage());
         Aem.logDetailMessage(DvdplayLevel.WARNING, "PaymentCard Disabled");
         action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4044,CartTableScreen "));
      } catch (RentOpException var11) {
         switch (var11.getCode()) {
            case -1007:
               Aem.logDetailMessage(DvdplayLevel.WARNING, "invalid card");
               action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4015,CartTableScreen "));
               break;
            case -1006:
            default:
               Aem.logDetailMessage(DvdplayLevel.WARNING, "Unknown error code: " + var11.getCode());
               action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4013,CartTableScreen "));
               break;
            case -1005:
               Aem.logDetailMessage(DvdplayLevel.WARNING, "unspecified");
               action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4015,CartTableScreen "));
               break;
            case -1004:
               Aem.logDetailMessage(DvdplayLevel.WARNING, "comm?");
               action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4015,CartTableScreen "));
               break;
            case -1003:
               Aem.logDetailMessage(DvdplayLevel.WARNING, "invalid account");
               action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4015,CartTableScreen "));
               break;
            case -1002:
               Aem.logDetailMessage(DvdplayLevel.WARNING, "expired card");
               action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4014,CartTableScreen "));
               break;
            case -1001:
               Aem.logDetailMessage(DvdplayLevel.WARNING, "declined card");
               action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4015,CartTableScreen "));
         }

         Aem.logDetailMessage(DvdplayLevel.WARNING, var11.getMessage());
      } catch (Exception var12) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Exception caught: " + var12.getMessage(), var12);
         action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4013,CartTableScreen "));
      }
   }
}
