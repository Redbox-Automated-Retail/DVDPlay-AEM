package net.dvdplay.inventory;

import net.dvdplay.aem.Aem;

public class PlayListItem {
   private int mPriority;
   private String mFilePath;
   private boolean mIsRentable;
   private int mMediaId;
   private int mButtonMode;

   public int getMediaId() {
      return this.mMediaId;
   }

   public int getButtonMode() {
      if (this.mButtonMode != 1) {
         return this.mButtonMode;
      } else {
         try {
            Aem.getDiscIndex();
            DiscIndexItem lDiscIndexItem = DiscIndex.getDiscIndexItemByTitleDetailId(this.mMediaId);
            return lDiscIndexItem.getDiscStatusId() == 3
                  && (lDiscIndexItem.isMarkedForRent() || lDiscIndexItem.isMarkedForSale())
                  && !lDiscIndexItem.isMarkedForRemoval()
               ? 1
               : 0;
         } catch (Exception var2) {
            return 0;
         }
      }
   }

   public boolean isRentable() {
      if (this.mIsRentable) {
         try {
            Aem.getDiscIndex();
            DiscIndex.getDiscIndexItemByTitleDetailId(this.mMediaId);
            return true;
         } catch (Exception var2) {
            return false;
         }
      } else {
         return false;
      }
   }

   public int getPriority() {
      return this.mPriority;
   }

   public String getFilePath() {
      return this.mFilePath;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof PlayListItem)) {
         return false;
      } else {
         PlayListItem lPlayListItem = (PlayListItem)o;
         return this.mPriority != lPlayListItem.mPriority ? false : this.mFilePath.equals(lPlayListItem.mFilePath);
      }
   }

   public int hashCode() {
      int result = this.mPriority;
      return 29 * result + this.mFilePath.hashCode();
   }

   public int compareTo(PlayListItem a) {
      if (this.mPriority < a.mPriority) {
         return -1;
      } else if (this.mPriority > a.mPriority) {
         return 1;
      } else if (this.mFilePath.compareTo(a.mFilePath) < 0) {
         return -1;
      } else {
         return this.mFilePath.compareTo(a.mFilePath) > 0 ? 1 : 0;
      }
   }

   public PlayListItem() {
      this.mPriority = 0;
      this.mFilePath = "";
      this.mIsRentable = false;
      this.mMediaId = 0;
   }

   public PlayListItem(PlayListItem a) {
      this.mPriority = a.mPriority;
      this.mFilePath = a.mFilePath;
      this.mIsRentable = a.mIsRentable;
      this.mMediaId = a.mMediaId;
      this.mButtonMode = a.mButtonMode;
   }

   public PlayListItem(int aPriority, String aMediaFilePath) {
      this.mPriority = aPriority;
      this.mFilePath = aMediaFilePath;
      this.mIsRentable = false;
      this.mMediaId = 0;
      this.mButtonMode = 0;
   }

   public PlayListItem(int aPriority, String aMediaFilePath, boolean aIsRentable, int aButtonMode, int aMediaId) {
      this.mPriority = aPriority;
      this.mFilePath = aMediaFilePath;
      this.mIsRentable = aIsRentable;
      this.mMediaId = aMediaId;
      this.mButtonMode = aButtonMode;
   }
}
