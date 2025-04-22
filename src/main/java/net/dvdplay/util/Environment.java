package net.dvdplay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;
import net.dvdplay.dataaccess.IDataProvider;
import net.dvdplay.dataaccess.IDataProviderFactory;
import net.dvdplay.dataaccess.IProcedure;
import net.dvdplay.dataaccess.mssql.MsSqlProviderFactory;

public class Environment implements IEnvironment {
   private static Vector mgInstances = new Vector();
   private String mName = null;
   private Properties constantProps = null;
   private Properties appProperties = null;
   private String appInitFailed = "";

   public static synchronized Environment getInstance(String aName, String aConstPropName, String aConstPropVal) {
      for (int li = 0; li < mgInstances.size(); li++) {
         Environment lEnv = (Environment)mgInstances.elementAt(li);
         if (aName.compareTo(lEnv.getName()) == 0) {
            return lEnv;
         }
      }

      Environment var5 = new Environment(aName, aConstPropName, aConstPropVal);
      mgInstances.addElement(var5);
      return var5;
   }

   private Environment(String aName, String aConstPropName, String aConstPropVal) {
      this.constantProps = new Properties();
      this.constantProps.setProperty(aConstPropName, aConstPropVal);
      this.appProperties = new Properties();
      this.mName = aName;
   }

   public String getProperty(String name) {
      String retValue = null;
      if (this.appProperties != null) {
         retValue = this.appProperties.getProperty(name);
      }

      if (retValue == null && this.constantProps != null) {
         retValue = this.constantProps.getProperty(name);
      }

      return retValue;
   }

   public String getDynamicProperty(String name) {
      String retVal = "";
      IDataProvider provider = null;

      label49:
      try {
         try {
            String dbHost = this.getProperty("db.host");
            String dbUser = this.getProperty("db.user");
            String dbPassword = this.getProperty("db.password");
            IDataProviderFactory factory = new MsSqlProviderFactory(dbHost, dbUser, dbPassword);
            provider = factory.getDataProvider();
            provider.open();
            IProcedure query = provider.createProcedure("{call ffGetServerConfig(?, ?, ?)}");
            query.setString(1, name);
            query.registerOutParameter(2, 12);
            query.registerOutParameter(3, 12);
            boolean isAResultSet = query.execute();
            if (!isAResultSet) {
               retVal = query.getString(2);
            }
         } catch (Exception var19) {
         }
      } finally {
         break label49;
      }

      try {
         provider.commit();
      } catch (Exception var18) {
      }

      try {
         provider.close();
      } catch (Exception var17) {
      }

      return retVal;
   }

   public String getName() {
      return this.mName;
   }

   public String getProperty(String name, String defaultValue) {
      String retValue = null;
      if (this.appProperties != null) {
         retValue = this.appProperties.getProperty(name);
      }

      if (retValue == null) {
         retValue = this.constantProps.getProperty(name);
      }

      return retValue == null ? defaultValue : retValue;
   }

   public void initAppProperties(String propertiesFilename) {
      InputStream in = null;
      String path = System.getProperty(propertiesFilename);
      if (path != null) {
         try {
            in = new FileInputStream(path);
         } catch (FileNotFoundException var9) {
            this.appInitFailed = "File not found: " + path;
            return;
         }
      }

      if (in == null) {
         ClassLoader classLoader = this.getClass().getClassLoader();
         in = classLoader.getResourceAsStream(propertiesFilename);
      }

      if (in == null) {
         ClassLoader classLoader = Environment.class
            .getClassLoader();
         in = classLoader.getResourceAsStream(propertiesFilename);
      }

      if (in == null) {
         String userDir = System.getProperty("user.dir");
         File propFile = new File(userDir, propertiesFilename);

         try {
            in = new FileInputStream(propFile);
         } catch (FileNotFoundException var8) {
            in = null;
         }
      }

      if (in == null) {
         this.appInitFailed = "Could not get resource file '" + propertiesFilename + "'";
      } else {
         try {
            this.appProperties.load(in);
         } catch (IOException var7) {
            this.appInitFailed = "Could not load properties file " + propertiesFilename + ": " + var7;
         }
      }
   }

   public void setConstantProperty(String name, String value) {
      this.constantProps.setProperty(name, value);
   }

   public void setProperty(String name, String value) {
      this.appProperties.setProperty(name, value);
   }
}
