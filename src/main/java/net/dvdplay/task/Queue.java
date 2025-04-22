package net.dvdplay.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import net.dvdplay.aem.Aem;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.userop.DiscFound;
import net.dvdplay.userop.DiscMissing;
import net.dvdplay.userop.DiscRemove;
import net.dvdplay.userop.DiscRental;
import net.dvdplay.userop.DiscReturn;
import net.dvdplay.util.Util;

public class Queue {
   private ArrayList mQueueJobList;

   public Queue() {
      Aem.logSummaryMessage("Creating Queue ...");
      this.mQueueJobList = new ArrayList();
      this.loadQueueFiles();
   }

   public void loadQueueFiles() {
      Aem.logSummaryMessage("Loading queue files ...");

      try {
         File lQueueDir = new File("c:\\windows\\system32\\var\\qtasks\\");
         File[] lAllFilesList = lQueueDir.listFiles();
         ArrayList<File> lFileArrayList = new ArrayList<>();
         File[] lQueueFileList = new File[0];
         DataPacketComposer lComposer = new DataPacketComposer();

          for (File file : lAllFilesList) {
              boolean lFileAdded = false;
              if (file.getName().startsWith("q-") && file.getName().endsWith(".xml")) {
                  for (int j = 0; j < lFileArrayList.size(); j++) {
                      if (file.getName().compareToIgnoreCase(((File) lFileArrayList.get(j)).getName()) < 0) {
                          lFileArrayList.add(j, file);
                          lFileAdded = true;
                          break;
                      }
                  }

                  if (!lFileAdded) {
                      lFileArrayList.add(file);
                  }
              }
          }

         lQueueFileList = lFileArrayList.toArray(lQueueFileList);

         for (int ix = 0; ix < lQueueFileList.length; ix++) {
            NvPairSet lNvPairSet;
            try {
               lNvPairSet = lComposer.nvDeMarshal(DataPacketComposer.load(lQueueFileList[ix].getAbsolutePath()));
            } catch (Exception var15) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Failed to read a queue job");
               Aem.setQueueError();
               Aem.moveFile(lQueueFileList[ix].getAbsolutePath(), lQueueFileList[ix].getAbsolutePath() + ".bak");
               continue;
            }

            Calendar lJobDate = Calendar.getInstance(Aem.getLocale());
            Calendar lNow = Calendar.getInstance(Aem.getLocale());
            lJobDate.setTime(Util.stringToDate(lNvPairSet.getNvPair("DTUpdated").getValueAsString()));
            lNow.add(Calendar.DATE, -5);
            int lJobId = Integer.parseInt(lNvPairSet.getNvPair("QueueJobId").getValueAsString());
            int lRequestId = Integer.parseInt(lNvPairSet.getNvPair("RequestId").getValueAsString());
            if (lJobDate.before(lNow)) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Deleting queue job (JobId " + lJobId + ", RequestId " + lRequestId + ")");
               Aem.setQueueError();
               File lTmpFile = new File(lQueueFileList[ix].getAbsolutePath());
               if (lTmpFile.exists() && !lTmpFile.delete()) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete (JobId " + lJobId + ", RequestId " + lRequestId + ")");
               }
            } else {
               Aem.logSummaryMessage("Adding queue job (JobId " + lJobId + ", RequestId " + lRequestId + ")");
               switch (lJobId) {
                  case 1:
                     this.addQueueJob(
                        new QueueJob(
                           new DiscRental(lComposer.nvDeMarshal(lNvPairSet.getNvPair("DataPacket").getValueAsString())),
                           lQueueFileList[ix].getAbsolutePath(),
                           lJobId,
                           lRequestId
                        ),
                        false
                     );
                     break;
                  case 2:
                     this.addQueueJob(
                        new QueueJob(
                           new DiscReturn(lComposer.nvDeMarshal(lNvPairSet.getNvPair("DataPacket").getValueAsString())),
                           lQueueFileList[ix].getAbsolutePath(),
                           lJobId,
                           lRequestId
                        ),
                        false
                     );
                     break;
                  case 3:
                     this.addQueueJob(
                        new QueueJob(
                           new DiscRemove(lComposer.nvDeMarshal(lNvPairSet.getNvPair("DataPacket").getValueAsString())),
                           lQueueFileList[ix].getAbsolutePath(),
                           lJobId,
                           lRequestId
                        ),
                        false
                     );
                     break;
                  case 4:
                     this.addQueueJob(
                        new QueueJob(
                           new DiscMissing(lComposer.nvDeMarshal(lNvPairSet.getNvPair("DataPacket").getValueAsString())),
                           lQueueFileList[ix].getAbsolutePath(),
                           lJobId,
                           lRequestId
                        ),
                        false
                     );
                     break;
                  case 5:
                     this.addQueueJob(
                        new QueueJob(
                           new DiscFound(lComposer.nvDeMarshal(lNvPairSet.getNvPair("DataPacket").getValueAsString())),
                           lQueueFileList[ix].getAbsolutePath(),
                           lJobId,
                           lRequestId
                        ),
                        false
                     );
                     break;
                  default:
                     Aem.logDetailMessage(DvdplayLevel.WARNING, "Invalid QueueJobId " + lJobId);
                     Aem.setQueueError();
               }
            }
         }
      } catch (Exception var16) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var16.getMessage(), var16);
         throw new DvdplayException("Loading queue files failed");
      }
   }

   public synchronized void addQueueJob(QueueJob aQueueJob, boolean aSave) {
      try {
         if (aSave) {
            aQueueJob.saveToFile();
         }

         this.mQueueJobList.add(aQueueJob);
         if (Util.checkLockFile("c:\\windows\\system32\\var\\qtasks\\")) {
            throw new DvdplayException("Found LockFile saving queue");
         }
      } catch (Exception var4) {
         Aem.logDetailMessage(DvdplayLevel.SEVERE, var4.getMessage());
         Aem.logSummaryMessage(var4.getMessage());
         Aem.setPersistenceError();
         Aem.disableRentals();
         Aem.disableReturns();
         Aem.sendLockFiles();
      }
   }

   public int getQueueJobCount() {
      return this.mQueueJobList.size();
   }

   public void runJobs() {
      int lNumJobs = this.getQueueJobCount();
      if (lNumJobs > 0) {
         if (!Aem.getServerConnectionStatus()) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "No server connection, not running queue jobs");
         } else {
            Aem.logSummaryMessage("Starting queue jobs");

            for (int i = 0; i < lNumJobs; i++) {
               try {
                  if (!Aem.getServerConnectionStatus()) {
                     Aem.logDetailMessage(DvdplayLevel.INFO, "Lost server connection, stop running queue jobs");
                     return;
                  }

                  if (Aem.inQuiesceMode()) {
                     Aem.logDetailMessage(DvdplayLevel.INFO, "In quiesce mode, stop running queue jobs");
                     return;
                  }

                  ((QueueJob)this.mQueueJobList.get(i)).execute();
                  this.mQueueJobList.remove(i--);
                  lNumJobs--;
               } catch (QueueException var4) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, var4.getMessage());
                  ((QueueJob)this.mQueueJobList.get(i)).incrementNumRetries();
                  if (((QueueJob)this.mQueueJobList.get(i)).getNumRetries() >= 5) {
                     Aem.logDetailMessage(
                        DvdplayLevel.WARNING,
                        "Removing queue job (JobId "
                           + ((QueueJob)this.mQueueJobList.get(i)).getJobId()
                           + ", RequestId "
                           + ((QueueJob)this.mQueueJobList.get(i)).getRequestId()
                           + ")"
                     );
                     this.mQueueJobList.remove(i--);
                     lNumJobs--;
                     Aem.setQueueError();
                  }
               }
            }

            Aem.logSummaryMessage("Finished queue jobs");
         }
      }
   }

   public void purgeQueueJob(int aRequestId) {
      File lQueueDir = new File("c:\\windows\\system32\\var\\qtasks\\");
      File[] lAllFilesList = lQueueDir.listFiles();

      for (int i = 0; i < lAllFilesList.length; i++) {
         try {
            String lFilename = lAllFilesList[i].getName();
            if (lFilename.startsWith("q-") && lFilename.endsWith(".xml")) {
               StringTokenizer lStrTok = new StringTokenizer(lFilename);
               lStrTok.nextToken("-");
               lStrTok.nextToken();
               lStrTok = new StringTokenizer(lStrTok.nextToken());
               int lRequestId = Integer.parseInt(lStrTok.nextToken("."));
               if (lRequestId == aRequestId) {
                  Aem.logDetailMessage(DvdplayLevel.INFO, "Purging queue job (RequestId " + lRequestId + ")");
                  lAllFilesList[i].setLastModified(System.currentTimeMillis());
                  Aem.moveFile(lAllFilesList[i].getCanonicalPath(), "c:\\windows\\system32\\var\\pdata\\archive\\" + lAllFilesList[i].getName());
                  return;
               }
            }
         } catch (Exception var8) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, var8.getMessage());
         }
      }
   }
}
