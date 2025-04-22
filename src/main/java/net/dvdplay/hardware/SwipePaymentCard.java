package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.CreditCardThread;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class SwipePaymentCard extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   String data;

   public SwipePaymentCard(ActionListener lAction, JComponent lComponent, String lData) {
      component = lComponent;
      action = lAction;
      this.data = lData;
   }

   public void run() {
      boolean cardSwipedSuccessful = true;

      try {
         AbstractHardwareThread.mRentOp.startCreditCardReader();

         while (!AbstractHardwareThread.mRentOp.isCreditCardReaderDone() && AEMGui.isCurrentThread(this)) {
            Util.sleep(100);
         }

         if (!AEMGui.isCurrentThread(this)) {
            AbstractHardwareThread.mRentOp.stopCreditCardReader();
         }

         if (CreditCardThread.stopped) {
            cardSwipedSuccessful = false;
         }

         Aem.logDetailMessage(DvdplayLevel.FINE, "cardswipesuccessful is " + cardSwipedSuccessful);
         String nextScreen;
         if (cardSwipedSuccessful) {
            Aem.logDetailMessage(DvdplayLevel.FINE, "Paymentcardtypeis " + AbstractHardwareThread.mRentOp.getPaymentCardTypeId());
            Aem.logDetailMessage(DvdplayLevel.FINE, "Verificationtype is " + Aem.getVerificationType(AbstractHardwareThread.mRentOp.getPaymentCardTypeId()));
            if (Aem.getVerificationType(AbstractHardwareThread.mRentOp.getPaymentCardTypeId()) == 1) {
               nextScreen = "ZipCodeScreen";
            } else {
               nextScreen = "AuthorizingPaymentScreen";
            }
         } else {
            this.data = "";
            nextScreen = "CartTableScreen";
         }

         if (AEMGui.isCurrentThread(this)) {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  SwipePaymentCard.action.actionPerformed(new ActionEvent(SwipePaymentCard.component, 1001, nextScreen + DvdplayBase.SPACE_STRING + SwipePaymentCard.this.data));
               }
            });

         }
      } catch (DvdplayException var4) {
         cardSwipedSuccessful = false;
         Aem.logDetailMessage(DvdplayLevel.WARNING, var4.getMessage());
         action.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4039,SwipePaymentCardScreen"));
      }
   }
}
