package net.dvdplay.aem;

public class ServoCallbackFactory {
   public static final int mNumberOfOpcode = 15;
   private static ServoCallback[] mCallbackObjectArray;

   protected static void createInstances() {
      mCallbackObjectArray = new ServoCallback[15];

      for (int i = 0; i < 15; i++) {
         mCallbackObjectArray[i] = new ServoCallback();
      }
   }

   public static ServoCallback getServoCallbackObject(int aIndex) {
      if (mCallbackObjectArray == null) {
         createInstances();
      }

      return mCallbackObjectArray[aIndex];
   }
}
