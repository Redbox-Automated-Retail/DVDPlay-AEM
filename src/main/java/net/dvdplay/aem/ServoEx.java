package net.dvdplay.aem;

import java.math.BigDecimal;
import java.util.logging.Level;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.RDataSetFieldValues;
import net.dvdplay.dom.DOMData;
import net.dvdplay.dom.PersistenceData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class ServoEx extends Servo {
   private Arm mArm;
   private Door mDoor;
   private Carousel mCarousel;
   private Roller mRoller;
   private Sensor mCaseSensor1;
   private Sensor mCaseSensor2;
   private Sensor mCaseInSensor;
   private Sensor mInputSensor;
   public final int PIODevice0 = 1;
   public final int PIODevice1 = 2;
   public final int PIODevice2 = 3;
   private final int mServoBaudRate = 19200;
   private final String mServoCOMPort = "COM1:";
   private final int NUM_MODULES = 3;
   private final double DEGREES_PER_SLOT = 3.4;
   private final int NUM_SLOTS = 102;
   public final int EMPTY_SLOT = 101;
   public final int FULL_SLOT = 102;
   private int mServoDiscThreshold = 50;
   private int mServoInputStep = 0;
   private int mServoOutputStep = 0;
   private int mServoOffset = -2756;
   private int mServoKp = 2500;
   private int mServoKd = 32000;
   private int mServoKi = 1200;
   private int mServoServoRate = 1;
   private int mServoDeadbandComp = 20;
   private int mServoIntegrationLimit = 20;
   private int mServoVelocity = 30000;
   private int mServoAcceleration = 500;
   private int mServoOutputLimit = 255;
   private int mServoCurrentLimit = 0;
   private int mServoPositionErrorLimit = 16000;
   private int mServoKp2 = 2500;
   private int mServoKd2 = 32000;
   private int mServoKi2 = 1200;
   private int mServoServoRate2 = 1;
   private int mServoDeadbandComp2 = 20;
   private int mServoIntegrationLimit2 = 20;
   private int mServoVelocity2 = 30000;
   private int mServoAcceleration2 = 500;
   private int mServoOutputLimit2 = 255;
   private int mServoCurrentLimit2 = 0;
   private int mServoPositionErrorLimit2 = 16000;
   private int mArmEjectWaitTime = 100;
   private int mMoveToOffsetWaitTime = 100;
   private int mMoveToOffsetTimeOut = 35000;
   private boolean mCaseInSensorIsReflective = true;
   private boolean mServoModuleStatus = false;

   public boolean getServoModuleStatus() {
      return this.mServoModuleStatus;
   }

   public static ServoEx valueOf(Servo aServo) {
      if (aServo != null && aServo instanceof ServoEx) {
         return (ServoEx)aServo;
      } else {
         throw new ServoException("ServoEx downcast failed.");
      }
   }

   public boolean isCaseInSensorReflective() {
      return this.mCaseInSensorIsReflective;
   }

   public void setCaseInSensorIsReflective(boolean a) {
      this.mCaseInSensorIsReflective = a;
      this.mCaseInSensor = new Sensor((byte)2, 5, !this.mCaseInSensorIsReflective);
   }

   public int getServoDiscThreshold() {
      return this.mServoDiscThreshold;
   }

   public void setServoDiscThreshold(int aServoDiscThreshold) {
      this.mServoDiscThreshold = aServoDiscThreshold;
   }

   public int getServoOffset() {
      return this.mServoOffset;
   }

   public void setServoOffset(int aServoOffset) {
      this.mServoOffset = aServoOffset;
   }

   public int getServoKp() {
      return this.mServoKp;
   }

   public void setServoKp(int aServoKp) {
      this.mServoKp = aServoKp;
   }

   public int getServoKd() {
      return this.mServoKd;
   }

   public void setServoKd(int aServoKd) {
      this.mServoKd = aServoKd;
   }

   public int getServoKi() {
      return this.mServoKi;
   }

   public void setServoKi(int aServoKi) {
      this.mServoKi = aServoKi;
   }

   public int getServoServoRate() {
      return this.mServoServoRate;
   }

   public void setServoServoRate(int aServoServoRate) {
      this.mServoServoRate = aServoServoRate;
   }

   public int getServoDeadbandComp() {
      return this.mServoDeadbandComp;
   }

   public void setServoDeadbandComp(int aServoDeadbandComp) {
      this.mServoDeadbandComp = aServoDeadbandComp;
   }

   public int getServoIntegrationLimit() {
      return this.mServoIntegrationLimit;
   }

   public void setServoIntegrationLimit(int aServoIntegrationLimit) {
      this.mServoIntegrationLimit = aServoIntegrationLimit;
   }

   public int getServoVelocity() {
      return this.mServoVelocity;
   }

   public void setServoVelocity(int aServoVelocity) {
      this.mServoVelocity = aServoVelocity;
   }

   public int getServoAcceleration() {
      return this.mServoAcceleration;
   }

   public void setServoAcceleration(int aServoAcceleration) {
      this.mServoAcceleration = aServoAcceleration;
   }

   public int getServoOutputLimit() {
      return this.mServoOutputLimit;
   }

   public void setServoOutputLimit(int aServoOutputLimit) {
      this.mServoOutputLimit = aServoOutputLimit;
   }

   public int getServoCurrentLimit() {
      return this.mServoCurrentLimit;
   }

   public void setServoCurrentLimit(int aServoCurrentLimit) {
      this.mServoCurrentLimit = aServoCurrentLimit;
   }

   public int getServoPositionErrorLimit() {
      return this.mServoPositionErrorLimit;
   }

   public void setServoPositionErrorLimit(int aServoPositionErrorLimit) {
      this.mServoPositionErrorLimit = aServoPositionErrorLimit;
   }

   public int getServoInputStep() {
      return this.mServoInputStep;
   }

   public void setServoInputStep(int aServoInputStep) {
      this.mServoInputStep = aServoInputStep;
   }

   public int getServoOutputStep() {
      return this.mServoOutputStep;
   }

   public void setServoOutputStep(int aServoOutputStep) {
      this.mServoOutputStep = aServoOutputStep;
   }

   public int getServoKp2() {
      return this.mServoKp2;
   }

   public void setServoKp2(int aServoKp2) {
      this.mServoKp2 = aServoKp2;
   }

   public int getServoKd2() {
      return this.mServoKd2;
   }

   public void setServoKd2(int aServoKd2) {
      this.mServoKd2 = aServoKd2;
   }

   public int getServoKi2() {
      return this.mServoKi2;
   }

   public void setServoKi2(int aServoKi2) {
      this.mServoKi2 = aServoKi2;
   }

   public int getServoServoRate2() {
      return this.mServoServoRate2;
   }

   public void setServoServoRate2(int aServoServoRate2) {
      this.mServoServoRate2 = aServoServoRate2;
   }

   public int getServoDeadbandComp2() {
      return this.mServoDeadbandComp2;
   }

   public void setServoDeadbandComp2(int aServoDeadbandComp2) {
      this.mServoDeadbandComp2 = aServoDeadbandComp2;
   }

   public int getServoIntegrationLimit2() {
      return this.mServoIntegrationLimit2;
   }

   public void setServoIntegrationLimit2(int aServoIntegrationLimit2) {
      this.mServoIntegrationLimit2 = aServoIntegrationLimit2;
   }

   public int getServoVelocity2() {
      return this.mServoVelocity2;
   }

   public void setServoVelocity2(int aServoVelocity2) {
      this.mServoVelocity2 = aServoVelocity2;
   }

   public int getServoAcceleration2() {
      return this.mServoAcceleration2;
   }

   public void setServoAcceleration2(int aServoAcceleration2) {
      this.mServoAcceleration2 = aServoAcceleration2;
   }

   public int getServoOutputLimit2() {
      return this.mServoOutputLimit2;
   }

   public void setServoOutputLimit2(int aServoOutputLimit2) {
      this.mServoOutputLimit2 = aServoOutputLimit2;
   }

   public int getServoCurrentLimit2() {
      return this.mServoCurrentLimit2;
   }

   public void setServoCurrentLimit2(int aServoCurrentLimit2) {
      this.mServoCurrentLimit2 = aServoCurrentLimit2;
   }

   public int getServoPositionErrorLimit2() {
      return this.mServoPositionErrorLimit2;
   }

   public void setServoPositionErrorLimit2(int aServoPositionErrorLimit2) {
      this.mServoPositionErrorLimit2 = aServoPositionErrorLimit2;
   }

   public int getArmEjectWaitTime() {
      return this.mArmEjectWaitTime;
   }

   public void setArmEjectWaitTime(int aArmEjectWaitTime) {
      this.mArmEjectWaitTime = aArmEjectWaitTime;
   }

   public int getMoveToOffsetWaitTime() {
      return this.mMoveToOffsetWaitTime;
   }

   public void setMoveToOffsetWaitTime(int aMoveToOffsetWaitTime) {
      this.mMoveToOffsetWaitTime = aMoveToOffsetWaitTime;
   }

   public int getMoveToOffsetTimeOut() {
      return this.mMoveToOffsetTimeOut;
   }

   public void setMoveToOffsetTimeOut(int aMoveToOffsetTimeOut) {
      this.mMoveToOffsetTimeOut = aMoveToOffsetTimeOut;
   }

   public void readServoProperties() {
      String lCurrentField = "";
      RDataSetFieldValues lRDataSetFieldValues = null;

      try {
         for (int i = 0; i < DOMData.mAemPropertiesData.rowCount(); i++) {
            if (!DOMData.mAemPropertiesData.isDeleted(i)) {
               lRDataSetFieldValues = DOMData.mAemPropertiesData.getRow(i);
               lCurrentField = "ServoOffset";
               this.mServoOffset = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoOffset")));
               lCurrentField = "ServoInputStep";
               this.mServoInputStep = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoInputStep")));
               lCurrentField = "ServoOutputStep";
               this.mServoOutputStep = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoOutputStep")));
               lCurrentField = "ServoDiscThreshold";
               this.mServoDiscThreshold = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoDiscThreshold")));
               lCurrentField = "ServoKp";
               this.mServoKp = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoKp")));
               lCurrentField = "ServoKd";
               this.mServoKd = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoKd")));
               lCurrentField = "ServoKi";
               this.mServoKi = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoKi")));
               lCurrentField = "ServoRate";
               this.mServoServoRate = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoRate")));
               lCurrentField = "ServoDeadband";
               this.mServoDeadbandComp = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoDeadband")));
               lCurrentField = "ServoIntegrationLimit";
               this.mServoIntegrationLimit = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoIntegrationLimit")));
               lCurrentField = "ServoVelocity";
               this.mServoVelocity = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoVelocity")));
               lCurrentField = "ServoAcceleration";
               this.mServoAcceleration = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoAcceleration")));
               lCurrentField = "ServoOutputLimit";
               this.mServoOutputLimit = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoOutputLimit")));
               lCurrentField = "ServoCurrentLimit";
               this.mServoCurrentLimit = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoCurrentLimit")));
               lCurrentField = "ServoPositionErrorLimit";
               this.mServoPositionErrorLimit = Integer.parseInt(
                  lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoPositionErrorLimit"))
               );
               lCurrentField = "ServoKp2";
               this.mServoKp2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoKp2")));
               lCurrentField = "ServoKd2";
               this.mServoKd2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoKd2")));
               lCurrentField = "ServoKi2";
               this.mServoKi2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoKi2")));
               lCurrentField = "ServoRate2";
               this.mServoServoRate2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoRate2")));
               lCurrentField = "ServoDeadband2";
               this.mServoDeadbandComp2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoDeadband2")));
               lCurrentField = "ServoIntegrationLimit2";
               this.mServoIntegrationLimit2 = Integer.parseInt(
                  lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoIntegrationLimit2"))
               );
               lCurrentField = "ServoVelocity2";
               this.mServoVelocity2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoVelocity2")));
               lCurrentField = "ServoAcceleration2";
               this.mServoAcceleration2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoAcceleration2")));
               lCurrentField = "ServoOutputLimit2";
               this.mServoOutputLimit2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoOutputLimit2")));
               lCurrentField = "ServoCurrentLimit2";
               this.mServoCurrentLimit2 = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoCurrentLimit2")));
               lCurrentField = "ServoPositionErrorLimit2";
               this.mServoPositionErrorLimit2 = Integer.parseInt(
                  lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoPositionErrorLimit2"))
               );
               lCurrentField = "ServoArmEjectWaitTime";
               this.mArmEjectWaitTime = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoArmEjectWaitTime")));
               lCurrentField = "ServoMoveToOffsetWaitTime";
               this.mMoveToOffsetWaitTime = Integer.parseInt(
                  lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoMoveToOffsetWaitTime"))
               );
               lCurrentField = "ServoMoveToOffsetTimeout";
               this.mMoveToOffsetTimeOut = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoMoveToOffsetTimeout")));
               lCurrentField = "ServoIsCaseInSensorReflective";
               if (lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServoIsCaseInSensorReflective")).equals(Integer.toString(1))) {
                  this.mCaseInSensorIsReflective = true;
               } else {
                  this.mCaseInSensorIsReflective = false;
               }
               break;
            }
         }
      } catch (Exception var4) {
         Aem.logDetailMessage(DvdplayLevel.SEVERE, "Error reading in field " + lCurrentField);
         Aem.logSummaryMessage("Error reading in field " + lCurrentField);
         throw new DvdplayException("Error reading servo params");
      }
   }

   private void resetInitalized() {
      Carousel.resetInitialized();
      Arm.resetInitialized();
      Door.resetInitialized();
   }

   public boolean isInitialized() {
      return Carousel.isInitialized() && Arm.isInitialized() && Door.isInitialized();
   }

   public boolean setServoParameters(
      int aServoKp,
      int aServoKd,
      int aServoKi,
      int aServoIntegrationLimit,
      int aServoOutputLimit,
      int aServoCurrentLimit,
      int aServoPositionErrorLimit,
      int aServoServoRate,
      int aServoDeadbandComp
   ) {
      return NMC.ServoSetGain(
         (byte)1,
         aServoKp,
         aServoKd,
         aServoKi,
         aServoIntegrationLimit,
         (byte)aServoOutputLimit,
         (byte)aServoCurrentLimit,
         aServoPositionErrorLimit,
         (byte)aServoServoRate,
         (byte)aServoDeadbandComp
      );
   }

   public boolean setServoParameters() {
      return !this.isOverThreshold()
         ? NMC.ServoSetGain(
            (byte)1,
            this.mServoKp,
            this.mServoKd,
            this.mServoKi,
            this.mServoIntegrationLimit,
            (byte)this.mServoOutputLimit,
            (byte)this.mServoCurrentLimit,
            this.mServoPositionErrorLimit,
            (byte)this.mServoServoRate,
            (byte)this.mServoDeadbandComp
         )
         : NMC.ServoSetGain(
            (byte)1,
            this.mServoKp2,
            this.mServoKd2,
            this.mServoKi2,
            this.mServoIntegrationLimit2,
            (byte)this.mServoOutputLimit2,
            (byte)this.mServoCurrentLimit2,
            this.mServoPositionErrorLimit2,
            (byte)this.mServoServoRate2,
            (byte)this.mServoDeadbandComp2
         );
   }

   public boolean isOverThreshold() {
      return Aem.getDiscCount() > this.mServoDiscThreshold;
   }

   public void enableMotors() throws ServoException {
      if (!NMC.IoClrOutBit((byte)3, 4)) {
         throw new ServoException("Enable motors failed.");
      }
   }

   public void disableMotors() throws ServoException {
      if (!NMC.IoSetOutBit((byte)3, 4)) {
         throw new ServoException("Disable motors failed.");
      }
   }

   private int openServo() {
      NMC.ErrorPrinting(0);
      return NMC.NmcInit("COM1:", 19200);
   }

   private void openPorts() {
      if (!NMC.IoClrOutBit((byte)2, 0)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 1)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 2)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 3)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 4)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 5)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 6)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 7)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 8)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 9)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 10)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 11)) {
      }

      if (!NMC.IoBitDirIn((byte)2, 0)) {
      }

      if (!NMC.IoBitDirOut((byte)2, 1)) {
      }

      if (!NMC.IoBitDirIn((byte)2, 2)) {
      }

      if (!NMC.IoBitDirOut((byte)2, 3)) {
      }

      if (!NMC.IoBitDirIn((byte)2, 4)) {
      }

      if (!NMC.IoBitDirIn((byte)2, 5)) {
      }

      if (!NMC.IoBitDirOut((byte)2, 6)) {
      }

      if (!NMC.IoBitDirIn((byte)2, 7)) {
      }

      if (!NMC.IoBitDirOut((byte)2, 8)) {
      }

      if (!NMC.IoBitDirOut((byte)2, 9)) {
      }

      if (!NMC.IoBitDirOut((byte)2, 10)) {
      }

      if (!NMC.IoBitDirOut((byte)2, 11)) {
      }

      if (!NMC.IoClrOutBit((byte)2, 11)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 0)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 1)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 2)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 3)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 4)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 5)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 6)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 7)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 8)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 9)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 10)) {
      }

      if (!NMC.IoClrOutBit((byte)3, 11)) {
      }

      if (!NMC.IoBitDirOut((byte)3, 0)) {
      }

      if (!NMC.IoBitDirOut((byte)3, 1)) {
      }

      if (!NMC.IoBitDirOut((byte)3, 2)) {
      }

      if (!NMC.IoBitDirOut((byte)3, 3)) {
      }

      if (!NMC.IoBitDirOut((byte)3, 4)) {
      }

      if (!NMC.IoBitDirOut((byte)3, 5)) {
      }

      if (!NMC.IoBitDirIn((byte)3, 6)) {
      }

      if (!NMC.IoBitDirIn((byte)3, 7)) {
      }

      if (!NMC.IoBitDirIn((byte)3, 8)) {
      }

      if (!NMC.IoBitDirIn((byte)3, 9)) {
      }

      if (!NMC.IoBitDirIn((byte)3, 10)) {
      }

      if (!NMC.IoBitDirIn((byte)3, 11)) {
      }

      if (!NMC.NmcDefineStatus((byte)2, (byte)1)) {
      }

      if (!NMC.NmcDefineStatus((byte)3, (byte)1)) {
      }
   }

   public void initServo() {
      int lMaxRetries = 3;
      int lNumRetry = 0;
      this.closeServo();
      this.resetInitalized();

      int lNumModules;
      do {
         lNumRetry++;
         lNumModules = this.openServo();
      } while (lNumRetry <= 3 && 3 != lNumModules);

      if (lNumRetry > 3) {
         this.mServoModuleStatus = false;
         Aem.setHardwareError();
         throw new ServoException("Found " + lNumModules + " modules. Expected " + 3 + " modules.");
      } else {
         this.mServoModuleStatus = true;
         this.openPorts();
         if (!this.setServoParameters()) {
            Aem.setHardwareError();
            throw new ServoException("Failed to set Servo parameters.");
         }
      }
   }

   public void closeServo() {
      NMC.NmcShutdown();
   }

   public void refreshPorts() {
      if (!NMC.NmcReadStatus((byte)2, (byte)1)) {
      }

      if (!NMC.NmcReadStatus((byte)3, (byte)1)) {
      }
   }

   public boolean isLcdPowerOn() {
      return NMC.IoInBitVal((byte)2, 0);
   }

   public void lcdOn() {
      if (!this.isLcdPowerOn()) {
         if (!NMC.IoSetOutBit((byte)2, 10)) {
            throw new ServoException("lcdOn failed: NMC.IoSetOutBit");
         }

         Util.sleep(100);
         if (!NMC.IoClrOutBit((byte)2, 10)) {
            throw new ServoException("lcdOn failed: NMC.IoClrOutBit");
         }
      }
   }

   public void lcdOff() {
      if (this.isLcdPowerOn()) {
         if (!NMC.IoSetOutBit((byte)2, 10)) {
            throw new ServoException("lcdOff failed: NMC.IoSetOutBit");
         }

         Util.sleep(100);
         if (!NMC.IoClrOutBit((byte)2, 10)) {
            throw new ServoException("lcdOff failed: NMC.IoClrOutBit");
         }
      }
   }

   public int goToSlot(int aSlotNum) throws ServoException {
      int slotStat = 1;
      if (aSlotNum > 0 && aSlotNum <= this.getNumSlots()) {
         int lOffset = this.getSlotOffset(aSlotNum);
         Aem.logDetailMessage(DvdplayLevel.INFO, "Going to slot " + aSlotNum + ". (Offset " + lOffset + ")");

         try {
            Carousel.goToOffset(lOffset);
            if (this.mCaseInSensor.on()) {
               slotStat = 2;
            }

            return slotStat;
         } catch (CarouselException var6) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "CarouselException caught: " + var6.getMessage());
            throw new CarouselException("Failed to go to slot " + aSlotNum);
         } catch (ServoException var7) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var7.getMessage());
            throw new ServoException("Failed to go to slot " + aSlotNum);
         }
      } else {
         throw new ServoException("Invalid slot number: " + aSlotNum);
      }
   }

   public int calculateBayNum(int aSlotNum) {
      return (aSlotNum - 1) / this.getSlotsPerUnit() + 1;
   }

   public int calculateBaySlot(int aSlotNum) {
      return (aSlotNum - 1) % this.getSlotsPerUnit() + 1;
   }

   public int calculateOffset(int aSlotNum) {
      int lBay = this.calculateBayNum(aSlotNum);
      int lBaySlot = this.calculateBaySlot(aSlotNum);
      double lDegrees = (lBay - 1) * (360 / this.getNumUnits()) + (lBaySlot - 1) * 3.4;
      double lCarouselUnits = lDegrees / 360.0 * this.getNumTicks() + this.mServoOffset;
      return new BigDecimal(lCarouselUnits).setScale(0, 4).intValue();
   }

   public PersistenceData createSlotOffsetData() {
      PersistenceData lPD = new PersistenceData(DvdplayBase.SLOT_OFFSET_FIELD_NAMES, DvdplayBase.SLOT_OFFSET_FIELD_TYPES, "SlotOffset", "SlOffst.dll");
      String[] lValues = new String[DvdplayBase.SLOT_OFFSET_FIELD_NAMES.length];

      for (int i = 0; i < this.getNumSlots(); i++) {
         lValues[lPD.getFieldIndex("Slot")] = Integer.toString(i + 1);
         lValues[lPD.getFieldIndex("Offset")] = Integer.toString(this.calculateOffset(i + 1));
         lPD.addRow(lValues);
      }

      return lPD;
   }

   public void setSlotOffset(int aSlotNum, int aOffset) throws ServoException {
      try {
         int lDelIndex = Util.getRCSetIndexForFieldValue(DOMData.mSlotOffsetData, "Slot", Integer.toString(aSlotNum));
         if (lDelIndex >= 0) {
            DOMData.mSlotOffsetData.deleteRow(lDelIndex);
         }

         String[] lValues = new String[DvdplayBase.SLOT_OFFSET_FIELD_NAMES.length];
         lValues[DOMData.mSlotOffsetData.getFieldIndex("Slot")] = Integer.toString(aSlotNum);
         lValues[DOMData.mSlotOffsetData.getFieldIndex("Offset")] = Integer.toString(aOffset);
         DOMData.mSlotOffsetData.addRow(lValues);
      } catch (DvdplayException var5) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var5.getMessage());
         throw new ServoException("setSlotOffset failed");
      }
   }

   public int getSlotOffset(int aSlotNum) {
      if (aSlotNum > 0 && aSlotNum <= this.getNumSlots()) {
         try {
            for (int i = 0; i < DOMData.mSlotOffsetData.rowCount(); i++) {
               if (!DOMData.mSlotOffsetData.isDeleted(i)
                  && Integer.parseInt(DOMData.mSlotOffsetData.getFieldValue(i, DOMData.mSlotOffsetData.getFieldIndex("Slot"))) == aSlotNum) {
                  return Integer.parseInt(DOMData.mSlotOffsetData.getFieldValue(i, DOMData.mSlotOffsetData.getFieldIndex("Offset")));
               }
            }
         } catch (DvdplayException var3) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
         }

         int lCalcOffset = this.calculateOffset(aSlotNum);
         this.setSlotOffset(aSlotNum, lCalcOffset);
         DOMData.save();
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Could not find slot " + aSlotNum + " in " + "SlotOffset" + ".  Using calcuated offset " + lCalcOffset);
         return lCalcOffset;
      } else {
         throw new ServoException("Invalid slot number: " + aSlotNum);
      }
   }

   public void ejectDisc(int action) throws ServoException {
      try {
         Door.open();
         Roller.out();
         Arm.eject();
         Door.clamp();
      } catch (ServoException var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var7.getMessage());

         try {
            Roller.stop();
            Door.close();
            Arm.clear();
         } catch (ServoException var6) {
         }

         throw new ServoException("Eject disc failed");
      }

      long lStartTime = System.currentTimeMillis();

      do {
         Util.sleep(Servo.mPollPIOBitsSleepTime);
      } while (System.currentTimeMillis() - lStartTime <= 3000L);

      try {
         Arm.clear();
         Door.close();
         Roller.stop();
         Util.sleep(1000);
      } catch (ServoException var8) {
         if (!this.clearDisc(false)) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "DoorException caught: " + var8.getMessage());
            throw new ServoException("Eject disc failed");
         }
      }
   }

   public boolean clearDisc(boolean aDoNotTimeOut) {
      int lMaxRetries = 3;
      int lNumRetry = 0;
      if (Door.isLocked()) {
         return true;
      } else {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Clearing disc");

         do {
            lNumRetry++;

            try {
               Door.clamp();
               Roller.out();
               if (!this.isDiscInSlot()) {
                  Arm.eject();
               }

               long lStartTime = System.currentTimeMillis();

               do {
                  Carousel.carouselShake();

                  try {
                     Door.close();
                  } catch (DoorException var8) {
                  }
               } while ((System.currentTimeMillis() - lStartTime <= 3000L || aDoNotTimeOut) && !Door.isLocked());
            } catch (ServoException var9) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var9.getMessage());
            }

            try {
               Roller.stop();
               Arm.clear();
               Door.close();
            } catch (ServoException var7) {
            }
         } while (!Door.isLocked() && lNumRetry < lMaxRetries);

         if (Door.isLocked()) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Clearing disc succeeded");
         } else {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Clearing disc failed");
         }

         return Door.isLocked();
      }
   }

   public boolean reseatDisc() {
      int lMaxRetries = 3;
      int lNumRetry = 0;
      if (Door.isLocked()) {
         return true;
      } else {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Reseating disc");

         do {
            lNumRetry++;

            try {
               Arm.clear();
               Roller.in();
               Door.clamp();
               long lStartTime = System.currentTimeMillis();

               do {
                  Carousel.carouselShake();
               } while (System.currentTimeMillis() - lStartTime <= 3000L && !Door.isLocked());
            } catch (ServoException var7) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var7.getMessage());
            }

            try {
               Roller.stop();
               Door.close();
            } catch (ServoException var6) {
            }
         } while (!Door.isLocked() && lNumRetry < lMaxRetries);

         if (Door.isLocked()) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Reseating disc succeeded");
         } else {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Reseating disc failed");
         }

         return Door.isLocked();
      }
   }

   public void intakeDisc(int aTimeout, boolean forInspection) throws ServoException {
      Arm.clear();
      Roller.in();
      Door.clamp();
      long lStartTime = System.currentTimeMillis();

      do {
         Util.sleep(100);
      } while (System.currentTimeMillis() - lStartTime <= aTimeout && !this.mCaseInSensor.on());

      if (!this.mCaseInSensor.on()) {
         throw new ServoException("Disc not in slot.  intakeDisc() failed");
      } else {
         try {
            Roller.stop();
            Door.close();
         } catch (ServoException var6) {
            if (!this.clearDisc(false)) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, "ServoException caught: " + var6.getMessage());
            }
         }
      }
   }

   public void readBarCode(BarCode aBarCode) throws ServoException {
      aBarCode.mCode1 = "";
      aBarCode.mCode2 = "";
      String lRet = BarCodeReader.readBarCode(aBarCode, Servo.mBarCodeAngles, false, "");
      if (aBarCode.mCode1 == "" || aBarCode.mCode2 == "") {
         if (lRet == null) {
            throw new ServoException("Barcode was not read.");
         } else {
            throw new ServoException(lRet);
         }
      }
   }

   public boolean isDiscJammed() {
      return !Door.isLocked();
   }

   public boolean isDiscAtDoor() {
      return this.mCaseSensor2.on() && !this.mCaseSensor1.on();
   }

   public boolean isDiscTaken() {
      return !this.mCaseSensor2.on() && !this.mCaseSensor1.on();
   }

   public boolean isDiscInSlot() {
      return this.mCaseInSensor.on();
   }

   public void logServoStatus() {
      try {
         String lPos = Carousel.getPosition();
         this.closeServo();
         Aem.logDetailMessage(DvdplayLevel.INFO, "Modules Found: " + this.openServo());
         Aem.logDetailMessage(DvdplayLevel.INFO, "Carousel Position: " + lPos);
         if (this.mCaseInSensor.on()) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "CaseIn: Yes");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "CaseIn: No");
         }

         Level var10000 = DvdplayLevel.INFO;
         StringBuffer var10001 = new StringBuffer().append("Bardcode Illumination: ");
         this.getLight();
         Aem.logDetailMessage(var10000, var10001.append(Light.getLevel()).toString());
         if (Door.isLocked()) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Locked: Yes");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Locked: No");
         }

         if (Door.getPosition() == "OPEN") {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Open: Yes");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Open: No");
         }

         if (Door.getPosition() == "CLOSE") {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Close: Yes");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Close: No");
         }

         if (Door.getPosition() == "CLAMP") {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Clamped: Yes");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Door Clamped: No");
         }

         if (Arm.getPosition() == "EJECT") {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Arm Eject: Yes");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Arm Eject: No");
         }

         if (Arm.getPosition() == "CLEAR") {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Arm Clear: Yes");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Arm Clear: No");
         }

         if (this.mCaseSensor1.on()) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Case Sensor1: Blocked");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Case Sensor1: Clear");
         }

         if (this.mCaseSensor2.on()) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Case Sensor2: Blocked");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Case Sensor2: Clear");
         }

         if (this.mInputSensor.on()) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Input Sensor: Blocked");
         } else {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Input Sensor: Clear");
         }
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage());
      }
   }

   public void initialize() throws ServoException {
      int lMaxRetries = 3;
      int lNumRetry = 0;
      boolean lIsInitialized = false;
      lNumRetry = 0;

      do {
         try {
            Aem.logSummaryMessage("Initializing Servo attempt " + ++lNumRetry);
            Door.initialize();
            Arm.initialize();
            Carousel.initialize();
            lIsInitialized = true;
         } catch (DoorException var6) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "DoorException caught: " + var6.getMessage());
            if (!this.reseatDisc()) {
               this.clearDisc(false);
            }
         } catch (ServoException var7) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var7.getMessage());
         }
      } while (!lIsInitialized && lNumRetry < 3);

      if (!lIsInitialized) {
         throw new ServoException("Servo initialize failed.");
      }
   }

   public ServoEx() {
      Servo.mPollPIOBitsSleepTime = 100;
      Servo.mServoHomePosition = 0;
      this.readServoProperties();
      this.mArm = new Arm();
      this.mDoor = new Door();
      this.mCarousel = new Carousel();
      this.mLight = new Light();
      this.mRoller = new Roller();
      this.mCaseSensor1 = new Sensor((byte)3, 10, true);
      this.mCaseSensor2 = new Sensor((byte)3, 11, true);
      this.mCaseInSensor = new Sensor((byte)2, 5, !this.mCaseInSensorIsReflective);
      this.mInputSensor = new Sensor((byte)2, 7, true);
      this.setNumSlots(102);
   }

   public Arm getArm() {
      return this.mArm;
   }

   public Door getDoor() {
      return this.mDoor;
   }

   public Roller getRoller() {
      return this.mRoller;
   }

   public Sensor getCaseSensor1() {
      return this.mCaseSensor1;
   }

   public Sensor getCaseSensor2() {
      return this.mCaseSensor2;
   }

   public Sensor getCaseInSensor() {
      return this.mCaseInSensor;
   }

   public Sensor getInputSensor() {
      return this.mInputSensor;
   }

   public Carousel getCarousel() {
      return this.mCarousel;
   }

   public Light getLight() {
      return this.mLight;
   }

   public int prepareExit(int aMode) {
      this.closeServo();
      switch (aMode) {
         case 1:
            return aMode;
         case 2:
         case 3:
         default:
            return 2;
      }
   }

   public void releaseDisc() {
      this.getRoller();
      Roller.stop();
      this.getDoor();
      Door.open();
      this.getArm();
      Arm.clear();
   }
}
