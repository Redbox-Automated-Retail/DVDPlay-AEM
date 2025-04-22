package net.dvdplay.screen;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
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
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class SwipePaymentCardScreen extends AbstractContentBar {
   ActionListener swipePaymentCardAction;
   JLabel[] text2;
   JLabel[] text4;
   JLabel text1;
   JLabel text3;
   JLabel secureGif;
   JButton aGif;
   JButton agreementButton;
   JButton[] cardButton;
   JPanel cardTypePanel;
   ImageIcon ii;
   String passingData;
   String cardFile;
   String topBarTitle;
   String t1012;
   String t5401;
   String t1001;
   TextToRows ttr;
   int size;
   File f;
   ImageIcon icon;
   ImageIcon secureIcon;
   Image tempIcon;
   Image newImage;
   Locale currentLocale;

   public SwipePaymentCardScreen(String prevScreen, String currScreen, String data) {
      try {
         this.swipePaymentCardAction = new SwipePaymentCardScreen.SwipePaymentCardAction();
         this.currentLocale = Aem.getLocale();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.text1 = new JLabel(Aem.getString(5402));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("SwipePaymentText1"));
         this.text1.setBounds(150, 200, 400, 30);
         this.ttr = new TextToRows(Aem.getString(5403), 40);
         this.text2 = new JLabel[2];

         for (int i = 0; i < this.ttr.getRowCount(); i++) {
            this.text2[i] = new JLabel(this.ttr.getRow(i));
            this.text2[i].setForeground(ScreenProperties.getColor("Black"));
            this.text2[i].setBackground(ScreenProperties.getColor("White"));
            this.text2[i].setFont(ScreenProperties.getFont("SwipePaymentText2"));
            this.text2[i].setBounds(150, 250 + i * 30, 400, 20);
            this.add(this.text2[i]);
         }

         this.text3 = new JLabel(Aem.getString(5404));
         this.text3.setForeground(ScreenProperties.getColor("Black"));
         this.text3.setBackground(ScreenProperties.getColor("White"));
         this.text3.setFont(ScreenProperties.getFont("SwipePaymentText3"));
         this.text3.setBounds(150, 320, 400, 20);
         this.ttr = new TextToRows(Aem.getString(5405), 55);
         this.text4 = new JLabel[2];

         for (int i = 0; i < this.ttr.getRowCount(); i++) {
            this.text4[i] = new JLabel(this.ttr.getRow(i));
            this.text4[i].setForeground(ScreenProperties.getColor("Black"));
            this.text4[i].setBackground(ScreenProperties.getColor("White"));
            this.text4[i].setFont(ScreenProperties.getFont("SwipePaymentText4"));
            this.text4[i].setBounds(150, 450 + i * 25, 400, 20);
            this.add(this.text4[i]);
         }

         if (Aem.isBuyDisabled()) {
            this.topBarTitle = Aem.getString(5407);
         } else {
            this.topBarTitle = Aem.getString(5406);
         }

         this.agreementButton = new JButton(this.topBarTitle);
         this.agreementButton.setBackground(ScreenProperties.getColor("Red"));
         this.agreementButton.setForeground(ScreenProperties.getColor("White"));
         this.agreementButton.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
         this.agreementButton.setFont(ScreenProperties.getFont("SwipePaymentText5"));
         this.agreementButton.setActionCommand("Agreement");
         this.agreementButton.setHorizontalTextPosition(0);
         this.agreementButton.setFocusPainted(false);
         this.agreementButton.setVerticalTextPosition(0);
         this.agreementButton.addActionListener(this.swipePaymentCardAction);
         this.agreementButton.setBounds(150, 520, 400, 50);
         this.createCardTypePanel();
         this.add(this.cardTypePanel);
         this.ii = ScreenProperties.getImage("SwipeCard.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(650, 230, 260, 311);
         this.aGif.setBorder(null);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setHorizontalAlignment(4);
         this.aGif.setPressedIcon(this.ii);
         this.secureIcon = ScreenProperties.getImage("SwipeCard.Secure.Gif");
         this.secureGif = new JLabel(this.secureIcon);
         this.secureGif.setBounds(635, 505, 260, 100);
         this.secureGif.setBackground(ScreenProperties.getColor("White"));
         this.secureGif.setHorizontalAlignment(4);
         this.add(this.aGif);
         this.add(this.secureGif);
         this.add(this.agreementButton);
         this.add(this.text1);
         this.add(this.text3);
         this.t1012 = Aem.getString(1012);
         this.t5401 = Aem.getString(5401);
         this.t1001 = Aem.getString(1001);
         this.createBlackBottomBar(true, false, "", "", this.t1012, "Back", "", "");
         this.createTopBar(this.t5401, "", "", "", "", this.t1001, "Help");
         this.bottomBar.addActionListener(this.swipePaymentCardAction);
         this.topBar.addActionListener(this.swipePaymentCardAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("SwipePaymentCardScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("SwipePaymentCardScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var6);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.passingData = data;
         if (this.currentLocale != Aem.getLocale()) {
            this.text1.setText(Aem.getString(5402));
            this.text3.setText(Aem.getString(5404));
            if (Aem.isBuyDisabled()) {
               this.topBarTitle = Aem.getString(5407);
            } else {
               this.topBarTitle = Aem.getString(5406);
            }

            this.agreementButton.setText(this.topBarTitle);

            for (int j = 0; j < this.text2.length; j++) {
               this.remove(this.text2[j]);
            }

            this.ttr = new TextToRows(Aem.getString(5403), 40);
            this.text2 = new JLabel[2];

            for (int i = 0; i < this.ttr.getRowCount(); i++) {
               this.text2[i] = new JLabel(this.ttr.getRow(i));
               this.text2[i].setForeground(ScreenProperties.getColor("Black"));
               this.text2[i].setBackground(ScreenProperties.getColor("White"));
               this.text2[i].setFont(ScreenProperties.getFont("SwipePaymentText2"));
               this.text2[i].setBounds(150, 250 + i * 30, 400, 20);
               this.add(this.text2[i]);
            }

            for (int j = 0; j < this.text4.length; j++) {
               this.remove(this.text4[j]);
            }

            this.ttr = new TextToRows(Aem.getString(5405), 55);
            this.text4 = new JLabel[2];

            for (int i = 0; i < this.ttr.getRowCount(); i++) {
               this.text4[i] = new JLabel(this.ttr.getRow(i));
               this.text4[i].setForeground(ScreenProperties.getColor("Black"));
               this.text4[i].setBackground(ScreenProperties.getColor("White"));
               this.text4[i].setFont(ScreenProperties.getFont("SwipePaymentText4"));
               this.text4[i].setBounds(150, 450 + i * 25, 400, 20);
               this.add(this.text4[i]);
            }

            this.t1012 = Aem.getString(1012);
            this.t5401 = Aem.getString(5401);
            this.t1001 = Aem.getString(1001);
            this.currentLocale = Aem.getLocale();
         }

         this.createCardTypePanel();
         AbstractContentBar.aemContent.setPaymentCardTypeId(Aem.getPaymentCardTypeIndexItemByPaymentCardTypeId(42).getPaymentCardCategoryId());
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, true, false, "", "", this.t1012, "Back", "", "");
         this.topBar.setProperty(this.t5401, "", "", "", "", this.t1001, "Help");
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
         this.msg = new StringBuffer("* ").append("SwipePaymentCardScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var8) {
         this.msg = new StringBuffer("[").append("SwipePaymentCardScreen").append("]").append(var8.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var8);
      }
   }

   private void createCardTypePanel() {
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
               this.cardButton[i] = new JButton(Aem.getPaymentCardTypeIndexItem(i).getPaymentCardTypeName());
            }

            this.cardButton[i].setBackground(ScreenProperties.getColor("White"));
            this.cardButton[i].setForeground(ScreenProperties.getColor("White"));
            this.cardButton[i].setActionCommand("PaymentCardType " + Aem.getPaymentCardTypeIndexItem(i).getPaymentCardTypeId());
            this.cardButton[i].setBorder(new LineBorder(ScreenProperties.getColor("Black")));
            this.cardButton[i].setBounds(i * 72, 0, 60, 38);
            this.cardTypePanel.add(this.cardButton[i]);
         }

         this.cardTypePanel.setBounds(150, 350, 400, 50);
      }
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

   public class SwipePaymentCardAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("SwipePaymentCardScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(SwipePaymentCardScreen.mainAction)) {
                  if (this.cmd[0].equals("Back")) {
                     SwipePaymentCardScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                  } else if (this.cmd[0].equals("Help")) {
                     SwipePaymentCardScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  } else if (this.cmd[0].equals("Agreement")) {
                     SwipePaymentCardScreen.mainAction
                        .actionPerformed(new ActionEvent(this, 1001, "RentalAgreementScreen " + SwipePaymentCardScreen.this.passingData));
                  } else if (this.cmd[0].equals("PaymentCardType")) {
                     SwipePaymentCardScreen.aemContent.setPaymentCardTypeId(Integer.parseInt(this.cmd[1]));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "SwipePaymentCardScreen");
         } catch (Throwable var5) {
            Log.error(var5, "SwipePaymentCardScreen");
         }
      }
   }
}
