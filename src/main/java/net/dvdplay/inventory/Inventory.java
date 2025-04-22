package net.dvdplay.inventory;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.RCSet;
import net.dvdplay.communication.RDataSetFieldValues;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.poll.PollIndex;
import net.dvdplay.poll.PollItem;
import net.dvdplay.util.Util;

public class Inventory {
   private static DiscIndex mDiscIndex;
   private static SortTitleDiscIndex mSortTitleDiscIndex;
   private static StreetDateDiscIndex mStreetDateDiscIndex;
   private static SlotIndex mSlotIndex;
   private static TitleIndex mTitleIndex;
   private static ArrayList mTopPickList;
   private static ArrayList mSortTitlePickList;
   private static ArrayList mStreetDatePickList;
   private static ArrayList mGenrePickList;
   private static ArrayList mGenreList;
   private static PlayList mMediaPlayList;
   private static PlayList mStaticPlayList;
   private static LocaleIndex mLocaleIndex;
   private static TitleTypeIndex mTitleTypeIndex;
   private static ArrayList mTitleTypeList;
   private static PaymentCardTypeIndex mPaymentCardTypeIndex;
   private static ArrayList mPaymentCardTypeList;
   private static PollIndex mPollIndex;

   public Inventory() {
      mDiscIndex = new DiscIndex();
      mSortTitleDiscIndex = new SortTitleDiscIndex();
      mStreetDateDiscIndex = new StreetDateDiscIndex();
      mSlotIndex = new SlotIndex();
      mTitleIndex = new TitleIndex();
      mTopPickList = new ArrayList();
      mSortTitlePickList = new ArrayList();
      mStreetDatePickList = new ArrayList();
      mGenrePickList = new ArrayList();
      mGenreList = new ArrayList();
      mMediaPlayList = new PlayList();
      mStaticPlayList = new PlayList();
      mLocaleIndex = new LocaleIndex();
      mTitleTypeIndex = new TitleTypeIndex();
      mTitleTypeList = new ArrayList();
      mPaymentCardTypeIndex = new PaymentCardTypeIndex();
      mPaymentCardTypeList = new ArrayList();
      mPollIndex = new PollIndex();
      createAllIndexes();
   }

   public static synchronized void createAllIndexes() {
      createIndexes();
      createLocaleIndex();
      createTitleTypeIndex();
      createPaymentCardTypeIndex();
      addBadSlotsToSlotIndex();
      createPollIndex();
   }

   public static synchronized void createIndexes() {
      Aem.logSummaryMessage("Creating indexes");
      DiscIndex.clear();
      SortTitleDiscIndex.clear();
      StreetDateDiscIndex.clear();
      mSlotIndex.clear();
      mTitleIndex.clear();

      try {
         for (int i = 0; i < DOMData.mDiscDetailData.rowCount(); i++) {
            if (!DOMData.mDiscDetailData.isDeleted(i)) {
               boolean lIndexAdded = false;
               boolean lFoundTitle = false;
               int lDisc_TitleDetailId = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(i, DOMData.mDiscDetailData.getFieldIndex("TitleDetailId")));

               for (int j = 0; j < DOMData.mTitleDetailData.rowCount(); j++) {
                  if (!DOMData.mTitleDetailData.isDeleted(j)) {
                     int lTitle_TitleDetailId = Integer.parseInt(DOMData.mTitleDetailData.getFieldValue(j, DOMData.mTitleDetailData.getFieldIndex("TitleDetailId")));
                     int lTitle_LocalizationId = Integer.parseInt(DOMData.mTitleDetailData.getFieldValue(j, DOMData.mTitleDetailData.getFieldIndex("LocaleId")));
                     if (lTitle_TitleDetailId == lDisc_TitleDetailId) {
                        lFoundTitle = true;
                        if (Aem.getLocaleId() == lTitle_LocalizationId) {
                           createIndex(DOMData.mDiscDetailData.getRow(i), DOMData.mTitleDetailData.getRow(j), true);
                           lIndexAdded = true;
                           break;
                        }
                     }
                  }
               }

               if (!lIndexAdded) {
                  if (lDisc_TitleDetailId > 0) {
                     if (lFoundTitle) {
                        Aem.logDetailMessage(
                           DvdplayLevel.WARNING, "Could not find TitleDetailId " + lDisc_TitleDetailId + " in TitleDetail for LocaleId " + Aem.getLocaleId()
                        );
                     } else {
                        Aem.logDetailMessage(DvdplayLevel.WARNING, "Could not find TitleDetailId " + lDisc_TitleDetailId + " in TitleDetail");
                        Aem.setDataError();
                     }
                  }

                  createIndex(DOMData.mDiscDetailData.getRow(i), null, true);
               }
            }
         }
      } catch (Exception var7) {
         Aem.logDetailMessage(DvdplayLevel.SEVERE, "Creating indexes failed");
         Aem.logSummaryMessage("Creating indexes failed");
         Aem.logDetailMessage(DvdplayLevel.SEVERE, var7.getMessage());
         Aem.logSummaryMessage(var7.getMessage());
         Aem.setDataError();
      }
   }

   public static synchronized void createIndex(int aDiscDetailId, boolean aIsNewDisc) {
      try {
         for (int i = 0; i < DOMData.mDiscDetailData.rowCount(); i++) {
            if (!DOMData.mDiscDetailData.isDeleted(i)
               && Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(i, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"))) == aDiscDetailId) {
               int lDisc_TitleDetailId = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(i, DOMData.mDiscDetailData.getFieldIndex("TitleDetailId")));

               for (int j = 0; j < DOMData.mTitleDetailData.rowCount(); j++) {
                  if (!DOMData.mTitleDetailData.isDeleted(j)) {
                     int lTitle_TitleDetailId = Integer.parseInt(DOMData.mTitleDetailData.getFieldValue(j, DOMData.mTitleDetailData.getFieldIndex("TitleDetailId")));
                     int lTitle_LocalizationId = Integer.parseInt(DOMData.mTitleDetailData.getFieldValue(j, DOMData.mTitleDetailData.getFieldIndex("LocaleId")));
                     if (lTitle_TitleDetailId == lDisc_TitleDetailId && Aem.getLocaleId() == lTitle_LocalizationId) {
                        createIndex(DOMData.mDiscDetailData.getRow(i), DOMData.mTitleDetailData.getRow(j), aIsNewDisc);
                        return;
                     }
                  }
               }

               createIndex(DOMData.mDiscDetailData.getRow(i), null, aIsNewDisc);
               return;
            }
         }
      } catch (Exception var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "Create Index failed");
         Aem.logSummaryMessage("Create Index failed");
         Aem.logDetailMessage(DvdplayLevel.ERROR, var7.getMessage());
         Aem.setDataError();
      }
   }

   public static synchronized void createIndex(RDataSetFieldValues lDiscDetailFieldValues, RDataSetFieldValues lTitleDetailFieldValues, boolean aIsNewDisc) {
      String lCurrentField = "";

      try {
         DiscIndexItem lDiscIndexItem = new DiscIndexItem();
         lCurrentField = "DiscDetailId";
         int lDiscDetailId = Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DiscDetailId")));
         lDiscIndexItem.setDiscDetailId(lDiscDetailId);
         Aem.logDetailMessage(DvdplayLevel.FINE, "Creating DiscIndex for DiscDetailId " + lDiscDetailId);
         lCurrentField = "Priority";
         lDiscIndexItem.setPriority(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("Priority"))));
         lCurrentField = "MarkedForRent";
         if (Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("MarkedForRent"))) == 1) {
            lDiscIndexItem.setMarkedForRent(true);
         } else {
            lDiscIndexItem.setMarkedForRent(false);
         }

         lCurrentField = "MarkedForSale";
         if (Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("MarkedForSale"))) == 1) {
            lDiscIndexItem.setMarkedForSale(true);
         } else {
            lDiscIndexItem.setMarkedForSale(false);
         }

         lCurrentField = "MarkedForRemoval";
         if (Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("MarkedForRemoval"))) == 1) {
            lDiscIndexItem.setMarkedForRemoval(true);
         } else {
            lDiscIndexItem.setMarkedForRemoval(false);
         }

         lCurrentField = "RemovalDate";

         try {
            String lRemovalDate = lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("RemovalDate"));
            if (lRemovalDate.trim().length() == 0) {
               lDiscIndexItem.setMarkedForRemovalDate(null);
            } else {
               lDiscIndexItem.setMarkedForRemovalDate(Util.stringToDate(lRemovalDate, "yyyy-MM-dd HH:mm:ss.SSS"));
            }
         } catch (Exception var8) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, lCurrentField + ": " + var8.getMessage());
            lDiscIndexItem.setMarkedForRemovalDate(null);
         }

         lCurrentField = "DiscStatusId";
         lDiscIndexItem.setDiscStatusId(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DiscStatusId"))));
         lCurrentField = "PriceOptionId";
         lDiscIndexItem.setPriceOptionId(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("PriceOptionId"))));
         lCurrentField = "Slot";
         lDiscIndexItem.setSlot(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("Slot"))));
         mSlotIndex.addSlot(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("Slot"))), lDiscDetailId);
         lCurrentField = "DiscCode";
         lDiscIndexItem.setDiscCode(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DiscCode")));
         lCurrentField = "GroupCode";
         lDiscIndexItem.setGroupCode(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("GroupCode")));
         lCurrentField = "DTUpdated";
         lDiscIndexItem.setDTUpdated(Util.stringToDate(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DTUpdated"))));
         if (lTitleDetailFieldValues != null) {
            lCurrentField = "TitleDetailId";
            String lTitleDetailId = lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("TitleDetailId"));
            lDiscIndexItem.setTitleDetailId(Integer.parseInt(lTitleDetailId));
            if (aIsNewDisc) {
               mTitleIndex.addTitle(Integer.parseInt(lTitleDetailId));
            }

            lCurrentField = "StreetDate";
            lDiscIndexItem.setStreetDate(Util.stringToDate(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("StreetDate"))));
            lCurrentField = "Title";
            lDiscIndexItem.setOriginalTitle(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Title")));
            lCurrentField = "TranslatedTitle";
            lDiscIndexItem.setTranslatedTitle(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("TranslatedTitle")));
            lCurrentField = "SortTitle";
            lDiscIndexItem.setSortTitle(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("SortTitle")));
            lCurrentField = "ShortTitle";
            lDiscIndexItem.setShortTitle(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("ShortTitle")));
            lCurrentField = "Genre1Id";
            lDiscIndexItem.setGenre1Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Genre1Id"))));
            lCurrentField = "Genre2Id";
            lDiscIndexItem.setGenre2Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Genre2Id"))));
            lCurrentField = "Genre3Id";
            lDiscIndexItem.setGenre3Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Genre3Id"))));
            lCurrentField = "Poster";
            lDiscIndexItem.setPoster(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Poster")));
            lCurrentField = "Description";
            lDiscIndexItem.setDescription(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Description")));
            lCurrentField = "TitleTypeId";
            lDiscIndexItem.setTitleTypeId(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("TitleTypeId"))));
            lCurrentField = "Attr1";
            lDiscIndexItem.setAttr1(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Attr1")));
            lCurrentField = "Attr2";
            lDiscIndexItem.setAttr2(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Attr2")));
            lCurrentField = "Attr3";
            lDiscIndexItem.setAttr3(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Attr3")));
            lCurrentField = "Attr4";
            lDiscIndexItem.setAttr4(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Attr4")));
            lCurrentField = "Attr5";
            lDiscIndexItem.setAttr5(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Attr5")));
            lCurrentField = "Attr6";
            lDiscIndexItem.setAttr6(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Attr6")));
            lCurrentField = "Rating1Id";
            lDiscIndexItem.setRating1Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Rating1Id"))));
            lCurrentField = "Rating2Id";
            lDiscIndexItem.setRating2Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Rating2Id"))));
            lCurrentField = "Rating3Id";
            lDiscIndexItem.setRating3Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("Rating3Id"))));
            lCurrentField = "RatingSystem1Id";
            lDiscIndexItem.setRatingSystem1Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("RatingSystem1Id"))));
            lCurrentField = "RatingSystem2Id";
            lDiscIndexItem.setRatingSystem2Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("RatingSystem2Id"))));
            lCurrentField = "RatingSystem3Id";
            lDiscIndexItem.setRatingSystem3Id(Integer.parseInt(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("RatingSystem3Id"))));
            lCurrentField = "ReleaseYear";
            lDiscIndexItem.setReleaseYear(lTitleDetailFieldValues.getValue(DOMData.mTitleDetailData.getFieldIndex("ReleaseYear")));
         }

         lCurrentField = "";
         DiscIndex.addDiscIndexItem(lDiscIndexItem);
         SortTitleDiscIndex.addDiscIndexItem(new SortTitleDiscIndexItem(lDiscIndexItem));
         StreetDateDiscIndex.addDiscIndexItem(new StreetDateDiscIndexItem(lDiscIndexItem));
      } catch (Exception var9) {
         if (lCurrentField.length() != 0) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "CurrentField is " + lCurrentField);
            Aem.logSummaryMessage("CurrentField is " + lCurrentField);
         }

         Aem.logDetailMessage(DvdplayLevel.ERROR, "CreateIndex() Exception caught: " + var9.getMessage());
         Aem.logSummaryMessage("CreateIndex() Exception caught: " + var9.getMessage());
         Aem.setDataError();
      }
   }

   public static Disc getDisc(int aDiscDetailId) {
      Disc lNewDisc = new Disc();

      try {
         int i = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, "DiscDetailId", Integer.toString(aDiscDetailId));
         if (i < 0) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "getDisc: Could not find DiscDetailId " + aDiscDetailId + " + in DiscDetail");
            return null;
         } else {
            RDataSetFieldValues lDiscDetailFieldValues = DOMData.mDiscDetailData.getRow(i);
            lNewDisc.setDiscDetailId(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"))));
            lNewDisc.setTitleDetailId(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("TitleDetailId"))));
            lNewDisc.setDiscStatusId(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DiscStatusId"))));
            lNewDisc.setPriceOptionId(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("PriceOptionId"))));
            lNewDisc.setSlot(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("Slot"))));
            lNewDisc.setPriority(Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("Priority"))));
            lNewDisc.setDiscCode(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DiscCode")));
            lNewDisc.setGroupCode(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("GroupCode")));
            if (Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("MarkedForRent"))) == 1) {
               lNewDisc.setMarkedForRent(true);
            } else {
               lNewDisc.setMarkedForRent(false);
            }

            if (Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("MarkedForSale"))) == 1) {
               lNewDisc.setMarkedForSale(true);
            } else {
               lNewDisc.setMarkedForSale(false);
            }

            if (Integer.parseInt(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("MarkedForRemoval"))) == 1) {
               lNewDisc.setMarkedForRemoval(true);
            } else {
               lNewDisc.setMarkedForRemoval(false);
            }

            try {
               String lRemovalDate = lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("RemovalDate"));
               if (lRemovalDate.trim().length() == 0) {
                  lNewDisc.setRemovalDate(null);
               } else {
                  lNewDisc.setRemovalDate(Util.stringToDate(lRemovalDate, "yyyy-MM-dd HH:mm:ss.SSS"));
               }
            } catch (Exception var5) {
               lNewDisc.setRemovalDate(null);
            }

            lNewDisc.setDTUpdated(Util.stringToDate(lDiscDetailFieldValues.getValue(DOMData.mDiscDetailData.getFieldIndex("DTUpdated"))));
            return lNewDisc;
         }
      } catch (DvdplayException var6) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "getDisc" + var6.getMessage());
         return null;
      }
   }

   public static synchronized void updateUnknownDisc(RCSet aDiscRCSet) {
      try {
         for (int i = 0; i < aDiscRCSet.rowCount(); i++) {
            if (!aDiscRCSet.isDeleted(i)) {
               int lDiscDetailId = Integer.parseInt(aDiscRCSet.getFieldValue(i, aDiscRCSet.getFieldIndex("DiscDetailId")));
               DOMData.mDiscDetailData.addRow(aDiscRCSet.getRow(i));
               createIndex(lDiscDetailId, false);
            }
         }
      } catch (DvdplayException var3) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "updateUnknownDisc: " + var3.getMessage());
      }
   }

   public static synchronized void updateDisc(RCSet aDiscRCSet) {
      try {
         for (int i = 0; i < aDiscRCSet.rowCount(); i++) {
            if (!aDiscRCSet.isDeleted(i)) {
               int lDiscDetailId = Integer.parseInt(aDiscRCSet.getFieldValue(i, aDiscRCSet.getFieldIndex("DiscDetailId")));
               int lTitleDetailId = Integer.parseInt(aDiscRCSet.getFieldValue(i, aDiscRCSet.getFieldIndex("TitleDetailId")));
               String lDiscCode = aDiscRCSet.getFieldValue(i, aDiscRCSet.getFieldIndex("DiscCode"));
               String lGroupCode = aDiscRCSet.getFieldValue(i, aDiscRCSet.getFieldIndex("GroupCode"));
               if (lDiscCode.trim().length() == 0 || lGroupCode.trim().length() == 0) {
                  int lSlot = Integer.parseInt(aDiscRCSet.getFieldValue(i, aDiscRCSet.getFieldIndex("Slot")));
                  if (removeUnknownDisc(lSlot)) {
                     DOMData.mDiscDetailData.addRow(aDiscRCSet.getRow(i));
                     Aem.logDetailMessage(DvdplayLevel.FINE, "i have seen this disc before");
                     createIndex(lDiscDetailId, false);
                  } else {
                     DOMData.mDiscDetailData.addRow(aDiscRCSet.getRow(i));
                     Aem.logDetailMessage(DvdplayLevel.FINE, "i have not seen this disc before");
                     createIndex(lDiscDetailId, true);
                  }
               } else if (removeDisc(lDiscDetailId, lTitleDetailId, lDiscCode, lGroupCode, false)) {
                  DOMData.mDiscDetailData.addRow(aDiscRCSet.getRow(i));
                  Aem.logDetailMessage(DvdplayLevel.FINE, "i have seen this disc before");
                  createIndex(lDiscDetailId, false);
               } else {
                  DOMData.mDiscDetailData.addRow(aDiscRCSet.getRow(i));
                  Aem.logDetailMessage(DvdplayLevel.FINE, "i have not seen this disc before");
                  createIndex(lDiscDetailId, true);
               }
            }
         }
      } catch (DvdplayException var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "updateDisc: " + var7.getMessage());
      }
   }

   public static synchronized void updateDisc(Disc aDisc) {
      String[] values = new String[DvdplayBase.DISC_DETAIL_FIELD_NAMES.length];

      try {
         values[0] = String.valueOf(aDisc.getDiscDetailId());
         values[1] = String.valueOf(aDisc.getTitleDetailId());
         values[2] = String.valueOf(aDisc.getDiscStatusId());
         values[3] = String.valueOf(aDisc.getPriceOptionId());
         values[4] = aDisc.getDiscCode();
         values[5] = aDisc.getGroupCode();
         values[6] = String.valueOf(aDisc.getSlot());
         values[7] = String.valueOf(aDisc.getPriority());
         if (aDisc.getMarkedForSale()) {
            values[8] = Integer.toString(1);
         } else {
            values[8] = Integer.toString(0);
         }

         if (aDisc.getMarkedForRent()) {
            values[9] = Integer.toString(1);
         } else {
            values[9] = Integer.toString(0);
         }

         if (aDisc.getMarkedForRemoval()) {
            values[10] = Integer.toString(1);
         } else {
            values[10] = Integer.toString(0);
         }

         try {
            if (aDisc.getRemovalDate() == null) {
               values[11] = "";
            } else {
               values[11] = Util.dateToString(aDisc.getRemovalDate());
            }
         } catch (Exception var3) {
            values[11] = "";
         }

         values[12] = Util.dateToString(aDisc.getDTUpdated());
         if (removeDisc(aDisc.getDiscDetailId(), aDisc.getTitleDetailId(), aDisc.getDiscCode(), aDisc.getGroupCode(), false)) {
            DOMData.mDiscDetailData.addRow(values);
            Aem.logDetailMessage(DvdplayLevel.FINE, "i have seen this disc before");
            createIndex(aDisc.getDiscDetailId(), false);
         } else {
            DOMData.mDiscDetailData.addRow(values);
            Aem.logDetailMessage(DvdplayLevel.FINE, "i have not seen this disc before");
            createIndex(aDisc.getDiscDetailId(), true);
         }
      } catch (DvdplayException var4) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "updateDisc(disc): " + var4.getMessage());
      }
   }

   public static synchronized boolean removeUnknownDisc(int aSlot) {
      Aem.logDetailMessage(DvdplayLevel.FINE, "removeUnknownDisc at slot " + aSlot);

      try {
         int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, "Slot", Integer.toString(aSlot));
         if (lIndex < 0) {
            return false;
         }

         String lDiscDetailId = DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"));
         Aem.logDetailMessage(DvdplayLevel.FINE, "removing DiscDetailid " + lDiscDetailId + " from DiscDetail and indexes");
         mSlotIndex.removeDiscDetailId(Integer.parseInt(lDiscDetailId));
         DiscIndex.removeDiscIndexItem(Integer.parseInt(lDiscDetailId));
         SortTitleDiscIndex.removeDiscIndexItem(Integer.parseInt(lDiscDetailId));
         StreetDateDiscIndex.removeDiscIndexItem(Integer.parseInt(lDiscDetailId));
         DOMData.mDiscDetailData.deleteRow(lIndex);
      } catch (DvdplayException var3) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "removeUnknownDisc: " + var3.getMessage());
      }

      return false;
   }

   public static synchronized boolean removeDisc(int aDiscDetailId, int aTitleDetailId, String aDiscCode, String aGroupCode, boolean aIsRemove) {
      boolean lFoundDisc = false;
      Aem.logDetailMessage(DvdplayLevel.FINE, "removing DiscDetailid " + aDiscDetailId + " from DiscDetail");
      if (aIsRemove) {
         Aem.logDetailMessage(DvdplayLevel.FINE, "removing from indexes");
         mSlotIndex.removeDiscDetailId(aDiscDetailId);
         DiscIndex.removeDiscIndexItem(aDiscDetailId);
         SortTitleDiscIndex.removeDiscIndexItem(aDiscDetailId);
         StreetDateDiscIndex.removeDiscIndexItem(aDiscDetailId);
         mTitleIndex.decrementTitle(aTitleDetailId);
      }

      try {
         int lDelIndex;
         do {
            String[] lNames = new String[2];
            String[] lValues = new String[2];
            lNames[0] = "DiscCode";
            lValues[0] = aDiscCode;
            lNames[1] = "GroupCode";
            lValues[1] = aGroupCode;
            lDelIndex = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, lNames, lValues);
            if (lDelIndex >= 0) {
               String lDiscDetailId = DOMData.mDiscDetailData.getFieldValue(lDelIndex, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"));
               String lTitleDetailId = DOMData.mDiscDetailData.getFieldValue(lDelIndex, DOMData.mDiscDetailData.getFieldIndex("TitleDetailId"));
               if (Integer.parseInt(lDiscDetailId) != aDiscDetailId) {
                  Aem.logDetailMessage(DvdplayLevel.FINE, "also removing DiscDetailid " + lDiscDetailId + " from DiscDetail and indexes");
                  mSlotIndex.removeDiscDetailId(Integer.parseInt(lDiscDetailId));
                  DiscIndex.removeDiscIndexItem(Integer.parseInt(lDiscDetailId));
                  SortTitleDiscIndex.removeDiscIndexItem(Integer.parseInt(lDiscDetailId));
                  StreetDateDiscIndex.removeDiscIndexItem(Integer.parseInt(lDiscDetailId));
                  mTitleIndex.decrementTitle(Integer.parseInt(lTitleDetailId));
               } else {
                  lFoundDisc = true;
               }

               DOMData.mDiscDetailData.deleteRow(lDelIndex);
            }
         } while (lDelIndex >= 0);

         return lFoundDisc;
      } catch (DvdplayException var11) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "removeDisc: " + var11.getMessage());
         return false;
      }
   }

   public static synchronized void updateTitle(RCSet aTitleRCSet) {
      try {
         for (int i = 0; i < aTitleRCSet.rowCount(); i++) {
            if (!aTitleRCSet.isDeleted(i)) {
               Aem.logDetailMessage(DvdplayLevel.FINE, "updating TitleDetailId " + aTitleRCSet.getFieldValue(i, aTitleRCSet.getFieldIndex("TitleDetailId")));
               removeTitle(Integer.parseInt(aTitleRCSet.getFieldValue(i, aTitleRCSet.getFieldIndex("TitleDetailId"))), false);
               DOMData.mTitleDetailData.addRow(aTitleRCSet.getRow(i));
            }
         }
      } catch (DvdplayException var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var2.getMessage());
      }
   }

   public static synchronized RDataSetFieldValues removeTitle(int aTitleDetailId, boolean aIsRemove) {
      int lDelIndex = 0;
      RDataSetFieldValues lrdsfv = null;
      Aem.logDetailMessage(DvdplayLevel.FINE, "Deleting TitleDetailId " + aTitleDetailId + " from TitleDetail.");

      try {
         while (lDelIndex >= 0) {
            lDelIndex = Util.getRCSetIndexForFieldValue(DOMData.mTitleDetailData, "TitleDetailId", String.valueOf(aTitleDetailId));
            if (lDelIndex >= 0) {
               lrdsfv = DOMData.mTitleDetailData.getRow(lDelIndex);
               if (aIsRemove) {
               }

               Aem.logDetailMessage(DvdplayLevel.FINE, "deleting TitleDetail row");
               DOMData.mTitleDetailData.deleteRow(lDelIndex);
            }
         }

         return lrdsfv;
      } catch (DvdplayException var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "removeTitle: " + var7.getMessage());
         return null;
      }
   }

   private static void removePosters(String aPosterName) {
      File lPosterFile = new File("c:\\aem\\content\\posters\\" + aPosterName);
      if (lPosterFile.exists() && !lPosterFile.delete()) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete poster " + lPosterFile.getAbsolutePath());
      }

      lPosterFile = new File("c:\\aem\\content\\posters\\" + Util.getPosterSmallName(aPosterName));
      if (lPosterFile.exists() && !lPosterFile.delete()) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete poster " + lPosterFile.getAbsolutePath());
      }
   }

   private static boolean checkStreetDatePickListCriterion(DiscIndexItem aDiscIndexItem) {
      return aDiscIndexItem.getDiscDetailId() >= 0
         && aDiscIndexItem.getTitleTypeId() == Aem.getTitleTypeId()
         && (aDiscIndexItem.isMarkedForRent() || aDiscIndexItem.isMarkedForSale())
         && !aDiscIndexItem.isMarkedForRemoval()
         && (aDiscIndexItem.getDiscStatusId() == 3 || aDiscIndexItem.getDiscStatusId() == 1)
         && Util.isDateBeforeNow(aDiscIndexItem.getStreetDate())
         && (aDiscIndexItem.getGenre1Id() != 10 || aDiscIndexItem.getGenre2Id() == 10 || aDiscIndexItem.getGenre3Id() == 10);
   }

   public static int createStreetDatePickList(int aNumPicks) {
      mStreetDatePickList = new ArrayList();
      Aem.logDetailMessage(DvdplayLevel.FINE, "Creating StreetDate picks list");

      for (int i = 0; i < StreetDateDiscIndex.size() && mStreetDatePickList.size() < aNumPicks; i++) {
         StreetDateDiscIndexItem lDiscIndexItem = StreetDateDiscIndex.getDiscIndexItem(i);
         if (lDiscIndexItem.getTitleDetailId() != 0) {
            boolean lInList = false;

            for (int j = 0; j < mStreetDatePickList.size(); j++) {
               if (((StreetDateDiscIndexItem)mStreetDatePickList.get(j)).getTitleDetailId() == lDiscIndexItem.getTitleDetailId()) {
                  if (((StreetDateDiscIndexItem)mStreetDatePickList.get(j)).getDiscStatusId() != 3
                     && checkStreetDatePickListCriterion(lDiscIndexItem)
                     && lDiscIndexItem.getDiscStatusId() == 3) {
                     mStreetDatePickList.remove(j);
                     mStreetDatePickList.add(lDiscIndexItem);
                  }

                  lInList = true;
                  break;
               }
            }

            if (!lInList && checkStreetDatePickListCriterion(lDiscIndexItem)) {
               mStreetDatePickList.add(lDiscIndexItem);
            }
         }
      }

      return mStreetDatePickList.size() == aNumPicks ? aNumPicks : mStreetDatePickList.size();
   }

   public static StreetDateDiscIndexItem getStreetDatePick(int aIndex) {
      return aIndex >= 0 && aIndex < mStreetDatePickList.size() ? (StreetDateDiscIndexItem)mStreetDatePickList.get(aIndex) : null;
   }

   private static boolean checkSortTitlePickListCriterion(DiscIndexItem aDiscIndexItem) {
      return checkStreetDatePickListCriterion(aDiscIndexItem);
   }

   public static int createSortTitlePickList(int aNumPicks) {
      mSortTitlePickList = new ArrayList();
      Aem.logDetailMessage(DvdplayLevel.FINE, "Creating SortTitle picks list");

      for (int i = 0; i < SortTitleDiscIndex.size() && mSortTitlePickList.size() < aNumPicks; i++) {
         SortTitleDiscIndexItem lDiscIndexItem = SortTitleDiscIndex.getDiscIndexItem(i);
         if (lDiscIndexItem.getTitleDetailId() != 0) {
            boolean lInList = false;

            for (int j = 0; j < mSortTitlePickList.size(); j++) {
               if (((SortTitleDiscIndexItem)mSortTitlePickList.get(j)).getTitleDetailId() == lDiscIndexItem.getTitleDetailId()) {
                  if (((SortTitleDiscIndexItem)mSortTitlePickList.get(j)).getDiscStatusId() != 3
                     && checkSortTitlePickListCriterion(lDiscIndexItem)
                     && lDiscIndexItem.getDiscStatusId() == 3) {
                     mSortTitlePickList.remove(j);
                     mSortTitlePickList.add(lDiscIndexItem);
                  }

                  lInList = true;
                  break;
               }
            }

            if (!lInList && checkSortTitlePickListCriterion(lDiscIndexItem)) {
               mSortTitlePickList.add(lDiscIndexItem);
            }
         }
      }

      return mSortTitlePickList.size() == aNumPicks ? aNumPicks : mSortTitlePickList.size();
   }

   public static SortTitleDiscIndexItem getSortTitlePick(int aIndex) {
      return aIndex >= 0 && aIndex < mSortTitlePickList.size() ? (SortTitleDiscIndexItem)mSortTitlePickList.get(aIndex) : null;
   }

   private static boolean checkTopPickListCriterion(DiscIndexItem aDiscIndexItem) {
      return aDiscIndexItem.getDiscDetailId() >= 0
         && aDiscIndexItem.getTitleTypeId() == Aem.getTitleTypeId()
         && (aDiscIndexItem.isMarkedForRent() || aDiscIndexItem.isMarkedForSale())
         && !aDiscIndexItem.isMarkedForRemoval()
         && (aDiscIndexItem.getDiscStatusId() == 3 || aDiscIndexItem.getDiscStatusId() == 1)
         && Util.isDateBeforeNow(aDiscIndexItem.getStreetDate())
         && (aDiscIndexItem.getGenre1Id() != 10 || aDiscIndexItem.getGenre2Id() == 10 || aDiscIndexItem.getGenre3Id() == 10);
   }

   public static int createTopPickList(int aNumPicks) {
      mTopPickList = new ArrayList();
      Aem.logDetailMessage(DvdplayLevel.FINE, "Creating top picks list");

      for (int i = 0; i < DiscIndex.size() && mTopPickList.size() < aNumPicks; i++) {
         DiscIndexItem lDiscIndexItem = DiscIndex.getDiscIndexItem(i);
         if (lDiscIndexItem.getTitleDetailId() != 0) {
            boolean lInList = false;

            for (int j = 0; j < mTopPickList.size(); j++) {
               if (((DiscIndexItem)mTopPickList.get(j)).getTitleDetailId() == lDiscIndexItem.getTitleDetailId()) {
                  if (((DiscIndexItem)mTopPickList.get(j)).getDiscStatusId() != 3
                     && checkTopPickListCriterion(lDiscIndexItem)
                     && lDiscIndexItem.getDiscStatusId() == 3) {
                     mTopPickList.remove(j);
                     mTopPickList.add(lDiscIndexItem);
                  }

                  lInList = true;
                  break;
               }
            }

            if (!lInList && checkTopPickListCriterion(lDiscIndexItem)) {
               mTopPickList.add(lDiscIndexItem);
            }
         }
      }

      return mTopPickList.size() == aNumPicks ? aNumPicks : mTopPickList.size();
   }

   public static DiscIndexItem getTopPick(int aIndex) {
      return aIndex >= 0 && aIndex < mTopPickList.size() ? (DiscIndexItem)mTopPickList.get(aIndex) : null;
   }

   private static boolean checkGenrePickListCriterion(DiscIndexItem lDiscIndexItem, int aGenreId) {
      return lDiscIndexItem.getDiscDetailId() >= 0
         && lDiscIndexItem.getTitleTypeId() == Aem.getTitleTypeId()
         && (lDiscIndexItem.isMarkedForRent() || lDiscIndexItem.isMarkedForSale())
         && !lDiscIndexItem.isMarkedForRemoval()
         && (lDiscIndexItem.getGenre1Id() == aGenreId || lDiscIndexItem.getGenre2Id() == aGenreId || lDiscIndexItem.getGenre3Id() == aGenreId)
         && Util.isDateBeforeNow(lDiscIndexItem.getStreetDate())
         && (lDiscIndexItem.getDiscStatusId() == 3 || lDiscIndexItem.getDiscStatusId() == 1);
   }

   public static int createGenrePickList(int aGenreId, int aNumPicks) {
      mGenrePickList = new ArrayList();
      Aem.logDetailMessage(DvdplayLevel.FINE, "Creating genre picks list");

      for (int i = 0; i < DiscIndex.size() && mGenrePickList.size() < aNumPicks; i++) {
         DiscIndexItem lDiscIndexItem = DiscIndex.getDiscIndexItem(i);
         if (lDiscIndexItem.getTitleDetailId() != 0) {
            boolean lInList = false;

            for (int j = 0; j < mGenrePickList.size(); j++) {
               if (((DiscIndexItem)mGenrePickList.get(j)).getTitleDetailId() == lDiscIndexItem.getTitleDetailId()) {
                  if (((DiscIndexItem)mGenrePickList.get(j)).getDiscStatusId() != 3
                     && checkGenrePickListCriterion(lDiscIndexItem, aGenreId)
                     && lDiscIndexItem.getDiscStatusId() == 3) {
                     mGenrePickList.remove(j);
                     mGenrePickList.add(lDiscIndexItem);
                  }

                  lInList = true;
                  break;
               }
            }

            if (!lInList && checkGenrePickListCriterion(lDiscIndexItem, aGenreId)) {
               mGenrePickList.add(lDiscIndexItem);
            }
         }
      }

      return mGenrePickList.size() == aNumPicks ? aNumPicks : mGenrePickList.size();
   }

   public static DiscIndexItem getGenrePick(int aIndex) {
      return aIndex >= 0 && aIndex < mGenrePickList.size() ? (DiscIndexItem)mGenrePickList.get(aIndex) : null;
   }

   public static int createGenreList() {
      Aem.logDetailMessage(DvdplayLevel.FINE, "Creating genre list");
      mGenreList = new ArrayList();

      for (int i = 0; i < DiscIndex.size(); i++) {
         DiscIndexItem lDiscIndexItem = DiscIndex.getDiscIndexItem(i);
         if (lDiscIndexItem.getTitleTypeId() == Aem.getTitleTypeId()
            && (lDiscIndexItem.getDiscStatusId() == 3 || lDiscIndexItem.getDiscStatusId() == 1)
            && (lDiscIndexItem.isMarkedForRent() || lDiscIndexItem.isMarkedForSale())
            && !lDiscIndexItem.isMarkedForRemoval()
            && Util.isDateBeforeNow(lDiscIndexItem.getStreetDate())) {
            boolean lInList = false;

            for (int j = 0; j < mGenreList.size(); j++) {
               if (((GenreItem)mGenreList.get(j)).getGenreId() == lDiscIndexItem.getGenre1Id()) {
                  lInList = true;
                  break;
               }
            }

            if (!lInList) {
               addGenreList(String.valueOf(lDiscIndexItem.getGenre1Id()));
            }

            lInList = false;

            for (int jx = 0; jx < mGenreList.size(); jx++) {
               if (((GenreItem)mGenreList.get(jx)).getGenreId() == lDiscIndexItem.getGenre2Id()) {
                  lInList = true;
                  break;
               }
            }

            if (!lInList) {
               addGenreList(String.valueOf(lDiscIndexItem.getGenre2Id()));
            }

            lInList = false;

            for (int jxx = 0; jxx < mGenreList.size(); jxx++) {
               if (((GenreItem)mGenreList.get(jxx)).getGenreId() == lDiscIndexItem.getGenre3Id()) {
                  lInList = true;
                  break;
               }
            }

            if (!lInList) {
               addGenreList(String.valueOf(lDiscIndexItem.getGenre3Id()));
            }
         }
      }

      return mGenreList.size();
   }

   private static void addGenreList(String aGenreId) {
      GenreItem lGenreItem = new GenreItem();
      int lIndex = -1;

      try {
         String[] lNames = new String[3];
         String[] lValues = new String[3];
         lNames[0] = "LocaleId";
         lNames[1] = "TitleTypeId";
         lNames[2] = "GenreId";
         lValues[0] = Integer.toString(Aem.getLocaleId());
         lValues[1] = Integer.toString(Aem.getTitleTypeId());
         lValues[2] = aGenreId;
         lIndex = Util.getRCSetIndexForFieldValue(DOMData.mGenreData, lNames, lValues);
         if (lIndex < 0) {
            return;
         }

         RDataSetFieldValues lFieldValues = DOMData.mGenreData.getRow(lIndex);
         lGenreItem.setGenreId(Integer.parseInt(aGenreId));
         lGenreItem.setPriority(Integer.parseInt(lFieldValues.getValue(DOMData.mGenreData.getFieldIndex("Priority"))));
         lGenreItem.setGenreName(lFieldValues.getValue(DOMData.mGenreData.getFieldIndex("Genre")));

         for (int j = 0; j < mGenreList.size(); j++) {
            if (lGenreItem.compareTo((GenreItem)mGenreList.get(j)) == -1) {
               mGenreList.add(j, lGenreItem);
               return;
            }
         }

         mGenreList.add(lGenreItem);
      } catch (DvdplayException var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var7.getMessage());
         Aem.setDataError();
      }
   }

   public static GenreItem getGenre(int aIndex) {
      return aIndex >= 0 && aIndex < mGenreList.size() ? (GenreItem)mGenreList.get(aIndex) : null;
   }

   public static PricingItem getPricing(int aPriceOptionId) {
      return getPricing(aPriceOptionId, new Date());
   }

   public static PricingItem getPricing(int aPriceOptionId, Date aDate) throws InventoryException {
      int lIndex = -1;
      Date lDate;
      if (aDate == null) {
         lDate = new Date();
      } else {
         lDate = aDate;
      }

      RDataSetFieldValues lValues = null;
      PricingItem lPricingItem = new PricingItem();
      Calendar lCalendar = Calendar.getInstance();
      lCalendar.setTime(lDate);
      RCSet lTmpRCSet = null;

      try {
         for (int i = 0; i < DOMData.mSpecialPricingData.rowCount(); i++) {
            if (!DOMData.mSpecialPricingData.isDeleted(i)
               && DOMData.mSpecialPricingData
                  .getFieldValue(i, DOMData.mSpecialPricingData.getFieldIndex("PriceOptionId"))
                  .equals(Integer.toString(aPriceOptionId))) {
               lValues = DOMData.mSpecialPricingData.getRow(i);
               Calendar lStartCal = Calendar.getInstance(Aem.getLocale());
               lStartCal.setTime(Util.stringToDate(lValues.getValue(DOMData.mSpecialPricingData.getFieldIndex("StartDate"))));
               Calendar lEndCal = Calendar.getInstance(Aem.getLocale());
               lEndCal.setTime(Util.stringToDate(lValues.getValue(DOMData.mSpecialPricingData.getFieldIndex("EndDate"))));
               Calendar lNow = Calendar.getInstance(Aem.getLocale());
               lNow.setTime(lDate);
               if (lNow.after(lStartCal) && lNow.before(lEndCal)) {
                  lPricingItem.setSpecialPricing();
                  lTmpRCSet = DOMData.mSpecialPricingData;
                  break;
               }
            }
         }

         if (lTmpRCSet == null) {
            String[] lNames = new String[2];
            String[] ltmpValues = new String[2];
            lNames[0] = "PriceOptionId";
            lNames[1] = "DayOfTheWeek";
            ltmpValues[0] = Integer.toString(aPriceOptionId);
            ltmpValues[1] = String.valueOf(lCalendar.get(7));
            lIndex = Util.getRCSetIndexForFieldValue(DOMData.mRegularPricingData, lNames, ltmpValues);
            if (lIndex < 0) {
               Aem.setDataError();
               throw new InventoryException("PriceOptionId " + aPriceOptionId + " not found.");
            }

            lValues = DOMData.mRegularPricingData.getRow(lIndex);
            lTmpRCSet = DOMData.mRegularPricingData;
         }

         lPricingItem.setPriceOptionId(aPriceOptionId);
         lPricingItem.setPriceModelId(Integer.parseInt(lValues.getValue(lTmpRCSet.getFieldIndex("PriceModelId"))));
         lPricingItem.setNewPrice(new BigDecimal(lValues.getValue(lTmpRCSet.getFieldIndex("NewPrice"))));
         lPricingItem.setUsedPrice(new BigDecimal(lValues.getValue(lTmpRCSet.getFieldIndex("UsedPrice"))));
         lPricingItem.setRentalPrice(new BigDecimal(lValues.getValue(lTmpRCSet.getFieldIndex("RentalPrice"))));
         lPricingItem.setLateRentalPrice(new BigDecimal(lValues.getValue(lTmpRCSet.getFieldIndex("LateRentalPrice"))));
         lPricingItem.setRentalDays(Integer.parseInt(lValues.getValue(lTmpRCSet.getFieldIndex("RentalDays"))));
         lPricingItem.setLateDays(Integer.parseInt(lValues.getValue(lTmpRCSet.getFieldIndex("LateDays"))));
         return lPricingItem;
      } catch (DvdplayException var12) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "getPricing: " + var12.getMessage());
         throw new InventoryException("getPricing error");
      }
   }

   public static synchronized boolean isCarouselFull() {
      try {
         getNextEmptySlot();
         return false;
      } catch (DvdplayException var1) {
         return true;
      }
   }

   public static synchronized int getNextEmptySlot() throws InventoryException {
      Servo lServo = ServoFactory.getInstance();
      int[] lBayArray = new int[lServo.getNumUnits()];
      int lBayNum = 0;

      for (int i = 0; i < lServo.getNumSlots(); i++) {
         if (mSlotIndex.getSlot(i + 1) != 0) {
            lBayArray[i / lServo.getSlotsPerUnit()]++;
         }
      }

      for (int ix = 0; ix < lServo.getNumUnits(); ix++) {
         if (lBayArray[lBayNum] > lBayArray[ix]) {
            lBayNum = ix;
         }
      }

      for (int ixx = lBayNum * lServo.getSlotsPerUnit() + 1; ixx <= (lBayNum + 1) * lServo.getSlotsPerUnit(); ixx++) {
         if (mSlotIndex.getSlot(ixx) == 0) {
            return ixx;
         }
      }

      throw new InventoryException("Carousel is full");
   }

   public static synchronized void addSlotIndex(int aSlotNum, int aDiscDetailId) {
      mSlotIndex.addSlot(aSlotNum, aDiscDetailId);
   }

   public static synchronized DiscIndex getDiscIndex() {
      return mDiscIndex;
   }

   public static synchronized void addUnknownDisc(String aGroupCode, String aDiscCode, int aSlotNum) {
      addUnknownDisc(aGroupCode, aDiscCode, aSlotNum, false);
   }

   public static synchronized void addUnknownDisc(String aGroupCode, String aDiscCode, int aSlotNum, boolean aIsFound) {
      String[] lValues = new String[DvdplayBase.DISC_DETAIL_FIELD_NAMES.length];
      RCSet lRCSet = new RCSet(DvdplayBase.DISC_DETAIL_FIELD_NAMES, DvdplayBase.DISC_DETAIL_FIELD_TYPES);
      lValues[DOMData.mDiscDetailData.getFieldIndex("DiscDetailId")] = Integer.toString(Aem.getNextIndex() * -1);
      lValues[DOMData.mDiscDetailData.getFieldIndex("TitleDetailId")] = "0";
      lValues[DOMData.mDiscDetailData.getFieldIndex("DiscStatusId")] = Integer.toString(9);
      lValues[DOMData.mDiscDetailData.getFieldIndex("PriceOptionId")] = "0";
      lValues[DOMData.mDiscDetailData.getFieldIndex("GroupCode")] = aGroupCode;
      lValues[DOMData.mDiscDetailData.getFieldIndex("DiscCode")] = aDiscCode;
      lValues[DOMData.mDiscDetailData.getFieldIndex("Slot")] = Integer.toString(aSlotNum);
      lValues[DOMData.mDiscDetailData.getFieldIndex("Priority")] = "9999";
      lValues[DOMData.mDiscDetailData.getFieldIndex("MarkedForSale")] = Integer.toString(0);
      lValues[DOMData.mDiscDetailData.getFieldIndex("MarkedForRent")] = Integer.toString(0);
      lValues[DOMData.mDiscDetailData.getFieldIndex("MarkedForRemoval")] = Integer.toString(0);
      lValues[DOMData.mDiscDetailData.getFieldIndex("RemovalDate")] = "";
      lValues[DOMData.mDiscDetailData.getFieldIndex("DTUpdated")] = Util.dateToString(new Date());
      lRCSet.addRow(lValues);
      if (aIsFound) {
         updateDisc(lRCSet);
      } else {
         updateUnknownDisc(lRCSet);
      }
   }

   public static synchronized int getDiscCount() {
      return mSlotIndex.getDiscCount();
   }

   public static synchronized void addTempBadSlot(int aBadSlotNum) {
      Aem.logDetailMessage(DvdplayLevel.WARNING, "Setting slot " + aBadSlotNum + " as temporary bad slot");
      mSlotIndex.addBadSlot(aBadSlotNum);
   }

   public static synchronized void addBadSlot(int aBadSlotNum, int aBadSlotId) {
      Servo lServo = ServoFactory.getInstance();
      if (aBadSlotNum > 0 && aBadSlotNum <= lServo.getNumSlots()) {
         RCSet lRCSet = new RCSet(DvdplayBase.BAD_SLOT_FIELD_NAMES, DvdplayBase.BAD_SLOT_FIELD_TYPES);
         String[] lValues = new String[DvdplayBase.BAD_SLOT_FIELD_NAMES.length];
         lValues[DOMData.mBadSlotData.getFieldIndex("BadSlot")] = Integer.toString(aBadSlotNum);
         lValues[DOMData.mBadSlotData.getFieldIndex("BadSlotId")] = Integer.toString(aBadSlotId);
         lRCSet.addRow(lValues);
         removeBadSlot(aBadSlotNum);
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Setting slot " + aBadSlotNum + " as bad slot");
         mSlotIndex.addBadSlot(aBadSlotNum);
         DOMData.mBadSlotData.addRow(lRCSet.getRow(0));
      }
   }

   public static synchronized void removeBadSlot(int aBadSlotNum) {
      Servo lServo = ServoFactory.getInstance();
      if (aBadSlotNum > 0 && aBadSlotNum <= lServo.getNumSlots()) {
         for (int i = 0; i < DOMData.mBadSlotData.rowCount(); i++) {
            if (!DOMData.mBadSlotData.isDeleted(i)
               && Integer.parseInt(DOMData.mBadSlotData.getFieldValue(i, DOMData.mBadSlotData.getFieldIndex("BadSlot"))) == aBadSlotNum) {
               DOMData.mBadSlotData.deleteRow(i);
            }
         }

         mSlotIndex.removeBadSlot(aBadSlotNum);
      }
   }

   public static synchronized void addBadSlotsToSlotIndex() {
      for (int i = 0; i < DOMData.mBadSlotData.rowCount(); i++) {
         if (!DOMData.mBadSlotData.isDeleted(i)) {
            int lBadSlotNum = Integer.parseInt(DOMData.mBadSlotData.getFieldValue(i, DOMData.mBadSlotData.getFieldIndex("BadSlot")));
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Setting slot " + lBadSlotNum + " as bad slot");
            mSlotIndex.addBadSlot(lBadSlotNum);
         }
      }
   }

   public static void createStaticPlayList() {
      Aem.logSummaryMessage("Create StaticPlayList ...");
      mStaticPlayList = new PlayList();

      for (int i = 0; i < DOMData.mStaticPlaylistData.rowCount(); i++) {
         try {
            if (!DOMData.mStaticPlaylistData.isDeleted(i)) {
               String lMediaId = DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("MediaId"));
               String lMediaTypeId = DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("MediaTypeId"));
               String[] lNames = new String[2];
               String[] lValues = new String[2];
               lNames[0] = "MediaId";
               lValues[0] = lMediaId;
               lNames[1] = "MediaTypeId";
               lValues[1] = lMediaTypeId;
               int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
               if (lIndex >= 0) {
                  boolean lIsDownloaded;
                  if (Integer.parseInt(DOMData.mMediaDetailData.getFieldValue(lIndex, DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"))) == 1) {
                     lIsDownloaded = true;
                  } else {
                     lIsDownloaded = false;
                  }

                  if (lIsDownloaded) {
                     Date lStartDate = Util.stringToDate(
                        DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("StartDate")), "yyyy-MM-dd HH:mm:ss.SSS"
                     );
                     Date lEndDate = Util.stringToDate(
                        DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("EndDate")), "yyyy-MM-dd HH:mm:ss.SSS"
                     );
                     if (Util.isDateBeforeNow(lStartDate) && Util.isDateAfterNow(lEndDate)) {
                        String lFilename = DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("Filename"));
                        File lFile = new File("c:\\aem\\content\\ads\\" + lFilename);
                        if (!lFile.exists()) {
                           Aem.logDetailMessage(DvdplayLevel.WARNING, "Cannot find c:\\aem\\content\\ads\\" + lFilename);
                           resetMediaDetailImage(lIndex);
                        } else {
                           int lPriority = Integer.parseInt(DOMData.mStaticPlaylistData.getFieldValue(i, DOMData.mStaticPlaylistData.getFieldIndex("Priority")));
                           mStaticPlayList.addPlayListItem(new PlayListItem(lPriority, "c:\\aem\\content\\ads\\" + lFilename));
                        }
                     }
                  }
               }
            }
         } catch (Exception var12) {
            Aem.setDataError();
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Exception caught: " + var12.getMessage(), var12);
         }
      }
   }

   public static void resetMediaDetailImage(int aIndex) {
      RDataSetFieldValues lRDSFV = DOMData.mMediaDetailData.getRow(aIndex);
      lRDSFV.setValue(DOMData.mMediaDetailData.getFieldIndex("ImageDownloaded"), Integer.toString(0));
      DOMData.mMediaDetailData.addRow(lRDSFV);
      DOMData.mMediaDetailData.deleteRow(aIndex);
      DOMData.save();
   }

   public static void resetMediaDetailMedia(int aIndex) {
      RDataSetFieldValues lRDSFV = DOMData.mMediaDetailData.getRow(aIndex);
      lRDSFV.setValue(DOMData.mMediaDetailData.getFieldIndex("NumDownloaded"), "0");
      lRDSFV.setValue(DOMData.mMediaDetailData.getFieldIndex("MediaDownloaded"), Integer.toString(0));
      DOMData.mMediaDetailData.addRow(lRDSFV);
      DOMData.mMediaDetailData.deleteRow(aIndex);
      DOMData.save();
   }

   public static void createMediaPlayList() {
      Aem.logSummaryMessage("Create MediaPlayList ...");
      mMediaPlayList = new PlayList();

      for (int i = 0; i < DOMData.mMediaPlaylistData.rowCount(); i++) {
         try {
            if (!DOMData.mMediaPlaylistData.isDeleted(i)) {
               String lMediaId = DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("MediaId"));
               String lMediaTypeId = DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("MediaTypeId"));
               String[] lNames = new String[2];
               String[] lValues = new String[2];
               lNames[0] = "MediaId";
               lValues[0] = lMediaId;
               lNames[1] = "MediaTypeId";
               lValues[1] = lMediaTypeId;
               int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mMediaDetailData, lNames, lValues);
               if (lIndex >= 0) {
                  boolean lIsDownloaded;
                  if (Integer.parseInt(DOMData.mMediaDetailData.getFieldValue(lIndex, DOMData.mMediaDetailData.getFieldIndex("MediaDownloaded"))) == 1) {
                     lIsDownloaded = true;
                  } else {
                     lIsDownloaded = false;
                  }

                  if (lIsDownloaded) {
                     Date lStartDate = Util.stringToDate(
                        DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("StartDate")), "yyyy-MM-dd HH:mm:ss.SSS"
                     );
                     Date lEndDate = Util.stringToDate(
                        DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("EndDate")), "yyyy-MM-dd HH:mm:ss.SSS"
                     );
                     if (Util.isDateBeforeNow(lStartDate) && Util.isDateAfterNow(lEndDate)) {
                        String lFilename = DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("Filename"));
                        long lFileSize = Integer.parseInt(DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("FileSize")));
                        File lFile = new File("c:\\aem\\content\\trailers\\" + lFilename);
                        if (!lFile.exists()) {
                           Aem.logDetailMessage(DvdplayLevel.WARNING, "Cannot find c:\\aem\\content\\trailers\\" + lFilename);
                           resetMediaDetailMedia(lIndex);
                        } else {
                           long lActualFileSize = lFile.length();
                           if (lActualFileSize != lFileSize) {
                              Aem.logDetailMessage(
                                 DvdplayLevel.WARNING,
                                 "c:\\aem\\content\\trailers\\"
                                    + lFilename
                                    + " filesize does not match. Expected "
                                    + lFileSize
                                    + " . "
                                    + "Found "
                                    + lActualFileSize
                              );
                           } else {
                              int lPriority = Integer.parseInt(
                                 DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("Priority"))
                              );
                              boolean lIsTitle;
                              if (Integer.parseInt(lMediaTypeId) == 1) {
                                 lIsTitle = true;
                              } else {
                                 lIsTitle = false;
                              }

                              if (lIsTitle) {
                                 Date lStreetDate = Util.stringToDate(
                                    DOMData.mMediaPlaylistData.getFieldValue(i, DOMData.mMediaPlaylistData.getFieldIndex("Attr1")), "yyyy-MM-dd HH:mm:ss.SSS"
                                 );
                                 if (Util.isDateBeforeNow(lStreetDate)) {
                                    mMediaPlayList.addPlayListItem(
                                       new PlayListItem(lPriority, "c:\\aem\\content\\trailers\\" + lFilename, true, 1, Integer.parseInt(lMediaId))
                                    );
                                 } else {
                                    mMediaPlayList.addPlayListItem(
                                       new PlayListItem(lPriority, "c:\\aem\\content\\trailers\\" + lFilename, false, 2, Integer.parseInt(lMediaId))
                                    );
                                 }
                              } else {
                                 mMediaPlayList.addPlayListItem(
                                    new PlayListItem(lPriority, "c:\\aem\\content\\trailers\\" + lFilename, false, 0, Integer.parseInt(lMediaId))
                                 );
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (Exception var19) {
            Aem.setDataError();
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Exception caught: " + var19.getMessage(), var19);
         }
      }
   }

   public static PlayListItem getNextMediaPlayListItem() {
      return mMediaPlayList.getNextPlayListItem();
   }

   public static PlayListItem getNextStaticPlayListItem() {
      return mStaticPlayList.getNextPlayListItem();
   }

   private static void createLocaleIndex() {
      String lCurrentField = "";
      mLocaleIndex = new LocaleIndex();

      try {
         for (int i = 0; i < DOMData.mLocaleData.rowCount(); i++) {
            if (!DOMData.mLocaleData.isDeleted(i)) {
               lCurrentField = "LocaleId";
               String lLocaleId = DOMData.mLocaleData.getFieldValue(i, DOMData.mLocaleData.getFieldIndex("LocaleId"));
               lCurrentField = "Country";
               String lCountry = DOMData.mLocaleData.getFieldValue(i, DOMData.mLocaleData.getFieldIndex("Country"));
               lCurrentField = "Language";
               String lLanguage = DOMData.mLocaleData.getFieldValue(i, DOMData.mLocaleData.getFieldIndex("Language"));
               lCurrentField = "";
               LocaleIndex.addLocaleIndexItem(new LocaleIndexItem(Integer.parseInt(lLocaleId), lLanguage, lCountry));
            }
         }
      } catch (Exception var5) {
         if (!lCurrentField.equals("")) {
            Aem.logDetailMessage(DvdplayLevel.SEVERE, "Error reading in field " + lCurrentField);
            Aem.logSummaryMessage("Error reading in field " + lCurrentField);
         }

         Aem.logDetailMessage(DvdplayLevel.SEVERE, "Exception caught: " + var5.getMessage());
         Aem.logSummaryMessage("Exception caught: " + var5.getMessage());
         throw new DvdplayException("Create LocaleIndex failed");
      }
   }

   public static synchronized LocaleIndexItem getLocaleIndexItem(int aIndex) {
      return LocaleIndex.getLocaleIndexItem(aIndex);
   }

   public static synchronized LocaleIndexItem getLocaleIndexItemByLocaleId(int aLocaleId) {
      return LocaleIndex.getLocaleIndexItemByLocaleId(aLocaleId);
   }

   public static synchronized int getLocaleIndexSize() {
      return LocaleIndex.size();
   }

   private static synchronized void createTitleTypeIndex() {
      String lCurrentField = "";
      mTitleTypeIndex = new TitleTypeIndex();

      for (int i = 0; i < DOMData.mTitleTypeData.rowCount(); i++) {
         if (!DOMData.mTitleTypeData.isDeleted(i)) {
            try {
               lCurrentField = "TitleTypeId";
               String lTitleTypeId = DOMData.mTitleTypeData.getFieldValue(i, DOMData.mTitleTypeData.getFieldIndex("TitleTypeId"));
               lCurrentField = "LocaleId";
               String lLocaleId = DOMData.mTitleTypeData.getFieldValue(i, DOMData.mTitleTypeData.getFieldIndex("LocaleId"));
               lCurrentField = "Priority";
               String lPriority = DOMData.mTitleTypeData.getFieldValue(i, DOMData.mTitleTypeData.getFieldIndex("Priority"));
               lCurrentField = "TitleType";
               String lTitleType = DOMData.mTitleTypeData.getFieldValue(i, DOMData.mTitleTypeData.getFieldIndex("TitleType"));
               lCurrentField = "TitleTypeSingular";
               String lTitleTypeSingular = DOMData.mTitleTypeData.getFieldValue(i, DOMData.mTitleTypeData.getFieldIndex("TitleTypeSingular"));
               lCurrentField = "";
               String[] lNames = new String[2];
               String[] lValues = new String[2];
               lNames[0] = "TitleTypeId";
               lNames[1] = "CapTypeId";
               lValues[0] = lTitleTypeId;
               lValues[1] = Integer.toString(1);
               int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mTitleTypeCapData, lNames, lValues);
               int lFirstTimeMax;
               if (lIndex < 0) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, "Could not find TitleTypeId " + lTitleTypeId + " and TitleTypeCapTypeId " + 1 + " in TitleTypeCap");
                  lFirstTimeMax = 0;
               } else {
                  lFirstTimeMax = new BigDecimal(DOMData.mTitleTypeCapData.getFieldValue(lIndex, DOMData.mTitleTypeCapData.getFieldIndex("Value")))
                     .toBigInteger()
                     .intValue();
               }

               lNames = new String[2];
               lValues = new String[2];
               lNames[0] = "TitleTypeId";
               lNames[1] = "CapTypeId";
               lValues[0] = lTitleTypeId;
               lValues[1] = Integer.toString(2);
               lIndex = Util.getRCSetIndexForFieldValue(DOMData.mTitleTypeCapData, lNames, lValues);
               int lRegularUserMax;
               if (lIndex < 0) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, "Could not find TitleTypeId " + lTitleTypeId + " and TitleTypeCapTypeId " + 2 + " in TitleTypeCap");
                  lRegularUserMax = 0;
               } else {
                  lRegularUserMax = new BigDecimal(DOMData.mTitleTypeCapData.getFieldValue(lIndex, DOMData.mTitleTypeCapData.getFieldIndex("Value")))
                     .toBigInteger()
                     .intValue();
               }

               lNames = new String[2];
               lValues = new String[2];
               lNames[0] = "TitleTypeId";
               lNames[1] = "CapTypeId";
               lValues[0] = lTitleTypeId;
               lValues[1] = Integer.toString(3);
               lIndex = Util.getRCSetIndexForFieldValue(DOMData.mTitleTypeCapData, lNames, lValues);
               BigDecimal lMaxCharge;
               if (lIndex < 0) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, "Could not find TitleTypeId " + lTitleTypeId + " and TitleTypeCapTypeId " + 3 + " in TitleTypeCap");
                  lMaxCharge = new BigDecimal(0.0);
               } else {
                  lMaxCharge = new BigDecimal(DOMData.mTitleTypeCapData.getFieldValue(lIndex, DOMData.mTitleTypeCapData.getFieldIndex("Value")));
               }

               TitleTypeIndex.addTitleTypeIndexItem(
                  new TitleTypeIndexItem(
                     Integer.parseInt(lTitleTypeId),
                     Integer.parseInt(lLocaleId),
                     Integer.parseInt(lPriority),
                     lTitleType,
                     lTitleTypeSingular,
                     lFirstTimeMax,
                     lRegularUserMax,
                     lMaxCharge
                  )
               );
            } catch (Exception var14) {
               if (!lCurrentField.equals("")) {
                  Aem.logDetailMessage(DvdplayLevel.SEVERE, "Error reading in field " + lCurrentField);
                  Aem.logSummaryMessage("Error reading in field " + lCurrentField);
               }

               Aem.logDetailMessage(DvdplayLevel.SEVERE, "createTitleTypeIndex: " + var14.getMessage());
               Aem.logSummaryMessage("createTitleTypeIndex: " + var14.getMessage());
               Aem.setDataError();
               throw new DvdplayException("Create TitleTypeIndex failed");
            }
         }
      }
   }

   public static int createTitleTypeList() {
      mTitleTypeList = new ArrayList();

      for (int i = 0; i < TitleTypeIndex.size(); i++) {
         try {
            TitleTypeIndexItem lTitleTypeIndexItem = TitleTypeIndex.getTitleTypeIndexItem(i);
            if (lTitleTypeIndexItem.getLocaleId() == Aem.getLocaleId()) {
               mTitleTypeList.add(lTitleTypeIndexItem);
            }
         } catch (DvdplayException var3) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "createTitleTypeList: " + var3.getMessage());
            Aem.setDataError();
         }
      }

      return mTitleTypeList.size();
   }

   public static TitleTypeIndexItem getTitleTypeIndexItem(int aIndex) {
      return aIndex >= 0 && aIndex < mTitleTypeList.size() ? (TitleTypeIndexItem)mTitleTypeList.get(aIndex) : null;
   }

   public static TitleTypeIndexItem getTitleTypeIndexItemByTitleTypeId(int aTitleTypeId) throws InventoryException {
      TitleTypeIndexItem a = null;

      for (int i = 0; i < mTitleTypeList.size(); i++) {
         a = (TitleTypeIndexItem)mTitleTypeList.get(i);
         if (a.getTitleTypeId() == aTitleTypeId && a.getLocaleId() == Aem.getLocaleId()) {
            return a;
         }
      }

      throw new InventoryException("Could not find TitleTypeId " + aTitleTypeId + " in TitleTypeIndex");
   }

   private static synchronized void createPaymentCardTypeIndex() {
      String lCurrentField = "";
      mPaymentCardTypeIndex = new PaymentCardTypeIndex();

      for (int i = 0; i < DOMData.mPaymentCardTypeData.rowCount(); i++) {
         if (!DOMData.mPaymentCardTypeData.isDeleted(i)) {
            try {
               lCurrentField = "PaymentCardTypeId";
               String lPaymentCardTypeId = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("PaymentCardTypeId"));
               lCurrentField = "VerificationTypeId";
               String lVerificationTypeId = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("VerificationTypeId"));
               lCurrentField = "Priority";
               String lPriority = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("Priority"));
               lCurrentField = "PaymentPicture";
               String lPaymentPicture = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("PaymentPicture"));
               lCurrentField = "PaymentCardTypeName";
               String lPaymentCardTypeName = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("PaymentCardTypeName"));
               lCurrentField = "LocaleId";
               String lLocaleId = DOMData.mPaymentCardTypeData.getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("LocaleId"));
               lCurrentField = "PaymentCardCategoryId";
               String lPaymentCardCategoryId = DOMData.mPaymentCardTypeData
                  .getFieldValue(i, DOMData.mPaymentCardTypeData.getFieldIndex("PaymentCardCategoryId"));
               lCurrentField = "";
               PaymentCardTypeIndex.addPaymentCardTypeIndexItem(
                  new PaymentCardTypeIndexItem(
                     Integer.parseInt(lPaymentCardTypeId),
                     Integer.parseInt(lVerificationTypeId),
                     Integer.parseInt(lPriority),
                     lPaymentPicture,
                     lPaymentCardTypeName,
                     Integer.parseInt(lLocaleId),
                     Integer.parseInt(lPaymentCardCategoryId)
                  )
               );
            } catch (Exception var10) {
               if (!lCurrentField.equals("")) {
                  Aem.logDetailMessage(DvdplayLevel.SEVERE, "Error reading in field " + lCurrentField);
                  Aem.logSummaryMessage("Error reading in field " + lCurrentField);
               }

               Aem.logDetailMessage(DvdplayLevel.SEVERE, "createPaymentCardTypeIndex: " + var10.getMessage());
               Aem.logSummaryMessage("createPaymentCardTypeIndex: " + var10.getMessage());
               Aem.setDataError();
               throw new DvdplayException("Create PaymentCardTypeIndex failed");
            }
         }
      }
   }

   public static int createPaymentCardTypeList() {
      mPaymentCardTypeList = new ArrayList();

      for (int i = 0; i < PaymentCardTypeIndex.size(); i++) {
         try {
            PaymentCardTypeIndexItem lPaymentCardTypeIndexItem = PaymentCardTypeIndex.getPaymentCardTypeIndexItem(i);
            if (lPaymentCardTypeIndexItem.getLocaleId() == Aem.getLocaleId()) {
               mPaymentCardTypeList.add(lPaymentCardTypeIndexItem);
            }
         } catch (DvdplayException var3) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "createPaymentCardTypeList: " + var3.getMessage());
            Aem.setDataError();
         }
      }

      return mPaymentCardTypeList.size();
   }

   public static PaymentCardTypeIndexItem getPaymentCardTypeIndexItem(int aIndex) {
      return aIndex >= 0 && aIndex < mPaymentCardTypeList.size() ? (PaymentCardTypeIndexItem)mPaymentCardTypeList.get(aIndex) : null;
   }

   public static PaymentCardTypeIndexItem getPaymentCardTypeIndexItemByPaymentCardTypeId(int aPaymentCardTypeId) {
      for (int i = 0; i < mPaymentCardTypeList.size(); i++) {
         PaymentCardTypeIndexItem lPaymentCardTypeIndexItem = (PaymentCardTypeIndexItem)mPaymentCardTypeList.get(i);
         if (lPaymentCardTypeIndexItem.getPaymentCardTypeId() == aPaymentCardTypeId) {
            return lPaymentCardTypeIndexItem;
         }
      }

      throw new DvdplayException("PaymentCardTypeId " + aPaymentCardTypeId + " not found in PaymentCardTypeList");
   }

   public static void incDispenseFailureCount(int aDiscDetailId) {
      Aem.logDetailMessage(DvdplayLevel.FINE, "Incrementing DispenseFailureCount for DiscDetailId " + aDiscDetailId);
      DiscIndex.incDispenseFailureCount(aDiscDetailId);
   }

   private static synchronized void createPollIndex() {
      mPollIndex = PollIndex.createPollIndex();
   }

   public static int createPollList() {
      return mPollIndex.createPollList();
   }

   public static PollItem getPollIndexItem(int aIndex) {
      return mPollIndex.getActivePollIndexItem(aIndex);
   }

   public static void clearPollResponses() {
      mPollIndex.clearPollResponses();
   }
}
