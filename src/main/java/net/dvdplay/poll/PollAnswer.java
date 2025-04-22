package net.dvdplay.poll;

public class PollAnswer {
   private int mOrderNum;
   private String mText;

   public PollAnswer(int aOrderNum, String aText) {
      this.mOrderNum = aOrderNum;
      this.mText = aText;
   }

   public int getOrderNum() {
      return this.mOrderNum;
   }

   public String getText() {
      return this.mText;
   }
}
