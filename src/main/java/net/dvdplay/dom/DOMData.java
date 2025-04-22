package net.dvdplay.dom;

import java.io.File;
import java.util.logging.Level;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class DOMData {
   public static PersistenceData mDiscDetailData;
   public static PersistenceData mTitleDetailData;
   public static PersistenceData mRegularPricingData;
   public static PersistenceData mGenreData;
   public static PersistenceData mIndexData;
   public static PersistenceData mSlotOffsetData;
   public static PersistenceData mAemPropertiesData;
   public static PersistenceDataBackup mAemPropertiesData2;
   public static PersistenceData mBadSlotData;
   public static PersistenceData mMediaPlaylistData;
   public static PersistenceData mMediaDetailData;
   public static PersistenceData mStaticPlaylistData;
   public static PersistenceData mTranslationData;
   public static PersistenceData mLocaleData;
   public static PersistenceData mRatingSystemData;
   public static PersistenceData mRatingData;
   public static PersistenceData mCheckSumData;
   public static PersistenceData mCheckSumData2;
   public static PersistenceData mSpecialPricingData;
   public static PersistenceData mGroupCodeData;
   public static PersistenceData mTitleTypeData;
   public static PersistenceData mTitleTypeCapData;
   public static PersistenceData mPaymentCardTypeData;
   public static PersistenceData mSecurityData;
   public static PersistenceData mPrivilegesData;
   public static PersistenceData mPollData;

   public DOMData(String aArgs, Aem aAem, boolean aIsReset) {
      File lTmpFile = new File("c:\\windows\\system32\\var\\pdata\\ChckSm.dll");
      DataPacketComposer lComposer = new DataPacketComposer();

      try {
         if (!lTmpFile.exists()) {
            throw new DvdplayException("CheckSum not found");
         }

         mCheckSumData = new PersistenceData(
            DvdplayBase.CHECK_SUM_FIELD_NAMES,
            DvdplayBase.CHECK_SUM_FIELD_TYPES,
            "ChckSm.dll",
            "CheckSum",
            lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\ChckSm.dll")),
            false
         );
      } catch (Throwable var10) {
         mCheckSumData = new PersistenceData(DvdplayBase.CHECK_SUM_FIELD_NAMES, DvdplayBase.CHECK_SUM_FIELD_TYPES, "ChckSm.dll", "CheckSum", false);
      }

      lTmpFile = new File("c:\\windows\\system32\\var\\pdata\\ChckSm2.dll");

      try {
         if (!lTmpFile.exists()) {
            throw new DvdplayException("CheckSum2 not found");
         }

         mCheckSumData2 = new PersistenceData(
            DvdplayBase.CHECK_SUM_FIELD_NAMES,
            DvdplayBase.CHECK_SUM_FIELD_TYPES,
            "ChckSm2.dll",
            "CheckSum2",
            lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\ChckSm2.dll")),
            false
         );
      } catch (Throwable var9) {
         mCheckSumData2 = new PersistenceData(DvdplayBase.CHECK_SUM_FIELD_NAMES, DvdplayBase.CHECK_SUM_FIELD_TYPES, "ChckSm2.dll", "CheckSum2", false);
      }

      if (aArgs == null) {
         mAemPropertiesData = new PersistenceData(
            DvdplayBase.AEM_PROPERTIES_FIELD_NAMES, DvdplayBase.AEM_PROPERTIES_FIELD_TYPES, "AmPrprts.dll", "AemProperties"
         );
         mAemPropertiesData2 = new PersistenceDataBackup(
            DvdplayBase.AEM_PROPERTIES_FIELD_NAMES, DvdplayBase.AEM_PROPERTIES_FIELD_TYPES, "AmPrprts2.dll", "AemProperties2", mAemPropertiesData
         );
         mDiscDetailData = new PersistenceData(DvdplayBase.DISC_DETAIL_FIELD_NAMES, DvdplayBase.DISC_DETAIL_FIELD_TYPES, "DscDtl.dll", "DiscDetail");
         mTitleDetailData = new PersistenceData(DvdplayBase.TITLE_DETAIL_FIELD_NAMES, DvdplayBase.TITLE_DETAIL_FIELD_TYPES, "TtlDtl.dll", "TitleDetail");
         mRegularPricingData = new PersistenceData(
            DvdplayBase.REGULAR_PRICING_FIELD_NAMES, DvdplayBase.REGULAR_PRICING_FIELD_TYPES, "RglrPrcng.dll", "RegularPricing"
         );
         mSpecialPricingData = new PersistenceData(
            DvdplayBase.SPECIAL_PRICING_FIELD_NAMES, DvdplayBase.SPECIAL_PRICING_FIELD_TYPES, "SpclPrcng.dll", "SpecialPricing"
         );
         mGenreData = new PersistenceData(DvdplayBase.GENRE_FIELD_NAMES, DvdplayBase.GENRE_FIELD_TYPES, "Gnr.dll", "Genre");
         mMediaPlaylistData = new PersistenceData(
            DvdplayBase.MEDIA_PLAYLIST_FIELD_NAMES, DvdplayBase.MEDIA_PLAYLIST_FIELD_TYPES, "MdPlylst.dll", "MediaPlaylist"
         );
         mStaticPlaylistData = new PersistenceData(
            DvdplayBase.STATIC_PLAYLIST_FIELD_NAMES, DvdplayBase.STATIC_PLAYLIST_FIELD_TYPES, "SttcPlylst.dll", "StaticPlaylist"
         );
         mTranslationData = new PersistenceData(DvdplayBase.TRANSLATION_FIELD_NAMES, DvdplayBase.TRANSLATION_FIELD_TYPES, "Trnsltn.dll", "Translation");
         mLocaleData = new PersistenceData(DvdplayBase.LOCALE_FIELD_NAMES, DvdplayBase.LOCALE_FIELD_TYPES, "Lcl.dll", "Locale");
         mRatingSystemData = new PersistenceData(DvdplayBase.RATING_SYSTEM_FIELD_NAMES, DvdplayBase.RATING_SYSTEM_FIELD_TYPES, "RtngSystm.dll", "RatingSystem");
         mRatingData = new PersistenceData(DvdplayBase.RATING_FIELD_NAMES, DvdplayBase.RATING_FIELD_TYPES, "Rtng.dll", "Rating");
         mGroupCodeData = new PersistenceData(DvdplayBase.GROUP_CODE_FIELD_NAMES, DvdplayBase.GROUP_CODE_FIELD_TYPES, "GrpCd.dll", "GroupCode");
         mTitleTypeData = new PersistenceData(DvdplayBase.TITLE_TYPE_FIELD_NAMES, DvdplayBase.TITLE_TYPE_FIELD_TYPES, "TtlTyp.dll", "TitleType");
         mTitleTypeCapData = new PersistenceData(DvdplayBase.TITLE_TYPE_CAP_FIELD_NAMES, DvdplayBase.TITLE_TYPE_CAP_FIELD_TYPES, "TtlTypCp.dll", "TitleTypeCap");
         mPaymentCardTypeData = new PersistenceData(
            DvdplayBase.PAYMENT_CARD_TYPE_FIELD_NAMES, DvdplayBase.PAYMENT_CARD_TYPE_FIELD_TYPES, "PymntCrdTyp.dll", "PaymentCardType"
         );
         mSecurityData = new PersistenceData(DvdplayBase.SECURITY_FIELD_NAMES, DvdplayBase.SECURITY_FIELD_TYPES, "Scrty.dll", "Security");
         mPrivilegesData = new PersistenceData(DvdplayBase.PRIVILEGES_FIELD_NAMES, DvdplayBase.PRIVILEGES_FIELD_TYPES, "Prvlgs.dll", "Privileges");
         mBadSlotData = new PersistenceData(DvdplayBase.BAD_SLOT_FIELD_NAMES, DvdplayBase.BAD_SLOT_FIELD_TYPES, "BdSlt.dll", "BadSlot");
         mSlotOffsetData = new PersistenceData(DvdplayBase.SLOT_OFFSET_FIELD_NAMES, DvdplayBase.SLOT_OFFSET_FIELD_TYPES, "SlOffst.dll", "SlotOffset");
         mIndexData = new PersistenceData(DvdplayBase.INDEX_FIELD_NAMES, DvdplayBase.INDEX_FIELD_TYPES, "Indx.dll", "Index");
         if (aIsReset) {
            mIndexData.touch();
         }

         mMediaDetailData = new PersistenceData(DvdplayBase.MEDIA_DETAIL_FIELD_NAMES, DvdplayBase.MEDIA_DETAIL_FIELD_TYPES, "MdDtl.dll", "MediaDetail");
         if (aIsReset) {
            mMediaDetailData.touch();
         }

         mPollData = new PersistenceData(DvdplayBase.POLL_FIELD_NAMES, DvdplayBase.POLL_FIELD_TYPES, "Pll.dll", "Poll");
         aAem.refreshAllData(false);
         save();
      } else {
         String lArgs = aArgs.trim().toUpperCase();

         for (int i = 0; i < lArgs.length(); i++) {
            String lChar = lArgs.substring(i, i + 1);
            if (lChar.equalsIgnoreCase("A")) {
               mAemPropertiesData = new PersistenceData(
                  DvdplayBase.AEM_PROPERTIES_FIELD_NAMES, DvdplayBase.AEM_PROPERTIES_FIELD_TYPES, "AmPrprts.dll", "AemProperties"
               );
               mAemPropertiesData2 = new PersistenceDataBackup(
                  DvdplayBase.AEM_PROPERTIES_FIELD_NAMES, DvdplayBase.AEM_PROPERTIES_FIELD_TYPES, "AmPrprts2.dll", "AemProperties2", mAemPropertiesData
               );
               Aem.refreshAemPropertiesData(false);
               mAemPropertiesData.save();
            } else if (lChar.equalsIgnoreCase("B")) {
               mDiscDetailData = new PersistenceData(DvdplayBase.DISC_DETAIL_FIELD_NAMES, DvdplayBase.DISC_DETAIL_FIELD_TYPES, "DscDtl.dll", "DiscDetail");
               Aem.refreshDiscDetailData();
               mDiscDetailData.save();
            } else if (lChar.equalsIgnoreCase("C")) {
               mTitleDetailData = new PersistenceData(DvdplayBase.TITLE_DETAIL_FIELD_NAMES, DvdplayBase.TITLE_DETAIL_FIELD_TYPES, "TtlDtl.dll", "TitleDetail");
               Aem.refreshTitleDetailData();
               mTitleDetailData.save();
            } else if (lChar.equalsIgnoreCase("D")) {
               mRegularPricingData = new PersistenceData(
                  DvdplayBase.REGULAR_PRICING_FIELD_NAMES, DvdplayBase.REGULAR_PRICING_FIELD_TYPES, "RglrPrcng.dll", "RegularPricing"
               );
               Aem.refreshRegularPricingData();
               mRegularPricingData.save();
            } else if (lChar.equalsIgnoreCase("E")) {
               mSpecialPricingData = new PersistenceData(
                  DvdplayBase.SPECIAL_PRICING_FIELD_NAMES, DvdplayBase.SPECIAL_PRICING_FIELD_TYPES, "SpclPrcng.dll", "SpecialPricing"
               );
               Aem.refreshSpecialPricingData();
               mSpecialPricingData.save();
            } else if (lChar.equalsIgnoreCase("F")) {
               mGenreData = new PersistenceData(DvdplayBase.GENRE_FIELD_NAMES, DvdplayBase.GENRE_FIELD_TYPES, "Gnr.dll", "Genre");
               Aem.refreshGenreData();
               mGenreData.save();
            } else if (lChar.equalsIgnoreCase("G")) {
               mMediaPlaylistData = new PersistenceData(
                  DvdplayBase.MEDIA_PLAYLIST_FIELD_NAMES, DvdplayBase.MEDIA_PLAYLIST_FIELD_TYPES, "MdPlylst.dll", "MediaPlaylist"
               );
               Aem.refreshMediaPlaylistData();
               mMediaPlaylistData.save();
            } else if (lChar.equalsIgnoreCase("H")) {
               mStaticPlaylistData = new PersistenceData(
                  DvdplayBase.STATIC_PLAYLIST_FIELD_NAMES, DvdplayBase.STATIC_PLAYLIST_FIELD_TYPES, "SttcPlylst.dll", "StaticPlaylist"
               );
               Aem.refreshStaticPlaylistData();
               mStaticPlaylistData.save();
            } else if (lChar.equalsIgnoreCase("I")) {
               mTranslationData = new PersistenceData(DvdplayBase.TRANSLATION_FIELD_NAMES, DvdplayBase.TRANSLATION_FIELD_TYPES, "Trnsltn.dll", "Translation");
               Aem.refreshTranslationData();
               mTranslationData.save();
            } else if (lChar.equalsIgnoreCase("J")) {
               mLocaleData = new PersistenceData(DvdplayBase.LOCALE_FIELD_NAMES, DvdplayBase.LOCALE_FIELD_TYPES, "Lcl.dll", "Locale");
               Aem.refreshLocaleData();
               mLocaleData.save();
            } else if (lChar.equalsIgnoreCase("K")) {
               mRatingSystemData = new PersistenceData(
                  DvdplayBase.RATING_SYSTEM_FIELD_NAMES, DvdplayBase.RATING_SYSTEM_FIELD_TYPES, "RtngSystm.dll", "RatingSystem"
               );
               Aem.refreshRatingSystemData();
               mRatingSystemData.save();
            } else if (lChar.equalsIgnoreCase("L")) {
               mRatingData = new PersistenceData(DvdplayBase.RATING_FIELD_NAMES, DvdplayBase.RATING_FIELD_TYPES, "Rtng.dll", "Rating");
               Aem.refreshRatingData();
               mRatingData.save();
            } else if (lChar.equalsIgnoreCase("M")) {
               mGroupCodeData = new PersistenceData(DvdplayBase.GROUP_CODE_FIELD_NAMES, DvdplayBase.GROUP_CODE_FIELD_TYPES, "GrpCd.dll", "GroupCode");
               Aem.refreshGroupCodeData();
               mGroupCodeData.save();
            } else if (lChar.equalsIgnoreCase("N")) {
               mTitleTypeData = new PersistenceData(DvdplayBase.TITLE_TYPE_FIELD_NAMES, DvdplayBase.TITLE_TYPE_FIELD_TYPES, "TtlTyp.dll", "TitleType");
               Aem.refreshTitleTypeData();
               mTitleTypeData.save();
            } else if (lChar.equalsIgnoreCase("O")) {
               mTitleTypeCapData = new PersistenceData(
                  DvdplayBase.TITLE_TYPE_CAP_FIELD_NAMES, DvdplayBase.TITLE_TYPE_CAP_FIELD_TYPES, "TtlTypCp.dll", "TitleTypeCap"
               );
               Aem.refreshTitleTypeCapData();
               mTitleTypeCapData.save();
            } else if (lChar.equalsIgnoreCase("P")) {
               mPaymentCardTypeData = new PersistenceData(
                  DvdplayBase.PAYMENT_CARD_TYPE_FIELD_NAMES, DvdplayBase.PAYMENT_CARD_TYPE_FIELD_TYPES, "PymntCrdTyp.dll", "PaymentCardType"
               );
               Aem.refreshPaymentCardData();
               mPaymentCardTypeData.save();
            } else if (lChar.equalsIgnoreCase("Q")) {
               mSecurityData = new PersistenceData(DvdplayBase.SECURITY_FIELD_NAMES, DvdplayBase.SECURITY_FIELD_TYPES, "Scrty.dll", "Security");
               Aem.refreshSecurityData();
               mSecurityData.save();
            } else if (lChar.equalsIgnoreCase("R")) {
               mPrivilegesData = new PersistenceData(DvdplayBase.PRIVILEGES_FIELD_NAMES, DvdplayBase.PRIVILEGES_FIELD_TYPES, "Prvlgs.dll", "Privileges");
               Aem.refreshPrivilegesData();
               mPrivilegesData.save();
            } else if (lChar.equalsIgnoreCase("S")) {
               mBadSlotData = new PersistenceData(DvdplayBase.BAD_SLOT_FIELD_NAMES, DvdplayBase.BAD_SLOT_FIELD_TYPES, "BdSlt.dll", "BadSlot");
               Aem.refreshBadSlotData();
               mBadSlotData.save();
            } else if (lChar.equalsIgnoreCase("T")) {
               mSlotOffsetData = new PersistenceData(DvdplayBase.SLOT_OFFSET_FIELD_NAMES, DvdplayBase.SLOT_OFFSET_FIELD_TYPES, "SlOffst.dll", "SlotOffset");
               Aem.refreshSlotOffsetData();
               mSlotOffsetData.save();
            } else if (lChar.equalsIgnoreCase("U")) {
               mPollData = new PersistenceData(DvdplayBase.POLL_FIELD_NAMES, DvdplayBase.POLL_FIELD_TYPES, "Pll.dll", "Poll");
               Aem.refreshPollData();
               mPollData.save();
            } else {
               System.out.println("Invalid option: " + lChar);
            }
         }

         mCheckSumData.save();
      }
   }

   public DOMData() {
      try {
         DataPacketComposer lComposer = new DataPacketComposer();

         try {
            mCheckSumData = new PersistenceData(
               DvdplayBase.CHECK_SUM_FIELD_NAMES,
               DvdplayBase.CHECK_SUM_FIELD_TYPES,
               "ChckSm.dll",
               "CheckSum",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\ChckSm.dll")),
               false
            );
         } catch (Exception var9) {
            mCheckSumData = new PersistenceData(DvdplayBase.CHECK_SUM_FIELD_NAMES, DvdplayBase.CHECK_SUM_FIELD_TYPES, "ChckSm.dll", "CheckSum", false);
         }

         try {
            mCheckSumData2 = new PersistenceData(
               DvdplayBase.CHECK_SUM_FIELD_NAMES,
               DvdplayBase.CHECK_SUM_FIELD_TYPES,
               "ChckSm2.dll",
               "CheckSum2",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\ChckSm2.dll")),
               false
            );
         } catch (Exception var8) {
            mCheckSumData2 = new PersistenceData(DvdplayBase.CHECK_SUM_FIELD_NAMES, DvdplayBase.CHECK_SUM_FIELD_TYPES, "ChckSm2.dll", "CheckSum2", false);
         }

         try {
            mAemPropertiesData = new PersistenceData(
               DvdplayBase.AEM_PROPERTIES_FIELD_NAMES,
               DvdplayBase.AEM_PROPERTIES_FIELD_TYPES,
               "AmPrprts.dll",
               "AemProperties",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\AmPrprts.dll"))
            );
            if (mAemPropertiesData.rowCount() == 0) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, mAemPropertiesData.mDisplayName + " has no data.");
               throw new DvdplayException(mAemPropertiesData.mDisplayName + " has no data.");
            }

            Aem.readAemProperties();
         } catch (Exception var7) {
            mAemPropertiesData = null;
         }

         try {
            mAemPropertiesData2 = new PersistenceDataBackup(
               DvdplayBase.AEM_PROPERTIES_FIELD_NAMES,
               DvdplayBase.AEM_PROPERTIES_FIELD_TYPES,
               "AmPrprts2.dll",
               "AemProperties2",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\AmPrprts2.dll")),
               mAemPropertiesData
            );
            if (mAemPropertiesData2.rowCount() == 0) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, mAemPropertiesData2.mDisplayName + " has no data.");
               throw new DvdplayException(mAemPropertiesData2.mDisplayName + " has no data.");
            }

            if (mAemPropertiesData == null) {
               Aem.logDetailMessage(DvdplayLevel.INFO, "Using AemProperties backup");
               mAemPropertiesData = new PersistenceData(
                  DvdplayBase.AEM_PROPERTIES_FIELD_NAMES, DvdplayBase.AEM_PROPERTIES_FIELD_TYPES, "AmPrprts.dll", "AemProperties"
               );
               mAemPropertiesData.refreshData(mAemPropertiesData2);
               Aem.readAemProperties();
               if (mCheckSumData == null) {
                  mCheckSumData = new PersistenceData(DvdplayBase.CHECK_SUM_FIELD_NAMES, DvdplayBase.CHECK_SUM_FIELD_TYPES, "ChckSm.dll", "CheckSum", false);
               }

               Aem.refreshAemPropertiesData(false);
            }
         } catch (Exception var10) {
            mAemPropertiesData2 = new PersistenceDataBackup(
               DvdplayBase.AEM_PROPERTIES_FIELD_NAMES, DvdplayBase.AEM_PROPERTIES_FIELD_TYPES, "AmPrprts2.dll", "AemProperties2", mAemPropertiesData
            );
            if (mAemPropertiesData == null) {
               throw new DvdplayException("Need to run Bootstrap");
            }

            mAemPropertiesData2.save();
            mCheckSumData2.save();
         }

         try {
            mDiscDetailData = new PersistenceData(
               DvdplayBase.DISC_DETAIL_FIELD_NAMES,
               DvdplayBase.DISC_DETAIL_FIELD_TYPES,
               "DscDtl.dll",
               "DiscDetail",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\DscDtl.dll"))
            );
         } catch (Exception var6) {
            mDiscDetailData = new PersistenceData(DvdplayBase.DISC_DETAIL_FIELD_NAMES, DvdplayBase.DISC_DETAIL_FIELD_TYPES, "DscDtl.dll", "DiscDetail");
            Aem.refreshDiscDetailData();
            Aem.setRefreshedDiscDetailStartup();
         }

         mTitleDetailData = PersistenceData.createInstance(
            DvdplayBase.TITLE_DETAIL_FIELD_NAMES,
            DvdplayBase.TITLE_DETAIL_FIELD_TYPES,
            "TtlDtl.dll",
            "TitleDetail",
            lComposer,
            "TitleDetail query",
            "select TitleID 'TitleDetailId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', OriginalTitle 'Title', TranslatedTitle 'TranslatedTitle', SortTitle 'SortTitle', ShortTitle 'ShortTitle', Genre1ID 'Genre1Id', Genre2ID 'Genre2Id', Genre3ID 'Genre3Id', Description 'Description', Poster 'Poster', Trailer 'Trailer', RatingSystem1ID 'RatingSystem1Id', RatingSystem2ID 'RatingSystem2Id', RatingSystem3ID 'RatingSystem3Id', Rating1ID 'Rating1Id', Rating2ID 'Rating2Id', Rating3ID 'Rating3Id', convert(varchar(30), StreetDate, 121) + ' "
               + Util.getTimeZoneString()
               + "'StreetDate, ReleaseYear 'ReleaseYear', Attr1 'Attr1', Attr2 'Attr2', Attr3 'Attr3', Attr4 'Attr4', Attr5 'Attr5', Attr6 'Attr6' from ffKioskTitle"
         );
         mRegularPricingData = PersistenceData.createInstance(
            DvdplayBase.REGULAR_PRICING_FIELD_NAMES,
            DvdplayBase.REGULAR_PRICING_FIELD_TYPES,
            "RglrPrcng.dll",
            "RegularPricing",
            lComposer,
            "RegularPricing query",
            "select PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', DayOfTheWeekID 'DayOfTheWeek', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskRegularPricing"
         );
         mSpecialPricingData = PersistenceData.createInstance(
            DvdplayBase.SPECIAL_PRICING_FIELD_NAMES,
            DvdplayBase.SPECIAL_PRICING_FIELD_TYPES,
            "SpclPrcng.dll",
            "SpecialPricing",
            lComposer,
            "SpecialPricing query",
            "select SpecialPricingID 'SpecialPricingId', PriceOptionID 'PriceOptionId', PriceModelID 'PriceModelId', StartDate 'StartDate', EndDate 'EndDate', NewPrice 'NewPrice', UsedPrice 'UsedPrice', RentalPrice 'RentalPrice', LateRentalPrice 'LateRentalPrice', RentDays 'RentalDays', LateRentDays 'LateDays' from ffKioskSpecialPricing"
         );
         mGenreData = PersistenceData.createInstance(
            DvdplayBase.GENRE_FIELD_NAMES,
            DvdplayBase.GENRE_FIELD_TYPES,
            "Gnr.dll",
            "Genre",
            lComposer,
            "Genre query",
            "select GenreID 'GenreId', LocaleID 'LocaleId', TitleTypeID 'TitleTypeId', Priority 'Priority', Genre 'Genre' from ffKioskGenre"
         );
         mSlotOffsetData = PersistenceData.createInstance(
            DvdplayBase.SLOT_OFFSET_FIELD_NAMES,
            DvdplayBase.SLOT_OFFSET_FIELD_TYPES,
            "SlOffst.dll",
            "SlotOffset",
            lComposer,
            "SlotOffset query",
            "select Slot 'Slot', Offset 'Offset' from ffKioskSlotOffset"
         );
         mBadSlotData = PersistenceData.createInstance(
            DvdplayBase.BAD_SLOT_FIELD_NAMES,
            DvdplayBase.BAD_SLOT_FIELD_TYPES,
            "BdSlt.dll",
            "BadSlot",
            lComposer,
            "BadSlot query",
            "Select Slot 'BadSlot', '1' 'BadSlotId' from ffKioskBadSlot"
         );
         mMediaPlaylistData = PersistenceData.createInstance(
            DvdplayBase.MEDIA_PLAYLIST_FIELD_NAMES,
            DvdplayBase.MEDIA_PLAYLIST_FIELD_TYPES,
            "MdPlylst.dll",
            "MediaPlaylist",
            lComposer,
            "VideoPlaylist query",
            "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', Attr1 'Attr1', StartDate 'StartDate', EndDate 'EndDate', SegmentName 'SegmentName', NumSegments 'NumSegments' from ffVideoPlayList"
         );

         try {
            mMediaDetailData = new PersistenceData(
               DvdplayBase.MEDIA_DETAIL_FIELD_NAMES,
               DvdplayBase.MEDIA_DETAIL_FIELD_TYPES,
               "MdDtl.dll",
               "MediaDetail",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\MdDtl.dll"))
            );
         } catch (Exception var5) {
            mMediaDetailData = new PersistenceData(DvdplayBase.MEDIA_DETAIL_FIELD_NAMES, DvdplayBase.MEDIA_DETAIL_FIELD_TYPES, "MdDtl.dll", "MediaDetail");
         }

         mStaticPlaylistData = PersistenceData.createInstance(
            DvdplayBase.STATIC_PLAYLIST_FIELD_NAMES,
            DvdplayBase.STATIC_PLAYLIST_FIELD_TYPES,
            "SttcPlylst.dll",
            "StaticPlaylist",
            lComposer,
            "GraphicPlaylist query",
            "select ContentID 'MediaId', ContentTypeID 'MediaTypeId', Priority 'Priority', FileSize 'FileSize', FileName 'Filename', StartDate 'StartDate', EndDate 'EndDate' from ffGraphicPlayList"
         );
         mTranslationData = PersistenceData.createInstance(
            DvdplayBase.TRANSLATION_FIELD_NAMES,
            DvdplayBase.TRANSLATION_FIELD_TYPES,
            "Trnsltn.dll",
            "Translation",
            lComposer,
            "Translation query",
            "select TextID 'TextId', LocaleID 'LocaleId', Text 'Text' from ffKioskTextTranslation"
         );
         mLocaleData = PersistenceData.createInstance(
            DvdplayBase.LOCALE_FIELD_NAMES,
            DvdplayBase.LOCALE_FIELD_TYPES,
            "Lcl.dll",
            "Translation",
            lComposer,
            "Locale query",
            "select LocaleID 'LocaleId', Language 'Language', Country 'Country' from ffKioskLocale"
         );
         mRatingSystemData = PersistenceData.createInstance(
            DvdplayBase.RATING_SYSTEM_FIELD_NAMES,
            DvdplayBase.RATING_SYSTEM_FIELD_TYPES,
            "RtngSystm.dll",
            "RatingSystem",
            lComposer,
            "RatingSystem query",
            "select RatingSystemId 'RatingSystemId', RatingSystem 'RatingSystem' from ffKioskRatingSystem"
         );
         mRatingData = PersistenceData.createInstance(
            DvdplayBase.RATING_FIELD_NAMES,
            DvdplayBase.RATING_FIELD_TYPES,
            "Rtng.dll",
            "Rating",
            lComposer,
            "Rating query",
            "select RatingID 'RatingId', RatingSystemID 'RatingSystemId', RatingCode 'RatingCode', RatingDesc 'RatingDesc' from ffKioskRating"
         );
         mGroupCodeData = PersistenceData.createInstance(
            DvdplayBase.GROUP_CODE_FIELD_NAMES,
            DvdplayBase.GROUP_CODE_FIELD_TYPES,
            "GrpCd.dll",
            "GroupCode",
            lComposer,
            "GroupCode query",
            "select GroupCodeID 'GroupCodeId', GroupCode 'GroupCode' from ffKioskGroupCode"
         );
         mTitleTypeData = PersistenceData.createInstance(
            DvdplayBase.TITLE_TYPE_FIELD_NAMES,
            DvdplayBase.TITLE_TYPE_FIELD_TYPES,
            "TtlTyp.dll",
            "TitleType",
            lComposer,
            "TitleType query",
            "select TitleTypeId 'TitleTypeId', LocaleID 'LocaleId', Priority 'Priority', TitleTypeName 'TitleType', TitleTypeName2 'TitleTypeSingular' from ffKioskTitleType"
         );
         mTitleTypeCapData = PersistenceData.createInstance(
            DvdplayBase.TITLE_TYPE_CAP_FIELD_NAMES,
            DvdplayBase.TITLE_TYPE_CAP_FIELD_TYPES,
            "TtlTypCp.dll",
            "TitleTypeCap",
            lComposer,
            "TitleTypeCap query",
            "select TitleTypeID 'TitleTypeId', CapTypeID 'CapTypeId', CapValue 'Value' from ffKioskTitleTypeCap"
         );
         mPaymentCardTypeData = PersistenceData.createInstance(
            DvdplayBase.PAYMENT_CARD_TYPE_FIELD_NAMES,
            DvdplayBase.PAYMENT_CARD_TYPE_FIELD_TYPES,
            "PymntCrdTyp.dll",
            "PaymentCardType",
            lComposer,
            "PaymentCardType query",
            "select PaymentCardCategoryID 'PaymentCardCategoryId', PaymentCardTypeID 'PaymentCardTypeId', LocaleID 'LocaleId', Priority 'Priority', SecureMode 'VerificationTypeId', PaymentPicture 'PaymentPicture', PaymentCardName 'PaymentCardTypeName' from ffKioskPaymentCard"
         );
         mSecurityData = PersistenceData.createInstance(
            DvdplayBase.SECURITY_FIELD_NAMES,
            DvdplayBase.SECURITY_FIELD_TYPES,
            "Scrty.dll",
            "Security",
            lComposer,
            "Security query",
            "select LoginID 'UserId', UserName 'UserName', Password 'UserPassword', RoleID 'RoleId' from ffKioskRoleLogin"
         );

         try {
            mPrivilegesData = new PersistenceData(
               DvdplayBase.PRIVILEGES_FIELD_NAMES,
               DvdplayBase.PRIVILEGES_FIELD_TYPES,
               "Prvlgs.dll",
               "Privileges",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\Prvlgs.dll"))
            );
         } catch (Exception var4) {
            mPrivilegesData = new PersistenceData(DvdplayBase.PRIVILEGES_FIELD_NAMES, DvdplayBase.PRIVILEGES_FIELD_TYPES, "Prvlgs.dll", "Privileges");
            Aem.refreshPrivilegesData();
         }

         mPollData = PersistenceData.createInstance(
            DvdplayBase.POLL_FIELD_NAMES,
            DvdplayBase.POLL_FIELD_TYPES,
            "Pll.dll",
            "Poll",
            lComposer,
            "KioskPoll query",
            "select PollID 'PollId', PollTypeID 'PollTypeId', LocaleID 'LocaleId', SeqNum 'Priority', OrderNum 'OrderNum', PollText 'PollText', StartDate 'StartDate', EndDate 'EndDate' from ffKioskPoll"
         );

         try {
            mIndexData = new PersistenceData(
               DvdplayBase.INDEX_FIELD_NAMES,
               DvdplayBase.INDEX_FIELD_TYPES,
               "Indx.dll",
               "Index",
               lComposer.rcDeMarshal(DataPacketComposer.load("c:\\windows\\system32\\var\\pdata\\Indx.dll"))
            );
         } catch (Exception var3) {
            mIndexData = new PersistenceData(DvdplayBase.INDEX_FIELD_NAMES, DvdplayBase.INDEX_FIELD_TYPES, "Indx.dll", "Index");
            Aem.createIndex();
         }

         save();
      } catch (DOMDataException var11) {
         Aem.logDetailMessage(Level.SEVERE, "Reading persistence failed. " + var11.getMessage(), var11);
         Aem.logSummaryMessage("Reading persistence failed. " + var11.getMessage());
         throw new DOMDataException("Need to run Bootstrap.");
      } catch (Exception var12) {
         Aem.logDetailMessage(Level.SEVERE, "Reading persistence failed. " + var12.getMessage(), var12);
         Aem.logSummaryMessage("Reading persistence failed. " + var12.getMessage());
         throw new DvdplayException("DOMData failure");
      }
   }

   public static synchronized void save() {
      try {
         boolean lNeedLock = false;
         String lSaveList = "";
         if (mDiscDetailData.isModified()) {
            lNeedLock = true;
            lSaveList = lSaveList + mDiscDetailData.getDisplayName() + "\n";
         }

         if (lNeedLock) {
            lSaveList = lSaveList + mCheckSumData.getDisplayName() + "\n";
            if (Util.checkLockFile("c:\\windows\\system32\\var\\pdata\\")) {
               throw new DvdplayException("Lockfile found while saving");
            }

            Util.setLockFile("c:\\windows\\system32\\var\\pdata\\", lSaveList);
         }

         if (mDiscDetailData.isModified()) {
            mDiscDetailData.save();
         }

         if (mTitleDetailData.isModified()) {
            mTitleDetailData.save();
         }

         if (mRegularPricingData.isModified()) {
            mRegularPricingData.save();
         }

         if (mGenreData.isModified()) {
            mGenreData.save();
         }

         if (mIndexData.isModified()) {
            mIndexData.save();
         }

         if (mSlotOffsetData.isModified()) {
            mSlotOffsetData.save();
         }

         if (mAemPropertiesData.isModified()) {
            mAemPropertiesData.save();
         }

         if (mBadSlotData.isModified()) {
            mBadSlotData.save();
         }

         if (mMediaPlaylistData.isModified()) {
            mMediaPlaylistData.save();
         }

         if (mMediaDetailData.isModified()) {
            mMediaDetailData.save();
         }

         if (mStaticPlaylistData.isModified()) {
            mStaticPlaylistData.save();
         }

         if (mTranslationData.isModified()) {
            mTranslationData.save();
         }

         if (mLocaleData.isModified()) {
            mLocaleData.save();
         }

         if (mRatingData.isModified()) {
            mRatingData.save();
         }

         if (mRatingSystemData.isModified()) {
            mRatingSystemData.save();
         }

         if (mPaymentCardTypeData.isModified()) {
            mPaymentCardTypeData.save();
         }

         if (mSpecialPricingData.isModified()) {
            mSpecialPricingData.save();
         }

         if (mGroupCodeData.isModified()) {
            mGroupCodeData.save();
         }

         if (mTitleTypeData.isModified()) {
            mTitleTypeData.save();
         }

         if (mTitleTypeCapData.isModified()) {
            mTitleTypeCapData.save();
         }

         if (mPaymentCardTypeData.isModified()) {
            mPaymentCardTypeData.save();
         }

         if (mSecurityData.isModified()) {
            mSecurityData.save();
         }

         if (mPrivilegesData.isModified()) {
            mPrivilegesData.save();
         }

         if (mPollData.isModified()) {
            mPollData.save();
         }

         if (mCheckSumData.isModified()) {
            mCheckSumData.save();
         }

         if (lNeedLock) {
            Util.releaseLockFile("c:\\windows\\system32\\var\\pdata\\");
         }
      } catch (Exception var3) {
         Aem.logDetailMessage(DvdplayLevel.SEVERE, var3.getMessage(), var3);
         Aem.logSummaryMessage(var3.getMessage());
         Aem.setPersistenceError();
         Aem.disableRentals();
         Aem.disableReturns();
         Aem.sendLockFiles();
      }
   }
}
