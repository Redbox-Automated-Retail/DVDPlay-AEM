package net.dvdplay.aemcontroller;

import java.io.InputStream;
import java.util.Properties;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.Log;
import net.dvdplay.screen.DvdplayScreenException;

public class CommandMap {
   Properties properties = new Properties();
   InputStream in;
   InputStream special;

   public String getNextScreen(String home, String action) {
      if (this.properties.containsKey(home + "," + action)) {
         return ((String)this.properties.get(home + "," + action)).trim();
      } else {
         throw new DvdplayScreenException("[CommandMap] Could not find next screen for Curr: " + home + "; Action: " + action);
      }
   }

   public void init(int franchiseID) {
      try {
         this.in = this.getClass().getResourceAsStream("/conf/UIFlow.conf");
         this.properties.load(this.in);
         AemFactory.getInstance();
         String specialFileName = Aem.getProperty("EXTRA_UIFLOW");
         if (AemFactory.getInstance().isDebugModeOn() && specialFileName.length() > 0) {
            Log.debug("Reading Special Debug UIFlow File " + specialFileName);
            this.special = this.getClass().getResourceAsStream("/conf/" + specialFileName);
         } else {
            this.special = this.getClass().getResourceAsStream("/conf/" + franchiseID + "UIFlow" + ".conf");
         }

         if (this.special != null) {
            this.properties.load(this.special);
            Log.info("Using franchise" + franchiseID + "-specific configuration in addition to default");
         }
      } catch (Exception e) {
         Log.error(e, "[CommandMap] Error loading the properties from file");
         throw new DvdplayException("[CommandMap] Error loading the properties from file");
      } finally {
         try {
            this.in.close();
            if (this.special != null) {
               this.special.close();
            }
         } catch (Exception e) {
            Log.error(e, "[CommandMap] Can't close inputstream");
         }
      }
   }
}
