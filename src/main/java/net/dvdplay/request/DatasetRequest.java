package net.dvdplay.request;

import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;

public class DatasetRequest extends Request {
   private static String NAME_DATASET = "DATASET";
   private static String NAME_KIOSK = "KIOSK";
   private static final String NAME_DATASET_NAME_0 = "DATASET_NAME_0";
   private static final String NAME_DATASET_DATA_0 = "DATASET_DATA_0";
   private static final String NAME_DATASET_COUNT = "DATASET_COUNT";

   public DatasetRequest(int kioskId, String version, String result, String resultMessage, String datasetName, RCSet dataset) throws DvdplayException {
      super(NAME_DATASET, NAME_KIOSK, kioskId, version);

      try {
         super.setParameter("RESULT", "STRING", result);
         super.setParameter("RESULT_MESSAGE", "STRING", resultMessage);
         super.setParameter("DATASET_COUNT", "INT", Integer.toString(1));
         super.setParameter("DATASET_NAME_0", "STRING", datasetName);
         super.setParameter("DATASET_DATA_0", "RCSET", dataset);
      } catch (Exception var8) {
         throw new DvdplayException(4000, var8);
      }
   }

   public DatasetRequest(int kioskId, String version, String result, String resultMessage, String[] datasetName, RCSet[] dataset) throws DvdplayException {
      super("DATASET", "KIOSK", kioskId, version);

      try {
         super.setParameter("RESULT", "STRING", result);
         super.setParameter("RESULT_MESSAGE", "STRING", resultMessage);
         int validCount = 0;

         for (int i = 0; i < datasetName.length && datasetName[i] != null && datasetName[i] != "" && dataset[i] != null; i++) {
            validCount++;
         }

         super.setParameter("DATASET_COUNT", "INT", Integer.toString(validCount));

         for (int i = 0; i < validCount; i++) {
            super.setParameter("DATASET_NAME_" + Integer.toString(i), "STRING", datasetName[i]);
            super.setParameter("DATASET_DATA_" + Integer.toString(i), "RCSET", dataset[i]);
         }
      } catch (Exception var10) {
         throw new DvdplayException(4000, var10);
      }
   }

   public DatasetRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         NvPair resultPair = payload.getNvPair("RESULT");
         String result = resultPair.getValueAsString();
         NvPair resultMessagePair = payload.getNvPair("RESULT_MESSAGE");
         String resultMessage = resultMessagePair.getValueAsString();
         super.setParameter("RESULT", "STRING", result);
         super.setParameter("RESULT_MESSAGE", "STRING", resultMessage);
         NvPair datasetCountPair = payload.getNvPair("DATASET_COUNT");
         String datasetCount = datasetCountPair.getValueAsString();
         super.setParameter("DATASET_COUNT", "INT", datasetCount);
         int howMany = Integer.parseInt(datasetCount);

         for (int i = 0; i < howMany; i++) {
            String fName = "DATASET_NAME_" + Integer.toString(i);
            NvPair datasetNamePair = payload.getNvPair(fName);
            String datasetName = datasetNamePair.getValueAsString();
            super.setParameter(fName, "STRING", datasetName);
            fName = "DATASET_DATA_" + Integer.toString(i);
            NvPair datasetDataPair = payload.getNvPair(fName);
            RCSet datasetData = datasetDataPair.getValueAsRCSet();
            super.setParameter(fName, "RCSET", datasetData);
         }
      } catch (Exception var15) {
         throw new DvdplayException(4000, var15);
      }
   }

   public int getDatasetCount() throws DvdplayException {
      try {
         String countStr = super.getParameterValueAsString("DATASET_COUNT");
         return Integer.parseInt(countStr);
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String[] getDatasetName() throws DvdplayException {
      try {
         int howMany = this.getDatasetCount();
         String[] retVal = new String[howMany];

         for (int i = 0; i < howMany; i++) {
            retVal[i] = super.getParameterValueAsString("DATASET_NAME_" + Integer.toString(i));
         }

         return retVal;
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public RCSet[] getDatasetData() throws DvdplayException {
      try {
         int howMany = this.getDatasetCount();
         RCSet[] retVal = new RCSet[howMany];

         for (int i = 0; i < howMany; i++) {
            retVal[i] = super.getParameterValueAsRCSet("DATASET_DATA_" + Integer.toString(i));
         }

         return retVal;
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }
}
