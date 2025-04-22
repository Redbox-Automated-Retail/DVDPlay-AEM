package net.dvdplay.userop;

import net.dvdplay.aem.Aem;
import net.dvdplay.inventory.Disc;
import net.dvdplay.logger.DvdplayLevel;

public class DiscItem extends Disc {
   private int mOperationId;
   private String mDispensedDiscDetailId;

   public DiscItem(Disc aDisc, int aOperationId) {
      super(aDisc);
      this.mOperationId = aOperationId;
      this.mDispensedDiscDetailId = "";
   }

   public int getOperationId() {
      return this.mOperationId;
   }

   public void setDispensedDiscDetailId(String aDispensedDiscDetailId) {
      this.mDispensedDiscDetailId = aDispensedDiscDetailId;
   }

   public void setDispensedDiscDetailId(int aDispensedDiscDetailId) {
      if (aDispensedDiscDetailId <= 0) {
         this.mDispensedDiscDetailId = "";
      } else {
         try {
            this.mDispensedDiscDetailId = Integer.toString(aDispensedDiscDetailId);
         } catch (Exception var3) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage(), var3);
            this.mDispensedDiscDetailId = "";
         }
      }
   }

   public String getDispensedDiscDetailId() {
      return this.mDispensedDiscDetailId;
   }

   public Disc getDisc() {
      return this;
   }

   public boolean isDispensed() {
      return this.getDiscStatusId() != 3 || this.getDispensedDiscDetailId().trim().length() != 0;
   }
}
