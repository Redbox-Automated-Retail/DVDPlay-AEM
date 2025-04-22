package net.dvdplay.poll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class PollItem extends Poll {
   protected int mPollResponse;
   protected ArrayList mPollAnswers;
   protected Date mStartDate;
   protected Date mStopDate;
   protected int mPriority;

   public PollItem(int aPollID, int aPollTypeID, int aLocaleID, int aPriority, Date aStartDate, Date aStopDate) {
      super(aPollID, aPollTypeID, aLocaleID);
      this.mPriority = aPriority;
      this.mStartDate = aStartDate;
      this.mStopDate = aStopDate;
      this.mPollAnswers = new ArrayList();
   }

   public PollItem(PollItem aAemPollItem) {
      super(aAemPollItem.getPollID(), aAemPollItem.getPollTypeID(), aAemPollItem.getLocaleID());
      this.mPriority = aAemPollItem.getPriority();
      this.mStartDate = aAemPollItem.getStartDate();
      this.mStopDate = aAemPollItem.getStopDate();
      this.mPollAnswers = new ArrayList(aAemPollItem.getPollAnwsers());
   }

   public int getPriority() {
      return this.mPriority;
   }

   public Date getStartDate() {
      return this.mStartDate;
   }

   public Date getStopDate() {
      return this.mStopDate;
   }

   public int getPollResponse() {
      return this.mPollResponse;
   }

   public void setPollResponse(int aOrderNum) {
      int lResponse = new BigDecimal(Math.pow(2.0, aOrderNum)).intValue();
      this.mPollResponse = lResponse;
   }

   public ArrayList getPollAnwsers() {
      return this.mPollAnswers;
   }

   public void setAnswer(int aOrderNum, String aText) {
      boolean lAdded = false;

      for (int i = 0; i < this.mPollAnswers.size(); i++) {
         if (((PollAnswer)this.mPollAnswers.get(i)).getOrderNum() >= aOrderNum) {
            if (((PollAnswer)this.mPollAnswers.get(i)).getOrderNum() == aOrderNum) {
               this.mPollAnswers.remove(i);
               i--;
            } else if (((PollAnswer)this.mPollAnswers.get(i)).getOrderNum() > aOrderNum) {
               this.mPollAnswers.add(i, new PollAnswer(aOrderNum, aText));
               lAdded = true;
               break;
            }
         }
      }

      if (!lAdded) {
         this.mPollAnswers.add(new PollAnswer(aOrderNum, aText));
      }
   }

   public int compareTo(PollItem a) {
      if (this.mPriority < a.mPriority) {
         return -1;
      } else {
         return this.mPriority > a.mPriority ? 1 : 0;
      }
   }

   public void clearResponse() {
      this.mPollResponse = 0;
   }
}
