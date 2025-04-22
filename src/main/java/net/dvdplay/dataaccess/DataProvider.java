package net.dvdplay.dataaccess;

import net.dvdplay.exception.DataaccessException;
import net.dvdplay.exception.DvdplayException;

public abstract class DataProvider implements IDataProvider {
   private int mProviderId = 0;
   private static int lastProviderId = 0;

   protected DataProvider() {
      lastProviderId++;
      this.mProviderId = lastProviderId++;
   }

   public final String getProviderId() throws DvdplayException {
      try {
         return Integer.toString(this.mProviderId);
      } catch (Exception var2) {
         throw new DataaccessException(5001, "Could not determine provider id.");
      }
   }
}
