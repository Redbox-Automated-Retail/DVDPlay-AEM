package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.aem.Aem;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class SortTitleDiscIndex {
   private static ArrayList mDiscIndexList;

   public SortTitleDiscIndex() {
      mDiscIndexList = new ArrayList();
   }

   public static void clear() {
      mDiscIndexList = new ArrayList();
   }

   public static void removeDiscIndexItem(int aDiscDetailId) {
      SortTitleDiscIndexItem tmpDiscIndexItem = new SortTitleDiscIndexItem();
      tmpDiscIndexItem.setDiscDetailId(aDiscDetailId);
      removeDiscIndexItem(tmpDiscIndexItem);
   }

   public static void removeDiscIndexItem(SortTitleDiscIndexItem aDiscIndexItem) {
      for (int i = 0; i < mDiscIndexList.size(); i++) {
         SortTitleDiscIndexItem lDiscIndexItem = (SortTitleDiscIndexItem)mDiscIndexList.get(i);
         if (aDiscIndexItem.equals(lDiscIndexItem)) {
            mDiscIndexList.remove(i);
            Aem.logDetailMessage(DvdplayLevel.FINE, "removing from SortTitleDiscIndexItem");
            return;
         }
      }
   }

   public static void addDiscIndexItem(SortTitleDiscIndexItem aDiscIndexItem) {
      removeDiscIndexItem(aDiscIndexItem);

      for (int i = 0; i < mDiscIndexList.size(); i++) {
         SortTitleDiscIndexItem lDiscIndexItem = (SortTitleDiscIndexItem)mDiscIndexList.get(i);
         if (aDiscIndexItem.compareTo(lDiscIndexItem) == -1) {
            mDiscIndexList.add(i, new SortTitleDiscIndexItem(aDiscIndexItem));
            return;
         }
      }

      mDiscIndexList.add(new SortTitleDiscIndexItem(aDiscIndexItem));
   }

   public static SortTitleDiscIndexItem getDiscIndexItem(int aIndex) {
      return aIndex >= 0 && aIndex <= mDiscIndexList.size() ? (SortTitleDiscIndexItem)mDiscIndexList.get(aIndex) : null;
   }

   public static SortTitleDiscIndexItem getDiscIndexItemByTitleDetailId(int aTitleDetailId) throws DvdplayException {
      for (int i = 0; i < mDiscIndexList.size(); i++) {
         SortTitleDiscIndexItem a = (SortTitleDiscIndexItem)mDiscIndexList.get(i);
         if (a.getTitleDetailId() == aTitleDetailId && a.getDiscStatusId() == 3) {
            return a;
         }
      }

      for (int ix = 0; ix < mDiscIndexList.size(); ix++) {
         SortTitleDiscIndexItem var4 = (SortTitleDiscIndexItem)mDiscIndexList.get(ix);
         if (var4.getTitleDetailId() == aTitleDetailId && var4.getDiscStatusId() == 1) {
            return var4;
         }
      }

      throw new DvdplayException("Could not find TitleDetailId " + aTitleDetailId + " in SortTitleDiscIndexItem");
   }

   public static int size() {
      return mDiscIndexList.size();
   }

   public static void a() {
      System.out.println("View All");

      for (int i = 0; i < mDiscIndexList.size(); i++) {
         SortTitleDiscIndexItem b = (SortTitleDiscIndexItem)mDiscIndexList.get(i);
         System.out.print(b.getPriority() + ", ");

         try {
            System.out.print(Util.dateToString(b.getStreetDate()) + ", ");
         } catch (Exception var3) {
         }

         System.out.println(b.getSortTitle());
      }
   }
}
