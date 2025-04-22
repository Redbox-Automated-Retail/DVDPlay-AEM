package net.dvdplay.communication;

import java.util.HashMap;
import java.util.Vector;
import net.dvdplay.exception.DvdplayException;

public class RMetaRow {
   private Vector<RMetaField> allMetaFields = null;
   private HashMap<String, Integer> nameLookup = null;

   public RMetaRow() {
      this.allMetaFields = new Vector<>();
      this.nameLookup = new HashMap<>();
   }

   public void addMetaField(String name, String type) throws DvdplayException {
      RMetaField mf = new RMetaField(name, type);
      boolean duplicateFlag = this.nameLookup.containsKey(name);
      if (duplicateFlag) {
         throw new DvdplayException("Duplicate name in metaRow");
      } else {
         int count = this.allMetaFields.size();
         this.allMetaFields.add(mf);
         this.nameLookup.put(name, count);
      }
   }

   public int getCount() {
      return this.allMetaFields.size();
   }

   public int getIndex(String fieldName) throws DvdplayException {
      int count = this.allMetaFields.size();
      if (this.nameLookup.containsKey(fieldName)) {
         Integer index = this.nameLookup.get(fieldName);
         return index;
      } else {
         throw new DvdplayException("Invalid field name");
      }
   }

   public String getFieldName(int index) throws DvdplayException {
      if (index >= 0 && index < this.getCount()) {
         RMetaField metaField = this.allMetaFields.get(index);
         return metaField.name;
      } else {
         throw new DvdplayException("Invalid field index");
      }
   }

   public String getFieldType(String fieldName) throws DvdplayException {
      return this.getFieldType(this.getIndex(fieldName));
   }

   public String getFieldType(int index) throws DvdplayException {
      if (index >= 0 && index < this.getCount()) {
         RMetaField metaField = this.allMetaFields.get(index);
         return metaField.type;
      } else {
         throw new DvdplayException("Invalid field index");
      }
   }

   public String[] getNames() throws DvdplayException {
      int totFields = this.getCount();
      String[] names = new String[totFields];

      for (int i = 0; i < totFields; i++) {
         names[i] = this.getFieldName(i);
      }

      return names;
   }

   public String[] getTypes() throws DvdplayException {
      int totFields = this.getCount();
      String[] types = new String[totFields];

      for (int i = 0; i < totFields; i++) {
         types[i] = this.getFieldType(i);
      }

      return types;
   }
}
