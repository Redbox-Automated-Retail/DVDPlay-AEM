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

public class ReturningMovie extends AbstractHardwareThread {
   ActionListener action;
   JComponent component;
   String nextScreen = "ReturnThankYouScreen Return";

   public ReturningMovie(ActionListener lAction, JComponent lComponent) {
      this.component = lComponent;
      this.action = lAction;
   }

   public void run() {
      try {
         AbstractHardwareThread.mReturnOp.intakeDisc1();
         if (Aem.inQuiesceMode()) {
            this.nextScreen = "MainScreen";
         }
      } catch (ServoException var3) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var3.getMessage());
         this.nextScreen = "ErrorScreen 4033,InitializingAEMScreen";
      } catch (DvdplayException var4) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var4.getMessage());
         this.nextScreen = "PushingDiscAllTheWayScreen";
      }

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ReturningMovie.this.action.actionPerformed(new ActionEvent(ReturningMovie.this.component, 1001, ReturningMovie.this.nextScreen));
         }
      });

   }
}
