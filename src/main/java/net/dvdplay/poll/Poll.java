package net.dvdplay.poll;

public abstract class Poll {
   protected int mPollID;
   protected int mOrderNum;
   protected int mPollTypeID;
   protected int mLocaleID;
   protected String mPollText;

   Poll(int aPollID, int aOrderNum, int aPollTypeID, int aLocaleID, String aPollText) {
      this.mPollID = aPollID;
      this.mOrderNum = aOrderNum;
      this.mPollTypeID = aPollTypeID;
      this.mLocaleID = aLocaleID;
      this.mPollText = aPollText;
   }

   Poll(int aPollID, int aPollTypeID, int aLocaleID) {
      this.mPollID = aPollID;
      this.mPollTypeID = aPollTypeID;
      this.mLocaleID = aLocaleID;
   }

   public int getPollID() {
      return this.mPollID;
   }

   public void setPollID(int aPollID) {
      this.mPollID = aPollID;
   }

   public int getOrderNum() {
      return this.mOrderNum;
   }

   public void setOrderNum(int aOrderNum) {
      this.mOrderNum = aOrderNum;
   }

   public int getPollTypeID() {
      return this.mPollTypeID;
   }

   public void setPollTypeID(int aPollTypeID) {
      this.mPollTypeID = aPollTypeID;
   }

   public int getLocaleID() {
      return this.mLocaleID;
   }

   public void setLocaleID(int aLocaleID) {
      this.mLocaleID = aLocaleID;
   }

   public String getPollText() {
      return this.mPollText;
   }

   public void setPollText(String aPollText) {
      this.mPollText = aPollText;
   }
}
