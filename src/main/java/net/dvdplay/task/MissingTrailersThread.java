package net.dvdplay.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.communication.RDataSetFieldValues;
import net.dvdplay.dom.DOMData;
import net.dvdplay.dom.PersistenceData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.ExecCommandLine;
import net.dvdplay.util.Util;

public class MissingTrailersThread extends Thread implements IEvent {
   private Date mStartTime;
   private Date mStopTime;
   private long mLastPosterDownloadTime;
   private final int mPollSleepTime = 60000;

   public MissingTrailersThread(Date aStartTime, Date aStopTime) {
      this.mStartTime = aStartTime;
      this.mStopTime = aStopTime;
      this.mLastPosterDownloadTime = -1800000L;
   }

   public boolean isTimeToExecute() {
      Calendar calStart = Calendar.getInstance(Aem.getLocale());
      Calendar calNow = Calendar.getInstance(Aem.getLocale());
      Calendar calStop = Calendar.getInstance(Aem.getLocale());
      calStart.setTime(this.mStartTime);
      calNow.setTime(Util.stringToTime(Util.timeToString(new Date())));
      calStop.setTime(this.mStopTime);
      return calNow.after(calStart) && calNow.before(calStop);
   }

   public boolean isDownloadPosterTime() {
      return System.currentTimeMillis() - this.mLastPosterDownloadTime >= 1800000L;
   }

   public void run() {
      while (true) {
         if (this.isDownloadPosterTime()) {
            Aem.getPosters();
            Aem.getAds();
            this.mLastPosterDownloadTime = System.currentTimeMillis();
         }

         this.execute();
         Util.sleep(60000);
      }
   }

   public void execute() {
      if (this.isTimeToExecute()) {
         for (int i = 0; i < DOMData.mMediaPlaylistData.rowCount(); i++) {
            if (!DOMData.mMediaPlaylistData.isDeleted(i)) {
               try {
                  String lId = DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("MediaId"));
                  String lTypeId = DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("MediaTypeId"));
                  PersistenceData lTmpPersistence = DOMData.mMediaDetailData;
                  String lIdName = "MediaId";
                  String lIsDownloadedName = "MediaDownloaded";
                  String[] lNames = new String[2];
                  String[] lValues = new String[2];
                  lNames[0] = lIdName;
                  lValues[0] = lId;
                  lNames[1] = "MediaTypeId";
                  lValues[1] = lTypeId;
                  int lIndex = Util.getRCSetIndexForFieldValue(lTmpPersistence, lNames, lValues);
                  if (lIndex < 0) {
                     Aem.createNewMediaDetailRecord(lId, lTypeId);
                     lIndex = Util.getRCSetIndexForFieldValue(lTmpPersistence, lNames, lValues);
                  }

                  boolean lIsDownloaded;
                  if (Integer.parseInt(lTmpPersistence.getFieldValue(lIndex, lTmpPersistence.getFieldIndex(lIsDownloadedName))) == 1) {
                     lIsDownloaded = true;
                  } else {
                     lIsDownloaded = false;
                  }

                  if (!lIsDownloaded) {
                     Date lEndDate = Util.stringToDate(
                        DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("EndDate")), "yyyy-MM-dd HH:mm:ss.SSS"
                     );
                     if (!Util.isDateBeforeNow(lEndDate)) {
                        int lNumDownloaded = Integer.parseInt(lTmpPersistence.getFieldValue(lIndex, lTmpPersistence.getFieldIndex("NumDownloaded")));
                        int lNumSegments = Integer.parseInt(
                           DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("NumSegments"))
                        );

                        String lBaseFilename;
                        for (lBaseFilename = DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("SegmentName"));
                           ++lNumDownloaded <= lNumSegments;
                           lIndex = Util.getRCSetIndexForFieldValue(lTmpPersistence, lNames, lValues)
                        ) {
                           String lFilename;
                           if (lNumDownloaded < 10) {
                              lFilename = lBaseFilename + ".z0" + lNumDownloaded;
                           } else {
                              lFilename = lBaseFilename + ".z" + lNumDownloaded;
                           }

                           Comm.downloadFile("/webdav/trailers/" + lFilename, "c:\\aem\\tmp\\" + lFilename);

                           try {
                              RDataSetFieldValues lRDSFV = lTmpPersistence.getRow(lIndex);
                              lRDSFV.setValue(lTmpPersistence.getFieldIndex("NumDownloaded"), Integer.toString(lNumDownloaded));
                              lTmpPersistence.addRow(lRDSFV);
                              lTmpPersistence.deleteRow(lIndex);
                              DOMData.save();
                           } catch (DvdplayException var24) {
                              Aem.logDetailMessage(DvdplayLevel.ERROR, var24.getMessage());
                           }
                        }

                        String var26 = lBaseFilename + ".zip";
                        Comm.downloadFile("/webdav/trailers/" + var26, "c:\\aem\\tmp\\" + var26);
                        String lFinalFilename = DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("Filename"));

                        try {
                           this.CombineSegments(lBaseFilename, lNumSegments, lFinalFilename);
                           File lFinalFile = new File("c:\\aem\\tmp\\" + lFinalFilename);
                           long lfilesize = lFinalFile.length();
                           long lGivenFileSize = Integer.parseInt(
                              DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("FileSize"))
                           );
                           if (lfilesize != lGivenFileSize) {
                              throw new DvdplayException("Filesizes do not match. Expected: " + lGivenFileSize + " Found: " + lfilesize);
                           }

                           Aem.moveFile("c:\\aem\\tmp\\" + lFinalFilename, "c:\\aem\\content\\trailers\\" + lFinalFilename);
                           Aem.logSummaryMessage("Download of " + lFinalFilename + " suceeded.");
                           RDataSetFieldValues lRDSFV = lTmpPersistence.getRow(lIndex);
                           lRDSFV.setValue(lTmpPersistence.getFieldIndex("MediaDownloaded"), Integer.toString(1));
                           lTmpPersistence.addRow(lRDSFV);
                           lTmpPersistence.deleteRow(lIndex);
                           DOMData.save();
                        } catch (DvdplayException var23) {
                           Aem.logDetailMessage(DvdplayLevel.ERROR, var23.getMessage());
                           Aem.logDetailMessage(DvdplayLevel.WARNING, "Download of " + lFinalFilename + " failed.");
                           RDataSetFieldValues lRDSFV = lTmpPersistence.getRow(lIndex);
                           lRDSFV.setValue(lTmpPersistence.getFieldIndex("NumDownloaded"), "0");
                           lRDSFV.setValue(lTmpPersistence.getFieldIndex("MediaDownloaded"), Integer.toString(0));
                           lTmpPersistence.addRow(lRDSFV);
                           lTmpPersistence.deleteRow(lIndex);
                           DOMData.save();
                        }

                        this.DeleteSegments(lBaseFilename, lNumSegments);
                     }
                  }
               } catch (DvdplayException var25) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, var25.getMessage());
               }
            }
         }
      }
   }

   public void DeleteSegments(String aSegmentName, int aNumSegments) {
      for (int i = 1; i <= aNumSegments; i++) {
         String lFilename;
         if (i < 10) {
            lFilename = aSegmentName + ".z0" + i;
         } else {
            lFilename = aSegmentName + ".z" + i;
         }

         File lTmpFile = new File("c:\\aem\\tmp\\" + lFilename);
         if (!lTmpFile.delete()) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete c:\\aem\\tmp\\" + lFilename);
         }
      }

      File var6 = new File("c:\\aem\\tmp\\" + aSegmentName + ".zip");
      if (!var6.delete()) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete c:\\aem\\tmp\\" + aSegmentName + ".zip");
      }
   }

   public void CombineSegments(String aSegmentName, int aNumSegments, String aFilename) {
      String lBatFile = "c:\\aem\\tmp\\" + Util.getTimeStamp() + ".bat";
      String lZipFile = "c:\\aem\\tmp\\" + Util.getTimeStamp() + ".zip";
      File lFinalFile = new File("c:\\aem\\tmp\\" + aFilename);
      String lDoneFlag = Util.getTimeStamp() + "DONE";
      if (lFinalFile.exists()) {
         lFinalFile.delete();
      }

      TimeZone tz = Calendar.getInstance(Aem.getLocale()).getTimeZone();
      long offset = tz.getOffset(new Date().getTime());
      long hour = offset / 1000L / 60L / 60L;
      long min = offset / 1000L / 60L % 60L;
      String timezonestring = tz.getDisplayName(tz.inDaylightTime(new Date()), 0) + hour + ":";
      if (min < 10L) {
         timezonestring = timezonestring + "0" + min;
      } else {
         timezonestring = timezonestring + min;
      }

      String lBatCmd = "@echo off\n\ncd c:\\aem\\tmp\\";
      lBatCmd = lBatCmd + "\n\nset tz=" + timezonestring;
      lBatCmd = lBatCmd + "\n\ncopy /b ";

      for (int i = 1; i <= aNumSegments; i++) {
         if (i < 10) {
            lBatCmd = lBatCmd + aSegmentName + ".z0" + i + "+";
         } else {
            lBatCmd = lBatCmd + aSegmentName + ".z" + i + "+";
         }
      }

      lBatCmd = lBatCmd + aSegmentName + ".zip";
      lBatCmd = lBatCmd + " " + lZipFile;
      lBatCmd = lBatCmd + "\n\nzip -FF " + lZipFile;
      lBatCmd = lBatCmd + "\n\nunzip32 -oqq " + lZipFile;
      lBatCmd = lBatCmd + "\n\nset tz= ";
      lBatCmd = lBatCmd + "\n\necho " + lDoneFlag;

      try {
         BufferedWriter bfout = new BufferedWriter(new FileWriter(lBatFile));
         bfout.write(lBatCmd);
         bfout.flush();
         bfout.close();
         ExecCommandLine.getExecCommandLine().exec(lBatFile, 60000, lDoneFlag);
         File lTmpFile = new File(lBatFile);
         if (!lTmpFile.delete()) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete " + lBatFile);
         }

         lTmpFile = new File(lZipFile);
         if (!lTmpFile.delete()) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete " + lZipFile);
         }

         if (!lFinalFile.exists()) {
            throw new DvdplayException("Failed to create " + aFilename);
         }
      } catch (Exception var20) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var20.getMessage());
         throw new DvdplayException("CombineSegments failed");
      }
   }
}
