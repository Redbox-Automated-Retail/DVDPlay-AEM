package net.dvdplay.util;

public interface IEnvironment {
   String PROPERTY_AEM_SERVER_ID = "AEM_SERVER_ID";
   String PROPERTY_AEM_SERVER_SENDER_TYPE = "AEM_SERVER";
   String PROPERTY_ADMIN_ID = "ADMIN_ID";
   String PROPERTY_ADMIN_SENDER_TYPE = "ADMIN";
   String PROPERTY_MONITOR_SERVER_ID = "MONITOR_SERVER_ID";
   String PROPERTY_MONITOR_SERVER_SENDER_TYPE = "MONITOR_SERVER";

   String getProperty(String var1, String var2);

   void initAppProperties(String var1);

   void setConstantProperty(String var1, String var2);

   void setProperty(String var1, String var2);
}
