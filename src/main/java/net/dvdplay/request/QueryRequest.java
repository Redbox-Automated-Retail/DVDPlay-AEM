package net.dvdplay.request;

import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;

public class QueryRequest extends Request {
   private static final String NAME_QUERY = "QUERY";
   private static final String NAME_QUERY_NAME = "QUERY_NAME";
   private static final String NAME_QUERY_STRING = "QUERY_STRING";

   public QueryRequest(int kioskId, String version, String queryName, String queryString) throws DvdplayException {
      super("QUERY", "KIOSK", kioskId, version);

      try {
         super.setParameter("QUERY_NAME", "STRING", queryName);
         super.setParameter("QUERY_STRING", "STRING", queryString);
      } catch (Exception var6) {
         throw new DvdplayException(4000, var6);
      }
   }

   public QueryRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         NvPair queryNamePair = payload.getNvPair("QUERY_NAME");
         String queryName = queryNamePair.getValueAsString();
         super.setParameter("QUERY_NAME", "STRING", queryName);
         NvPair queryStringPair = payload.getNvPair("QUERY_STRING");
         String queryString = queryStringPair.getValueAsString();
         super.setParameter("QUERY_STRING", "STRING", queryString);
      } catch (Exception var6) {
         throw new DvdplayException(4000, var6);
      }
   }

   public String getQueryName() throws DvdplayException {
      try {
         return super.getParameterValueAsString("QUERY_NAME");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getQueryString() throws DvdplayException {
      try {
         return super.getParameterValueAsString("QUERY_STRING");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }
}
