package net.dvdplay.screen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.models.Help;
import net.dvdplay.ui.ScreenProperties;

public class HelpMainScreen extends AbstractContentBar {
   Icon questionIcon;
   Icon questionPressedIcon;
   ArrayList qList;
   ActionListener helpMainAction;
   Help help;
   JButton check;
   int helpPageNum;
   int rowCount;
   int remainCol;
   int index;
   int lastItemIndex = 7;
   int questionPerPage = 7;
   String tempStr;
   String tempId;
   JPanel statusLightPanel;
   ImageIcon redLight;
   ImageIcon whiteLight;
   ImageIcon greenLight;
   ImageIcon yellowLight;
   JLabel hardwareError;
   JLabel queueBusy;
   JLabel serverConnection;
   static boolean checkStatus = true;

   public HelpMainScreen(String prevScreen, String currScreen, String data) {
      try {
         this.help = AbstractContentBar.aemContent.getHelp();
         this.helpPageNum = AbstractContentBar.aemContent.getHelpPageNum();
         this.helpMainAction = new HelpMainScreen.HelpMainAction();
         this.questionIcon = ScreenProperties.getImage("Help.Icon.Question");
         this.questionPressedIcon = ScreenProperties.getImage("Help.Icon.QuestionPressed");
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         this.qList = new ArrayList();
         this.rowCount = this.help.size() / this.questionPerPage + 1;
         this.remainCol = this.help.size() % this.questionPerPage;
         this.index = this.questionPerPage;
         if (this.helpPageNum == this.rowCount) {
            this.index = this.remainCol;
         }

         for (int i = 0; i < this.index; i++) {
            this.tempStr = this.help.getQuestion(i + (this.helpPageNum - 1) * this.questionPerPage);
            this.check = new JButton("   " + this.tempStr, this.questionIcon);
            this.check.setPressedIcon(this.questionPressedIcon);
            this.check.setBackground(ScreenProperties.getColor("White"));
            this.check.setForeground(ScreenProperties.getColor("Black"));
            this.check.setFont(ScreenProperties.getFont("HelpQuestionFont"));
            this.check.setHorizontalTextPosition(4);
            this.check.setHorizontalAlignment(2);
            this.check.setActionCommand("HelpAnswerScreen " + this.help.getQuestionId(i + (this.helpPageNum - 1) * this.questionPerPage));
            this.check.setBorderPainted(false);
            this.check.setFocusPainted(false);
            this.check.setBounds(80, 80 + i * 80, this.tempStr.length() * 14 + 80, 76);
            this.check.addActionListener(this.helpMainAction);
            this.qList.add(this.check);
            this.add(this.check);
         }

         this.createStatusLightPanel();
         this.createBlackBottomBar(false, false, Aem.getString(1007), "About", Aem.getString(1012), "Back", Aem.getString(1010), "MoreHelp");
         this.createTopBar(Aem.getString(1001), "", "", "", "", Aem.getString(1004), "StartOver");
         this.topBar.addActionListener(this.helpMainAction);
         this.bottomBar.addActionListener(this.helpMainAction);
         this.add(this.statusLightPanel);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("HelpMainScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("HelpMainScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.helpPageNum = AbstractContentBar.aemContent.getHelpPageNum();
         if (!prevScreen.equals("HelpMainScreen")
            && !prevScreen.equals("HelpAnswerScreen")
            && !prevScreen.equals("TimeOutScreen")
            && !prevScreen.equals("AboutCompanyScreen")) {
            AbstractContentBar.aemContent.setOriginalPointBeforeHelp(prevScreen);
         }

         this.rowCount = this.help.size() / this.questionPerPage + 1;
         this.remainCol = this.help.size() % this.questionPerPage;
         this.index = this.questionPerPage;
         this.lastItemIndex = 7;
         if (this.helpPageNum == this.rowCount) {
            this.index = this.remainCol;
            this.bottomBar
               .setProperty(ScreenProperties.getColor("Black"), true, false, false, Aem.getString(1007), "About", Aem.getString(1012), "Back", "", "");
         } else {
            this.bottomBar
               .setProperty(
                  ScreenProperties.getColor("Black"),
                  true,
                  false,
                  false,
                  Aem.getString(1007),
                  "About",
                  Aem.getString(1012),
                  "Back",
                  Aem.getString(1010),
                  "MoreHelp"
               );
         }

         this.topBar.setProperty(Aem.getString(1001), "", "", "", "", Aem.getString(1004), "StartOver");
         this.createStatusLightPanel();

         for (int i = 0; i < this.qList.size() && i < this.index; this.lastItemIndex = i++) {
            this.check = (JButton)this.qList.get(i);
            this.tempStr = this.help.getQuestion(i + (this.helpPageNum - 1) * this.questionPerPage);
            this.check.setPressedIcon(this.questionPressedIcon);
            this.check.setIcon(this.questionIcon);
            this.check.setText("   " + this.tempStr);
            this.check.setEnabled(true);
            this.check.setFocusPainted(false);
            this.check.setBounds(80, 80 + i * 80, this.tempStr.length() * 14 + 80, 76);
            this.check.setActionCommand("HelpAnswerScreen " + this.help.getQuestionId(i + (this.helpPageNum - 1) * this.questionPerPage));
         }

         for (int j = this.lastItemIndex + 1; j < 7; j++) {
            this.check = (JButton)this.qList.get(j);
            this.check.setIcon(null);
            this.check.setPressedIcon(null);
            this.check.setEnabled(false);
            this.check.setText("");
         }

         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
         this.msg = new StringBuffer("* ").append("HelpMainScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("HelpMainScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var6);
      }
   }

   private void createStatusLightPanel() {
      if (this.statusLightPanel == null) {
         this.statusLightPanel = new JPanel(null);
         this.statusLightPanel.setBackground(ScreenProperties.getColor("White"));
         this.statusLightPanel.setBounds(850, 70, 150, 50);
         this.redLight = ScreenProperties.getImage("Status.Red");
         this.whiteLight = ScreenProperties.getImage("Status.White");
         this.greenLight = ScreenProperties.getImage("Status.Green");
         this.yellowLight = ScreenProperties.getImage("Status.Yellow");
         this.hardwareError = new JLabel();
         this.hardwareError.setBackground(ScreenProperties.getColor("White"));
         this.hardwareError.setHorizontalAlignment(0);
         this.hardwareError.setVerticalAlignment(0);
         this.hardwareError.setBounds(0, 0, 50, 50);
         this.serverConnection = new JLabel();
         this.serverConnection.setBackground(ScreenProperties.getColor("White"));
         this.serverConnection.setHorizontalAlignment(0);
         this.serverConnection.setVerticalAlignment(0);
         this.serverConnection.setBounds(50, 0, 50, 50);
         this.queueBusy = new JLabel();
         this.queueBusy.setBackground(ScreenProperties.getColor("White"));
         this.queueBusy.setHorizontalAlignment(0);
         this.queueBusy.setVerticalAlignment(0);
         this.queueBusy.setBounds(100, 0, 50, 50);
         this.statusLightPanel.add(this.hardwareError);
         this.statusLightPanel.add(this.serverConnection);
         this.statusLightPanel.add(this.queueBusy);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
      this.listenerList
         .add(
            ActionListener.class,
            l
         );
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
   }

   public class HelpMainAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("HelpMainScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(HelpMainScreen.mainAction)) {
                  if (this.cmd[0].equals("HelpAnswerScreen")) {
                     HelpMainScreen.aemContent.setHelpQuestionNum(Integer.parseInt(this.cmd[1]));
                     HelpMainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpAnswerScreen"));
                  } else if (this.cmd[0].equals("StartOver")) {
                     HelpMainScreen.aemContent.setHelpPageNum(1);
                     if (Aem.needToReinitialize()) {
                        HelpMainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                     } else {
                        HelpMainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                     }
                  } else if (this.cmd[0].equals("Back")) {
                     if (HelpMainScreen.aemContent.getHelpPageNum() == 1) {
                        if (Aem.needToReinitialize() && HelpMainScreen.aemContent.getOriginalPointBeforeHelp().equals("ReturnThankYouScreen")) {
                           HelpMainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                        } else {
                           HelpMainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, HelpMainScreen.aemContent.getOriginalPointBeforeHelp()));
                        }
                     } else {
                        HelpMainScreen.aemContent.setHelpPageNum(HelpMainScreen.aemContent.getHelpPageNum() - 1);
                        HelpMainScreen.this.update(HelpMainScreen.aemContent.getOriginalPointBeforeHelp(), "HelpMainScreen", "");
                        HelpMainScreen.this.repaint();
                     }
                  } else if (this.cmd[0].equals("MoreHelp")) {
                     HelpMainScreen.aemContent.setHelpPageNum(HelpMainScreen.aemContent.getHelpPageNum() + 1);
                     HelpMainScreen.this.update("HelpMainScreen", "HelpMainScreen", "");
                     HelpMainScreen.this.repaint();
                  } else if (this.cmd[0].equals("About")) {
                     HelpMainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "AboutCompanyScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "HelpMainScreen");
         } catch (Throwable var5) {
            Log.error(var5, "HelpMainScreen");
         }
      }
   }
}
