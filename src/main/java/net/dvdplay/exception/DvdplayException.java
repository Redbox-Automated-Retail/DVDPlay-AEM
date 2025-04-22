package net.dvdplay.exception;

import java.sql.SQLException;

public class DvdplayException extends RuntimeException {
   private int mCode = 1002;

   public DvdplayException(String message) {
      super(message);
   }

   public DvdplayException(int code, String message) {
      super(message);
      this.mCode = code;
   }

   public DvdplayException(int code, Exception e) {
      super(e == null ? "" : e.getMessage());
      this.mCode = code;
   }

   public DvdplayException(int code, SQLException e) {
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

   public boolean isSystemCondition() {
      switch (this.mCode) {
         case 1000:
         case 1001:
         case 1002:
         case 1003:
         case 1004:
         case 1005:
         case 1006:
         case 1007:
            return true;
         default:
            return false;
      }
   }
}
