package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.logger.DvdplayLevel;

public class Updating extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   private volatile boolean stop = false;

   public Updating(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      String lNextScreen = "MainScreen";

      try {
         Aem lAem = AemFactory.getInstance();
         lAem.updateDiscDetailMarkedForRemovalData();
         Aem.logDetailMessage(DvdplayLevel.INFO, "End updatingscreen thread");
         if (Aem.isHardwareError()) {
            lNextScreen = "InitializingAEMScreen";
         }
      } catch (Exception var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var7.getMessage(), var7);
      } finally {
         action.actionPerformed(new ActionEvent(component, 1001, lNextScreen));
      }
   }
}
