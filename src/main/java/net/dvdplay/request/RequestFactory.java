package net.dvdplay.request;

import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.Log;

public class RequestFactory {
   private static final String NAME_GET_FRAGMENT = "GET_FRAGMENT";
   private static final String NAME_DATASET = "DATASET";
   private static final String NAME_DATA_RECORD = "DATA_RECORD";
   private static final String NAME_QUERY = "QUERY";
   private static final String NAME_SYNCH = "SYNCH";
   private static final String NAME_ASYNCH = "ASYNCH";
   private static final String NAME_GET_RECORD = "GET_RECORD";
   private static final String NAME_GET_PK_RECORD = "GET_PK_RECORD";
   private static final String NAME_REQUEST_NAME = "REQUEST_NAME";

   private RequestFactory() {
   }

   public static Request makeRequest(String xmlString) throws DvdplayException {
      Request retReq = null;

      try {
         DataPacketComposer dc = new DataPacketComposer();
         NvPairSet payload = dc.nvDeMarshal(xmlString);
         int reqNameIdx = payload.findName("REQUEST_NAME");
         NvPair reqNameEntry = payload.getNvPair(reqNameIdx);
         String reqName = reqNameEntry.getValueAsString();
         if ("PING".equalsIgnoreCase(reqName)) {
            retReq = new PingRequest(payload);
         } else if ("GET_FRAGMENT".equalsIgnoreCase(reqName)) {
            retReq = new GetFragmentRequest(payload);
         } else if ("DATASET".equalsIgnoreCase(reqName)) {
            retReq = new DatasetRequest(payload);
         } else if ("DATA_RECORD".equalsIgnoreCase(reqName)) {
            retReq = new DataRecordRequest(payload);
         } else if ("QUERY".equalsIgnoreCase(reqName)) {
            retReq = new QueryRequest(payload);
         } else if ("SYNCH".equalsIgnoreCase(reqName)) {
            retReq = new SynchRequest(payload);
         } else if ("ASYNCH".equalsIgnoreCase(reqName)) {
            retReq = new AsynchRequest(payload);
         } else if ("GET_RECORD".equalsIgnoreCase(reqName)) {
            retReq = new GetRecordRequest(payload);
         } else {
            if (!"GET_PK_RECORD".equalsIgnoreCase(reqName)) {
               throw new DvdplayException(4001, "Name (" + reqName + ") unknown to Request Factory.");
            }

            retReq = new GetPKRecordRequest(payload);
         }

         return retReq;
      } catch (DvdplayException var7) {
         throw var7;
      } catch (Exception var8) {
         throw new DvdplayException(4000, var8);
      }
   }

   public static String getFailureResponse(int senderId, String senderType, Exception anException) {
      String[] responseNames = new String[]{"RESULT", "RESULT_MESSAGE"};
      String[] responseTypes = new String[]{"INTEGER", "STRING"};
      String[] responseValues = new String[]{"0", "FAILURE"};
      DataRecordRequest resPacket = null;
      String retVal = "";

      try {
         if (anException instanceof DvdplayException) {
            responseValues[0] = ((DvdplayException)anException).getCodeAsString();
         } else {
            responseValues[0] = String.valueOf(1002);
         }

         responseValues[1] = anException.getMessage();
         NvPairSet result = new NvPairSet(responseNames, responseTypes, responseValues);
         resPacket = new DataRecordRequest(senderId, senderType, "1.0", result);
         return resPacket.getAsXmlString();
      } catch (Exception var9) {
         Log.summary("Error generating a failure response. No xml response generated.");
         throw new DvdplayException(1008, var9);
      }
   }
}
