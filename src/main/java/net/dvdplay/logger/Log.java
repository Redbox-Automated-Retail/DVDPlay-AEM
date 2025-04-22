package net.dvdplay.logger;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.util.IEnvironment;

public class Log {
   private static final Properties dvdLogProperties = new Properties();
   private static final String PROPERTY_LOG_DETAIL_ENABLE = "log.detail.enable";
   private static final String PROPERTY_LOG_DETAIL_DIRECTORY = "log.detail.directory";
   private static final String PROPERTY_LOG_DETAIL_FILE = "log.detail.file";
   private static final String PROPERTY_LOG_DETAIL_LEVEL = "log.detail.level";
   private static final String PROPERTY_LOG_SUMMARY_ENABLE = "log.summary.enable";
   private static final String PROPERTY_LOG_SUMMARY_DIRECTORY = "log.summary.directory";
   private static final String PROPERTY_LOG_SUMMARY_FILE = "log.summary.file";
   private static final Logger detailedLogger = Logger.getLogger("net.dvdsys.log.detail");
   private static final Logger summaryLogger = Logger.getLogger("net.dvdsys.log.summary");
   private static final String mDateFormatString = "yyyyMMdd";
   private static final DvdplayFormatter simpleFormat = new DvdplayFormatter();
   private static FileHandler detailedLogHandler = null;
   private static FileHandler summaryLogHandler = null;
   private static Date nextLogHandlerSwitchTS = null;
   private static String curDetailLogName = null;
   private static String curSummaryLogName = null;
   private static String relativeLogPathOrigin = "";
   private static boolean mgInitialized = false;

   public static String getRelativeLogPathOrigin() {
      return relativeLogPathOrigin != null && !relativeLogPathOrigin.isEmpty() ? relativeLogPathOrigin : "";
   }

   public static void setRelativeLogPathOrigin(String path) {
      try {
         File aFile = new File(path);
         if (!aFile.isAbsolute()) {
            throw new DvdplayException(1005, "LogPathOrigin not an absolute path: " + path);
         } else {
            relativeLogPathOrigin = path;
         }
      } catch (DvdplayException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new DvdplayException(1005, "LogPathOrigin not an absolute path: " + path);
      }
   }

   private static void readLoggingConfig() {
      String targetDir = System.getProperty("user.dir");
      System.out.println("targetDir:" + targetDir);
      File file = new File(targetDir, "log.properties");
      if (!file.exists()) {
         file = new File(targetDir + "\\WEB-INF\\lib");
      }

      try {
         LogManager.getLogManager().readConfiguration(new FileInputStream(file));
      } catch (Exception ignored) {
      }
   }

   private static String makeFullFilename(String dirPath, String fileName) {
      if (fileName == null) {
         return fileName;
      } else if (dirPath != null && dirPath != "") {
         String retVal;
         if (dirPath.lastIndexOf("\\") != dirPath.length() - 1) {
            retVal = dirPath + "\\" + fileName;
         } else {
            retVal = dirPath + fileName;
         }

         File a = new File(retVal);
         retVal = a.getAbsolutePath();
         System.out.println("LogName resolved to: " + retVal);
         return retVal;
      } else {
         return fileName;
      }
   }

   public static synchronized void initialize(IEnvironment appEnvironment) {
      if (!mgInitialized) {
         String pValue = appEnvironment.getProperty(PROPERTY_LOG_DETAIL_ENABLE, "false");
         dvdLogProperties.put(PROPERTY_LOG_DETAIL_ENABLE, pValue);
         pValue = appEnvironment.getProperty(PROPERTY_LOG_DETAIL_DIRECTORY, "NONE");
         dvdLogProperties.put(PROPERTY_LOG_DETAIL_DIRECTORY, pValue);
         pValue = appEnvironment.getProperty(PROPERTY_LOG_DETAIL_FILE, "NONE");
         dvdLogProperties.put(PROPERTY_LOG_DETAIL_FILE, pValue);
         pValue = appEnvironment.getProperty(PROPERTY_LOG_DETAIL_LEVEL, "ALL");
         dvdLogProperties.put(PROPERTY_LOG_DETAIL_LEVEL, pValue);
         pValue = appEnvironment.getProperty(PROPERTY_LOG_SUMMARY_ENABLE, "false");
         dvdLogProperties.put(PROPERTY_LOG_SUMMARY_ENABLE, pValue);
         pValue = appEnvironment.getProperty(PROPERTY_LOG_SUMMARY_DIRECTORY, "NONE");
         dvdLogProperties.put(PROPERTY_LOG_SUMMARY_DIRECTORY, pValue);
         pValue = appEnvironment.getProperty(PROPERTY_LOG_SUMMARY_FILE, "NONE");
         dvdLogProperties.put(PROPERTY_LOG_SUMMARY_FILE, pValue);
         logSwitch();
         mgInitialized = true;
      }
   }

   public static void initialize(
      boolean doDetailFlag, boolean doSummaryFlag, String detailLogDirname, String detailLogFilename, String summaryLogDirname, String summaryLogFilename
   ) {
      try {
         dvdLogProperties.put(PROPERTY_LOG_DETAIL_ENABLE, Boolean.toString(doDetailFlag));
         dvdLogProperties.put(PROPERTY_LOG_DETAIL_DIRECTORY, detailLogDirname);
         dvdLogProperties.put(PROPERTY_LOG_DETAIL_FILE, detailLogFilename);
         dvdLogProperties.put(PROPERTY_LOG_SUMMARY_ENABLE, Boolean.toString(doSummaryFlag));
         dvdLogProperties.put(PROPERTY_LOG_SUMMARY_DIRECTORY, summaryLogDirname);
         dvdLogProperties.put(PROPERTY_LOG_SUMMARY_FILE, summaryLogFilename);
         Date now = Calendar.getInstance().getTime();
         String detailed = genDetailedLogName(now);
         String summary = genSummaryLogName(now);
         _initialize(isDetailedEnabled(), isSummaryEnabled(), detailed, summary);
      } catch (Exception var9) {
         throw new DvdplayException(1005, var9);
      }
   }

   private static void _initialize(boolean doDetailFlag, boolean doSummaryFlag, String detailLogFilename, String summaryLogFilename) {
      try {
         readLoggingConfig();
         String firstDir = getDetailedDirName();
         File lckFile = new File(firstDir);
         File[] matchFiles = lckFile.listFiles(new LckFileFilter());

         for (int i = 0; i < matchFiles.length; i++) {
            try {
               matchFiles[i].delete();
            } catch (Exception var14) {
            }
         }

         String secondDir = getSummaryDirName();
         lckFile = new File(secondDir);
         matchFiles = lckFile.listFiles(new LckFileFilter());

         for (int i = 0; i < matchFiles.length; i++) {
            try {
               matchFiles[i].delete();
            } catch (Exception var13) {
            }
         }

         if (doDetailFlag) {
            if (detailedLogHandler != null) {
               detailedLogHandler.close();
               detailedLogger.removeHandler(detailedLogHandler);
               File aFile = new File(curDetailLogName + ".lck");
               aFile.delete();
            }

            detailedLogHandler = new FileHandler(detailLogFilename);
            detailedLogHandler.setFormatter(simpleFormat);
            detailedLogger.addHandler(detailedLogHandler);
            Level defLevel = getDetailedLogLevel();
            setDetailedLevel(defLevel);
            detailedLogger.info("Log Started");
            curDetailLogName = detailLogFilename;
         } else {
            setDetailedLevel(Level.OFF);
         }

         if (doSummaryFlag) {
            if (summaryLogHandler != null) {
               summaryLogHandler.close();
               summaryLogger.removeHandler(summaryLogHandler);
               File aFile = new File(curSummaryLogName + ".lck");
               aFile.delete();
            }

            summaryLogHandler = new FileHandler(summaryLogFilename);
            summaryLogHandler.setFormatter(simpleFormat);
            summaryLogger.addHandler(summaryLogHandler);
            setSummaryLevel(Level.ALL);
            summaryLogger.config("Log Started");
            curSummaryLogName = summaryLogFilename;
         } else {
            setSummaryLevel(Level.OFF);
         }

         try {
            summaryLogger.setUseParentHandlers(false);
            detailedLogger.setUseParentHandlers(false);
         } catch (Exception var12) {
         }

         nextLogHandlerSwitchTS = getNextLogSwitchTime();
         System.out.println("nextSwitch:" + nextLogHandlerSwitchTS.toString());
      } catch (Exception var15) {
         throw new DvdplayException(1005, var15);
      }
   }

   private static void logSwitch() {
      try {
         Date now = Calendar.getInstance().getTime();
         String detailed = genDetailedLogName(now);
         Level detailLevel = getDetailedLevel();
         Level sumLevel = getSummaryLevel();
         String summary = genSummaryLogName(now);
         _initialize(isDetailedEnabled(), isSummaryEnabled(), detailed, summary);
         setDetailedLevel(detailLevel);
         setSummaryLevel(sumLevel);
      } catch (Exception var5) {
         throw new DvdplayException(1005, var5);
      }
   }

   private static boolean isDetailedEnabled() {
      String flag = dvdLogProperties.getProperty(PROPERTY_LOG_DETAIL_ENABLE);
      if (flag == null) {
         flag = "true";
      }

      return "true".equalsIgnoreCase(flag);
   }

   private static Level getDetailedLogLevel() {
      String value = dvdLogProperties.getProperty(PROPERTY_LOG_DETAIL_LEVEL);
      Level retVal = Level.ALL;

      try {
         retVal = DvdplayLevel.parse(value);
      } catch (Exception var3) {
      }

      return retVal;
   }

   public static String getDetailedLogLevelName() {
      Level curLevel = getDetailedLogLevel();
      return curLevel.getName();
   }

   private static boolean isSummaryEnabled() {
      String flag = dvdLogProperties.getProperty(PROPERTY_LOG_SUMMARY_ENABLE);
      if (flag == null) {
         flag = "true";
      }

      return "true".equalsIgnoreCase(flag);
   }

   private static String getDetailedDirName() {
      String dir = dvdLogProperties.getProperty(PROPERTY_LOG_DETAIL_DIRECTORY);
      if (dir == null) {
         dir = "";
      }

      File aFile = new File(dir);
      if (!aFile.isAbsolute()) {
         dir = getRelativeLogPathOrigin() + dir;
         aFile = new File(dir);
      }

      dir = aFile.getAbsolutePath();
      if (!aFile.exists()) {
         try {
            boolean done = aFile.mkdirs();
         } catch (Exception var3) {
            throw new DvdplayException(1005, var3);
         }
      }

      return dir;
   }

   private static String genDetailedLogName(Date now) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      String ts = dateFormat.format(now);
      String retVal = null;
      String dir = getDetailedDirName();
      String extension = dvdLogProperties.getProperty(PROPERTY_LOG_DETAIL_FILE);
      if (dir == null || extension == null) {
         dir = "";
         extension = ".log";
      }

      return makeFullFilename(dir, "d" + ts + extension);
   }

   private static String getSummaryDirName() {
      String dir = dvdLogProperties.getProperty(PROPERTY_LOG_SUMMARY_DIRECTORY);
      if (dir == null) {
         dir = "";
      }

      File aFile = new File(dir);
      if (!aFile.isAbsolute()) {
         dir = getRelativeLogPathOrigin() + dir;
         aFile = new File(dir);
      }

      dir = aFile.getAbsolutePath();
      if (!aFile.exists()) {
         try {
            boolean done = aFile.mkdirs();
         } catch (Exception var3) {
            throw new DvdplayException(1005, var3);
         }
      }

      return dir;
   }

   private static String genSummaryLogName(Date now) {
      String retVal = null;
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      String ts = dateFormat.format(now);
      String dir = getSummaryDirName();
      String extension = dvdLogProperties.getProperty(PROPERTY_LOG_SUMMARY_FILE);
      if (dir == null || extension == null) {
         dir = "";
         extension = ".log";
      }

      return makeFullFilename(dir, "s" + ts + extension);
   }

   public static void setDetailedLevel(Level level) {
      if (level != null) {
         detailedLogger.setLevel(level);
         detailedLogHandler.setLevel(level);
      }
   }

   public static Level getDetailedLevel() {
      return detailedLogger.getLevel();
   }

   public static void setSummaryLevel(Level level) {
      summaryLogger.setLevel(Level.ALL);
      if (level != null) {
         summaryLogHandler.setLevel(level);
      }
   }

   public static Level getSummaryLevel() {
      return summaryLogger.getLevel();
   }

   private static Date getNextLogSwitchTime() {
      Calendar cal = Calendar.getInstance();
      cal.set(11, 0);
      cal.set(12, 0);
      cal.set(13, 0);
      cal.add(5, 1);
      return cal.getTime();
   }

   private static synchronized boolean checkLogSwitch() {
      Calendar cal = Calendar.getInstance();
      Date now = cal.getTime();
      return now.after(nextLogHandlerSwitchTS);
   }

   public static void write(Level level, String msg) {
      if (checkLogSwitch()) {
         logSwitch();
      }

      detailedLogger.log(level, msg);
   }

   public static void write(Level level, String msg, Throwable t) {
      if (checkLogSwitch()) {
         logSwitch();
      }

      detailedLogger.log(level, msg, t);
   }

   public static void debug(String msg) {
      write(Level.FINER, msg);
   }

   public static void debug(Throwable e, String msg) {
      write(Level.FINER, msg, e);
   }

   public static void info(String msg) {
      write(Level.INFO, msg);
   }

   public static void config(String msg) {
      write(Level.CONFIG, msg);
   }

   public static void fine(String msg) {
      write(Level.FINE, msg);
   }

   public static void finest(String msg) {
      write(Level.FINEST, msg);
   }

   public static void severe(String msg) {
      write(Level.SEVERE, msg);
   }

   public static void severe(Throwable e, String msg) {
      write(Level.SEVERE, msg, e);
   }

   public static void error(String msg) {
      write(DvdplayLevel.ERROR, msg);
   }

   public static void error(Throwable e, String msg) {
      write(DvdplayLevel.ERROR, msg, e);
   }

   public static void warning(String msg) {
      write(Level.WARNING, msg);
   }

   public static void warning(Throwable e, String msg) {
      write(Level.WARNING, msg, e);
   }

   public static void summary(String msg) {
      if (checkLogSwitch()) {
         logSwitch();
      }

      summaryLogger.log(Level.ALL, msg);
      info("SUMMARY: " + msg);
   }

   public static void summary(Throwable e, String msg) {
      if (checkLogSwitch()) {
         logSwitch();
      }

      summaryLogger.log(Level.ALL, msg, e);
      detailedLogger.log(Level.ALL, "[SUMMARY]" + msg, e);
   }

   public static void summary(String msg, Level detailLogLevel) {
      if (checkLogSwitch()) {
         logSwitch();
      }

      summaryLogger.log(Level.ALL, "[DETAIL_LEVEL=" + detailLogLevel.getName() + "] " + msg);
      detailedLogger.log(detailLogLevel, "[SUMMARY] " + msg);
   }

   public static void summary(Throwable e, String msg, Level detailLogLevel) {
      if (checkLogSwitch()) {
         logSwitch();
      }

      summaryLogger.log(Level.ALL, "[DETAIL_LEVEL=" + detailLogLevel.getName() + "] " + msg, e);
      detailedLogger.log(Level.ALL, "[SUMMARY] " + msg, e);
   }

   public static String getCurrentDetailLogName() {
      return curDetailLogName;
   }

   public static String getCurrentSummaryLogName() {
      return curSummaryLogName;
   }
}
