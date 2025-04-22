package net.dvdplay.dataaccess.mssql;

import net.dvdplay.dataaccess.DataProviderFactory;
import net.dvdplay.dataaccess.IDataProvider;
import net.dvdplay.exception.DataaccessException;
import net.dvdplay.exception.DvdplayException;

public class MsSqlProviderFactory extends DataProviderFactory {
   static final int DEFAULT_POOL_SIZE = 1;

   public MsSqlProviderFactory(int poolSize, String connectString, String user, String password) throws DvdplayException {
      super(poolSize, connectString, user, password);
   }

   public MsSqlProviderFactory(String connectString, String user, String password) throws DvdplayException {
      super(1, connectString, user, password);
   }

   public IDataProvider makeDataProvider() throws DvdplayException {
      try {
         return new MsSqlDataProvider(this, super.getmHost(), super.getmUser(), super.getmPassword());
      } catch (Exception var2) {
         throw new DataaccessException(5008, var2);
      }
   }

   protected synchronized void releaseDataProvider(IDataProvider provider) throws DvdplayException {
      super.releaseDataProvider(provider);
   }

   protected synchronized void discardDataProvider(IDataProvider provider) throws DvdplayException {
      super.discardDataProvider(provider);
   }
}
