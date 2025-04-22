package net.dvdplay.inventory;

import net.dvdplay.aem.Aem;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.logger.DvdplayLevel;

public class SlotIndex {
   private int[] mSlotIndexList;

   public SlotIndex() {
      Servo lServo = ServoFactory.getInstance();
      this.mSlotIndexList = new int[lServo.getNumSlots()];
   }

   public void clear() {
      Servo lServo = ServoFactory.getInstance();
      this.mSlotIndexList = new int[lServo.getNumSlots()];
   }

   public void addBadSlot(int aSlot) {
      Servo lServo = ServoFactory.getInstance();
      if (aSlot >= 1 && aSlot <= lServo.getNumSlots()) {
         this.mSlotIndexList[aSlot - 1] = -1;
      } else {
         Aem.logDetailMessage(DvdplayLevel.FINE, "addBadSlot: invalid slot " + aSlot);
      }
   }

   public void removeBadSlot(int aSlot) {
      Servo lServo = ServoFactory.getInstance();
      if (aSlot >= 1 && aSlot <= lServo.getNumSlots()) {
         this.mSlotIndexList[aSlot - 1] = 0;
      }
   }

   public void addSlot(int aSlot, int aDiscDetailId) {
      this.removeDiscDetailId(aDiscDetailId);
      Servo lServo = ServoFactory.getInstance();
      if (aSlot >= 1 && aSlot <= lServo.getNumSlots()) {
         if (this.mSlotIndexList[aSlot - 1] == 0) {
            Aem.logDetailMessage(DvdplayLevel.FINE, "SlotIndex: adding DiscDetailId " + aDiscDetailId + " to slot " + aSlot);
            this.mSlotIndexList[aSlot - 1] = aDiscDetailId;
         }
      }
   }

   public int getSlot(int aSlot) {
      Servo lServo = ServoFactory.getInstance();
      return aSlot >= 1 && aSlot <= lServo.getNumSlots() ? this.mSlotIndexList[aSlot - 1] : 0;
   }

   public int removeSlot(int aSlot) {
      Servo lServo = ServoFactory.getInstance();
      if (aSlot >= 1 && aSlot <= lServo.getNumSlots()) {
         int lTmp = this.mSlotIndexList[aSlot - 1];
         this.mSlotIndexList[aSlot - 1] = 0;
         return lTmp;
      } else {
         return 0;
      }
   }

   public int removeDiscDetailId(int aDiscDetailId) {
      boolean lFound = false;
      Servo lServo = ServoFactory.getInstance();

      int i;
      for (i = 0; i < lServo.getNumSlots(); i++) {
         if (this.mSlotIndexList[i] == aDiscDetailId) {
            lFound = true;
            this.mSlotIndexList[i] = 0;
            break;
         }
      }

      if (lFound) {
         Aem.logDetailMessage(DvdplayLevel.FINE, "SlotIndex: removing DiscDetailId " + aDiscDetailId + " from slot " + (i + 1));
         return i + 1;
      } else {
         return -1;
      }
   }

   public int getDiscCount() {
      int counter = 0;
      Servo lServo = ServoFactory.getInstance();

      for (int i = 0; i < lServo.getNumSlots(); i++) {
         if (this.mSlotIndexList[i] != 0) {
            counter++;
         }
      }

      return counter;
   }
}
