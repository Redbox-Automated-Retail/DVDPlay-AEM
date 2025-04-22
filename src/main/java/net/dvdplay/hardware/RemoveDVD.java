package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.ServoException;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.inventory.InventoryException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class RemoveDVD extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   int passingData = 0;
   boolean hasMoreDiscToDispense = false;
   private final int TIME_OUT = 10000;

   public RemoveDVD(ActionListener lAction, JComponent lComponent, String data) {
      component = lComponent;
      action = lAction;
      this.passingData = Integer.parseInt(data);
   }

   public void run() {
      Aem lAem = AemFactory.getInstance();
      if (this.passingData >= AbstractHardwareThread.mRentOp.getDiscCount() - 1) {
         try {
            lAem.goToNextEmptySlot();
         } catch (InventoryException var10) {
            Aem.logDetailMessage(DvdplayLevel.INFO, var10.getMessage());
         } catch (ServoException var11) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var11.getMessage());
            Aem.setNeedToReinitialize();
         }
      }

      long lNow = 0L;
      long lStart = 0L;
      boolean lTimedOut = false;
      lStart = System.currentTimeMillis();

      while (!AbstractHardwareThread.mRentOp.discTaken() && AEMGui.isCurrentThread(this)) {
         Util.sleep(100);
         lNow = System.currentTimeMillis();
         if (lNow - lStart > 10000L) {
            lTimedOut = true;
            break;
         }

         if (Aem.inQuiesceMode()) {
            break;
         }
      }

      boolean error = false;
      String data;
      String nextScreen;
      if (error) {
         data = "ErrorPushing,";
         nextScreen = "ErrorPushingScreen ";
      } else if (this.passingData < AbstractHardwareThread.mRentOp.getDiscCount() - 1 && !lTimedOut && !Aem.inQuiesceMode()) {
         data = "" + (this.passingData + 1);
         nextScreen = "DeliveringDVDScreen";
      } else {
         data = "Rent";
         nextScreen = "ReturnThankYouScreen";
      }

      SwingUtilities.invokeLater(() -> RemoveDVD.action.actionPerformed(new ActionEvent(RemoveDVD.component, 1001, nextScreen + DvdplayBase.SPACE_STRING + data)));

   }
}
