package net.dvdplay.userop;

public class MaxDiscsAllowedException extends RentOpException {
   public MaxDiscsAllowedException(String message) {
      super(message);
   }

   public MaxDiscsAllowedException(int code, String message) {
      super(code, message);
   }
}
