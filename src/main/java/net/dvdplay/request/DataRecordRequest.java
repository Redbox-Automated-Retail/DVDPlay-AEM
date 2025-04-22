package net.dvdplay.request;

import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;

public class DataRecordRequest extends Request {
   private static String NAME_DATA_RECORD = "DATA_RECORD";
   private static String NAME_KIOSK = "KIOSK";

   public DataRecordRequest(int kioskId, String version, NvPairSet userRecord) throws DvdplayException {
      super(NAME_DATA_RECORD, NAME_KIOSK, kioskId, version);

      try {
         super.addUserParameters(userRecord);
      } catch (Exception var5) {
         throw new DvdplayException(4000, var5);
      }
   }

   public DataRecordRequest(int senderId, String senderType, String version, NvPairSet userRecord) throws DvdplayException {
      super(NAME_DATA_RECORD, senderType, senderId, version);

      try {
         super.addUserParameters(userRecord);
      } catch (Exception var6) {
         throw new DvdplayException(4000, var6);
      }
   }

   public DataRecordRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         int headerSize = super.headerSize();
         int totElements = payload.size();
         NvPairSet userParms = new NvPairSet();

         for (int i = headerSize; i < totElements; i++) {
            NvPair aPair = payload.getNvPair(i);
            userParms.add(aPair);
         }

         super.addUserParameters(userParms);
      } catch (Exception var7) {
         throw new DvdplayException(4000, var7);
      }
   }

   public NvPairSet getUserRecord() throws DvdplayException {
      try {
         return super.getUserParameters();
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }
}
