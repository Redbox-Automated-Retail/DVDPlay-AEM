package net.dvdplay.inventory;

import java.util.Date;

public class Disc {
   private int mDiscDetailId;
   private int mTitleDetailId;
   private int mDiscStatusId;
   private int mPriceOptionId;
   private String mDiscCode;
   private int mGroupCodeID;
   private String mGroupCode;
   private int mSlot;
   private int mPriority;
   private boolean mMarkedForSale;
   private boolean mMarkedForRent;
   private boolean mMarkedForRemoval;
   private Date mRemovalDate;
   private Date mDTUpdated;
   private int mTitleId;
   private String mLocation;
   private int mTimesRented;

   public int getTimesRented() {
      return this.mTimesRented;
   }

   public void setTimesRented(int mTimesRented) {
      this.mTimesRented = mTimesRented;
   }

   public String getLocation() {
      return this.mLocation;
   }

   public void setmLocation(String mLocation) {
      this.mLocation = mLocation;
   }

   public int getTitleId() {
      return this.mTitleId;
   }

   public void setTitleId(int mTitleId) {
      this.mTitleId = mTitleId;
   }

   public int getDiscDetailId() {
      return this.mDiscDetailId;
   }

   public void setDiscDetailId(int aDiscDetailId) {
      this.mDiscDetailId = aDiscDetailId;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public int getTitleDetailId() {
      return this.mTitleDetailId;
   }

   public void setTitleDetailId(int aTitleDetailId) {
      this.mTitleDetailId = aTitleDetailId;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public int getDiscStatusId() {
      return this.mDiscStatusId;
   }

   public void setDiscStatusId(int aDiscStatusId) {
      this.mDiscStatusId = aDiscStatusId;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public int getPriceOptionId() {
      return this.mPriceOptionId;
   }

   public void setPriceOptionId(int aPriceOptionId) {
      this.mPriceOptionId = aPriceOptionId;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public String getDiscCode() {
      return this.mDiscCode;
   }

   public void setDiscCode(String aDiscCode) {
      this.mDiscCode = aDiscCode;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public int getGroupCodeID() {
      return this.mGroupCodeID;
   }

   public void setGroupCodeID(int aGroupCodeID) {
      this.mGroupCodeID = aGroupCodeID;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public String getGroupCode() {
      return this.mGroupCode;
   }

   public void setGroupCode(String aGroupCode) {
      this.mGroupCode = aGroupCode;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public int getSlot() {
      return this.mSlot;
   }

   public void setSlot(int aSlot) {
      this.mSlot = aSlot;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public int getPriority() {
      return this.mPriority;
   }

   public void setPriority(int aPriority) {
      this.mPriority = aPriority;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public boolean getMarkedForSale() {
      return this.mMarkedForSale;
   }

   public void setMarkedForSale(boolean aMarkedForSale) {
      this.mMarkedForSale = aMarkedForSale;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public boolean getMarkedForRent() {
      return this.mMarkedForRent;
   }

   public void setMarkedForRent(boolean aMarkedForRent) {
      this.mMarkedForRent = aMarkedForRent;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public boolean getMarkedForRemoval() {
      return this.mMarkedForRemoval;
   }

   public void setMarkedForRemoval(boolean aMarkedForRemoval) {
      this.mMarkedForRemoval = aMarkedForRemoval;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public Date getRemovalDate() {
      return this.mRemovalDate;
   }

   public void setRemovalDate(Date aRemovalDate) {
      this.mRemovalDate = aRemovalDate;
   }

   public Date getDTUpdated() {
      return this.mDTUpdated;
   }

   public void setDTUpdated(Date aDate) {
      this.mDTUpdated = aDate;
   }

   public void updateDTUpdated() {
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      int aGroupCodeID,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval
   ) {
      this(
         aDiscDetailId,
         aTitleDetailId,
         aDiscStatusId,
         aPriceOptionId,
         aDiscCode,
         aGroupCodeID,
         aSlot,
         aPriority,
         aMarkedForSale,
         aMarkedForRent,
         aMarkedForRemoval,
         null
      );
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      int aGroupCodeID,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval,
      Date aRemovalDate
   ) {
      this.mDiscDetailId = aDiscDetailId;
      this.mTitleDetailId = aTitleDetailId;
      this.mDiscStatusId = aDiscStatusId;
      this.mPriceOptionId = aPriceOptionId;
      this.mDiscCode = aDiscCode;
      this.mGroupCode = "";
      this.mGroupCodeID = aGroupCodeID;
      this.mSlot = aSlot;
      this.mPriority = aPriority;
      this.mMarkedForSale = aMarkedForSale;
      this.mMarkedForRent = aMarkedForRent;
      this.mMarkedForRemoval = aMarkedForRemoval;
      this.mRemovalDate = aRemovalDate;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      String aGroupCode,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval
   ) {
      this(
         aDiscDetailId,
         aTitleDetailId,
         aDiscStatusId,
         aPriceOptionId,
         aDiscCode,
         aGroupCode,
         aSlot,
         aPriority,
         aMarkedForSale,
         aMarkedForRent,
         aMarkedForRemoval,
         null
      );
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      String aGroupCode,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval,
      Date aRemovalDate
   ) {
      this.mDiscDetailId = aDiscDetailId;
      this.mTitleDetailId = aTitleDetailId;
      this.mDiscStatusId = aDiscStatusId;
      this.mPriceOptionId = aPriceOptionId;
      this.mDiscCode = aDiscCode;
      this.mGroupCode = aGroupCode;
      this.mSlot = aSlot;
      this.mPriority = aPriority;
      this.mMarkedForSale = aMarkedForSale;
      this.mMarkedForRent = aMarkedForRent;
      this.mMarkedForRemoval = aMarkedForRemoval;
      this.mRemovalDate = aRemovalDate;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      int aGroupCodeID,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval,
      int aTitleId,
      String aLocation,
      int aTimesRented
   ) {
      this(
         aDiscDetailId,
         aTitleDetailId,
         aDiscStatusId,
         aPriceOptionId,
         aDiscCode,
         aGroupCodeID,
         aSlot,
         aPriority,
         aMarkedForSale,
         aMarkedForRent,
         aMarkedForRemoval,
         aTitleId,
         aLocation,
         aTimesRented,
         null
      );
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      int aGroupCodeID,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval,
      int aTitleId,
      String aLocation,
      int aTimesRented,
      Date aRemovalDate
   ) {
      this.mDiscDetailId = aDiscDetailId;
      this.mTitleDetailId = aTitleDetailId;
      this.mDiscStatusId = aDiscStatusId;
      this.mPriceOptionId = aPriceOptionId;
      this.mDiscCode = aDiscCode;
      this.mGroupCodeID = aGroupCodeID;
      this.mGroupCode = "";
      this.mSlot = aSlot;
      this.mPriority = aPriority;
      this.mMarkedForSale = aMarkedForSale;
      this.mMarkedForRent = aMarkedForRent;
      this.mMarkedForRemoval = aMarkedForRemoval;
      this.mDTUpdated = new Date(System.currentTimeMillis());
      this.mTitleId = aTitleId;
      this.mLocation = aLocation;
      this.mTimesRented = aTimesRented;
      this.mRemovalDate = aRemovalDate;
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      String aGroupCode,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval,
      int aTitleId,
      String aLocation,
      int aTimesRented
   ) {
      this(
         aDiscDetailId,
         aTitleDetailId,
         aDiscStatusId,
         aPriceOptionId,
         aDiscCode,
         aGroupCode,
         aSlot,
         aPriority,
         aMarkedForSale,
         aMarkedForRent,
         aMarkedForRemoval,
         aTitleId,
         aLocation,
         aTimesRented,
         null
      );
   }

   public Disc(
      int aDiscDetailId,
      int aTitleDetailId,
      int aDiscStatusId,
      int aPriceOptionId,
      String aDiscCode,
      String aGroupCode,
      int aSlot,
      int aPriority,
      boolean aMarkedForSale,
      boolean aMarkedForRent,
      boolean aMarkedForRemoval,
      int aTitleId,
      String aLocation,
      int aTimesRented,
      Date aRemovalDate
   ) {
      this.mDiscDetailId = aDiscDetailId;
      this.mTitleDetailId = aTitleDetailId;
      this.mDiscStatusId = aDiscStatusId;
      this.mPriceOptionId = aPriceOptionId;
      this.mDiscCode = aDiscCode;
      this.mGroupCode = aGroupCode;
      this.mSlot = aSlot;
      this.mPriority = aPriority;
      this.mMarkedForSale = aMarkedForSale;
      this.mMarkedForRent = aMarkedForRent;
      this.mMarkedForRemoval = aMarkedForRemoval;
      this.mDTUpdated = new Date(System.currentTimeMillis());
      this.mTitleId = aTitleId;
      this.mLocation = aLocation;
      this.mTimesRented = aTimesRented;
      this.mRemovalDate = aRemovalDate;
   }

   public Disc() {
      this.mDiscDetailId = 0;
      this.mTitleDetailId = 0;
      this.mDiscStatusId = 0;
      this.mPriceOptionId = 0;
      this.mDiscCode = "";
      this.mGroupCode = "";
      this.mGroupCodeID = 0;
      this.mSlot = 0;
      this.mPriority = 999;
      this.mMarkedForSale = false;
      this.mMarkedForRent = false;
      this.mMarkedForRemoval = false;
      this.mRemovalDate = null;
      this.mDTUpdated = new Date(System.currentTimeMillis());
      this.mTitleId = 0;
      this.mLocation = "";
      this.mTimesRented = 0;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Disc)) {
         return false;
      } else {
         Disc disc = (Disc)o;
         if (this.mDiscCode != null ? this.mDiscCode.equals(disc.mDiscCode) : disc.mDiscCode == null) {
            return this.mGroupCode != null ? this.mGroupCode.equals(disc.mGroupCode) : disc.mGroupCode == null;
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int result = this.mDiscCode != null ? this.mDiscCode.hashCode() : 0;
      return 29 * result + (this.mGroupCode != null ? this.mGroupCode.hashCode() : 0);
   }

   public Disc(Disc aDisc) {
      this.mDiscDetailId = aDisc.getDiscDetailId();
      this.mTitleDetailId = aDisc.getTitleDetailId();
      this.mDiscStatusId = aDisc.getDiscStatusId();
      this.mPriceOptionId = aDisc.getPriceOptionId();
      this.mDiscCode = aDisc.getDiscCode();
      this.mGroupCode = aDisc.getGroupCode();
      this.mSlot = aDisc.getSlot();
      this.mPriority = aDisc.getPriority();
      this.mMarkedForSale = aDisc.getMarkedForSale();
      this.mMarkedForRent = aDisc.getMarkedForRent();
      this.mMarkedForRemoval = aDisc.getMarkedForRemoval();
      this.mRemovalDate = aDisc.getRemovalDate();
      this.mDTUpdated = new Date(System.currentTimeMillis());
      this.mTitleId = aDisc.getTitleId();
      this.mLocation = aDisc.getLocation();
      this.mTimesRented = aDisc.getTimesRented();
   }
}
