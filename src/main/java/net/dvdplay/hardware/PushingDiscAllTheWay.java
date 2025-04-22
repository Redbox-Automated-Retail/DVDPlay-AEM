package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.ServoException;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;

public class PushingDiscAllTheWay extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   String nextScreen = "ReturnThankYouScreen Return";

   public PushingDiscAllTheWay(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      try {
         AbstractHardwareThread.mReturnOp.intakeDisc2();
         if (Aem.inQuiesceMode()) {
            this.nextScreen = "MainScreen";
         }
      } catch (ServoException var3) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var3.getMessage());
         this.nextScreen = "ErrorScreen 4033,InitializingAEMScreen";
      } catch (DvdplayException var4) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var4.getMessage());
         this.nextScreen = "ReturnErrorScreen";
      }

      SwingUtilities.invokeLater(() -> PushingDiscAllTheWay.action.actionPerformed(new ActionEvent(PushingDiscAllTheWay.component, 1001, PushingDiscAllTheWay.this.nextScreen)));
   }
}
