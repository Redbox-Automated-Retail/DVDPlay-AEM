package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class Main extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   private volatile boolean stop = false;

   public Main(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      AbstractHardwareThread.mReturnOp.createQueueJob();
      AbstractHardwareThread.mRentOp.clearDiscItems();
      AbstractHardwareThread.mReturnOp.clearDiscItems();
      Aem lAem = AemFactory.getInstance();
      Aem.resetDiscActive();

      try {
         for (; AEMGui.isCurrentThread(this); Util.sleep(60000)) {
            if (Aem.inQuiesceMode()) {
               lAem.exitApp(Aem.getQuiesceMode());
            }

            Aem.runQueueJobs();
            if (Aem.needToRestartcompter() && (!Aem.isQueueBusy() || !Aem.getServerConnectionStatus())) {
               Aem.logSummaryMessage("Performing scheduled reboot.");
               lAem.exitApp(2);
            }

            if (Aem.getQueueJobCount() == 0 && Aem.isGetDataCmd()) {
               lAem.executeGetDataCmd();
            }

            if (Aem.needToUpdateMarkedForRemoval()) {
               System.gc();
               ServoFactory.getInstance().logTemperatures();
               action.actionPerformed(new ActionEvent(component, 1001, "UpdatingScreen"));
            }
         }

         Aem.logDetailMessage(DvdplayLevel.INFO, "End mainscreen thread");
      } catch (DvdplayException var3) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
      }
   }
}
