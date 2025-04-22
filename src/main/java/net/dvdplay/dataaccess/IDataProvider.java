package net.dvdplay.dataaccess;

import net.dvdplay.exception.DvdplayException;

public interface IDataProvider {
   IQuery createQuery() throws DvdplayException;

   IProcedure createProcedure(String var1) throws DvdplayException;

   void open() throws DvdplayException;

   void close() throws DvdplayException;

   void discard() throws DvdplayException;

   void commit() throws DvdplayException;

   void rollback() throws DvdplayException;

   boolean isClosed() throws DvdplayException;

   boolean isReadOnly() throws DvdplayException;

   String getProviderId() throws DvdplayException;
}
