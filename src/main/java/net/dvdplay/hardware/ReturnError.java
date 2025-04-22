package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class ReturnError extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   String nextScreen = "ReturnMovieScreen";
   private final int TIME_OUT = 10000;

   public ReturnError(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      try {
         long lNow = 0L;
         long lStart = 0L;
         boolean lDiscJammed = false;
         if (!AbstractHardwareThread.mReturnOp.clearSlot()) {
            lDiscJammed = true;
         }

         if (ServoFactory.getInstance().isDiscInSlot()) {
            AbstractHardwareThread.mReturnOp.recordDisc();
            this.nextScreen = "ReturnThankYouScreen Return";
         } else {
            AbstractHardwareThread.mReturnOp.setTempBadSlot();
         }

         lStart = System.currentTimeMillis();

         while (!AbstractHardwareThread.mRentOp.discTaken() && AEMGui.isCurrentThread(this)) {
            Util.sleep(100);
            lNow = System.currentTimeMillis();
            if (lNow - lStart > 10000L || Aem.inQuiesceMode()) {
               break;
            }
         }

         if (!Aem.inQuiesceMode() && (lNow - lStart <= 10000L || lDiscJammed)) {
            if (lDiscJammed) {
               this.nextScreen = "InitializingAEMScreen";
            }
         } else {
            this.nextScreen = "MainScreen";
         }
      } catch (DvdplayException var6) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var6.getMessage());
      }

      SwingUtilities.invokeLater(() -> ReturnError.action.actionPerformed(new ActionEvent(ReturnError.component, 1001, ReturnError.this.nextScreen)));
   }
}
