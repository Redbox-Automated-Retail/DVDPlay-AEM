package net.dvdplay.dataaccess;

import net.dvdplay.exception.DvdplayException;

public interface IDataProviderFactory {
   IDataProvider getDataProvider() throws DvdplayException;

   String getSignature() throws DvdplayException;

   void forcedFlush() throws DvdplayException;

   int availPoolSize() throws DvdplayException;

   int preAllocatedPoolSize() throws DvdplayException;

   int inUsePoolSize() throws DvdplayException;
}
