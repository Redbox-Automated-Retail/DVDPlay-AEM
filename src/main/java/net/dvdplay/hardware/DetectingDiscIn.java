package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.util.Util;

public class DetectingDiscIn extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   String nextScreen = "IdentifyingMovieScreen";

   public DetectingDiscIn(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public void run() {
      while (AEMGui.isCurrentThread(this) && !AbstractHardwareThread.mReturnOp.discAtDoor()) {
         Util.sleep(100);
      }

      if (AEMGui.isCurrentThread(this)) {
         SwingUtilities.invokeLater(() -> DetectingDiscIn.action.actionPerformed(new ActionEvent(DetectingDiscIn.component, 1001, DetectingDiscIn.this.nextScreen)));

      }
   }
}
