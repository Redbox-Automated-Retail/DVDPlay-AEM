package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.aem.Aem;
import net.dvdplay.dom.DOMData;
import net.dvdplay.logger.DvdplayLevel;

public class TitleIndex {
   private static ArrayList mTitleIndexList;

   public TitleIndex() {
      mTitleIndexList = new ArrayList();
   }

   public void clear() {
      mTitleIndexList = new ArrayList();
   }

   public void addTitle(int aTitleDetailId) {
      for (int i = 0; i < mTitleIndexList.size(); i++) {
         if (((TitleIndexItem)mTitleIndexList.get(i)).getTitleDetailId() == aTitleDetailId) {
            ((TitleIndexItem)mTitleIndexList.get(i)).addCount();
            Aem.logDetailMessage(DvdplayLevel.FINE, "incrementing TitleDetailId " + aTitleDetailId + " in titleindex");
            return;
         }
      }

      Aem.logDetailMessage(DvdplayLevel.FINE, "adding TitleDetailId " + aTitleDetailId + " to titleindex");
      mTitleIndexList.add(new TitleIndexItem(aTitleDetailId));
   }

   public void decrementTitle(int aTitleDetailId) {
      for (int i = 0; i < mTitleIndexList.size(); i++) {
         TitleIndexItem lTitleIndexItem = (TitleIndexItem)mTitleIndexList.get(i);
         if (lTitleIndexItem.getTitleDetailId() == aTitleDetailId) {
            Aem.logDetailMessage(DvdplayLevel.FINE, "decrementing TitleDetailId " + aTitleDetailId + " in titleindex");
            lTitleIndexItem.decrementCount();
            if (lTitleIndexItem.getCount() == 0) {
               Aem.logDetailMessage(DvdplayLevel.FINE, "removing TitleDetailId " + aTitleDetailId + " from titleindex");
               mTitleIndexList.remove(i);
               Aem.removeTitle(aTitleDetailId, true);
               DOMData.save();
            }

            return;
         }
      }

      Aem.logDetailMessage(DvdplayLevel.FINE, "Could not find TitleDetailId " + aTitleDetailId + " in titleindex");
   }

   public void a() {
      for (int i = 0; i < mTitleIndexList.size(); i++) {
         System.out.println(((TitleIndexItem)mTitleIndexList.get(i)).getTitleDetailId() + ", " + ((TitleIndexItem)mTitleIndexList.get(i)).getCount());
      }
   }
}
