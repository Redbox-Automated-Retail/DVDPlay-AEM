package net.dvdplay.communication;

import java.util.Vector;
import net.dvdplay.exception.DvdplayException;

public class RDataSetFieldValues {
   private Vector values = null;

   public RDataSetFieldValues(int nFields) {
      this.values = new Vector();

      for (int i = 0; i < nFields; i++) {
         this.values.add(null);
      }
   }

   public void setValue(int fieldIndex, String value) throws DvdplayException {
      int count = this.values.size();
      if (fieldIndex >= 0 && fieldIndex < count) {
         this.values.setElementAt(value, fieldIndex);
      } else {
         throw new DvdplayException("Bad field index");
      }
   }

   public void addValue(String[] fieldValues) {
      int count = fieldValues.length;

      for (int i = 0; i < count; i++) {
         this.values.add(fieldValues[i]);
      }
   }

   public int getCount() {
      return this.values.size();
   }

   public RDataSetFieldValues deepCopy() {
      int fieldCount = this.values.size();
      RDataSetFieldValues fv = new RDataSetFieldValues(fieldCount);
      fv.values = (Vector)this.values.clone();
      return fv;
   }

   public String getValue(int fieldIndex) throws DvdplayException {
      if (fieldIndex >= 0 && fieldIndex < this.getCount()) {
         return (String)this.values.get(fieldIndex);
      } else {
         throw new DvdplayException("Bad Field Index");
      }
   }
}
