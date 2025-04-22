package net.dvdplay.aem;

public class Servo {
   public Light mLight = null;
   protected int mNumSlots = 102;
   protected final int mSlotsPerUnit = 17;
   protected int mNumUnits = 6;
   protected int mNumTicks = 17408;
   public static int mPollPIOBitsSleepTime;
   public static int mServoHomePosition;
   public static final int EJECT_FOR_INSPECT = 0;
   public static final int EJECT_FOR_RENTAL = 1;
   public static final int EJECT_FOR_REMOVAL = 2;
   public static final int INTERNAL_TEMP = 4;
   public static final int EXTERNAL_TEMP = 5;
   public static final int CPU_TEMP = 6;
   public static final int LCD_TEMP = 7;
   public static String mBarCodeAngles = "0,30,60,90,120,150,180,210,240,270,300,330,15,45,75,105,135,165,195,225,255,285,315,345";

   public void initServo() {
   }

   public void closeServo() {
   }

   public void initialize() {
   }

   public void refreshPorts() {
   }

   public boolean isInitialized() {
      return true;
   }

   public boolean isOverThreshold() {
      return true;
   }

   public void releaseDisc() {
   }

   public void ejectDisc(int action) {
   }

   public void intakeDisc(int aTimeout, boolean forInspection) {
   }

   public boolean reseatDisc() {
      return true;
   }

   public boolean clearDisc(boolean aDoNotTimeOut) {
      return true;
   }

   public boolean isDiscInSlot() {
      return true;
   }

   public boolean isDiscJammed() {
      return true;
   }

   public boolean isDiscTaken() {
      return true;
   }

   public boolean isDiscAtDoor() {
      return true;
   }

   public int prepareExit(int aMode) {
      return aMode;
   }

   public int goToSlot(int aSlotNum) {
      return 0;
   }

   public int getTemperature(int aLocation) {
      return -9999;
   }

   public void logTemperatures() {
   }

   public int calculateBayNum(int aSlotNum) {
      return 0;
   }

   public int calculateBaySlot(int aSlotNum) {
      return 0;
   }

   public int calculateOffset(int aSlotNum) {
      return 0;
   }

   public int getSlotOffset(int aSlotNum) {
      return 0;
   }

   public void setSlotOffset(int aSlotNum, int aOffset) {
   }

   public void readServoProperties() {
   }

   public boolean getServoModuleStatus() {
      return true;
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
      return true;
   }

   public boolean setServoParameters() {
      return true;
   }

   public void enableMotors() {
   }

   public void disableMotors() {
   }

   public boolean isLcdPowerOn() {
      return true;
   }

   public void lcdOn() {
   }

   public void lcdOff() {
   }

   public boolean isCaseInSensorReflective() {
      return true;
   }

   public void logServoStatus() {
   }

   public void readBarCode(BarCode aBarCode) throws ServoException {
      aBarCode.mCode1 = "";
      aBarCode.mCode2 = "";
      String lRet = BarCodeReader.readBarCode(aBarCode, mBarCodeAngles, false, "");
      if (aBarCode.mCode1 == "" || aBarCode.mCode2 == "") {
         if (lRet == null) {
            throw new ServoException("Barcode was not read.");
         } else {
            throw new ServoException(lRet);
         }
      }
   }

   public int getArmEjectWaitTime() {
      return 0;
   }

   public int getMoveToOffsetWaitTime() {
      return 0;
   }

   public int getMoveToOffsetTimeOut() {
      return 0;
   }

   public void setArmEjectWaitTime(int i) {
   }

   public void setMoveToOffsetWaitTime(int i) {
   }

   public void setMoveToOffsetTimeOut(int i) {
   }

   public int getServoOffset() {
      return 0;
   }

   public int getServoInputStep() {
      return 0;
   }

   public int getServoOutputStep() {
      return 0;
   }

   public int getServoDiscThreshold() {
      return 0;
   }

   public int getServoKp() {
      return 0;
   }

   public int getServoKp2() {
      return 0;
   }

   public int getServoKd() {
      return 0;
   }

   public int getServoKd2() {
      return 0;
   }

   public int getServoKi() {
      return 0;
   }

   public int getServoKi2() {
      return 0;
   }

   public int getServoIntegrationLimit() {
      return 0;
   }

   public int getServoIntegrationLimit2() {
      return 0;
   }

   public int getServoOutputLimit() {
      return 0;
   }

   public int getServoOutputLimit2() {
      return 0;
   }

   public int getServoCurrentLimit() {
      return 0;
   }

   public int getServoCurrentLimit2() {
      return 0;
   }

   public int getServoPositionErrorLimit() {
      return 0;
   }

   public int getServoPositionErrorLimit2() {
      return 0;
   }

   public int getServoServoRate() {
      return 0;
   }

   public int getServoServoRate2() {
      return 0;
   }

   public int getServoDeadbandComp() {
      return 0;
   }

   public int getServoDeadbandComp2() {
      return 0;
   }

   public int getServoVelocity() {
      return 0;
   }

   public int getServoVelocity2() {
      return 0;
   }

   public int getServoAcceleration() {
      return 0;
   }

   public int getServoAcceleration2() {
      return 0;
   }

   public void setServoOffset(int i) {
   }

   public void setServoInputStep(int i) {
   }

   public void setServoOutputStep(int i) {
   }

   public void setServoDiscThreshold(int i) {
   }

   public void setServoKp(int i) {
   }

   public void setServoKd(int i) {
   }

   public void setServoKi(int i) {
   }

   public void setServoIntegrationLimit(int i) {
   }

   public void setServoOutputLimit(int i) {
   }

   public void setServoCurrentLimit(int i) {
   }

   public void setServoPositionErrorLimit(int i) {
   }

   public void setServoServoRate(int i) {
   }

   public void setServoDeadbandComp(int i) {
   }

   public void setServoVelocity(int i) {
   }

   public void setServoAcceleration(int i) {
   }

   public void setServoKp2(int i) {
   }

   public void setServoKd2(int i) {
   }

   public void setServoKi2(int i) {
   }

   public void setServoIntegrationLimit2(int i) {
   }

   public void setServoOutputLimit2(int i) {
   }

   public void setServoCurrentLimit2(int i) {
   }

   public void setServoPositionErrorLimit2(int i) {
   }

   public void setServoServoRate2(int i) {
   }

   public void setServoDeadbandComp2(int i) {
   }

   public void setServoVelocity2(int i) {
   }

   public void setServoAcceleration2(int i) {
   }

   public synchronized int getNumSlots() {
      return this.mNumSlots;
   }

   protected synchronized void setNumSlots(int aSlots) {
      this.mNumSlots = aSlots;
   }

   public synchronized int getSlotsPerUnit() {
      return 17;
   }

   public synchronized int getNumUnits() {
      return this.mNumUnits;
   }

   protected synchronized void setNumUnits(int aUnits) {
      this.mNumUnits = aUnits;
   }

   public int getNumTicks() {
      return this.mNumTicks;
   }

   protected void setNumTicks(int aTicks) {
      this.mNumTicks = aTicks;
   }

   public Roller getRoller() {
      return null;
   }

   public void getTaggedInfo(int tagType) {
   }

   public void pushCaseOutToInspect() {
   }

   public void acceptCaseToInspect() {
   }

   public void pullCaseInFromInspect() {
   }

   public void ejectCaseFromInspect() {
   }

   public void ejectCaseFromSlot() {
   }

   public void pullCaseIntoSlot() {
   }

   public void rebootHost() {
   }

   public int startAppWatchdog() {
      return 0;
   }

   public int strokeAppWatchdog() {
      return this.strokeAppWatchdog(false);
   }

   public int strokeAppWatchdog(boolean aLog) {
      return 0;
   }

   public int killAppWatchdog() {
      return 0;
   }

   public Carousel getCarousel() {
      return null;
   }

   public void setEjectCaseStatus(int status) {
   }

   public String resultAnnouncer(int aResult) {
      return " ";
   }

   public String opCodeAnnouncer(int OpCode) {
      return " ";
   }

   public String tagTypeAnnouncer(int tagType) {
      return " ";
   }
}
