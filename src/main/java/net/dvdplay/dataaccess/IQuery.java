package net.dvdplay.dataaccess;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;

public interface IQuery {
   void close() throws DvdplayException;

   ResultSetMetaData getMetaData() throws DvdplayException;

   RCSet executeAsRcSet(String var1) throws DvdplayException;

   boolean execute(String var1) throws DvdplayException;

   int executeUpdate(String var1) throws DvdplayException;

   int getUpdateCount() throws DvdplayException;

   int getFetchDirection() throws DvdplayException;

   int getFetchSize() throws DvdplayException;

   int getMaxRows() throws DvdplayException;

   boolean getMoreResults() throws DvdplayException;

   int getQueryTimeout() throws DvdplayException;

   void setQueryTimeout(int var1) throws DvdplayException;

   SQLWarning getWarnings() throws DvdplayException;

   void setFetchDirection(int var1) throws DvdplayException;

   void setFetchSize(int var1) throws DvdplayException;

   void setMaxRows(int var1) throws DvdplayException;

   void afterLast() throws DvdplayException;

   void beforeFirst() throws DvdplayException;

   void cancelRowUpdates() throws DvdplayException;

   void deleteRow() throws DvdplayException;

   int findColumn(String var1) throws DvdplayException;

   boolean columnExists(String var1) throws DvdplayException;

   boolean first() throws DvdplayException;

   Array getArray(int var1) throws DvdplayException;

   Array getArray(String var1) throws DvdplayException;

   BigDecimal getBigDecimal(int var1) throws DvdplayException;

   BigDecimal getBigDecimal(String var1) throws DvdplayException;

   boolean getBoolean(int var1) throws DvdplayException;

   boolean getBoolean(String var1) throws DvdplayException;

   byte getByte(int var1) throws DvdplayException;

   byte getByte(String var1) throws DvdplayException;

   byte[] getBytes(int var1) throws DvdplayException;

   byte[] getBytes(String var1) throws DvdplayException;

   Date getDate(int var1) throws DvdplayException;

   Date getDate(String var1) throws DvdplayException;

   Date getDate(int var1, Calendar var2) throws DvdplayException;

   Date getDate(String var1, Calendar var2) throws DvdplayException;

   double getDouble(int var1) throws DvdplayException;

   double getDouble(String var1) throws DvdplayException;

   float getFloat(int var1) throws DvdplayException;

   float getFloat(String var1) throws DvdplayException;

   int getInt(int var1) throws DvdplayException;

   int getInt(String var1) throws DvdplayException;

   long getLong(int var1) throws DvdplayException;

   long getLong(String var1) throws DvdplayException;

   Object getObject(int var1) throws DvdplayException;

   Object getObject(String var1) throws DvdplayException;

   int getRow() throws DvdplayException;

   short getShort(int var1) throws DvdplayException;

   short getShort(String var1) throws DvdplayException;

   String getString(int var1) throws DvdplayException;

   String getString(String var1) throws DvdplayException;

   Time getTime(int var1) throws DvdplayException;

   Time getTime(String var1) throws DvdplayException;

   Time getTime(int var1, Calendar var2) throws DvdplayException;

   Time getTime(String var1, Calendar var2) throws DvdplayException;

   Timestamp getTimestamp(int var1) throws DvdplayException;

   Timestamp getTimestamp(String var1) throws DvdplayException;

   Timestamp getTimestamp(int var1, Calendar var2) throws DvdplayException;

   Timestamp getTimestamp(String var1, Calendar var2) throws DvdplayException;

   void insertRow() throws DvdplayException;

   boolean isAfterLast() throws DvdplayException;

   boolean isFirst() throws DvdplayException;

   boolean isLast() throws DvdplayException;

   boolean last() throws DvdplayException;

   void moveToCurrentRow() throws DvdplayException;

   void moveToInsertRow() throws DvdplayException;

   boolean next() throws DvdplayException;

   boolean previous() throws DvdplayException;

   void refreshRow() throws DvdplayException;

   boolean absolute(int var1) throws DvdplayException;

   boolean relative(int var1) throws DvdplayException;

   boolean rowDeleted() throws DvdplayException;

   boolean rowInserted() throws DvdplayException;

   boolean rowUpdated() throws DvdplayException;

   void updateArray(int var1, Array var2) throws DvdplayException;

   void updateArray(String var1, Array var2) throws DvdplayException;

   void updateBigDecimal(int var1, BigDecimal var2) throws DvdplayException;

   void updateBigDecimal(String var1, BigDecimal var2) throws DvdplayException;

   void updateBoolean(int var1, boolean var2) throws DvdplayException;

   void updateBoolean(String var1, boolean var2) throws DvdplayException;

   void updateByte(int var1, byte var2) throws DvdplayException;

   void updateByte(String var1, byte var2) throws DvdplayException;

   void updateBytes(int var1, byte[] var2) throws DvdplayException;

   void updateBytes(String var1, byte[] var2) throws DvdplayException;

   void updateDate(int var1, Date var2) throws DvdplayException;

   void updateDate(String var1, Date var2) throws DvdplayException;

   void updateDouble(int var1, double var2) throws DvdplayException;

   void updateDouble(String var1, double var2) throws DvdplayException;

   void updateFloat(int var1, float var2) throws DvdplayException;

   void updateFloat(String var1, float var2) throws DvdplayException;

   int updateInt(int var1) throws DvdplayException;

   void updateInt(String var1, int var2) throws DvdplayException;

   void updateLong(int var1, long var2) throws DvdplayException;

   void updateLong(String var1, long var2) throws DvdplayException;

   void updatebject(int var1, Object var2) throws DvdplayException;

   void updateObject(String var1, Object var2) throws DvdplayException;

   void updateNull(int var1) throws DvdplayException;

   void updateNull(String var1) throws DvdplayException;

   void updateRow() throws DvdplayException;

   void updateShort(int var1, short var2) throws DvdplayException;

   void updateShort(String var1, short var2) throws DvdplayException;

   void updateString(int var1, String var2) throws DvdplayException;

   void updateString(String var1, String var2) throws DvdplayException;

   void updateTime(int var1, Time var2) throws DvdplayException;

   void updateTime(String var1, Time var2) throws DvdplayException;

   void updateTimestamp(int var1, Timestamp var2) throws DvdplayException;

   void updateTimestamp(String var1, Timestamp var2) throws DvdplayException;

   void getTimestamp(String var1, Timestamp var2) throws DvdplayException;

   boolean wasNull() throws DvdplayException;

   boolean isNull(String var1) throws DvdplayException;
}
