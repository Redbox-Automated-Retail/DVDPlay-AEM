package net.dvdplay.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;

public abstract class BaseActionListener implements ActionListener {
   protected String[] cmd;
   protected boolean isCurrentScreenAction = false;
   private String mScreenName = "";
   private StringBuffer msg = null;

   public BaseActionListener() {
      this.cmd = new String[3];
   }

   protected void setScreenName(String aScreenName) {
      this.mScreenName = aScreenName;
   }

   public void actionPerformed(ActionEvent ae) {
      this.cmd = ae.getActionCommand().split(" ");
      this.isCurrentScreenAction = AEMGui.isCurrentScreen(this.mScreenName);
   }

   protected void actionPerformed(ActionEvent ae, ActionListener aMainAction) {
      this.actionPerformed(ae);
      this.checkRobotAndLogoClick(aMainAction);
   }

   protected void logCommandFlow() {
      this.msg = new StringBuffer("[").append(this.mScreenName).append("]").append(" Cmd ").append(this.cmd[0]);
      Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
   }

   protected boolean checkRobotAndLogoClick(ActionListener aMainAction, Timer aTimer) {
      Log.debug("++++ Getting " + this.cmd[0] + " as command");
      if (this.cmd[0].equals("DVDPlayLogo")) {
         aTimer.stop();
         aMainAction.actionPerformed(new ActionEvent(this, 1001, "DVDPlayLogo 0"));
      } else {
         if (!this.cmd[0].equals("RobotClicking")) {
            return false;
         }

         aMainAction.actionPerformed(new ActionEvent(this, 1001, "RobotClicking"));
      }

      return true;
   }

   protected boolean checkRobotAndLogoClick(ActionListener aMainAction) {
      Log.debug("++++ Getting " + this.cmd[0] + " as command");
      if (this.cmd[0].equals("DVDPlayLogo")) {
         aMainAction.actionPerformed(new ActionEvent(this, 1001, "DVDPlayLogo 0"));
      } else {
         if (!this.cmd[0].equals("RobotClicking")) {
            return false;
         }

         aMainAction.actionPerformed(new ActionEvent(this, 1001, "RobotClicking"));
      }

      return true;
   }
}
