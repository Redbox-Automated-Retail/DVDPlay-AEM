package net.dvdplay.userop;

import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.BarCode;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoException;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.RCSet;
import net.dvdplay.dom.DOMData;
import net.dvdplay.inventory.Disc;
import net.dvdplay.inventory.InventoryException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.task.QueueJob;

public class ReturnOp {
   private DiscReturn mDiscReturn = new DiscReturn();
   private BarCode mBarCode = new BarCode();
   private int mSlot;

   public int getDiscCount() {
      return this.mDiscReturn.getDiscItemListCount();
   }

   public void readBarCode() {
      Aem lAem = AemFactory.getInstance();
      Servo lServo = ServoFactory.getInstance();
      Aem.logSummaryMessage("Starting bar code reader.");

      try {
         lServo.mLight.on();
         lServo.readBarCode(this.mBarCode);
         if (this.mBarCode.mCode1 == null || this.mBarCode.mCode2 == null) {
            throw new ReturnOpException("BarCode read failed");
         }

         if (!Aem.checkGroupCode(this.mBarCode.mCode1)) {
            throw new GroupCodeException("GroupCode " + this.mBarCode.mCode1 + " does not belong to this AEM");
         }
      } catch (ServoException var8) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var8.getMessage());
         throw new ReturnOpException("BarCode read failed");
      } finally {
         lServo.mLight.off();
      }
   }

   public boolean intakeDisc1() {
      Aem.setDiscActive();
      Aem lAem = AemFactory.getInstance();
      Servo lServo = ServoFactory.getInstance();

      try {
         this.mSlot = lAem.goToNextEmptySlot();
      } catch (InventoryException var6) {
         Aem.logDetailMessage(DvdplayLevel.FINE, "InventoryException caught: " + var6.getMessage());
      }

      try {
         lServo.intakeDisc(4000, false);
         this.recordDisc();
      } catch (ServoException var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var5.getMessage());
         throw new ReturnOpException("Intake disc 1 failed.");
      }

      try {
         lAem.goToNextEmptySlot();
      } catch (InventoryException var4) {
         Aem.logDetailMessage(DvdplayLevel.FINE, "InventoryException caught: " + var4.getMessage());
      }

      return true;
   }

   public boolean intakeDisc2() {
      Aem lAem = AemFactory.getInstance();
      Servo lServo = ServoFactory.getInstance();

      try {
         lServo.intakeDisc(5000, false);
         this.recordDisc();
      } catch (ServoException var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var5.getMessage());
         throw new ReturnOpException("Intake disc 2 failed.");
      }

      try {
         lAem.goToNextEmptySlot();
      } catch (InventoryException var4) {
         Aem.logDetailMessage(DvdplayLevel.FINE, "InventoryException caught: " + var4.getMessage());
      }

      return true;
   }

   public boolean clearSlot() {
      Servo lServo = ServoFactory.getInstance();
      return !lServo.isDiscJammed() ? true : lServo.clearDisc(true);
   }

   public boolean discAtDoor() {
      return ServoFactory.getInstance().isDiscAtDoor();
   }

   public void recordDisc() {
      Disc lDisc = new Disc();
      String[] lValues = new String[DvdplayBase.DISC_DETAIL_FIELD_NAMES.length];
      new RCSet(DvdplayBase.DISC_DETAIL_FIELD_NAMES, DvdplayBase.DISC_DETAIL_FIELD_TYPES);
      lDisc.setGroupCode(this.mBarCode.mCode1);
      lDisc.setDiscCode(this.mBarCode.mCode2);
      lDisc.setSlot(this.mSlot);
      this.mDiscReturn.addDiscItem(new DiscItem(lDisc, 3));
      Aem.addUnknownDisc(this.mBarCode.mCode1, this.mBarCode.mCode2, this.mSlot);
      DOMData.save();
      ServoFactory.getInstance().setServoParameters();
      Aem.logSummaryMessage("Disc returned: (" + this.mBarCode.mCode1 + "," + this.mBarCode.mCode2 + ") in slot " + this.mSlot + ".");
   }

   public void setTempBadSlot() {
      Aem.addTempBadSlot(this.mSlot);
   }

   public synchronized void createQueueJob() {
      if (this.getDiscCount() > 0) {
         this.mDiscReturn.mNvPairSet = this.mDiscReturn.getDiscReturnedNvPairSet();
         Aem.mQueue.addQueueJob(new QueueJob(new DiscReturn(this.mDiscReturn)), true);
         this.mDiscReturn.clearDiscItems();
      }
   }

   public String getGroupCode() {
      return this.mBarCode.mCode1;
   }

   public String getDiscCode() {
      return this.mBarCode.mCode2;
   }

   public void clearDiscItems() {
      this.mDiscReturn.clearDiscItems();
   }
}
