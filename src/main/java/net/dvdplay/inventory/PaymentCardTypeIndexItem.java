package net.dvdplay.inventory;

public class PaymentCardTypeIndexItem {
   private int mPaymentCardTypeId;
   private int mVerificationTypeId;
   private String mPaymentPicture;
   private int mPriority;
   private String mPaymentCardTypeName;
   private int mLocaleId;
   private int mPaymentCardCategoryId;

   public int getPaymentCardCategoryId() {
      return this.mPaymentCardCategoryId;
   }

   public int getPaymentCardTypeId() {
      return this.mPaymentCardTypeId;
   }

   public int getVerificationTypeId() {
      return this.mVerificationTypeId;
   }

   public String getPaymentPicture() {
      return this.mPaymentPicture;
   }

   public int getPriority() {
      return this.mPriority;
   }

   public String getPaymentCardTypeName() {
      return this.mPaymentCardTypeName;
   }

   public int getLocaleId() {
      return this.mLocaleId;
   }

   public int compareTo(PaymentCardTypeIndexItem a) {
      if (this.mPriority < a.mPriority) {
         return -1;
      } else {
         return this.mPriority > a.mPriority ? 1 : 0;
      }
   }

   public PaymentCardTypeIndexItem(
      int aPaymentCardTypeId,
      int aVerificationTypeId,
      int aPriority,
      String aPaymentPicture,
      String aPaymentCardTypeName,
      int aLocaleId,
      int aPaymentCardCategoryId
   ) {
      this.mPaymentCardTypeId = aPaymentCardTypeId;
      this.mVerificationTypeId = aVerificationTypeId;
      this.mPaymentPicture = aPaymentPicture;
      this.mPriority = aPriority;
      this.mPaymentCardTypeName = aPaymentCardTypeName;
      this.mLocaleId = aLocaleId;
      this.mPaymentCardCategoryId = aPaymentCardCategoryId;
   }

   public PaymentCardTypeIndexItem(PaymentCardTypeIndexItem a) {
      this.mPaymentCardTypeId = a.mPaymentCardTypeId;
      this.mVerificationTypeId = a.mVerificationTypeId;
      this.mPaymentPicture = a.mPaymentPicture;
      this.mPriority = a.mPriority;
      this.mPaymentCardTypeName = a.mPaymentCardTypeName;
      this.mLocaleId = a.mLocaleId;
      this.mPaymentCardCategoryId = a.mPaymentCardCategoryId;
   }
}
