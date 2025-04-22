package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import net.dvdplay.base.AbstractHardwareThread;

public class ErrorLog extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;

   public ErrorLog() {
   }

   public ErrorLog(ActionListener lAction, JComponent lComponent) {
      action = lAction;
      component = lComponent;
   }

   public static String send(String data) {
      return "Nelson";
   }
}
