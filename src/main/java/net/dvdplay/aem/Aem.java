package net.dvdplay.aem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Level;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.RCSet;
import net.dvdplay.communication.RDataSetFieldValues;
import net.dvdplay.dom.DOMData;
import net.dvdplay.dom.PersistenceData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.Disc;
import net.dvdplay.inventory.DiscIndex;
import net.dvdplay.inventory.DiscIndexItem;
import net.dvdplay.inventory.GenreItem;
import net.dvdplay.inventory.Inventory;
import net.dvdplay.inventory.InventoryException;
import net.dvdplay.inventory.LocaleIndexItem;
import net.dvdplay.inventory.PaymentCardTypeIndexItem;
import net.dvdplay.inventory.PlayListItem;
import net.dvdplay.inventory.PricingItem;
import net.dvdplay.inventory.SortTitleDiscIndexItem;
import net.dvdplay.inventory.StreetDateDiscIndexItem;
import net.dvdplay.inventory.TitleTypeIndexItem;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.poll.PollItem;
import net.dvdplay.request.DatasetRequest;
import net.dvdplay.request.GetFragmentRequest;
import net.dvdplay.request.QueryRequest;
import net.dvdplay.task.HeartBeatThread;
import net.dvdplay.task.MissingTrailersThread;
import net.dvdplay.task.Queue;
import net.dvdplay.task.QueueJob;
import net.dvdplay.task.QueueThread;
import net.dvdplay.task.SendLogThread;
import net.dvdplay.task.ServerCommandThread;
import net.dvdplay.task.ShutdownThread;
import net.dvdplay.userop.DiscFound;
import net.dvdplay.userop.DiscItem;
import net.dvdplay.userop.DiscMissing;
import net.dvdplay.util.ExecCommandLine;
import net.dvdplay.util.Util;

public class Aem {
   protected static Servo mServo;
   public static Queue mQueue;
   private static QueueThread mQueueThread;
   public static Comm mComm;
   private static DOMData mDOMData;
   public static Inventory mInventory;
   private static DiscMissing mDiscMissing;
   private static DiscFound mDiscFound;
   private static String mVersionNum = "2.0.2";
   private static int mLocaleId = 1;
   private static int mTitleTypeId = 1;
   private static BigDecimal mTaxRate = new BigDecimal("8.25");
   private static int mCurrencyFractionalDigits;
   private static String mServerAddress = "192.168.1.15:8085";
   private static String mFTPAddress = "192.168.1.11";
   private static boolean mServerConnectionStatus;
   private static boolean mSavingStatus;
   private static int mHardwareStatus;
   private static int mInventoryStatus;
   private static int mPersistenceStatus;
   private static int mDataStatus;
   private static int mQueueStatus;
   private static int mInternalStatus;
   private static int mScreenStatus;
   private static int mRequestStatus;
   private static int mPowerStatus;
   private static int mTemperatureStatus;
   private static int mGetDataCmd;
   private static ArrayList mServerCmdList;
   private static RCSet mAckRCSet;
   private static int mAemId = 248;
   private static int mFranchiseId = 0;
   private static Date mDueTime = Util.stringToTime("23:59:59");
   private static Locale mLocale;
   private static int mDefaultLocaleId = 1;
   private static String mTimeZoneId = "America/Los_Angeles";
   private static int mTimeZoneAutoAdj = 1;
   private static Date mTrailerDownloadStartTime = Util.stringToTime("00:30:00");
   private static Date mTrailerDownloadStopTime = Util.stringToTime("03:30:00");
   private static Date mShutdownTime = Util.stringToTime("04:00:00");
   private static Date mSendLogTime = Util.stringToTime("00:01:00");
   private static Date mLastSynchDate = new Date(0L);
   private static boolean mIsStandAlone = false;
   private static boolean mIsDebugMode = true;
   private static boolean mNeedToReinitialize = false;
   private static boolean mRestartComputer = false;
   private static boolean mDisableRentals = false;
   private static boolean mDisableReturns = false;
   private static boolean mLogInitialized = false;
   private static boolean mDisableBuy = false;
   private static boolean mUpdateMarkedForRemoval = false;
   private static Date mUpdateMarkedForRemovalSyncDate;
   private static long mLastLogReceived = System.currentTimeMillis();
   private static long mLastRobotClickReceived = System.currentTimeMillis();
   private static boolean mIsToolsRunning = false;
   private static boolean mUploadSlotData = false;
   private static boolean mUploadServoData = false;
   private static Properties mDebugProperties = new Properties();
   private static boolean mIsDiscActive = false;
   private static boolean mIsExitingApp = false;
   private static boolean mRebuildQueue = false;
   private static boolean mRefreshedDiscDetailStartup = false;
   private static Boolean mGettingPosters = false;
   private static int mQuiesceMode = 0;
   private static boolean mDoLog = true;

   public Aem(int aAemId, String aServerAddress, String aId, boolean aIsInstall, boolean aIsReset) {
      mAemId = aAemId;
      mServerAddress = aServerAddress;
      checkDir("c:\\aem\\logs\\archive\\");
      initializeLog();
      logSummaryMessage("****************** BootStrap " + mAemId + " " + mServerAddress + " ******************");
      checkDir("c:\\windows\\system32\\var\\pdata\\archive\\");
      checkDir("c:\\windows\\system32\\var\\qtasks\\");
      if (Util.checkLockFile("c:\\windows\\system32\\var\\pdata\\")) {
         logSummaryMessage("Clearing persistence locks");
         Util.releaseLockFile("c:\\windows\\system32\\var\\pdata\\");
      }

      if (Util.checkLockFile("c:\\windows\\system32\\var\\qtasks\\")) {
         logSummaryMessage("Clearing queue locks");
         Util.releaseLockFile("c:\\windows\\system32\\var\\qtasks\\");
      }

      if (areQueueFiles()) {
         if (aIsInstall) {
            mDOMData = new DOMData(aId, this, aIsReset);
            purgeDir("c:\\windows\\system32\\var\\qtasks\\", System.currentTimeMillis());
         } else {
            if (aId != null) {
               throw new DvdplayException("Need to run full bootstrap.");
            }

            mDOMData = new DOMData(aId, this, aIsReset);
            mComm = new Comm();
            mServo = ServoFactory.getInstance();
            mInventory = new Inventory();
            mLocale = getLocaleIndexItemByLocaleId(mDefaultLocaleId).getLocale();
            mLocaleId = mDefaultLocaleId;
            mCurrencyFractionalDigits = NumberFormat.getCurrencyInstance(mLocale).getCurrency().getDefaultFractionDigits();
            mServerConnectionStatus = true;
            mQueue = new Queue();
            mQueue.runJobs();
            refreshDiscDetailData();
            DOMData.save();
         }
      } else {
         mDOMData = new DOMData(aId, this, aIsReset);
      }

      if (DOMData.mAemPropertiesData.rowCount() == 0) {
         logDetailMessage(DvdplayLevel.ERROR, "Bootstrap failed.  Check server address and AEM Id");
         throw new DvdplayException("Bootstrap failed.  Check server address and AEM Id");
      }
   }

   public Aem(boolean aFlag) {
      checkDir("c:\\aem\\logs\\archive\\");
      initializeLog();
   }

   public Aem() {
      this.init();
   }

   private synchronized void init() {
      if (!BarCodeReader.checkInstance()) {
         System.out.println("AEM instance already running.");
         System.out.flush();
         System.exit(1);
      }

      boolean lLockError = false;
      checkDir("c:\\aem\\logs\\archive\\");
      initializeLog();
      checkDir("c:\\windows\\system32\\var\\pdata\\archive\\");
      checkDir("c:\\windows\\system32\\var\\qtasks\\");
      checkDir("c:\\aem\\content\\posters\\");
      checkDir("c:\\aem\\content\\trailers\\");
      checkDir("c:\\aem\\content\\ads\\");
      checkDir("c:\\aem\\content\\images\\");
      checkDir("c:\\aem\\tmp\\");
      checkDir("c:\\aem\\content\\html\\");
      mHardwareStatus = 0;
      mInventoryStatus = 0;
      mDataStatus = 0;
      mQueueStatus = 0;
      mInternalStatus = 0;
      mScreenStatus = 0;
      mRequestStatus = 0;
      mPowerStatus = 0;
      mTemperatureStatus = 0;
      if (getSavingStatus()) {
         logDetailMessage(DvdplayLevel.SEVERE, "Lockfile found on startup");
         logSummaryMessage("Lockfile found on startup");
         setPersistenceError();
         lLockError = true;
      }

      mDOMData = new DOMData();
      mUpdateMarkedForRemovalSyncDate = new Date(mLastSynchDate.getTime());
      if (lLockError) {
         sendLockFiles();
         sendLogs();
         Comm.ping();
         throw new DvdplayException("Lockfile found on startup");
      } else {
         logSummaryMessage("****************** Starting AEM " + mAemId + " ******************");
         logSummaryMessage("Version " + mVersionNum);
         if (mIsStandAlone) {
            logSummaryMessage("**Demo Mode**");
         }

         mComm = new Comm();
         mServerCmdList = new ArrayList();
         mAckRCSet = new RCSet(DvdplayBase.PING_ACK_FIELD_NAMES, DvdplayBase.PING_ACK_FIELD_TYPES);
         mServo = ServoFactory.getInstance();

         try {
            Comm.ping();
            this.updateAllData(mLastSynchDate);
            DOMData.save();
         } catch (Exception var3) {
            logDetailMessage(DvdplayLevel.WARNING, var3.getMessage(), var3);
         }

         logTimeZone();
         mInventory = new Inventory();
         mDiscMissing = new DiscMissing();
         mDiscFound = new DiscFound();
         mLocale = getLocaleIndexItemByLocaleId(mDefaultLocaleId).getLocale();
         mLocaleId = mDefaultLocaleId;
         mCurrencyFractionalDigits = NumberFormat.getCurrencyInstance(mLocale).getCurrency().getDefaultFractionDigits();
         mServerConnectionStatus = true;
         mQueueThread = new QueueThread();
         mQueue = new Queue();
         createMediaPlayList();
         createStaticPlayList();
         getPaymentPictures();
         getDataFranchiseId();
         getFranchiseLogo();
         getDisableBuy();
         if (!isStandAlone()) {
            new HeartBeatThread(5000).start();
            new MissingTrailersThread(mTrailerDownloadStartTime, mTrailerDownloadStopTime).start();
            new ServerCommandThread(60000).start();
            new SendLogThread(mSendLogTime).start();
         }

         new ShutdownThread(mShutdownTime).start();
         updateAemProperties();
         if (mRefreshedDiscDetailStartup && mQueue.getQueueJobCount() > 0) {
            mQueue.runJobs();
            refreshDiscDetailData();
         }
      }
   }

   public Servo getServo() {
      return mServo;
   }

   public static void readAemProperties() {
      RDataSetFieldValues lRDataSetFieldValues = null;
      String lCurrentField = "";

      try {
         for (int i = 0; i < DOMData.mAemPropertiesData.rowCount(); i++) {
            if (!DOMData.mAemPropertiesData.isDeleted(i)) {
               lRDataSetFieldValues = DOMData.mAemPropertiesData.getRow(i);
               lCurrentField = "DueTimeOfDay";
               mDueTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("DueTimeOfDay")));
               lCurrentField = "ShutdownTime";
               mShutdownTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ShutdownTime")));
               lCurrentField = "MediaDownloadStartTime";
               mTrailerDownloadStartTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("MediaDownloadStartTime")));
               lCurrentField = "MediaDownloadStopTime";
               mTrailerDownloadStopTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("MediaDownloadStopTime")));
               lCurrentField = "SendLogTime";
               mSendLogTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("SendLogTime")));
               lCurrentField = "LastSynchDate";
               mLastSynchDate = Util.stringToDate(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("LastSynchDate")));
               lCurrentField = "AemId";
               mAemId = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("AemId")));
               lCurrentField = "ServerAddress";
               mServerAddress = lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServerAddress"));
               lCurrentField = "FTPAddress";
               mFTPAddress = lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("FTPAddress"));
               lCurrentField = "TaxRate";
               mTaxRate = new BigDecimal(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("TaxRate")));
               lCurrentField = "DefaultLocaleId";
               mDefaultLocaleId = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("DefaultLocaleId")));
               lCurrentField = "TimeZoneId";
               mTimeZoneId = lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("TimeZoneId"));
               lCurrentField = "TimeZoneAutoAdj";
               mTimeZoneAutoAdj = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("TimeZoneAutoAdj")));
               break;
            }
         }
      } catch (DvdplayException var3) {
         logDetailMessage(DvdplayLevel.SEVERE, "Error reading in field " + lCurrentField);
         logSummaryMessage("Error reading in field " + lCurrentField);
         logDetailMessage(DvdplayLevel.SEVERE, "readAemProperties failed: " + var3.getMessage());
         logSummaryMessage("readAemProperties failed: " + var3.getMessage());
      }
   }

   public static void checkDebugModeFlag() {
      if (new File("c:\\aem\\debug.txt").exists()) {
         setDebugModeOn();

         try {
            FileInputStream in = new FileInputStream("c:\\aem\\debug.txt");
            mDebugProperties.load(in);
         } catch (Exception var1) {
            Log.debug(var1, "Error Reading debug.txt configuration " + var1.toString());
            return;
         }
      } else {
         setDebugModeOff();
      }
   }

   public static void setRefreshedDiscDetailStartup() {
      mRefreshedDiscDetailStartup = true;
   }

   public static String getProperty(String aPropertyName) {
      return mDebugProperties.containsKey(aPropertyName) ? mDebugProperties.getProperty(aPropertyName) : "";
   }

   public static String getTimeZoneId() {
      return mTimeZoneId;
   }

   public static void setGetDataCmd(int aCmdId) {
      mGetDataCmd = aCmdId;
   }

   public static boolean isGetDataCmd() {
      return mGetDataCmd != 0;
   }

   public static void setDebugModeOn() {
      mIsDebugMode = true;
      logDetailMessage(DvdplayLevel.INFO, "Debug mode on");
      Log.setDetailedLevel(DvdplayLevel.FINEST);
   }

   public static void setDebugModeOff() {
      mIsDebugMode = false;
      logDetailMessage(DvdplayLevel.INFO, "Debug mode off");
      Log.setDetailedLevel(DvdplayLevel.INFO);
   }

   public boolean isDebugModeOn() {
      return mIsDebugMode;
   }

   public static void setRestartComputer() {
      mRestartComputer = true;
   }

   public static boolean needToRestartcompter() {
      return mRestartComputer;
   }

   public static boolean needToUpdateMarkedForRemoval() {
      return mUpdateMarkedForRemoval;
   }

   public static void setNeedToUpdateMarkedForRemoval() {
      mUpdateMarkedForRemoval = true;
   }

   public static boolean needToReinitialize() {
      return mNeedToReinitialize;
   }

   public static void setNeedToReinitialize() {
      mNeedToReinitialize = true;
   }

   public static boolean isStandAlone() {
      return mIsStandAlone;
   }

   public static String getVersionNum() {
      return mVersionNum;
   }

   public static void setStandAloneMode() {
      mIsStandAlone = true;
   }

   public static int getQueueJobCount() {
      return mQueue.getQueueJobCount();
   }

   public static boolean areQueueFiles() {
      File lQueueDir = new File("c:\\windows\\system32\\var\\qtasks\\");
      File[] lAllFilesList = lQueueDir.listFiles();

      for (int i = 0; i < lAllFilesList.length; i++) {
         if (lAllFilesList[i].getName().startsWith("q-") && lAllFilesList[i].getName().endsWith(".xml")) {
            return true;
         }
      }

      return false;
   }

   public static String getDueTimeAsString() {
      SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)DateFormat.getTimeInstance(3, getLocale());
      return lSimpleDateFormat.format(mDueTime);
   }

   public static Date getDueTime() {
      return mDueTime;
   }

   public static String getShutdownTime() {
      SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)DateFormat.getTimeInstance(2, getLocale());
      return lSimpleDateFormat.format(mShutdownTime);
   }

   public static int getAemStatus() {
      return mHardwareStatus
         + mInventoryStatus
         + mPersistenceStatus
         + mDataStatus
         + mQueueStatus
         + mInternalStatus
         + mPowerStatus
         + mTemperatureStatus
         + mScreenStatus
         + mRequestStatus;
   }

   public static RCSet getAemAckRCSet() {
      return mAckRCSet;
   }

   public static void clearAckRCSet() {
      mAckRCSet = new RCSet(DvdplayBase.PING_ACK_FIELD_NAMES, DvdplayBase.PING_ACK_FIELD_TYPES);
   }

   public static boolean setServerCmdList(RCSet aCmdRCSet) {
      String[] lValues = new String[mAckRCSet.fieldCount()];
      String lCmdId = aCmdRCSet.getFieldValue(0, aCmdRCSet.getFieldIndex("CmdId"));
      String lSequenceId = aCmdRCSet.getFieldValue(0, aCmdRCSet.getFieldIndex("SequenceId"));
      lValues[mAckRCSet.getFieldIndex("SequenceId")] = lSequenceId;
      lValues[mAckRCSet.getFieldIndex("CmdAck")] = lCmdId;

      for (int i = 0; i < mServerCmdList.size(); i++) {
         RCSet lCmdRCSet = (RCSet)mServerCmdList.get(i);
         if (lCmdRCSet.getFieldValue(0, lCmdRCSet.getFieldIndex("CmdId")).equals(lCmdId)
            && lCmdRCSet.getFieldValue(0, lCmdRCSet.getFieldIndex("SequenceId")).equals(lSequenceId)) {
            return false;
         }
      }

      logSummaryMessage("adding ServerCmd " + lCmdId);
      mServerCmdList.add(aCmdRCSet);
      mAckRCSet.addRow(lValues);
      return true;
   }

   public static void unsetServerCmdList(int aCmdId) {
      for (int i = 0; i < mServerCmdList.size(); i++) {
         RCSet lCmdRCSet = (RCSet)mServerCmdList.get(i);
         int lCmdId = Integer.parseInt(lCmdRCSet.getFieldValue(0, lCmdRCSet.getFieldIndex("CmdId")));
         if (lCmdId == aCmdId) {
            mServerCmdList.remove(i);
            logSummaryMessage("Removing ServerCmd " + aCmdId);
            return;
         }
      }

      logDetailMessage(DvdplayLevel.WARNING, "unsetServerCmdList could not find aCmdId " + aCmdId);
   }

   public static ArrayList getServerCmdList() {
      return mServerCmdList;
   }

   public static void setQueueError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting queue error");
      logSummaryMessage("Setting queue error");
      mQueueStatus = 4;
   }

   public static void setDataError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting data error");
      logSummaryMessage("Setting data error");
      mDataStatus = 2;
   }

   public static void setPersistenceError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting persistence error");
      logSummaryMessage("Setting persistence error");
      mPersistenceStatus = 16;
   }

   public static void setInventoryError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting inventory Error");
      logSummaryMessage("Setting inventory Error");
      mInventoryStatus = 8;
   }

   public static void setInternalError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting internal error");
      mInternalStatus = 32;
   }

   public static void setPowerError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting power error");
      mPowerStatus = 256;
   }

   public static void setTemperatureError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting temperature error");
      mTemperatureStatus = 512;
   }

   public static void setScreenError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting screen error");
      mScreenStatus = 64;
   }

   public static void setRequestError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting request error");
      mRequestStatus = 128;
   }

   public static void setHardwareError() {
      logDetailMessage(DvdplayLevel.ERROR, "Setting hardware error");
      mHardwareStatus = 1;
   }

   public static boolean isHardwareError() {
      return mHardwareStatus != 0;
   }

   public static void setQuiesceMode(int aMode) {
      if (mQuiesceMode < aMode) {
         mQuiesceMode = aMode;
         logSummaryMessage("Setting Quiesce Mode: " + aMode);
      }
   }

   public static int getQuiesceMode() {
      return mQuiesceMode;
   }

   public static boolean inQuiesceMode() {
      return mQuiesceMode != 0;
   }

   public static boolean isQueueBusy() {
      return mQueue.getQueueJobCount() > 0;
   }

   public static void resetHardwareError() {
      mHardwareStatus = 0;
   }

   public static void resetAemStatus() {
      logSummaryMessage("Resetting Aem Status");
      mInternalStatus = 0;
      mInventoryStatus = 0;
      mPersistenceStatus = 0;
      mDataStatus = 0;
      mQueueStatus = 0;
      mScreenStatus = 0;
      mRequestStatus = 0;
   }

   public static void disableRentals() {
      logSummaryMessage("Disabling rentals.");
      mDisableRentals = true;
   }

   public static void enableRentals() {
      logSummaryMessage("Enabling rentals");
      mDisableRentals = false;
   }

   public static boolean isRentalDisabled() {
      return isHardwareError() || !getServerConnectionStatus() || mDisableRentals;
   }

   public static void disableReturns() {
      logSummaryMessage("Disabling returns.");
      mDisableReturns = true;
   }

   public static void enableReturns() {
      logSummaryMessage("Enabling returns");
      mDisableReturns = false;
   }

   public static boolean isReturnDisabled() {
      return isHardwareError() || mDisableReturns;
   }

   public static boolean isExitingApp() {
      return mIsExitingApp;
   }

   public static void setExitingApp() {
      mIsExitingApp = true;
   }

   public static boolean isRebuildQueue() {
      return mRebuildQueue;
   }

   public static void setRebuildQueue() {
      mRebuildQueue = true;
   }

   public static boolean isDiscActive() {
      return mIsDiscActive;
   }

   public static void setDiscActive() {
      mIsDiscActive = true;
   }

   public static void resetDiscActive() {
      mIsDiscActive = false;
   }

   public static void disableBuy() {
      mDisableBuy = true;
   }

   public static void enableBuy() {
      mDisableBuy = false;
   }

   public static boolean isBuyDisabled() {
      return mDisableBuy;
   }

   public static void setAemId(int aAemId) {
      mAemId = aAemId;
   }

   public static int getAemId() {
      return mAemId;
   }

   public static void setServerConnectionStatus(boolean aStatus) {
      mServerConnectionStatus = aStatus;
   }

   public static boolean getServerConnectionStatus() {
      return mServerConnectionStatus;
   }

   public static boolean getSavingStatus() {
      return Util.checkLockFile("c:\\windows\\system32\\var\\pdata\\") || Util.checkLockFile("c:\\windows\\system32\\var\\qtasks\\");
   }

   public static String getServerAddress() {
      return mServerAddress;
   }

   public static void setServerAddress(String aServerAddress) {
      mServerAddress = aServerAddress;
   }

   public static String getFTPAddress() {
      return mFTPAddress;
   }

   public static void setFTPAddress(String aFTPAddress) {
      mFTPAddress = aFTPAddress;
   }

   public static int getCurrencyFractionalDigits() {
      return mCurrencyFractionalDigits;
   }

   public static int getDefaultLocaleId() {
      return mDefaultLocaleId;
   }

   public static Locale getLocale() {
      return mLocale;
   }

   public static int getLocaleId() {
      return mLocaleId;
   }

   public static void setLocale(int aLocaleId) {
      try {
         mLocale = getLocaleIndexItemByLocaleId(aLocaleId).getLocale();
         mLocaleId = aLocaleId;
         Inventory.createAllIndexes();
      } catch (DvdplayException var2) {
         logDetailMessage(DvdplayLevel.ERROR, var2.getMessage());
      }
   }

   public static int getTitleTypeId() {
      return mTitleTypeId;
   }

   public static void setTitleTypeId(int aTitleTypeId) {
      mTitleTypeId = aTitleTypeId;
   }

   public static BigDecimal getTaxRate() {
      return mTaxRate;
   }

   public static void setTaxRate(BigDecimal aTaxRate) {
      mTaxRate = aTaxRate;
   }

   public static int getFranchiseID() {
      return mFranchiseId;
   }

   public static synchronized void runQueueJobs() {
      if (!QueueThread.isQueueBusy()) {
         if (isRebuildQueue()) {
            rebuildQueue();
         }

         if (getQueueJobCount() > 0) {
            new QueueThread().start();
         }
      }
   }

   public void initializeHardware() {
      try {
         mNeedToReinitialize = false;
         resetHardwareError();
         this.getServo().initServo();
         this.getServo().initialize();

         try {
            this.goToNextEmptySlot();
         } catch (InventoryException var2) {
         }
      } catch (ServoException var3) {
         logSummaryMessage("Failed to initialize hardware");
         logDetailMessage(DvdplayLevel.ERROR, var3.getMessage());
         setHardwareError();
      }
   }

   public static Disc getDisc(int aDiscDetailId) throws AemException {
      try {
         return Inventory.getDisc(aDiscDetailId);
      } catch (InventoryException var2) {
         throw new AemException(var2.getMessage());
      }
   }

   public static int createTopPickList(int aNumTopPicks) {
      return Inventory.createTopPickList(aNumTopPicks);
   }

   public static DiscIndexItem getTopPick(int aIndex) {
      return Inventory.getTopPick(aIndex);
   }

   public static int createSortTitlePickList(int aNumTopPicks) {
      return Inventory.createSortTitlePickList(aNumTopPicks);
   }

   public static SortTitleDiscIndexItem getSortTitlePick(int aIndex) {
      return Inventory.getSortTitlePick(aIndex);
   }

   public static int createStreetDatePickList(int aNumTopPicks) {
      return Inventory.createStreetDatePickList(aNumTopPicks);
   }

   public static StreetDateDiscIndexItem getStreetDatePick(int aIndex) {
      return Inventory.getStreetDatePick(aIndex);
   }

   public static int createGenrePickList(int aGenreId, int aNumGenrePicks) {
      return Inventory.createGenrePickList(aGenreId, aNumGenrePicks);
   }

   public static DiscIndexItem getGenrePick(int aIndex) {
      return Inventory.getGenrePick(aIndex);
   }

   public static int createGenreList() {
      return Inventory.createGenreList();
   }

   public static GenreItem getGenre(int aIndex) {
      return Inventory.getGenre(aIndex);
   }

   public static String getGenreName(int aGenreId) {
      String[] lNames = new String[3];
      String[] lValues = new String[3];
      lNames[0] = "GenreId";
      lNames[1] = "LocaleId";
      lNames[2] = "TitleTypeId";
      lValues[0] = Integer.toString(aGenreId);
      lValues[1] = Integer.toString(getLocaleId());
      lValues[2] = Integer.toString(getTitleTypeId());
      if (aGenreId == -1) {
         return getString(6301);
      } else if (aGenreId == -2) {
         return getString(6303);
      } else if (aGenreId == -3) {
         return getString(6302);
      } else {
         int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mGenreData, lNames, lValues);
         if (lIndex < 0) {
            logDetailMessage(
               DvdplayLevel.WARNING, "Could not find GenreId " + aGenreId + " for LocaleId " + getLocaleId() + " and TitleTypeId " + getTitleTypeId()
            );
            return "";
         } else {
            return DOMData.mGenreData.getFieldValue(lIndex, DOMData.mGenreData.getFieldIndex("Genre"));
         }
      }
   }

   public static PricingItem getPricing(int aPriceOptionId) {
      return Inventory.getPricing(aPriceOptionId);
   }

   public static PricingItem getPricing(int aPriceOptionId, Date aDate) {
      return Inventory.getPricing(aPriceOptionId, aDate);
   }

   public static boolean isCarouselFull() {
      return Inventory.isCarouselFull();
   }

   private static int getNextEmptySlot() throws InventoryException {
      return Inventory.getNextEmptySlot();
   }

   public int goToNextEmptySlot() throws InventoryException, ServoException {
      int lSlot = 0;

      for (int i = 0; i < this.getServo().getNumSlots(); i++) {
         try {
            lSlot = getNextEmptySlot();
         } catch (InventoryException var4) {
            throw new InventoryException(var4.getMessage());
         }

         logSummaryMessage("Next empty slot is " + lSlot + ".");

         try {
            if (this.getServo().goToSlot(lSlot) != 2) {
               break;
            }

            logDetailMessage(DvdplayLevel.WARNING, "Slot " + lSlot + " is not empty.");
            createDiscFound("", "", lSlot);
            addDiscFoundQueueJob();
            this.getServo().setServoParameters();
         } catch (ServoException var5) {
            throw new ServoException(var5.getMessage());
         }
      }

      return lSlot;
   }

   public static void updateDisc(Disc aDisc) {
      Inventory.updateDisc(aDisc);
   }

   public static void updateDisc(RCSet aDiscRCSet) {
      Inventory.updateDisc(aDiscRCSet);
   }

   public static boolean removeUnknownDisc(int aSlot) {
      return Inventory.removeUnknownDisc(aSlot);
   }

   public static boolean removeDisc(int aDiscDetailId, int aTitleDetailId, String aDiscCode, String aGroupCode, boolean aIsRemove) {
      return Inventory.removeDisc(aDiscDetailId, aTitleDetailId, aDiscCode, aGroupCode, aIsRemove);
   }

   public static void updateTitle(RCSet aTitleRCSet) {
      Inventory.updateTitle(aTitleRCSet);
   }

   public static void removeTitle(int aTitleDetailId, boolean aIsRemove) {
      Inventory.removeTitle(aTitleDetailId, aIsRemove);
   }

   public static DiscIndex getDiscIndex() {
      return Inventory.getDiscIndex();
   }

   public static int createIndex() {
      String[] values = new String[]{"1"};
      logDetailMessage(DvdplayLevel.FINE, "creating Index");

      for (int i = 0; i < DOMData.mIndexData.rowCount(); i++) {
         if (!DOMData.mIndexData.isDeleted(i)) {
            DOMData.mIndexData.deleteRow(i);
         }
      }

      DOMData.mIndexData.addRow(values);
      DOMData.save();
      return 1;
   }

   public static synchronized int getNextIndex() {
      int lIndex = 1;
      String[] lValues = new String[DvdplayBase.INDEX_FIELD_NAMES.length];
      int lMaxIndex = 100000;

      for (int i = 0; i < DOMData.mIndexData.rowCount(); i++) {
         if (!DOMData.mIndexData.isDeleted(i)) {
            lIndex = Integer.parseInt(DOMData.mIndexData.getFieldValue(i, DOMData.mIndexData.getFieldIndex("Index")));
            int lIncIndex = lIndex + 1;
            if (lIncIndex >= 100000) {
               lIncIndex = 1;
            }

            lValues[DOMData.mIndexData.getFieldIndex("Index")] = Integer.toString(lIncIndex);
            DOMData.mIndexData.deleteRow(i);
            DOMData.mIndexData.addRow(lValues);
            DOMData.save();
            return lIncIndex;
         }
      }

      return createIndex();
   }

   public static void addUnknownDisc(String aGroupCode, String aDiscCode, int aSlotNum) {
      Inventory.addUnknownDisc(aGroupCode, aDiscCode, aSlotNum);
   }

   public static void addUnknownDisc(String aGroupCode, String aDiscCode, int aSlotNum, boolean aIsFound) {
      Inventory.addUnknownDisc(aGroupCode, aDiscCode, aSlotNum, aIsFound);
   }

   public static int getDiscCount() {
      return Inventory.getDiscCount();
   }

   public static void updateAemProperties() {
      RCSet lRCSet = new RCSet(DvdplayBase.AEM_PROPERTIES_FIELD_NAMES, DvdplayBase.AEM_PROPERTIES_FIELD_TYPES);
      String[] lValues = new String[DvdplayBase.AEM_PROPERTIES_FIELD_NAMES.length];
      Servo lServo = ServoFactory.getInstance();
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoOffset")] = Integer.toString(lServo.getServoOffset());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoInputStep")] = Integer.toString(lServo.getServoInputStep());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoOutputStep")] = Integer.toString(lServo.getServoOutputStep());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoDiscThreshold")] = Integer.toString(lServo.getServoDiscThreshold());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoKp")] = Integer.toString(lServo.getServoKp());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoKp2")] = Integer.toString(lServo.getServoKp2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoKd")] = Integer.toString(lServo.getServoKd());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoKd2")] = Integer.toString(lServo.getServoKd2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoKi")] = Integer.toString(lServo.getServoKi());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoKi2")] = Integer.toString(lServo.getServoKi2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoIntegrationLimit")] = Integer.toString(lServo.getServoIntegrationLimit());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoIntegrationLimit2")] = Integer.toString(lServo.getServoIntegrationLimit2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoOutputLimit")] = Integer.toString(lServo.getServoOutputLimit());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoOutputLimit2")] = Integer.toString(lServo.getServoOutputLimit2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoCurrentLimit")] = Integer.toString(lServo.getServoCurrentLimit());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoCurrentLimit2")] = Integer.toString(lServo.getServoCurrentLimit2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoPositionErrorLimit")] = Integer.toString(lServo.getServoPositionErrorLimit());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoPositionErrorLimit2")] = Integer.toString(lServo.getServoPositionErrorLimit2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoRate")] = Integer.toString(lServo.getServoServoRate());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoRate2")] = Integer.toString(lServo.getServoServoRate2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoDeadband")] = Integer.toString(lServo.getServoDeadbandComp());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoDeadband2")] = Integer.toString(lServo.getServoDeadbandComp2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoVelocity")] = Integer.toString(lServo.getServoVelocity());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoVelocity2")] = Integer.toString(lServo.getServoVelocity2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoAcceleration")] = Integer.toString(lServo.getServoAcceleration());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoAcceleration2")] = Integer.toString(lServo.getServoAcceleration2());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoArmEjectWaitTime")] = Integer.toString(lServo.getArmEjectWaitTime());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoMoveToOffsetWaitTime")] = Integer.toString(lServo.getMoveToOffsetWaitTime());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoMoveToOffsetTimeout")] = Integer.toString(lServo.getMoveToOffsetTimeOut());
      if (lServo.isCaseInSensorReflective()) {
         lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoIsCaseInSensorReflective")] = Integer.toString(1);
      } else {
         lValues[DOMData.mAemPropertiesData.getFieldIndex("ServoIsCaseInSensorReflective")] = Integer.toString(0);
      }

      lValues[DOMData.mAemPropertiesData.getFieldIndex("DueTimeOfDay")] = Util.timeToString(mDueTime);
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ShutdownTime")] = Util.timeToString(mShutdownTime);
      lValues[DOMData.mAemPropertiesData.getFieldIndex("MediaDownloadStartTime")] = Util.timeToString(mTrailerDownloadStartTime);
      lValues[DOMData.mAemPropertiesData.getFieldIndex("MediaDownloadStopTime")] = Util.timeToString(mTrailerDownloadStopTime);
      lValues[DOMData.mAemPropertiesData.getFieldIndex("SendLogTime")] = Util.timeToString(mSendLogTime);
      lValues[DOMData.mAemPropertiesData.getFieldIndex("LastSynchDate")] = Util.dateToString(mLastSynchDate);
      lValues[DOMData.mAemPropertiesData.getFieldIndex("AemId")] = Integer.toString(getAemId());
      lValues[DOMData.mAemPropertiesData.getFieldIndex("ServerAddress")] = getServerAddress();
      lValues[DOMData.mAemPropertiesData.getFieldIndex("FTPAddress")] = getFTPAddress();
      lValues[DOMData.mAemPropertiesData.getFieldIndex("TaxRate")] = getTaxRate().toString();
      lValues[DOMData.mAemPropertiesData.getFieldIndex("DefaultLocaleId")] = Integer.toString(mDefaultLocaleId);
      lValues[DOMData.mAemPropertiesData.getFieldIndex("TimeZoneId")] = mTimeZoneId;
      lValues[DOMData.mAemPropertiesData.getFieldIndex("TimeZoneAutoAdj")] = Integer.toString(mTimeZoneAutoAdj);
      lRCSet.addRow(lValues);

      for (int i = 0; i < DOMData.mAemPropertiesData.rowCount(); i++) {
         if (!DOMData.mAemPropertiesData.isDeleted(i)) {
            DOMData.mAemPropertiesData.deleteRow(i);
         }
      }

      DOMData.mAemPropertiesData.addRow(lRCSet.getRow(0));
      DOMData.save();
   }

   public static void addBadSlot(int aBadSlotNum, int aBadSlotId) {
      Inventory.addBadSlot(aBadSlotNum, aBadSlotId);
   }

   public static void removeBadSlot(int aBadSlotNum) {
      Inventory.removeBadSlot(aBadSlotNum);
   }

   public static void createNewMediaDetailRecord(String aId, String aTypeId) {
      String[] values = new String[DvdplayBase.MEDIA_DETAIL_FIELD_NAMES.length];
      values[DOMData.mMediaDetailData.getFieldIndex("MediaId")] = aId;
      values[DOMData.mMediaDetailData.getFieldIndex("MediaTypeId")] = aTypeId;
      values[DOMData.mMediaDetailData.getFieldIndex("NumDownloaded")] = "0";
      values[DOMData.mMediaDetailData.getFieldIndex("MediaDownloaded")] = Integer.toString(0);
      values[DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded")] = Integer.toString(0);
      DOMData.mMediaDetailData.addRow(values);
      logDetailMessage(DvdplayLevel.FINE, "added MediaId " + aId + " MediaTypeId " + aTypeId + " to MediaDetail");
      DOMData.save();
   }

   public static void deleteMediaDetailRecord(String aId, String aTypeId) {
      String[] lNames = new String[2];
      String[] lValues = new String[2];
      logDetailMessage(DvdplayLevel.FINE, "Deleting MediaId " + aId + ", MediaTypeId " + aTypeId + " from MediaDetail");
      lNames[0] = "MediaId";
      lNames[1] = "MediaTypeId";
      lValues[0] = aId;
      lValues[1] = aTypeId;
      int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
      if (lIndex >= 0) {
         DOMData.mMediaDetailData.deleteRow(lIndex);
         DOMData.save();
      }
   }

   public static void createMediaPlayList() {
      Inventory.createMediaPlayList();
   }

   public static PlayListItem getNextMediaPlayListItem() {
      return Inventory.getNextMediaPlayListItem();
   }

   public static void createStaticPlayList() {
      Inventory.createStaticPlayList();
   }

   public static PlayListItem getNextStaticPlayListItem() {
      return Inventory.getNextStaticPlayListItem();
   }

   public static String getString(int aTextId) {
      try {
         String[] lNames = new String[2];
         String[] lValues = new String[2];
         lNames[0] = "TextId";
         lValues[0] = Integer.toString(aTextId);
         lNames[1] = "LocaleId";
         lValues[1] = Integer.toString(getLocaleId());
         int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mTranslationData, lNames, lValues);
         if (lIndex < 0) {
            logDetailMessage(DvdplayLevel.ERROR, "getString could not find TextId " + aTextId);
            return "";
         } else {
            return DOMData.mTranslationData.getFieldValue(lIndex, DOMData.mTranslationData.getFieldIndex("Text"));
         }
      } catch (DvdplayException var4) {
         logDetailMessage(DvdplayLevel.ERROR, "getString failed for TextId " + aTextId);
         logDetailMessage(DvdplayLevel.ERROR, var4.getMessage(), var4);
         return "";
      }
   }

   public static LocaleIndexItem getLocalIndexItem(int aIndex) {
      return Inventory.getLocaleIndexItem(aIndex);
   }

   public static LocaleIndexItem getLocaleIndexItemByLocaleId(int aLocaleId) {
      return Inventory.getLocaleIndexItemByLocaleId(aLocaleId);
   }

   public static int getLocaleIndexSize() {
      return Inventory.getLocaleIndexSize();
   }

   public static void getData(String aQueryName, RCSet aRCSet, String aQuery, Date aSinceDate, boolean aIsUpdate, String[] aKeyFieldNames) {
      getData(aQueryName, aRCSet, aQuery, aSinceDate, aIsUpdate, aKeyFieldNames, true);
   }

   public static void getData(String aQueryName, RCSet aRCSet, String aQuery, Date aSinceDate, boolean aIsUpdate, String[] aKeyFieldNames, boolean aUseWhere) {
      String mDateFormatString = "yyyyMMdd HH:mm:ss.SSS";
      TimeZone tz = TimeZone.getTimeZone("UTC");
      SimpleDateFormat formatter = new SimpleDateFormat(mDateFormatString);
      formatter.setTimeZone(tz);
      if (!isStandAlone()) {
         if (!aIsUpdate) {
            logSummaryMessage("Refreshing data: " + aQueryName);
         } else {
            logSummaryMessage("Updating data: " + aQueryName);
         }

         String lQuery;
         if (aUseWhere) {
            lQuery = aQuery + " where KioskId = " + getAemId() + " and LastUpdatedDate > '" + formatter.format(aSinceDate) + "'";
         } else {
            lQuery = aQuery + " where LastUpdatedDate > '" + formatter.format(aSinceDate) + "'";
         }

         String lrequeststring = new QueryRequest(getAemId(), "2.0", aQueryName, lQuery).getAsXmlString();
         logDetailMessage(DvdplayLevel.FINER, lrequeststring);
         String lRet = Comm.sendRequest(lrequeststring);
         DataPacketComposer ldpc = new DataPacketComposer();
         DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
         int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
         if (lResult != 0) {
            String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
            throw new DvdplayException("GetData " + aQueryName + " request failed: " + lMsg);
         } else {
            RCSet lDataRCSet = lRequest.getDatasetData()[0];
            if (aIsUpdate) {
               updateRCSet(lDataRCSet, aRCSet, aKeyFieldNames);
            } else {
               refreshRCSet(lDataRCSet, aRCSet);
            }
         }
      }
   }

   public static int getData(String aQueryName, RCSet aRCSet, String aQuery, boolean aIsUpdate, String[] aKeyFieldNames) {
      String mDateFormatString = "yyyyMMdd HH:mm:ss.SSS";
      TimeZone tz = TimeZone.getTimeZone("UTC");
      SimpleDateFormat formatter = new SimpleDateFormat(mDateFormatString);
      formatter.setTimeZone(tz);
      if (isStandAlone()) {
         return 0;
      } else {
         if (!aIsUpdate) {
            logSummaryMessage("Refreshing data: " + aQueryName);
         } else {
            logSummaryMessage("Updating data: " + aQueryName);
         }

         String lrequeststring = new QueryRequest(getAemId(), "2.0", aQueryName, aQuery).getAsXmlString();
         logDetailMessage(DvdplayLevel.FINER, lrequeststring);
         String lRet = Comm.sendRequest(lrequeststring);
         DataPacketComposer ldpc = new DataPacketComposer();
         DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
         int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
         if (lResult != 0) {
            String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
            throw new DvdplayException("GetData " + aQueryName + " request failed: " + lMsg);
         } else {
            RCSet lDataRCSet = lRequest.getDatasetData()[0];
            if (aIsUpdate) {
               updateRCSet(lDataRCSet, aRCSet, aKeyFieldNames, false);
            } else {
               refreshRCSet(lDataRCSet, aRCSet);
            }

            return lDataRCSet.rowCount();
         }
      }
   }

   private static void updateRCSet(RCSet aSourceRCSet, RCSet aTargetRCSet, String[] aKeyFieldNames) {
      updateRCSet(aSourceRCSet, aTargetRCSet, aKeyFieldNames, true);
   }

   private static void updateRCSet(RCSet aSourceRCSet, RCSet aTargetRCSet, String[] aKeyFieldNames, boolean aDoInsert) {
      String[] lValues = new String[aKeyFieldNames.length];
      int lIndex = -1;

      for (int i = 0; i < aSourceRCSet.rowCount(); i++) {
         if (!aSourceRCSet.isDeleted(i)) {
            for (int j = 0; j < aKeyFieldNames.length; j++) {
               lValues[j] = aSourceRCSet.getFieldValue(i, aSourceRCSet.getFieldIndex(aKeyFieldNames[j]));
            }

            boolean lDeletedRow = false;

            do {
               lIndex = Util.getRCSetIndexForFieldValue(aTargetRCSet, aKeyFieldNames, lValues);
               if (lIndex >= 0) {
                  aTargetRCSet.deleteRow(lIndex);
                  lDeletedRow = true;
               }
            } while (lIndex >= 0);

            if (!aDoInsert && !lDeletedRow) {
               setDataError();
               String lMsg = "";

               for (int k = 0; k < aKeyFieldNames.length; k++) {
                  lMsg = lMsg + aKeyFieldNames[k] + " " + lValues[k] + " ";
               }

               lMsg = lMsg + "not inserted.";
               logDetailMessage(DvdplayLevel.INFO, lMsg);
            } else {
               aTargetRCSet.addRow(aSourceRCSet.getRow(i));
            }
         }
      }
   }

   private static void refreshRCSet(RCSet aSourceRCSet, RCSet aTargetRCSet) {
      for (int i = 0; i < aTargetRCSet.rowCount(); i++) {
         if (!aTargetRCSet.isDeleted(i)) {
            aTargetRCSet.deleteRow(i);
         }
      }

      for (int ix = 0; ix < aSourceRCSet.rowCount(); ix++) {
         if (!aSourceRCSet.isDeleted(ix)) {
            aTargetRCSet.addRow(aSourceRCSet.getRow(ix));
         }
      }

      ((PersistenceData)aTargetRCSet).touch();
   }

   private static boolean isNewData(String aViewName, Date aSinceDate) {
      try {
         String mDateFormatString = "yyyyMMdd HH:mm:ss.SSS";
         TimeZone tz = TimeZone.getTimeZone("UTC");
         SimpleDateFormat formatter = new SimpleDateFormat(mDateFormatString);
         formatter.setTimeZone(tz);
         String lQuery = "select distinct KioskId from "
            + aViewName
            + " where KioskId = "
            + getAemId()
            + " and LastUpdatedDate > '"
            + formatter.format(aSinceDate)
            + "'";
         String lRequestString = new QueryRequest(getAemId(), "2.0", "isNewData query", lQuery).getAsXmlString();
         logDetailMessage(DvdplayLevel.FINER, lRequestString);
         String lRet = Comm.sendRequest(lRequestString);
         DataPacketComposer ldpc = new DataPacketComposer();
         DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
         int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
         if (lResult != 0) {
            String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
            throw new DvdplayException("isNewData request failed: " + lMsg);
         } else {
            RCSet lDataRCSet = lRequest.getDatasetData()[0];
            return lDataRCSet.rowCount() != 0;
         }
      } catch (Exception var12) {
         logDetailMessage(DvdplayLevel.ERROR, "Exception caught: " + var12.getMessage(), var12);
         return false;
      }
   }

   private static void getGroupCodeData(Date aSinceDate) {
      if (isNewData("ffKioskGroupCode", aSinceDate)) {
         refreshGroupCodeData();
      }
   }

   public static void refreshGroupCodeData() {
      getData(
         "GroupCode query", DOMData.mGroupCodeData, "select GroupCodeID 'GroupCodeId', GroupCode 'GroupCode' from ffKioskGroupCode", new Date(0L), false, null
      );
   }

   public static void updateGroupCodeData(Date aSinceDate) {
      String[] lNames = new String[]{"GroupCodeId"};
      getData(
         "GroupCode query", DOMData.mGroupCodeData, "select GroupCodeID 'GroupCodeId', GroupCode 'GroupCode' from ffKioskGroupCode", aSinceDate, true, lNames
      );
   }

   private static void getTranslationData(Date aSinceDate) {
      if (isNewData("ffKioskTextTranslation", aSinceDate)) {
         refreshTranslationData();
      }
   }

   public static void refreshTranslationData() {
      getData(
         "Translation query",
         DOMData.mTranslationData,
         "select TextID 'TextId', LocaleID 'LocaleId', Text 'Text' from ffKioskTextTranslation",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateTranslationData(Date aSinceDate) {
      String[] lNames = new String[]{"TextId", "LocaleId"};
      getData(
         "Translation query",
         DOMData.mTranslationData,
         "select TextID 'TextId', LocaleID 'LocaleId', Text 'Text' from ffKioskTextTranslation",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getGenreData(Date aSinceDate) {
      if (isNewData("ffKioskGenre", aSinceDate)) {
         refreshGenreData();
      }
   }

   public static void refreshGenreData() {
      getData(
         "Genre query",
         DOMData.mGenreData,
         "select GenreID 'GenreId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', Priority 'Priority', Genre 'Genre' from ffKioskGenre",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateGenreData(Date aSinceDate) {
      String[] lNames = new String[]{"GenreId", "LocaleId", "TitleTypeId"};
      getData(
         "Genre query",
         DOMData.mGenreData,
         "select GenreID 'GenreId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', Priority 'Priority', Genre 'Genre' from ffKioskGenre",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getRatingData(Date aSinceDate) {
      if (isNewData("ffKioskRating", aSinceDate)) {
         refreshRatingData();
      }
   }

   public static void refreshRatingData() {
      getData(
         "Rating query",
         DOMData.mRatingData,
         "select RatingID 'RatingId', RatingSystemID 'RatingSystemId', RatingCode 'RatingCode', RatingDesc 'RatingDesc' from ffKioskRating",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateRatingData(Date aSinceDate) {
      String[] lNames = new String[]{"RatingId", "RatingSystemId"};
      getData(
         "Rating query",
         DOMData.mRatingData,
         "select RatingID 'RatingId', RatingSystemID 'RatingSystemId', RatingCode 'RatingCode', RatingDesc 'RatingDesc' from ffKioskRating",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getRatingSystemData(Date aSinceDate) {
      if (isNewData("ffKioskRatingSystem", aSinceDate)) {
         refreshRatingSystemData();
      }
   }

   public static void refreshRatingSystemData() {
      getData(
         "RatingSystem query",
         DOMData.mRatingSystemData,
         "select RatingSystemId 'RatingSystemId', RatingSystem 'RatingSystem' from ffKioskRatingSystem",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateRatingSystemData(Date aSinceDate) {
      String[] lNames = new String[]{"RatingSystemId"};
      getData(
         "RatingSystem query",
         DOMData.mRatingSystemData,
         "select RatingSystemId 'RatingSystemId', RatingSystem 'RatingSystem' from ffKioskRatingSystem",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getRegularPricingData(Date aSinceDate) {
      if (isNewData("ffKioskRegularPricing", aSinceDate)) {
         refreshRegularPricingData();
      }
   }

   public static void refreshRegularPricingData() {
      getData(
         "RegularPricing query",
         DOMData.mRegularPricingData,
         "select PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', DayOfTheWeekID 'DayOfTheWeek', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskRegularPricing",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateRegularPricingData(Date aSinceDate) {
      String[] lNames = new String[]{"PriceOptionId", "PriceModelId", "DayOfTheWeek"};
      getData(
         "RegularPricing query",
         DOMData.mRegularPricingData,
         "select PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', DayOfTheWeekID 'DayOfTheWeek', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskRegularPricing",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getSpecialPricingData(Date aSinceDate) {
      if (isNewData("ffKioskSpecialPricing", aSinceDate)) {
         refreshSpecialPricingData();
      }
   }

   public static void refreshSpecialPricingData() {
      getData(
         "SpecialPricing query",
         DOMData.mSpecialPricingData,
         "select SpecialPricingID 'SpecialPricingId', PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', StartDate 'StartDate', EndDate 'EndDate', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskSpecialPricing",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateSpecialPricingData(Date aSinceDate) {
      String[] lNames = new String[]{"PriceOptionId", "PriceModelId", "DayOfTheWeek"};
      getData(
         "SpecialPricing query",
         DOMData.mSpecialPricingData,
         "select SpecialPricingID 'SpecialPricingId', PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', StartDate 'StartDate', EndDate 'EndDate', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskSpecialPricing",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getStaticPlaylistData(Date aSinceDate) {
      if (isNewData("ffGraphicPlayList", aSinceDate)) {
         refreshStaticPlaylistData();
      }
   }

   public static void refreshStaticPlaylistData() {
      getData(
         "GraphicPlaylist query",
         DOMData.mStaticPlaylistData,
         "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', StartDate 'StartDate', EndDate 'EndDate' from ffGraphicPlayList",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateStaticPlaylistData(Date aSinceDate) {
      String[] lNames = new String[]{"MediaId", "MediaTypeId"};
      getData(
         "GraphicPlaylist query",
         DOMData.mStaticPlaylistData,
         "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', StartDate 'StartDate', EndDate 'EndDate' from ffGraphicPlayList",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getLocaleData(Date aSinceDate) {
      if (isNewData("ffKioskLocale", aSinceDate)) {
         refreshLocaleData();
      }
   }

   public static void refreshLocaleData() {
      getData(
         "Locale query",
         DOMData.mLocaleData,
         "select LocaleID 'LocaleId', Language 'Language', Country 'Country' from ffKioskLocale",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateLocaleData(Date aSinceDate) {
      String[] lNames = new String[]{"LocaleId"};
      getData(
         "Locale query", DOMData.mLocaleData, "select LocaleID 'LocaleId', Language 'Language', Country 'Country' from ffKioskLocale", aSinceDate, true, lNames
      );
   }

   private static void getMediaPlaylistData(Date aSinceDate) {
      if (!isNewData("ffVideoPlayList", aSinceDate)) {
         logSummaryMessage("NELSON: no new mediaplaylist data");
      } else {
         refreshMediaPlaylistData();
      }
   }

   public static void refreshMediaPlaylistData() {
      getData(
         "VideoPlaylist query",
         DOMData.mMediaPlaylistData,
         "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', Attr1 'Attr1', StartDate 'StartDate', EndDate 'EndDate', SegmentName 'SegmentName', NumSegments 'NumSegments' from ffVideoPlayList",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateMediaPlaylistData(Date aSinceDate) {
      String[] lNames = new String[]{"MediaId", "MediaTypeId"};
      getData(
         "VideoPlaylist query",
         DOMData.mMediaPlaylistData,
         "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', Attr1 'Attr1', StartDate 'StartDate', EndDate 'EndDate', SegmentName 'SegmentName', NumSegments 'NumSegments' from ffVideoPlayList",
         aSinceDate,
         true,
         lNames
      );
   }

   private void getAemPropertiesData(Date aSinceDate) {
      if (isNewData("ffKioskBasicsAndSetup", aSinceDate)) {
         refreshAemPropertiesData();
      }
   }

   public static void refreshAemPropertiesData() {
      refreshAemPropertiesData(true);
   }

   public static void refreshAemPropertiesData(boolean aReadData) {
      getData(
         "Aem query",
         DOMData.mAemPropertiesData,
         "select KioskID 'AemId', convert(varchar(15), DueTimeOfDay, 108) 'DueTimeOfDay', TaxRate 'TaxRate', NoChargeTime 'NoChargeTime', convert(varchar(15), ShutDownTime, 108) 'ShutdownTime', convert(varchar(15), MediaDownloadStartTime, 108) 'MediaDownloadStartTime', convert(varchar(15), MediaDownloadStopTime, 108) 'MediaDownloadStopTime', ServerAddress 'ServerAddress', FTPAddress 'FTPAddress', DefaultLocaleID 'DefaultLocaleId', convert(varchar(30), LastSynchDate, 121) + ' UTC'LastSynchDate, TimeZoneId 'TimeZoneId', TimeZoneAutoAdj 'TimeZoneAutoAdj', convert(varchar(15), SendLogTime, 108) 'SendLogTime', ServoOffset 'ServoOffset', ServoInputStep 'ServoInputStep', ServoOutputStep 'ServoOutputStep', ServoDiscThreshold 'ServoDiscThreshold', ServoKp 'ServoKp', ServoKp2 'ServoKp2', ServoKd 'ServoKd', ServoKd2 'ServoKd2', ServoKi 'ServoKi', ServoKi2 'ServoKi2', ServoRate 'ServoRate', ServoRate2 'ServoRate2', ServoDeadband 'ServoDeadband', ServoDeadband2 'ServoDeadband2', ServoIntegrationLimit 'ServoIntegrationLimit', ServoIntegrationLimit2 'ServoIntegrationLimit2', ServoVelocity 'ServoVelocity', ServoVelocity2 'ServoVelocity2', ServoAcceleration 'ServoAcceleration', ServoAcceleration2 'ServoAcceleration2', ServoOutputLimit 'ServoOutputLimit', ServoOutputLimit2 'ServoOutputLimit2', ServoCurrentLimit 'ServoCurrentLimit', ServoCurrentLimit2 'ServoCurrentLimit2', ServoPositionErrorLimit 'ServoPositionErrorLimit', ServoPositionErrorLimit2 'ServoPositionErrorLimit2', ServoArmEjectWaitTime 'ServoArmEjectWaitTime', ServoMoveToOffsetWaitTime 'ServoMoveToOffsetWaitTime', ServoMoveToOffsetTimeout 'ServoMoveToOffsetTimeout', ServoIsCaseInSensorReflective 'ServoIsCaseInSensorReflective' from ffKioskBasicsAndSetup",
         new Date(0L),
         false,
         null
      );
      if (aReadData) {
         readAemProperties();
         ServoFactory.getInstance().readServoProperties();
      }

      DOMData.mAemPropertiesData2.save();
      DOMData.mCheckSumData2.save();
   }

   public static void updateAemPropertiesData(Date aSinceDate) {
      String mDateFormatString = "yyyyMMdd HH:mm:ss.SSS";
      TimeZone tz = TimeZone.getTimeZone("UTC");
      SimpleDateFormat formatter = new SimpleDateFormat(mDateFormatString);
      formatter.setTimeZone(tz);
      if (!isStandAlone()) {
         logSummaryMessage("Updating data: Aem query");
         String lQuery = "select KioskID 'AemId', convert(varchar(15), DueTimeOfDay, 108) 'DueTimeOfDay', TaxRate 'TaxRate', NoChargeTime 'NoChargeTime', convert(varchar(15), ShutDownTime, 108) 'ShutdownTime', convert(varchar(15), MediaDownloadStartTime, 108) 'MediaDownloadStartTime', convert(varchar(15), MediaDownloadStopTime, 108) 'MediaDownloadStopTime', ServerAddress 'ServerAddress', FTPAddress 'FTPAddress', DefaultLocaleID 'DefaultLocaleId', convert(varchar(30), LastSynchDate, 121) + ' UTC'LastSynchDate, TimeZoneId 'TimeZoneId', TimeZoneAutoAdj 'TimeZoneAutoAdj', convert(varchar(15), SendLogTime, 108) 'SendLogTime', ServoOffset 'ServoOffset', ServoInputStep 'ServoInputStep', ServoOutputStep 'ServoOutputStep', ServoDiscThreshold 'ServoDiscThreshold', ServoKp 'ServoKp', ServoKp2 'ServoKp2', ServoKd 'ServoKd', ServoKd2 'ServoKd2', ServoKi 'ServoKi', ServoKi2 'ServoKi2', ServoRate 'ServoRate', ServoRate2 'ServoRate2', ServoDeadband 'ServoDeadband', ServoDeadband2 'ServoDeadband2', ServoIntegrationLimit 'ServoIntegrationLimit', ServoIntegrationLimit2 'ServoIntegrationLimit2', ServoVelocity 'ServoVelocity', ServoVelocity2 'ServoVelocity2', ServoAcceleration 'ServoAcceleration', ServoAcceleration2 'ServoAcceleration2', ServoOutputLimit 'ServoOutputLimit', ServoOutputLimit2 'ServoOutputLimit2', ServoCurrentLimit 'ServoCurrentLimit', ServoCurrentLimit2 'ServoCurrentLimit2', ServoPositionErrorLimit 'ServoPositionErrorLimit', ServoPositionErrorLimit2 'ServoPositionErrorLimit2', ServoArmEjectWaitTime 'ServoArmEjectWaitTime', ServoMoveToOffsetWaitTime 'ServoMoveToOffsetWaitTime', ServoMoveToOffsetTimeout 'ServoMoveToOffsetTimeout', ServoIsCaseInSensorReflective 'ServoIsCaseInSensorReflective' from ffKioskBasicsAndSetup";
         lQuery = lQuery + " where KioskId = " + getAemId() + " and LastUpdatedDate > '" + formatter.format(aSinceDate) + "'";
         String lrequeststring = new QueryRequest(getAemId(), "2.0", "Aem query", lQuery).getAsXmlString();
         logDetailMessage(DvdplayLevel.FINER, lrequeststring);
         String lRet = Comm.sendRequest(lrequeststring);
         DataPacketComposer ldpc = new DataPacketComposer();
         DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
         int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
         if (lResult != 0) {
            String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
            throw new DvdplayException("GetData Aem query request failed: " + lMsg);
         } else {
            RCSet lDataRCSet = lRequest.getDatasetData()[0];
            if (lDataRCSet.rowCount() > 0) {
               RDataSetFieldValues lRDataSetFieldValues = lDataRCSet.getRow(0);
               String lCurrentField = "DueTimeOfDay";
               mDueTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("DueTimeOfDay")));
               lCurrentField = "ShutdownTime";
               mShutdownTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ShutdownTime")));
               lCurrentField = "MediaDownloadStartTime";
               mTrailerDownloadStartTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("MediaDownloadStartTime")));
               lCurrentField = "MediaDownloadStopTime";
               mTrailerDownloadStopTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("MediaDownloadStopTime")));
               lCurrentField = "SendLogTime";
               mSendLogTime = Util.stringToTime(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("SendLogTime")));
               lCurrentField = "LastSynchDate";
               mLastSynchDate = Util.stringToDate(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("LastSynchDate")));
               lCurrentField = "AemId";
               mAemId = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("AemId")));
               lCurrentField = "ServerAddress";
               mServerAddress = lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("ServerAddress"));
               lCurrentField = "FTPAddress";
               mFTPAddress = lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("FTPAddress"));
               lCurrentField = "TaxRate";
               mTaxRate = new BigDecimal(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("TaxRate")));
               lCurrentField = "DefaultLocaleId";
               mDefaultLocaleId = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("DefaultLocaleId")));
               lCurrentField = "TimeZoneId";
               mTimeZoneId = lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("TimeZoneId"));
               lCurrentField = "TimeZoneAutoAdj";
               mTimeZoneAutoAdj = Integer.parseInt(lRDataSetFieldValues.getValue(DOMData.mAemPropertiesData.getFieldIndex("TimeZoneAutoAdj")));
               updateAemProperties();
               DOMData.save();
            }
         }
      }
   }

   private static void getDiscDetailData(Date aSinceDate) {
      if (isNewData("ffDiscInventory", aSinceDate)) {
         refreshDiscDetailData();
      }
   }

   public static void refreshDiscDetailData() {
      String mDateFormatString = "yyyyMMdd HH:mm:ss.SSS";
      TimeZone tz = TimeZone.getTimeZone("UTC");
      SimpleDateFormat formatter = new SimpleDateFormat(mDateFormatString);
      formatter.setTimeZone(tz);
      String lQuery = "select DiscDetailID 'DiscDetailId', TitleID 'TitleDetailId', DiscStatusID 'DiscStatusId', PriceOptionID 'PriceOptionId', DiscCode 'DiscCode', GroupCode 'GroupCode', Slot 'Slot', Priority 'Priority', MarkedForSale 'MarkedForSale', MarkedForRent 'MarkedForRent', MarkedForRemoval 'MarkedForRemoval', RemovalDate 'RemovalDate', convert(varchar(30), LastUpdatedDate, 121) + ' UTC'DTUpdated from ffDiscInventory";
      lQuery = lQuery
         + " where KioskId = "
         + getAemId()
         + " and LastUpdatedDate > '"
         + formatter.format(new Date(0L))
         + "' "
         + "and "
         + "DiscStatusId"
         + " in ("
         + 3
         + ","
         + 9
         + ")";
      getData("DiscDetail query", DOMData.mDiscDetailData, lQuery, false, null);
   }

   public static void updateDiscDetailData(Date aSinceDate) {
      String mDateFormatString = "yyyyMMdd HH:mm:ss.SSS";
      TimeZone tz = TimeZone.getTimeZone("UTC");
      SimpleDateFormat formatter = new SimpleDateFormat(mDateFormatString);
      formatter.setTimeZone(tz);
      String[] lNames = new String[]{"DiscDetailId"};
      String lQuery = "select DiscDetailID 'DiscDetailId', TitleID 'TitleDetailId', DiscStatusID 'DiscStatusId', PriceOptionID 'PriceOptionId', DiscCode 'DiscCode', GroupCode 'GroupCode', Slot 'Slot', Priority 'Priority', MarkedForSale 'MarkedForSale', MarkedForRent 'MarkedForRent', MarkedForRemoval 'MarkedForRemoval', RemovalDate 'RemovalDate', convert(varchar(30), LastUpdatedDate, 121) + ' UTC'DTUpdated from ffDiscInventory";
      lQuery = lQuery
         + " where KioskId = "
         + getAemId()
         + " and LastUpdatedDate > '"
         + formatter.format(aSinceDate)
         + "' "
         + "and "
         + "DiscStatusId"
         + " in ("
         + 3
         + ","
         + 9
         + ")";
      getData("DiscDetail query", DOMData.mDiscDetailData, lQuery, true, lNames);
   }

   public void updateDiscDetailMarkedForRemovalData() {
      try {
         logSummaryMessage("Updating MarkedForRemoval data");
         String mDateFormatString = "yyyyMMdd HH:mm:ss.SSS";
         TimeZone tz = TimeZone.getTimeZone("UTC");
         SimpleDateFormat formatter = new SimpleDateFormat(mDateFormatString);
         formatter.setTimeZone(tz);
         String[] lNames = new String[]{"DiscDetailId"};
         String lQuery = "select DiscDetailID 'DiscDetailId', TitleID 'TitleDetailId', DiscStatusID 'DiscStatusId', PriceOptionID 'PriceOptionId', DiscCode 'DiscCode', GroupCode 'GroupCode', Slot 'Slot', Priority 'Priority', MarkedForSale 'MarkedForSale', MarkedForRent 'MarkedForRent', MarkedForRemoval 'MarkedForRemoval', RemovalDate 'RemovalDate', convert(varchar(30), LastUpdatedDate, 121) + ' UTC'DTUpdated from ffDiscInventory";
         lQuery = lQuery
            + " where KioskId = "
            + getAemId()
            + " and LastUpdatedDate > '"
            + formatter.format(mUpdateMarkedForRemovalSyncDate)
            + "' "
            + "and "
            + "MarkedForRemoval"
            + " = 1 "
            + "and "
            + "DiscStatusId"
            + " in ("
            + 3
            + ","
            + 9
            + ")";
         int lNumRecords = getData("MarkedForRemoval query", DOMData.mDiscDetailData, lQuery, true, lNames);
         DOMData.save();
         if (lNumRecords > 0) {
            this.createAllIndexes();
         }

         mUpdateMarkedForRemovalSyncDate = new Date();
      } catch (Exception var11) {
         logDetailMessage(DvdplayLevel.WARNING, var11.getMessage(), var11);
      } finally {
         mUpdateMarkedForRemoval = false;
      }
   }

   private static void getTitleDetailData(Date aSinceDate) {
      if (isNewData("ffKioskTitle", aSinceDate)) {
         refreshTitleDetailData();
      }
   }

   public static void refreshTitleDetailData() {
      String lQuery = "select TitleID 'TitleDetailId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', OriginalTitle 'Title', TranslatedTitle 'TranslatedTitle', SortTitle 'SortTitle', ShortTitle 'ShortTitle', Genre1ID 'Genre1Id', Genre2ID 'Genre2Id', Genre3ID 'Genre3Id', Description 'Description', Poster 'Poster', Trailer 'Trailer', RatingSystem1ID 'RatingSystem1Id', RatingSystem2ID 'RatingSystem2Id', RatingSystem3ID 'RatingSystem3Id', Rating1ID 'Rating1Id', Rating2ID 'Rating2Id', Rating3ID 'Rating3Id', convert(varchar(30), StreetDate, 121) + ' ";
      lQuery = lQuery + Util.getTimeZoneString();
      lQuery = lQuery
         + "'StreetDate, ReleaseYear 'ReleaseYear', Attr1 'Attr1', Attr2 'Attr2', Attr3 'Attr3', Attr4 'Attr4', Attr5 'Attr5', Attr6 'Attr6' from ffKioskTitle";
      getData("TitleDetail query", DOMData.mTitleDetailData, lQuery, new Date(0L), false, null);
   }

   public static void updateTitleDetailData(Date aSinceDate) {
      String lQuery = "select TitleID 'TitleDetailId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', OriginalTitle 'Title', TranslatedTitle 'TranslatedTitle', SortTitle 'SortTitle', ShortTitle 'ShortTitle', Genre1ID 'Genre1Id', Genre2ID 'Genre2Id', Genre3ID 'Genre3Id', Description 'Description', Poster 'Poster', Trailer 'Trailer', RatingSystem1ID 'RatingSystem1Id', RatingSystem2ID 'RatingSystem2Id', RatingSystem3ID 'RatingSystem3Id', Rating1ID 'Rating1Id', Rating2ID 'Rating2Id', Rating3ID 'Rating3Id', convert(varchar(30), StreetDate, 121) + ' ";
      lQuery = lQuery + Util.getTimeZoneString();
      lQuery = lQuery
         + "'StreetDate, ReleaseYear 'ReleaseYear', Attr1 'Attr1', Attr2 'Attr2', Attr3 'Attr3', Attr4 'Attr4', Attr5 'Attr5', Attr6 'Attr6' from ffKioskTitle";
      String[] lNames = new String[]{"TitleDetailId", "LocaleId"};
      getData("TitleDetail query", DOMData.mTitleDetailData, lQuery, aSinceDate, true, lNames);
   }

   private static void getPaymentCardData(Date aSinceDate) {
      if (isNewData("ffKioskPaymentCard", aSinceDate)) {
         refreshPaymentCardData();
      }
   }

   public static void refreshPaymentCardData() {
      getData(
         "PaymentCardType query",
         DOMData.mPaymentCardTypeData,
         "select PaymentCardCategoryID 'PaymentCardCategoryId', PaymentCardTypeID 'PaymentCardTypeId', LocaleID 'LocaleId', Priority 'Priority', SecureMode 'VerificationTypeId', PaymentPicture 'PaymentPicture', PaymentCardName 'PaymentCardTypeName' from ffKioskPaymentCard",
         new Date(0L),
         false,
         null
      );
   }

   public static void updatePaymentCardData(Date aSinceDate) {
      String[] lNames = new String[]{"PaymentCardTypeId"};
      getData(
         "PaymentCardType query",
         DOMData.mPaymentCardTypeData,
         "select PaymentCardCategoryID 'PaymentCardCategoryId', PaymentCardTypeID 'PaymentCardTypeId', LocaleID 'LocaleId', Priority 'Priority', SecureMode 'VerificationTypeId', PaymentPicture 'PaymentPicture', PaymentCardName 'PaymentCardTypeName' from ffKioskPaymentCard",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getTitleTypeData(Date aSinceDate) {
      if (isNewData("ffKioskTitleType", aSinceDate)) {
         refreshTitleTypeData();
      }
   }

   public static void refreshTitleTypeData() {
      getData(
         "TitleType query",
         DOMData.mTitleTypeData,
         "select TitleTypeId 'TitleTypeId', LocaleID 'LocaleId', Priority 'Priority', TitleTypeName 'TitleType', TitleTypeName2 'TitleTypeSingular' from ffKioskTitleType",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateTitleTypeData(Date aSinceDate) {
      String[] lNames = new String[]{"TitleTypeId", "LocaleId"};
      getData(
         "TitleType query",
         DOMData.mTitleTypeData,
         "select TitleTypeId 'TitleTypeId', LocaleID 'LocaleId', Priority 'Priority', TitleTypeName 'TitleType', TitleTypeName2 'TitleTypeSingular' from ffKioskTitleType",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getTitleTypeCapData(Date aSinceDate) {
      if (isNewData("ffKioskTitleTypeCap", aSinceDate)) {
         refreshTitleTypeCapData();
      }
   }

   public static void refreshTitleTypeCapData() {
      getData(
         "TitleTypeCap query",
         DOMData.mTitleTypeCapData,
         "select TitleTypeID 'TitleTypeId', CapTypeID 'CapTypeId', CapValue 'Value' from ffKioskTitleTypeCap",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateTitleTypeCapData(Date aSinceDate) {
      String[] lNames = new String[]{"TitleTypeId", "CapTypeId"};
      getData(
         "TitleTypeCap query",
         DOMData.mTitleTypeCapData,
         "select TitleTypeID 'TitleTypeId', CapTypeID 'CapTypeId', CapValue 'Value' from ffKioskTitleTypeCap",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getSecurityData(Date aSinceDate) {
      if (isNewData("ffKioskRoleLogin", aSinceDate)) {
         refreshSecurityData();
      }
   }

   public static void refreshSecurityData() {
      getData(
         "Security query",
         DOMData.mSecurityData,
         "select LoginID 'UserId', UserName 'UserName', Password 'UserPassword', RoleID 'RoleId' from ffKioskRoleLogin",
         new Date(0L),
         false,
         null
      );
   }

   public static void updateSecurityData(Date aSinceDate) {
      String[] lNames = new String[]{"UserId"};
      getData(
         "Security query",
         DOMData.mSecurityData,
         "select LoginID 'UserId', UserName 'UserName', Password 'UserPassword', RoleID 'RoleId' from ffKioskRoleLogin",
         aSinceDate,
         true,
         lNames
      );
   }

   private static void getPrivilegesData(Date aSinceDate) {
      refreshPrivilegesData();
   }

   public static void refreshPrivilegesData() {
      getData("Privileges query", DOMData.mPrivilegesData, "select RoleID 'RoleId', PageID 'PageId' from ffKioskRolePage", new Date(0L), false, null, false);
   }

   public static void updatePrivilegesData(Date aSinceDate) {
      String[] lNames = new String[]{"RoleId", "PageId"};
      getData("Privileges query", DOMData.mPrivilegesData, "select RoleID 'RoleId', PageID 'PageId' from ffKioskRolePage", aSinceDate, true, lNames, false);
   }

   private static void getSlotOffsetData(Date aSinceDate) {
      if (isNewData("ffKioskSlotOffset", aSinceDate)) {
         refreshSlotOffsetData();
      }
   }

   public static void refreshSlotOffsetData() {
      getData("SlotOffset query", DOMData.mSlotOffsetData, "select Slot 'Slot', Offset 'Offset' from ffKioskSlotOffset", new Date(0L), false, null);
   }

   public static void updateSlotOffsetData(Date aSinceDate) {
      String[] lNames = new String[]{"Slot"};
      getData("SlotOffset query", DOMData.mSlotOffsetData, "select Slot 'Slot', Offset 'Offset' from ffKioskSlotOffset", aSinceDate, true, lNames);
   }

   private static void getBadSlotData(Date aSinceDate) {
      if (isNewData("ffKioskBadSlot", aSinceDate)) {
         refreshBadSlotData();
      }
   }

   public static void refreshBadSlotData() {
      getData("BadSlot query", DOMData.mBadSlotData, "Select Slot 'BadSlot', '1' 'BadSlotId' from ffKioskBadSlot", new Date(0L), false, null);
   }

   public static void updateBadSlotData(Date aSinceDate) {
      String[] lNames = new String[]{"BadSlot"};
      getData("BadSlot query", DOMData.mBadSlotData, "Select Slot 'BadSlot', '1' 'BadSlotId' from ffKioskBadSlot", aSinceDate, true, lNames);
   }

   private static void getPollData(Date aSinceDate) {
      if (isNewData("ffKioskPoll", aSinceDate)) {
         refreshPollData();
      }
   }

   public static void refreshPollData() {
      getData(
         "KioskPoll query",
         DOMData.mPollData,
         "select PollID 'PollId', PollTypeID 'PollTypeId', LocaleID 'LocaleId', SeqNum 'Priority', OrderNum 'OrderNum', PollText 'PollText', StartDate 'StartDate', EndDate 'EndDate' from ffKioskPoll",
         new Date(0L),
         false,
         null
      );
   }

   public static void updatePollData(Date aSinceDate) {
      String[] lNames = new String[]{"PollId", "PollTypeId", "LocaleId", "OrderNum"};
      getData(
         "KioskPoll query",
         DOMData.mPollData,
         "select PollID 'PollId', PollTypeID 'PollTypeId', LocaleID 'LocaleId', SeqNum 'Priority', OrderNum 'OrderNum', PollText 'PollText', StartDate 'StartDate', EndDate 'EndDate' from ffKioskPoll",
         aSinceDate,
         true,
         lNames
      );
   }

   public void updateAllData(Date aSinceDate) {
      Date lnow = new Date();
      updateGroupCodeData(aSinceDate);
      updateTranslationData(aSinceDate);
      updateGenreData(aSinceDate);
      updateRatingData(aSinceDate);
      updateRatingSystemData(aSinceDate);
      refreshRegularPricingData();
      updateSpecialPricingData(aSinceDate);
      refreshStaticPlaylistData();
      updateLocaleData(aSinceDate);
      refreshMediaPlaylistData();
      updateAemPropertiesData(aSinceDate);
      updateDiscDetailData(aSinceDate);
      updateTitleDetailData(aSinceDate);
      updatePaymentCardData(aSinceDate);
      refreshTitleTypeData();
      refreshTitleTypeCapData();
      updateSecurityData(aSinceDate);
      updatePrivilegesData(aSinceDate);
      refreshPollData();
      setLastSynchDate(lnow);
   }

   public void refreshAllData() {
      this.refreshAllData(true);
   }

   public void refreshAllData(boolean aReadData) {
      Date lnow = new Date();
      refreshGroupCodeData();
      refreshTranslationData();
      refreshGenreData();
      refreshRatingData();
      refreshRatingSystemData();
      refreshRegularPricingData();
      refreshSpecialPricingData();
      refreshStaticPlaylistData();
      refreshLocaleData();
      refreshMediaPlaylistData();
      refreshAemPropertiesData(aReadData);
      refreshDiscDetailData();
      refreshTitleDetailData();
      refreshPaymentCardData();
      refreshTitleTypeData();
      refreshTitleTypeCapData();
      refreshSecurityData();
      refreshPrivilegesData();
      refreshBadSlotData();
      refreshSlotOffsetData();
      refreshPollData();
      setLastSynchDate(lnow);
   }

   public static Date getLastSynchDate() {
      return mLastSynchDate;
   }

   public static void setLastSynchDate() {
      setLastSynchDate(new Date(System.currentTimeMillis()));
   }

   public static void setLastSynchDate(Date aDate) {
      mLastSynchDate = aDate;
   }

   public static void resetLastSynchDate() {
      mLastSynchDate.setTime(0L);
      updateAemProperties();
   }

   public static String getRatingSystem(int aRatingSystemId) {
      int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mRatingSystemData, "RatingSystemId", Integer.toString(aRatingSystemId));
      if (lIndex < 0) {
         logDetailMessage(DvdplayLevel.WARNING, "Could not find ratingsystemid " + aRatingSystemId);
         return "";
      } else {
         return DOMData.mRatingSystemData.getFieldValue(lIndex, DOMData.mRatingSystemData.getFieldIndex("RatingSystem"));
      }
   }

   public static String getRating(int aRatingId, int aRatingSystemId) {
      String[] lNames = new String[2];
      String[] lValues = new String[2];
      lNames[0] = "RatingId";
      lNames[1] = "RatingSystemId";
      lValues[0] = Integer.toString(aRatingId);
      lValues[1] = Integer.toString(aRatingSystemId);
      int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mRatingData, lNames, lValues);
      if (lIndex < 0) {
         logDetailMessage(DvdplayLevel.WARNING, "Could not find ratingsystemid " + aRatingSystemId + " and ratingid " + aRatingId);
         return "";
      } else {
         return DOMData.mRatingData.getFieldValue(lIndex, DOMData.mRatingData.getFieldIndex("RatingCode"));
      }
   }

   public static BigInteger getCheckSum(String aFilename, String aDisplayName, PersistenceData aCheckSumPData) throws DvdplayException {
      int lIndex = Util.getRCSetIndexForFieldValue(aCheckSumPData, "Filename", aFilename);
      if (lIndex < 0) {
         throw new DvdplayException(aDisplayName + " not found in " + aCheckSumPData.getDisplayName());
      } else {
         return new BigInteger(aCheckSumPData.getFieldValue(lIndex, aCheckSumPData.getFieldIndex("CheckSum")));
      }
   }

   public static String getCurrencySymbol() {
      return Currency.getInstance(getLocale()).getSymbol();
   }

   public static String getTitle(int aTitleDetailId) {
      String[] lNames = new String[2];
      String[] lValues = new String[2];
      lNames[0] = "TitleDetailId";
      lNames[1] = "LocaleId";
      lValues[0] = Integer.toString(aTitleDetailId);
      lValues[1] = Integer.toString(getLocaleId());
      int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mTitleDetailData, lNames, lValues);
      if (lIndex < 0) {
         logDetailMessage(DvdplayLevel.ERROR, "Could not find TitleDetailId " + aTitleDetailId + ".");
         return "";
      } else {
         return DOMData.mTitleDetailData.getFieldValue(lIndex, DOMData.mTitleDetailData.getFieldIndex("Title"));
      }
   }

   public static void addTempBadSlot(int aBadSlotNum) {
      Inventory.addTempBadSlot(aBadSlotNum);
   }

   public static int getVerificationType(int aPaymentCategoryType) {
      try {
         int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mPaymentCardTypeData, "PaymentCardCategoryId", Integer.toString(aPaymentCategoryType));
         return lIndex < 0
            ? 0
            : Integer.parseInt(DOMData.mPaymentCardTypeData.getFieldValue(lIndex, DOMData.mPaymentCardTypeData.getFieldIndex("VerificationTypeId")));
      } catch (DvdplayException var2) {
         logDetailMessage(DvdplayLevel.ERROR, var2.getMessage());
         return 0;
      }
   }

   public static boolean checkGroupCode(String aGroupCode) {
      try {
         return Util.getRCSetIndexForFieldValue(DOMData.mGroupCodeData, "GroupCode", aGroupCode) >= 0;
      } catch (DvdplayException var2) {
         logDetailMessage(DvdplayLevel.ERROR, var2.getMessage());
         return false;
      }
   }

   private static void restartComputer() {
      shutdownComputer(true);
   }

   private static void shutdownComputer() {
      shutdownComputer(false);
   }

   private static void shutdownComputer(boolean aRestart) {
      String lBatFile = "c:\\aem\\content\\trailers\\" + Util.getTimeStamp() + ".bat";
      String lBatCmd = "@echo off\n\nc:\\windows\\system32\\shutdown.exe -t 1 -f";
      if (aRestart) {
         lBatCmd = lBatCmd + " -r";
      } else {
         lBatCmd = lBatCmd + " -s";
      }

      try {
         BufferedWriter bfout = new BufferedWriter(new FileWriter(lBatFile));
         bfout.write(lBatCmd);
         bfout.flush();
         bfout.close();
         Runtime.getRuntime().exec(lBatFile);
         Util.sleep(1000);
         File lTmpFile = new File(lBatFile);
         if (!lTmpFile.delete()) {
            logDetailMessage(DvdplayLevel.WARNING, "Failed to delete " + lBatFile);
         }
      } catch (Exception var5) {
         logDetailMessage(DvdplayLevel.ERROR, "Failed to restart computer");
         logSummaryMessage("Failed to restart computer");
         logDetailMessage(DvdplayLevel.ERROR, var5.getMessage());
      }
   }

   public static void createDiscMissing(Disc aDisc) {
      try {
         setInventoryError();
         mDiscMissing.addDiscItem(new DiscItem(aDisc, 5));
      } catch (DvdplayException var2) {
         logDetailMessage(DvdplayLevel.ERROR, "createDiscMissing failed: " + var2.getMessage());
      }
   }

   public static void addDiscMissingQueueJob() {
      try {
         if (mDiscMissing.getDiscItemListCount() <= 0) {
            return;
         }

         mDiscMissing.mNvPairSet = mDiscMissing.getDiscMissingNvPairSet();
         mQueue.addQueueJob(new QueueJob(new DiscMissing(mDiscMissing)), true);
         mDiscMissing = new DiscMissing();
      } catch (DvdplayException var1) {
         logDetailMessage(DvdplayLevel.ERROR, "addDiscMissingQueueJob failed: " + var1.getMessage());
      }
   }

   public static void createDiscFound(String aGroupCode, String aDiscCode, int aSlot) {
      try {
         setInventoryError();
         Disc lDisc = new Disc();
         lDisc.setGroupCode(aGroupCode);
         lDisc.setDiscCode(aDiscCode);
         lDisc.setSlot(aSlot);
         mDiscFound.addDiscItem(new DiscItem(lDisc, 6));
         addUnknownDisc(aGroupCode, aDiscCode, aSlot, true);
      } catch (DvdplayException var4) {
         logDetailMessage(DvdplayLevel.ERROR, "createDiscFound failed: " + var4.getMessage());
      }
   }

   public static void addDiscFoundQueueJob() {
      try {
         if (mDiscFound.getDiscItemListCount() <= 0) {
            return;
         }

         mDiscFound.mNvPairSet = mDiscFound.getDiscFoundNvPairSet();
         mQueue.addQueueJob(new QueueJob(new DiscFound(mDiscFound)), true);
         mDiscFound = new DiscFound();
      } catch (DvdplayException var1) {
         logDetailMessage(DvdplayLevel.ERROR, "addDiscFoundQueueJob failed: " + var1.getMessage());
      }
   }

   public static int getVolume() {
      try {
         return BarCodeReader.getVolume();
      } catch (Exception var1) {
         logDetailMessage(DvdplayLevel.ERROR, var1.getMessage());
         return 0;
      }
   }

   public static void setVolume(int aVol) {
      try {
         BarCodeReader.setVolume(aVol);
      } catch (Exception var2) {
         logDetailMessage(DvdplayLevel.ERROR, var2.getMessage());
      }
   }

   public static void startButton() {
      try {
         BarCodeReader.startButton();
      } catch (Exception var1) {
         logDetailMessage(DvdplayLevel.ERROR, var1.getMessage());
      }
   }

   public static int createTitleTypeList() {
      return Inventory.createTitleTypeList();
   }

   public static TitleTypeIndexItem getTitleTypeIndexItem(int aIndex) {
      return Inventory.getTitleTypeIndexItem(aIndex);
   }

   public static TitleTypeIndexItem getTitleTypeIndexItemByTitleTypeId(int aTitleTypeId) {
      return Inventory.getTitleTypeIndexItemByTitleTypeId(aTitleTypeId);
   }

   public static int createPaymentCardTypeList() {
      return Inventory.createPaymentCardTypeList();
   }

   public static PaymentCardTypeIndexItem getPaymentCardTypeIndexItem(int aIndex) {
      return Inventory.getPaymentCardTypeIndexItem(aIndex);
   }

   public static PaymentCardTypeIndexItem getPaymentCardTypeIndexItemByPaymentCardTypeId(int aIndex) {
      return Inventory.getPaymentCardTypeIndexItemByPaymentCardTypeId(aIndex);
   }

   public static int createPollList() {
      return Inventory.createPollList();
   }

   public static PollItem getPollIndexItem(int aIndex) {
      return Inventory.getPollIndexItem(aIndex);
   }

   public static void clearPollResponses() {
      Inventory.clearPollResponses();
   }

   public static void getPaymentPictures() {
      if (!isStandAlone()) {
         logSummaryMessage("Getting Payment Pictures ...");

         for (int i = 0; i < DOMData.mPaymentCardTypeData.rowCount(); i++) {
            if (!DOMData.mPaymentCardTypeData.isDeleted(i)) {
               String lPaymentCardTypeId = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("PaymentCardTypeId"));
               String[] lNames = new String[2];
               String[] lValues = new String[2];
               lNames[0] = "MediaId";
               lValues[0] = lPaymentCardTypeId;
               lNames[1] = "MediaTypeId";
               lValues[1] = Integer.toString(-1);
               int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
               if (lIndex < 0) {
                  createNewMediaDetailRecord(lPaymentCardTypeId, Integer.toString(-1));
                  lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
               }

               boolean lIsDownloaded;
               if (Integer.parseInt(DOMData.mMediaDetailData.getFieldValue(lIndex, DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"))) == 1) {
                  lIsDownloaded = true;
               } else {
                  lIsDownloaded = false;
               }

               if (!lIsDownloaded) {
                  String lPosterFile = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("PaymentPicture"));

                  try {
                     Comm.downloadFile("/webdav/posters/" + lPosterFile, "c:\\aem\\content\\images\\" + lPosterFile);
                     RDataSetFieldValues lRDataSetFieldValues = DOMData.mMediaDetailData.getRow(lIndex);
                     lRDataSetFieldValues.setValue(DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"), Integer.toString(1));
                     DOMData.mMediaDetailData.addRow(lRDataSetFieldValues);
                     DOMData.mMediaDetailData.deleteRow(lIndex);
                  } catch (DvdplayException var9) {
                     logDetailMessage(DvdplayLevel.WARNING, var9.getMessage());
                  }
               }
            }
         }

         DOMData.save();
      }
   }

   public static void getFranchiseLogo() {
      getFranchiseLogo(false);
   }

   public static void getFranchiseLogo(boolean aForce) {
      logSummaryMessage("Getting AboutFranchise ...");

      for (int i = 0; i < getLocaleIndexSize(); i++) {
         getFranchiseLogo(aForce, getLocalIndexItem(i).getLocaleId());
      }
   }

   public static void getFranchiseLogo(boolean aForce, int aLocaleId) {
      if (!isStandAlone()) {
         String[] lNames = new String[2];
         String[] lValues = new String[2];
         lNames[0] = "MediaId";
         lValues[0] = Integer.toString(aLocaleId);
         lNames[1] = "MediaTypeId";
         lValues[1] = Integer.toString(-2);
         int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
         if (lIndex < 0) {
            createNewMediaDetailRecord(Integer.toString(aLocaleId), Integer.toString(-2));
            lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
         }

         boolean lIsDownloaded;
         if (Integer.parseInt(DOMData.mMediaDetailData.getFieldValue(lIndex, DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"))) == 1) {
            lIsDownloaded = true;
         } else {
            lIsDownloaded = false;
         }

         if (!lIsDownloaded || aForce) {
            try {
               String lLogoFile = "About-" + mFranchiseId + "-" + aLocaleId + ".zip";
               Comm.downloadFile("/webdav/html/" + lLogoFile, "c:\\aem\\content\\html\\" + lLogoFile);
               RDataSetFieldValues lRDataSetFieldValues = DOMData.mMediaDetailData.getRow(lIndex);
               lRDataSetFieldValues.setValue(DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"), Integer.toString(1));
               DOMData.mMediaDetailData.addRow(lRDataSetFieldValues);
               DOMData.mMediaDetailData.deleteRow(lIndex);
               String lDoneFlag = Util.getTimeStamp() + "DONE";
               String lBatFile = "c:\\aem\\tmp\\" + Util.getTimeStamp() + ".bat";
               String lBatCmd = "@echo off\n\ncd c:\\aem\\content\\html\\";
               lBatCmd = lBatCmd + "\n\nunzip32 -oqq c:\\aem\\content\\html\\" + lLogoFile;
               lBatCmd = lBatCmd + "\n\necho " + lDoneFlag;
               BufferedWriter bfout = new BufferedWriter(new FileWriter(lBatFile));
               bfout.write(lBatCmd);
               bfout.flush();
               bfout.close();
               ExecCommandLine.getExecCommandLine().exec(lBatFile, 60000, lDoneFlag);
               File lTmpFile = new File(lBatFile);
               if (!lTmpFile.delete()) {
                  logDetailMessage(DvdplayLevel.WARNING, "Failed to delete " + lBatFile);
               }

               lTmpFile = new File("c:\\aem\\content\\html\\" + lLogoFile);
               if (!lTmpFile.delete()) {
                  logDetailMessage(DvdplayLevel.WARNING, "Failed to delete c:\\aem\\content\\html\\" + lLogoFile);
               }
            } catch (Exception var13) {
               logDetailMessage(DvdplayLevel.WARNING, var13.getMessage());
            }
         }

         DOMData.save();
      }
   }

   public static int checkLogin(String aUser, String aPassword) {
      String[] lNames = new String[2];
      String[] lValues = new String[2];
      lNames[0] = "UserName";
      lNames[1] = "UserPassword";
      lValues[0] = aUser;
      lValues[1] = aPassword;
      int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mSecurityData, lNames, lValues);
      if (lIndex < 0) {
         return -1;
      } else {
         String lRoleId = DOMData.mSecurityData.getFieldValue(lIndex, DOMData.mSecurityData.getFieldIndex("RoleId"));
         return Integer.parseInt(lRoleId);
      }
   }

   public static boolean isRolePrivileged(int aPageId, int aRoleId) {
      String[] lNames = new String[2];
      String[] lValues = new String[2];
      lNames[0] = "RoleId";
      lNames[1] = "PageId";
      lValues[0] = Integer.toString(aRoleId);
      lValues[1] = Integer.toString(aPageId);
      int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mPrivilegesData, lNames, lValues);
      return lIndex >= 0;
   }

   public static void initializeLog() {
      if (!mLogInitialized) {
         checkDir("c:\\aem\\logs\\archive\\");
         Log.initialize(true, true, "c:\\aem\\logs\\", "detail.log", "c:\\aem\\logs\\", "summary.log");
         checkDebugModeFlag();
         mLogInitialized = true;
      }
   }

   public static void setDoLog(boolean a) {
      mDoLog = a;
   }

   public static void logSummaryMessage(String aMsg) {
      if (mDoLog) {
         Log.summary(aMsg);
      }
   }

   public static void logSummaryMessage(String aMsg, Throwable e) {
      if (mDoLog) {
         Log.summary(e, aMsg);
      }
   }

   public static void logDetailMessage(Level aLevel, String aMsg) {
      if (mDoLog) {
         Log.write(aLevel, aMsg);
      }
   }

   public static void logDetailMessage(Level aLevel, String aMsg, Throwable e) {
      if (mDoLog) {
         Log.write(aLevel, aMsg, e);
      }
   }

   public static void sendLogs() {
      String lLogZipFile = "";

      try {
         logSummaryMessage("Sending logs");
         File lLogDir = new File("c:\\aem\\logs\\");
         String[] lFileList = lLogDir.list();
         ArrayList ltmplist = new ArrayList();

         for (int i = 0; i < lFileList.length; i++) {
            if (lFileList[i].endsWith(".log")) {
               logSummaryMessage("sending " + lFileList[i]);
               ltmplist.add(new String("c:\\aem\\logs\\" + lFileList[i]));
            }
         }

         String[] lLogList = new String[ltmplist.size()];

         for (int ix = 0; ix < ltmplist.size(); ix++) {
            lLogList[ix] = (String)ltmplist.get(ix);
         }

         lLogZipFile = Util.getTimeStamp() + "-AEM" + getAemId() + "Logs.zip";
         Util.createZipFile(lLogList, "c:\\aem\\logs\\archive\\" + lLogZipFile);
         Comm.sendFile("c:\\aem\\logs\\archive\\" + lLogZipFile, "kiosklogs\\" + Integer.toString(getAemId()));
         archiveLogs();
      } catch (Exception var12) {
         logDetailMessage(Level.WARNING, var12.getMessage());
      } finally {
         File lZipFile = new File("c:\\aem\\logs\\archive\\" + lLogZipFile);
         if (lZipFile.exists()) {
            lZipFile.delete();
         }
      }
   }

   public static void archiveLogs() {
      File lArchiveDir = new File("c:\\aem\\logs\\archive\\");
      if (lArchiveDir.exists()) {
         if (!lArchiveDir.isDirectory()) {
            lArchiveDir.delete();
            lArchiveDir.mkdirs();
         }
      } else {
         lArchiveDir.mkdirs();
      }

      String lCurrentDetailLog = Log.getCurrentDetailLogName();
      String lCurrentSummaryLog = Log.getCurrentSummaryLogName();
      File lLogDir = new File("c:\\aem\\logs\\");
      String[] lFileList = lLogDir.list();

      for (int i = 0; i < lFileList.length; i++) {
         if (lFileList[i].endsWith(".log")
            && !("c:\\aem\\logs\\" + lFileList[i]).equals(lCurrentDetailLog)
            && !("c:\\aem\\logs\\" + lFileList[i]).equals(lCurrentSummaryLog)) {
            File lLogFile = new File("c:\\aem\\logs\\" + lFileList[i]);
            moveFile(lLogFile.getAbsolutePath(), "c:\\aem\\logs\\archive\\" + lLogFile.getName());
         }
      }
   }

   public static void purgeLogs() {
      purgeDir("c:\\aem\\logs\\archive\\");
   }

   public static void purgeTmpDir() {
      purgeDir("c:\\aem\\tmp\\");
   }

   public static void purgeDir(String aDir) {
      Calendar lPurgeCal = Calendar.getInstance();
      lPurgeCal.add(2, -2);
      long lPurgeTime = lPurgeCal.getTimeInMillis();
      purgeDir(aDir, lPurgeTime);
   }

   public static void purgeDir(String aDir, long aPurgeTime) {
      File lArchiveDir = new File(aDir);
      File[] lFileList = lArchiveDir.listFiles();

      for (int i = 0; i < lFileList.length; i++) {
         if (lFileList[i].lastModified() < aPurgeTime) {
            lFileList[i].delete();
         }
      }
   }

   public static void purgePersistence() {
      purgeDir("c:\\windows\\system32\\var\\pdata\\archive\\");
   }

   public static void sendPersistenceFiles() {
      ArrayList<String> lArrayList = new ArrayList<>();
      String[] lPersistenceFiles = new String[0];
      logSummaryMessage("Sending Persistence Files");

      for (int i = 0; i < DvdplayBase.PERSISTENCE_FILES_LIST.length; i++) {
         if (new File("c:\\windows\\system32\\var\\pdata\\" + DvdplayBase.PERSISTENCE_FILES_LIST[i]).exists()) {
            lArrayList.add(new String("c:\\windows\\system32\\var\\pdata\\" + DvdplayBase.PERSISTENCE_FILES_LIST[i]));
         }
      }

      lPersistenceFiles = lArrayList.toArray(lPersistenceFiles);
      String lPersistenceZip = Util.getTimeStamp() + "-AEM" + getAemId() + "Persistence.zip";
      if (lPersistenceFiles.length != 0) {
         try {
            Util.createZipFile(lPersistenceFiles, "c:\\windows\\system32\\var\\pdata\\archive\\" + lPersistenceZip);
            Comm.sendFile("c:\\windows\\system32\\var\\pdata\\archive\\" + lPersistenceZip, "kiosklogs\\" + Integer.toString(getAemId()));
         } catch (DvdplayException var5) {
            logDetailMessage(Level.WARNING, var5.getMessage());
         }
      }
   }

   public static void sendLockFiles() {
      logSummaryMessage("Sending Lock Files");
      File lDir = new File("c:\\windows\\system32\\var\\qtasks\\");
      String[] lFileList = lDir.list();
      ArrayList<String> lArrayList = new ArrayList<>();
      String[] lLockFiles = new String[0];

      for (int i = 0; i < lFileList.length; i++) {
         if (lFileList[i].startsWith("lock")) {
            lArrayList.add("c:\\windows\\system32\\var\\qtasks\\" + lFileList[i]);
         }
      }

      lDir = new File("c:\\windows\\system32\\var\\pdata\\");
      lFileList = lDir.list();

      for (int ix = 0; ix < lFileList.length; ix++) {
         if (lFileList[ix].startsWith("lock")) {
            lArrayList.add("c:\\windows\\system32\\var\\pdata\\" + lFileList[ix]);
         }
      }

      lLockFiles = lArrayList.toArray(lLockFiles);
      String lLockZip = Util.getTimeStamp() + "-AEM" + getAemId() + "Lock.zip";
      if (lLockFiles.length != 0) {
         try {
            Util.createZipFile(lLockFiles, "c:\\windows\\system32\\var\\pdata\\archive\\" + lLockZip);
            Comm.sendFile("c:\\windows\\system32\\var\\pdata\\archive\\" + lLockZip, "kiosklogs\\" + Integer.toString(getAemId()));
         } catch (DvdplayException var8) {
            logDetailMessage(Level.WARNING, var8.getMessage());
         }
      }
   }

   public static void sendQueueFiles() {
      ArrayList<String> lArrayList = new ArrayList<>();
      String[] lQueueFiles = new String[0];
      File lQueueDir = new File("c:\\windows\\system32\\var\\qtasks\\");
      String[] lFileList = lQueueDir.list();
      logSummaryMessage("Sending Queue Files");

      for (int i = 0; i < lFileList.length; i++) {
         if (lFileList[i].startsWith("q-") && lFileList[i].endsWith(".xml")) {
            lArrayList.add("c:\\windows\\system32\\var\\qtasks\\" + lFileList[i]);
         }
      }

      lQueueFiles = lArrayList.toArray(lQueueFiles);
      String lQueueZip = Util.getTimeStamp() + "-AEM" + getAemId() + "Queue.zip";
      if (lQueueFiles.length != 0) {
         try {
            Util.createZipFile(lQueueFiles, "c:\\windows\\system32\\var\\pdata\\archive\\" + lQueueZip);
            Comm.sendFile("c:\\windows\\system32\\var\\pdata\\archive\\" + lQueueZip, "kiosklogs\\" + Integer.toString(getAemId()));
         } catch (DvdplayException var7) {
            logDetailMessage(Level.WARNING, var7.getMessage());
         }
      }
   }

   public static void logTimeZone() {
      try {
         TimeZone lTZ = Calendar.getInstance().getTimeZone();
         logSummaryMessage("Timezone is " + lTZ.getDisplayName(lTZ.inDaylightTime(new Date()), 0, Locale.getDefault()));
         logSummaryMessage("Bias is " + lTZ.getOffset(new Date().getTime()));
         logSummaryMessage("DST is " + lTZ.getDSTSavings());
      } catch (Exception var1) {
         logDetailMessage(DvdplayLevel.ERROR, var1.getMessage(), var1);
      }
   }

   public static void setTimeZone() {
      StringTokenizer lStk = new StringTokenizer(NMC.getTimeZoneInfo(), ",");
      int lCurrentBias = Integer.parseInt(lStk.nextToken());
      int lCurrentDSTBias = Integer.parseInt(lStk.nextToken());
      TimeZone lTimeZone = TimeZone.getTimeZone(mTimeZoneId);
      int lBias = lTimeZone.getRawOffset() / 1000 / -60;
      int lDSTBias = lTimeZone.getDSTSavings() / 1000 / -60;
      if (lCurrentBias != lBias || lCurrentDSTBias != lDSTBias) {
         int lUsesDST;
         if (lTimeZone.useDaylightTime()) {
            lUsesDST = 1;
         } else {
            lUsesDST = 0;
         }

         logDetailMessage(Level.WARNING, "Setting TimeZone to " + mTimeZoneId);
         NMC.setTimeZoneInfo(lBias, lDSTBias, lUsesDST, mTimeZoneAutoAdj);
      }
   }

   public static void purgeTitleDetailRecords(RCSet aRCSet) {
      for (int i = 0; i < aRCSet.rowCount(); i++) {
         if (!aRCSet.isDeleted(i)) {
            for (int j = 2; j < aRCSet.fieldCount(); j++) {
               removeTitle(Integer.parseInt(aRCSet.getFieldValue(i, j)), true);
            }
         }
      }

      DOMData.save();
   }

   public static void purgeDiscDetailRecords(RCSet aRCSet) {
      for (int i = 0; i < aRCSet.rowCount(); i++) {
         if (!aRCSet.isDeleted(i)) {
            for (int j = 2; j < aRCSet.fieldCount(); j++) {
               int lDiscDetailId = Integer.parseInt(aRCSet.getFieldValue(i, j));
               int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, "DiscDetailId", Integer.toString(lDiscDetailId));
               if (lIndex >= 0) {
                  int lTitleDetailId = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("TitleDetailId")));
                  String lDiscCode = DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("DiscCode"));
                  String lGroupCode = DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("GroupCode"));
                  removeDisc(lDiscDetailId, lTitleDetailId, lDiscCode, lGroupCode, true);
               }
            }
         }
      }

      DOMData.save();
   }

   public static void resetMediaDetailRecordImage(RCSet aRCSet) {
      for (int i = 0; i < aRCSet.rowCount(); i++) {
         if (!aRCSet.isDeleted(i)) {
            String lMediaId = aRCSet.getFieldValue(i, 2);
            String lMediaTypeId = aRCSet.getFieldValue(i, 3);
            logDetailMessage(DvdplayLevel.INFO, "resetting image MediaId " + lMediaId + " MediaTypeId " + lMediaTypeId);
            String[] lNames = new String[2];
            String[] lValues = new String[2];
            lNames[0] = "MediaId";
            lValues[0] = lMediaId;
            lNames[1] = "MediaTypeId";
            lValues[1] = lMediaTypeId;
            int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
            if (lIndex >= 0) {
               Inventory.resetMediaDetailImage(lIndex);
            }
         }
      }

      DOMData.save();
   }

   public static void resetMediaDetailRecordMedia(RCSet aRCSet) {
      for (int i = 0; i < aRCSet.rowCount(); i++) {
         if (!aRCSet.isDeleted(i)) {
            String lMediaId = aRCSet.getFieldValue(i, 2);
            String lMediaTypeId = aRCSet.getFieldValue(i, 3);
            logDetailMessage(DvdplayLevel.INFO, "resetting media MediaId " + lMediaId + " MediaTypeId " + lMediaTypeId);
            String[] lNames = new String[2];
            String[] lValues = new String[2];
            lNames[0] = "MediaId";
            lValues[0] = lMediaId;
            lNames[1] = "MediaTypeId";
            lValues[1] = lMediaTypeId;
            int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
            if (lIndex >= 0) {
               Inventory.resetMediaDetailMedia(lIndex);
            }
         }
      }

      DOMData.save();
   }

   public static void purgeMediaDetailRecord(RCSet aRCSet) {
      for (int i = 0; i < aRCSet.rowCount(); i++) {
         if (!aRCSet.isDeleted(i)) {
            String lMediaId = aRCSet.getFieldValue(i, 2);
            String lMediaTypeId = aRCSet.getFieldValue(i, 3);
            logDetailMessage(DvdplayLevel.INFO, "deleting MediaId " + lMediaId + " MediaTypeId " + lMediaTypeId);
            String[] lNames = new String[2];
            String[] lValues = new String[2];
            lNames[0] = "MediaId";
            lValues[0] = lMediaId;
            lNames[1] = "MediaTypeId";
            lValues[1] = lMediaTypeId;
            int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
            if (lIndex >= 0) {
               DOMData.mMediaDetailData.deleteRow(lIndex);
            }
         }
      }

      DOMData.save();
   }

   public static void downloadFiles(RCSet aRCSet) {
      for (int i = 0; i < aRCSet.rowCount(); i++) {
         try {
            if (!aRCSet.isDeleted(i)) {
               String lServerFilePath = aRCSet.getFieldValue(i, 2);
               String lLocalFilePath = aRCSet.getFieldValue(i, 3);
               File lFile = new File(lLocalFilePath);
               if (lFile.exists() && !lFile.delete()) {
                  logDetailMessage(DvdplayLevel.ERROR, "Could not delete " + lLocalFilePath);
               } else {
                  Comm.downloadFile(lServerFilePath, lLocalFilePath);
               }
            }
         } catch (Exception var5) {
            logDetailMessage(DvdplayLevel.WARNING, var5.getMessage());
         }
      }
   }

   public static void getAds() {
      logSummaryMessage("Downloading ads ...");

      for (int i = 0; i < DOMData.mStaticPlaylistData.rowCount(); i++) {
         if (!DOMData.mStaticPlaylistData.isDeleted(i)) {
            String lId = DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("MediaId"));
            String lTypeId = DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("MediaTypeId"));
            String[] lNames = new String[2];
            String[] lValues = new String[2];
            lNames[0] = "MediaId";
            lValues[0] = lId;
            lNames[1] = "MediaTypeId";
            lValues[1] = lTypeId;
            int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
            if (lIndex < 0) {
               createNewMediaDetailRecord(lId, lTypeId);
               lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
            }

            boolean lIsDownloaded;
            if (Integer.parseInt(DOMData.mMediaDetailData.getFieldValue(lIndex, DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"))) == 1) {
               lIsDownloaded = true;
            } else {
               lIsDownloaded = false;
            }

            if (!lIsDownloaded) {
               String lFilename = DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("Filename"));

               try {
                  Comm.downloadFile("/webdav/posters/" + lFilename, "c:\\aem\\content\\ads\\" + lFilename);
                  RDataSetFieldValues lRDataSetFieldValues = DOMData.mMediaDetailData.getRow(lIndex);
                  lRDataSetFieldValues.setValue(DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"), Integer.toString(1));
                  DOMData.mMediaDetailData.addRow(lRDataSetFieldValues);
                  DOMData.mMediaDetailData.deleteRow(lIndex);
               } catch (DvdplayException var10) {
                  logDetailMessage(DvdplayLevel.WARNING, var10.getMessage());
               }
            }
         }
      }

      DOMData.save();
   }

   public static void getPosters() {
      synchronized (mGettingPosters) {
         if (mGettingPosters) {
            return;
         }

         mGettingPosters = true;
      }

      logSummaryMessage("Getting Missing Posters ...");

      for (int i = 0; i < DOMData.mTitleDetailData.rowCount(); i++) {
         if (!DOMData.mTitleDetailData.isDeleted(i)) {
            String lTitleDetailId = DOMData.mTitleDetailData.getFieldValue(i, DOMData.mTitleDetailData.getFieldIndex("TitleDetailId"));
            String[] lNames = new String[2];
            String[] lValues = new String[2];
            lNames[0] = "MediaId";
            lValues[0] = lTitleDetailId;
            lNames[1] = "MediaTypeId";
            lValues[1] = Integer.toString(1);
            int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
            if (lIndex < 0) {
               createNewMediaDetailRecord(lTitleDetailId, Integer.toString(1));
               lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
            }

            boolean lIsDownloaded;
            if (Integer.parseInt(DOMData.mMediaDetailData.getFieldValue(lIndex, DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"))) == 1) {
               lIsDownloaded = true;
            } else {
               lIsDownloaded = false;
            }

            if (!lIsDownloaded) {
               String lPosterFile = DOMData.mTitleDetailData.getFieldValue(i, DOMData.mTitleDetailData.getFieldIndex("Poster"));

               try {
                  Comm.downloadFile("/webdav/posters/" + lPosterFile, "c:\\aem\\content\\posters\\" + lPosterFile);
                  Util.scaleAndSaveImage(
                     "c:\\aem\\content\\posters\\" + lPosterFile, "c:\\aem\\content\\posters\\" + Util.getPosterSmallName(lPosterFile), 100, 140
                  );
                  RDataSetFieldValues lRDataSetFieldValues = DOMData.mMediaDetailData.getRow(lIndex);
                  lRDataSetFieldValues.setValue(DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"), Integer.toString(1));
                  DOMData.mMediaDetailData.addRow(lRDataSetFieldValues);
                  DOMData.mMediaDetailData.deleteRow(lIndex);
               } catch (DvdplayException var10) {
                  logDetailMessage(DvdplayLevel.WARNING, var10.getMessage());
               }
            }
         }
      }

      DOMData.save();
      mGettingPosters = false;
   }

   public static void moveFile(String aSourceFile, String aTargetFile) {
      String lBatCmd = "@echo off\n\n";
      String lBatFile = "c:\\aem\\tmp\\" + Util.getTimeStamp() + ".bat";
      lBatCmd = lBatCmd + "move /Y " + aSourceFile + " " + aTargetFile + "\n";

      try {
         BufferedWriter bfout = new BufferedWriter(new FileWriter(lBatFile));
         bfout.write(lBatCmd);
         bfout.flush();
         bfout.close();
         System.gc();
         Runtime.getRuntime().exec(lBatFile).waitFor();
         File lTmpFile = new File(lBatFile);
         if (!lTmpFile.delete()) {
            logDetailMessage(DvdplayLevel.WARNING, "Failed to delete " + lBatFile);
         }
      } catch (Exception var6) {
         logDetailMessage(DvdplayLevel.WARNING, var6.getMessage());
         logDetailMessage(DvdplayLevel.WARNING, "moveFile failed");
      }

      File lFile = new File(aSourceFile);
      File lTarget = new File(aTargetFile);
      if (lFile.exists() || !lTarget.exists()) {
         logDetailMessage(DvdplayLevel.WARNING, "moveFile failed to move " + lFile.getName());
      }
   }

   public static void moveFiles(String[] aSourceFiles, String[] aTargetFiles) {
      String lBatCmd = "@echo off\n\n";
      String lBatFile = "c:\\aem\\tmp\\" + Util.getTimeStamp() + ".bat";

      for (int i = 0; i < aSourceFiles.length; i++) {
         lBatCmd = lBatCmd + "move " + aSourceFiles[i] + " " + aTargetFiles[i] + "\n";
      }

      try {
         BufferedWriter bfout = new BufferedWriter(new FileWriter(lBatFile));
         bfout.write(lBatCmd);
         bfout.flush();
         bfout.close();
         System.gc();
         Runtime.getRuntime().exec(lBatFile).waitFor();
         File lTmpFile = new File(lBatFile);
         if (!lTmpFile.delete()) {
            logDetailMessage(DvdplayLevel.WARNING, "Failed to delete " + lBatFile);
         }
      } catch (Exception var7) {
         logDetailMessage(DvdplayLevel.ERROR, var7.getMessage());
         throw new DvdplayException("moveFiles failed");
      }
   }

   private static void checkDir(String aDir) {
      File lDir = new File(aDir);
      if (lDir.exists()) {
         if (!lDir.isDirectory()) {
            if (!lDir.delete()) {
               throw new DvdplayException(aDir + "could not be created");
            }

            if (!lDir.mkdirs()) {
               throw new DvdplayException(aDir + "could not be created");
            }
         }
      } else if (!lDir.mkdirs()) {
         throw new DvdplayException(aDir + "could not be created");
      }
   }

   public void executeGetDataCmd() {
      logSummaryMessage("Executing GetDataCmd " + mGetDataCmd);
      switch (mGetDataCmd) {
         case 1:
            refreshGenreData();
            break;
         case 2:
            refreshTitleDetailData();
            break;
         case 3:
            refreshGroupCodeData();
            break;
         case 4:
            refreshTranslationData();
            break;
         case 5:
            refreshRatingData();
            break;
         case 6:
            refreshRatingSystemData();
            break;
         case 7:
            refreshRegularPricingData();
            break;
         case 8:
            refreshSpecialPricingData();
            break;
         case 9:
            refreshStaticPlaylistData();
            break;
         case 10:
            refreshLocaleData();
            break;
         case 11:
            refreshMediaPlaylistData();
            break;
         case 12:
            refreshAemPropertiesData();
            break;
         case 13:
            refreshDiscDetailData();
            break;
         case 14:
            refreshPaymentCardData();
            break;
         case 15:
            refreshTitleTypeData();
            break;
         case 16:
            refreshTitleTypeCapData();
            break;
         case 17:
            refreshSecurityData();
            break;
         case 18:
            refreshPrivilegesData();
            break;
         case 19:
            refreshBadSlotData();
            break;
         case 20:
            refreshSlotOffsetData();
            break;
         case 21:
            this.refreshAllData();
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         default:
            break;
         case 43:
            refreshPollData();
      }

      DOMData.save();
      mGetDataCmd = 0;
      this.createAllIndexes();
   }

   public void createAllIndexes() {
      readAemProperties();
      this.getServo().readServoProperties();
      Inventory.createAllIndexes();
      createMediaPlayList();
      createStaticPlayList();
   }

   public static void generateCopyMoveBat() {
      logDetailMessage(DvdplayLevel.FINE, "Generating copymove.bat");
      String lCmd = "@echo off\n\ncopy /Y %currentfile% %oldfile%\nmove /Y %newfile% %currentfile%\necho %doneflag%";

      try {
         Util.writeToFile("c:\\aem\\bin\\copymove.bat", "@echo off\n\ncopy /Y %currentfile% %oldfile%\nmove /Y %newfile% %currentfile%\necho %doneflag%");
         if (!new File("c:\\aem\\bin\\copymove.bat").exists()) {
            throw new DvdplayException("Failed to create copymove.bat");
         }
      } catch (Exception var2) {
         logSummaryMessage(var2.getMessage());
         logDetailMessage(DvdplayLevel.SEVERE, var2.getMessage());
         throw new DvdplayException("genereateCopyMoveBat failed.");
      }
   }

   public synchronized void exitApp(int aMode) {
      logSummaryMessage("Exiting App ...");

      try {
         Comm.ping(10000);
      } catch (Exception var3) {
         logDetailMessage(DvdplayLevel.WARNING, "Exception from ping: " + var3.getMessage());
      }

      checkExitApp();
      setExitingApp();
      HeartBeatThread.stopStrokeWatchdog();
      int lMode = mServo.prepareExit(aMode);
      switch (lMode) {
         case 1:
            logSummaryMessage("Exiting application");
            System.exit(0);
            break;
         case 2:
            logSummaryMessage("Restarting computer");
            restartComputer();
            break;
         case 3:
            logSummaryMessage("Shuttingdown computer");
            shutdownComputer();
            break;
         default:
            logDetailMessage(DvdplayLevel.WARNING, "Invalid exit mode: " + lMode);
            logSummaryMessage("Restarting computer");
            restartComputer();
      }
   }

   private static void checkExitApp() {
      try {
         if (getSavingStatus()) {
            long now = System.currentTimeMillis();

            while (System.currentTimeMillis() - now <= 30000L) {
               Util.sleep(100);
               if (!getSavingStatus()) {
                  break;
               }
            }
         }

         if (!DOMData.mAemPropertiesData.checkCheckSum()) {
            DOMData.mAemPropertiesData.touch();
         }

         if (!DOMData.mDiscDetailData.checkCheckSum()) {
            DOMData.mDiscDetailData.touch();
         }

         DOMData.save();
      } catch (Exception var3) {
         logDetailMessage(DvdplayLevel.ERROR, var3.getMessage(), var3);
      }
   }

   public static boolean isInventoryForTitleType(int aTitleTypeId) {
      int lCurrentTitleTypeId = getTitleTypeId();
      setTitleTypeId(aTitleTypeId);
      int lCount = createGenreList();
      setTitleTypeId(lCurrentTitleTypeId);
      return lCount > 0;
   }

   private static int getDataFranchiseId() {
      if (isStandAlone()) {
         return 0;
      } else {
         try {
            String lQuery = "Select FranchiseID 'FranchiseID' From ffKiosk where KioskID = " + getAemId();
            String lRequestString = new QueryRequest(getAemId(), "2.0", "FranchiseID query", lQuery).getAsXmlString();
            logDetailMessage(DvdplayLevel.FINER, lRequestString);
            String lRet = Comm.sendRequest(lRequestString);
            DataPacketComposer ldpc = new DataPacketComposer();
            DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
            int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
            if (lResult != 0) {
               String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
               throw new DvdplayException("getFranchiseId request failed: " + lMsg);
            } else {
               RCSet lDataRCSet = lRequest.getDatasetData()[0];
               mFranchiseId = Integer.parseInt(lDataRCSet.getFieldValue(0, lDataRCSet.getFieldIndex("FranchiseID")));
               return mFranchiseId;
            }
         } catch (Exception var7) {
            logDetailMessage(DvdplayLevel.WARNING, var7.getMessage(), var7);
            return 0;
         }
      }
   }

   private static void getDisableBuy() {
      if (!isStandAlone()) {
         try {
            String lQuery = "Select * From ffKioskProperty";
            lQuery = lQuery + " where KioskID = " + getAemId();
            lQuery = lQuery + " and PropertyTypeID = 3";
            String lRequestString = new QueryRequest(getAemId(), "2.0", "DisableBuy query", lQuery).getAsXmlString();
            logDetailMessage(DvdplayLevel.FINER, lRequestString);
            String lRet = Comm.sendRequest(lRequestString);
            DataPacketComposer ldpc = new DataPacketComposer();
            DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
            int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
            if (lResult != 0) {
               String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
               throw new DvdplayException("getDisableBuy request failed: " + lMsg);
            }

            RCSet lDataRCSet = lRequest.getDatasetData()[0];
            if (lDataRCSet.rowCount() == 0) {
               enableBuy();
            } else {
               disableBuy();
            }
         } catch (Exception var7) {
            logDetailMessage(DvdplayLevel.WARNING, var7.getMessage(), var7);
         }
      }
   }

   public static long getLastLogReceived() {
      return mLastLogReceived;
   }

   public static void setLastLogReceived(long aLastLogReceived) {
      mLastLogReceived = aLastLogReceived;
   }

   public static long getLastRobotClickReceived() {
      return mLastRobotClickReceived;
   }

   public static void setLastRobotClickReceived(long aLastRobotClickReceived) {
      mLastRobotClickReceived = aLastRobotClickReceived;
   }

   public static boolean isToolsRunning() {
      return mIsToolsRunning;
   }

   public static void setToolsRunning(boolean aIsToolsRunning) {
      mIsToolsRunning = aIsToolsRunning;
   }

   public static void uploadServoData() {
      String lStatement = "";

      for (int i = 0; i < DOMData.mAemPropertiesData.rowCount(); i++) {
         if (!DOMData.mAemPropertiesData.isDeleted(i)) {
            lStatement = "update ffKioskSetup set ServoOffset="
               + mServo.getServoOffset()
               + ", ServoInputStep="
               + mServo.getServoInputStep()
               + ", ServoOutputStep="
               + mServo.getServoOutputStep()
               + ", ServoDiscThreshold="
               + mServo.getServoDiscThreshold()
               + ", ServoKp="
               + mServo.getServoKp()
               + ", ServoKp2="
               + mServo.getServoKp2()
               + ", ServoKd="
               + mServo.getServoKd()
               + ", ServoKd2="
               + mServo.getServoKd2()
               + ", ServoKi="
               + mServo.getServoKi()
               + ", ServoKi2="
               + mServo.getServoKi2()
               + ", ServoRate="
               + mServo.getServoServoRate()
               + ", ServoRate2="
               + mServo.getServoServoRate2()
               + ", ServoDeadband="
               + mServo.getServoDeadbandComp()
               + ", ServoDeadband2="
               + mServo.getServoDeadbandComp2()
               + ", ServoIntegrationLimit="
               + mServo.getServoIntegrationLimit()
               + ", ServoIntegrationLimit2="
               + mServo.getServoIntegrationLimit2()
               + ", ServoVelocity="
               + mServo.getServoVelocity()
               + ", ServoVelocity2="
               + mServo.getServoVelocity2()
               + ", ServoAcceleration="
               + mServo.getServoAcceleration()
               + ", ServoAcceleration2="
               + mServo.getServoAcceleration2()
               + ", ServoOutputLimit="
               + mServo.getServoOutputLimit()
               + ", ServoOutputLimit2="
               + mServo.getServoOutputLimit2()
               + ", ServoCurrentLimit="
               + mServo.getServoCurrentLimit()
               + ", ServoCurrentLimit2="
               + mServo.getServoCurrentLimit2()
               + ", ServoPositionErrorLimit="
               + mServo.getServoPositionErrorLimit()
               + ", ServoPositionErrorLimit2="
               + mServo.getServoPositionErrorLimit2()
               + ", ServoArmEjectWaitTime="
               + mServo.getArmEjectWaitTime()
               + ", ServoMoveToOffsetWaitTime="
               + mServo.getMoveToOffsetWaitTime()
               + ", ServoMoveToOffsetTimeout="
               + mServo.getMoveToOffsetTimeOut()
               + " where KioskID="
               + getAemId();
            GetFragmentRequest fragment = new GetFragmentRequest(getAemId(), "2.0", lStatement, new Date());
            String lRequestString = fragment.getAsXmlString();
            logDetailMessage(DvdplayLevel.FINER, lRequestString);
            String lRet = Comm.sendRequest(lRequestString);
            DataPacketComposer ldpc = new DataPacketComposer();
            DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
            int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
            if (lResult != 0) {
               String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
               throw new DvdplayException("uploadServoData request failed: " + lMsg);
            }

            return;
         }
      }
   }

   public static void uploadSlotData() {
      StringBuilder lStatement = new StringBuilder();

      for (int i = 0; i < DOMData.mSlotOffsetData.rowCount(); i++) {
         if (!DOMData.mSlotOffsetData.isDeleted(i)) {
            String lSlotId = DOMData.mSlotOffsetData.getFieldValue(i, DOMData.mSlotOffsetData.getFieldIndex("Slot"));
            String lSlotOffset = DOMData.mSlotOffsetData.getFieldValue(i, DOMData.mSlotOffsetData.getFieldIndex("Offset"));
            int lIndex = -1;
            lIndex = Util.getRCSetIndexForFieldValue(DOMData.mBadSlotData, "Slot", lSlotId);
            String lBad;
            if (lIndex < 0) {
               lBad = "0";
            } else {
               lBad = "1";
            }

            lStatement.append("update ffKioskSlot set Offset=").append(lSlotOffset).append(", Bad=").append(lBad).append(" where KioskID=").append(getAemId()).append(" and Slot=").append(lSlotId).append(";");
         }
      }

      GetFragmentRequest fragment = new GetFragmentRequest(getAemId(), "2.0", lStatement.toString(), new Date());
      String lRequestString = fragment.getAsXmlString();
      logDetailMessage(DvdplayLevel.FINER, lRequestString);
      String lRet = Comm.sendRequest(lRequestString);
      DataPacketComposer ldpc = new DataPacketComposer();
      DatasetRequest lRequest = new DatasetRequest(ldpc.nvDeMarshal(lRet));
      int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
      if (lResult != 0) {
         String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
         throw new DvdplayException("uploadSlotData request failed: " + lMsg);
      }
   }

   public void purgeQueueJob(RCSet aRCSet) {
      for (int i = 0; i < aRCSet.rowCount(); i++) {
         try {
            if (!aRCSet.isDeleted(i)) {
               for (int j = 2; j < aRCSet.fieldCount(); j++) {
                  int lRequestId = Integer.parseInt(aRCSet.getFieldValue(i, j));
                  mQueue.purgeQueueJob(lRequestId);
               }
            }
         } catch (Exception var5) {
            logDetailMessage(DvdplayLevel.WARNING, var5.getMessage());
         }
      }
   }

   public static void rebuildQueue() {
      logSummaryMessage("Rebuilding queue");
      mQueue = new Queue();
      mRebuildQueue = false;
   }

   public static void uploadFiles(RCSet aRCSet) {
      ArrayList<String> lFileList = new ArrayList<>();
      String[] lFileArray = new String[0];
      String lUploadZip = null;

      for (int i = 0; i < aRCSet.rowCount(); i++) {
         try {
            if (!aRCSet.isDeleted(i)) {
               for (int j = 2; j < aRCSet.fieldCount(); j++) {
                  String lFile = aRCSet.getFieldValue(i, j);
                  if (new File(lFile).exists()) {
                     lFileList.add(lFile);
                  }
               }

               lFileArray = lFileList.toArray(lFileArray);
               lUploadZip = Util.getTimeStamp() + "-AEM" + getAemId() + "Upload.zip";
               Util.createZipFile(lFileArray, "c:\\aem\\tmp\\" + lUploadZip);
               Comm.sendFile("c:\\aem\\tmp\\" + lUploadZip, "kiosklogs\\" + Integer.toString(getAemId()));
            }
         } catch (Exception var12) {
            logDetailMessage(DvdplayLevel.WARNING, var12.getMessage());
         } finally {
            if (lUploadZip != null) {
               File a = new File("c:\\aem\\tmp\\" + lUploadZip);
               if (a.exists()) {
                  a.delete();
               }
            }
         }
      }
   }
}
