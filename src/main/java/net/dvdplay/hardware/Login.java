package net.dvdplay.hardware;

import java.util.StringTokenizer;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.logger.DvdplayLevel;

public class Login extends AbstractHardwareThread {
   public static int authenticate(String data) {
      int role = -1;

      try {
         StringTokenizer stk = new StringTokenizer(data, ",");
         String username = stk.nextToken();
         String password = "";
         if (stk.hasMoreTokens()) {
            password = stk.nextToken();
         }

         role = Aem.checkLogin(username, password);
      } catch (Exception var9) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "[LoginAuthentication] " + var9.toString(), var9);
      }

      return role;
   }

   public static void delete(String data) {
   }

   public static boolean addNew(String data) {
      boolean status = true;

      try {
         StringTokenizer stk = new StringTokenizer(data, ",");
         String role = stk.nextToken();
         String user = stk.nextToken();
         String pass = stk.nextToken();
      } catch (Exception var10) {
         status = false;
      }

      return status;
   }

   public static void update(String data) {
   }
}
