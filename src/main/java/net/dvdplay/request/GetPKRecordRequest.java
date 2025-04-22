package net.dvdplay.request;

import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;

public class GetPKRecordRequest extends Request {
   private static final String NAME_TABLE_NAME = "TABLE_NAME";
   private static final String NAME_PK_FIELD = "PK_FIELD";
   private static final String NAME_PK_TYPE = "PK_TYPE";
   private static final String NAME_PK_VALUE = "PK_VALUE";
   private static final String NAME_GET_PK_RECORD = "GET_PK_RECORD";

   public GetPKRecordRequest(int kioskId, String version, String tableName, String pkField, String pkValue) throws DvdplayException {
      super("GET_PK_RECORD", "KIOSK", kioskId, version);

      try {
         super.setParameter("TABLE_NAME", "STRING", tableName);
         super.setParameter("PK_FIELD", "STRING", pkField);
         super.setParameter("PK_TYPE", "STRING", "STRING");
         super.setParameter("PK_VALUE", "STRING", pkValue);
      } catch (Exception var7) {
         throw new DvdplayException(4000, var7);
      }
   }

   public GetPKRecordRequest(int kioskId, String version, String tableName, String pkField, int pkValue) throws DvdplayException {
      super("GET_PK_RECORD", "KIOSK", kioskId, version);

      try {
         super.setParameter("TABLE_NAME", "STRING", tableName);
         super.setParameter("PK_FIELD", "STRING", pkField);
         super.setParameter("PK_TYPE", "STRING", "INT");
         super.setParameter("PK_VALUE", "STRING", Integer.toString(pkValue));
      } catch (Exception var7) {
         throw new DvdplayException(4000, var7);
      }
   }

   public GetPKRecordRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         NvPair tableNamePair = payload.getNvPair("TABLE_NAME");
         String tableName = tableNamePair.getValueAsString();
         super.setParameter("TABLE_NAME", "STRING", tableName);
         NvPair pkFieldPair = payload.getNvPair("PK_FIELD");
         String pkField = pkFieldPair.getValueAsString();
         super.setParameter("PK_FIELD", "STRING", pkField);
         NvPair pkTypePair = payload.getNvPair("PK_TYPE");
         String pkType = pkTypePair.getValueAsString();
         super.setParameter("PK_TYPE", "STRING", pkType);
         NvPair pkValuePair = payload.getNvPair("PK_VALUE");
         String pkValue = pkValuePair.getValueAsString();
         super.setParameter("PK_VALUE", "STRING", pkValue);
      } catch (Exception var10) {
         throw new DvdplayException(4000, var10);
      }
   }

   public String getTableName() throws DvdplayException {
      try {
         return super.getParameterValueAsString("TABLE_NAME");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getPKField() throws DvdplayException {
      try {
         return super.getParameterValueAsString("PK_FIELD");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getPKValueAsString() throws DvdplayException {
      try {
         return super.getParameterValueAsString("PK_VALUE");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public int getPKValueAsInt() throws DvdplayException {
      try {
         return Integer.parseInt(super.getParameterValueAsString("PK_VALUE"));
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getPKValue() throws DvdplayException {
      try {
         return super.getParameterValueAsString("PK_VALUE");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getPKType() throws DvdplayException {
      try {
         return super.getParameterValueAsString("PK_TYPE");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }
}
