package net.dvdplay.aem;

import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class Carousel {
   private static final int MAX_SETTLING_TRIES = 10;
   public static final int MAX_SETTLING_ERROR = 4;
   private static final int CAROUSEL_SHAKE_POS = 5;
   private static final int HOMING_TIMEOUT = 60000;
   private static boolean mStopped;
   private static boolean mInitialized;

   public Carousel() {
      mStopped = false;
      mInitialized = false;
   }

   public static boolean isInitialized() {
      return mInitialized;
   }

   public static void resetInitialized() {
      mInitialized = false;
   }

   public static void enable() throws CarouselException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.ServoStopMotor((byte)1, (byte)1)) {
         lServoEx.refreshPorts();
         throw new CarouselException("NMC.ServoStopMotor failed");
      } else if (!NMC.ServoStopMotor((byte)1, (byte)9)) {
         lServoEx.refreshPorts();
         throw new CarouselException("NMC.ServoStopMotor failed");
      }
   }

   public static void disable() throws CarouselException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.ServoStopMotor((byte)1, (byte)2)) {
         lServoEx.refreshPorts();
         throw new CarouselException("NMC.ServoStopMotor failed");
      }
   }

   private static void checkOkayToRotate() throws CarouselException {
      if (!Arm.getPosition().equals("CLEAR")) {
         throw new CarouselException("Cannot rotate.  Arm not clear.");
      } else if (!Door.isLocked()) {
         throw new CarouselException("Cannot rotate.  Door not locked.");
      }
   }

   public static void goToOffset(int aPosition) throws CarouselException {
      checkOkayToRotate();

      for (int i = 1; i <= 10; i++) {
         try {
            moveCarouselToOffset(aPosition, false);
            Util.sleep(1000);
         } catch (CarouselException var4) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "CarouselException caught: " + var4.getMessage());
            throw new CarouselException("goToOffset(" + aPosition + ") failed.");
         }

         int lPositionError = Integer.parseInt(getPosition()) - aPosition;
         if (Math.abs(lPositionError) <= 4) {
            return;
         }

         Aem.logDetailMessage(DvdplayLevel.WARNING, "Not settled on try " + i + ", position error = " + lPositionError);
      }

      throw new CarouselException("Could not settle carousel in 10 tries.");
   }

   private static void moveCarouselToOffset(int aPosition, boolean aIsHoming) throws CarouselException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      moveCarouselToOffset(aPosition, lServoEx.getServoVelocity(), lServoEx.getServoAcceleration(), aIsHoming);
   }

   public static void loadTraj(int aPosition, int aVelocity, int aAcceleration) {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.ServoLoadTraj((byte)1, (byte)-105, aPosition + Servo.mServoHomePosition, aVelocity, aAcceleration, (byte)0)) {
         lServoEx.refreshPorts();
         throw new CarouselException("NMC.ServoLoadTraj failed");
      }
   }

   public static void moveCarouselToOffset(int aPosition, int aVelocity, int aAcceleration, boolean aIsHoming) throws CarouselException {
      mStopped = false;
      if (!getPosition().equals(Integer.toString(aPosition))) {
         loadTraj(aPosition, aVelocity, aAcceleration);
         if (!aIsHoming) {
            long lStartTime = System.currentTimeMillis();
            ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());

            boolean lMoveDone;
            do {
               Util.sleep(lServoEx.getMoveToOffsetWaitTime());
               if (!NMC.NmcReadStatus((byte)1, (byte)1)) {
                  lServoEx.refreshPorts();
                  throw new CarouselException("NMC.NmcReadStatus failed");
               }

               lMoveDone = (NMC.NmcGetStat((byte)1) & 1) != 0;
            } while (!lMoveDone && System.currentTimeMillis() - lStartTime < lServoEx.getMoveToOffsetTimeOut());

            if (!lMoveDone) {
               stop();
               throw new CarouselException("MoveCarousel timeout: offset: " + aPosition);
            } else {
               Util.sleep(1000);
            }
         }
      }
   }

   public static void moveCarouselVelocityMode(int aVelocity, int aAcceleration) {
      mStopped = false;
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.ServoLoadTraj((byte)1, (byte)-73, 0, aVelocity, aAcceleration, (byte)0)) {
         lServoEx.refreshPorts();
         throw new CarouselException("NMC.ServoLoadTraj failed");
      }
   }

   public static void carouselShake() throws CarouselException {
      try {
         int lCurrPos = Integer.parseInt(getPosition());
         moveCarouselToOffset(lCurrPos + 5, false);
         Util.sleep(100);
         moveCarouselToOffset(lCurrPos - 5, false);
         Util.sleep(100);
         moveCarouselToOffset(lCurrPos, false);
      } catch (CarouselException var1) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "CarouselException caught: " + var1.getMessage());
         throw new CarouselException("Carousel shake failed");
      }
   }

   public static void stop() throws CarouselException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.ServoStopMotor((byte)1, (byte)9)) {
         lServoEx.refreshPorts();
         throw new CarouselException("NMC.ServoStopMotor failed");
      } else {
         mStopped = true;
      }
   }

   public static String getPosition() throws CarouselException {
      int lPosition = 0;
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.NmcReadStatus((byte)1, (byte)1)) {
         lServoEx.refreshPorts();
         throw new CarouselException("NMC.NmcReadStatus failed");
      } else {
         lPosition = NMC.ServoGetPos((byte)1);
         return String.valueOf(lPosition - Servo.mServoHomePosition);
      }
   }

   public boolean isCarouselAtSlot(int aSlot) {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (Math.abs(Math.abs(lServoEx.getSlotOffset(aSlot)) - Math.abs(Integer.parseInt(getPosition()))) <= 4) {
         return true;
      } else {
         Aem.logDetailMessage(DvdplayLevel.INFO, "Carousel is not at slot " + aSlot + ". Position = " + getPosition());
         return false;
      }
   }

   public static void initialize() throws CarouselException {
      Aem.logSummaryMessage("Carousel initialize");
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());

      try {
         enable();
         checkOkayToRotate();
         moveCarouselToOffset(lServoEx.getNumTicks() * 2, true);
         if (!NMC.ServoSetHoming((byte)1, (byte)33)) {
            lServoEx.refreshPorts();
            throw new CarouselException("NMC.ServoSetHoming failed");
         } else {
            long lStartTime = System.currentTimeMillis();

            do {
               Util.sleep(Servo.mPollPIOBitsSleepTime);
               NMC.NmcReadStatus((byte)1, (byte)16);
               if (mStopped) {
                  mStopped = false;
                  return;
               }
            } while ((NMC.NmcGetStat((byte)1) & 128) == 128 && System.currentTimeMillis() - lStartTime <= 60000L);

            if (System.currentTimeMillis() - lStartTime > 60000L) {
               stop();
               throw new CarouselException("Carousel homing timed out.");
            } else {
               Util.sleep(3000);
               Servo.mServoHomePosition = NMC.ServoGetHome((byte)1);
               mInitialized = true;
            }
         }
      } catch (CarouselException var4) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "CarouselException caught: " + var4.getMessage());
         throw new CarouselException("Carousel initialize failed.");
      }
   }
}
