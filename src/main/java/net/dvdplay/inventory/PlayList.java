package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.aem.Aem;

public class PlayList {
   private int mIndex = 0;
   private ArrayList mList = new ArrayList();

   public void addPlayListItem(PlayListItem aPlayListItem) {
      Aem.logSummaryMessage("adding " + aPlayListItem.getFilePath());

      for (int i = 0; i < this.mList.size(); i++) {
         PlayListItem lPlayListItem = (PlayListItem)this.mList.get(i);
         if (aPlayListItem.compareTo(lPlayListItem) == -1) {
            this.mList.add(i, new PlayListItem(aPlayListItem));
            return;
         }
      }

      this.mList.add(new PlayListItem(aPlayListItem));
   }

   public PlayListItem getNextPlayListItem() {
      if (this.mList.size() == 0) {
         return null;
      } else {
         PlayListItem litem = (PlayListItem)this.mList.get(this.mIndex++);
         if (this.mIndex == this.mList.size()) {
            this.mIndex = 0;
         }

         return litem;
      }
   }
}
