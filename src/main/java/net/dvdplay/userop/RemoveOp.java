package net.dvdplay.userop;

import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemException;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoException;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.dom.DOMData;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.task.QueueJob;

public class RemoveOp {
   private DiscRemove mDiscRemove = new DiscRemove();

   public int getDiscCount() {
      return this.mDiscRemove.getDiscItemListCount();
   }

   public void addDisc(int aDiscDetailId) throws RemoveOpException {
      try {
         this.mDiscRemove.addDiscItem(new DiscItem(Aem.getDisc(aDiscDetailId), 4));
      } catch (AemException var3) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "AemException caught: " + var3.getMessage());
         throw new RemoveOpException("Error creating Disc object");
      }
   }

   public void removeDisc(int aIndex) {
      this.mDiscRemove.removeDiscItem(aIndex);
   }

   public DiscItem getDiscItem(int aIndex) {
      return this.mDiscRemove.getDiscItem(aIndex);
   }

   public void dispenseDisc(int aIndex) throws RemoveOpException {
      int lSlot = this.mDiscRemove.getDiscItem(aIndex).getSlot();
      Servo lServo = ServoFactory.getInstance();

      try {
         if (lServo.goToSlot(lSlot) == 1) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "No Disc in slot " + lSlot + ".");
            lServo.setServoParameters();
            throw new RemoveOpException("Dispense disc failed");
         } else {
            lServo.ejectDisc(2);
            if (lServo.isDiscInSlot()) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Disc was not dispensed.");
               throw new RemoveOpException("Dispense disc failed");
            } else {
               Aem.logSummaryMessage(
                  "Disc removed: (" + this.mDiscRemove.getDiscItem(aIndex).getGroupCode() + "," + this.mDiscRemove.getDiscItem(aIndex).getDiscCode() + ")."
               );
               if (this.mDiscRemove.getDiscItem(aIndex).getDiscCode().trim().length() == 0) {
                  Aem.removeUnknownDisc(lSlot);
               } else {
                  Aem.removeDisc(
                     this.mDiscRemove.getDiscItem(aIndex).getDiscDetailId(),
                     this.mDiscRemove.getDiscItem(aIndex).getTitleDetailId(),
                     this.mDiscRemove.getDiscItem(aIndex).getDiscCode(),
                     this.mDiscRemove.getDiscItem(aIndex).getGroupCode(),
                     true
                  );
               }

               DOMData.save();
               lServo.setServoParameters();
            }
         }
      } catch (ServoException var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var5.getMessage());
         throw new RemoveOpException("Dispense disc failed");
      }
   }

   public synchronized void createQueueJob() {
      if (this.getDiscCount() > 0) {
         try {
            this.mDiscRemove.mNvPairSet = this.mDiscRemove.getDiscRemovedNvPairSet();
            Aem.mQueue.addQueueJob(new QueueJob(new DiscRemove(this.mDiscRemove)), true);
         } catch (RemoveOpException var2) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var2.getMessage());
         }

         this.mDiscRemove.clearDiscItems();
      }
   }
}
