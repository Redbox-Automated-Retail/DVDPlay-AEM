package net.dvdplay.inventory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.util.Util;

public class PricingItem {
   private int mPriceOptionId = 0;
   private int mPriceModelId = 0;
   private BigDecimal mNewPrice = new BigDecimal(0.0);
   private BigDecimal mUsedPrice;
   private BigDecimal mRentalPrice;
   private BigDecimal mLateRentalPrice;
   private int mRentalDays;
   private int mLateDays;
   private boolean mIsSpecial;

   private Date checkTransactionTime(Date aDate) {
      return aDate == null ? new Date() : aDate;
   }

   public String getDueDateShort(Date aTransactionTime) {
      Date lTransactionTime = this.checkTransactionTime(aTransactionTime);
      SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)DateFormat.getDateInstance(1, Aem.getLocale());
      Calendar lCal = Calendar.getInstance(Aem.getLocale());
      lCal.setTime(this.getDueDate(lTransactionTime));
      return lSimpleDateFormat.format(lCal.getTime());
   }

   public Date getDueDate(Date aTransactionTime) {
      Date lTransactionTime = this.checkTransactionTime(aTransactionTime);
      String mDateFormatString = "yyyy-MM-dd";
      String mTimeFormatString = "HH:mm:ss.SSS";
      String mTimeZoneFormatString = "z";
      Calendar lCal = Calendar.getInstance(Aem.getLocale());
      lCal.setTime(lTransactionTime);
      lCal.add(5, this.mRentalDays);
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(mDateFormatString);
      String ltmp = lSimpleDateFormat.format(lCal.getTime());
      lSimpleDateFormat = new SimpleDateFormat(mTimeFormatString);
      ltmp = ltmp + " ";
      ltmp = ltmp + lSimpleDateFormat.format(Aem.getDueTime());
      lSimpleDateFormat = new SimpleDateFormat(mTimeZoneFormatString);
      ltmp = ltmp + " ";
      ltmp = ltmp + lSimpleDateFormat.format(lCal.getTime());
      return Util.stringToDate(ltmp);
   }

   public String getDueDateLong(Date aTransactionTime) {
      Date lTransactionTime = this.checkTransactionTime(aTransactionTime);
      SimpleDateFormat lSimpleDateFormat = (SimpleDateFormat)DateFormat.getDateTimeInstance(0, 3, Aem.getLocale());
      Calendar lCal = Calendar.getInstance(Aem.getLocale());
      lCal.setTime(this.getDueDate(lTransactionTime));
      return lSimpleDateFormat.format(lCal.getTime());
   }

   public int getPriceOptionId() {
      return this.mPriceOptionId;
   }

   public void setPriceOptionId(int aPriceOptionId) {
      this.mPriceOptionId = aPriceOptionId;
   }

   public int getPriceModelId() {
      return this.mPriceModelId;
   }

   public void setPriceModelId(int aPriceModelId) {
      this.mPriceModelId = aPriceModelId;
   }

   public BigDecimal getNewPrice() {
      return this.mNewPrice;
   }

   public void setNewPrice(BigDecimal aNewPrice) {
      this.mNewPrice = aNewPrice;
   }

   public BigDecimal getUsedPrice() {
      return this.mUsedPrice;
   }

   public void setUsedPrice(BigDecimal aUsedPrice) {
      this.mUsedPrice = aUsedPrice;
   }

   public BigDecimal getRentalPrice() {
      return this.mRentalPrice;
   }

   public void setRentalPrice(BigDecimal aRentalPrice) {
      this.mRentalPrice = aRentalPrice;
   }

   public BigDecimal getLateRentalPrice() {
      return this.mLateRentalPrice;
   }

   public void setLateRentalPrice(BigDecimal aLateRentalPrice) {
      this.mLateRentalPrice = aLateRentalPrice;
   }

   public int getRentalDays() {
      return this.mRentalDays;
   }

   public void setRentalDays(int aRentalDays) {
      this.mRentalDays = aRentalDays;
   }

   public int getLateDays() {
      return this.mLateDays;
   }

   public void setLateDays(int aLateDays) {
      this.mLateDays = aLateDays;
   }

   public boolean isSpecialPricing() {
      return this.mIsSpecial;
   }

   public void setSpecialPricing() {
      this.mIsSpecial = true;
   }

   public PricingItem() {
      this.mNewPrice = this.mNewPrice.setScale(Aem.getCurrencyFractionalDigits(), 4);
      this.mUsedPrice = new BigDecimal(0.0);
      this.mUsedPrice = this.mUsedPrice.setScale(Aem.getCurrencyFractionalDigits(), 4);
      this.mRentalPrice = new BigDecimal(0.0);
      this.mRentalPrice = this.mRentalPrice.setScale(Aem.getCurrencyFractionalDigits(), 4);
      this.mLateRentalPrice = new BigDecimal(0.0);
      this.mLateRentalPrice = this.mLateRentalPrice.setScale(Aem.getCurrencyFractionalDigits(), 4);
      this.mRentalDays = 0;
      this.mLateDays = 0;
      this.mIsSpecial = false;
   }
}
