package net.dvdplay.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DvdplayFormatter extends Formatter {
   Date dat = new Date();
   private static final String format = "{0,date} {0,time}";
   private MessageFormat formatter;
   private Object[] args = new Object[1];
   private String lineSeparator = System.lineSeparator();

   public synchronized String format(LogRecord record) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
      StringBuilder sb = new StringBuilder();
      this.dat.setTime(record.getMillis());
      sb.append(dateFormat.format(this.dat));
      sb.append(" ");
      String message = this.formatMessage(record);
      sb.append("[");
      sb.append(record.getLevel().getLocalizedName());
      sb.append(" : ");
      sb.append(Thread.currentThread().getName());
      sb.append("] ");
      sb.append(message);
      sb.append(this.lineSeparator);
      if (record.getThrown() != null) {
         try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            sb.append(sw);
         } catch (Exception ignored) {
         }
      }

      return sb.toString();
   }
}
