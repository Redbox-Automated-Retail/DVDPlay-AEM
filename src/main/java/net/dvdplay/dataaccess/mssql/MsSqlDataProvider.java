package net.dvdplay.dataaccess.mssql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import net.dvdplay.dataaccess.DataProvider;
import net.dvdplay.dataaccess.IProcedure;
import net.dvdplay.dataaccess.IQuery;
import net.dvdplay.exception.DataaccessException;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.Log;

public class MsSqlDataProvider extends DataProvider {
   private Connection mConnection = null;
   private String mHost = null;
   private String mUser = null;
   private String mPassword = null;
   private MsSqlProviderFactory mFactory = null;

   MsSqlDataProvider(MsSqlProviderFactory factory, String dbHost, String dbUser, String dbPassword) throws DvdplayException {
      this.mUser = dbUser;
      this.mPassword = dbPassword;
      this.mHost = dbHost;
      this.mFactory = factory;
   }

   public IQuery createQuery() throws DvdplayException {
      try {
         Statement stmt = this.mConnection.createStatement(1004, 1008);
         return new MsSqlQuery(stmt);
      } catch (DvdplayException var4) {
         throw var4;
      } catch (SQLException var5) {
         throw new DataaccessException(1001, var5);
      } catch (Exception var6) {
         throw new DataaccessException(1000, var6);
      }
   }

   public IProcedure createProcedure(String sql) throws DvdplayException {
      try {
         CallableStatement stmt = this.mConnection.prepareCall(sql);
         return new MsSqlProcedure(stmt);
      } catch (DvdplayException var5) {
         throw var5;
      } catch (SQLException var6) {
         throw new DataaccessException(1001, var6);
      } catch (Exception var7) {
         throw new DataaccessException(1000, var7);
      }
   }

   public void open() throws DvdplayException {
      try {
         if (this.mConnection != null && !this.mConnection.isClosed()) {
            this.mConnection.rollback();
         } else {
            Class.forName("com.inet.tds.TdsDriver").newInstance();
            this.mConnection = DriverManager.getConnection("jdbc:inetdae7:" + this.mHost + ":1433", this.mUser, this.mPassword);
            this.mConnection.setAutoCommit(false);
         }
      } catch (ClassNotFoundException var6) {
         Log.severe(var6, "Open Connection failure. Driver not found");
         throw new DataaccessException(5005, var6);
      } catch (InstantiationException var7) {
         Log.severe(var7, "Open Connection failure. InstantiationException ");
         throw new DataaccessException(5005, var7);
      } catch (IllegalAccessException var8) {
         Log.severe(var8, "Open Connection failure: IllegalAccess");
         throw new DataaccessException(5005, var8);
      } catch (SQLException var9) {
         Log.severe(var9, "Open Connection failure:  SQL exception");
         throw new DataaccessException(5005, var9);
      } catch (Exception var10) {
         Log.severe(var10, "Open Connection failure: Exception");
         throw new DataaccessException(5005, var10);
      }
   }

   public synchronized void close() throws DvdplayException {
      try {
         try {
            this.mConnection.rollback();
         } catch (Exception var2) {
         }

         this.mFactory.releaseDataProvider(this);
      } catch (Exception var3) {
      }
   }

   public synchronized void discard() throws DvdplayException {
      try {
         this.mConnection.close();
         this.mFactory.discardDataProvider(this);
      } catch (Exception var2) {
      }
   }

   public boolean isClosed() {
      try {
         return this.mConnection.isClosed();
      } catch (Exception var2) {
         return true;
      }
   }

   public synchronized void rollback() throws DvdplayException {
      try {
         this.mConnection.rollback();
      } catch (SQLException var3) {
         throw new DataaccessException(1001, var3);
      } catch (Exception var4) {
         throw new DataaccessException(1000, var4);
      }
   }

   public synchronized void commit() throws DvdplayException {
      try {
         this.mConnection.commit();
      } catch (SQLException var3) {
         throw new DataaccessException(5006, var3);
      } catch (Exception var4) {
         throw new DataaccessException(5006, var4);
      }
   }

   public boolean isReadOnly() {
      try {
         return this.mConnection.isReadOnly();
      } catch (Exception var2) {
         return false;
      }
   }
}
