package net.dvdplay.aem;

import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class Arm {
   public static final String EJECT = "EJECT";
   public static final String CLEAR = "CLEAR";
   public static final String UNKNOWN = "UNKNOWN";
   private static boolean mInitialized;

   public static void clear() throws ArmException {
      try {
         changeArmPosition("CLEAR");
      } catch (ArmException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ArmException caught: " + var1.getMessage());
         throw new ArmException("Arm clear failed.");
      }
   }

   public static void eject() throws ArmException {
      try {
         changeArmPosition("EJECT");
      } catch (ArmException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ArmException caught: " + var1.getMessage());
         throw new ArmException("Arm eject failed.");
      }
   }

   public static void initialize() throws ArmException {
      Aem.logSummaryMessage("Arm initialize");

      try {
         clear();
         mInitialized = true;
      } catch (ArmException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ArmException caught: " + var1.getMessage());
         throw new ArmException("Arm initialize failed.");
      }
   }

   public static String getPosition() {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      lServoEx.refreshPorts();
      boolean lPos0 = NMC.IoInBitVal((byte)2, 2);
      boolean lPos1 = NMC.IoInBitVal((byte)2, 4);
      if (lPos0) {
         return "EJECT";
      } else {
         return lPos1 ? "CLEAR" : "UNKNOWN";
      }
   }

   public static void motorOn() throws ArmException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)3, 1)) {
         throw new ArmException("NMC.IoSetOutBit failed");
      }
   }

   public static void motorOff() throws ArmException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)3, 1)) {
         throw new ArmException("NMC.IoClrOutBit failed");
      }
   }

   private static void enableClearStopOnFlag() throws ArmException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)2, 3)) {
         throw new ArmException("NMC.IoSetOutBit failed");
      }
   }

   private static void disableClearStopOnFlag() throws ArmException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)2, 3)) {
         throw new ArmException("NMC.IoClrOutBit failed");
      }
   }

   private static void enableEjectStopOnFlag() throws ArmException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)2, 6)) {
         throw new ArmException("NMC.IoSetOutBit failed");
      }
   }

   private static void disableEjectStopOnFlag() throws ArmException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)2, 6)) {
         throw new ArmException("NMC.IoClrOutBit failed");
      }
   }

   private static void changeArmPosition(String aPosition) throws ArmException {
      int lTimeout = 3000;
      if (aPosition != getPosition()) {
         motorOn();
         if (aPosition == "CLEAR") {
            disableEjectStopOnFlag();
            enableClearStopOnFlag();
         } else {
            if (aPosition != "EJECT") {
               throw new ArmException("Invalid position: " + aPosition);
            }

            disableClearStopOnFlag();
            enableEjectStopOnFlag();
         }

         long lStartTime = System.currentTimeMillis();
         Servo lServo = ServoFactory.getInstance();

         String lPosition;
         do {
            Util.sleep(lServo.getArmEjectWaitTime());
            lPosition = getPosition();
         } while (lPosition != aPosition && System.currentTimeMillis() - lStartTime < 3000L);

         motorOff();
         disableEjectStopOnFlag();
         disableClearStopOnFlag();
         if (lPosition != aPosition) {
            throw new ArmException("Arm operation timeout: " + aPosition);
         }
      }
   }

   public static boolean isInitialized() {
      return mInitialized;
   }

   public static void resetInitialized() {
      mInitialized = false;
   }

   public Arm() {
      mInitialized = false;
   }
}
