package net.dvdplay.aem;

public class BarCodeReader {
   private static boolean mIsReading = false;

   public static String zreadBarCode(BarCode a, String angleList, boolean aSaveImage, String aFilename) {
      if (mIsReading) {
         return "";
      } else {
         mIsReading = true;
         String ret = readBarCode(a, angleList, aSaveImage, aFilename);
         mIsReading = false;
         return ret;
      }
   }

   public static native String readBarCode(BarCode var0, String var1, boolean var2, String var3);

   public static native void showVideoFormatDlg();

   public static native void showVideoSourceDlg();

   public static native void showVideoDisplayDlg();

   public static native void showVideoCompressionDlg();

   public static native int getVolume();

   public static native void setVolume(int var0);

   public static native void startButton();

   public static native boolean checkInstance();

   static {
      System.loadLibrary("DvdBarCode1_0");
   }
}
