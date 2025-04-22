package net.dvdplay.communication;

import java.util.Vector;
import net.dvdplay.exception.DvdplayException;

class RMetaField {
   String name = null;
   String type = null;

   public RMetaField(String name, String type) {
      this.type = type;
      this.name = name;
   }

   public boolean matches(RMetaField p1) {
      return this.name.compareTo(p1.name) != 0 ? false : this.type.compareTo(p1.type) == 0;
   }

   public void print() {
      System.out.println("name=" + this.name + "(" + this.type + ")");
   }

   public static void printVector(Vector nvVector) {
      int count = nvVector.size();

      for (int i = 0; i < count; i++) {
         RMetaField nvPair = (RMetaField)nvVector.get(i);
         nvPair.print();
      }
   }

   public static Vector mkVector(String[] names, String[] types, String[] values) throws DvdplayException {
      int count = names.length;
      Vector nvPairVector = new Vector();
      if (types.length == count && values.length == count) {
         for (int i = 0; i < count; i++) {
            RMetaField nvPair = new RMetaField(names[i], types[i]);
            nvPairVector.add(nvPair);
         }

         return nvPairVector;
      } else {
         throw new DvdplayException("Cardinality mismatch creating nvt pair vector");
      }
   }

   public static boolean matches(Vector v1, Vector v2) {
      int count = v1.size();
      if (v2.size() != count) {
         return false;
      } else {
         for (int i = 0; i < count; i++) {
            RMetaField nvPair1 = (RMetaField)v1.get(i);
            RMetaField nvPair2 = (RMetaField)v2.get(i);
            if (!nvPair1.matches(nvPair2)) {
               return false;
            }
         }

         return true;
      }
   }

   public static int findName(String name, Vector v1) {
      if (v1 == null) {
         return -1;
      } else {
         int count = v1.size();

         for (int i = 0; i < count; i++) {
            RMetaField nvPair = (RMetaField)v1.get(i);
            if (nvPair.name.compareToIgnoreCase(name) == 0) {
               return i;
            }
         }

         return -1;
      }
   }
}
