package net.dvdplay.dataaccess;

import java.util.Hashtable;
import java.util.Iterator;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.Log;

class DataProviderPool {
   private int mPoolSize = 0;
   private Hashtable preAllocated = null;
   private Hashtable<String, IDataProvider> inUse = null;
   private DataProviderFactory mFactory = null;
   private static Hashtable pools = new Hashtable();

   private void pushPreAllocated(String key, IDataProvider provider) {
      if (!this.preAllocated.containsKey(key)) {
         this.preAllocated.put(key, provider);
      }
   }

   private IDataProvider popPreAllocated() throws DvdplayException {
      try {
         if (!this.preAllocated.isEmpty()) {
            Iterator it = this.preAllocated.keySet().iterator();
            if (it.hasNext()) {
               String key = (String)it.next();
               IDataProvider retVal = (IDataProvider)this.preAllocated.get(key);
               this.preAllocated.remove(key);
               return retVal;
            }
         }

         throw new DvdplayException(5009, "Provider pool is empty");
      } catch (DvdplayException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new DvdplayException(5009, var5);
      }
   }

   private DataProviderPool(int poolSize, DataProviderFactory factory) throws DvdplayException {
      this.mPoolSize = poolSize;
      this.inUse = new Hashtable<>();
      this.preAllocated = new Hashtable();
      this.mFactory = factory;

      for (int i = 0; i < poolSize; i++) {
         IDataProvider provider = factory.makeDataProvider();
         this.pushPreAllocated(provider.getProviderId(), provider);
      }
   }

   static DataProviderPool getDataProviderPool(int poolSize, DataProviderFactory factory) throws DvdplayException {
      synchronized (pools) {
         String key = factory.getSignature();
         if (pools.containsKey(key)) {
            DataProviderPool pool = (DataProviderPool)pools.get(key);
            pool.setPoolSize(poolSize);
            return pool;
         } else {
            DataProviderPool aPool = new DataProviderPool(poolSize, factory);
            pools.put(key, aPool);
            return aPool;
         }
      }
   }

   private void setPoolSize(int poolSize) {
      if (poolSize > this.actualPoolSize()) {
         this.mPoolSize = poolSize;
      }
   }

   IDataProvider allocateProvider() throws DvdplayException {
      synchronized (pools) {
         if (this.preAllocated.isEmpty()) {
            if (this.mPoolSize < this.actualPoolSize()) {
               throw new DvdplayException("Cannot allocate Data Provider. Pool is empty");
            }

            IDataProvider provider = this.mFactory.makeDataProvider();
            this.pushPreAllocated(provider.getProviderId(), provider);
         }

         IDataProvider provider = this.popPreAllocated();
         this.inUse.put(provider.getProviderId(), provider);
         return provider;
      }
   }

   void deallocateProvider(IDataProvider dataProvider) throws DvdplayException {
      synchronized (pools) {
         if (this.inUse.containsKey(dataProvider.getProviderId())) {
            this.inUse.remove(dataProvider.getProviderId());
            this.pushPreAllocated(dataProvider.getProviderId(), dataProvider);
         }
      }
   }

   void discardProvider(IDataProvider dataProvider) throws DvdplayException {
      synchronized (pools) {
         if (!dataProvider.isClosed()) {
            throw new DvdplayException(" Provider must be closed first.");
         } else {
            if (this.inUse.containsKey(dataProvider.getProviderId())) {
               this.inUse.remove(dataProvider.getProviderId());
            } else {
               this.preAllocated.remove(dataProvider.getProviderId());
            }
         }
      }
   }

   int availCount() {
      return this.mPoolSize - this.inUseCount();
   }

   int preAllocatedCount() {
      return this.preAllocated.size();
   }

   int inUseCount() {
      return this.inUse.keySet().size();
   }

   private int actualPoolSize() {
      return this.preAllocated.size() + this.inUseCount();
   }

   void forcedFlush() {
      try {
         for (IDataProvider provider : this.inUse.values()) {
            try {
               provider.discard();
            } catch (DvdplayException var4) {
               Log.info("Warning: Pool Flush failed: " + var4.getMessage());
            }
         }
      } catch (Exception var6) {
         Log.info("Warning: Pool Flush failed: " + var6.getMessage());
      }

      try {
         IDataProvider provider = null;

         while (!this.preAllocated.isEmpty()) {
            provider = this.popPreAllocated();
            provider.discard();
         }
      } catch (Exception var5) {
         Log.info("Warning: Pool Flush failed: " + var5.getMessage());
      }
   }
}
