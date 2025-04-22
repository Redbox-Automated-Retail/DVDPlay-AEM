package net.dvdplay.aem;

public class Sensor {
   private final byte mAddr;
   private final int mBitNum;
   private boolean mInvert;

   public Sensor(byte aAddr, int aBitNum, boolean aInvert) {
      this.mAddr = aAddr;
      this.mBitNum = aBitNum;
      this.mInvert = aInvert;
   }

   public boolean on() {
      ServoFactory.getInstance().refreshPorts();
      return this.mInvert ? !NMC.IoInBitVal(this.mAddr, this.mBitNum) : NMC.IoInBitVal(this.mAddr, this.mBitNum);
   }
}
