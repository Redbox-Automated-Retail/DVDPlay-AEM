package net.dvdplay.userop;

import java.util.ArrayList;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.inventory.Disc;
import net.dvdplay.logger.DvdplayLevel;

public class DiscAction {
   public Date mDTUpdated;
   public int mDiscItemListMaxCapacity;
   public NvPairSet mNvPairSet;
   protected int mQueueJobId;
   protected ArrayList mDiscItemList;

   public int getDiscItemListCount() {
      return this.mDiscItemList.size();
   }

   public void addDiscItem(DiscItem aDiscItem) throws DiscActionException {
      if (this.getDiscItemListCount() >= this.mDiscItemListMaxCapacity) {
         throw new DiscActionException("DiscItemList at maximum capacity: " + this.mDiscItemListMaxCapacity);
      } else {
         this.mDiscItemList.add(aDiscItem);
         this.mDTUpdated = new Date(System.currentTimeMillis());
      }
   }

   public DiscItem getDiscItem(int aIndex) throws DiscActionException {
      if (aIndex >= this.getDiscItemListCount()) {
         Aem.setScreenError();
         throw new DiscActionException("Disc item index " + aIndex + " out of range " + this.getDiscItemListCount());
      } else {
         return (DiscItem)this.mDiscItemList.get(aIndex);
      }
   }

   public DiscItem removeDiscItem(Disc aDiscItem) throws DiscActionException {
      if (this.getDiscItemListCount() <= 0) {
         throw new DiscActionException("DiscItemList is empty");
      } else {
         int lIndex;
         if ((lIndex = this.mDiscItemList.indexOf(aDiscItem)) == -1) {
            throw new DiscActionException("Disc (" + aDiscItem.getGroupCode() + "," + aDiscItem.getDiscCode() + ") not found in DiscItemList");
         } else {
            this.mDTUpdated = new Date(System.currentTimeMillis());
            return (DiscItem)this.mDiscItemList.remove(lIndex);
         }
      }
   }

   public DiscItem removeDiscItem(int aIndex) throws DiscActionException {
      if (this.getDiscItemListCount() <= 0) {
         throw new DiscActionException("DiscItemList is empty");
      } else if (aIndex >= this.getDiscItemListCount()) {
         Aem.setScreenError();
         throw new DiscActionException("Disc item index " + aIndex + " out of range " + this.getDiscItemListCount());
      } else {
         this.mDTUpdated = new Date(System.currentTimeMillis());
         return (DiscItem)this.mDiscItemList.remove(aIndex);
      }
   }

   public void clearDiscItems() {
      this.mDTUpdated = new Date(System.currentTimeMillis());
      this.mDiscItemList = new ArrayList();
      Aem.logDetailMessage(DvdplayLevel.INFO, "Disc items cleared");
   }

   public void queueExecute() {
   }

   public int getQueueJobId() {
      return this.mQueueJobId;
   }

   public DiscAction() {
      this.mDiscItemList = new ArrayList();
      this.mDTUpdated = new Date(System.currentTimeMillis());
      Servo lServo = ServoFactory.getInstance();
      this.mDiscItemListMaxCapacity = lServo.getNumSlots();
      this.mQueueJobId = 0;
   }

   public DiscAction(DiscAction aDiscAction) {
      this.mDiscItemListMaxCapacity = aDiscAction.mDiscItemListMaxCapacity;
      this.mDTUpdated = new Date(System.currentTimeMillis());
      this.mDiscItemList = new ArrayList(aDiscAction.mDiscItemList);
      this.mNvPairSet = aDiscAction.mNvPairSet;
      this.mQueueJobId = 0;
   }

   public DiscAction(NvPairSet aNvPairSet) {
      this();
      this.mNvPairSet = aNvPairSet;
   }
}
