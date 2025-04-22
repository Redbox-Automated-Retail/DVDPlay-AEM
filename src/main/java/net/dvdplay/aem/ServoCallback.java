package net.dvdplay.aem;

import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class ServoCallback {
   private int mOpID = 0;
   private int mKey = 0;
   private int mReturnOpID = 0;
   private int mReturnKey = 0;
   private int mReturnRes = 0;
   private int mSlotStat = 0;
   private int mQuickStat = 0;
   private int mOpStat = 0;
   private static byte mVersionMajor = 0;
   private static byte mVersionMinor = 0;
   private static short mVersionBuild = 0;
   private static String mVersionString = "apple";
   private static String mTaggedInfo = "";

   public static void eventDispatcher(int key, int event) {
      Servo lServo = ServoFactory.getInstance();
      switch (event) {
         case 5:
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Event: High Temperature Alert!");
            new Thread(lServo::logTemperatures).start();
            Aem.setTemperatureError();
            break;
         case 6:
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Event: Low Temperature Alert!");
            new Thread(lServo::logTemperatures).start();
            Aem.setTemperatureError();
            break;
         case 7:
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Event: Power Supply Alert");
            break;
         case 8:
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Event: Watchdog Timer Alert");
            break;
         case 9:
            Aem.logDetailMessage(DvdplayLevel.ERROR, "Event: High Temperature Fault!");
            new Thread() {
               @Override
               public void run() {
                  lServo.logTemperatures();
               }
            }.start();
            Aem.setTemperatureError();
            break;
         case 10:
            Aem.logDetailMessage(DvdplayLevel.ERROR, "Event: Low Temperature Fault!");
            new Thread() {
               @Override
               public void run() {
                  lServo.logTemperatures();
               }
            }.start();
            Aem.setTemperatureError();
            break;
         case 11:
            Aem.logDetailMessage(DvdplayLevel.ERROR, "Event: Power Supply Fault");
            Aem.setPowerError();
            AemFactory.getInstance();
            Aem.setQuiesceMode(3);
            break;
         case 12:
            Aem.logDetailMessage(DvdplayLevel.ERROR, "Event: Watchdog Timer Fault");
            AemFactory.getInstance();
            Aem.setQuiesceMode(3);
            break;
         case 13:
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Event: Intrusion Switch Open!");
            break;
         case 14:
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Event: Intrusion Switch Close!");
      }
   }

   private static int searchQueueForOpCode(int aOpID) {
      ServoCallback sc = null;

      for (int i = 0; i < 15; i++) {
         sc = ServoCallbackFactory.getServoCallbackObject(i);
         if (sc.getOpID() == aOpID) {
            return i;
         }
      }

      Aem.logDetailMessage(DvdplayLevel.WARNING, "can not locate ServoCallback Object from the queue!");
      return 0;
   }

   public static void callbackCommonFunction(int key, int OpID, int res) {
      try {
         int opCode = searchQueueForOpCode(OpID);
         ServoCallback sc = ServoCallbackFactory.getServoCallbackObject(opCode);
         sc.setReturnedKey(key);
         sc.setReturnedRes(res);
         sc.setReturnedOpID(OpID);
      } catch (Throwable var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var5.getMessage(), var5);
      }
   }

   public static void callbackEjectCaseFromSlot(int key, int OpID, int res) {
      try {
         ServoCallback sc = ServoCallbackFactory.getServoCallbackObject(14);
         ServoFactory.getInstance().setEjectCaseStatus(res);
         sc.setReturnedKey(key);
         sc.setReturnedRes(res);
         sc.setReturnedOpID(OpID);
      } catch (Throwable var4) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var4.getMessage(), var4);
      }
   }

   public static void callbackGetStatusQuick(int key, int OpID, int res, int quickStat) {
      try {
         ServoCallback sc = ServoCallbackFactory.getServoCallbackObject(6);
         sc.setQuickStat(quickStat);
         sc.setReturnedKey(key);
         sc.setReturnedRes(res);
         sc.setReturnedOpID(OpID);
      } catch (Throwable var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var5.getMessage(), var5);
      }
   }

   public static void callbackRotateToSlot(int key, int OpID, int res, int slotStat) {
      try {
         ServoCallback sc = ServoCallbackFactory.getServoCallbackObject(12);
         sc.setSlotStat(slotStat);
         sc.setReturnedKey(key);
         sc.setReturnedRes(res);
         sc.setReturnedOpID(OpID);
      } catch (Throwable var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var5.getMessage(), var5);
      }
   }

   public static void callbackGetTaggedInfo(int key, int OpID, int res, String strStatus) {
      try {
         ServoCallback sc = ServoCallbackFactory.getServoCallbackObject(4);
         mTaggedInfo = strStatus;
         sc.setReturnedKey(key);
         sc.setReturnedRes(res);
         sc.setReturnedOpID(OpID);
      } catch (Throwable var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var5.getMessage(), var5);
      }
   }

   public int waitForCallback(int aOpID, int aKey) {
      this.setOpID(aOpID);
      this.setKey(aKey);

      while (this.mReturnOpID != aOpID) {
         Util.sleep(500);
      }

      return this.mReturnRes;
   }

   public int getOpStat() {
      return this.mOpStat;
   }

   public int getOpID() {
      return this.mOpID;
   }

   public int getVersionMajor() {
      return mVersionMajor;
   }

   public int getVersionMinor() {
      return mVersionMinor;
   }

   public int getVersionBuild() {
      return mVersionBuild;
   }

   public String getVersionString() {
      return mVersionString;
   }

   public int getReturnedOpID() {
      return this.mReturnOpID;
   }

   public int getReturnedKey() {
      return this.mReturnKey;
   }

   public int getReturnedRes() {
      return this.mReturnRes;
   }

   public int getSlotStat() {
      return this.mSlotStat;
   }

   public int getQuickStat() {
      return this.mQuickStat;
   }

   public String getTaggedInfoString() {
      return mTaggedInfo;
   }

   private void setOpID(int aOpID) {
      this.mOpID = aOpID;
   }

   private void setKey(int aKey) {
      this.mKey = aKey;
   }

   private void setReturnedOpID(int aOpID) {
      this.mReturnOpID = aOpID;
   }

   private void setReturnedKey(int aKey) {
      this.mReturnKey = aKey;
   }

   private void setReturnedRes(int aResult) {
      this.mReturnRes = aResult;
   }

   private void setQuickStat(int aQuickStat) {
      this.mQuickStat = aQuickStat;
   }

   private void setSlotStat(int aSlotStat) {
      this.mSlotStat = aSlotStat;
   }
}
