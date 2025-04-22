package net.dvdplay.task;

import net.dvdplay.aem.Aem;

public class QueueThread extends Thread {
   private static boolean mQueueIsBusy;

   public QueueThread() {
      mQueueIsBusy = false;
   }

   public void run() {
      mQueueIsBusy = true;
      Aem.mQueue.runJobs();
      mQueueIsBusy = false;
   }

   public static boolean isQueueBusy() {
      return mQueueIsBusy;
   }
}
