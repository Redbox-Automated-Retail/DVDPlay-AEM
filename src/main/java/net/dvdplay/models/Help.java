package net.dvdplay.models;

import java.util.Hashtable;
import net.dvdplay.aem.Aem;
import net.dvdplay.util.Util;

public class Help {
   private Hashtable ht;
   private String[] index = new String[]{"1510", "1521", "1530", "1540", "1550", "1560", "1570", "1580", "1590", "1600", "1610"};
   private String tempStr;
   private String answer;
   private String[] strSlot;

   public Help() {
      this.init();
   }

   public int size() {
      return this.index.length;
   }

   public String getQuestionId(int i) {
      return this.index[i];
   }

   public String getQuestion(int i) {
      if (this.index[i].equals("1510")) {
         return Aem.isBuyDisabled() ? Aem.getString(1515) : Aem.getString(1510);
      } else {
         return Aem.getString(Integer.parseInt(this.index[i]));
      }
   }

   public String getQuestionById(int id) {
      if (id == 1510) {
         return Aem.isBuyDisabled() ? Aem.getString(1515) : Aem.getString(1510);
      } else {
         return Aem.getString(id);
      }
   }

   public String getAnswerById(int id) {
      this.answer = "";

      try {
         this.tempStr = (String)this.ht.get("" + id);
         this.strSlot = this.tempStr.split(",");
         int numTitleTypes = Aem.createTitleTypeList();

         for (int i = 0; i < this.strSlot.length; i++) {
            if (!this.strSlot[i].startsWith("&")) {
               this.answer = this.answer + " " + Aem.getString(Integer.parseInt(this.strSlot[i]));
            } else if (this.strSlot[i].equals("&MaxPriceFormat1")) {
               for (int j = 0; j < numTitleTypes; j++) {
                  this.answer = this.answer
                     + " \\n  "
                     + Aem.getString(1542)
                     + Util.capFirstChar(Aem.getTitleTypeIndexItem(j).getTitleTypeSingular(), Aem.getLocale())
                     + ": "
                     + Aem.getCurrencySymbol()
                     + Aem.getTitleTypeIndexItem(j).getMaxCharge();
               }

               this.answer = this.answer + " \\n \\n ";
            } else if (!this.strSlot[i].equals("&MaxPriceFormat2")) {
               if (this.strSlot[i].equals("&FranchiseeName")) {
                  this.answer = this.answer + Aem.getString(2011);
               } else if (this.strSlot[i].equals("&MaxRental")) {
                  int maxFirst = Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getFirstTimeMax();
                  int maxRepeat = Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getRegularUserMax();
                  this.answer = this.answer + " \\n " + Aem.getString(1602) + maxFirst;
                  this.answer = this.answer + (maxFirst > 1 ? Aem.getString(4067) : Aem.getString(4066));
                  this.answer = this.answer + Aem.getString(1603) + " \\n ";
                  this.answer = this.answer + Aem.getString(1604) + maxRepeat;
                  this.answer = this.answer + (maxRepeat > 1 ? Aem.getString(4067) : Aem.getString(4066));
                  this.answer = this.answer + " \\n \\n ";
               }
            } else {
               for (int j = 0; j < numTitleTypes; j++) {
                  this.answer = this.answer
                     + " \\n "
                     + Aem.getCurrencySymbol()
                     + Aem.getTitleTypeIndexItem(j).getMaxCharge()
                     + Aem.getString(1582)
                     + Util.capFirstChar(Aem.getTitleTypeIndexItem(j).getTitleTypeSingular(), Aem.getLocale());
               }

               this.answer = this.answer + " \\n \\n ";
            }
         }

         return this.answer;
      } catch (Exception var6) {
         return "";
      }
   }

   private void init() {
      this.ht = new Hashtable();
      this.ht.put("1510", "1511,1512,1513,1514");
      this.ht.put("1521", "1522,1523,1524");
      this.ht.put("1530", "1531");
      this.ht.put("1540", "1541,&MaxPriceFormat1");
      this.ht.put("1550", "1551");
      this.ht.put("1560", "1561");
      this.ht.put("1570", "1571");
      this.ht.put("1580", "1581,&MaxPriceFormat2,1583");
      this.ht.put("1590", "1592,1593,1594,1595,1596,1597,1598");
      this.ht.put("1600", "1601,&MaxRental,1605");
      this.ht.put("1610", "1611,&FranchiseeName,1612");
   }
}
