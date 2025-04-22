package net.dvdplay.userop;

import net.dvdplay.exception.DvdplayException;

public class RentOpException extends DvdplayException {
   public RentOpException(String message) {
      super(message);
   }

   public RentOpException(int code, String message) {
      super(code, message);
   }
}
