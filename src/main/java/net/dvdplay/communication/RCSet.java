package net.dvdplay.communication;

import java.util.Vector;
import net.dvdplay.exception.DvdplayException;

public class RCSet {
   private int mNFields = 0;
   private Vector mAllRows = null;
   private RMetaRow mMetaRow = null;

   public RCSet() throws DvdplayException {
      this.mNFields = 0;
      this.mAllRows = new Vector();
      this.mMetaRow = new RMetaRow();
   }

   public RCSet(String[] names, String[] types) throws DvdplayException {
      this.mNFields = names.length;
      if (this.mNFields != types.length) {
         throw new DvdplayException("Field number mismatch.");
      } else {
         this.mAllRows = new Vector();
         this.mMetaRow = new RMetaRow();

         for (int i = 0; i < this.mNFields; i++) {
            this.mMetaRow.addMetaField(names[i], types[i]);
         }
      }
   }

   public RCSet(RMetaRow metaRow) throws DvdplayException {
      this.mNFields = metaRow.getCount();
      this.mAllRows = new Vector();
      this.mMetaRow = new RMetaRow();

      for (int i = 0; i < this.mNFields; i++) {
         this.mMetaRow.addMetaField(metaRow.getFieldName(i), metaRow.getFieldType(i));
      }
   }

   public void addRow(RDataSetFieldValues fieldValues) throws DvdplayException {
      if (fieldValues != null && fieldValues.getCount() == this.mNFields) {
         RDataSetFieldValues newRow = fieldValues.deepCopy();
         this.mAllRows.add(newRow);
      } else {
         throw new DvdplayException("Field number mismatch in row assignment");
      }
   }

   public void addRow(String[] fieldValues) throws DvdplayException {
      if (fieldValues != null && fieldValues.length == this.mNFields) {
         RDataSetFieldValues fv = new RDataSetFieldValues(this.mNFields);

         for (int i = 0; i < this.mNFields; i++) {
            fv.setValue(i, fieldValues[i]);
         }

         this.mAllRows.add(fv);
      } else {
         throw new DvdplayException("Field number mismatch in row assignment");
      }
   }

   public RDataSetFieldValues getRow(int index) throws DvdplayException {
      int count = this.mAllRows.size();
      if (index >= 0 && index < count) {
         RDataSetFieldValues row = (RDataSetFieldValues)this.mAllRows.get(index);
         if (row == null) {
            throw new DvdplayException("Row has been deleted before");
         } else {
            return row;
         }
      } else {
         throw new DvdplayException("Bad Row index");
      }
   }

   public void deleteRow(int index) throws DvdplayException {
      int count = this.mAllRows.size();
      RDataSetFieldValues row = this.getRow(index);
      this.mAllRows.set(index, null);
      int count1 = this.mAllRows.size();
   }

   public boolean isDeleted(int index) throws DvdplayException {
      int count = this.mAllRows.size();
      if (index >= 0 && index < count) {
         RDataSetFieldValues row = (RDataSetFieldValues)this.mAllRows.get(index);
         return row == null;
      } else {
         throw new DvdplayException("Bad Row index");
      }
   }

   public int rowCount() {
      return this.mAllRows.size();
   }

   public int fieldCount() {
      return this.mMetaRow.getCount();
   }

   public String getFieldName(int index) throws DvdplayException {
      return this.mMetaRow.getFieldName(index);
   }

   public int getFieldIndex(String name) throws DvdplayException {
      return this.mMetaRow.getIndex(name);
   }

   public String getFieldValue(int rowIndex, int fieldIndex) throws DvdplayException {
      RDataSetFieldValues aRow = this.getRow(rowIndex);
      return aRow.getValue(fieldIndex);
   }

   public String[] getNames() throws DvdplayException {
      return this.mMetaRow.getNames();
   }

   public String[] getTypes() throws DvdplayException {
      return this.mMetaRow.getTypes();
   }
}
