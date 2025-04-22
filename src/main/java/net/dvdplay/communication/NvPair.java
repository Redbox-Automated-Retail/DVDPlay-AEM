package net.dvdplay.communication;

import net.dvdplay.exception.DvdplayException;

public class NvPair {
   String name = null;
   private Object value = null;
   private String type = null;
   String format = null;

   public NvPair(String name, String type, String value) {
      this.type = type;
      this.name = name;
      this.value = value;
      this.format = "STRING";
   }

   public NvPair(String name, String type, RCSet value) {
      this.type = type;
      this.name = name;
      this.value = value;
      this.format = "RCSET";
   }

   public String getValueAsString() throws DvdplayException {
      if (this.format.compareToIgnoreCase("STRING") != 0) {
         throw new DvdplayException("Value not in String format");
      } else {
         return (String)this.value;
      }
   }

   public RCSet getValueAsRCSet() throws DvdplayException {
      if (this.format.compareToIgnoreCase("RCSET") != 0) {
         throw new DvdplayException("Value not in String format");
      } else {
         return (RCSet)this.value;
      }
   }

   public Object getValue() throws DvdplayException {
      return this.value;
   }

   public boolean isFormat_RCSET() {
      return this.format.compareTo("RCSET") == 0;
   }

   public boolean isFormat_STRING() {
      return this.format.compareTo("STRING") == 0;
   }

   public boolean matches(NvPair p1) {
      if (this.name.compareTo(p1.name) != 0) {
         return false;
      } else if (this.getType().compareTo(p1.getType()) != 0) {
         return false;
      } else {
         return this.format.compareTo(p1.format) == 0 && (this.format.compareTo("STRING") != 0 || ((String) this.value).compareTo((String) p1.value) == 0);
      }
   }

   public void print() {
      System.out.println("name=" + this.name + "(" + this.getType() + ") = " + this.value);
   }

   public String getType() {
      return this.type;
   }
}
