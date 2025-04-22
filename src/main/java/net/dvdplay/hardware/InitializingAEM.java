package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;

public class InitializingAEM extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;

   public InitializingAEM(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      try {
         if (Aem.needToReinitialize()) {
            ServoFactory.getInstance().logServoStatus();
         }

         AemFactory.getInstance().initializeHardware();
         SwingUtilities.invokeLater(() -> InitializingAEM.action.actionPerformed(new ActionEvent(InitializingAEM.component, 1001, DvdplayBase.MAIN_SCREEN)));

      } catch (DvdplayException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }
   }
}
