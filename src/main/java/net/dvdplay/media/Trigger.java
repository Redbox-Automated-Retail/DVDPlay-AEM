package net.dvdplay.media;

import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.util.Util;

public class Trigger extends Thread implements Runnable {
   private PlayListManager plm;
   private boolean flag;

   public Trigger(PlayListManager _plm) {
      this.plm = _plm;
      this.flag = false;
   }

   public void run() {
      while (true) {
         if (AEMGui.isCurrentScreen("MainScreen") && this.flag && this.plm.playNext()) {
            this.flag = false;
         }

         Util.sleep(1000);
      }
   }

   public void setFlag() {
      this.flag = true;
   }
}
