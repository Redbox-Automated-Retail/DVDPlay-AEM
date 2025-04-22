package net.dvdplay.aem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.dvdplay.aemcontroller.AEMGui;

public class Bootstrap {
   private static final String mPassword = "b00tstrap";
   private static String mAemId;
   private static String mServerAddress;
   private static Aem mAem;

   public static void main(String[] args) {
      try {
         boolean lIsInteractive = false;
         boolean lIsInstall = false;
         boolean lIsReset = false;
         String lId = null;
         if (args.length < 3 || args.length > 6) {
            System.out.println("Invalid parameters");
            System.out
               .println(
                  "Usage: "
                     + Bootstrap.class
                        .getName()
                     + " password AemId ServerAddress [args]"
               );
            System.exit(1);
         }

         if (!"b00tstrap".equals(args[0])) {
            System.out.println("Invalid password.");
         } else {
            mAemId = args[1];
            mServerAddress = args[2];

            for (int i = 3; i < args.length; i++) {
               if (args[i].equalsIgnoreCase("http")) {
                  AEMGui.mHttps = false;
               } else if (args[i].equalsIgnoreCase("-i")) {
                  lIsInteractive = true;
               } else if (args[i].equalsIgnoreCase("-install")) {
                  lIsInstall = true;
               } else if (args[i].equalsIgnoreCase("-reset")) {
                  lIsReset = true;
               } else {
                  if (args[i].equalsIgnoreCase("-cleanup")) {
                     Aem.purgeDir("c:\\windows\\system32\\var\\qtasks\\", System.currentTimeMillis());
                     System.out.println("Done.");
                     return;
                  }

                  lId = args[i];
               }
            }

            if (lIsInteractive) {
               System.out
                  .println(
                     "\nA - AemProperties\t\tB - DiscDetail\nC - TitleDetail\t\t\tD - RegularPricing\nE - SpecialPricing              F - Genre\nG - MediaPlaylist\t\tH - StaticPlaylist\nI - Translation\t\t\tJ - Locale\nK - RatingSystem\t\tL - Rating\nM - GroupCode\t\t\tN - TitleType\nO - TitleTypeCap\t\tP - PaymentCardType\nQ - Security\t\t\tR - Privileges\nS - BadSlot\t\t\tT - SlotOffset\nU - Poll"
                  );
               System.out.print("\nEnter params (<enter> for all): ");
               BufferedReader lIn = new BufferedReader(new InputStreamReader(System.in));
               lId = lIn.readLine().trim();
               if (lId.length() == 0) {
                  lId = null;
               }

               lIn.close();
            }

            mAem = new Aem(Integer.parseInt(mAemId), mServerAddress, lId, lIsInstall, lIsReset);
            System.out.println("Done.");
         }
      } catch (Exception var7) {
         System.out.println(var7.getMessage());
         var7.printStackTrace();
      }
   }
}
