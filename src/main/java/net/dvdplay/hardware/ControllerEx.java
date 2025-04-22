package net.dvdplay.hardware;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemException;
import net.dvdplay.aem.Arm;
import net.dvdplay.aem.Carousel;
import net.dvdplay.aem.Door;
import net.dvdplay.aem.Light;
import net.dvdplay.aem.Roller;
import net.dvdplay.aem.Sensor;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoEx;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.ButtonStatusHash;
import net.dvdplay.util.Util;

public class ControllerEx extends Controller {
   static ActionListener action;
   static JComponent component;
   static boolean updateContinuously = false;
   static JTextField armMacrosStatus;
   static JTextField doorMacrosStatus;
   static Hashtable temp = new Hashtable();
   private static final String mStatus = "";
   protected static ServoEx mServo = null;

   public ControllerEx(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   protected static synchronized ServoEx getServoEx() {
      if (mServo == null) {
         Servo lServo = ServoFactory.getInstance();
         if (lServo == null || !(lServo instanceof ServoEx)) {
            throw new AemException("ControllerEx: Incorrect servo type or null object.");
         }

         mServo = (ServoEx)lServo;
      }

      return mServo;
   }

   protected static Arm getArm() {
      return getServoEx().getArm();
   }

   protected static Door getDoor() {
      return getServoEx().getDoor();
   }

   protected static Roller getRoller() {
      return getServoEx().getRoller();
   }

   protected static Light getLight() {
      return getServoEx().getLight();
   }

   protected static Sensor getCaseSensor1() {
      return getServoEx().getCaseSensor1();
   }

   protected static Sensor getCaseSensor2() {
      return getServoEx().getCaseSensor2();
   }

   protected static Sensor getCaseInSensor() {
      return getServoEx().getCaseInSensor();
   }

   protected static Sensor getInputSensor() {
      return getServoEx().getInputSensor();
   }

   protected static Carousel getCarousel() {
      return getServoEx().getCarousel();
   }

   public static String armMotorOn(String data) {
      try {
         getArm();
         Arm.motorOn();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String armMotorOff(String data) {
      try {
         getArm();
         Arm.motorOff();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String doorMotorOn(String data) {
      try {
         getDoor();
         Door.motorOn();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String doorMotorOff(String data) {
      try {
         getDoor();
         Door.motorOff();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String rollerMotorIn(String data) {
      try {
         getRoller();
         Roller.in();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String rollerMotorOut(String data) {
      try {
         getRoller();
         Roller.out();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String rollerMotorBrakeOn(String data) {
      try {
         getRoller();
         Roller.stop();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String rollerMotorBrakeOff(String data) {
      try {
         getRoller();
         Roller.start();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String allMotorEnable(String data) {
      getServoEx().enableMotors();
      return "";
   }

   public static String allMotorDisable(String data) {
      getServoEx().disableMotors();
      return "";
   }

   public static Hashtable armMacrosClear(String data) {
      temp.clear();

      try {
         Arm lArm = getArm();
         Arm.clear();
         temp.put("armMacrosStatusField", new String(Arm.getPosition()));
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return temp;
   }

   public static Hashtable armMacrosEject(String data) {
      temp.clear();

      try {
         Arm lArm = getArm();
         Arm.eject();
         temp.put("armMacrosStatusField", new String(Arm.getPosition()));
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return temp;
   }

   public static Hashtable armMacrosStatus(String data) {
      temp.clear();
      Hashtable var10000 = temp;
      getArm();
      var10000.put("armMacrosStatusField", new String(Arm.getPosition()));
      return temp;
   }

   public static Hashtable doorMacrosOpen(String data) {
      temp.clear();

      try {
         Door lDoor = getDoor();
         Door.open();
         temp.put("doorMacrosStatusField", new String(Door.getPosition()));
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return temp;
   }

   public static Hashtable doorMacrosClamp(String data) {
      temp.clear();

      try {
         Door lDoor = getDoor();
         Door.clamp();
         temp.put("doorMacrosStatusField", new String(Door.getPosition()));
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return temp;
   }

   public static Hashtable doorMacrosClose(String data) {
      temp.clear();

      try {
         Door lDoor = getDoor();
         Door.close();
         temp.put("doorMacrosStatusField", new String(Door.getPosition()));
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return temp;
   }

   public static Hashtable doorMacrosStatus(String data) {
      temp.clear();
      Hashtable var10000 = temp;
      getDoor();
      var10000.put("doorMacrosStatusField", new String(Door.getPosition()));
      return temp;
   }

   public static String lcdPowerOn(String data) {
      try {
         getServoEx().lcdOn();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String lcdPowerOff(String data) {
      try {
         getServoEx().lcdOff();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String barcodeIlluminationLevelOn(String data) {
      try {
         getLight().setLevel(Integer.parseInt(data));
         getLight().on();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String barcodeIlluminationLevelOff(String data) {
      try {
         getLight().off();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static Hashtable statusBitGet(String data) {
      Hashtable temp = new Hashtable();
      Door lDoor = getDoor();
      if (Door.isLocked()) {
         temp.put("statusBitLockField", "1");
      } else {
         temp.put("statusBitLockField", "0");
      }

      if (Door.getPosition().compareTo("OPEN") == 0) {
         temp.put("statusBitOpenField", "1");
      } else {
         temp.put("statusBitOpenField", "0");
      }

      if (Door.getPosition().compareTo("CLAMP") == 0) {
         temp.put("statusBitOpenClampedField", "1");
      } else {
         temp.put("statusBitOpenClampedField", "0");
      }

      if (Door.getPosition().compareTo("CLOSE") == 0) {
         temp.put("statusBitClosedField", "1");
      } else {
         temp.put("statusBitClosedField", "0");
      }

      if (getCaseSensor1().on()) {
         temp.put("statusBitCase1Field", "1");
      } else {
         temp.put("statusBitCase1Field", "0");
      }

      if (getCaseSensor2().on()) {
         temp.put("statusBitCase2Field", "1");
      } else {
         temp.put("statusBitCase2Field", "0");
      }

      if (getServoEx().isLcdPowerOn()) {
         temp.put("statusBitLCDPowerField", "1");
      } else {
         temp.put("statusBitLCDPowerField", "0");
      }

      getArm();
      if (Arm.getPosition().compareTo("CLEAR") == 0) {
         temp.put("statusBitClearField", "1");
      } else {
         temp.put("statusBitClearField", "0");
      }

      getArm();
      if (Arm.getPosition().compareTo("EJECT") == 0) {
         temp.put("statusBitEjectField", "1");
      } else {
         temp.put("statusBitEjectField", "0");
      }

      if (getCaseInSensor().on()) {
         temp.put("statusBitCaseInField", "1");
      } else {
         temp.put("statusBitCaseInField", "0");
      }

      if (getInputSensor().on()) {
         temp.put("statusBitInputField", "1");
      } else {
         temp.put("statusBitInputField", "0");
      }

      return temp;
   }

   public static ButtonStatusHash enableServoEnable(String data) {
      ButtonStatusHash temp = new ButtonStatusHash();
      temp.put("enableDisableServo", "true");
      temp.put("homeCarouselStart", "true");
      temp.put("moveCarouselPosition", "true");
      temp.put("moveCarouselStartVelocity", "true");
      temp.put("moveCarouselStop", "true");
      temp.put("slotMovementGo", "true");
      return temp;
   }

   public static ButtonStatusHash disableServoEnable(String data) {
      ButtonStatusHash temp = new ButtonStatusHash();
      temp.put("enableDisableServo", "false");
      temp.put("homeCarouselStart", "false");
      temp.put("moveCarouselPosition", "false");
      temp.put("moveCarouselStartVelocity", "false");
      temp.put("moveCarouselStop", "false");
      temp.put("slotMovementGo", "false");
      return temp;
   }

   public static String enableHardStop(String data) {
      try {
         getCarousel();
         Carousel.stop();
      } catch (AemException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }

      return "";
   }

   public static String homeCarouselStart(String data) {
      JPanel resultPanel = (JPanel)component.getComponentAt(10, 430);
      JPanel statusLight = (JPanel)resultPanel.getComponent(0);
      JLabel statusField = (JLabel)statusLight.getComponent(0);
      JButton startButton = (JButton)resultPanel.getComponent(1);
      new Thread() {
         @Override
         public void run() {
            statusLight.setBackground(Color.RED);
            statusField.setText("Homing in Progress");
            startButton.setEnabled(false);
            ControllerEx.disableAllButton(ControllerEx.component, "ControllerEx enableHardStop");
            try {
               ControllerEx.getServoEx().initialize();
            } catch (DvdplayException e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
            }
            statusLight.setBackground(Color.GREEN);
            statusField.setText("Homing Off");
            startButton.setEnabled(true);
            ControllerEx.enableAllButton(ControllerEx.component, "ControllerEx enableHardStop");
         }
      }.start();

      return null;
   }

   public static String slotMovementGo(String data) {
      JPanel resultPanel = (JPanel)component.getComponentAt(340, 260);
      JPanel bottomPanel = (JPanel)resultPanel.getComponent(1);
      JPanel smallPanel = (JPanel)bottomPanel.getComponent(0);
      JTextField valueField = (JTextField)smallPanel.getComponent(1);
      JTextField slotField = (JTextField)smallPanel.getComponent(3);
      JTextField positionField = (JTextField)smallPanel.getComponent(5);
      new Thread() {
         @Override
         public void run() {
            try {
               ServoEx lServoEx = ControllerEx.getServoEx();
               valueField.setText(String.valueOf(lServoEx.calculateBayNum(Integer.parseInt(data))));
               slotField.setText(String.valueOf(lServoEx.calculateBaySlot(Integer.parseInt(data))));
               positionField.setText(String.valueOf(lServoEx.getSlotOffset(Integer.parseInt(data))));
               ControllerEx.disableAllButton(ControllerEx.component, "ControllerEx enableHardStop");
               lServoEx.goToSlot(Integer.parseInt(data));
            } catch (AemException e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
            }
            ControllerEx.enableAllButton(ControllerEx.component, "ControllerEx enableHardStop");
         }
      }.start();

      return null;
   }

   public static Hashtable<String, String> servoMotorGetPosition(String data) {
      Hashtable<String, String> temp = new Hashtable<>();
      getCarousel();
      temp.put("servoMotorStatusPositionField", Carousel.getPosition());
      return temp;
   }

   public static Hashtable<String, String> servoMotorClear(String data) {
      Hashtable<String, String> temp = new Hashtable<>();
      temp.put("servoMotorStatusPositionField", "");
      return temp;
   }

   public static String updateContinuously(String data) {
      JPanel resultPanel = (JPanel)component.getComponentAt(340, 400);
      JPanel leftPanel = (JPanel)resultPanel.getComponent(0);
      JTextField position = (JTextField)leftPanel.getComponent(1);
      updateContinuously = data.equals("true");
      new Thread() {
         @Override
         public void run() {
            if (ControllerEx.updateContinuously) {
               while (ControllerEx.updateContinuously) {
                  try {
                     Util.sleep(100);
                     ControllerEx.getCarousel();
                     position.setText(Carousel.getPosition());
                  } catch (DvdplayException e) {
                     Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
                  }
               }
            }
         }
      }.start();

      return null;
   }

   public static String moveCarouselMove(String data) {
      new Thread() {
         private final String val$sdata;

         {
            this.val$sdata = data;
         }

         @Override
         public void run() {
            StringTokenizer lStrTok = new StringTokenizer(this.val$sdata, DvdplayBase.COMMA);
            try {
               ControllerEx.getCarousel();
               Carousel.loadTraj(Integer.parseInt(lStrTok.nextToken()), Integer.parseInt(lStrTok.nextToken()), Integer.parseInt(lStrTok.nextToken()));
            } catch (AemException e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
            }
            try {
               Thread.sleep(3000L);
            } catch (InterruptedException e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, "[BarCamPegTest] " + e);
            }
         }
      }.start();

      return "" + data;
   }

   public static String moveCarouselVelocity(String data) {
      new Thread() {
         private final String val$sdata;

         {
            this.val$sdata = data;
         }

         @Override
         public void run() {
            StringTokenizer lStrTok = new StringTokenizer(this.val$sdata, DvdplayBase.COMMA);
            try {
               ControllerEx.getCarousel();
               Carousel.moveCarouselVelocityMode(Integer.parseInt(lStrTok.nextToken()), Integer.parseInt(lStrTok.nextToken()));
            } catch (AemException e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
            }
            try {
               Thread.sleep(3000L);
            } catch (InterruptedException e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, "[ControllerEx] " + e);
            }
         }
      }.start();

      return data;
   }

   public static String moveCarouselStop(String data) {
       return enableHardStop("");
   }

   public String getArmMacrosSatus() {
      return "None";
   }

   public String getDoorMacrosSatus() {
      return "None";
   }

   public String getBarcodeIlluminationLevel() {
      return "255";
   }

   public String getStatusBitLock() {
      return "0";
   }

   public String getStatusBitOpen() {
      return "0";
   }

   public String getStatusBitClamped() {
      return "0";
   }

   public String getStatusBitClosed() {
      return "0";
   }

   public String getStatusBitCase1() {
      return "0";
   }

   public String getStatusBitCase2() {
      return "0";
   }

   public String getStatusBitLCDPower() {
      return "0";
   }

   public String getStatusBitClear() {
      return "0";
   }

   public String getStatusBitEject() {
      return "0";
   }

   public String getStatusBitCaseIn() {
      return "0";
   }

   public String getStatusBitInput() {
      return "0";
   }

   public String getHomingCarouselStatus() {
      return "Off";
   }

   public String getSlotMovementOffset() {
      return Integer.toString(getServoEx().getServoOffset());
   }

   public String getSlotMovementNo() {
      return "1";
   }

   public String getSlotMovementBay() {
      return "";
   }

   public String getSlotMovementSlot() {
      return "";
   }

   public String getSlotMovementPosition() {
      return "";
   }

   public String getServoMotorStatusPosition() {
      return "";
   }

   public String getMoveCarouselPositon() {
      return "0";
   }

   public String getMoveCarouselVelocity() {
      return Integer.toString(getServoEx().getServoVelocity());
   }

   public String getMoveCarouselAcceleration() {
      return Integer.toString(getServoEx().getServoAcceleration());
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
