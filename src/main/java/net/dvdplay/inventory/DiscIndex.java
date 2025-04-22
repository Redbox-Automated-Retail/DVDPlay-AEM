package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.aem.Aem;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class DiscIndex {
   private static ArrayList mDiscIndexList;

   public DiscIndex() {
      mDiscIndexList = new ArrayList();
   }

   public static void clear() {
      mDiscIndexList = new ArrayList();
   }

   public static void removeDiscIndexItem(int aDiscDetailId) {
      DiscIndexItem tmpDiscIndexItem = new DiscIndexItem();
      tmpDiscIndexItem.setDiscDetailId(aDiscDetailId);
      removeDiscIndexItem(tmpDiscIndexItem);
   }

   public static void removeDiscIndexItem(DiscIndexItem aDiscIndexItem) {
      for (int i = 0; i < mDiscIndexList.size(); i++) {
         DiscIndexItem lDiscIndexItem = (DiscIndexItem)mDiscIndexList.get(i);
         if (aDiscIndexItem.equals(lDiscIndexItem)) {
            mDiscIndexList.remove(i);
            Aem.logDetailMessage(DvdplayLevel.FINE, "removing from discindex");
            return;
         }
      }
   }

   public static void addDiscIndexItem(DiscIndexItem aDiscIndexItem) {
      removeDiscIndexItem(aDiscIndexItem);

      for (int i = 0; i < mDiscIndexList.size(); i++) {
         DiscIndexItem lDiscIndexItem = (DiscIndexItem)mDiscIndexList.get(i);
         if (aDiscIndexItem.compareTo(lDiscIndexItem) == -1) {
            mDiscIndexList.add(i, new DiscIndexItem(aDiscIndexItem));
            return;
         }
      }

      mDiscIndexList.add(new DiscIndexItem(aDiscIndexItem));
   }

   public static DiscIndexItem getDiscIndexItem(int aIndex) {
      return aIndex >= 0 && aIndex <= mDiscIndexList.size() ? (DiscIndexItem)mDiscIndexList.get(aIndex) : null;
   }

   public static ArrayList getDiscIndexItemListByTitleDetailId(int aTitleDetailId) {
      ArrayList lList = new ArrayList();
      boolean lAdded = false;

      for (int i = 0; i < mDiscIndexList.size(); i++) {
         DiscIndexItem a = (DiscIndexItem)mDiscIndexList.get(i);
         lAdded = false;
         if (a.getTitleDetailId() == aTitleDetailId && (a.isMarkedForRent() || a.isMarkedForSale()) && !a.isMarkedForRemoval() && a.getDiscStatusId() == 3) {
            for (int j = 0; j < lList.size(); j++) {
               if (((DiscIndexItem)lList.get(j)).getDispenseFailureCount() > a.getDispenseFailureCount()) {
                  lList.add(j, a);
                  lAdded = true;
                  break;
               }
            }

            if (!lAdded) {
               lList.add(a);
            }
         }
      }

      return lList;
   }

   public static DiscIndexItem getDiscIndexItemByTitleDetailId(int aTitleDetailId) throws DvdplayException {
      ArrayList aList = getDiscIndexItemListByTitleDetailId(aTitleDetailId);
      if (aList.size() == 0) {
         throw new DvdplayException("Could not find an available disc of TitleDetailId " + aTitleDetailId + " in DiscIndex");
      } else {
         return (DiscIndexItem)aList.get(0);
      }
   }

   public static int size() {
      return mDiscIndexList.size();
   }

   public static void incDispenseFailureCount(int aDiscDetailId) {
      for (int i = 0; i < mDiscIndexList.size(); i++) {
         DiscIndexItem a = (DiscIndexItem)mDiscIndexList.get(i);
         if (a.getDiscDetailId() == aDiscDetailId) {
            a.incDispenseFailureCount();
         }
      }
   }

   public static void a() {
      System.out.println("Top Picks");

      for (int i = 0; i < mDiscIndexList.size(); i++) {
         DiscIndexItem b = (DiscIndexItem)mDiscIndexList.get(i);
         System.out.print(b.getPriority() + ", ");

         try {
            System.out.print(Util.dateToString(b.getStreetDate()) + ", ");
         } catch (Exception var3) {
         }

         System.out.println(b.getSortTitle());
      }
   }
}
