package net.dvdplay.dataaccess;

import net.dvdplay.exception.DataaccessException;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.Log;

public abstract class DataProviderFactory implements IDataProviderFactory {
   private String mHost = null;
   private String mUser = null;
   private String mPassword = null;
   private DataProviderPool pool = null;

   protected DataProviderFactory(int poolSize, String host, String user, String password) throws DvdplayException {
      this.mHost = host;
      this.mUser = user;
      this.mPassword = password;
      this.pool = DataProviderPool.getDataProviderPool(poolSize, this);
   }

   public IDataProvider getDataProvider() throws DvdplayException {
      try {
         synchronized (this.pool) {
            Log.info("Allocating Provider. Current PoolAvail=" + this.availPoolSize());
            return this.pool.allocateProvider();
         }
      } catch (DvdplayException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new DataaccessException(5002, var5, "Provider Allocation failed [" + this.mHost + " " + this.mUser + "]");
      }
   }

   public void forcedFlush() throws DvdplayException {
      try {
         synchronized (this.pool) {
            this.pool.forcedFlush();
         }
      } catch (Exception var4) {
         throw new DataaccessException(5003, var4, "Pool flush failed  [" + this.mHost + " " + this.mUser + "]");
      }
   }

   protected abstract IDataProvider makeDataProvider() throws DvdplayException;

   protected void releaseDataProvider(IDataProvider provider) throws DvdplayException {
      synchronized (this.pool) {
         this.pool.deallocateProvider(provider);
      }
   }

   protected void discardDataProvider(IDataProvider provider) throws DvdplayException {
      synchronized (this.pool) {
         this.pool.discardProvider(provider);
      }
   }

   public int availPoolSize() throws DvdplayException {
      try {
         synchronized (this.pool) {
            return this.pool.availCount();
         }
      } catch (Exception var4) {
         throw new DataaccessException(1000, var4, "No Pool [" + this.mHost + " " + this.mUser + "]");
      }
   }

   public int preAllocatedPoolSize() throws DvdplayException {
      try {
         synchronized (this.pool) {
            return this.pool.preAllocatedCount();
         }
      } catch (Exception var4) {
         throw new DataaccessException(1000, var4, "No Pool [" + this.mHost + " " + this.mUser + "]");
      }
   }

   public int inUsePoolSize() throws DvdplayException {
      try {
         synchronized (this.pool) {
            return this.pool.inUseCount();
         }
      } catch (Exception var4) {
         throw new DataaccessException(1000, var4, "No Pool  [" + this.mHost + " " + this.mUser + "]");
      }
   }

   public String getSignature() throws DvdplayException {
      try {
         return "_" + this.getmHost() + "_" + this.getmUser() + "_" + this.getmPassword() + "_";
      } catch (Exception var2) {
         throw new DataaccessException(1000, var2, "No Provider Factory [" + this.mHost + " " + this.mUser + "]");
      }
   }

   protected String getmHost() {
      return this.mHost;
   }

   protected String getmUser() {
      return this.mUser;
   }

   protected String getmPassword() {
      return this.mPassword;
   }
}
