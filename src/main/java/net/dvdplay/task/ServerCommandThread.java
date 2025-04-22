package net.dvdplay.task;

import java.util.ArrayList;
import java.util.logging.Level;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class ServerCommandThread extends Thread implements IEvent {
   private static int mSleepTime;
   private static ArrayList mCmdList;

   public ServerCommandThread(int aSleepTime) {
      mSleepTime = aSleepTime;
   }

   public boolean isTimeToExecute() {
      return true;
   }

   public void execute() {
      mCmdList = Aem.getServerCmdList();
      if (mCmdList.size() > 0) {
         Aem.logSummaryMessage("Starting Server Commands");
         int i = 0;

         while (i < mCmdList.size()) {
            RCSet lCmdRCSet = (RCSet)mCmdList.get(i);
            int lCmdId = Integer.parseInt(lCmdRCSet.getFieldValue(0, lCmdRCSet.getFieldIndex("CmdId")));
            Aem.logSummaryMessage("Executing CmdId " + lCmdId);

            try {
               switch (lCmdId) {
                  case 1:
                     Aem.setGetDataCmd(1);
                     break;
                  case 2:
                     Aem.setGetDataCmd(2);
                     break;
                  case 3:
                     Aem.setGetDataCmd(3);
                     break;
                  case 4:
                     Aem.setGetDataCmd(4);
                     break;
                  case 5:
                     Aem.setGetDataCmd(5);
                     break;
                  case 6:
                     Aem.setGetDataCmd(6);
                     break;
                  case 7:
                     Aem.setGetDataCmd(7);
                     break;
                  case 8:
                     Aem.setGetDataCmd(8);
                     break;
                  case 9:
                     Aem.setGetDataCmd(9);
                     break;
                  case 10:
                     Aem.setGetDataCmd(10);
                     break;
                  case 11:
                     Aem.setGetDataCmd(11);
                     break;
                  case 12:
                     Aem.setGetDataCmd(12);
                     break;
                  case 13:
                     Aem.setGetDataCmd(13);
                     break;
                  case 14:
                     Aem.setGetDataCmd(14);
                     break;
                  case 15:
                     Aem.setGetDataCmd(15);
                     break;
                  case 16:
                     Aem.setGetDataCmd(16);
                     break;
                  case 17:
                     Aem.setGetDataCmd(17);
                     break;
                  case 18:
                     Aem.setGetDataCmd(18);
                     break;
                  case 19:
                     Aem.setGetDataCmd(19);
                     break;
                  case 20:
                     Aem.setGetDataCmd(20);
                     break;
                  case 21:
                     Aem.setGetDataCmd(21);
                     break;
                  case 22:
                     Aem.sendLogs();
                     Aem.sendPersistenceFiles();
                     break;
                  case 23:
                     Aem.purgeTitleDetailRecords(lCmdRCSet);
                     break;
                  case 24:
                     Aem.purgeDiscDetailRecords(lCmdRCSet);
                     break;
                  case 26:
                     Aem.setDebugModeOff();
                     break;
                  case 27:
                     Aem.downloadFiles(lCmdRCSet);
                     break;
                  case 28:
                     Aem.setRestartComputer();
                     break;
                  case 29:
                     Aem.sendQueueFiles();
                     break;
                  case 30:
                     Aem.resetLastSynchDate();
                     break;
                  case 31:
                     Aem.disableRentals();
                     break;
                  case 32:
                     Aem.disableReturns();
                     break;
                  case 33:
                     Aem.enableRentals();
                     break;
                  case 34:
                     Aem.enableReturns();
                     break;
                  case 35:
                  default:
                     Aem.logDetailMessage(Level.WARNING, "Unknown ServerCmd " + lCmdId);
                     break;
                  case 36:
                     Util.releaseLockFile("c:\\windows\\system32\\var\\pdata\\");
                     Util.releaseLockFile("c:\\windows\\system32\\var\\qtasks\\");
                     break;
                  case 37:
                     Aem.resetAemStatus();
                     break;
                  case 38:
                     Aem.getFranchiseLogo(true);
                     break;
                  case 39:
                     Aem.setNeedToReinitialize();
                     break;
                  case 40:
                     Aem.purgeMediaDetailRecord(lCmdRCSet);
                     break;
                  case 41:
                     Aem.resetMediaDetailRecordImage(lCmdRCSet);
                     break;
                  case 42:
                     Aem.resetMediaDetailRecordMedia(lCmdRCSet);
                     break;
                  case 43:
                     Aem.setGetDataCmd(43);
                     break;
                  case 44:
                     Aem.uploadSlotData();
                     break;
                  case 45:
                     Aem.uploadServoData();
                     break;
                  case 46:
                     Aem.setQuiesceMode(3);
                     break;
                  case 47:
                     Aem.setQuiesceMode(2);
                     break;
                  case 48:
                     HeartBeatThread.stopStrokeWatchdog();
                     break;
                  case 49:
                     AemFactory.getInstance().purgeQueueJob(lCmdRCSet);
                  case 25:
                     Aem.setDebugModeOn();
                     break;
                  case 50:
                     Aem.setRebuildQueue();
                     break;
                  case 51:
                     Aem.uploadFiles(lCmdRCSet);
               }

               Aem.unsetServerCmdList(lCmdId);
            } catch (DvdplayException var5) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "ServerCmd " + lCmdId + " failed.");
            }
         }

         Aem.logSummaryMessage("Finished Server Commands");
      }
   }

   public void run() {
      while (true) {
         if (this.isTimeToExecute()) {
            this.execute();
         }

         Util.sleep(mSleepTime);
      }
   }
}
