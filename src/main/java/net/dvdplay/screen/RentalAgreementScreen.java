package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;
import net.dvdplay.util.Util;

public class RentalAgreementScreen extends AbstractContentBar {
   ActionListener rentalAgreementAction;
   JPanel contentPanel;
   JLabel label1_h;
   JLabel label2_h;
   JLabel label3_h;
   JLabel label4_h;
   JLabel label5_h;
   JLabel label6_h;
   JLabel label6_1;
   JLabel label6_2;
   JLabel label6_3;
   JLabel label6_4;
   JLabel label6_5;
   JLabel label6_6;
   JLabel[] label1_1;
   JLabel[] label1_2;
   JLabel[] label1_3;
   JLabel[] label1_4;
   JLabel[] label2;
   JLabel[] label3;
   JLabel[] label4;
   JLabel[] label5;
   JLabel[] label6;
   JLabel[] label1_h1;
   String passingData = "";
   String topBarTitle = "";
   TextToRows ttr1;
   int locationY = 0;
   int charInARow = 150;
   int numTitleTypes = Aem.createTitleTypeList();

   public RentalAgreementScreen(String prevScreen, String currScreen, String data) {
      try {
         this.rentalAgreementAction = new RentalAgreementScreen.RentalAgreementAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.contentPanel = new JPanel(null);
         this.contentPanel.setBackground(ScreenProperties.getColor("White"));
         this.createPanel1();
         this.createPanel2();
         this.createPanel3();
         this.createPanel4();
         this.createPanel5();
         this.createPanel6();
         this.contentPanel.setBounds(65, 70, 900, 630);
         if (Aem.isBuyDisabled()) {
            this.topBarTitle = Aem.getString(5407);
         } else {
            this.topBarTitle = Aem.getString(5406);
         }

         this.createBlackBottomBar(true, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.createTopBar(this.topBarTitle, "", "", "", "", "", "");
         this.bottomBar.addActionListener(this.rentalAgreementAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.contentPanel);
         this.setBounds(0, 0, 1524, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("RentalAgreementScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("RentalAgreementScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.passingData = data;
         this.label1_h1 = new JLabel[this.numTitleTypes];

         for (int i = 0; i < this.numTitleTypes; i++) {
            this.label1_h1[i] = new JLabel(
               Aem.getString(7119)
                  + " "
                  + Util.capFirstChar(Aem.getTitleTypeIndexItem(i).getTitleTypeSingular(), Aem.getLocale())
                  + " : "
                  + Aem.getCurrencySymbol()
                  + Aem.getTitleTypeIndexItem(i).getMaxCharge()
            );
         }

         if (Aem.isBuyDisabled()) {
            this.topBarTitle = Aem.getString(5407);
         } else {
            this.topBarTitle = Aem.getString(5406);
         }

         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, true, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.topBar.setProperty(this.topBarTitle, "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("RentalAgreementScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("RentalAgreementScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void createPanel1() {
      this.locationY = 0;
      if (Aem.isBuyDisabled()) {
         this.label1_h = new JLabel(Aem.getString(7120));
      } else {
         this.label1_h = new JLabel(Aem.getString(7102));
      }

      this.label1_h.setForeground(ScreenProperties.getColor("Red"));
      this.label1_h.setBackground(ScreenProperties.getColor("White"));
      this.label1_h.setFont(ScreenProperties.getFont("RentalAgreementTitle"));
      this.label1_h.setBounds(0, this.locationY, 950, 30);
      this.contentPanel.add(this.label1_h);
      this.locationY = 10;
      this.ttr1 = new TextToRows(Aem.getString(7103), this.charInARow);
      this.label1_1 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label1_1[i] = new JLabel(this.ttr1.getRow(i));
         this.label1_1[i].setForeground(ScreenProperties.getColor("Black"));
         this.label1_1[i].setBackground(ScreenProperties.getColor("White"));
         this.label1_1[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label1_1[i].setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label1_1[i]);
      }

      this.locationY += 10;
      this.ttr1 = new TextToRows(Aem.getString(7104), this.charInARow);
      this.label1_2 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label1_2[i] = new JLabel(this.ttr1.getRow(i));
         this.label1_2[i].setForeground(ScreenProperties.getColor("Black"));
         this.label1_2[i].setBackground(ScreenProperties.getColor("White"));
         this.label1_2[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label1_2[i].setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label1_2[i]);
      }

      this.label1_h1 = new JLabel[this.numTitleTypes];

      for (int i = 0; i < this.numTitleTypes; i++) {
         this.label1_h1[i] = new JLabel(
            Aem.getString(7119)
               + " "
               + Util.capFirstChar(Aem.getTitleTypeIndexItem(i).getTitleTypeSingular(), Aem.getLocale())
               + " : "
               + Aem.getCurrencySymbol()
               + Aem.getTitleTypeIndexItem(i).getMaxCharge()
         );
         this.label1_h1[i].setForeground(ScreenProperties.getColor("Black"));
         this.label1_h1[i].setBackground(ScreenProperties.getColor("White"));
         this.label1_h1[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label1_h1[i].setBounds(0, this.locationY, 950, 30);
         this.contentPanel.add(this.label1_h1[i]);
      }

      this.locationY += 10;
      this.ttr1 = new TextToRows(Aem.getString(7105), this.charInARow);
      this.label1_3 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label1_3[i] = new JLabel(this.ttr1.getRow(i));
         this.label1_3[i].setForeground(ScreenProperties.getColor("Black"));
         this.label1_3[i].setBackground(ScreenProperties.getColor("White"));
         this.label1_3[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label1_3[i].setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label1_3[i]);
      }

      if (!Aem.isBuyDisabled()) {
         this.locationY += 10;
         this.ttr1 = new TextToRows(Aem.getString(7106), this.charInARow);
         this.label1_4 = new JLabel[this.ttr1.getRowCount()];

         for (int i = 0; i < this.ttr1.getRowCount(); i++) {
            this.label1_4[i] = new JLabel(this.ttr1.getRow(i));
            this.label1_4[i].setForeground(ScreenProperties.getColor("Black"));
            this.label1_4[i].setBackground(ScreenProperties.getColor("White"));
            this.label1_4[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
            this.locationY += 15;
            this.label1_4[i].setBounds(0, this.locationY, 950, 20);
            this.contentPanel.add(this.label1_4[i]);
         }
      }
   }

   public void createPanel2() {
      this.locationY += 15;
      this.label2_h = new JLabel(Aem.getString(7108));
      this.label2_h.setForeground(ScreenProperties.getColor("Red"));
      this.label2_h.setBackground(ScreenProperties.getColor("White"));
      this.label2_h.setFont(ScreenProperties.getFont("RentalAgreementTitle"));
      this.label2_h.setBounds(0, this.locationY, 950, 30);
      this.contentPanel.add(this.label2_h);
      this.locationY += 10;
      this.ttr1 = new TextToRows(Aem.getString(7109), this.charInARow);
      this.label2 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label2[i] = new JLabel(this.ttr1.getRow(i));
         this.label2[i].setForeground(ScreenProperties.getColor("Black"));
         this.label2[i].setBackground(ScreenProperties.getColor("White"));
         this.label2[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label2[i].setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label2[i]);
      }
   }

   public void createPanel3() {
      this.locationY += 15;
      this.label3_h = new JLabel(Aem.getString(7110));
      this.label3_h.setForeground(ScreenProperties.getColor("Red"));
      this.label3_h.setBackground(ScreenProperties.getColor("White"));
      this.label3_h.setFont(ScreenProperties.getFont("RentalAgreementTitle"));
      this.label3_h.setBounds(0, this.locationY, 950, 30);
      this.contentPanel.add(this.label3_h);
      this.locationY += 10;
      this.ttr1 = new TextToRows(Aem.getString(7111), this.charInARow);
      this.label3 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label3[i] = new JLabel(this.ttr1.getRow(i));
         this.label3[i].setForeground(ScreenProperties.getColor("Black"));
         this.label3[i].setBackground(ScreenProperties.getColor("White"));
         this.label3[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label3[i].setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label3[i]);
      }
   }

   public void createPanel4() {
      this.locationY += 15;
      this.label4_h = new JLabel(Aem.getString(7112));
      this.label4_h.setForeground(ScreenProperties.getColor("Red"));
      this.label4_h.setBackground(ScreenProperties.getColor("White"));
      this.label4_h.setFont(ScreenProperties.getFont("RentalAgreementTitle"));
      this.label4_h.setBounds(0, this.locationY, 950, 30);
      this.contentPanel.add(this.label4_h);
      this.locationY += 10;
      this.ttr1 = new TextToRows(Aem.getString(7113) + Aem.getString(2011) + Aem.getString(7114), this.charInARow);
      this.label4 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label4[i] = new JLabel(this.ttr1.getRow(i));
         this.label4[i].setForeground(ScreenProperties.getColor("Black"));
         this.label4[i].setBackground(ScreenProperties.getColor("White"));
         this.label4[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label4[i].setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label4[i]);
      }
   }

   public void createPanel5() {
      this.locationY += 15;
      this.label5_h = new JLabel(Aem.getString(7115));
      this.label5_h.setForeground(ScreenProperties.getColor("Red"));
      this.label5_h.setBackground(ScreenProperties.getColor("White"));
      this.label5_h.setFont(ScreenProperties.getFont("RentalAgreementTitle"));
      this.label5_h.setBounds(0, this.locationY, 950, 30);
      this.contentPanel.add(this.label5_h);
      this.locationY += 10;
      this.ttr1 = new TextToRows(Aem.getString(2011) + Aem.getString(7116), this.charInARow);
      this.label5 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label5[i] = new JLabel(this.ttr1.getRow(i));
         this.label5[i].setForeground(ScreenProperties.getColor("Black"));
         this.label5[i].setBackground(ScreenProperties.getColor("White"));
         this.label5[i].setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.locationY += 15;
         this.label5[i].setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label5[i]);
      }
   }

   public void createPanel6() {
      this.locationY += 25;
      this.label6_h = new JLabel(Aem.getString(7117));
      this.label6_h.setForeground(ScreenProperties.getColor("Black"));
      this.label6_h.setBackground(ScreenProperties.getColor("White"));
      this.label6_h.setFont(ScreenProperties.getFont("RentalAgreementScreen"));
      this.label6_h.setBounds(0, this.locationY, 950, 20);
      this.locationY += 25;
      this.label6_1 = new JLabel(Aem.getString(2012));
      this.label6_1.setForeground(ScreenProperties.getColor("Red"));
      this.label6_1.setBackground(ScreenProperties.getColor("White"));
      this.label6_1.setFont(ScreenProperties.getFont("RentalAgreementTitle"));
      this.label6_1.setBounds(0, this.locationY, 950, 30);
      this.locationY += 25;
      this.label6_2 = new JLabel(Aem.getString(2013));
      this.label6_2.setForeground(ScreenProperties.getColor("Black"));
      this.label6_2.setBackground(ScreenProperties.getColor("White"));
      this.label6_2.setFont(ScreenProperties.getFont("RentalAgreementScreen"));
      this.label6_2.setBounds(0, this.locationY, 950, 20);
      if (!Aem.getString(2014).equals("")) {
         this.locationY += 15;
         this.label6_3 = new JLabel(Aem.getString(2014));
         this.label6_3.setForeground(ScreenProperties.getColor("Black"));
         this.label6_3.setBackground(ScreenProperties.getColor("White"));
         this.label6_3.setFont(ScreenProperties.getFont("RentalAgreementScreen"));
         this.label6_3.setBounds(0, this.locationY, 950, 20);
         this.contentPanel.add(this.label6_3);
      }

      this.locationY += 15;
      this.label6_4 = new JLabel(Aem.getString(2015));
      this.label6_4.setForeground(ScreenProperties.getColor("Black"));
      this.label6_4.setBackground(ScreenProperties.getColor("White"));
      this.label6_4.setFont(ScreenProperties.getFont("RentalAgreementScreen"));
      this.label6_4.setBounds(0, this.locationY, 950, 20);
      this.locationY += 15;
      this.label6_5 = new JLabel(Aem.getString(2016));
      this.label6_5.setForeground(ScreenProperties.getColor("Black"));
      this.label6_5.setBackground(ScreenProperties.getColor("White"));
      this.label6_5.setFont(ScreenProperties.getFont("RentalAgreementScreen"));
      this.label6_5.setBounds(0, this.locationY, 950, 20);
      this.locationY += 15;
      this.label6_6 = new JLabel(Aem.getString(2017));
      this.label6_6.setForeground(ScreenProperties.getColor("Black"));
      this.label6_6.setBackground(ScreenProperties.getColor("White"));
      this.label6_6.setFont(ScreenProperties.getFont("RentalAgreementScreen"));
      this.label6_6.setBounds(0, this.locationY, 950, 20);
      this.contentPanel.add(this.label6_h);
      this.contentPanel.add(this.label6_1);
      this.contentPanel.add(this.label6_2);
      this.contentPanel.add(this.label6_4);
      this.contentPanel.add(this.label6_5);
      this.contentPanel.add(this.label6_6);
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

   public class RentalAgreementAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("RentalAgreementScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(RentalAgreementScreen.mainAction) && this.cmd[0].equals("Continue")) {
                  RentalAgreementScreen.mainAction
                     .actionPerformed(new ActionEvent(this, 1001, "SwipePaymentCardScreen " + RentalAgreementScreen.this.passingData));
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "RentalAgreementScreen");
         } catch (Throwable var5) {
            Log.error(var5, "RentalAgreementScreen");
         }
      }
   }
}
