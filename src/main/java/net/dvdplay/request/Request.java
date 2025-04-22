package net.dvdplay.request;

import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;

public abstract class Request implements IRequest {
   static int HEADER_SIZE = 4;
   NvPairSet parameters = null;
   public static final String NAME_REQUEST_NAME = "REQUEST_NAME";
   public static final String NAME_SENDER_TYPE = "SENDER_TYPE";
   public static final String NAME_SENDER_ID = "SENDER_ID";
   public static final String NAME_VERSION = "VERSION";

   protected Request(NvPairSet payload) throws DvdplayException {
      NvPair reqNamePair = payload.getNvPair("REQUEST_NAME");
      String reqName = reqNamePair.getValueAsString();
      NvPair senderTypePair = payload.getNvPair("SENDER_TYPE");
      String senderType = senderTypePair.getValueAsString();
      NvPair senderIdPair = payload.getNvPair("SENDER_ID");
      int senderId = Integer.parseInt(senderIdPair.getValueAsString());
      NvPair versionPair = payload.getNvPair("VERSION");
      String version = versionPair.getValueAsString();
      this.init(reqName, senderType, senderId, version);
   }

   protected Request(String requestName, String senderType, int senderId, String version) {
      this.init(requestName, senderType, senderId, version);
   }

   private void init(String requestName, String senderType, int senderId, String version) {
      this.parameters = new NvPairSet();
      this.setParameter("REQUEST_NAME", "STRING", requestName);
      this.setParameter("SENDER_TYPE", "STRING", senderType);
      this.setParameter("SENDER_ID", "INTEGER", Integer.toString(senderId));
      this.setParameter("VERSION", "STRING", version);
   }

   protected int headerSize() {
      return HEADER_SIZE;
   }

   protected NvPairSet getHeaderParameters() {
      NvPairSet headerValues = new NvPairSet();

      for (int i = 0; i < HEADER_SIZE; i++) {
         headerValues.add(this.parameters.getNvPair(i));
      }

      return headerValues;
   }

   protected NvPairSet getUserParameters() {
      NvPairSet userValues = new NvPairSet();
      int count = this.parameters.size();

      for (int i = HEADER_SIZE; i < count; i++) {
         userValues.add(this.parameters.getNvPair(i));
      }

      return userValues;
   }

   protected NvPairSet getParameters(int loIndex, int hiIndex) throws DvdplayException {
      NvPairSet userValues = new NvPairSet();
      int count = this.parameters.size();
      if (loIndex >= 0 && hiIndex < count) {
         for (int i = loIndex + 1; i <= hiIndex; i++) {
            userValues.add(this.parameters.getNvPair(i));
         }

         return userValues;
      } else {
         throw new DvdplayException("Index out of range.");
      }
   }

   protected void addUserParameters(NvPairSet userParameters) {
      int count = userParameters.size();

      for (int i = 0; i < count; i++) {
         NvPair valuePair = userParameters.getNvPair(i);
         this.parameters.add(valuePair);
      }
   }

   protected int size() {
      return this.parameters.size();
   }

   public String getRequestName() throws DvdplayException {
      try {
         return this.getParameterValueAsString("REQUEST_NAME");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getSenderType() throws DvdplayException {
      try {
         return this.getParameterValueAsString("SENDER_TYPE");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public int getSenderId() throws DvdplayException {
      try {
         return Integer.parseInt(this.getParameterValueAsString("SENDER_ID"));
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getVersion() throws DvdplayException {
      try {
         return this.getParameterValueAsString("VERSION");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   protected void setParameter(String name, String type, String value) {
      NvPair nvPair = new NvPair(name, type, value);
      this.parameters.add(nvPair);
   }

   protected void setParameter(String name, String type, RCSet value) {
      NvPair nvPair = new NvPair(name, type, value);
      this.parameters.add(nvPair);
   }

   public String getParameterValueAsString(String name) throws DvdplayException {
      try {
         int index = this.parameters.findName(name);
         NvPair nvPair = this.parameters.getNvPair(index);
         return nvPair.getValueAsString();
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public RCSet getParameterValueAsRCSet(String name) throws DvdplayException {
      try {
         int index = this.parameters.findName(name);
         NvPair nvPair = this.parameters.getNvPair(index);
         return nvPair.getValueAsRCSet();
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public String getParameterType(String name) throws DvdplayException {
      try {
         int index = this.parameters.findName(name);
         NvPair nvPair = this.parameters.getNvPair(index);
         return nvPair.getType();
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public boolean isParameterFormat_STRING(String name) throws DvdplayException {
      try {
         int index = this.parameters.findName(name);
         NvPair nvPair = this.parameters.getNvPair(index);
         return nvPair.isFormat_STRING();
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public boolean isParameterFormat_RCSET(String name) throws DvdplayException {
      try {
         int index = this.parameters.findName(name);
         NvPair nvPair = this.parameters.getNvPair(index);
         return nvPair.isFormat_RCSET();
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public String getAsXmlString() throws DvdplayException {
      try {
         DataPacketComposer dc = new DataPacketComposer();
         return this.getAsXmlString(dc);
      } catch (DvdplayException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   private String getAsXmlString(DataPacketComposer dataPacketComposer) throws DvdplayException {
      try {
         return dataPacketComposer.nvMarshal(this.parameters);
      } catch (DvdplayException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new DvdplayException(4000, var5);
      }
   }

   public String getPrettyXmlString() throws DvdplayException {
      try {
         DataPacketComposer dc = new DataPacketComposer();
         String xmlString = this.getAsXmlString(dc);
         return dc.prettyPrint(xmlString);
      } catch (DvdplayException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   public static String getPrettyXmlString(String xmlString) throws DvdplayException {
      try {
         DataPacketComposer dc = new DataPacketComposer();
         return dc.prettyPrint(xmlString);
      } catch (DvdplayException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }
}
