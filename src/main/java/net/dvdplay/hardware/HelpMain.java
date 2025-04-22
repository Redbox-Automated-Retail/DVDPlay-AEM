package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.util.Util;

public class HelpMain extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   JLabel hardwareError;
   JLabel queueBusy;
   JLabel serverConnection;
   ImageIcon redLight;
   ImageIcon whiteLight;
   ImageIcon greenLight;
   ImageIcon yellowLight;
   ImageIcon blueLight;
   JPanel statusPanel;

   public HelpMain(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
      this.redLight = ScreenProperties.getImage("Status.Red");
      this.whiteLight = ScreenProperties.getImage("Status.White");
      this.greenLight = ScreenProperties.getImage("Status.Green");
      this.yellowLight = ScreenProperties.getImage("Status.Yellow");
      this.blueLight = ScreenProperties.getImage("Status.Blue");
      this.statusPanel = (JPanel)lComponent.getComponentAt(850, 70);
      this.hardwareError = (JLabel)this.statusPanel.getComponentAt(0, 0);
      this.queueBusy = (JLabel)this.statusPanel.getComponentAt(100, 0);
      this.serverConnection = (JLabel)this.statusPanel.getComponentAt(50, 0);
   }

   public void run() {
      try {
         while (true) {
            if (AEMGui.isCurrentThread(this)) {
               if (Aem.isHardwareError()) {
                  this.hardwareError.setIcon(this.redLight);
               } else {
                  this.hardwareError.setIcon(this.greenLight);
               }

               if (Aem.getSavingStatus()) {
                  this.queueBusy.setIcon(this.blueLight);
               } else if (Aem.isQueueBusy()) {
                  this.queueBusy.setIcon(this.yellowLight);
               } else {
                  this.queueBusy.setIcon(this.greenLight);
               }

               if (Aem.getServerConnectionStatus()) {
                  this.serverConnection.setIcon(this.greenLight);
               } else {
                  this.serverConnection.setIcon(this.whiteLight);
               }

               Util.sleep(1000);
            } else {
               Aem.logDetailMessage(DvdplayLevel.INFO, "End helpmain thread");
               break;
            }
         }
      } catch (DvdplayException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage(), var2);
      }
   }
}
