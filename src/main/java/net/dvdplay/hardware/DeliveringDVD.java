package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.userop.NoDiscInSlotException;

public class DeliveringDVD extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   int discId;
   int passingData = 0;

   public DeliveringDVD(ActionListener lAction, JComponent lComponent, String data) {
      component = lComponent;
      action = lAction;
      if (!data.isEmpty()) {
         this.passingData = Integer.parseInt(data);
      }
   }

   public void run() {
      boolean error = false;

      try {
         AbstractHardwareThread.mRentOp.dispenseDisc(this.passingData);
      } catch (NoDiscInSlotException var4) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var4.getMessage());
         error = true;
      } catch (DvdplayException var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var5.getMessage());
         Aem.setNeedToReinitialize();
         error = !AbstractHardwareThread.mRentOp.isDiscDispensed();
      }

      String data;
      String nextScreen;
      if (error) {
         data = "4034,DeliveringDVDScreen," + this.passingData;
         nextScreen = "ErrorScreen";
      } else {
         data = "" + this.passingData;
         nextScreen = "RemoveDVDScreen";
      }

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeliveringDVD.action.actionPerformed(new ActionEvent(DeliveringDVD.component, 1001, nextScreen + DvdplayBase.SPACE_STRING + data));
         }
      });

   }
}
