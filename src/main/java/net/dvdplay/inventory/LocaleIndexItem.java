package net.dvdplay.inventory;

import java.util.Locale;
import net.dvdplay.aem.Aem;

public class LocaleIndexItem {
   private int mLocaleId;
   private String mCountry;
   private String mLanguage;

   public int getLocaleId() {
      return this.mLocaleId;
   }

   public String getDisplayLanguage() {
      Locale lLocale = new Locale(this.mLanguage, this.mCountry);
      return lLocale.getDisplayLanguage(Aem.getLocale()).toUpperCase();
   }

   public Locale getLocale() {
      return new Locale(this.mLanguage, this.mCountry);
   }

   public LocaleIndexItem(int aLocaleId, String aLanguage, String aCountry) {
      this.mLocaleId = aLocaleId;
      this.mLanguage = aLanguage;
      this.mCountry = aCountry;
   }

   public LocaleIndexItem(LocaleIndexItem a) {
      this.mLocaleId = a.mLocaleId;
      this.mLanguage = a.mLanguage;
      this.mCountry = a.mCountry;
   }
}
