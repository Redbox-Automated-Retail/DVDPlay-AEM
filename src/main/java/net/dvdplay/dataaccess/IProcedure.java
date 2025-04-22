package net.dvdplay.dataaccess;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import net.dvdplay.exception.DvdplayException;

public interface IProcedure extends IQuery {
   boolean execute() throws DvdplayException;

   boolean executeQuery() throws DvdplayException;

   void registerOutParameter(int var1, int var2) throws DvdplayException;

   void registerOutParameter(int var1, int var2, int var3) throws DvdplayException;

   void registerOutParameter(int var1, int var2, String var3) throws DvdplayException;

   void registerOutParameter(String var1, int var2) throws DvdplayException;

   void registerOutParameter(String var1, int var2, int var3) throws DvdplayException;

   void registerOutParameter(String var1, int var2, String var3) throws DvdplayException;

   void setInt(int var1, int var2) throws DvdplayException;

   void setInt(String var1, int var2) throws DvdplayException;

   void setBigDecimal(int var1, BigDecimal var2) throws DvdplayException;

   void setBigDecimal(String var1, BigDecimal var2) throws DvdplayException;

   void setDouble(int var1, double var2) throws DvdplayException;

   void setDouble(String var1, double var2) throws DvdplayException;

   void setDate(String var1, Date var2) throws DvdplayException;

   void setDate(int var1, Date var2) throws DvdplayException;

   void setNull(String var1, int var2) throws DvdplayException;

   void setNull(int var1, int var2) throws DvdplayException;

   void setString(String var1, String var2) throws DvdplayException;

   void setString(int var1, String var2) throws DvdplayException;

   void setTimestamp(String var1, Timestamp var2) throws DvdplayException;

   void setTimestamp(int var1, Timestamp var2) throws DvdplayException;
}
