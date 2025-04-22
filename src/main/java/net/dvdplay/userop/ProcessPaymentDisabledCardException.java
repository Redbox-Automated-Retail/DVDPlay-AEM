package net.dvdplay.userop;

import net.dvdplay.exception.DvdplayException;

public class ProcessPaymentDisabledCardException extends DvdplayException {
   public ProcessPaymentDisabledCardException(String msg) {
      super(msg);
   }
}
