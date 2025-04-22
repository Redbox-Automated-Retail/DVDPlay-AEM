package net.dvdplay.userop;

import net.dvdplay.exception.DvdplayException;

public class ProcessPaymentValidationException extends DvdplayException {
   public ProcessPaymentValidationException(String msg) {
      super(msg);
   }
}
