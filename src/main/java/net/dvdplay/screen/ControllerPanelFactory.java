package net.dvdplay.screen;

import net.dvdplay.aem.ServoFactory;

public class ControllerPanelFactory {
   private static ControllerPanel mInstance = null;

   public static synchronized ControllerPanel getInstance() {
      if (mInstance == null) {
         switch (ServoFactory.getConfig()) {
            case 1:
               mInstance = new ControllerPanelEx();
               break;
            case 2:
               mInstance = new ControllerPanelPlus();
            case 3:
         }
      }

      return mInstance;
   }

   public static synchronized void reset() {
      mInstance = null;
   }
}
