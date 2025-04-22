package net.dvdplay.ui;

import java.awt.AWTException;
import java.awt.Robot;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.logger.Log;

public class ClickRobot extends Thread {
   Robot robot = null;
   final int CLICK_DELAY = 1000;

   public ClickRobot() {
      try {
         this.robot = new Robot();
      } catch (AWTException var2) {
         Log.error(var2, "Error initializing Screen Monitor");
      }
   }

   public void run() {
      try {
         while (true) {
            AemFactory.getInstance();
            if (!Aem.isToolsRunning()) {
               this.mouseMoveAndClick(2, 2);
            }

            Thread.sleep(120000L);
         }
      } catch (InterruptedException var3) {
      } catch (Exception var4) {
         Log.error("Error in Screen Monitor");
      }
   }

   public void mouseMoveAndClick(int xLoc, int yLoc) {
      this.robot.mouseMove(xLoc, yLoc);
      this.robot.mousePress(16);
      this.robot.mouseRelease(16);
      this.robot.delay(1000);
   }
}
