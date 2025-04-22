package net.dvdplay.base;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.userop.RentOp;
import net.dvdplay.userop.ReturnOp;

public abstract class AbstractHardwareThread extends Thread {
   static ActionListener action;
   static JComponent component;
   public static Aem mAem = null;
   public static RentOp mRentOp = null;
   public static ReturnOp mReturnOp = null;

   public static void init() {
      if (mAem == null) {
         mAem = AemFactory.getInstance();
      }

      if (mRentOp == null) {
         mRentOp = new RentOp();
      }

      if (mReturnOp == null) {
         mReturnOp = new ReturnOp();
      }
   }

   protected static void disableAllButton(JComponent component, String skip) {
      Component[] all = component.getComponents();

      for (int i = 0; i < all.length; i++) {
         if (all[i] instanceof JButton) {
            JButton dd = (JButton)all[i];
            if (dd.getActionCommand() != skip) {
               dd.setEnabled(false);
            }
         }

         if (all[i] instanceof JPanel) {
            disableAllButton((JComponent)all[i], skip);
         }
      }
   }

   protected static void enableAllButton(JComponent component, String skip) {
      Component[] all = component.getComponents();

      for (int i = 0; i < all.length; i++) {
         if (all[i] instanceof JButton) {
            JButton dd = (JButton)all[i];
            if (dd.getActionCommand() != skip) {
               dd.setEnabled(true);
            }
         }

         if (all[i] instanceof JPanel) {
            enableAllButton((JComponent)all[i], skip);
         }
      }
   }
}
