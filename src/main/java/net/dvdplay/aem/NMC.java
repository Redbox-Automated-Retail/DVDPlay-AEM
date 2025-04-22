package net.dvdplay.aem;

public class NMC {
   public static final int SEND_INPUTS = 1;
   public static final int SEND_POS = 1;
   public static final int SEND_HOME = 16;
   public static final int LOAD_POS = 1;
   public static final int LOAD_VEL = 2;
   public static final int LOAD_ACC = 4;
   public static final int ENABLE_SERVO = 16;
   public static final int VEL_MODE = 32;
   public static final int START_NOW = 128;
   public static final int ON_LIMIT1 = 1;
   public static final int HOME_STOP_SMOOTH = 32;
   public static final int AMP_ENABLE = 1;
   public static final int MOTOR_OFF = 2;
   public static final int STOP_ABRUPT = 4;
   public static final int STOP_SMOOTH = 8;
   public static final int MOVE_DONE = 1;
   public static final int HOME_IN_PROG = 128;

   public static synchronized native int NmcInit(String var0, int var1);

   public static synchronized native void NmcShutdown();

   private static native boolean NmcHardReset(byte var0);

   private static native boolean NmcNoOp(byte var0);

   public static synchronized native boolean NmcDefineStatus(byte var0, byte var1);

   public static synchronized native boolean NmcReadStatus(byte var0, byte var1);

   public static synchronized native int NmcGetStat(byte var0);

   public static synchronized native void ErrorPrinting(int var0);

   private static native char SioOpen(String var0, int var1);

   private static native boolean SioClose(char var0);

   private static native boolean SioPutChars(char var0, String var1, int var2);

   private static native boolean SioChangeBaud(char var0, int var1);

   private static native boolean SioClrInbuf(char var0);

   public static synchronized native boolean IoClrOutBit(byte var0, int var1);

   public static synchronized native boolean IoBitDirIn(byte var0, int var1);

   public static synchronized native boolean IoBitDirOut(byte var0, int var1);

   public static synchronized native boolean IoInBitVal(byte var0, int var1);

   public static synchronized native boolean IoSetOutBit(byte var0, int var1);

   public static synchronized native boolean IoSetPWMVal(byte var0, byte var1, byte var2);

   public static synchronized native boolean ServoSetGain(
      byte var0, int var1, int var2, int var3, int var4, byte var5, byte var6, int var7, byte var8, byte var9
   );

   public static synchronized native boolean ServoStopMotor(byte var0, byte var1);

   public static synchronized native boolean ServoLoadTraj(byte var0, byte var1, int var2, int var3, int var4, byte var5);

   public static synchronized native boolean ServoSetHoming(byte var0, byte var1);

   public static synchronized native int ServoGetHome(byte var0);

   public static synchronized native int ServoGetPos(byte var0);

   public static native String getTimeZoneInfo();

   public static native void setTimeZoneInfo(int var0, int var1, int var2, int var3);

   static {
      System.loadLibrary("DvdServo1_0");
   }
}
