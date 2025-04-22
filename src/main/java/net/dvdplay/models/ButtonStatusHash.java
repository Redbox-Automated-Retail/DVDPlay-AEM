package net.dvdplay.models;

import java.util.Enumeration;
import java.util.Hashtable;

public class ButtonStatusHash {
   private Hashtable ht = new Hashtable();

   public Enumeration keys() {
      return this.ht.keys();
   }

   public Object get(Object key) {
      return this.ht.get(key);
   }

   public void put(Object key, Object value) {
      this.ht.put(key, value);
   }
}
