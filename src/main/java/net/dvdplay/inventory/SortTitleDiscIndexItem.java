package net.dvdplay.inventory;

public class SortTitleDiscIndexItem extends DiscIndexItem {
   public SortTitleDiscIndexItem() {
   }

   public int compareTo(DiscIndexItem a) {
      if (this.mSortTitle.compareTo(a.mSortTitle) < 0) {
         return -1;
      } else {
         return this.mSortTitle.compareTo(a.mSortTitle) > 0 ? 1 : 0;
      }
   }

   public SortTitleDiscIndexItem(DiscIndexItem a) {
      super(a);
   }

   public SortTitleDiscIndexItem(SortTitleDiscIndexItem a) {
      super(a);
   }
}
