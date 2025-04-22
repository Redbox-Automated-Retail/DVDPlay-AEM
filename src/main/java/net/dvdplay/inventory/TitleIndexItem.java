package net.dvdplay.inventory;

public class TitleIndexItem {
   private int mTitleDetailId;
   private int mCount;

   public TitleIndexItem(int aTitleDetailId) {
      this.mTitleDetailId = aTitleDetailId;
      this.mCount = 1;
   }

   public int getTitleDetailId() {
      return this.mTitleDetailId;
   }

   public int getCount() {
      return this.mCount;
   }

   public void addCount() {
      this.mCount++;
   }

   public void decrementCount() {
      this.mCount--;
      if (this.mCount < 0) {
         this.mCount = 0;
      }
   }
}
