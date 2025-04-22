package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.userop.GroupCodeException;

public class IdentifyingDisc extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   String nextScreen;

   public IdentifyingDisc(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      try {
         AbstractHardwareThread.mReturnOp.readBarCode();
         this.nextScreen = "ReturningMovieScreen";
      } catch (GroupCodeException var3) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
         this.nextScreen = "DiscNotBelongScreen";
      } catch (DvdplayException var4) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var4.getMessage());
         this.nextScreen = "UnableToRecognizeMovieScreen";
      }

      SwingUtilities.invokeLater(() -> IdentifyingDisc.action.actionPerformed(new ActionEvent(IdentifyingDisc.component, 1001, IdentifyingDisc.this.nextScreen)));

   }
}
