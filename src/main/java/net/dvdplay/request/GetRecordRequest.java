package net.dvdplay.request;

import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;

public class GetRecordRequest extends Request {
   private static final String NAME_GET_RECORD = "GET_RECORD";
   private static final String NAME_TABLE_NAME = "TABLE_NAME";
   private static final String NAME_RECORD_ID = "RECORD_ID";

   public GetRecordRequest(int kioskId, String version, String tableName, String recordId) throws DvdplayException {
      super("GET_RECORD", "KIOSK", kioskId, version);

      try {
         super.setParameter("TABLE_NAME", "STRING", tableName);
         super.setParameter("RECORD_ID", "STRING", recordId);
      } catch (Exception var6) {
         throw new DvdplayException(4000, var6);
      }
   }

   public GetRecordRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         NvPair tableNamePair = payload.getNvPair("TABLE_NAME");
         String tableName = tableNamePair.getValueAsString();
         super.setParameter("TABLE_NAME", "STRING", tableName);
         NvPair recordIdPair = payload.getNvPair("RECORD_ID");
         String recordId = recordIdPair.getValueAsString();
         super.setParameter("RECORD_ID", "STRING", recordId);
      } catch (Exception var6) {
         throw new DvdplayException(4000, var6);
      }
   }

   public String getTableName() throws DvdplayException {
      try {
         return super.getParameterValueAsString("TABLE_NAME");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getRecordId() throws DvdplayException {
      try {
         return super.getParameterValueAsString("RECORD_ID");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }
}
