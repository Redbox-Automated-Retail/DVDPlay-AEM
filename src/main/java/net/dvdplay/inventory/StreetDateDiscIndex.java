package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.aem.Aem;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class StreetDateDiscIndex {
   private static ArrayList mDiscIndexList;

   public StreetDateDiscIndex() {
      mDiscIndexList = new ArrayList();
   }

   public static void clear() {
      mDiscIndexList = new ArrayList();
   }

   public static void removeDiscIndexItem(int aDiscDetailId) {
      StreetDateDiscIndexItem tmpDiscIndexItem = new StreetDateDiscIndexItem();
      tmpDiscIndexItem.setDiscDetailId(aDiscDetailId);
      removeDiscIndexItem(tmpDiscIndexItem);
   }

   public static void removeDiscIndexItem(StreetDateDiscIndexItem aDiscIndexItem) {
      for (int i = 0; i < mDiscIndexList.size(); i++) {
         StreetDateDiscIndexItem lDiscIndexItem = (StreetDateDiscIndexItem)mDiscIndexList.get(i);
         if (aDiscIndexItem.equals(lDiscIndexItem)) {
            mDiscIndexList.remove(i);
            Aem.logDetailMessage(DvdplayLevel.FINE, "removing from StreetDateDiscIndexItem");
            return;
         }
      }
   }

   public static void addDiscIndexItem(StreetDateDiscIndexItem aDiscIndexItem) {
      removeDiscIndexItem(aDiscIndexItem);

      for (int i = 0; i < mDiscIndexList.size(); i++) {
         StreetDateDiscIndexItem lDiscIndexItem = (StreetDateDiscIndexItem)mDiscIndexList.get(i);
         if (aDiscIndexItem.compareTo(lDiscIndexItem) == -1) {
            mDiscIndexList.add(i, new StreetDateDiscIndexItem(aDiscIndexItem));
            return;
         }
      }

      mDiscIndexList.add(new StreetDateDiscIndexItem(aDiscIndexItem));
   }

   public static StreetDateDiscIndexItem getDiscIndexItem(int aIndex) {
      return aIndex >= 0 && aIndex <= mDiscIndexList.size() ? (StreetDateDiscIndexItem)mDiscIndexList.get(aIndex) : null;
   }

   public static StreetDateDiscIndexItem getDiscIndexItemByTitleDetailId(int aTitleDetailId) throws DvdplayException {
      for (int i = 0; i < mDiscIndexList.size(); i++) {
         StreetDateDiscIndexItem a = (StreetDateDiscIndexItem)mDiscIndexList.get(i);
         if (a.getTitleDetailId() == aTitleDetailId && a.getDiscStatusId() == 3) {
            return a;
         }
      }

      for (int ix = 0; ix < mDiscIndexList.size(); ix++) {
         StreetDateDiscIndexItem var4 = (StreetDateDiscIndexItem)mDiscIndexList.get(ix);
         if (var4.getTitleDetailId() == aTitleDetailId && var4.getDiscStatusId() == 1) {
            return var4;
         }
      }

      throw new DvdplayException("Could not find TitleDetailId " + aTitleDetailId + " in StreetDateDiscIndexItem");
   }

   public static int size() {
      return mDiscIndexList.size();
   }

   public static void a() {
      System.out.println("New Releases");

      for (int i = 0; i < mDiscIndexList.size(); i++) {
         StreetDateDiscIndexItem b = (StreetDateDiscIndexItem)mDiscIndexList.get(i);
         System.out.print(b.getPriority() + ", ");

         try {
            System.out.print(Util.dateToString(b.getStreetDate()) + ", ");
         } catch (Exception var3) {
         }

         System.out.println(b.getSortTitle());
      }
   }
}
