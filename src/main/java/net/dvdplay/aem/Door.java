package net.dvdplay.aem;

import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class Door {
   public static final String CLAMP = "CLAMP";
   public static final String CLOSE = "CLOSE";
   public static final String OPEN = "OPEN";
   public static final String UNKOWN = "UNKNOWN";
   private static boolean mInitialized;

   public static String getPosition() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      lServoEx.refreshPorts();
      boolean lPos0 = NMC.IoInBitVal((byte)3, 7);
      boolean lPos1 = NMC.IoInBitVal((byte)3, 8);
      boolean lPos2 = NMC.IoInBitVal((byte)3, 9);
      if (lPos0) {
         return "OPEN";
      } else if (lPos1) {
         return "CLAMP";
      } else {
         return lPos2 ? "CLOSE" : "UNKNOWN";
      }
   }

   public static void clamp() throws DoorException {
      try {
         changeDoorPosition("CLAMP");
      } catch (DoorException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "DoorException caught: " + var1.getMessage());
         throw new DoorException("Door clamp failed.");
      }
   }

   public static void open() throws DoorException {
      try {
         changeDoorPosition("OPEN");
      } catch (DoorException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "DoorException caught: " + var1.getMessage());
         throw new DoorException("Door open failed.");
      }
   }

   public static boolean isLocked() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      return !NMC.IoInBitVal((byte)3, 6);
   }

   public static void close() throws DoorException {
      try {
         changeDoorPosition("CLOSE");
      } catch (DoorException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "DoorException caught: " + var1.getMessage());
         throw new DoorException("Door close failed.");
      }

      if (!isLocked()) {
         throw new DoorException("Door not locked.");
      }
   }

   private static void enableOpenStopOnFlag() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)3, 0)) {
         throw new DoorException("NMC.IoSetOutBit failed");
      }
   }

   private static void disableOpenStopOnFlag() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)3, 0)) {
         throw new DoorException("NMC.IoClrOutBit failed");
      }
   }

   private static void enableClampStopOnFlag() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)3, 5)) {
         throw new DoorException("NMC.IoSetOutBit failed");
      }
   }

   private static void disableClampStopOnFlag() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)3, 5)) {
         throw new DoorException("NMC.IoClrOutBit failed");
      }
   }

   private static void enableCloseStopOnFlag() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)2, 1)) {
         throw new DoorException("NMC.IoSetOutBit failed");
      }
   }

   private static void disableCloseStopOnFlag() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)2, 1)) {
         throw new DoorException("NMC.IoClrOutBit failed");
      }
   }

   private static void changeDoorPosition(String aPosition) throws DoorException {
      int lTimeout = 7000;
      if (aPosition != getPosition()) {
         motorOn();
         disableOpenStopOnFlag();
         disableClampStopOnFlag();
         disableCloseStopOnFlag();
         if (aPosition == "OPEN") {
            enableOpenStopOnFlag();
         } else if (aPosition == "CLAMP") {
            enableClampStopOnFlag();
         } else {
            if (aPosition != "CLOSE") {
               throw new DoorException("Invalid position: " + aPosition);
            }

            enableCloseStopOnFlag();
         }

         long lStartTime = System.currentTimeMillis();

         String lPosition;
         do {
            Util.sleep(Servo.mPollPIOBitsSleepTime);
            lPosition = getPosition();
         } while (lPosition != aPosition && System.currentTimeMillis() - lStartTime < 7000L);

         disableOpenStopOnFlag();
         disableClampStopOnFlag();
         disableCloseStopOnFlag();
         motorOff();
         if (lPosition != aPosition) {
            throw new DoorException("Door operation timeout: " + aPosition);
         }
      }
   }

   public static void motorOn() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)2, 11)) {
         throw new DoorException("NMC.IoSetOutBit failed");
      }
   }

   public static void motorOff() throws DoorException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)2, 11)) {
         throw new DoorException("NMC.IoClrOutBit failed");
      }
   }

   public static void initialize() throws DoorException {
      Aem.logSummaryMessage("Door initialize");

      try {
         clamp();
         open();
         close();
         mInitialized = true;
      } catch (DoorException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "DoorException caught: " + var1.getMessage());
         throw new DoorException("Door initialize failed.");
      }
   }

   public static boolean isInitialized() {
      return mInitialized;
   }

   public static void resetInitialized() {
      mInitialized = false;
   }

   public Door() {
      mInitialized = false;
   }
}
