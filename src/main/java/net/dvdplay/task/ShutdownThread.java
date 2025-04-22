package net.dvdplay.task;

import java.util.Calendar;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.util.Util;

public class ShutdownThread extends Thread implements IEvent {
   private final int mPollSleepTime = 59000;
   private Date mShutdownTime;

   public ShutdownThread(Date aShutdownTime) {
      this.mShutdownTime = aShutdownTime;
   }

   public boolean isTimeToExecute() {
      Calendar calShutdown = Calendar.getInstance(Aem.getLocale());
      Calendar calNow = Calendar.getInstance(Aem.getLocale());
      Calendar calStop = Calendar.getInstance(Aem.getLocale());
      calShutdown.setTime(this.mShutdownTime);
      calNow.setTime(Util.stringToTime(Util.timeToString(new Date())));
      calStop.setTime(this.mShutdownTime);
      calStop.add(Calendar.MINUTE, 1);
      return calNow.after(calShutdown) && calNow.before(calStop);
   }

   public void run() {
      while (true) {
         this.execute();
         Util.sleep(59000);
      }
   }

   public void execute() {
      if (this.isTimeToExecute()) {
         Aem.setRestartComputer();
      }
   }
}
