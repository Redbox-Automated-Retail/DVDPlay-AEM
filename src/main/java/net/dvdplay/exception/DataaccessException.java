package net.dvdplay.exception;

import java.sql.SQLException;

public class DataaccessException extends DvdplayException {
   public DataaccessException(String message) {
      super(message);
   }

   public DataaccessException(int code, String message) {
      super(code, message);
   }

   public DataaccessException(int code, Exception e) {
      super(code, e == null ? "" : e.getMessage());
      this.initCause(e);
   }

   public DataaccessException(int code, Exception e, String message) {
      super(code, e == null ? "" : message);
      this.initCause(e);
   }

   public DataaccessException(int code, SQLException e) {
      super(code, e == null ? "" : e.getMessage());
      this.initCause(e);
   }

   public DataaccessException(int code, SQLException e, String message) {
      super(code, e == null ? "" : message);
      this.initCause(e);
   }

   public DataaccessException(int code, int sqlErrorCode) {
      super(code, "");
      SQLException e = new SQLException("", "", sqlErrorCode);
      this.initCause(e);
   }
}
