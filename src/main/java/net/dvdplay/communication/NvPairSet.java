package net.dvdplay.communication;

import java.util.Vector;
import net.dvdplay.exception.DvdplayException;

public class NvPairSet {
   private Vector allPairs = null;

   public NvPairSet() {
      this.allPairs = new Vector();
   }

   public NvPairSet(String[] names, String[] types, String[] values) throws DvdplayException {
      int count = names.length;
      this.allPairs = new Vector();
      if (types.length == count && values.length == count) {
         for (int i = 0; i < count; i++) {
            NvPair nvPair = new NvPair(names[i], types[i], values[i]);
            this.allPairs.add(nvPair);
         }
      } else {
         throw new DvdplayException("Cardinality mismatch creating nvt pair vector");
      }
   }

   public int size() {
      return this.allPairs.size();
   }

   public NvPair getNvPair(int i) {
      return (NvPair)this.allPairs.get(i);
   }

   public NvPair getNvPair(String name) {
      return (NvPair)this.allPairs.get(this.findName(name));
   }

   public void add(NvPair nvPair) {
      this.allPairs.add(nvPair);
   }

   public void print() {
      int count = this.size();

      for (int i = 0; i < count; i++) {
         NvPair nvPair = this.getNvPair(i);
         nvPair.print();
      }
   }

   public boolean isFormat_RCSET(int i) {
      NvPair nvPair = this.getNvPair(i);
      return nvPair.isFormat_RCSET();
   }

   public boolean matches(NvPairSet targetPair) {
      int count = this.size();
      if (count != targetPair.size()) {
         return false;
      } else {
         for (int i = 0; i < count; i++) {
            NvPair nvPair1 = this.getNvPair(i);
            NvPair nvPair2 = targetPair.getNvPair(i);
            if (!nvPair1.matches(nvPair2)) {
               return false;
            }
         }

         return true;
      }
   }

   public int findName(String name) {
      if (this.allPairs == null) {
         return -1;
      } else {
         int count = this.size();

         for (int i = 0; i < count; i++) {
            NvPair nvPair = this.getNvPair(i);
            if (nvPair.name.compareToIgnoreCase(name) == 0) {
               return i;
            }
         }

         return -1;
      }
   }
}
