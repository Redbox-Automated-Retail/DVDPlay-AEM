package net.dvdplay.screen;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.inventory.PlayListItem;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class ReturnThankYouScreen extends AbstractContentBar {
   ActionListener returnThankYouAction;
   JLabel text1;
   JLabel text3;
   JLabel text4;
   JLabel text5;
   JLabel text6;
   JLabel text7;
   JLabel adGif;
   JLabel totalAmount1;
   JLabel totalAmount2;
   JLabel text8;
   JLabel advertisement;
   JButton returnAnother;
   JLabel[] text2;
   int text1LocationY = 190;
   int franchiseInfoLocationY;
   TextToRows thankYouNote;
   ImageIcon icon;
   String ad;
   File f;
   Image tempIcon;
   Image newImage;
   PlayListItem lPlayListItem;
   static Timer retTimer = new Timer(DvdplayBase.ERROR_SCREEN_TIME_OUT, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
         ReturnThankYouScreen.retTimer.stop();
         if (Aem.inQuiesceMode() || !Aem.needToReinitialize()) {
            AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.MAIN_SCREEN));
         } else {
            AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.INITIALIZING_AEM_SCREEN));
         }
      }
   });


   public ReturnThankYouScreen(String prevScreen, String currScreen, String data) {
      try {
         this.returnThankYouAction = new ReturnThankYouScreen.ReturnThankYouAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.createRentAnotherButton();
         this.createFranchiseeInfo();
         this.createTotalAmount();
         this.add(this.returnAnother);
         this.add(this.totalAmount1);
         this.add(this.totalAmount2);
         this.text1 = new JLabel(Aem.getString(6901));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("ReturnMovieThankYou"));
         this.text1.setBounds(80, this.text1LocationY, 500, 35);
         this.thankYouNote = new TextToRows(Aem.getString(6902), 40);
         this.text2 = new JLabel[this.thankYouNote.getRowCount()];

         for (int i = 0; i < (this.thankYouNote.getRowCount() <= 3 ? this.thankYouNote.getRowCount() : 3); i++) {
            this.text2[i] = new JLabel(this.thankYouNote.getRow(i));
            this.text2[i].setForeground(ScreenProperties.getColor("Black"));
            this.text2[i].setBackground(ScreenProperties.getColor("White"));
            this.text2[i].setFont(ScreenProperties.getFont("ReturnMovieTitle"));
            this.text2[i].setBounds(80, 270 + i * 30, 500, 20);
            this.add(this.text2[i]);
         }

         this.advertisement = new JLabel(new ImageIcon());
         this.advertisement.setBackground(ScreenProperties.getColor("White"));
         this.advertisement.setBorder(new LineBorder(ScreenProperties.getColor("Black"), 3));
         this.advertisement.setBounds(530, 110, 380, 510);
         this.createBlackBottomBar(false, false, Aem.getString(1007), "About", "", "", "", "");
         this.createTopBar("", "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.bottomBar.addActionListener(this.returnThankYouAction);
         this.topBar.addActionListener(this.returnThankYouAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.text1);
         this.add(this.text3);
         this.add(this.text4);
         this.add(this.text5);
         this.add(this.text6);
         this.add(this.text7);
         this.add(this.text8);
         this.add(this.advertisement);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("ReturnThankYouScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ReturnThankYouScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.text1LocationY = 190;
         this.franchiseInfoLocationY = 370;
         if (data.equals("Rent")) {
            this.totalAmount1.setText(Aem.getString(6903));
            AbstractContentBar.aemContent.calculateFinalTotals();
            this.totalAmount2.setText(AbstractContentBar.aemContent.getTotal());
            this.totalAmount1.setVisible(true);
            this.totalAmount2.setVisible(true);
            this.returnAnother.setVisible(false);
            this.text1LocationY = 170;
            AbstractContentBar.aemContent.createRentalQueueJob();
         }

         if (data.equals("Return")) {
            this.returnAnother.setText(Aem.getString(6904));
            this.totalAmount1.setVisible(false);
            this.totalAmount2.setVisible(false);
            this.returnAnother.setVisible(true);
         }

         this.text1.setText(Aem.getString(6901));
         this.text1.setBounds(80, this.text1LocationY, 500, 35);

         for (int j = 0; j < this.text2.length; j++) {
            this.remove(this.text2[j]);
         }

         this.thankYouNote = new TextToRows(Aem.getString(6902), 40);
         this.text2 = new JLabel[this.thankYouNote.getRowCount()];

         for (int i = 0; i < (this.thankYouNote.getRowCount() <= 3 ? this.thankYouNote.getRowCount() : 3); i++) {
            this.text2[i] = new JLabel(this.thankYouNote.getRow(i));
            this.text2[i].setForeground(ScreenProperties.getColor("Black"));
            this.text2[i].setBackground(ScreenProperties.getColor("White"));
            this.text2[i].setFont(ScreenProperties.getFont("ReturnMovieTitle"));
            this.text2[i].setBounds(80, 270 + i * 30, 500, 20);
            this.add(this.text2[i]);
         }

         this.text3.setText(Aem.getString(2012));
         this.text4.setVisible(false);
         this.text5.setVisible(false);
         this.text6.setVisible(false);
         this.text7.setVisible(false);
         this.text8.setVisible(false);
         if (!Aem.getString(2013).trim().equals("")) {
            this.text4.setText(Aem.getString(2013));
            this.franchiseInfoLocationY += 25;
            this.text4.setBounds(80, this.franchiseInfoLocationY, 500, 25);
            this.text4.setVisible(true);
         }

         if (!Aem.getString(2014).trim().equals("")) {
            this.text5.setText(Aem.getString(2014));
            this.franchiseInfoLocationY += 25;
            this.text5.setBounds(80, this.franchiseInfoLocationY, 500, 25);
            this.text5.setVisible(true);
         }

         if (!Aem.getString(2015).trim().equals("")) {
            this.text6.setText(Aem.getString(2015));
            this.franchiseInfoLocationY += 25;
            this.text6.setBounds(80, this.franchiseInfoLocationY, 500, 25);
            this.text6.setVisible(true);
         }

         if (!Aem.getString(2016).trim().equals("")) {
            this.franchiseInfoLocationY += 25;
            this.text7.setText(Aem.getString(2016));
            this.text7.setBounds(80, this.franchiseInfoLocationY, 500, 25);
            this.text7.setVisible(true);
         }

         if (!Aem.getString(2017).trim().equals("")) {
            this.franchiseInfoLocationY += 25;
            this.text8.setText(Aem.getString(2017));
            this.text8.setBounds(80, this.franchiseInfoLocationY, 500, 25);
            this.text8.setVisible(true);
         }

         this.lPlayListItem = Aem.getNextStaticPlayListItem();
         if (this.lPlayListItem == null) {
            this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
         } else {
            this.ad = this.lPlayListItem.getFilePath();
            this.f = new File(this.ad);
            if (this.f.exists()) {
               Aem.logSummaryMessage("[ADVERTISEMENT] " + this.ad);
               this.icon = new ImageIcon(this.ad);
            } else {
               Aem.logSummaryMessage("[ADVERTISEMENT] " + this.ad + " not found, use default image");
               this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
            }
         }

         this.tempIcon = this.icon.getImage();
         this.newImage = this.tempIcon.getScaledInstance(380, 510, 0);
         this.advertisement.setIcon(new ImageIcon(this.newImage));
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, Aem.getString(1007), "About", "", "", "", "");
         this.topBar.setProperty("", "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         AbstractContentBar.timer.stop();
         retTimer.start();
         this.msg = new StringBuffer("* ").append("ReturnThankYouScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("ReturnThankYouScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var6);
      }
   }

   public void createFranchiseeInfo() {
      int locationY = 370;
      this.text3 = new JLabel(Aem.getString(2012));
      this.text3.setForeground(ScreenProperties.getColor("Red"));
      this.text3.setBackground(ScreenProperties.getColor("White"));
      this.text3.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      this.text3.setBounds(80, locationY, 500, 25);
      this.text4 = new JLabel(Aem.getString(2013));
      this.text4.setForeground(ScreenProperties.getColor("Black"));
      this.text4.setBackground(ScreenProperties.getColor("White"));
      this.text4.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      if (!Aem.getString(2013).trim().equals("")) {
         locationY += 25;
         this.text4.setBounds(80, locationY, 500, 25);
      }

      this.text5 = new JLabel(Aem.getString(2014));
      this.text5.setForeground(ScreenProperties.getColor("Black"));
      this.text5.setBackground(ScreenProperties.getColor("White"));
      this.text5.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      if (!Aem.getString(2014).trim().equals("")) {
         locationY += 25;
         this.text5.setBounds(80, locationY, 500, 25);
      }

      this.text6 = new JLabel(Aem.getString(2015));
      this.text6.setForeground(ScreenProperties.getColor("Black"));
      this.text6.setBackground(ScreenProperties.getColor("White"));
      this.text6.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      if (!Aem.getString(2015).trim().equals("")) {
         locationY += 25;
         this.text6.setBounds(80, locationY, 500, 25);
      }

      this.text7 = new JLabel(Aem.getString(2016));
      this.text7.setForeground(ScreenProperties.getColor("Black"));
      this.text7.setBackground(ScreenProperties.getColor("White"));
      this.text7.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      if (!Aem.getString(2016).trim().equals("")) {
         locationY += 25;
         this.text7.setBounds(80, locationY, 500, 25);
      }

      this.text8 = new JLabel(Aem.getString(2017));
      this.text8.setForeground(ScreenProperties.getColor("Black"));
      this.text8.setBackground(ScreenProperties.getColor("White"));
      this.text8.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      if (!Aem.getString(2016).trim().equals("")) {
         locationY += 25;
         this.text8.setBounds(80, locationY, 500, 25);
      }
   }

   public void createTotalAmount() {
      this.totalAmount1 = new JLabel(Aem.getString(6903));
      this.totalAmount1.setForeground(ScreenProperties.getColor("Black"));
      this.totalAmount1.setBackground(ScreenProperties.getColor("White"));
      this.totalAmount1.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      this.totalAmount1.setBounds(80, 220, 500, 35);
      this.totalAmount2 = new JLabel();
      this.totalAmount2.setForeground(ScreenProperties.getColor("Red"));
      this.totalAmount2.setBackground(ScreenProperties.getColor("White"));
      this.totalAmount2.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
      this.totalAmount2.setBounds(400, 220, 500, 35);
   }

   public void createRentAnotherButton() {
      this.returnAnother = new JButton(Aem.getString(6904));
      this.returnAnother.setBackground(ScreenProperties.getColor("Red"));
      this.returnAnother.setForeground(ScreenProperties.getColor("White"));
      this.returnAnother.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
      this.returnAnother.setFont(ScreenProperties.getFont("SwipePaymentText5"));
      this.returnAnother.setActionCommand("ReturnAnother");
      this.returnAnother.setFocusPainted(false);
      this.returnAnother.setHorizontalTextPosition(0);
      this.returnAnother.setVerticalTextPosition(0);
      this.returnAnother.addActionListener(this.returnThankYouAction);
      this.returnAnother.setBounds(80, 530, 300, 50);
   }

   public void addActionListener(ActionListener l) {
      this.listenerList
         .add(
            ActionListener.class,
            l
         );
      AbstractContentBar.mainAction = l;
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
   }

   public class ReturnThankYouAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("ReturnThankYouScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(ReturnThankYouScreen.mainAction, ReturnThankYouScreen.retTimer)) {
                  ReturnThankYouScreen.retTimer.stop();
                  if (Aem.inQuiesceMode()) {
                     ReturnThankYouScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                     return;
                  }

                  if (this.cmd[0].equals("Help")) {
                     ReturnThankYouScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  }

                  if (this.cmd[0].equals("StartOver")) {
                     if (Aem.needToReinitialize()) {
                        ReturnThankYouScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                     } else {
                        ReturnThankYouScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                     }
                  }

                  if (this.cmd[0].equals("About")) {
                     ReturnThankYouScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "AboutCompanyScreen"));
                  }

                  if (this.cmd[0].equals("ReturnAnother")) {
                     if (Aem.isCarouselFull()) {
                        ReturnThankYouScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4021,MainScreen"));
                     } else {
                        ReturnThankYouScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ReturnMovieScreen"));
                     }
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "ReturnThankYouScreen");
         } catch (Throwable var5) {
            Log.error(var5, "ReturnThankYouScreen");
         }
      }
   }
}
