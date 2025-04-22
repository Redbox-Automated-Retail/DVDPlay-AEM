package net.dvdplay.aem;

import net.dvdplay.logger.DvdplayLevel;

public class ServoFactory {
   private static Servo mInstance = null;
   private static int mConfig = 0;

   public static Servo getInstance() {
      if (mInstance == null) {
         mInstance = createInstance();
      }

      return mInstance;
   }

   public static synchronized int getConfig() {
      return mConfig;
   }

   protected static synchronized void setConfig(int aConfig) {
      mConfig = aConfig;
   }

   protected static Servo createInstance() {
      int lResult = 931;
      Aem.logDetailMessage(DvdplayLevel.INFO, "Servo.createInstance");
      if (mInstance == null) {
         lResult = DPEC.Connect();
         if (lResult == 0) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "Continue ServoPlus");
            mInstance = new ServoPlus();
            setConfig(2);
            return mInstance;
         } else if (lResult == 6) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "ServoFactory attempted to reconnect to the controller board!");
            return mInstance;
         } else if (lResult == 13) {
            mInstance = new ServoEx();
            setConfig(1);
            throw new ServoException("ServoFacotry reported incompatible DPEC firmware and library!");
         } else {
            if (lResult == 2001) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, "ServoFactory: JNI Bridge failed to load DPEC_API.Dll!");
            }

            if (lResult == 2002) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, "ServoFactory: JNI Bridge failed to unload DPEC_API.Dll!");
            }

            Aem.logDetailMessage(DvdplayLevel.INFO, "Continue ServoEx (DPEC_Connect result = " + String.valueOf(lResult) + ")");
            mInstance = new ServoEx();
            setConfig(1);
            return mInstance;
         }
      } else {
         return mInstance;
      }
   }
}
