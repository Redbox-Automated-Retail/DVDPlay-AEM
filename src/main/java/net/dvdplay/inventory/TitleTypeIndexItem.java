package net.dvdplay.inventory;

import java.math.BigDecimal;
import net.dvdplay.aem.Aem;

public class TitleTypeIndexItem {
   private int mTitleTypeId;
   private int mLocaleId;
   private String mTitleType;
   private String mTitleTypeSingular;
   private int mPriority;
   private int mFirstTimeMax;
   private int mRegularUserMax;
   private BigDecimal mMaxCharge;

   public BigDecimal getMaxCharge() {
      return this.mMaxCharge.setScale(Aem.getCurrencyFractionalDigits(), 4);
   }

   public int getFirstTimeMax() {
      return this.mFirstTimeMax;
   }

   public int getRegularUserMax() {
      return this.mRegularUserMax;
   }

   public int getTitleTypeId() {
      return this.mTitleTypeId;
   }

   public int getLocaleId() {
      return this.mLocaleId;
   }

   public String getTitleType() {
      return this.mTitleType;
   }

   public String getTitleTypeSingular() {
      return this.mTitleTypeSingular;
   }

   public int getPriority() {
      return this.mPriority;
   }

   public int compareTo(TitleTypeIndexItem a) {
      if (this.mPriority < a.mPriority) {
         return -1;
      } else {
         return this.mPriority > a.mPriority ? 1 : 0;
      }
   }

   public TitleTypeIndexItem(
      int aTitleTypeId,
      int aLocaleId,
      int aPriority,
      String aTitleType,
      String aTitleTypeSingular,
      int aFirstTimeMax,
      int aRegularUserMax,
      BigDecimal aMaxCharge
   ) {
      this.mTitleTypeId = aTitleTypeId;
      this.mLocaleId = aLocaleId;
      this.mTitleType = aTitleType;
      this.mTitleTypeSingular = aTitleTypeSingular;
      this.mPriority = aPriority;
      this.mFirstTimeMax = aFirstTimeMax;
      this.mRegularUserMax = aRegularUserMax;
      this.mMaxCharge = aMaxCharge;
   }

   public TitleTypeIndexItem(TitleTypeIndexItem a) {
      this.mTitleTypeId = a.mTitleTypeId;
      this.mLocaleId = a.mLocaleId;
      this.mTitleType = a.mTitleType;
      this.mTitleTypeSingular = a.mTitleTypeSingular;
      this.mPriority = a.mPriority;
      this.mFirstTimeMax = a.mFirstTimeMax;
      this.mRegularUserMax = a.mRegularUserMax;
      this.mMaxCharge = a.mMaxCharge;
   }
}
