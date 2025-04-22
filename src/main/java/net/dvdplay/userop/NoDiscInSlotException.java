package net.dvdplay.userop;

import net.dvdplay.exception.DvdplayException;

public class NoDiscInSlotException extends DvdplayException {
   public NoDiscInSlotException(String aMsg) {
      super(aMsg);
   }
}
