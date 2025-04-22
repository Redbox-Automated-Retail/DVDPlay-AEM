package net.dvdplay.dataaccess.mssql;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import net.dvdplay.communication.RCSet;
import net.dvdplay.communication.RDataSetFieldValues;
import net.dvdplay.communication.RMetaRow;
import net.dvdplay.dataaccess.IQuery;
import net.dvdplay.dataaccess.SqlStates;
import net.dvdplay.exception.DataaccessException;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.Log;

public class MsSqlQuery implements IQuery {
   ResultSet mResultSet = null;
   Statement mStatement = null;

   MsSqlQuery(Statement theStatement) throws DvdplayException {
      this.mStatement = theStatement;
   }

   public ResultSetMetaData getMetaData() throws DvdplayException {
      try {
         return this.mResultSet.getMetaData();
      } catch (SQLException var3) {
         throw new DataaccessException(5004, var3);
      } catch (Exception var4) {
         throw new DataaccessException(5004, var4);
      }
   }

   public void close() throws DvdplayException {
      try {
         this.mStatement.close();
      } catch (Exception var16) {
         Log.info("Warning. Database close failed. " + var16.getMessage());
      } finally {
         this.mStatement = null;
      }

      try {
         this.mResultSet.close();
      } catch (Exception var14) {
         Log.info("Warning. ResultSet close failed. " + var14.getMessage());
      } finally {
         this.mResultSet = null;
      }
   }

   public RCSet executeAsRcSet(String sql) throws DvdplayException {
      RCSet retVal = null;

      try {
         if (this.mStatement.execute(sql)) {
            this.mResultSet = this.mStatement.getResultSet();
            ResultSetMetaData rsmd = this.mResultSet.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            RMetaRow mr = new RMetaRow();

            for (int i = 1; i <= numberOfColumns; i++) {
               mr.addMetaField(rsmd.getColumnName(i), rsmd.getColumnTypeName(i));
            }

            retVal = new RCSet(mr);
            this.mResultSet.beforeFirst();

            while (this.mResultSet.next()) {
               RDataSetFieldValues values = new RDataSetFieldValues(numberOfColumns);

               for (int i = 1; i <= numberOfColumns; i++) {
                  values.setValue(i - 1, this.mResultSet.getString(i));
               }

               retVal.addRow(values);
            }
         }

         return retVal;
      } catch (DvdplayException var9) {
         throw var9;
      } catch (SQLException var10) {
         if (var10.getErrorCode() == SqlStates.OBJECT_NOT_FOUND) {
            throw new DvdplayException(5013, var10);
         } else {
            throw new DvdplayException(5004, var10);
         }
      } catch (Exception var11) {
         throw new DvdplayException(5004, var11);
      }
   }

   public boolean execute(String sql) throws DvdplayException {
      try {
         boolean retVal = this.mStatement.execute(sql);
         if (retVal) {
            this.mResultSet = this.mStatement.getResultSet();
         }

         return retVal;
      } catch (SQLException var4) {
         if (var4.getErrorCode() == SqlStates.OBJECT_NOT_FOUND) {
            throw new DvdplayException(5013, var4);
         } else {
            throw new DvdplayException(5004, var4);
         }
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public int executeUpdate(String sql) throws DvdplayException {
      try {
         return this.mStatement.executeUpdate(sql);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public int getUpdateCount() throws DvdplayException {
      try {
         return this.mStatement.getUpdateCount();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public int getFetchDirection() throws DvdplayException {
      try {
         return this.mStatement.getFetchDirection();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public int getFetchSize() throws DvdplayException {
      try {
         return this.mStatement.getFetchSize();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public int getMaxRows() throws DvdplayException {
      try {
         return this.mStatement.getMaxRows();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean getMoreResults() throws DvdplayException {
      try {
         return this.mStatement.getMoreResults();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public int getQueryTimeout() throws DvdplayException {
      try {
         return this.mStatement.getQueryTimeout();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void setQueryTimeout(int seconds) throws DvdplayException {
      try {
         this.mStatement.setQueryTimeout(seconds);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public SQLWarning getWarnings() throws DvdplayException {
      try {
         return this.mStatement.getWarnings();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void setFetchDirection(int direction) throws DvdplayException {
      try {
         this.mStatement.setFetchDirection(direction);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public void setFetchSize(int rows) throws DvdplayException {
      try {
         this.mStatement.setFetchSize(rows);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public void setMaxRows(int max) throws DvdplayException {
      try {
         this.mStatement.setMaxRows(max);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public boolean absolute(int row) throws DvdplayException {
      try {
         return this.mResultSet.absolute(row);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public void afterLast() throws DvdplayException {
      try {
         this.mResultSet.afterLast();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void beforeFirst() throws DvdplayException {
      try {
         this.mResultSet.beforeFirst();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void cancelRowUpdates() throws DvdplayException {
      try {
         this.mResultSet.cancelRowUpdates();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void deleteRow() throws DvdplayException {
      try {
         this.mResultSet.deleteRow();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public int findColumn(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.findColumn(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public boolean columnExists(String columnName) throws DvdplayException {
      try {
         int col = this.mResultSet.findColumn(columnName);
         return true;
      } catch (SQLException var4) {
         if (var4.getSQLState() == SqlStates.COLUMN_NOT_FOUND) {
            return false;
         } else {
            throw new DvdplayException(5004, var4);
         }
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public boolean first() throws DvdplayException {
      try {
         return this.mResultSet.first();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public Array getArray(int i) throws DvdplayException {
      try {
         return this.getArray(i);
      } catch (Exception var3) {
         throw new DvdplayException(5004, var3);
      }
   }

   public Array getArray(String colName) throws DvdplayException {
      try {
         return this.mResultSet.getArray(colName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public BigDecimal getBigDecimal(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getBigDecimal(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public BigDecimal getBigDecimal(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getBigDecimal(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public boolean getBoolean(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getBoolean(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public boolean getBoolean(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getBoolean(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public byte getByte(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getByte(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public byte getByte(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getByte(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public byte[] getBytes(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getBytes(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public byte[] getBytes(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getBytes(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Date getDate(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getDate(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Date getDate(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getDate(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Date getDate(int columnIndex, Calendar cal) throws DvdplayException {
      try {
         return this.mResultSet.getDate(columnIndex, cal);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public Date getDate(String columnName, Calendar cal) throws DvdplayException {
      try {
         return this.mResultSet.getDate(columnName, cal);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public double getDouble(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getDouble(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public double getDouble(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getDouble(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public float getFloat(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getFloat(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public float getFloat(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getFloat(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public int getInt(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getInt(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public int getInt(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getInt(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public long getLong(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getLong(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public long getLong(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getLong(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Object getObject(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getObject(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Object getObject(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getObject(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public int getRow() throws DvdplayException {
      try {
         return this.mResultSet.getRow();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public short getShort(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getShort(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public short getShort(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getShort(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public String getString(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getString(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public String getString(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getString(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Time getTime(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getTime(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Time getTime(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getTime(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Time getTime(int columnIndex, Calendar cal) throws DvdplayException {
      try {
         return this.mResultSet.getTime(columnIndex, cal);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public Time getTime(String columnName, Calendar cal) throws DvdplayException {
      try {
         return this.mResultSet.getTime(columnName, cal);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public Timestamp getTimestamp(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getTimestamp(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Timestamp getTimestamp(String columnName) throws DvdplayException {
      try {
         return this.mResultSet.getTimestamp(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public Timestamp getTimestamp(int columnIndex, Calendar cal) throws DvdplayException {
      try {
         return this.mResultSet.getTimestamp(columnIndex, cal);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public Timestamp getTimestamp(String columnName, Calendar cal) throws DvdplayException {
      try {
         return this.mResultSet.getTimestamp(columnName, cal);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void insertRow() throws DvdplayException {
      try {
         this.mResultSet.insertRow();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean isAfterLast() throws DvdplayException {
      try {
         return this.mResultSet.isAfterLast();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean isFirst() throws DvdplayException {
      try {
         return this.mResultSet.isFirst();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean isLast() throws DvdplayException {
      try {
         return this.mResultSet.isLast();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean last() throws DvdplayException {
      try {
         return this.mResultSet.last();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void moveToCurrentRow() throws DvdplayException {
      try {
         this.mResultSet.moveToCurrentRow();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void moveToInsertRow() throws DvdplayException {
      try {
         this.mResultSet.moveToInsertRow();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean next() throws DvdplayException {
      try {
         return this.mResultSet.next();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean previous() throws DvdplayException {
      try {
         return this.mResultSet.previous();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void refreshRow() throws DvdplayException {
      try {
         this.mResultSet.refreshRow();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean relative(int rows) throws DvdplayException {
      try {
         return this.mResultSet.relative(rows);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public boolean rowDeleted() throws DvdplayException {
      try {
         return this.mResultSet.rowDeleted();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean rowInserted() throws DvdplayException {
      try {
         return this.mResultSet.rowInserted();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean rowUpdated() throws DvdplayException {
      try {
         return this.mResultSet.rowUpdated();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void updateArray(int i, Array x) throws DvdplayException {
      try {
         this.mResultSet.updateArray(i, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateArray(String colName, Array x) throws DvdplayException {
      try {
         this.mResultSet.updateArray(colName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateBigDecimal(int columnIndex, BigDecimal x) throws DvdplayException {
      try {
         this.mResultSet.updateBigDecimal(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateBigDecimal(String columnName, BigDecimal x) throws DvdplayException {
      try {
         this.mResultSet.updateBigDecimal(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateBoolean(int columnIndex, boolean x) throws DvdplayException {
      try {
         this.mResultSet.updateBoolean(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateBoolean(String columnName, boolean x) throws DvdplayException {
      try {
         this.mResultSet.updateBoolean(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateByte(int columnIndex, byte x) throws DvdplayException {
      try {
         this.mResultSet.updateByte(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateByte(String columnName, byte x) throws DvdplayException {
      try {
         this.mResultSet.updateByte(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateBytes(int columnIndex, byte[] x) throws DvdplayException {
      try {
         this.mResultSet.updateBytes(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateBytes(String columnName, byte[] x) throws DvdplayException {
      try {
         this.mResultSet.updateBytes(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateDate(int columnIndex, Date x) throws DvdplayException {
      try {
         this.mResultSet.updateDate(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateDate(String columnName, Date x) throws DvdplayException {
      try {
         this.mResultSet.updateDate(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateDouble(int columnIndex, double x) throws DvdplayException {
      try {
         this.mResultSet.updateDouble(columnIndex, x);
      } catch (SQLException var6) {
         throw new DvdplayException(5004, var6);
      } catch (Exception var7) {
         throw new DvdplayException(5004, var7);
      }
   }

   public void updateDouble(String columnName, double x) throws DvdplayException {
      try {
         this.mResultSet.updateDouble(columnName, x);
      } catch (SQLException var6) {
         throw new DvdplayException(5004, var6);
      } catch (Exception var7) {
         throw new DvdplayException(5004, var7);
      }
   }

   public void updateFloat(int columnIndex, float x) throws DvdplayException {
      try {
         this.mResultSet.updateFloat(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateFloat(String columnName, float x) throws DvdplayException {
      try {
         this.mResultSet.updateFloat(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public int updateInt(int columnIndex) throws DvdplayException {
      try {
         return this.mResultSet.getInt(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public void updateInt(String columnName, int x) throws DvdplayException {
      try {
         this.mResultSet.updateInt(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateLong(int columnIndex, long x) throws DvdplayException {
      try {
         this.mResultSet.updateLong(columnIndex, x);
      } catch (SQLException var6) {
         throw new DvdplayException(5004, var6);
      } catch (Exception var7) {
         throw new DvdplayException(5004, var7);
      }
   }

   public void updateLong(String columnName, long x) throws DvdplayException {
      try {
         this.mResultSet.updateLong(columnName, x);
      } catch (SQLException var6) {
         throw new DvdplayException(5004, var6);
      } catch (Exception var7) {
         throw new DvdplayException(5004, var7);
      }
   }

   public void updatebject(int columnIndex, Object x) throws DvdplayException {
      try {
         this.mResultSet.updateObject(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateObject(String columnName, Object x) throws DvdplayException {
      try {
         this.mResultSet.updateObject(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateNull(int columnIndex) throws DvdplayException {
      try {
         this.mResultSet.updateNull(columnIndex);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public void updateNull(String columnName) throws DvdplayException {
      try {
         this.mResultSet.updateNull(columnName);
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }

   public void updateRow() throws DvdplayException {
      try {
         this.mResultSet.updateRow();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public void updateShort(int columnIndex, short x) throws DvdplayException {
      try {
         this.mResultSet.updateShort(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateShort(String columnName, short x) throws DvdplayException {
      try {
         this.mResultSet.updateShort(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateString(int columnIndex, String x) throws DvdplayException {
      try {
         this.mResultSet.updateString(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateString(String columnName, String x) throws DvdplayException {
      try {
         this.mResultSet.updateString(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateTime(int columnIndex, Time x) throws DvdplayException {
      try {
         this.mResultSet.updateTime(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateTime(String columnName, Time x) throws DvdplayException {
      try {
         this.mResultSet.updateTime(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateTimestamp(int columnIndex, Timestamp x) throws DvdplayException {
      try {
         this.mResultSet.updateTimestamp(columnIndex, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void updateTimestamp(String columnName, Timestamp x) throws DvdplayException {
      try {
         this.mResultSet.updateTimestamp(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public void getTimestamp(String columnName, Timestamp x) throws DvdplayException {
      try {
         this.mResultSet.updateTimestamp(columnName, x);
      } catch (SQLException var5) {
         throw new DvdplayException(5004, var5);
      } catch (Exception var6) {
         throw new DvdplayException(5004, var6);
      }
   }

   public boolean wasNull() throws DvdplayException {
      try {
         return this.mResultSet.wasNull();
      } catch (SQLException var3) {
         throw new DvdplayException(5004, var3);
      } catch (Exception var4) {
         throw new DvdplayException(5004, var4);
      }
   }

   public boolean isNull(String columnName) throws DvdplayException {
      try {
         Object obj = this.mResultSet.getObject(columnName);
         return this.mResultSet.wasNull();
      } catch (SQLException var4) {
         throw new DvdplayException(5004, var4);
      } catch (Exception var5) {
         throw new DvdplayException(5004, var5);
      }
   }
}
