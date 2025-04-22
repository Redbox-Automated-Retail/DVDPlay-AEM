package net.dvdplay.request;

import java.util.Date;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;

public class GetFragmentRequest extends Request {
   private static final String NAME_FRAGMENT_COUNT = "FRAGMENT_COUNT";
   private static final String NAME_SINCE_DATE = "SINCE_DATE";
   private static final String NAME_FRAGMENT_NAME_0 = "FRAGMENT_NAME_0";
   private static final String NAME_GET_FRAGMENT = "GET_FRAGMENT";

   public GetFragmentRequest(int kioskId, String version, String fragmentName, Date sinceDate) throws DvdplayException {
      super("GET_FRAGMENT", "KIOSK", kioskId, version);

      try {
         super.setParameter("FRAGMENT_COUNT", "INT", "1");
         super.setParameter("FRAGMENT_NAME_0", "STRING", fragmentName);
         super.setParameter("SINCE_DATE", "DATE", sinceDate.toString());
      } catch (Exception var6) {
         throw new DvdplayException(4000, var6);
      }
   }

   public GetFragmentRequest(int kioskId, String version, String[] fragmentName, Date sinceDate) throws DvdplayException {
      super("GET_FRAGMENT", "KIOSK", kioskId, version);

      try {
         int howMany = 0;

         for (int i = 0; i < fragmentName.length && fragmentName[i] != null && fragmentName[i] != ""; i++) {
            howMany++;
         }

         super.setParameter("FRAGMENT_COUNT", "INT", Integer.toString(howMany));

         for (int i = 0; i < howMany; i++) {
            super.setParameter("FRAGMENT_NAME_" + Integer.toString(i), "STRING", fragmentName[i]);
         }

         super.setParameter("SINCE_DATE", "DATE", sinceDate.toString());
      } catch (Exception var8) {
         throw new DvdplayException(4000, var8);
      }
   }

   public GetFragmentRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         NvPair fragmentCountPair = payload.getNvPair("FRAGMENT_COUNT");
         String fragmentCount = fragmentCountPair.getValueAsString();
         super.setParameter("FRAGMENT_COUNT", "INT", fragmentCount);
         int howMany = Integer.parseInt(fragmentCount);

         for (int i = 0; i < howMany; i++) {
            String fName = "FRAGMENT_NAME_" + Integer.toString(i);
            NvPair tableNamePair = payload.getNvPair(fName);
            String tableName = tableNamePair.getValueAsString();
            super.setParameter(fName, "STRING", tableName);
         }

         NvPair sDatePair = payload.getNvPair("SINCE_DATE");
         String sDate = sDatePair.getValueAsString();
         super.setParameter("SINCE_DATE", "DATE", sDate);
      } catch (Exception var9) {
         throw new DvdplayException(4000, var9);
      }
   }

   public int getFragmentCount() throws DvdplayException {
      try {
         String countStr = super.getParameterValueAsString("FRAGMENT_COUNT");
         return Integer.parseInt(countStr);
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String[] getFragmentNames() throws DvdplayException {
      try {
         int howMany = this.getFragmentCount();
         String[] retVal = new String[howMany];

         for (int i = 0; i < howMany; i++) {
            retVal[i] = super.getParameterValueAsString("FRAGMENT_NAME_" + Integer.toString(i));
         }

         return retVal;
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public Date getSinceDate() throws DvdplayException {
      try {
         String sinceDateStr = super.getParameterValueAsString("SINCE_DATE");
         return new Date(sinceDateStr);
      } catch (Exception var3) {
         throw new DvdplayException(4000, var3);
      }
   }
}
