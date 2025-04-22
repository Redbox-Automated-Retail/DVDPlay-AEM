package net.dvdplay.screen;

import net.dvdplay.aem.ServoFactory;

public class SlotCalPanelFactory {
   private static SlotCalPanel mInstance = null;

   public static synchronized SlotCalPanel getInstance() {
      if (mInstance == null) {
         switch (ServoFactory.getConfig()) {
            case 1:
               mInstance = new SlotCalPanelEx();
               break;
            case 2:
               mInstance = new SlotCalPanelPlus();
            case 3:
         }
      }

      return mInstance;
   }

   public static synchronized void reset() {
      mInstance = null;
   }
}
