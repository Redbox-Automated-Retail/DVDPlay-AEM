package net.dvdplay.aem;

public class AemFactory {
   private static Aem mInstance = null;

   public static synchronized Aem getInstance() {
      if (mInstance == null) {
         mInstance = new Aem();
      }

      return mInstance;
   }
}
