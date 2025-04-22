package net.dvdplay.screen;

import net.dvdplay.aem.ServoFactory;

public class ServoParamsPanelFactory {
   private static ServoParamsPanel mInstance = null;

   public static synchronized ServoParamsPanel getInstance() {
      if (mInstance == null) {
         switch (ServoFactory.getConfig()) {
            case 1:
               mInstance = new ServoParamsPanelEx();
               break;
            case 2:
               mInstance = new ServoParamsPanelPlus();
            case 3:
         }
      }

      return mInstance;
   }

   public static synchronized void reset() {
      mInstance = null;
   }
}
