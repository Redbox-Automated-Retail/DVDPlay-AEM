package net.dvdplay.inventory;

public class StreetDateDiscIndexItem extends DiscIndexItem {
   public StreetDateDiscIndexItem() {
   }

   public int compareTo(DiscIndexItem a) {
      if (this.mStreetDate != null) {
         if (a.mStreetDate == null) {
            return 1;
         }

         if (this.mStreetDate.compareTo(a.mStreetDate) < 0) {
            return 1;
         }

         if (this.mStreetDate.compareTo(a.mStreetDate) > 0) {
            return -1;
         }
      }

      if (this.mSortTitle.compareTo(a.mSortTitle) < 0) {
         return -1;
      } else {
         return this.mSortTitle.compareTo(a.mSortTitle) > 0 ? 1 : 0;
      }
   }

   public StreetDateDiscIndexItem(DiscIndexItem a) {
      super(a);
   }

   public StreetDateDiscIndexItem(StreetDateDiscIndexItem a) {
      super(a);
   }
}
