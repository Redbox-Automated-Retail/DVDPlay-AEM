package net.dvdplay.poll;

import java.util.ArrayList;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class PollIndex {
   private ArrayList mPollItemList = new ArrayList();
   private ArrayList mActivePollList = new ArrayList();

   public void clear() {
      this.mPollItemList = new ArrayList();
   }

   public int size() {
      return this.mPollItemList.size();
   }

   public int addPollIndexItem(PollItem aPollItem) {
      for (int i = 0; i < this.mPollItemList.size(); i++) {
         PollItem lPollItem = (PollItem)this.mPollItemList.get(i);
         if (aPollItem.compareTo(lPollItem) == -1) {
            this.mPollItemList.add(i, new PollItem(aPollItem));
            return i;
         }
      }

      this.mPollItemList.add(new PollItem(aPollItem));
      return this.mPollItemList.size() - 1;
   }

   public PollItem getPollIndexItem(int aIndex) throws DvdplayException {
      if (aIndex >= 0 && aIndex <= this.mPollItemList.size()) {
         return (PollItem)this.mPollItemList.get(aIndex);
      } else {
         throw new DvdplayException("getPollIndexItem: invalid index " + aIndex);
      }
   }

   public static PollIndex createPollIndex() {
      String lCurrentField = "";
      PollIndex lPollIndex = new PollIndex();

      for (int i = 0; i < DOMData.mPollData.rowCount(); i++) {
         if (!DOMData.mPollData.isDeleted(i)) {
            try {
               lCurrentField = "PollId";
               String lPollId = DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("PollId"));
               lCurrentField = "PollTypeId";
               String lPollTypeId = DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("PollTypeId"));
               lCurrentField = "Priority";
               String lPriority = DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("Priority"));
               lCurrentField = "StartDate";
               Date lStartDate = Util.stringToDate(DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("StartDate")), "yyyy-MM-dd HH:mm:ss.SSS");
               lCurrentField = "EndDate";
               Date lStopDate = Util.stringToDate(DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("EndDate")), "yyyy-MM-dd HH:mm:ss.SSS");
               lCurrentField = "LocaleId";
               String lLocaleId = DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("LocaleId"));
               lCurrentField = "OrderNum";
               String lOrderNum = DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("OrderNum"));
               lCurrentField = "PollText";
               String lText = DOMData.mPollData.getFieldValue(i, DOMData.mPollData.getFieldIndex("PollText"));
               lCurrentField = "";
               int lIndex = -1;

               for (int j = 0; j < lPollIndex.size(); j++) {
                  if (lPollIndex.getPollIndexItem(j).getPollID() == Integer.parseInt(lPollId)
                     && lPollIndex.getPollIndexItem(j).getPollTypeID() == Integer.parseInt(lPollTypeId)
                     && lPollIndex.getPollIndexItem(j).getLocaleID() == Integer.parseInt(lLocaleId)) {
                     lIndex = j;
                     break;
                  }
               }

               if (lIndex < 0) {
                  lIndex = lPollIndex.addPollIndexItem(
                     new PollItem(
                        Integer.parseInt(lPollId),
                        Integer.parseInt(lPollTypeId),
                        Integer.parseInt(lLocaleId),
                        Integer.parseInt(lPriority),
                        lStartDate,
                        lStopDate
                     )
                  );
               }

               if (Integer.parseInt(lOrderNum) == 0) {
                  lPollIndex.getPollIndexItem(lIndex).setPollText(lText);
               } else {
                  lPollIndex.getPollIndexItem(lIndex).setAnswer(Integer.parseInt(lOrderNum), lText);
               }
            } catch (Exception var13) {
               if (!lCurrentField.equals("")) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "Error reading in field " + lCurrentField);
                  Aem.logSummaryMessage("Error reading in field " + lCurrentField);
               }

               Aem.logDetailMessage(DvdplayLevel.ERROR, "createPollIndex: " + var13.getMessage());
               Aem.logSummaryMessage("createPollIndex: " + var13.getMessage());
               Aem.setDataError();
            }
         }
      }

      return lPollIndex;
   }

   public int createPollList() {
      this.mActivePollList = new ArrayList();

      for (int i = 0; i < this.size(); i++) {
         try {
            PollItem lAemPollItem = this.getPollIndexItem(i);
            if (lAemPollItem.getLocaleID() == Aem.getLocaleId()
               && !Util.isDateAfterNow(lAemPollItem.getStartDate())
               && !Util.isDateBeforeNow(lAemPollItem.getStopDate())) {
               this.mActivePollList.add(lAemPollItem);
            }
         } catch (DvdplayException var4) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "createPollList: " + var4.getMessage());
            Aem.setDataError();
         }
      }

      return this.mActivePollList.size();
   }

   public PollItem getActivePollIndexItem(int aIndex) {
      return aIndex >= 0 && aIndex < this.mActivePollList.size() ? (PollItem)this.mActivePollList.get(aIndex) : null;
   }

   public void clearPollResponses() {
      for (int i = 0; i < this.size(); i++) {
         this.getPollIndexItem(i).clearResponse();
      }
   }
}
