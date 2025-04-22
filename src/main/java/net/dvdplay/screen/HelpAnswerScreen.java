package net.dvdplay.screen;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.models.Help;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class HelpAnswerScreen extends AbstractContentBar {
   Icon answerIcon;
   ActionListener helpAnswerAction;
   JLabel checkLabel;
   JLabel text3;
   JLabel text4;
   JLabel text5;
   JLabel text6;
   JLabel text7;
   JLabel text8;
   JLabel[] answerLabel;
   JLabel[] questionLabel;
   String dot = "";
   String t1012;
   String t1502;
   String t5001;
   String t1004;
   String cardFile;
   Help help;
   int size = 0;
   int offsetY = 130;
   int index = 1;
   int currentAnswerNum = 0;
   TextToRows questionRows;
   TextToRows answerRows;
   JPanel cardTypePanel;
   JPanel franchiseeInfoPanel;
   JButton[] cardButton;
   File f;
   ImageIcon icon;
   Image tempIcon;
   Image newImage;
   Locale currentLocale;

   public HelpAnswerScreen(String prevScreen, String currScreen, String data) {
      try {
         this.currentLocale = Aem.getLocale();
         this.helpAnswerAction = new HelpAnswerScreen.HelpAnswerAction();
         this.help = AbstractContentBar.aemContent.getHelp();
         this.answerIcon = ScreenProperties.getImage("Help.Icon.Answer");
         this.answerLabel = new JLabel[14];
         this.questionLabel = new JLabel[2];
         this.currentAnswerNum = AbstractContentBar.aemContent.getHelpQuestionNum();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.answerRows = new TextToRows(this.help.getAnswerById(AbstractContentBar.aemContent.getHelpQuestionNum()), 80);
         this.questionRows = new TextToRows(this.help.getQuestionById(AbstractContentBar.aemContent.getHelpQuestionNum()), 60);
         this.showPaymentCard();
         this.showFranchiseeInfo();
         if (AbstractContentBar.aemContent.getHelpQuestionNum() == 1550) {
            this.cardTypePanel.setVisible(true);
         }

         if (AbstractContentBar.aemContent.getHelpQuestionNum() == 1590 || AbstractContentBar.aemContent.getHelpQuestionNum() == 1610) {
            this.franchiseeInfoPanel.setVisible(true);
         }

         this.checkLabel = new JLabel(this.answerIcon);
         this.checkLabel.setBackground(ScreenProperties.getColor("White"));
         this.checkLabel.setBounds(80, 110, 84, 72);
         if (this.questionRows.getRowCount() > 1) {
            this.index = 2;
            this.offsetY = 60;
            if (this.questionRows.getRowCount() > 2) {
               this.dot = " ...";
            }
         }

         for (int i = 0; i < this.index; i++) {
            this.questionLabel[i] = new JLabel();
            if (i == 1) {
               this.questionLabel[i].setText(this.questionRows.getRow(i) + this.dot);
            } else {
               this.questionLabel[i].setText(this.questionRows.getRow(i));
            }

            this.questionLabel[i].setBackground(ScreenProperties.getColor("White"));
            this.questionLabel[i].setForeground(ScreenProperties.getColor("Black"));
            this.questionLabel[i].setFont(ScreenProperties.getFont("HelpAnswerQuestion"));
            this.questionLabel[i].setBounds(180, this.offsetY + i * 30, 600, 30);
            this.add(this.questionLabel[i]);
         }

         for (int i = 0; i < this.answerRows.getRowCount(); i++) {
            this.answerLabel[i] = new JLabel();
            this.answerLabel[i].setText(this.answerRows.getRow(i));
            this.answerLabel[i].setBackground(ScreenProperties.getColor("White"));
            this.answerLabel[i].setForeground(ScreenProperties.getColor("Black"));
            this.answerLabel[i].setFont(ScreenProperties.getFont("HelpAnswerAnswer"));
            this.answerLabel[i].setBounds(180, 200 + i * 30, 750, 20);
            this.add(this.answerLabel[i]);
         }

         for (int i = this.answerRows.getRowCount(); i < 14; i++) {
            this.answerLabel[i] = new JLabel();
            this.answerLabel[i].setText("");
            this.answerLabel[i].setBackground(ScreenProperties.getColor("White"));
            this.answerLabel[i].setForeground(ScreenProperties.getColor("Black"));
            this.answerLabel[i].setFont(ScreenProperties.getFont("HelpAnswerAnswer"));
            this.answerLabel[i].setBounds(180, 200 + i * 30, 750, 20);
            this.add(this.answerLabel[i]);
         }

         this.add(this.checkLabel);
         this.t1012 = Aem.getString(1012);
         this.t1502 = Aem.getString(1502);
         this.t5001 = Aem.getString(5001);
         this.t1004 = Aem.getString(1004);
         this.createBlackBottomBar(false, false, this.t1012, "Back", this.t1502, "CloseHelp", "", "");
         this.createTopBar(this.t5001, "", "", "", "", this.t1004, "StartOver");
         this.topBar.addActionListener(this.helpAnswerAction);
         this.bottomBar.addActionListener(this.helpAnswerAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("HelpAnswerScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var7) {
         this.msg = new StringBuffer("[").append("HelpAnswerScreen").append("]").append(var7.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var7);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setOriginalHelpAnswerBackBeforeTimeout(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (this.currentLocale != Aem.getLocale() || this.currentAnswerNum != AbstractContentBar.aemContent.getHelpQuestionNum()) {
            this.t1012 = Aem.getString(1012);
            this.t1502 = Aem.getString(1502);
            this.t5001 = Aem.getString(5001);
            this.t1004 = Aem.getString(1004);
            this.offsetY = 130;
            this.index = 1;
            this.answerRows = new TextToRows(this.help.getAnswerById(AbstractContentBar.aemContent.getHelpQuestionNum()), 80);
            this.questionRows = new TextToRows(this.help.getQuestionById(AbstractContentBar.aemContent.getHelpQuestionNum()), 60);
            if (AbstractContentBar.aemContent.getHelpQuestionNum() == 1550) {
               this.cardTypePanel.setVisible(true);
            } else {
               this.cardTypePanel.setVisible(false);
            }

            if (AbstractContentBar.aemContent.getHelpQuestionNum() == 1590) {
               this.showFranchiseeInfo();
               this.franchiseeInfoPanel.setBounds(180, 510, 400, 150);
               this.franchiseeInfoPanel.setVisible(true);
            } else if (AbstractContentBar.aemContent.getHelpQuestionNum() == 1610) {
               this.showFranchiseeInfo();
               this.franchiseeInfoPanel.setBounds(180, 350, 400, 150);
               this.franchiseeInfoPanel.setVisible(true);
            } else {
               this.franchiseeInfoPanel.setVisible(false);
            }

            if (this.questionRows.getRowCount() > 1) {
               this.index = 2;
               this.offsetY = 60;
               if (this.questionRows.getRowCount() > 2) {
                  this.dot = " ...";
               }
            }

            for (int i = 0; i < this.index; i++) {
               if (this.questionLabel[i] == null) {
                  this.questionLabel[i] = new JLabel();
               }

               if (i == 1) {
                  this.questionLabel[i].setText(this.questionRows.getRow(i) + this.dot);
               } else {
                  this.questionLabel[i].setText(this.questionRows.getRow(i));
               }

               this.questionLabel[i].setBackground(ScreenProperties.getColor("White"));
               this.questionLabel[i].setForeground(ScreenProperties.getColor("Black"));
               this.questionLabel[i].setFont(ScreenProperties.getFont("HelpAnswerQuestion"));
               this.questionLabel[i].setBounds(180, this.offsetY + i * 30, 600, 30);
            }

            for (int i = this.index; i < 2; i++) {
               if (this.questionLabel[i] == null) {
                  this.questionLabel[i] = new JLabel();
               }

               this.questionLabel[i].setText("");
               this.questionLabel[i].setBounds(180, this.offsetY + i * 30, 600, 30);
            }

            for (int i = 0; i < this.answerRows.getRowCount(); i++) {
               if (this.answerLabel[i] == null) {
                  this.answerLabel[i] = new JLabel();
               }

               this.answerLabel[i].setText(this.answerRows.getRow(i));
            }

            for (int i = this.answerRows.getRowCount(); i < 14; i++) {
               this.answerLabel[i].setText("");
            }

            this.currentLocale = Aem.getLocale();
            this.currentAnswerNum = AbstractContentBar.aemContent.getHelpQuestionNum();
         }

         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, this.t1012, "Back", this.t1502, "CloseHelp", "", "");
         this.topBar.setProperty(this.t5001, "", "", "", "", this.t1004, "StartOver");
         this.msg = new StringBuffer("* ").append("HelpAnswerScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var8) {
         this.msg = new StringBuffer("[").append("HelpAnswerScreen").append("]").append(var8.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var8);
      }
   }

   private void showPaymentCard() {
      if (this.cardTypePanel == null) {
         this.cardTypePanel = new JPanel(null);
         this.cardTypePanel.setBackground(ScreenProperties.getColor("White"));
         this.size = Aem.createPaymentCardTypeList();
         this.cardButton = new JButton[this.size];

         for (int i = 0; i < this.size; i++) {
            this.cardFile = "c:\\aem\\content\\images\\/" + Aem.getPaymentCardTypeIndexItem(i).getPaymentPicture();
            this.cardButton[i] = new JButton();
            this.f = new File(this.cardFile);
            if (this.f.exists()) {
               this.icon = new ImageIcon(this.cardFile);
               this.tempIcon = this.icon.getImage();
               this.newImage = this.tempIcon.getScaledInstance(60, 38, 0);
               this.cardButton[i].setIcon(new ImageIcon(this.newImage));
            } else {
               Aem.logDetailMessage(DvdplayLevel.FINE, this.cardFile + " not found, use alternative");
            }

            this.cardButton[i].setBackground(ScreenProperties.getColor("White"));
            this.cardButton[i].setForeground(ScreenProperties.getColor("White"));
            this.cardButton[i].setActionCommand("PaymentCardType " + Aem.getPaymentCardTypeIndexItem(i).getPaymentCardTypeId());
            this.cardButton[i].setBorder(new LineBorder(ScreenProperties.getColor("Black")));
            this.cardButton[i].setBounds(i * 66, 0, 60, 38);
            this.cardTypePanel.add(this.cardButton[i]);
         }

         this.cardTypePanel.setBounds(200, 250, 400, 38);
         this.cardTypePanel.setVisible(false);
         this.add(this.cardTypePanel);
      }
   }

   private void showFranchiseeInfo() {
      int locationY = 0;
      if (this.franchiseeInfoPanel == null) {
         this.franchiseeInfoPanel = new JPanel(null);
         this.franchiseeInfoPanel.setBackground(ScreenProperties.getColor("White"));
      }

      if (this.text3 == null) {
         this.text3 = new JLabel();
      }

      this.text3.setText(Aem.getString(2012));
      this.text3.setForeground(ScreenProperties.getColor("Red"));
      this.text3.setBackground(ScreenProperties.getColor("White"));
      this.text3.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      this.text3.setBounds(0, locationY, 500, 25);
      this.franchiseeInfoPanel.add(this.text3);
      if (this.text4 == null) {
         this.text4 = new JLabel();
      }

      this.text4.setVisible(false);
      if (!Aem.getString(2013).trim().equals("")) {
         locationY += 30;
         this.text4.setText(Aem.getString(2013));
         this.text4.setForeground(ScreenProperties.getColor("Black"));
         this.text4.setBackground(ScreenProperties.getColor("White"));
         this.text4.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text4.setBounds(0, locationY, 500, 25);
         this.text4.setVisible(true);
      }

      this.franchiseeInfoPanel.add(this.text4);
      if (this.text5 == null) {
         this.text5 = new JLabel();
      }

      this.text5.setVisible(false);
      if (!Aem.getString(2014).trim().equals("")) {
         locationY += 30;
         this.text5.setText(Aem.getString(2014));
         this.text5.setForeground(ScreenProperties.getColor("Black"));
         this.text5.setBackground(ScreenProperties.getColor("White"));
         this.text5.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text5.setBounds(0, 60, 500, 25);
         this.text5.setVisible(true);
      }

      this.franchiseeInfoPanel.add(this.text5);
      if (this.text6 == null) {
         this.text6 = new JLabel();
      }

      this.text6.setVisible(false);
      if (!Aem.getString(2015).trim().equals("")) {
         locationY += 30;
         this.text6.setText(Aem.getString(2015));
         this.text6.setForeground(ScreenProperties.getColor("Black"));
         this.text6.setBackground(ScreenProperties.getColor("White"));
         this.text6.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text6.setBounds(0, locationY, 500, 25);
         this.text6.setVisible(true);
      }

      this.franchiseeInfoPanel.add(this.text6);
      if (this.text7 == null) {
         this.text7 = new JLabel();
      }

      this.text7.setVisible(false);
      if (!Aem.getString(2016).trim().equals("")) {
         locationY += 30;
         this.text7.setText(Aem.getString(2016));
         this.text7.setForeground(ScreenProperties.getColor("Black"));
         this.text7.setBackground(ScreenProperties.getColor("White"));
         this.text7.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text7.setBounds(0, locationY, 500, 25);
         this.text7.setVisible(true);
      }

      this.franchiseeInfoPanel.add(this.text7);
      if (this.text8 == null) {
         this.text8 = new JLabel();
      }

      this.text8.setVisible(false);
      if (!Aem.getString(2017).trim().equals("")) {
         locationY += 30;
         this.text8.setText(Aem.getString(2017));
         this.text8.setForeground(ScreenProperties.getColor("Black"));
         this.text8.setBackground(ScreenProperties.getColor("White"));
         this.text8.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text8.setBounds(0, locationY, 500, 25);
         this.text8.setVisible(true);
      }

      this.franchiseeInfoPanel.add(this.text8);
      this.franchiseeInfoPanel.setBounds(180, 510, 400, 150);
      this.franchiseeInfoPanel.setVisible(false);
      this.add(this.franchiseeInfoPanel);
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

   public class HelpAnswerAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("HelpAnswerScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(HelpAnswerScreen.mainAction)) {
                  if (this.cmd[0].equals("Back")) {
                     HelpAnswerScreen.mainAction
                        .actionPerformed(new ActionEvent(this, 1001, HelpAnswerScreen.aemContent.getOriginalHelpAnswerBackBeforeTimeout()));
                  } else if (this.cmd[0].equals("CloseHelp")) {
                     HelpAnswerScreen.aemContent.setHelpPageNum(1);
                     if (Aem.needToReinitialize() && HelpAnswerScreen.aemContent.getOriginalPointBeforeHelp().equals("ReturnThankYouScreen")) {
                        HelpAnswerScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                     } else {
                        HelpAnswerScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, HelpAnswerScreen.aemContent.getOriginalPointBeforeHelp()));
                     }
                  } else if (this.cmd[0].equals("StartOver")) {
                     HelpAnswerScreen.aemContent.setHelpPageNum(1);
                     if (Aem.needToReinitialize()) {
                        HelpAnswerScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                     } else {
                        HelpAnswerScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                     }
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "HelpAnswerScreen");
         } catch (Throwable var5) {
            Log.error(var5, "HelpAnswerScreen");
         }
      }
   }
}
