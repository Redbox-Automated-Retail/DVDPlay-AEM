package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.PopupYesNo;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.util.Util;

public class Operator extends AbstractHardwareThread {
   static ActionListener al;
   static JComponent component;

   public Operator(ActionListener lal, JComponent lcomponent) {
      al = lal;
      component = lcomponent;
   }

   public static String save(String data) {
      StringTokenizer lStrTok = new StringTokenizer(data, ",");
      Aem.setAemId(Integer.parseInt(lStrTok.nextToken()));
      Aem.setServerAddress(lStrTok.nextToken());
      Aem.setFTPAddress(lStrTok.nextToken());
      Aem.updateAemProperties();
      DOMData.save();
      return "Dah";
   }

   public static String testServerConnection(String data) {
      JPanel operatorPanel = (JPanel)component.getComponentAt(500, 350);
      JScrollPane jsp = (JScrollPane)operatorPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      StringTokenizer stk = new StringTokenizer(data, ",");
      String server = stk.nextToken();
      String ftp = stk.nextToken();

      try {
         Comm.ping(server);
         textarea.setText(textarea.getText() + "\nServer Connection to " + server + " is Success");
      } catch (Exception var10) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "[Operator] " + var10.toString());
         textarea.setText(textarea.getText() + "\nServer Connection to " + server + " is Failure");
      }

      try {
         Comm.testFTP(ftp);
         textarea.setText(textarea.getText() + "\nFTP Connection to " + ftp + " is Success");
      } catch (Exception var9) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "[Operator] " + var9.toString());
         textarea.setText(textarea.getText() + "\nFTP Connection to " + ftp + " is Failure");
      }

      textarea.setText(textarea.getText() + "\n ===================");
      return "";
   }

   public static PopupYesNo reload(String data) {
      PopupYesNo pyn = new PopupYesNo();

      try {
         pyn.setMessage("Reloading Data from server. Ok?");
      } catch (Exception var3) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "[Operator] " + var3.toString());
      }

      return pyn;
   }

   public static void startIndicator(JLabel hardwareError, JLabel queueBusy, JLabel serverConnection) {
      ImageIcon redLight = ScreenProperties.getImage("Status.Red");
      ImageIcon whiteLight = ScreenProperties.getImage("Status.White");
      ImageIcon greenLight = ScreenProperties.getImage("Status.Green");
      ImageIcon yellowLight = ScreenProperties.getImage("Status.Yellow");
      ImageIcon blueLight = ScreenProperties.getImage("Status.Blue");
      Thread worker = new Thread() {
         @Override
         public void run() {
            while (Aem.isToolsRunning()) {
               try {
                  if (Aem.isHardwareError()) {
                     hardwareError.setIcon(redLight);
                  } else {
                     hardwareError.setIcon(greenLight);
                  }
                  if (Aem.getSavingStatus()) {
                     queueBusy.setIcon(blueLight);
                  } else if (Aem.isQueueBusy()) {
                     queueBusy.setIcon(yellowLight);
                  } else {
                     queueBusy.setIcon(greenLight);
                  }
                  if (Aem.getServerConnectionStatus()) {
                     serverConnection.setIcon(greenLight);
                  } else {
                     serverConnection.setIcon(whiteLight);
                  }
                  Util.sleep(3000);
               } catch (DvdplayException e) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage(), e);
               }
            }
            Aem.logDetailMessage(DvdplayLevel.INFO, "Status Light Indicator's Thread Finished ...");
         }
      };

      worker.start();
   }

   public static String getAemID() {
      return Integer.toString(Aem.getAemId());
   }

   public static String getVersionNo() {
      return Aem.getVersionNum();
   }

   public static String getServerAddress() {
      return Aem.getServerAddress();
   }

   public static String getFtpAddress() {
      return Aem.getFTPAddress();
   }

   public static String getDayTimeOfDay() {
      return Aem.getDueTimeAsString();
   }

   public static String getShutDownTime() {
      return Aem.getShutdownTime();
   }

   public static String getContactName() {
      return Aem.getString(2012);
   }

   public static String getAddress() {
      return Aem.getString(2013);
   }

   public static String getAddress2() {
      return Aem.getString(2014);
   }

   public static String getCityStateZip() {
      return Aem.getString(2015);
   }

   public static String getEmail() {
      return Aem.getString(2017);
   }

   public static String getHelpPhone() {
      return Aem.getString(2016);
   }

   public static String getTaxRate() {
      return Aem.getTaxRate().setScale(4, 4).toString();
   }

   public static String getNoChargeTimeMins() {
      return "0";
   }
}
