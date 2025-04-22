package net.dvdplay.inventory;

import java.sql.SQLException;
import net.dvdplay.exception.DvdplayException;

public class InventoryException extends DvdplayException {
   private int mCode = 1002;

   public InventoryException(String message) {
      super(message);
   }

   public InventoryException(int code, String message) {
      super(message);
      this.mCode = code;
   }

   public InventoryException(int code, Exception e) {
      super(e == null ? "" : e.getMessage());
      this.mCode = code;
   }

   public InventoryException(int code, SQLException e) {
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
