package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.exception.DvdplayException;

public class TitleTypeIndex {
   private static ArrayList mTitleTypeItemList;

   public TitleTypeIndex() {
      mTitleTypeItemList = new ArrayList();
   }

   public static void clear() {
      mTitleTypeItemList = new ArrayList();
   }

   public static int size() {
      return mTitleTypeItemList.size();
   }

   public static void addTitleTypeIndexItem(TitleTypeIndexItem aTitleTypeIndexItem) {
      for (int i = 0; i < mTitleTypeItemList.size(); i++) {
         TitleTypeIndexItem lTitleTypeIndexItem = (TitleTypeIndexItem)mTitleTypeItemList.get(i);
         if (aTitleTypeIndexItem.compareTo(lTitleTypeIndexItem) == -1) {
            mTitleTypeItemList.add(i, new TitleTypeIndexItem(aTitleTypeIndexItem));
            return;
         }
      }

      mTitleTypeItemList.add(new TitleTypeIndexItem(aTitleTypeIndexItem));
   }

   public static TitleTypeIndexItem getTitleTypeIndexItem(int aIndex) throws DvdplayException {
      if (aIndex >= 0 && aIndex <= mTitleTypeItemList.size()) {
         return (TitleTypeIndexItem)mTitleTypeItemList.get(aIndex);
      } else {
         throw new DvdplayException("getTitleTypeIndex: invalid index " + aIndex);
      }
   }
}
