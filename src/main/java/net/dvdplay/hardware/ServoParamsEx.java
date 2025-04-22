package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoEx;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.dom.DOMData;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.Utility;

public class ServoParamsEx extends ServoParams {
   static ActionListener action;
   static JComponent component;
   static ServoEx mServo = null;

   public ServoParamsEx(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public static void init() {
      Servo lServo = ServoFactory.getInstance();
      if (lServo != null && lServo instanceof ServoEx) {
         mServo = (ServoEx)lServo;
      }
   }

   private static JTextArea getMessageTextArea() {
      JPanel statusPanel = (JPanel)component.getComponentAt(670, 10);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      jsp.setAutoscrolls(true);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea jt = (JTextArea)jvp.getComponent(0);
      jt.setAutoscrolls(true);
      return jt;
   }

   public static void save(String data, boolean toDisk) {
      JTextArea jt = getMessageTextArea();
      StringTokenizer lStrTok = new StringTokenizer(data, ",");
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      lServoEx.setServoOffset(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoInputStep(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoOutputStep(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoDiscThreshold(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoKp(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoKd(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoKi(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoIntegrationLimit(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoOutputLimit(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoCurrentLimit(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoPositionErrorLimit(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoServoRate(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoDeadbandComp(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoVelocity(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoAcceleration(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoKp2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoKd2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoKi2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoIntegrationLimit2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoOutputLimit2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoCurrentLimit2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoPositionErrorLimit2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoServoRate2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoDeadbandComp2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoVelocity2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoAcceleration2(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setArmEjectWaitTime(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setMoveToOffsetWaitTime(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setMoveToOffsetTimeOut(Integer.parseInt(lStrTok.nextToken()));
      lServoEx.setServoParameters();
      Aem.updateAemProperties();
      if (toDisk) {
         try {
            DOMData.save();
            Utility.statAnnouncer(jt, "Saved to disk.");
         } catch (Exception var7) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var7.getMessage(), var7);
            Utility.statAnnouncer(jt, "Save to disk failed.");
         }
      } else {
         try {
            DOMData.save();
            Aem.uploadServoData();
            Utility.statAnnouncer(jt, "Saved to server.");
         } catch (Exception var6) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var6.getMessage(), var6);
            Utility.statAnnouncer(jt, "Save to server failed.");
         }
      }
   }

   public static String verifyAndSaveToDisk(String data) {
      save(data, true);
      return null;
   }

   public static String verifyAndSaveToServer(String data) {
      save(data, false);
      return null;
   }

   public static String set(String data) {
      StringTokenizer lStrTok = new StringTokenizer(data, ",");
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!lServoEx.isOverThreshold()) {
         lServoEx.setServoParameters(
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken())
         );
      } else {
         for (int i = 0; i < 11; i++) {
            lStrTok.nextToken();
         }

         lServoEx.setServoParameters(
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken()),
            Integer.parseInt(lStrTok.nextToken())
         );
      }

      return null;
   }

   public static String getKpOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoKp2());
   }

   public static String getKdOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoKd2());
   }

   public static String getKiOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoKi2());
   }

   public static String getIntegrationLimitOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoIntegrationLimit2());
   }

   public static String getOutputLimitOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoOutputLimit2());
   }

   public static String getCurrentLimitOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoCurrentLimit2());
   }

   public static String getPositionErrorLimitOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoPositionErrorLimit2());
   }

   public static String getServoRateOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoServoRate2());
   }

   public static String getDeadbandCompOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoDeadbandComp2());
   }

   public static String getVelocityOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoVelocity2());
   }

   public static String getAccelerationOver() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoAcceleration2());
   }

   public static String getKpUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoKp());
   }

   public static String getKdUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoKd());
   }

   public static String getKiUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoKi());
   }

   public static String getIntegrationLimitUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoIntegrationLimit());
   }

   public static String getOutputLimitUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoOutputLimit());
   }

   public static String getCurrentLimitUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoCurrentLimit());
   }

   public static String getPositionErrorLimitUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoPositionErrorLimit());
   }

   public static String getServoRateUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoServoRate());
   }

   public static String getDeadbandCompUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoDeadbandComp());
   }

   public static String getVelocityUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoVelocity());
   }

   public static String getAccelerationUnder() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoAcceleration());
   }

   public static String getSlotOneOffset() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoOffset());
   }

   public static String getInputStep() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoInputStep());
   }

   public static String getOutputStep() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoOutputStep());
   }

   public static String getDiscThreshold() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getServoDiscThreshold());
   }

   public static String getArmEjectWaitTime() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getArmEjectWaitTime());
   }

   public static String getServoMoveToOffsetWaitTime() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getMoveToOffsetWaitTime());
   }

   public static String getServoMoveToOffsetTimeout() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return Integer.toString(lServoEx.getMoveToOffsetTimeOut());
   }

   public static String get3rdHomingVelocity() {
      return "123";
   }

   public static boolean getReflectiveSensor() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return lServoEx.isCaseInSensorReflective();
   }

   public static boolean getInterruptedSensor() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return !lServoEx.isCaseInSensorReflective();
   }
}
