package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class UnableToRecognizeMovie extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   String nextScreen;
   private final int TIME_OUT = 10000;

   public UnableToRecognizeMovie(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      try {
         long lNow = 0L;
         long lStart = 0L;
         lStart = System.currentTimeMillis();

         while (!AbstractHardwareThread.mRentOp.discTaken() && AEMGui.isCurrentThread(this)) {
            Util.sleep(100);
            lNow = System.currentTimeMillis();
            if (lNow - lStart > 10000L || Aem.inQuiesceMode()) {
               break;
            }
         }

         if (!Aem.inQuiesceMode() && lNow - lStart <= 10000L) {
            this.nextScreen = "ReturnMovieScreen";
         } else {
            this.nextScreen = "MainScreen";
         }
      } catch (DvdplayException var5) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var5.getMessage());
      }

      if (AEMGui.isCurrentThread(this)) {
         SwingUtilities.invokeLater(() -> UnableToRecognizeMovie.action.actionPerformed(new ActionEvent(UnableToRecognizeMovie.component, 1001, UnableToRecognizeMovie.this.nextScreen)));

      }
   }
}
