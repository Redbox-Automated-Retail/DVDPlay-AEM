package net.dvdplay.aem;

public class CreditCardReader {
   public static final int mReadTimeout = 15000;

   public static native String startCardRead(CreditCard mCC, boolean mStripSentinels);

   public static native int stopCardRead();

   static {
      System.loadLibrary("DvdCardReader1_0");
   }
}
