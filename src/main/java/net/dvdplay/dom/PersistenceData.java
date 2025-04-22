package net.dvdplay.dom;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.RCSet;
import net.dvdplay.communication.RDataSetFieldValues;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.ExecCommandLine;
import net.dvdplay.util.Util;

public class PersistenceData extends RCSet {
   protected boolean mIsModified;
   protected String[] mNames;
   protected String[] mTypes;
   protected String mFilename;
   protected String mDisplayName;
   protected PersistenceData mCheckSumPData;
   protected boolean mUseCheckSum = true;

   public boolean isModified() {
      return this.mIsModified;
   }

   public void touch() {
      this.mIsModified = true;
   }

   public String getDisplayName() {
      return this.mDisplayName;
   }

   public String getFilename() {
      return this.mFilename;
   }

   public PersistenceData(String[] aNames, String[] aTypes, String aFilename, String aDisplayName) {
      this(aNames, aTypes, aFilename, aDisplayName, true);
   }

   public PersistenceData(String[] aNames, String[] aTypes, String aFilename, String aDisplayName, boolean aCheckCheckSum) {
      this(aNames, aTypes, aFilename, aDisplayName, aCheckCheckSum, DOMData.mCheckSumData);
   }

   public PersistenceData(String[] aNames, String[] aTypes, String aFilename, String aDisplayName, boolean aCheckCheckSum, PersistenceData aCheckSumPData) {
      super(aNames, aTypes);
      this.mNames = aNames;
      this.mTypes = aTypes;
      this.mFilename = aFilename;
      this.mDisplayName = aDisplayName;
      this.mUseCheckSum = aCheckCheckSum;
      this.mCheckSumPData = aCheckSumPData;
      this.mIsModified = false;
   }

   public PersistenceData(String[] aNames, String[] aTypes, String aFilename, String aDisplayName, RCSet aRCSet) {
      this(aNames, aTypes, aFilename, aDisplayName, aRCSet, true);
   }

   public PersistenceData(String[] aNames, String[] aTypes, String aFilename, String aDisplayName, RCSet aRCSet, boolean aCheckCheckSum) {
      this(aNames, aTypes, aFilename, aDisplayName, aRCSet, aCheckCheckSum, DOMData.mCheckSumData);
   }

   public PersistenceData(
      String[] aNames, String[] aTypes, String aFilename, String aDisplayName, RCSet aRCSet, boolean aCheckCheckSum, PersistenceData aCheckSumPData
   ) {
      super(aNames, aTypes);
      this.mNames = aNames;
      this.mTypes = aTypes;
      this.mFilename = aFilename;
      this.mDisplayName = aDisplayName;
      this.mUseCheckSum = aCheckCheckSum;
      this.mCheckSumPData = aCheckSumPData;
      if (this.mUseCheckSum && !this.checkCheckSum(aFilename, aDisplayName)) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, aDisplayName + " failed " + this.mCheckSumPData.getDisplayName());
         Aem.setPersistenceError();
         this.archive();
         throw new DvdplayException(aDisplayName + " failed " + this.mCheckSumPData.getDisplayName());
      } else if (aRCSet != null) {
         for (int i = 0; i < aRCSet.rowCount(); i++) {
            if (!aRCSet.isDeleted(i)) {
               this.addRow(aRCSet.getRow(i));
            }
         }

         this.mIsModified = false;
      } else {
         Aem.logDetailMessage(DvdplayLevel.WARNING, aDisplayName + " cannot be read");
         Aem.setPersistenceError();
         this.archive();
         throw new DvdplayException(aDisplayName + " cannot be read");
      }
   }

   public void refreshData(RCSet aRCSet) {
      for (int i = 0; i < this.rowCount(); i++) {
         this.deleteRow(i);
      }

      for (int i = 0; i < aRCSet.rowCount(); i++) {
         if (!aRCSet.isDeleted(i)) {
            this.addRow(aRCSet.getRow(i));
         }
      }
   }

   public void addRow(RDataSetFieldValues a) {
      super.addRow(a);
      this.mIsModified = true;
   }

   public void addRow(String[] a) {
      super.addRow(a);
      this.mIsModified = true;
   }

   public void deleteRow(int a) {
      super.deleteRow(a);
      this.mIsModified = true;
   }

   public synchronized void archive() {
      Aem.logDetailMessage(DvdplayLevel.WARNING, "archiving " + this.mDisplayName);
      String lTmpFilename = Util.getTimeStamp() + this.mFilename;
      File lTmpFile = new File("c:\\windows\\system32\\var\\pdata\\archive\\" + lTmpFilename);
      File lCurrFile = new File("c:\\windows\\system32\\var\\pdata\\" + this.mFilename);
      lCurrFile.renameTo(lTmpFile);
   }

   public synchronized void save() {
      this.save(true);
   }

   public synchronized void save(boolean aDoLogging) {
      int SAVE_RETRIES = 3;

      for (int i = 0; i < 3; i++) {
         if (Aem.isExitingApp()) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "App is exiting!!  Not saving " + this.mDisplayName);
            return;
         }

         if (aDoLogging) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "saving " + this.mDisplayName + " attempt " + (i + 1));
         }

         if (i != 0) {
            System.gc();
         }

         if (this.saveAttempt(aDoLogging)) {
            return;
         }
      }

      Aem.setPersistenceError();
      if (aDoLogging) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "Failed to save " + this.mDisplayName + " after " + 3 + " tries.");
      }
   }

   private boolean saveAttempt(boolean aDoLogging) {
      String lTmpFilename = "tmp" + Util.getTimeStamp() + ".xml";
      File lTmpFile = new File("c:\\windows\\system32\\var\\pdata\\" + lTmpFilename);
      File lCurrFile = new File("c:\\windows\\system32\\var\\pdata\\" + this.mFilename);
      String lOldFilename = "c:\\windows\\system32\\var\\pdata\\" + this.mFilename + ".bak";
      File lOldFile = new File(lOldFilename);
      int SAVE_RETRIES = 3;
      String lDoneFlag = Util.getTimeStamp() + "DONE";

      try {
         DataPacketComposer ldpc = new DataPacketComposer();
         int counter = 0;

         while (counter++ < 3) {
            try {
               DataPacketComposer.save(ldpc.rcMarshal(this.mNames, this.mTypes, this), "c:\\windows\\system32\\var\\pdata\\" + lTmpFilename);
               break;
            } catch (DvdplayException var13) {
               if (aDoLogging) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "Attempt " + counter + " to save " + this.mDisplayName + " failed: " + var13.getMessage());
               } else {
                  System.out.println(var13.getMessage());
               }
            }
         }

         if (lTmpFile.exists()) {
            if (!new File("c:\\aem\\bin\\copymove.bat").exists()) {
               Aem.generateCopyMoveBat();
            }

            String[] args = new String[]{
               "oldfile=" + lOldFile.getAbsolutePath(),
               "currentfile=" + lCurrFile.getAbsolutePath(),
               "newfile=" + lTmpFile.getAbsolutePath(),
               "doneflag=" + lDoneFlag
            };
            ExecCommandLine.getExecCommandLine().exec("c:\\aem\\bin\\copymove.bat", 30000, lDoneFlag, args, aDoLogging);
            if (!lTmpFile.exists() && lCurrFile.exists()) {
               Calendar lCal = Calendar.getInstance();
               lCal.setTimeInMillis(lCurrFile.lastModified());
               lCal.add(1, -2);
               if (!lCurrFile.setLastModified(lCal.getTimeInMillis())) {
               }

               this.mIsModified = false;
               this.saveCheckSum();
               if (aDoLogging) {
                  Aem.logDetailMessage(DvdplayLevel.INFO, this.mDisplayName + " saved");
               }

               return true;
            } else {
               if (aDoLogging) {
                  if (lTmpFile.exists()) {
                     Aem.logDetailMessage(DvdplayLevel.ERROR, "TmpFile still exists");
                  }

                  if (!lCurrFile.exists()) {
                     Aem.logDetailMessage(DvdplayLevel.ERROR, "CurrFile still exists");
                  }
               }

               throw new DvdplayException("save file failed: " + this.mDisplayName);
            }
         } else {
            throw new DvdplayException("failed to create temp file: " + this.mDisplayName);
         }
      } catch (Exception var14) {
         if (aDoLogging) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "Persistence Error: " + var14.getMessage(), var14);
         }

         if (!lCurrFile.exists()) {
            if (lTmpFile.exists() && lTmpFile.renameTo(lCurrFile)) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Recovered from new");
               return true;
            }

            if (lOldFile.exists() && lOldFile.renameTo(lCurrFile)) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Recovered from old");
               return false;
            }

            Aem.logDetailMessage(DvdplayLevel.ERROR, "Did not recover");
         }

         return false;
      }
   }

   protected void saveCheckSum() {
      if (this.mUseCheckSum) {
         try {
            long lCheckSum = Util.calculateCheckSum("c:\\windows\\system32\\var\\pdata\\" + this.mFilename);
            int lIndex = Util.getRCSetIndexForFieldValue(this.mCheckSumPData, "Filename", this.mFilename);
            RCSet lTmpRCSet = new RCSet(DvdplayBase.CHECK_SUM_FIELD_NAMES, DvdplayBase.CHECK_SUM_FIELD_TYPES);
            String[] lValues = new String[DvdplayBase.CHECK_SUM_FIELD_NAMES.length];
            lValues[this.mCheckSumPData.getFieldIndex("Filename")] = this.mFilename;
            lValues[this.mCheckSumPData.getFieldIndex("CheckSum")] = BigDecimal.valueOf(lCheckSum).toString();
            lTmpRCSet.addRow(lValues);
            if (lIndex < 0) {
               this.mCheckSumPData.addRow(lTmpRCSet.getRow(0));
            } else {
               this.mCheckSumPData.deleteRow(lIndex);
               this.mCheckSumPData.addRow(lTmpRCSet.getRow(0));
            }
         } catch (Exception var6) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "Save " + this.mCheckSumPData.getDisplayName() + " failed: " + var6.getMessage());
         }
      }
   }

   public boolean recover() {
      File lBakFile = new File("c:\\windows\\system32\\var\\pdata\\" + this.mFilename + ".bak");
      if (!lBakFile.exists()) {
         return false;
      } else {
         File lCurrFile = new File("c:\\windows\\system32\\var\\pdata\\" + this.mFilename);
         Aem.moveFile(lBakFile.getAbsolutePath(), lCurrFile.getAbsolutePath());
         return lCurrFile.exists();
      }
   }

   public boolean checkCheckSum() {
      return this.checkCheckSum(this.mFilename, this.mDisplayName);
   }

   public boolean checkCheckSum(String aFilename, String aDisplayName) {
      try {
         long a = Util.calculateCheckSum("c:\\windows\\system32\\var\\pdata\\" + aFilename);
         long b = Aem.getCheckSum(aFilename, aDisplayName, this.mCheckSumPData).longValue();
         return a == b;
      } catch (Exception var7) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var7.getMessage());
         return false;
      }
   }

   public static PersistenceData createInstance(
      String[] aFieldNames, String[] aFieldTypes, String aFilename, String aDisplayName, DataPacketComposer aComposer, String aQueryName, String aQueryString
   ) {
      PersistenceData lPersistenceData = null;

      try {
         lPersistenceData = new PersistenceData(
            aFieldNames,
            aFieldTypes,
            aFilename,
            aDisplayName,
            aComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\" + aFilename))
         );
      } catch (Exception var11) {
         lPersistenceData = new PersistenceData(aFieldNames, aFieldTypes, aFilename, aDisplayName);

         try {
            refreshData(aQueryName, lPersistenceData, aQueryString);
         } catch (Exception var10) {
            Aem.logDetailMessage(DvdplayLevel.SEVERE, var10.getMessage(), var10);
            throw new DvdplayException("Could not refresh " + aDisplayName);
         }
      }

      if (lPersistenceData == null) {
         throw new DvdplayException("Failed to create " + aDisplayName);
      } else {
         return lPersistenceData;
      }
   }

   private static void refreshData(String aQueryName, RCSet aRCSet, String aQueryString) {
      Aem.getData(aQueryName, aRCSet, aQueryString, new Date(0L), false, null);
   }
}
