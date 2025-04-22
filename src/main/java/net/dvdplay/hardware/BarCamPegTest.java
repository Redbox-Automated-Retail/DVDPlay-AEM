package net.dvdplay.hardware;

import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.BarCode;
import net.dvdplay.aem.BarCodeReader;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.BarCodeHash;
import net.dvdplay.util.Util;

public class BarCamPegTest extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   static BarCode mBarCode = new BarCode();

   public BarCamPegTest() {
   }

   public BarCamPegTest(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public static String videoFormat(String data) {
      BarCodeReader.showVideoFormatDlg();
      return "dah";
   }

   public static String videoSource(String data) {
      BarCodeReader.showVideoSourceDlg();
      return "dah";
   }

   public static String videoDisplay(String data) {
      BarCodeReader.showVideoDisplayDlg();
      return "dah";
   }

   public static String videoCompression(String data) {
      BarCodeReader.showVideoCompressionDlg();
      return "dah";
   }

   public static String getAngles() {
      return "0,30,60,90,120,150,180,210,240,270,300,330";
   }

   public static BarCodeHash commandsAcquire(String data) {
      BarCodeHash bch = new BarCodeHash();
      JPanel picturePanel = (JPanel)component.getComponentAt(200, 10);
      JLabel pictureLabel = (JLabel)picturePanel.getComponent(0);

      try {
         JPanel commandsPanel = (JPanel)component.getComponentAt(10, 10);
         JButton acquireButton = (JButton)commandsPanel.getComponent(4);
         acquireButton.setEnabled(false);
         String outputFile = "c:\\aem\\tmp\\" + Util.getTimeStamp() + "BarCamPegTest.jpg";
         mBarCode = new BarCode();
         Thread def = new Thread(() -> {
            BarCodeReader.readBarCode(BarCamPegTest.mBarCode, BarCamPegTest.getAngles(), true, outputFile);
            acquireButton.setEnabled(true);
         });

         def.start();

         while (!acquireButton.isEnabled()) {
            Util.sleep(100);
         }

         if (mBarCode.mCode1 != null && mBarCode.mCode2 != null) {
            bch.put("code1", mBarCode.mCode1);
            bch.put("code2", mBarCode.mCode2);
            Image tempImage = getPicture(outputFile).getImage();
            Image newImage = tempImage.getScaledInstance(570, 380, 0);
            pictureLabel.setIcon(new ImageIcon(newImage));
         } else {
            bch.put("code1", "");
            bch.put("code2", "");
            Image tempImage = getPicture(outputFile).getImage();
            Image newImage = tempImage.getScaledInstance(570, 380, 0);
            pictureLabel.setIcon(new ImageIcon(newImage));
         }
      } catch (Exception var10) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Error " + var10.toString(), var10);
      }

      return bch;
   }

   public static String barcodeIlluminationLevelOn(String data) {
      Servo lServo = ServoFactory.getInstance();
      lServo.mLight.setLevel(Integer.parseInt(data));
      lServo.mLight.on();
      return "Dah";
   }

   public static String barcodeIlluminationLevelOff(String data) {
      Servo lServo = ServoFactory.getInstance();
      lServo.mLight.off();
      return "Dah";
   }

   public static String save(String data) {
      return "dah " + data;
   }

   public static boolean getSaveImages() {
      return false;
   }

   public static boolean getStopOnSuccess() {
      return true;
   }

   public static int getBarcodeIlluminationLevel() {
      return 255;
   }

   public static String[] getDevice() {
      return new String[]{"VICAM Digital Camera", "Dummy Camera"};
   }

   public static ImageIcon getPicture(String aImageFile) {
      return new ImageIcon(aImageFile);
   }
}
