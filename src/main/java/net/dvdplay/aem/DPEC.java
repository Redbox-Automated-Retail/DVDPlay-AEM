package net.dvdplay.aem;

public class DPEC {
   public static final int MODEL_UNKNOWN = 0;
   public static final int MODEL_A100 = 1;
   public static final int MODEL_A100P = 2;
   public static final int MODEL_A350 = 3;
   public static final int MODE_UNKNOWN = 0;
   public static final int MODE_TRANSITION = 1;
   public static final int MODE_OPERATIONAL = 2;
   public static final int MODE_DIAGNOSTIC = 3;
   public static final int SLOTSTAT_UNKNOWN = 0;
   public static final int SLOTSTAT_EMPTY = 1;
   public static final int SLOTSTAT_FULL = 2;
   public static final int SLOTSTAT_BAD = 3;
   public static final int OP_NONE = 0;
   public static final int OP_NOP_ASYNC = 1;
   public static final int OP_NOP_QUICK = 2;
   public static final int OP_NOP_ROBOT = 3;
   public static final int OP_GETTAGGEDINFO = 4;
   public static final int OP_SETCONTROLQUICK = 5;
   public static final int OP_GETSTATUSQUICK = 6;
   public static final int OP_RAPIDSLOTCHECK = 7;
   public static final int OP_ACCEPTCASETOINSPECT = 8;
   public static final int OP_EJECTCASEFROMINSPECT = 9;
   public static final int OP_PULLCASEINFROMINSPECT = 10;
   public static final int OP_PUSHCASEOUTTOINSPECT = 11;
   public static final int OP_ROTATETOSLOT = 12;
   public static final int OP_PULLCASEINTOSLOT = 13;
   public static final int OP_EJECTCASEFROMSLOT = 14;
   public static final int OP_PULLCASETOINSPECT = 15;
   public static final int OP_PUSHCASEFROMINSPECT = 16;
   public static final int OP_MOVEINSPECTTOSLOT = 17;
   public static final int OP_MOVESLOTTOINSPECT = 18;
   public static final int OP_MOVESLOTTOOUTPUT = 19;
   public static final int OP_MOVESLOTTOSLOT = 20;
   public static final int OP_CHECKSLOTFORCASE = 21;
   public static final int Op_UPDATEFIRMWARE = 22;
   public static final int Op_CALIBRATE = 23;
   public static final int Op_DIAG100P_TBD = 24;
   public static final int Op_DIAG350_TBD = 25;
   public static final int CONTROLITEM_NONE = 0;
   public static final int CONTROLITEM_MAINMONITOR = 1;
   public static final int CONTROLITEM_BARCODELIGHT = 2;
   public static final int CONTROLITEM_REBOOTHOST = 3;
   public static final int OFF = 0;
   public static final int ON = 1;
   public static final int NO = 0;
   public static final int YES = 1;
   public static final int STATUSITEM_NONE = 0;
   public static final int STATUSITEM_CASEINENTRY = 1;
   public static final int STATUSITEM_VALIDCASE = 2;
   public static final int STATUSITEM_CASEINSLOT = 3;
   public static final int STATUSITEM_INTAMPTEMP = 4;
   public static final int STATUSITEM_EXTAMPTEMP = 5;
   public static final int STATUSITEM_CPUAMPTEMP = 6;
   public static final int STATUSITEM_LCDAMPTEMP = 7;
   public static final int STATUSITEM_PWRMONSTAT = 8;
   public static final int STATUSITEM_DOORLOCKED = 9;
   public static final int EVENT_NONE = 0;
   public static final int EVENT_ENTRYFILLED = 1;
   public static final int EVENT_ENTRYEMPTY = 2;
   public static final int EVENT_VALIDCASE = 3;
   public static final int EVENT_NOVALIDCASE = 4;
   public static final int EVENT_HITEMPALERT = 5;
   public static final int EVENT_LOTEMPALERT = 6;
   public static final int EVENT_POWERALERT = 7;
   public static final int EVENT_APPWDTALERT = 8;
   public static final int EVENT_HITEMPFAULT = 9;
   public static final int EVENT_LOTEMPFAULT = 10;
   public static final int EVENT_POWERFAULT = 11;
   public static final int EVENT_APPWDTFAULT = 12;
   public static final int EVENT_INTRUSWOPEN = 13;
   public static final int EVENT_INTRUSWCLOSE = 14;
   public static final int RESULT_SUCCESS = 0;
   public static final int RESULT_FAILED = 1;
   public static final int RESULT_ABORTED = 2;
   public static final int RESULT_UNKNOWN = 3;
   public static final int RESULT_TIMEOUT = 4;
   public static final int RESULT_NOCONNECT = 5;
   public static final int RESULT_CONNECTED = 6;
   public static final int RESULT_NORESOURCE = 7;
   public static final int RESULT_CANTCOMPLETE = 8;
   public static final int RESULT_SRCEMPTY = 9;
   public static final int RESULT_DSTOCCUPIED = 10;
   public static final int RESULT_SLOTEMPTY = 11;
   public static final int RESULT_SLOTFULL = 12;
   public static final int RESULT_BADVALUE = 13;
   public static final int RESULT_INVALIDSLOT = 14;
   public static final int RESULT_INVALIDOP = 15;
   public static final int RESULT_WRONGMODE = 16;
   public static final int RESULT_WRONGSTATE = 17;
   public static final int RESULT_WRONGMODEL = 18;
   public static final int RESULT_CASEREJECTED = 19;
   public static final int RESULT_CASEJAMMED = 20;
   public static final int RESULT_CASERESEATED = 21;
   public static final int RESULT_CASEYANKEDOUT = 22;
   public static final int RESULT_CASESHOVEDIN = 23;
   public static final int RESULT_CANTLOADDLL = 2001;
   public static final int RESULT_CANTFREEDLL = 2002;
   public static final int OPSTATE_UNKNOWN = 0;
   public static final int OPSTATE_QUEUED = 1;
   public static final int OPSTATE_PENDING = 2;
   public static final int OPSTATE_INPROCESS = 3;
   public static final int OPSTATE_COMPLETE = 4;
   public static final int OPSTATE_RETIRED = 5;
   public static final int TAG_UNKNOWN = 0;
   public static final int TAG_CNTRCAPLIST = 1;
   public static final int TAG_CNTRHWVERS = 2;
   public static final int TAG_CNTRPLVERS = 3;
   public static final int TAG_CNTRFWVERS = 4;
   public static final int TAG_CNTRSERNUM = 5;
   public static final int TAG_CNTRSTATUS = 6;
   public static final int TAG_100P_TEMPFAN1 = 7;
   public static final int TAG_100P_TEMPFAN2 = 8;
   public static final int TAG_100P_TEMPFAN3 = 9;
   public static final int TAG_100P_TEMPFAN4 = 10;
   public static final int TAG_100P_POWER = 11;
   public static final int TAG_100P_SERVO = 12;
   public static final int TAG_JRKERR_PARAMS = 13;
   public static final int TAG_100P_DOOR = 14;
   public static final int TAG_100P_ARM = 15;
   public static final int TAG_100P_ROLLER = 16;
   public static final int TAG_100P_MISCRBT = 17;
   public static final int TAG_100P_LED = 18;

   public static synchronized native int Connect();

   public static synchronized native void SetGlobalRef(
      String lClassName,
      String lExceptionClassName,
      String lRegForAsynEventsName,
      String lCommonFunctionName,
      String lGetTaggedInfoName,
      String lGetStatusQuickName,
      String lRotateToSlotName,
      String lEjectCaseFromSlotName,
      String lVersionMajor,
      String lVersionMinor,
      String lVersionBuild,
      String lVersionString
   );

   public static synchronized native void ReleaseGlobalRef();

   public static synchronized native int Disconnect();

   public static synchronized native int StartOperation(int lKey, int aLocation, int tagType, int var3);

   public static synchronized native int GetOpState(int var0);

   public static synchronized native int AbortOperation(int var0);

   public static synchronized native int RegForAsyncEvents(int cbRegEvtKey);

   public static synchronized native int SetMode(int mode);

   public static synchronized native int GetMode();

   public static synchronized native int StartAppWatchdog();

   public static synchronized native int StrokeAppWatchdog();

   public static synchronized native int KillAppWatchdog();

   public static synchronized native void GetLibraryVersion();

   public static synchronized native void AddLineToLog(String var0);

   public static synchronized native void StartLoggingService();

   public static synchronized native void KillLoggingService();

   static {
      System.loadLibrary("DvdServo2_0");
   }
}
