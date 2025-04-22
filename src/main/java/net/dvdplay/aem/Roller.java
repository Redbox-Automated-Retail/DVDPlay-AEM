package net.dvdplay.aem;

public class Roller {
   public static final int TIME_OUT = 3000;
   public static final int TIME_OUT2 = 5000;

   public static void start() throws RollerException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)3, 3)) {
         throw new RollerException("Roller start failed");
      }
   }

   public static void stop() throws RollerException {
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)3, 3)) {
         throw new RollerException("Roller stop failed");
      }
   }

   public static void in() throws RollerException {
      stop();
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoClrOutBit((byte)3, 2)) {
         throw new RollerException("Roller in failed");
      } else {
         start();
      }
   }

   public static void out() throws RollerException {
      stop();
      ServoEx lServoEx = ServoEx.valueOf(ServoFactory.getInstance());
      if (!NMC.IoSetOutBit((byte)3, 2)) {
         throw new RollerException("Roller out failed");
      } else {
         start();
      }
   }
}
