package net.dvdplay.hardware;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemException;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.Arm;
import net.dvdplay.aem.Carousel;
import net.dvdplay.aem.CarouselException;
import net.dvdplay.aem.Door;
import net.dvdplay.aem.Light;
import net.dvdplay.aem.NMC;
import net.dvdplay.aem.Roller;
import net.dvdplay.aem.Sensor;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoEx;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.SlotCalTableModel;
import net.dvdplay.util.Util;
import net.dvdplay.view.Utility;

public class SlotCalEx extends SlotCal {
   static ActionListener action;
   static ActionListener removeDiscAction;
   static JComponent component;
   static boolean beenInit = false;
   public static boolean beenCalibrateFull = false;
   static final int SLOT_WIDTH = 164;
   static final int SLOTRIB_WIDTH = 270;
   static final int TOOL_ABSENT = 5432123;
   static final int ERR_HOMING_FAILURE = 2000;
   static final int ERR_NO_SLOT_SELECTED = 2001;
   static final int ERR_UNSAFE_TO_ROTATE = 2002;
   static final int ERR_SLOT_OCCUPIED = 2003;
   static final int ERR_TOOL_INSERT_TIMEOUT = 2004;
   static final int ERR_TOOL_REMOVE_TIMEOUT = 2005;
   static final int ERR_NO_TOOL = 2006;
   static int toolOffsetValue = 0;
   static String calCount;

   public SlotCalEx() {
   }

   public SlotCalEx(ActionListener lAction, JComponent lComponent) {
      action = lAction;
      component = lComponent;
   }

   protected static ServoEx getServoEx() {
      Servo lServo = ServoFactory.getInstance();
      if (lServo != null && lServo instanceof ServoEx) {
         return (ServoEx)lServo;
      } else {
         throw new AemException("SlotCalEx: Incorrect Servo type.");
      }
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

   public static String getPWMValue() {
      return "50";
   }

   public static String getToolOffset() {
      return "0";
   }

   public static String getSlot1Adjustment() {
      return "0";
   }

   public static void reset(String data) {
      JPanel statusPanel = (JPanel)component.getComponentAt(550, 180);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      jsp.setAutoscrolls(true);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea jt = (JTextArea)jvp.getComponent(0);
      jt.setAutoscrolls(true);
      new Thread() {
         @Override
         public void run() {
            StringTokenizer stk = new StringTokenizer(data, DvdplayBase.COMMA);
            int lNumTokens = stk.countTokens();
            Servo lServo = ServoFactory.getInstance();
            if (lNumTokens != lServo.getNumSlots()) {
               return;
            }
            for (int i = 0; i < lNumTokens; i++) {
               lServo.setSlotOffset(i + 1, Integer.parseInt(stk.nextToken()));
            }
            DOMData.save();
            SlotCalEx.beenCalibrateFull = false;
            Utility.statAnnouncer(jt, "- Reset all values to default!");
         }
      }.start();

   }

   public static void refresh(SlotCalTableModel sctm) {
      JPanel statusPanel = (JPanel)component.getComponentAt(550, 180);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      jsp.setAutoscrolls(true);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea jt = (JTextArea)jvp.getComponent(0);
      jt.setAutoscrolls(true);
      new Thread() {
         @Override
         public void run() {
            SlotCalEx.calculateSlotTable(sctm);
            Utility.statAnnouncer(jt, "- Refresh calculated slot positions!");
         }
      }.start();

   }

   private static JTextArea getMessageTextArea() {
      JPanel statusPanel = (JPanel)component.getComponentAt(550, 180);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      jsp.setAutoscrolls(true);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea jt = (JTextArea)jvp.getComponent(0);
      jt.setAutoscrolls(true);
      return jt;
   }

   public static void save(String data, boolean toDisk) {
      JTextArea jt = getMessageTextArea();
      new Thread() {
         @Override
         public void run() {
            StringTokenizer stk = new StringTokenizer(data, DvdplayBase.COMMA);
            int lNumTokens = stk.countTokens();
            Servo lServo = ServoFactory.getInstance();
            if (lNumTokens != lServo.getNumSlots()) {
               return;
            }
            for (int i = 0; i < lNumTokens; i++) {
               lServo.setSlotOffset(i + 1, Integer.parseInt(stk.nextToken()));
            }
            if (toDisk) {
               try {
                  DOMData.save();
                  if (SlotCalEx.beenCalibrateFull) {
                     Utility.statAnnouncer(jt, "- Results of calibration are saved!");
                  } else {
                     Utility.statAnnouncer(jt, "- Saved new values in slot table!");
                  }
                  return;
               } catch (Exception e) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, e.getMessage(), e);
                  Utility.statAnnouncer(jt, "- Save to disk failed.");
                  return;
               }
            }
            try {
               DOMData.save();
               Aem.uploadSlotData();
               Utility.statAnnouncer(jt, "- Saved to server.");
            } catch (Exception e2) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, e2.getMessage(), e2);
               Utility.statAnnouncer(jt, "- Save to server failed.");
            }
         }
      }.start();

   }

   public static String saveToServer(String data) {
      save(data, false);
      return null;
   }

   public static String saveToDisk(String data) {
      save(data, true);
      return null;
   }

   public static Vector<Object> getSlots() {
      Servo lServo = ServoFactory.getInstance();
      int ind = lServo.getNumSlots();
      Vector<Object> slots = new Vector<>();

      for (int i = 0; i < ind; i++) {
         Object[] def = new Object[]{
            false,
            i + 1,
            Integer.toString(lServo.calculateOffset(i + 1)),
            Integer.toString(lServo.getSlotOffset(i + 1)),
                 "---"
         };
         slots.add(i, def);
      }

      return slots;
   }

   public static String adjust(SlotCalTableModel sctm, String slot1Adjustment) {
      JPanel statusPanel = (JPanel)component.getComponentAt(550, 180);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      jsp.setAutoscrolls(true);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea jt = (JTextArea)jvp.getComponent(0);
      jt.setAutoscrolls(true);
      if (beenCalibrateFull) {
         new Thread() {
            @Override
            public void run() {
               int iAdj = Integer.parseInt(slot1Adjustment);
               Servo lServo = ServoFactory.getInstance();
               Utility.statAnnouncer(jt, "- Apply adjustment value = " + slot1Adjustment);
               for (int i = 0; i < lServo.getNumSlots(); i++) {
                  int iOrg = Integer.parseInt(String.valueOf(sctm.getValueAt(i, 2))) + iAdj;
                  sctm.setValueAt(String.valueOf(iOrg), i, 2);
                  int iOrg2 = Integer.parseInt(String.valueOf(sctm.getValueAt(i, 4))) + iAdj;
                  sctm.setValueAt(String.valueOf(iOrg2), i, 4);
               }
               sctm.fireTableDataChanged();
            }
         }.start();

      }

      return "";
   }

   public static String toSlot(SlotCalTableModel sctm, int slotNum) {
      getServoEx().getCarousel();
      Carousel.goToOffset(Integer.parseInt(String.valueOf(sctm.getValueAt(slotNum - 1, 3))));
      return "";
   }

   public static String goToSlot(SlotCalTableModel sctm, String slotNum) {
      JPanel statusPanel = (JPanel)component.getComponentAt(550, 180);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      jsp.setAutoscrolls(true);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea jt = (JTextArea)jvp.getComponent(0);
      jt.setAutoscrolls(true);
      if (Integer.parseInt(slotNum) > 0 && Integer.parseInt(slotNum) < 103) {
         new Thread() {
            @Override
            public void run() {
               SlotCalEx.allButtonNot();
               if (!SlotCalEx.beenInit) {
                  Utility.statAnnouncer(jt, "- Homing carousel! (Do not insert tool at this time!)");
                  boolean initializeNMC = SlotCalEx.initializeNMC();
                  SlotCalEx.beenInit = initializeNMC;
                  if (initializeNMC) {
                     Utility.statAnnouncer(jt, "- Initialization Done!");
                  } else {
                     SlotCalEx.AbortAbort(sctm, jt, 2000, false);
                     return;
                  }
               }
               Utility.statAnnouncer(jt, "- Moving to slot " + slotNum + "...");
               SlotCalEx.toSlot(sctm, Integer.parseInt(slotNum));
               Utility.statAnnouncer(jt, "- Moved to slot " + slotNum + " OK");
               SlotCalEx.allButtonOk();
            }
         }.start();
      } else {
         new Thread() {
            @Override
            public void run() {
               Utility.statAnnouncer(jt, "- Invalid slot number entered !");
            }
         }.start();
      }

      return "";
   }

   public static String calibrate(SlotCalTableModel sctm, String pwmValue, String toolOffset, String CalibrationPerBay) {
      JPanel statusPanel = (JPanel)component.getComponentAt(550, 180);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      jsp.setAutoscrolls(true);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea jt = (JTextArea)jvp.getComponent(0);
      jt.setAutoscrolls(true);
      int iPwm = Integer.parseInt(pwmValue);
      if (iPwm > 44 && iPwm < 76) {
         new Thread() {
            @Override
            public void run() {
               SlotCalEx.allButtonNot();
               if (!SlotCalEx.beenInit) {
                  Utility.statAnnouncer(jt, "- Homing carousel! (Do not insert tool at this point!)");
                  boolean initializeNMC = SlotCalEx.initializeNMC();
                  SlotCalEx.beenInit = initializeNMC;
                  if (initializeNMC) {
                     Utility.statAnnouncer(jt, "- Homing carousel done!");
                  } else {
                     SlotCalEx.AbortAbort(sctm, jt, 2000, false);
                     return;
                  }
               }
               if (!SlotCalEx.checkOkToRotate()) {
                  SlotCalEx.AbortAbort(sctm, jt, 2002, false);
                  return;
               }
               if (SlotCalEx.beenCalibrateFull) {
                  SlotCalEx.calibrateSelect(sctm, Integer.parseInt(pwmValue), jt);
                  sctm.unSelectAll();
                  SlotCalEx.allButtonOk();
               } else if (SlotCalEx.calibrateFull(sctm, Integer.parseInt(pwmValue), Integer.parseInt(toolOffset), Integer.parseInt(CalibrationPerBay), jt)) {
                  SlotCalEx.beenCalibrateFull = true;
                  SlotCalEx.allButtonOk();
               }
            }
         }.start();
      } else {
         new Thread(() -> Utility.statAnnouncer(jt, "- Valid PWM values are within 45 and 75 range!!")).start();
      }

      return "";
   }

   public static boolean isSlotEmpty() {
      return !getServoEx().getCaseInSensor().on();
   }

   public static boolean checkSlotEmpty() {
      if (!isSlotEmpty()) {
         getServoEx().getDoor();
         Door.close();
         return false;
      } else {
         return true;
      }
   }

   public static boolean isToolInserted() {
      boolean bInputSensor = getServoEx().getInputSensor().on();
      boolean bCaseSensor1 = getServoEx().getCaseSensor1().on();
      boolean bCaseSensor2 = getServoEx().getCaseSensor2().on();
      return bInputSensor && bCaseSensor1 && !bCaseSensor2;
   }

   public static boolean checkToolInserted(JTextArea jt) {
      int iTimer = 21;

      while (--iTimer > -1) {
         jt.setText(jt.getText().substring(0, jt.getDocument().getLength() - (iTimer < 9 ? 11 : 12)) + String.valueOf(iTimer) + " seconds!\n");
         jt.setCaretPosition(jt.getDocument().getLength());
         jt.setCaretPosition(jt.getDocument().getLength());
         if (iTimer < 10) {
            jt.setForeground(Color.red);
            jt.setBackground(Color.gray);
            jt.setCaretPosition(jt.getDocument().getLength());
            Util.sleep(490);
            jt.setForeground(Color.black);
            jt.setBackground(Color.white);
            jt.setCaretPosition(jt.getDocument().getLength());
            Util.sleep(490);
         } else {
            Util.sleep(1000);
         }

         if (isToolInserted()) {
            break;
         }
      }

      return iTimer > -1;
   }

   public static boolean isToolRemoved() {
      boolean bCaseSensor1 = getServoEx().getCaseSensor1().on();
      boolean bCaseSensor2 = getServoEx().getCaseSensor2().on();
      if (!bCaseSensor1 && !bCaseSensor2) {
         getServoEx().getDoor();
         Door.close();
         Util.sleep(500);
      }

      getServoEx().getDoor();
      return Door.isLocked();
   }

   public static boolean checkToolRemoved(JTextArea jt) {
      int iTimer = 21;

      while (--iTimer > -1) {
         jt.setText(jt.getText().substring(0, jt.getDocument().getLength() - (iTimer < 9 ? 11 : 12)) + String.valueOf(iTimer) + " seconds!\n");
         jt.setCaretPosition(jt.getDocument().getLength());
         jt.setCaretPosition(jt.getDocument().getLength());
         if (iTimer < 10) {
            jt.setForeground(Color.red);
            jt.setBackground(Color.gray);
            jt.setCaretPosition(jt.getDocument().getLength());
            Util.sleep(490);
            jt.setForeground(Color.black);
            jt.setBackground(Color.white);
            jt.setCaretPosition(jt.getDocument().getLength());
            Util.sleep(490);
         } else {
            Util.sleep(1000);
         }

         if (isToolRemoved()) {
            break;
         }
      }

      return iTimer > -1;
   }

   public static boolean checkOkToRotate() throws CarouselException {
      boolean cresult = true;
      int iCounter = 0;

      do {
         getServoEx().getArm();
         String var10000 = Arm.getPosition();
         getServoEx().getArm();
         if (var10000 != "CLEAR") {
            try {
               getServoEx().getArm();
               Arm.clear();
            } catch (DvdplayException var4) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, var4.getMessage());
            }

            Util.sleep(1000);
            if (iCounter == 2) {
               return !cresult;
            }
         } else {
            cresult = false;
         }
      } while (cresult && iCounter++ < 3);

      cresult = true;
      iCounter = 0;

      do {
         getServoEx().getDoor();
         if (!Door.isLocked()) {
            try {
               getServoEx().getDoor();
               Door.close();
            } catch (DvdplayException var3) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
            }

            Util.sleep(1000);
            if (iCounter == 2) {
               return !cresult;
            }
         } else {
            cresult = false;
         }
      } while (cresult && iCounter++ < 3);

      return !cresult;
   }

   public static void stopCarousel() throws CarouselException {
      if (!NMC.ServoStopMotor((byte)1, (byte)5)) {
         getServoEx().refreshPorts();
         throw new CarouselException("NMC.ServoStopMotor failed");
      } else {
         Util.sleep(510);
      }
   }

   public static boolean initializeNMC() {
      try {
         Servo lServo = ServoFactory.getInstance();
         lServo.initialize();
      } catch (DvdplayException var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
         return false;
      }

      try {
         getServoEx().getCarousel();
         Carousel.goToOffset(0);
         return true;
      } catch (DvdplayException var1) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var1.getMessage());
         return false;
      }
   }

   public static int calibrateSlot(JTextArea jt, int iPwm) throws CarouselException {
      getServoEx().getCarousel();
      int curPos = Integer.parseInt(Carousel.getPosition());
      int maxPos = curPos + 40;
      int minPos = curPos - 40;
      jt.setText(jt.getText().substring(0, jt.getDocument().getLength() - 1) + " \n");
      jt.setCaretPosition(jt.getDocument().getLength());
      jt.setCaretPosition(jt.getDocument().getLength());
      int posTimer = 0;
      NMC.ServoLoadTraj((byte)1, (byte)-113, 0, 10000, 5, (byte)iPwm);
      Util.sleep(551);

      while (true) {
         getServoEx().getCarousel();
         if (Integer.parseInt(Carousel.getPosition()) >= maxPos || ++posTimer >= 8) {
            jt.setCaretPosition(jt.getDocument().getLength());
            jt.setCaretPosition(jt.getDocument().getLength());
            Util.sleep(304);
            if (!isToolInserted()) {
               stopCarousel();
               return 5432123;
            } else {
               stopCarousel();
               jt.setText(jt.getText().substring(0, jt.getDocument().getLength() - 1) + " \n");
               jt.setCaretPosition(jt.getDocument().getLength());
               jt.setCaretPosition(jt.getDocument().getLength());
               getServoEx().getCarousel();
               maxPos = Integer.parseInt(Carousel.getPosition());
               Aem lAem = AemFactory.getInstance();
               getServoEx();
               NMC.ServoLoadTraj((byte)1, (byte)-105, curPos + ServoEx.mServoHomePosition, 5000, 5, (byte)0);
               Util.sleep(551);
               stopCarousel();
               posTimer = 0;
               NMC.ServoLoadTraj((byte)1, (byte)-49, 0, 10000, 5, (byte)iPwm);
               Util.sleep(551);

               while (true) {
                  getServoEx().getCarousel();
                  if (Integer.parseInt(Carousel.getPosition()) <= minPos || ++posTimer >= 8) {
                     jt.setCaretPosition(jt.getDocument().getLength());
                     jt.setCaretPosition(jt.getDocument().getLength());
                     Util.sleep(304);
                     if (!isToolInserted()) {
                        stopCarousel();
                        return 5432123;
                     } else {
                        stopCarousel();
                        jt.setText(jt.getText().substring(0, jt.getDocument().getLength() - 1) + " OK!\n");
                        jt.setCaretPosition(jt.getDocument().getLength());
                        jt.setCaretPosition(jt.getDocument().getLength());
                        getServoEx().getCarousel();
                        minPos = Integer.parseInt(Carousel.getPosition());
                        curPos = (maxPos + minPos) / 2;
                        getServoEx();
                        NMC.ServoLoadTraj((byte)1, (byte)-105, curPos + ServoEx.mServoHomePosition, 5000, 5, (byte)0);
                        Util.sleep(204);
                        stopCarousel();
                        return curPos;
                     }
                  }

                  jt.setText(jt.getText().substring(0, jt.getDocument().getLength() - 1) + ".\n");
               }
            }
         }

         jt.setText(jt.getText().substring(0, jt.getDocument().getLength() - 1) + ".\n");
      }
   }

   public static int plotTable(SlotCalTableModel sctm, int basePos, int currentCycle, int CalibrationPerBay, int toolOffset) {
      int curSlotPosition = basePos - toolOffset;
      Servo lServo = ServoFactory.getInstance();
      int beginSlot;
      if (currentCycle % CalibrationPerBay == 0) {
         beginSlot = currentCycle / CalibrationPerBay * lServo.getSlotsPerUnit() + 1;
      } else {
         beginSlot = (int)((float)currentCycle / CalibrationPerBay * lServo.getSlotsPerUnit() + 2.0F);
      }

      int endSlot;
      switch (CalibrationPerBay) {
         case 1:
            endSlot = beginSlot + 16;
            break;
         case 2:
            endSlot = beginSlot + (currentCycle % CalibrationPerBay == 1 ? 7 : 8);
            break;
         default:
            endSlot = beginSlot + (currentCycle % CalibrationPerBay == 2 ? 4 : 5);
      }

      sctm.setValueAt(String.valueOf(curSlotPosition), beginSlot - 1, 4);

      for (int i = beginSlot; i < endSlot; i++) {
         sctm.setValueAt(String.valueOf(curSlotPosition += 164), i, 4);
      }

      sctm.fireTableDataChanged();
      return endSlot % lServo.getSlotsPerUnit() == 0 ? curSlotPosition + 270 - toolOffset : curSlotPosition + 164 - toolOffset;
   }

   public static boolean calibrateFull(SlotCalTableModel sctm, int pwmValue, int toolOffset, int CalibrationPerBay, JTextArea jt) {
      Servo lServo = ServoFactory.getInstance();
      int nextSlotPosition = lServo.getSlotOffset(1);
      int CalibrationPerCarousel = lServo.getNumUnits() * CalibrationPerBay;
      int slotNum = 1;
      toolOffsetValue = toolOffset;
      Utility.statAnnouncer(jt, "- Start Full Calibration");

      for (int i = 0; i < CalibrationPerCarousel; i++) {
         calCount = String.valueOf(i + 1 + "/" + CalibrationPerCarousel);
         if (i % CalibrationPerBay == 0) {
            slotNum = i / CalibrationPerBay * lServo.getSlotsPerUnit() + 1;
         } else {
            slotNum = (int)((float)i / CalibrationPerBay * lServo.getSlotsPerUnit() + 2.0F);
         }

         Utility.statAnnouncer(jt, "- " + calCount + " Moving to Slot " + slotNum + "...");
         getServoEx().getDoor();
         Door.close();
         if (!checkOkToRotate()) {
            return AbortAbort(sctm, jt, 2002, true);
         }

         if (i % CalibrationPerBay == 0) {
            getServoEx().getCarousel();
            Carousel.goToOffset(lServo.calculateOffset(slotNum));
         } else {
            getServoEx().getCarousel();
            Carousel.goToOffset(nextSlotPosition);
         }

         if (!checkSlotEmpty()) {
            return AbortAbort(sctm, jt, 2003, true);
         }

         getServoEx().getCarousel();
         Carousel.disable();
         getServoEx().getDoor();
         Door.open();
         Utility.statAnnouncer(jt, "- " + calCount + " Insert Tool! Timeout in 20 seconds!");
         if (!checkToolInserted(jt)) {
            getServoEx().getCarousel();
            Carousel.enable();
            return AbortAbort(sctm, jt, 2004, true);
         }

         Utility.statAnnouncer(jt, "- " + calCount + " Calibrating Slot " + slotNum + "...");
         getServoEx().getCarousel();
         Carousel.enable();
         getServoEx().getDoor();
         Door.clamp();
         int basePos;
         if ((basePos = calibrateSlot(jt, pwmValue)) == 5432123) {
            return AbortAbort(sctm, jt, 2006, true);
         }

         nextSlotPosition = plotTable(sctm, basePos, i, CalibrationPerBay, toolOffset);
         getServoEx().getDoor();
         Door.open();
         Utility.statAnnouncer(jt, "- " + calCount + " Remove Tool! Timeout in 20 seconds!");
         if (!checkToolRemoved(jt)) {
            return AbortAbort(sctm, jt, 2005, true);
         }

         Utility.statAnnouncer(jt, "- " + calCount + " Finished calibrating slot " + slotNum);
      }

      getServoEx().getDoor();
      Door.close();
      if (!checkOkToRotate()) {
         return AbortAbort(sctm, jt, 2002, false);
      } else {
         toSlot(sctm, 1);
         Utility.statAnnouncer(jt, "- Average of mis-alignment per slot: " + calculateStat(sctm));
         Utility.statAnnouncer(jt, "- End of Full Calibration");
         return true;
      }
   }

   public static boolean calibrateSelect(SlotCalTableModel sctm, int pwmValue, JTextArea jt) {
      int totalCal = sctm.getSelected().size();
      if (totalCal == 0) {
         return AbortAbort(sctm, jt, 2001, false);
      } else {
         Utility.statAnnouncer(jt, "- Start Selective Calibration");

         for (int i = 0; i < totalCal; i++) {
            calCount = i + 1 + "/" + totalCal;
            getServoEx().getDoor();
            Door.close();
            if (!checkOkToRotate()) {
               return AbortAbort(sctm, jt, 2002, true);
            }

            int slotNum = Integer.parseInt(String.valueOf(sctm.getSelected().get(i))) + 1;
            Utility.statAnnouncer(jt, "- " + calCount + " Moving to slot " + slotNum + "...");
            toSlot(sctm, slotNum);
            if (!checkSlotEmpty()) {
               return AbortAbort(sctm, jt, 2003, true);
            }

            getServoEx().getCarousel();
            Carousel.disable();
            getServoEx().getDoor();
            Door.open();
            Utility.statAnnouncer(jt, "- " + calCount + " Insert Tool! Timeout in 20 seconds!");
            if (!checkToolInserted(jt)) {
               getServoEx().getCarousel();
               Carousel.enable();
               return AbortAbort(sctm, jt, 2004, true);
            }

            Utility.statAnnouncer(jt, "- " + calCount + " Calibrating slot " + slotNum + "...");
            getServoEx().getCarousel();
            Carousel.enable();
            getServoEx().getDoor();
            Door.clamp();
            int newPos;
            if ((newPos = calibrateSlot(jt, pwmValue)) == 5432123) {
               return AbortAbort(sctm, jt, 2006, true);
            }

            sctm.setValueAt(String.valueOf(newPos - toolOffsetValue), slotNum - 1, 4);
            sctm.setListItem(slotNum - 1, true);
            sctm.fireTableCellUpdated(slotNum - 1, 4);
            getServoEx().getDoor();
            Door.open();
            Utility.statAnnouncer(jt, "- " + calCount + " Remove Tool! Timeout in 20 seconds!");
            if (!checkToolRemoved(jt)) {
               return AbortAbort(sctm, jt, 2005, true);
            }

            Utility.statAnnouncer(jt, "- " + calCount + " Finished calibrating slot " + slotNum);
         }

         getServoEx().getDoor();
         Door.close();
         if (!checkOkToRotate()) {
            return AbortAbort(sctm, jt, 2002, false);
         } else {
            toSlot(sctm, 1);
            Utility.statAnnouncer(jt, "- Average of mis-alignment per slot: " + calculateStat(sctm));
            Utility.statAnnouncer(jt, "- End of Selective Calibration");
            return true;
         }
      }
   }

   public static boolean AbortAbort(SlotCalTableModel sctm, JTextArea jt, int errCode, boolean displayCycle) {
      String errMsg;
      switch (errCode) {
         case 2000:
            errMsg = "Failed to home carousel! Check log for more info...";
            break;
         case 2001:
            errMsg = "Select the slots from the table and try again!";
            break;
         case 2002:
            errMsg = "Unsafe to rotate carousel! Door Lock or Arm problem.";
            break;
         case 2003:
            errMsg = "Current slot is occupied!";
            break;
         case 2004:
            errMsg = "No tool detected!";
            break;
         case 2005:
            errMsg = "Tool needs to be removed!";
            break;
         case 2006:
            errMsg = "Tool was removed during slot calibration!";
            break;
         default:
            errMsg = "Possible sensor or mechanical malfunctioning!";
      }

      if (displayCycle) {
         Utility.statAnnouncer(jt, "- " + calCount + " Abort: " + errMsg);
      } else {
         Utility.statAnnouncer(jt, "- Abort: " + errMsg);
      }

      allButtonOk();
      sctm.unSelectAll();
      getServoEx().getDoor();
      Door.close();
      return false;
   }

   public static void allButtonNot() {
      AbstractHardwareThread.disableAllButton(component, "save");
      AbstractHardwareThread.disableAllButton(component, "goToSlot");
      AbstractHardwareThread.disableAllButton(component, "calibrate");
      AbstractHardwareThread.disableAllButton(component, "adjust");
      AbstractHardwareThread.disableAllButton(component, "toggleCheckAll");
   }

   public static void allButtonOk() {
      AbstractHardwareThread.enableAllButton(component, "save");
      AbstractHardwareThread.enableAllButton(component, "goToSlot");
      AbstractHardwareThread.enableAllButton(component, "calibrate");
      AbstractHardwareThread.enableAllButton(component, "adjust");
      AbstractHardwareThread.enableAllButton(component, "toggleCheckAll");
   }

   public static String calculateSlotTable(SlotCalTableModel sctm) {
      Servo lServo = ServoFactory.getInstance();

      for (int i = 0; i < lServo.getNumSlots(); i++) {
         sctm.setValueAt(String.valueOf(lServo.calculateOffset(i + 1)), i, 2);
      }

      sctm.fireTableDataChanged();
      return "";
   }

   public static String calculateStat(SlotCalTableModel sctm) {
      Servo lServo = ServoFactory.getInstance();
      int absValue = 0;

      for (int i = 1; i < lServo.getNumSlots(); i++) {
         absValue += Math.abs(Integer.parseInt(String.valueOf(sctm.getValueAt(i, 4))) - Integer.parseInt(String.valueOf(sctm.getValueAt(i, 2))));
      }

      return String.valueOf(Math.round((float)absValue / lServo.getNumSlots()));
   }
}
