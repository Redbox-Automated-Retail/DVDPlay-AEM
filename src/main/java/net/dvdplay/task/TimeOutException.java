package net.dvdplay.task;

import java.sql.SQLException;
import net.dvdplay.exception.DvdplayException;

public class TimeOutException extends DvdplayException {
   private int mCode = 1002;

   public TimeOutException(String message) {
      super(message);
   }

   public TimeOutException(int code, String message) {
      super(message);
      this.mCode = code;
   }

   public TimeOutException(int code, Exception e) {
      super(e == null ? "" : e.getMessage());
      this.mCode = code;
   }

   public TimeOutException(int code, SQLException e) {
      super(e == null ? "" : e.getMessage());
      this.mCode = code;
      this.initCause(e);
   }

   public int getCode() {
      return this.mCode;
   }

   public String getCodeAsString() {
      return Integer.toString(this.mCode);
   }

   public void setCode(int newCode) {
      this.mCode = newCode;
   }
}
