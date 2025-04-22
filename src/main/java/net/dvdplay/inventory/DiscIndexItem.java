package net.dvdplay.inventory;

import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class DiscIndexItem {
   protected int mDiscDetailId;
   protected int mPriority;
   protected Date mStreetDate;
   protected String mSortTitle;
   protected String mShortTitle;
   protected String mOriginalTitle;
   protected String mTranslatedTitle;
   protected int mTitleDetailId;
   protected int mGenre1Id;
   protected int mGenre2Id;
   protected int mGenre3Id;
   protected boolean mMarkedForRent;
   protected boolean mMarkedForSale;
   protected boolean mMarkedForRemoval;
   protected Date mMarkedForRemovalDate;
   protected String mPoster;
   protected int mDiscStatusId;
   protected int mPriceOptionId;
   protected String mDescription;
   protected int mTitleTypeId;
   protected int mSlot;
   protected String mDiscCode;
   protected String mGroupCode;
   protected Date mDTUpdated;
   protected String mAttr1;
   protected String mAttr2;
   protected String mAttr3;
   protected String mAttr4;
   protected String mAttr5;
   protected String mAttr6;
   protected int mRating1Id;
   protected int mRating2Id;
   protected int mRating3Id;
   protected int mRatingSystem1Id;
   protected int mRatingSystem2Id;
   protected int mRatingSystem3Id;
   protected String mReleaseYear;
   protected int mDispenseFailureCount;

   public DiscIndexItem() {
      this.mDiscDetailId = 0;
      this.mPriority = 0;
      this.mStreetDate = null;
      this.mSortTitle = "";
      this.mShortTitle = "";
      this.mOriginalTitle = "";
      this.mTranslatedTitle = "";
      this.mTitleDetailId = 0;
      this.mGenre1Id = 0;
      this.mGenre2Id = 0;
      this.mGenre3Id = 0;
      this.mMarkedForRent = false;
      this.mMarkedForSale = false;
      this.mMarkedForRemoval = false;
      this.mMarkedForRemovalDate = null;
      this.mPoster = "";
      this.mDiscStatusId = 0;
      this.mPriceOptionId = 0;
      this.mDescription = "";
      this.mTitleTypeId = 0;
      this.mSlot = 0;
      this.mDiscCode = "";
      this.mGroupCode = "";
      this.mAttr1 = "";
      this.mAttr2 = "";
      this.mAttr3 = "";
      this.mAttr4 = "";
      this.mAttr5 = "";
      this.mAttr6 = "";
      this.mRating1Id = 0;
      this.mRating2Id = 0;
      this.mRating3Id = 0;
      this.mRatingSystem1Id = 0;
      this.mRatingSystem2Id = 0;
      this.mRatingSystem3Id = 0;
      this.mDTUpdated = null;
      this.mReleaseYear = "";
      this.mDispenseFailureCount = 0;
   }

   public DiscIndexItem(DiscIndexItem a) {
      this.mDiscDetailId = a.mDiscDetailId;
      this.mPriority = a.mPriority;
      this.mStreetDate = a.mStreetDate;
      this.mSortTitle = new String(a.mSortTitle);
      this.mShortTitle = new String(a.mShortTitle);
      this.mOriginalTitle = new String(a.mOriginalTitle);
      this.mTranslatedTitle = new String(a.mTranslatedTitle);
      this.mTitleDetailId = a.mTitleDetailId;
      this.mGenre1Id = a.mGenre1Id;
      this.mGenre2Id = a.mGenre2Id;
      this.mGenre3Id = a.mGenre3Id;
      this.mMarkedForRent = a.mMarkedForRent;
      this.mMarkedForSale = a.mMarkedForSale;
      this.mMarkedForRemoval = a.mMarkedForRemoval;
      this.mMarkedForRemovalDate = a.mMarkedForRemovalDate;
      this.mPoster = a.mPoster;
      this.mDiscStatusId = a.mDiscStatusId;
      this.mPriceOptionId = a.mPriceOptionId;
      this.mDescription = a.mDescription;
      this.mTitleTypeId = a.mTitleTypeId;
      this.mSlot = a.mSlot;
      this.mDiscCode = a.mDiscCode;
      this.mGroupCode = a.mGroupCode;
      this.mAttr1 = a.mAttr1;
      this.mAttr2 = a.mAttr2;
      this.mAttr3 = a.mAttr3;
      this.mAttr4 = a.mAttr4;
      this.mAttr5 = a.mAttr5;
      this.mAttr6 = a.mAttr6;
      this.mRating1Id = a.mRating1Id;
      this.mRating2Id = a.mRating2Id;
      this.mRating3Id = a.mRating3Id;
      this.mRatingSystem1Id = a.mRatingSystem1Id;
      this.mRatingSystem2Id = a.mRatingSystem2Id;
      this.mRatingSystem3Id = a.mRatingSystem3Id;
      this.mDTUpdated = a.mDTUpdated;
      this.mReleaseYear = a.mReleaseYear;
      this.mDispenseFailureCount = a.mDispenseFailureCount;
   }

   public int getDiscDetailId() {
      return this.mDiscDetailId;
   }

   public void setDiscDetailId(int aDiscDetailId) {
      this.mDiscDetailId = aDiscDetailId;
   }

   public int getPriority() {
      return this.mPriority;
   }

   public void setPriority(int aPriority) {
      this.mPriority = aPriority;
   }

   public Date getStreetDate() {
      return this.mStreetDate;
   }

   public void setStreetDate(Date aStreetDate) {
      this.mStreetDate = aStreetDate;
   }

   public String getSortTitle() {
      return this.mSortTitle;
   }

   public void setSortTitle(String aSortTitle) {
      this.mSortTitle = aSortTitle;
   }

   public String getShortTitle() {
      return this.mShortTitle;
   }

   public void setShortTitle(String aShortTitle) {
      this.mShortTitle = aShortTitle;
   }

   public String getOriginalTitle() {
      return this.mOriginalTitle;
   }

   public void setOriginalTitle(String aOriginalTitle) {
      this.mOriginalTitle = aOriginalTitle;
   }

   public String getTranslatedTitle() {
      return this.mTranslatedTitle;
   }

   public void setTranslatedTitle(String aTranslatedTitle) {
      this.mTranslatedTitle = aTranslatedTitle;
   }

   public int getTitleDetailId() {
      return this.mTitleDetailId;
   }

   public void setTitleDetailId(int aTitleDetailId) {
      this.mTitleDetailId = aTitleDetailId;
   }

   public int getGenre1Id() {
      return this.mGenre1Id;
   }

   public void setGenre1Id(int aGenre1Id) {
      this.mGenre1Id = aGenre1Id;
   }

   public int getGenre2Id() {
      return this.mGenre2Id;
   }

   public void setGenre2Id(int aGenre2Id) {
      this.mGenre2Id = aGenre2Id;
   }

   public int getGenre3Id() {
      return this.mGenre3Id;
   }

   public void setGenre3Id(int aGenre3Id) {
      this.mGenre3Id = aGenre3Id;
   }

   public boolean isMarkedForRent() {
      return this.mMarkedForRent;
   }

   public void setMarkedForRent(boolean aMarkedForRent) {
      this.mMarkedForRent = aMarkedForRent;
   }

   public boolean isMarkedForSale() {
      return this.mMarkedForSale;
   }

   public void setMarkedForSale(boolean aMarkedForSale) {
      this.mMarkedForSale = aMarkedForSale;
   }

   public boolean isMarkedForRemoval() {
      if (this.mMarkedForRemoval) {
         if (this.mMarkedForRemovalDate == null) {
            return true;
         } else {
            try {
               return Util.isDateBeforeNow(this.mMarkedForRemovalDate);
            } catch (Exception var2) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, var2.getMessage(), var2);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public void setMarkedForRemoval(boolean aMarkedForRemoval) {
      this.mMarkedForRemoval = aMarkedForRemoval;
   }

   public Date getMarkedForRemovealDate() {
      return this.mMarkedForRemovalDate;
   }

   public void setMarkedForRemovalDate(Date aMarkedForRemovalDate) {
      this.mMarkedForRemovalDate = aMarkedForRemovalDate;
   }

   public String getPoster() {
      return this.mPoster;
   }

   public String getSmallPoster() {
      return Util.getPosterSmallName(this.mPoster);
   }

   public void setPoster(String aPoster) {
      this.mPoster = aPoster;
   }

   public int getDiscStatusId() {
      return this.mDiscStatusId;
   }

   public void setDiscStatusId(int aDiscStatusId) {
      this.mDiscStatusId = aDiscStatusId;
   }

   public int getPriceOptionId() {
      return this.mPriceOptionId;
   }

   public void setPriceOptionId(int aPriceOptionId) {
      this.mPriceOptionId = aPriceOptionId;
   }

   public String getDescription() {
      return this.mDescription;
   }

   public void setDescription(String aDescription) {
      this.mDescription = aDescription;
   }

   public int getTitleTypeId() {
      return this.mTitleTypeId;
   }

   public void setTitleTypeId(int aTitleTypeId) {
      this.mTitleTypeId = aTitleTypeId;
   }

   public int getSlot() {
      return this.mSlot;
   }

   public void setSlot(int aSlot) {
      this.mSlot = aSlot;
   }

   public String getDiscCode() {
      return this.mDiscCode;
   }

   public void setDiscCode(String aDiscCode) {
      this.mDiscCode = aDiscCode;
   }

   public String getGroupCode() {
      return this.mGroupCode;
   }

   public void setGroupCode(String aGroupCode) {
      this.mGroupCode = aGroupCode;
   }

   public Date getDTUpdated() {
      return this.mDTUpdated;
   }

   public void setDTUpdated(Date aDTUpdated) {
      this.mDTUpdated = aDTUpdated;
   }

   public String getAttr1() {
      return this.mAttr1;
   }

   public void setAttr1(String aAttr1) {
      this.mAttr1 = aAttr1;
   }

   public String getAttr2() {
      return this.mAttr2;
   }

   public void setAttr2(String aAttr2) {
      this.mAttr2 = aAttr2;
   }

   public String getAttr3() {
      return this.mAttr3;
   }

   public void setAttr3(String aAttr3) {
      this.mAttr3 = aAttr3;
   }

   public String getAttr4() {
      return this.mAttr4;
   }

   public void setAttr4(String aAttr4) {
      this.mAttr4 = aAttr4;
   }

   public String getAttr5() {
      return this.mAttr5;
   }

   public void setAttr5(String aAttr5) {
      this.mAttr5 = aAttr5;
   }

   public String getAttr6() {
      return this.mAttr6;
   }

   public void setAttr6(String aAttr6) {
      this.mAttr6 = aAttr6;
   }

   public int getRating1Id() {
      return this.mRating1Id;
   }

   public void setRating1Id(int aRating1Id) {
      this.mRating1Id = aRating1Id;
   }

   public int getRating2Id() {
      return this.mRating2Id;
   }

   public void setRating2Id(int aRating2Id) {
      this.mRating2Id = aRating2Id;
   }

   public int getRating3Id() {
      return this.mRating3Id;
   }

   public void setRating3Id(int aRating3Id) {
      this.mRating3Id = aRating3Id;
   }

   public int getRatingSystem1Id() {
      return this.mRatingSystem1Id;
   }

   public void setRatingSystem1Id(int aRatingSystem1Id) {
      this.mRatingSystem1Id = aRatingSystem1Id;
   }

   public int getRatingSystem2Id() {
      return this.mRatingSystem2Id;
   }

   public void setRatingSystem2Id(int aRatingSystem2Id) {
      this.mRatingSystem2Id = aRatingSystem2Id;
   }

   public int getRatingSystem3Id() {
      return this.mRatingSystem3Id;
   }

   public void setRatingSystem3Id(int aRatingSystem3Id) {
      this.mRatingSystem3Id = aRatingSystem3Id;
   }

   public String getReleaseYear() {
      return this.mReleaseYear;
   }

   public void setReleaseYear(String aReleaseYear) {
      this.mReleaseYear = aReleaseYear;
   }

   public void incDispenseFailureCount() {
      this.mDispenseFailureCount++;
   }

   public int getDispenseFailureCount() {
      return this.mDispenseFailureCount;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof DiscIndexItem)) {
         return false;
      } else {
         DiscIndexItem discIndexItem = (DiscIndexItem)o;
         return this.mDiscDetailId == discIndexItem.mDiscDetailId;
      }
   }

   public int hashCode() {
      return this.mDiscDetailId;
   }

   public int compareTo(DiscIndexItem a) {
      if (this.mPriority < a.mPriority) {
         return -1;
      } else if (this.mPriority > a.mPriority) {
         return 1;
      } else {
         if (this.mStreetDate != null) {
            if (a.mStreetDate == null) {
               return 1;
            }

            if (this.mStreetDate.compareTo(a.mStreetDate) < 0) {
               return 1;
            }

            if (this.mStreetDate.compareTo(a.mStreetDate) > 0) {
               return -1;
            }
         }

         if (this.mSortTitle.compareTo(a.mSortTitle) < 0) {
            return -1;
         } else {
            return this.mSortTitle.compareTo(a.mSortTitle) > 0 ? 1 : 0;
         }
      }
   }
}
