package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.exception.DvdplayException;

public class LocaleIndex {
   private static ArrayList mLocaleList;

   public static void addLocaleIndexItem(LocaleIndexItem a) {
      mLocaleList.add(new LocaleIndexItem(a));
   }

   public static LocaleIndexItem getLocaleIndexItem(int aIndex) {
      return aIndex >= 0 && aIndex < mLocaleList.size() ? (LocaleIndexItem)mLocaleList.get(aIndex) : null;
   }

   public static LocaleIndexItem getLocaleIndexItemByLocaleId(int aLocaleId) {
      for (int i = 0; i < mLocaleList.size(); i++) {
         if (getLocaleIndexItem(i).getLocaleId() == aLocaleId) {
            return getLocaleIndexItem(i);
         }
      }

      throw new DvdplayException("LocaleId " + aLocaleId + " not in LocaleIndex");
   }

   public static int size() {
      return mLocaleList.size();
   }

   public LocaleIndex() {
      mLocaleList = new ArrayList();
   }
}
