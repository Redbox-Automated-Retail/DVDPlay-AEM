package net.dvdplay.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.dvdplay.communication.RCSet;
import net.dvdplay.dataaccess.IDataProvider;
import net.dvdplay.dataaccess.IProcedure;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;

import javax.imageio.ImageIO;

public class Util {
   private static final int MILLISECONDS_PER_DAY = 86400000;
   public static final String mDateFormatString = "yyyy-MM-dd HH:mm:ss.SSS z";
   public static final String mDateFormatStringNoTimeZone = "yyyy-MM-dd HH:mm:ss.SSS";
   private static final String mTimeStampFormatString = "yyyyMMddHHmmssSSS";
   private static final String mTimeFormatString = "HH:mm:ss";
   private static final String mLogTimeStampFormatString = "yyyyMMdd";
   private static final String mTimeZoneFormatString = "z";
   public static final String mMMDDYYYYFormatString = "MM/dd/yyyy";
   private static final String HTTP = "http://";

   public static void sleep(int aMillSeconds) {
      try {
         Thread.sleep(aMillSeconds);
      } catch (Exception var2) {
         Log.write(DvdplayLevel.WARNING, var2.getMessage());
      }
   }

   public static String getTimeZoneString() {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("z");
      return lSimpleDateFormat.format(Calendar.getInstance().getTime());
   }

   public static Date stringToTime(String lTimeString) {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");

      try {
         return lSimpleDateFormat.parse(lTimeString);
      } catch (ParseException var4) {
         Log.write(DvdplayLevel.WARNING, "ParseException caught: " + var4.getMessage(), var4);
         return null;
      }
   }

   public static String timeToString(Date aDate) {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
      return lSimpleDateFormat.format(aDate);
   }

   public static Date stringToDate(String lDateString) {
      return stringToDate(lDateString, "yyyy-MM-dd HH:mm:ss.SSS z");
   }

   public static Date stringToDate(String aDateString, String aFormatString) {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(aFormatString);

      try {
         return lSimpleDateFormat.parse(aDateString);
      } catch (ParseException var5) {
         Log.write(DvdplayLevel.WARNING, "ParseException caught: " + var5.getMessage(), var5);
         return null;
      }
   }

   public static String dateToString(Date aDate) {
      return dateToString(aDate, "yyyy-MM-dd HH:mm:ss.SSS z");
   }

   public static String dateToString(Date aDate, String aFormat) {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(aFormat);
      return lSimpleDateFormat.format(aDate);
   }

   public static String getLogTimeStamp(Date aDate) {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
      return lSimpleDateFormat.format(aDate);
   }

   public static String getTimeStamp() {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      return lSimpleDateFormat.format(new Date(System.currentTimeMillis()));
   }

   public static int getRCSetIndexForFieldValue(RCSet aRCSet, String aFieldName, String aFieldValue) {
      try {
         for (int i = 0; i < aRCSet.rowCount(); i++) {
            if (!aRCSet.isDeleted(i) && aRCSet.getFieldValue(i, aRCSet.getFieldIndex(aFieldName)).compareTo(aFieldValue) == 0) {
               return i;
            }
         }

         return -1;
      } catch (DvdplayException var5) {
         Log.write(DvdplayLevel.WARNING, var5.getMessage());
         return -1;
      }
   }

   public static int getRCSetIndexForFieldValue(RCSet aRCSet, String[] aFieldNames, String[] aFieldValues) {
      try {
         for (int i = 0; i < aRCSet.rowCount(); i++) {
            if (!aRCSet.isDeleted(i)) {
               boolean lFoundIt = true;

               for (int j = 0; j < aFieldNames.length; j++) {
                  if (aRCSet.getFieldValue(i, aRCSet.getFieldIndex(aFieldNames[j])).compareTo(aFieldValues[j]) != 0) {
                     lFoundIt = false;
                     break;
                  }
               }

               if (lFoundIt) {
                  return i;
               }
            }
         }

         return -1;
      } catch (DvdplayException var6) {
         Log.write(DvdplayLevel.WARNING, var6.getMessage());
         return -1;
      }
   }

   public static boolean isDateBeforeNow(Date aDate) {
      Calendar lCal = Calendar.getInstance();
      Calendar lNow = Calendar.getInstance();
      lCal.setTime(aDate);
      lNow.setTime(new Date());
      return lCal.before(lNow);
   }

   public static boolean isDateAfterNow(Date aDate) {
      Calendar lCal = Calendar.getInstance();
      Calendar lNow = Calendar.getInstance();
      lCal.setTime(aDate);
      lNow.setTime(new Date());
      return lCal.after(lNow);
   }

   public static final boolean isNullOrBlankOrEmpty(String string) {
      return string == null || string.trim().equals("") || string.equalsIgnoreCase("<NONE>");
   }

   public static final boolean isNullOrBlankOrEmpty(String[] stringArray) {
      return stringArray == null ? true : isNullOrBlankOrEmpty(toString(stringArray, "")) || toString(stringArray, "").equalsIgnoreCase("<NONE>");
   }

   public static final boolean isNullOrBlankOrEmpty(Object object) {
      return object == null ? true : isNullOrBlankOrEmpty(object.toString());
   }

   public static final boolean isNullOrEmpty(String string) {
      return string == null || string.equals("");
   }

   public static final boolean isNullOrEmpty(Object object) {
      return object == null ? true : isNullOrEmpty(object.toString());
   }

   public static Date convertTimeToUTC(Date aDateLocal) {
      Date lUTCDate = null;

      try {
         TimeZone tz = TimeZone.getTimeZone("UTC");
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
         formatter.setTimeZone(tz);
         Log.debug(formatter.format(aDateLocal));
         lUTCDate = formatter.parse(formatter.format(aDateLocal));
         Log.debug("UTC Date = " + lUTCDate);
      } catch (Exception var4) {
         Log.error("Util.convertTimeToUTC(): Exception = " + var4.getMessage());
      }

      return lUTCDate;
   }

   public static String convertTimeToUTCString(Date aDateLocal) {
      String lUTCString = "";

      try {
         TimeZone tz = TimeZone.getTimeZone("UTC");
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
         formatter.setTimeZone(tz);
         lUTCString = formatter.format(aDateLocal);
         Log.debug(formatter.format(aDateLocal));
      } catch (Exception var4) {
         Log.error("Util.convertTimeToUTC exception : " + var4);
      }

      return lUTCString;
   }

   public static String convertTimeToUTCStringNoTimeZone(Date aDateLocal) {
      String lUTCString = "";

      try {
         TimeZone tz = TimeZone.getTimeZone("UTC");
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
         formatter.setTimeZone(tz);
         lUTCString = formatter.format(aDateLocal);
      } catch (Exception var4) {
         Log.error("Util.convertTimeToUTC exception : " + var4);
      }

      return lUTCString;
   }

   public static Calendar convertTimeToCalenderUTC(Date aDate) {
      Calendar lCalendar = null;

      try {
         SimpleTimeZone utc = new SimpleTimeZone(0, "UTC");
         lCalendar = new GregorianCalendar(utc);
         lCalendar.setTime(aDate);
      } catch (Exception var3) {
         Log.error("Util.convertTimeToUTC(): Exception = " + var3.getMessage());
      }

      return lCalendar;
   }

   public static Calendar getCalendarUTCTime(Date aUTCTime) {
      String lDateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
      String lUTC = " UTC";
      Calendar lCalendarUTCTime = null;

      try {
         SimpleDateFormat sf = new SimpleDateFormat(lDateFormat);
         String lDueTimeString = sf.format(aUTCTime);
         Date lTime = stringToDate(lDueTimeString + lUTC);
         lCalendarUTCTime = convertTimeToCalenderUTC(lTime);
      } catch (Exception var7) {
         Log.debug("RenewalTransaction.getCalendar(): Exception, ERROR = " + var7.getMessage());
      }

      return lCalendarUTCTime;
   }

   public static long calculateCheckSum(String aFile) throws FileNotFoundException, IOException {
      Adler32 inChecker = new Adler32();
      CheckedInputStream in = new CheckedInputStream(new FileInputStream(aFile), inChecker);

      int c;
      while ((c = in.read()) != -1) {
         inChecker.update(c);
      }

      in.close();
      return in.getChecksum().getValue();
   }

   public static void unzipFile(String aZipFile, String aDestinationPath) throws Exception {
      ZipInputStream in = null;
      ZipEntry lZipEntry = null;
      DataOutputStream out = null;
      in = new ZipInputStream(new DataInputStream(new BufferedInputStream(new FileInputStream(aZipFile))));

      while ((lZipEntry = in.getNextEntry()) != null) {
         out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(aDestinationPath + "\\" + lZipEntry.getName())));
         byte[] bytes = new byte[1024];

         int len;
         while ((len = in.read(bytes)) > 0) {
            out.write(bytes, 0, len);
         }

         out.flush();
      }

      in.close();
      out.close();
   }

   public static void createZipFile(String[] aSourceFiles, String aZipFile) {
      DataInputStream in = null;
      ZipOutputStream out = null;
      if (aSourceFiles.length != 0) {
         try {
            out = new ZipOutputStream(new DataOutputStream(new BufferedOutputStream(new FileOutputStream(aZipFile))));

            for (int i = 0; i < aSourceFiles.length; i++) {
               in = new DataInputStream(new BufferedInputStream(new FileInputStream(aSourceFiles[i])));
               out.putNextEntry(new ZipEntry(aSourceFiles[i].substring(aSourceFiles[i].lastIndexOf("\\") + 1)));
               byte[] bytes = new byte[1024];

               int len;
               while ((len = in.read(bytes)) > 0) {
                  out.write(bytes, 0, len);
               }

               out.flush();
            }

            in.close();
            out.close();
         } catch (Exception var7) {
            Log.write(DvdplayLevel.WARNING, var7.getMessage());
         }
      }
   }

   public static final String toString(String[] stringArray, String delimiter) {
      if (stringArray != null && stringArray.length >= 1) {
         StringBuffer sbuffer = new StringBuffer();
         int arrayLength = stringArray.length - 1;

         for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(stringArray[i]);
            sbuffer.append(delimiter);
         }

         sbuffer.append(stringArray[arrayLength]);
         return sbuffer.toString();
      } else {
         return "";
      }
   }

   public static final String toString(int[] intArray, String delimiter) {
      if (intArray != null && intArray.length >= 1) {
         StringBuffer sbuffer = new StringBuffer();
         int arrayLength = intArray.length - 1;

         for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(intArray[i]);
            sbuffer.append(delimiter);
         }

         sbuffer.append(intArray[arrayLength]);
         return sbuffer.toString();
      } else {
         return "";
      }
   }

   public static final String toString(String string1, String string2, String delimiter) {
      if (isNullOrBlankOrEmpty(string1) && isNullOrBlankOrEmpty(string2)) {
         return "";
      } else if (isNullOrBlankOrEmpty(string1)) {
         return string2;
      } else if (isNullOrBlankOrEmpty(string2)) {
         return string1;
      } else {
         StringBuffer sbuffer = new StringBuffer();
         sbuffer.append(string1).append(delimiter).append(string2);
         return sbuffer.toString();
      }
   }

   public static int getTimeLapseDays(Calendar aEarlyTime, Calendar aLaterTime) {
      long lEarlytime = aEarlyTime.getTimeInMillis();
      long lLatertime = aLaterTime.getTimeInMillis();
      BigDecimal lDiffT = new BigDecimal((double)(lLatertime - lEarlytime));
      BigDecimal lSecondsPerDay = new BigDecimal(8.64E7);
      int lDays = lDiffT.divide(lSecondsPerDay, 2).intValue();
      Log.info("Util.getTimeLapseDays() : earlyT = " + lEarlytime + ", LaterT = " + lLatertime + ", diff = " + lDiffT + ", days = " + lDays);
      return lDays;
   }

   public static int getTimeLapseDays(Date aEarlyTime, Date aLaterTime) {
      Calendar lEarly = dateToCalendar(convertTimeToUTC(aEarlyTime));
      Calendar lLater = dateToCalendar(convertTimeToUTC(aLaterTime));
      return getTimeLapseDays(lEarly, lLater);
   }

   public static Calendar dateToCalendar(Date aDate) {
      Calendar lCalDate = Calendar.getInstance();
      lCalDate.setTime(aDate);
      return lCalDate;
   }

   public static String getServerConfig(String aConfigName, IDataProvider aProvider) {
      String lServerName = "";

      try {
         IProcedure lproc = aProvider.createProcedure("{call ffGetServerConfig(?,?)}");
         lproc.setString(1, aConfigName);
         lproc.registerOutParameter(2, 12);
         lproc.execute();
         lServerName = lproc.getString(2);
      } catch (Exception var4) {
         Log.error("Util.getServerConfig(): Exception, ERROR = " + var4.getMessage());
      }

      return lServerName;
   }

   public static String getRequestURL(String aRequest, IDataProvider aProvider) {
      try {
         String lURLHost = getServerConfig("receipt.urlhost", aProvider);
         String lURL = "http://" + lURLHost + "/AEMServer/servlet/AEMServer";
         String lCommandString = URLEncoder.encode(aRequest, "UTF-8");
         lURL = lURL + "?COMMAND=" + lCommandString;
         Log.debug("URL String = " + lURL);
         return lURL;
      } catch (IOException var5) {
         Log.error("Util.getRequestURL(): IOException, ERROR = " + var5.getMessage());
         return null;
      }
   }

   public static String[] getShortWeekdays(Locale aLocale) {
      DateFormatSymbols lSymbols = new DateFormatSymbols(aLocale);
      return lSymbols.getShortWeekdays();
   }

   public static String capFirstChar(String origString, Locale aLocale) {
      return origString.substring(0, 1).toUpperCase(aLocale) + origString.toLowerCase(aLocale).substring(1, origString.length());
   }

   public static void scaleAndSaveImage(String aOriginalFilename, String aNewFilename, int aNewWidth, int aNewHeight) {
      try {
         BufferedImage originalImage = ImageIO.read(new File(aOriginalFilename));
         BufferedImage resizedImage = new BufferedImage(aNewWidth, aNewHeight, BufferedImage.TYPE_INT_RGB);

         Graphics2D graphics2D = resizedImage.createGraphics();
         graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         graphics2D.drawImage(originalImage, 0, 0, aNewWidth, aNewHeight, null);
         graphics2D.dispose();

         ImageIO.write(resizedImage, "jpg", new File(aNewFilename));
      } catch (IOException e) {
         throw new DvdplayException(e.getMessage());
      }
   }

   public static String getPosterSmallName(String aFilename) {
      try {
         String lFilename = new File(aFilename).getName();
         return lFilename.substring(0, lFilename.lastIndexOf(".")) + "-small" + ".jpg";
      } catch (Exception var2) {
         return "PosterNotFoundSmall.jpg";
      }
   }

   public static void writeToFile(String aFilename, String aContent) throws Exception {
      BufferedWriter bfout = null;

      try {
         bfout = new BufferedWriter(new FileWriter(aFilename));
         bfout.write(aContent);
         bfout.flush();
      } finally {
         if (bfout != null) {
            bfout.close();
         }
      }
   }

   public static void setLockFile(String aPath, String aContent) throws Exception {
      String lFilename = "lock" + getTimeStamp();
      File lCurrFile = new File(aPath + "\\" + lFilename);
      Log.write(DvdplayLevel.INFO, "Setting lockfile");
      writeToFile(aPath + "\\" + lFilename, aContent);
      Calendar lCal = Calendar.getInstance();
      lCal.setTimeInMillis(lCurrFile.lastModified());
      lCal.add(1, -2);
      if (!lCurrFile.setLastModified(lCal.getTimeInMillis())) {
      }
   }

   public static void releaseLockFile(String aPath) {
      File lDir = new File(aPath);
      File[] lAllFilesList = lDir.listFiles();

      for (int i = 0; i < lAllFilesList.length; i++) {
         if (!lAllFilesList[i].isDirectory() && lAllFilesList[i].getName().startsWith("lock")) {
            if (!lAllFilesList[i].delete()) {
               throw new DvdplayException("Could not delete lockfile");
            }

            Log.write(DvdplayLevel.INFO, "Releasing lockfile");
         }
      }
   }

   public static boolean checkLockFile(String aPath) {
      File lDir = new File(aPath);
      File[] lAllFilesList = lDir.listFiles();

      for (int i = 0; i < lAllFilesList.length; i++) {
         if (!lAllFilesList[i].isDirectory() && lAllFilesList[i].getName().startsWith("lock")) {
            Log.write(DvdplayLevel.FINE, "Found lockfile");
            return true;
         }
      }

      return false;
   }

   public static String dateToTZString(Date aDate, String aTimeZoneId, String aDateFormat) {
      String lTZString = "";

      try {
         TimeZone tz = TimeZone.getTimeZone(aTimeZoneId);
         SimpleDateFormat formatter = new SimpleDateFormat(aDateFormat);
         formatter.setTimeZone(tz);
         lTZString = formatter.format(aDate);
      } catch (Exception var6) {
         Log.error("Util.dateToTZString exception : " + var6);
      }

      return lTZString;
   }

   public static Date timestampToDate(Date aTimestamp, String aTimezone) {
      return stringToDate(dateToString(aTimestamp, "yyyy-MM-dd HH:mm:ss.SSS") + " " + aTimezone);
   }

   public static String changeDateStringFormat(String aFromFormat, String aFromString, String aToFormat) {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(aFromFormat);

      Date lDate;
      try {
         lDate = lSimpleDateFormat.parse(aFromString);
      } catch (ParseException var6) {
         Log.write(DvdplayLevel.WARNING, "ParseException caught: " + var6.getMessage());
         return null;
      }

      return dateToString(lDate, aToFormat);
   }

   public static int setNBit(int aValue, int aBitMask) {
      return aValue | aBitMask;
   }

   public static int resetNBit(int value, int bitMask) {
      return value & ~bitMask;
   }

   public static boolean isBitNSet(int value, int bitMask) {
      int result = value & bitMask;
      return result == bitMask;
   }

   public static boolean isCurrVersion(String aReqestVersion) {
      int lDigit = 2;
      boolean lIsCurrV = true;
      BigDecimal lReqestVersion = new BigDecimal(aReqestVersion).setScale(lDigit, 4);
      if (lReqestVersion.compareTo(new BigDecimal("2.0")) < 0) {
         lIsCurrV = false;
      }

      return lIsCurrV;
   }

   public static String getDayString(int dayCount, int localeID) {
      return dayCount > 1 ? " days" : " day";
   }
}
