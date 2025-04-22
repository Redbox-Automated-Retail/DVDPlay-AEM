package net.dvdplay.aem;

public class Light {
   private static final int MAX_LEVEL = 255;
   private static int mLevel = 255;

   public void setLevel(int aLevel) {
      if (aLevel > 255) {
         mLevel = 255;
      } else {
         mLevel = aLevel;
      }
   }

   public static int getLevel() {
      return mLevel;
   }

   public void on() throws LightException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetPWMVal((byte)3, (byte)0, (byte)mLevel)) {
         throw new LightException("Light could not turn on.");
      }
   }

   public void off() throws LightException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetPWMVal((byte)3, (byte)0, (byte)0)) {
         throw new LightException("Light could not turn off.");
      }
   }
}
