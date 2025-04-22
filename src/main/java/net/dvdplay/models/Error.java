package net.dvdplay.models;

import java.util.Hashtable;
import net.dvdplay.aem.Aem;

public class Error {
   private Hashtable ht;
   private String tempStr;
   private String error;
   private String[] strSlot;

   public Error() {
      this.init();
   }

   public String getErrorById(int id) {
      try {
         this.error = "";
         if (this.ht.containsKey("" + id)) {
            this.tempStr = (String)this.ht.get("" + id);
            this.strSlot = this.tempStr.split(",");
            Aem.createTitleTypeList();

             for (String s : this.strSlot) {
                 if (s.startsWith("&")) {
                     switch (s) {
                         case "&FirstTimeRentalAllowed" ->
                                 this.error = this.error + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getFirstTimeMax() + " ";
                         case "&MaxRentalAllowed" ->
                                 this.error = this.error + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getRegularUserMax() + " ";
                         case "&FranchiseeName" -> this.error = this.error + " " + Aem.getString(2012) + " ";
                     }
                 } else {
                     this.error = this.error + " " + Aem.getString(Integer.parseInt(s));
                 }
             }

            return this.error;
         } else {
            return Aem.getString(id) != null ? Aem.getString(id) : null;
         }
      } catch (Exception var3) {
         System.out.println("ERROR Gettng ERROR Code " + var3.toString());
         return "";
      }
   }

   private void init() {
      this.ht = new Hashtable();
      this.ht.put("4016", "4016,&FirstTimeRentalAllowed,4017,&MaxRentalAllowed,4018");
      this.ht.put("4021", "4021,&FranchiseeName,4022");
   }
}
