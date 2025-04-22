package net.dvdplay.aem;

import net.dvdplay.logger.DvdplayLevel;

public class ServoPlus extends Servo {
   private boolean mDpecInitialized = false;
   private static int mKeyCounter = 0;
   private Carousel mCarousel;
   private int mEjectCaseStatus = 0;

   public void initServo() {
      String lClassName = "net/dvdplay/aem/ServoCallback";
      String lExceptionClassName = "net/dvdplay/aem/ServoException";
      String lRegForAsynEventsName = "eventDispatcher";
      String lCommonFunctionName = "callbackCommonFunction";
      String lGetTaggedInfoName = "callbackGetTaggedInfo";
      String lGetStatusQuickName = "callbackGetStatusQuick";
      String lRotateToSlotName = "callbackRotateToSlot";
      String lEjectCaseFromSlotName = "callbackEjectCaseFromSlot";
      String lVersionMajor = "mVersionMajor";
      String lVersionMinor = "mVersionMinor";
      String lVersionBuild = "mVersionBuild";
      String lVersionString = "mVersionString";
      if (!this.mDpecInitialized) {
         try {
            DPEC.SetGlobalRef(
               lClassName,
               lExceptionClassName,
               lRegForAsynEventsName,
               lCommonFunctionName,
               lGetTaggedInfoName,
               lGetStatusQuickName,
               lRotateToSlotName,
               lEjectCaseFromSlotName,
               lVersionMajor,
               lVersionMinor,
               lVersionBuild,
               lVersionString
            );
            int cbRegEvtKey = 931;
            DPEC.RegForAsyncEvents(cbRegEvtKey);
            this.mDpecInitialized = true;
         } catch (Exception var15) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "ServoPlus initServo exception: " + var15.getMessage(), var15);
            throw new ServoException("ServoPlus initServo Failed");
         } catch (Error var16) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "ServoPlus initServo exception: " + var16.getMessage(), var16);
            throw new ServoException("ServoPlus initServo Failed");
         }
      }
   }

   public void closeServo() {
      DPEC.ReleaseGlobalRef();
      DPEC.Disconnect();
   }

   public void initialize() {
      this.lcdOn();
      int lMaxRetries = 3;
      int lNumRetry = 0;
      boolean lIsInitialized = false;
      lNumRetry = 0;

      do {
         Aem.logSummaryMessage("Initializing ServoPlus attempt " + ++lNumRetry);
         int lResult = DPEC.SetMode(2);
         if (lResult != 0) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "ServoPlus setMode returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult);
         } else {
            lIsInitialized = true;
         }
      } while (!lIsInitialized && lNumRetry < 3);

      if (!lIsInitialized) {
         throw new ServoException("ServoPlus initialize unsuccessful.");
      }
   }

   public void logServoStatus() {
      this.logTaggedInfo(12);
      this.logTaggedInfo(14);
      this.logTaggedInfo(15);
      this.logTaggedInfo(16);
      this.logTaggedInfo(18);
   }

   public void logTaggedInfo(int tagType) {
      this.getTaggedInfo(tagType);
      Aem.logDetailMessage(DvdplayLevel.INFO, this.tagTypeAnnouncer(tagType) + ": " + ServoCallbackFactory.getServoCallbackObject(4).getTaggedInfoString());
   }

   public void getTaggedInfo(int tagType) {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 4, tagType, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(4).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.WARNING, this.opCodeAnnouncer(4) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
         );
         throw new ServoException("ServoPlus getTaggedInfo failed!");
      }
   }

   public void acceptCaseToInspect() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 8, 0, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(8).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.WARNING, this.opCodeAnnouncer(8) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
         );
         throw new ServoException("ServoPlus acceptCaseToInspect failed!");
      }
   }

   public void ejectCaseFromInspect() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 9, 0, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(9).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.WARNING, this.opCodeAnnouncer(9) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
         );
         throw new ServoException("ServoPlus ejectCaseFromInspect failed!");
      }
   }

   public void pullCaseInFromInspect() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 10, 0, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(10).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.WARNING, this.opCodeAnnouncer(10) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
         );
         throw new ServoException("ServoPlus pullCaseInFromInspect failed!");
      }
   }

   public void pushCaseOutToInspect() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 11, 0, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(11).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.WARNING, this.opCodeAnnouncer(11) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
         );
         throw new ServoException("ServoPlus pushCaseOutToInspect failed!");
      }
   }

   public int goToSlot(int aSlotNum) {
      Aem.logDetailMessage(DvdplayLevel.INFO, "Going to slot " + aSlotNum);
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 12, aSlotNum, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(12).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.WARNING, this.opCodeAnnouncer(12) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
         );
         throw new ServoException("ServoPlus goToSlot failed!");
      } else {
         return ServoCallbackFactory.getServoCallbackObject(12).getSlotStat();
      }
   }

   public void pullCaseIntoSlot() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 13, 0, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(13).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         if (lResult != 23) {
            Aem.logDetailMessage(
               DvdplayLevel.WARNING, this.opCodeAnnouncer(13) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
            );
            throw new ServoException("ServoPlus pullCaseIntoSlot failed!");
         }

         Aem.logDetailMessage(DvdplayLevel.INFO, this.opCodeAnnouncer(13) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult);
      }
   }

   public void ejectCaseFromSlot() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 14, 0, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(14).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         if (lResult != 22) {
            Aem.logDetailMessage(
               DvdplayLevel.WARNING, this.opCodeAnnouncer(13) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
            );
            throw new ServoException("ServoPlus ejectCaseFromSlot failed!");
         }

         Aem.logDetailMessage(DvdplayLevel.INFO, this.opCodeAnnouncer(13) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult);
      }
   }

   public void spitCaseFromSlot() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 14, 1, 0);
      int lResult = ServoCallbackFactory.getServoCallbackObject(14).waitForCallback(lOpID, lKey);
      if (lResult != 0) {
         if (lResult != 22) {
            Aem.logDetailMessage(
               DvdplayLevel.WARNING, this.opCodeAnnouncer(13) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult
            );
            throw new ServoException("ServoPlus ejectCaseFromSlot failed!");
         }

         Aem.logDetailMessage(DvdplayLevel.INFO, this.opCodeAnnouncer(13) + " Callback returned " + this.resultAnnouncer(lResult) + "! ResultCode = " + lResult);
      }
   }

   public void rebootHost() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 5, 3, 0);
      ServoCallbackFactory.getServoCallbackObject(5).waitForCallback(lOpID, lKey);
   }

   public ServoPlus() {
      this.mLight = new Light() {
         private final ServoPlus this$0;

         {
            this.this$0 = ServoPlus.this;
         }

         @Override
         public void on() {
            int lKey = this.this$0.getKey();
            int lOpID = DPEC.StartOperation(lKey, 5, 2, 1);
            ServoCallbackFactory.getServoCallbackObject(5).waitForCallback(lOpID, lKey);
         }

         @Override
         public void off() {
            int lKey = this.this$0.getKey();
            int lOpID = DPEC.StartOperation(lKey, 5, 2, 0);
            ServoCallbackFactory.getServoCallbackObject(5).waitForCallback(lOpID, lKey);
         }

         @Override
         public void setLevel(int aLevel) {
         }
      };

      this.mCarousel = new Carousel() {
         @Override
         public boolean isCarouselAtSlot(int aSlot) {
            return true;
         }
      };
   }

   public Carousel getCarousel() {
      return this.mCarousel;
   }

   public void ejectDisc(int action) {
      switch (action) {
         case 0:
            this.pushCaseOutToInspect();
            break;
         case 1:
            this.ejectCaseFromSlot();
            break;
         case 2:
            this.spitCaseFromSlot();
      }
   }

   public void intakeDisc(int aTimeout, boolean forInspection) {
      if (forInspection) {
         this.pullCaseInFromInspect();
      } else {
         this.pullCaseIntoSlot();
      }
   }

   public int startAppWatchdog() {
      int lResult = DPEC.StartAppWatchdog();
      if (lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.INFO, "ServoPlus StartAppWatchdog failed! Result = " + String.valueOf(lResult) + ", " + this.resultAnnouncer(lResult)
         );
      }

      return lResult;
   }

   public int strokeAppWatchdog() {
      return this.strokeAppWatchdog(false);
   }

   public int strokeAppWatchdog(boolean aLog) {
      if (aLog) {
         Aem.logDetailMessage(DvdplayLevel.INFO, "ServoPlus StrokeAppWatchdog...");
      }

      int lResult = DPEC.StrokeAppWatchdog();
      if (aLog || lResult != 0) {
         Aem.logDetailMessage(
            DvdplayLevel.INFO, "ServoPlus StrokeAppWatchdog failed! Result = " + String.valueOf(lResult) + ", " + this.resultAnnouncer(lResult)
         );
      }

      return lResult;
   }

   public int killAppWatchdog() {
      Aem.logSummaryMessage("Killing App Watchdog");
      int lResult = DPEC.KillAppWatchdog();
      if (lResult != 0) {
         Aem.logDetailMessage(DvdplayLevel.INFO, "ServoPlus KillAppWatchdog failed! Result = " + String.valueOf(lResult) + ", " + this.resultAnnouncer(lResult));
      }

      return lResult;
   }

   public boolean isDiscInSlot() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 6, 3, 0);
      ServoCallbackFactory.getServoCallbackObject(6).waitForCallback(lOpID, lKey);
      return ServoCallbackFactory.getServoCallbackObject(6).getQuickStat() == 1;
   }

   public boolean isDiscTaken() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 6, 1, 0);
      ServoCallbackFactory.getServoCallbackObject(6).waitForCallback(lOpID, lKey);
      return ServoCallbackFactory.getServoCallbackObject(6).getQuickStat() == 0;
   }

   public boolean isDiscAtDoor() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 6, 2, 0);
      ServoCallbackFactory.getServoCallbackObject(6).waitForCallback(lOpID, lKey);
      return ServoCallbackFactory.getServoCallbackObject(6).getQuickStat() == 1;
   }

   public boolean isDiscJammed() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 6, 9, 0);
      ServoCallbackFactory.getServoCallbackObject(6).waitForCallback(lOpID, lKey);
      return ServoCallbackFactory.getServoCallbackObject(6).getQuickStat() != 1;
   }

   public boolean clearDisc(boolean aDoNotTimeOut) {
      return !this.isDiscJammed();
   }

   public boolean reseatDisc() {
      return !this.isDiscJammed();
   }

   public void lcdOn() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 5, 1, 1);
      ServoCallbackFactory.getServoCallbackObject(5).waitForCallback(lOpID, lKey);
   }

   public void lcdOff() {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 5, 1, 0);
      ServoCallbackFactory.getServoCallbackObject(5).waitForCallback(lOpID, lKey);
   }

   public int getTemperature(int aLocation) {
      int lKey = this.getKey();
      int lOpID = DPEC.StartOperation(lKey, 6, aLocation, 0);
      ServoCallbackFactory.getServoCallbackObject(6).waitForCallback(lOpID, lKey);
      return ServoCallbackFactory.getServoCallbackObject(6).getQuickStat();
   }

   public void logTemperatures() {
      Aem.logSummaryMessage("Internal Temp: " + this.getTemperature(4) + "°C");
      Aem.logSummaryMessage("External Temp: " + this.getTemperature(5) + "°C");
      Aem.logSummaryMessage("CPU Temp: " + this.getTemperature(6) + "°C");
      Aem.logSummaryMessage("LCD Temp: " + this.getTemperature(7) + "°C");
   }

   public void setEjectCaseStatus(int status) {
      this.mEjectCaseStatus = status;
   }

   public int getEjectCaseStatus() {
      return this.mEjectCaseStatus;
   }

   public boolean isInitialized() {
      return this.mDpecInitialized;
   }

   private int getKey() {
      return ++mKeyCounter;
   }

   public String tagTypeAnnouncer(int tagType) {
      String lTagString;
      switch (tagType) {
         case 0:
            lTagString = "Unknown";
            break;
         case 1:
            lTagString = "CntrCapList";
            break;
         case 2:
            lTagString = "CntrHwVers";
            break;
         case 3:
            lTagString = "CntrPlVers";
            break;
         case 4:
            lTagString = "CntrFwVers";
            break;
         case 5:
            lTagString = "CntrSerNum";
            break;
         case 6:
            lTagString = "CntrStatus";
            break;
         case 7:
            lTagString = "100P_TempFan1";
            break;
         case 8:
            lTagString = "100P_TempFan2";
            break;
         case 9:
            lTagString = "100P_TempFan3";
            break;
         case 10:
            lTagString = "100P_TempFan4";
            break;
         case 11:
            lTagString = "100P_Power";
            break;
         case 12:
            lTagString = "100P_Servo";
            break;
         case 13:
            lTagString = "JRKerr_Params";
            break;
         case 14:
            lTagString = "100P_Door";
            break;
         case 15:
            lTagString = "100P_Arm";
            break;
         case 16:
            lTagString = "100P_Roller";
            break;
         case 17:
            lTagString = "100P_MiscRbt";
            break;
         case 18:
            lTagString = "100P_LED";
            break;
         default:
            lTagString = "Undefined TagType!";
      }

      return lTagString;
   }

   public String resultAnnouncer(int res) {
      String lReason;
      switch (res) {
         case 0:
            lReason = "Success";
            break;
         case 1:
            lReason = "Failed";
            break;
         case 2:
            lReason = "Aborted";
            break;
         case 3:
            lReason = "Unknown";
            break;
         case 4:
            lReason = "Timeout";
            break;
         case 5:
            lReason = "NoConnect";
            break;
         case 6:
            lReason = "Connected";
            break;
         case 7:
            lReason = "NoResource";
            break;
         case 8:
            lReason = "CantComplete";
            break;
         case 9:
            lReason = "SrcEmpty";
            break;
         case 10:
            lReason = "DstOccupied";
            break;
         case 11:
            lReason = "SlotEmpty";
            break;
         case 12:
            lReason = "SlotFull";
            break;
         case 13:
            lReason = "BadValue";
            break;
         case 14:
            lReason = "InvalidSlot";
            break;
         case 15:
            lReason = "InvalidOp";
            break;
         case 16:
            lReason = "WrongMode";
            break;
         case 17:
            lReason = "WrongState";
            break;
         case 18:
            lReason = "WrongModel";
            break;
         case 19:
            lReason = "DiscCleared";
            break;
         case 20:
            lReason = "DiscJammed";
            break;
         case 21:
            lReason = "DiscReseated";
            break;
         case 22:
            lReason = "DiscYankedOut";
            break;
         case 23:
            lReason = "DiscShovedIn";
            break;
         default:
            lReason = "Undefined result code!";
      }

      return lReason;
   }

   public String opCodeAnnouncer(int OpCode) {
      String lOpString;
      switch (OpCode) {
         case 0:
            lOpString = "None";
            break;
         case 1:
            lOpString = "NOP_Async";
            break;
         case 2:
            lOpString = "NOP_Quick";
            break;
         case 3:
            lOpString = "NOP_Robot";
            break;
         case 4:
            lOpString = "GetTaggedInfo";
            break;
         case 5:
            lOpString = "SetControlQuick";
            break;
         case 6:
            lOpString = "GetStatusQuick";
            break;
         case 7:
            lOpString = "RapidSlotCheck";
            break;
         case 8:
            lOpString = "AcceptCaseToInspect";
            break;
         case 9:
            lOpString = "EjectCaseFromInspect";
            break;
         case 10:
            lOpString = "PullCaseInFromInspect";
            break;
         case 11:
            lOpString = "PushCaseOutToInspect";
            break;
         case 12:
            lOpString = "RotateToSlot";
            break;
         case 13:
            lOpString = "PullCaseIntoSlot";
            break;
         case 14:
            lOpString = "EjectCaseFromSlot";
            break;
         default:
            lOpString = "Undefined OpCode!";
      }

      return lOpString;
   }

   public int prepareExit(int aMode) {
      switch (aMode) {
         case 1:
            this.killAppWatchdog();
            this.closeServo();
            return aMode;
         case 2:
            this.rebootHost();
         case 3:
            this.lcdOff();
            this.closeServo();
            return 3;
         default:
            this.lcdOff();
            this.rebootHost();
            this.closeServo();
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Invalid exit mode: " + aMode);
            return 3;
      }
   }
}
