package net.dvdplay.logger;

import java.util.logging.Level;

public class DvdplayLevel extends Level {
   public static final Level ERROR = new DvdplayLevel("ERROR", 950);

   DvdplayLevel(String name, int value) {
      super(name, value);
   }

   public static Level parse(String name) throws IllegalArgumentException {
      try {
         return Level.parse(name);
      } catch (IllegalArgumentException e) {
         if (!"ERROR".equalsIgnoreCase(name) && !"950".equalsIgnoreCase(name)) {
            throw e;
         } else {
            return ERROR;
         }
      }
   }
}
