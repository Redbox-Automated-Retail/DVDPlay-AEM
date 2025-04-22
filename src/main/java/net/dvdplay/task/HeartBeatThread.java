package net.dvdplay.task;

import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ClickRobot;
import net.dvdplay.util.Util;

public class HeartBeatThread extends Thread implements IEvent {
   private Servo mServo;
   private int mSleepTime;
   private int mWatchdogSleepTime = 60000;
   private int mPingSleepTime = 300000;
   private int mPingErrorSleepTime = 30000;
   private int mClickRobotSleepTime = 120000;
   private int mMarkedForRemovalSleepTime = 3600000;
   private ClickRobot mClickRobot;
   private int mClickRobotCount = 0;
   private long mLastPingTime = 0L;
   private long mLastCheckClickRobot = 0L;
   private long mLastCheckEQScreenMonitor = 0L;
   private long mLastUpdateMarkedForRemoval = System.currentTimeMillis();
   private long mLastStrokeWatchdog = System.currentTimeMillis();
   private static boolean mStrokeWatchdog = true;
   private static boolean mSendPings = true;

   public HeartBeatThread(int aSleepTime) {
      this.mSleepTime = aSleepTime;
      this.mClickRobot = new ClickRobot();
      this.mServo = ServoFactory.getInstance();
   }

   public static void stopSendPings() {
      Aem.logSummaryMessage("Stop sending Pings!!");
      mSendPings = false;
   }

   public static void stopStrokeWatchdog() {
      Aem.logSummaryMessage("Stop stroking Watchdog!!");
      mStrokeWatchdog = false;
   }

   public boolean isTimeToExecute() {
      return true;
   }

   public void execute() {
      if (mStrokeWatchdog) {
         this.strokeWatchdog();
      }

      this.checkEQScreenMonitor();
      this.checkClickRobot();
      this.updateMarkedForRemoval();
      if (mSendPings) {
         this.sendHeartBeat();
      }
   }

   private void updateMarkedForRemoval() {
      long lNow = System.currentTimeMillis();
      if (lNow - this.mLastUpdateMarkedForRemoval > this.mMarkedForRemovalSleepTime) {
         Aem.setNeedToUpdateMarkedForRemoval();
         this.mLastUpdateMarkedForRemoval = lNow;
      }
   }

   private void checkEQScreenMonitor() {
      AemFactory.getInstance();
      if (!Aem.isToolsRunning()) {
         long lNow = System.currentTimeMillis();
         if (lNow - this.mLastCheckEQScreenMonitor > this.mPingSleepTime) {
            AemFactory.getInstance();
            if (lNow - Aem.getLastLogReceived() >= 300000L) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "EQ Monitor alert triggered");
               Aem.setScreenError();
               Aem.setQuiesceMode(2);
            }

            this.mLastCheckEQScreenMonitor = lNow;
         }
      }
   }

   private void checkClickRobot() {
      AemFactory.getInstance();
      if (!Aem.isToolsRunning()) {
         long lNow = System.currentTimeMillis();
         if (lNow - this.mLastCheckClickRobot > this.mClickRobotSleepTime) {
            AemFactory.getInstance();
            if (lNow - Aem.getLastRobotClickReceived() >= 300000L) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Screen Monitor alert triggered");
               if (this.mClickRobotCount < 5) {
                  this.mClickRobotCount++;
                  this.mClickRobot.interrupt();
                  this.mClickRobot = new ClickRobot();
                  this.mClickRobot.start();
               } else {
                  this.mClickRobotCount = 0;
                  Aem.setScreenError();
               }
            }

            this.mLastCheckClickRobot = lNow;
         }
      }
   }

   private void sendHeartBeat() {
      int lPingInterval;
      if (Aem.getServerConnectionStatus()) {
         lPingInterval = this.mPingSleepTime;
      } else {
         lPingInterval = this.mPingErrorSleepTime;
      }

      long lNow = System.currentTimeMillis();
      if (lNow - this.mLastPingTime > lPingInterval) {
         try {
            Comm.ping();
            Aem.setServerConnectionStatus(true);
         } catch (Exception var5) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var5.getMessage());
            Aem.setServerConnectionStatus(false);
         }

         this.mLastPingTime = lNow;
      }
   }

   private void strokeWatchdog() {
      long lNow = System.currentTimeMillis();
      if (lNow - this.mLastStrokeWatchdog > this.mWatchdogSleepTime) {
         if (lNow - this.mLastStrokeWatchdog > 2 * this.mWatchdogSleepTime) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Missed stroking Watchdog.  Last stroke: " + Util.dateToString(new Date(this.mLastStrokeWatchdog)));
            this.mServo.strokeAppWatchdog(true);
         } else {
            this.mServo.strokeAppWatchdog();
         }

         this.mLastStrokeWatchdog = lNow;
      }
   }

   public void run() {
      AEMGui.initEQHeartbeat();
      this.mClickRobot.start();
      Aem.logSummaryMessage("Starting App Watchdog");
      this.mServo.startAppWatchdog();

      while (true) {
         if (this.isTimeToExecute()) {
            this.execute();
         }

         Util.sleep(this.mSleepTime);
         if (Aem.inQuiesceMode() && !Aem.isDiscActive()) {
            AemFactory.getInstance().exitApp(Aem.getQuiesceMode());
         }
      }
   }
}
