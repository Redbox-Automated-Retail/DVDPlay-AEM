package net.dvdplay.task;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.userop.DiscAction;
import net.dvdplay.userop.DiscActionException;
import net.dvdplay.util.Util;

public class QueueJob {
   private DiscAction mDiscAction;
   private int mNumRetries;
   private String mQueueFileName;
   private int mJobId;
   private int mRequestId;

   public QueueJob(DiscAction aDiscAction) {
      this.mDiscAction = aDiscAction;
      this.mNumRetries = 0;
      this.mQueueFileName = "";
      this.mJobId = 0;
      this.mRequestId = 0;
   }

   public QueueJob(DiscAction aDiscAction, String aQueueFileName, int aJobId, int aRequestId) {
      this.mDiscAction = aDiscAction;
      this.mNumRetries = 0;
      this.mQueueFileName = aQueueFileName;
      this.mJobId = aJobId;
      this.mRequestId = aRequestId;
   }

   public int getJobId() {
      return this.mJobId;
   }

   public int getRequestId() {
      return this.mRequestId;
   }

   public int getNumRetries() {
      return this.mNumRetries;
   }

   public void incrementNumRetries() {
      this.mNumRetries++;
   }

   public void execute() throws QueueException {
      try {
         File lFile = new File(this.mQueueFileName);
         Aem.logSummaryMessage("Starting queue job (JobId " + this.mJobId + ", RequestId " + this.mRequestId + ")");
         this.mDiscAction.queueExecute();
         Aem.logSummaryMessage("Finished queue job (JobId " + this.mJobId + ", RequestId " + this.mRequestId + ")");
         if (lFile.exists() && !lFile.delete()) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete (JobId " + this.mJobId + ", RequestId " + this.mRequestId + ")");
         }

         DOMData.save();
      } catch (DiscActionException var2) {
         DOMData.save();
         throw new QueueException(var2.getMessage());
      }
   }

   public void saveToFile() {
      try {
         int SAVE_RETRIES = 3;
         DataPacketComposer lComposer = new DataPacketComposer();
         NvPairSet lNvPairSet = new NvPairSet();
         String lRequestId = this.mDiscAction.mNvPairSet.getNvPair("RequestId").getValueAsString();
         int lJobId = this.mDiscAction.getQueueJobId();
         Util.setLockFile("c:\\windows\\system32\\var\\qtasks\\", lComposer.nvMarshal(this.mDiscAction.mNvPairSet));
         Aem.logSummaryMessage("Adding queue job (JobId " + lJobId + ", RequestId " + lRequestId + ")");
         this.mJobId = lJobId;
         this.mRequestId = Integer.parseInt(lRequestId);
         lNvPairSet.add(new NvPair("QueueJobId", "int", String.valueOf(this.mDiscAction.getQueueJobId())));
         lNvPairSet.add(new NvPair("DataPacket", "String", lComposer.nvMarshal(this.mDiscAction.mNvPairSet)));
         lNvPairSet.add(new NvPair("DTUpdated", "Date", Util.dateToString(new Date(System.currentTimeMillis()))));
         lNvPairSet.add(new NvPair("RequestId", "int", lRequestId));
         this.mQueueFileName = "c:\\windows\\system32\\var\\qtasks\\q-" + Util.getTimeStamp() + "-" + lRequestId + ".xml";
         int lCounter = 0;

         while (lCounter++ < 3) {
            try {
               DataPacketComposer.save(lComposer.nvMarshal(lNvPairSet), this.mQueueFileName);
               break;
            } catch (DvdplayException var14) {
               Aem.logDetailMessage(
                  DvdplayLevel.ERROR,
                  "Attempt" + lCounter + " to save " + "queue job (JobId " + lJobId + ", RequestId " + lRequestId + ")" + " failed: " + var14.getMessage()
               );
            }
         }

         if (!new File(this.mQueueFileName).exists()) {
            throw new DvdplayException("Failed to save queue job (JobId " + lJobId + ", RequestId " + lRequestId + ")");
         }

         Calendar lCal = Calendar.getInstance();
         lCal.setTimeInMillis(new File(this.mQueueFileName).lastModified());
         lCal.add(1, -2);
         if (!new File(this.mQueueFileName).setLastModified(lCal.getTimeInMillis())) {
         }
      } catch (DvdplayException var15) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var15.getMessage());
         Aem.setQueueError();
      } catch (Exception var16) {
         Aem.logDetailMessage(DvdplayLevel.SEVERE, var16.getMessage());
         Aem.logSummaryMessage(var16.getMessage());
         Aem.setPersistenceError();
         Aem.disableRentals();
         Aem.disableReturns();
         Aem.sendLockFiles();
      } finally {
         Util.releaseLockFile("c:\\windows\\system32\\var\\qtasks\\");
      }
   }
}
