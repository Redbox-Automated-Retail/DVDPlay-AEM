package net.dvdplay.poll;

import java.sql.SQLException;
import net.dvdplay.exception.DvdplayException;

public class PollException extends DvdplayException {
   private int mCode = 1002;

   public PollException(String message) {
      super(message);
   }

   public PollException(int code, String message) {
      super(message);
      this.mCode = code;
   }

   public PollException(int code, Exception e) {
      super(e == null ? "" : e.getMessage());
      this.mCode = code;
   }

   public PollException(int code, SQLException e) {
      super(e == null ? "" : e.getMessage());
      this.mCode = code;
      this.initCause(e);
   }

   public PollException(int errCode, Exception e, String errMessage) {
      super(errCode, errMessage);
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
