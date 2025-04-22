package net.dvdplay.task;

import java.util.Calendar;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.util.Util;

public class SendLogThread extends Thread implements IEvent {
   private final int mPollSleepTime = 59000;
   private Date mSendLogTime;

   public SendLogThread(Date aSendLogTime) {
      this.mSendLogTime = aSendLogTime;
   }

   public boolean isTimeToExecute() {
      Calendar calSendLog = Calendar.getInstance(Aem.getLocale());
      Calendar calNow = Calendar.getInstance(Aem.getLocale());
      Calendar calStop = Calendar.getInstance(Aem.getLocale());
      calSendLog.setTime(this.mSendLogTime);
      calNow.setTime(Util.stringToTime(Util.timeToString(new Date())));
      calStop.setTime(this.mSendLogTime);
      calStop.add(Calendar.MINUTE, 1);
      return calNow.after(calSendLog) && calNow.before(calStop);
   }

   public void run() {
      while (true) {
         this.execute();
         Util.sleep(59000);
      }
   }

   public void execute() {
      if (this.isTimeToExecute()) {
         Aem.sendLogs();
         Aem.sendPersistenceFiles();
         Aem.purgeLogs();
         Aem.purgePersistence();
         Aem.purgeTmpDir();
      }
   }
}
