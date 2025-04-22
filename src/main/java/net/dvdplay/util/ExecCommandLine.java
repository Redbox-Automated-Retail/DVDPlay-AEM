package net.dvdplay.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.dvdplay.aem.Aem;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;

public class ExecCommandLine {
   public static ExecCommandLine getExecCommandLine() {
      return new ExecCommandLine();
   }

   public void exec(String aCmdString, int aTimeOut, String aDoneFlag) {
      String[] args = null;
      this.exec(aCmdString, aTimeOut, aDoneFlag, args, true);
   }

   public void exec(String aCmdString, int aTimeOut, String aDoneFlag, String[] aArgs, boolean aDoLogging) {
      try {
         Thread t = new Thread() {
            @Override
            public void run() {
               try {
                  runCommand(aCmdString, aTimeOut, aDoneFlag, aArgs, aDoLogging);
               } catch (Exception e) {
                  if (!aDoLogging) {
                     System.err.println(e.getMessage());
                     e.printStackTrace(System.err);
                  } else {
                     Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
                  }
               }
            }
         };

         t.start();
         t.join(aTimeOut * 2);
      } catch (Exception var12) {
         if (aDoLogging) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, var12.getMessage());
         } else {
            System.err.println(var12.getMessage());
         }

         throw new DvdplayException("ExecCommandLine.exec failed");
      }
   }

   private void runCommand(String aCmdString, int aTimeOut, String aDoneFlag, String[] aArgs, boolean aDoLogging) {
      if (aDoLogging) {
         Aem.logDetailMessage(DvdplayLevel.INFO, "Running " + aCmdString);
      }

      try {
         Process p = Runtime.getRuntime().exec(aCmdString, aArgs);
         BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
         BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
         String inputLine = "";
         String input = "";
         long start = System.currentTimeMillis();
         long now = System.currentTimeMillis();

         while (now - start <= aTimeOut) {
            now = System.currentTimeMillis();
            inputLine = in.readLine();
            input = input + "\n" + inputLine;
            if (input.lastIndexOf(aDoneFlag) != -1) {
               try {
                  p.exitValue();
               } catch (Exception var16) {
                  if (aDoLogging) {
                     Aem.logDetailMessage(DvdplayLevel.INFO, "Finishing " + aCmdString);
                  }

                  p.destroy();
               }
               break;
            }

            Util.sleep(100);
         }

         while ((inputLine = in.readLine()) != null) {
            input = input + "\n" + inputLine;
         }

         in.close();
         if (aDoLogging && input.trim().length() != 0) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "STDOUT: " + input);
         }

         inputLine = "";
         input = "";

         while ((inputLine = err.readLine()) != null) {
            input = input + "\n" + inputLine;
         }

         err.close();
         if (aDoLogging && input.trim().length() != 0) {
            Aem.logDetailMessage(DvdplayLevel.INFO, "STDERR: " + input);
         }

         if (now - start <= aTimeOut) {
            return;
         }

         p.destroy();
         if (aDoLogging) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "runCommand time out. Killing process");
         }
      } catch (Exception var17) {
         if (aDoLogging) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "runCommand() exception caught: " + var17.getMessage());
         } else {
            System.err.println(var17.getMessage());
         }
      }

      throw new DvdplayException("runCommand() failed");
   }
}
