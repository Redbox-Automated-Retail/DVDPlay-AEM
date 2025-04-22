package net.dvdplay.screen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class MaximumDiscExceededScreen extends AbstractContentBar {
   ActionListener maximumDiscExceededAction;
   JLabel text1;
   JLabel text2;
   JLabel text3;
   JLabel text3_1;
   JLabel text4;
   JLabel text5;
   JLabel text6;
   JLabel text6_1;
   JLabel text7;
   JLabel text7_1;
   JLabel text8;
   JLabel text8_1;
   JLabel text8_2;
   JLabel text9;
   JLabel text9_1;
   JLabel text10;
   StringBuffer dvdString;
   JPanel outterBorder;
   JPanel centerBar;
   JPanel buttonPanel;
   JPanel titlePanel;
   JPanel contentPanel;
   JPanel seperatorBar;
   JLabel[] errLabel;
   TextToRows err;
   Locale currentLocale;
   int charSize = 8;
   int currentTitleTypeID = 0;
   protected Timer ownTimer = new Timer(DvdplayBase.ERROR_SCREEN_TIME_OUT, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
         AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.CART_TABLE_SCREEN));
         ownTimer.stop();
         Aem.logDetailMessage(DvdplayLevel.FINE, DvdplayBase.OPEN_QUOTE + DvdplayBase.MAXIMUM_DISC_EXCEEDED_SCREEN + DvdplayBase.CLOSE_QUOTE + " timeout ");
      }
   });


   public MaximumDiscExceededScreen(String prevScreen, String currScreen, String data) {
      try {
         this.currentLocale = Aem.getLocale();
         this.maximumDiscExceededAction = new MaximumDiscExceededScreen.MaximumDiscExceededAction();
         this.currentTitleTypeID = Aem.getTitleTypeId();
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         this.createBorder();
         this.createTitlePanel();
         this.createContent();
         this.outterBorder.add(this.titlePanel);
         this.outterBorder.add(this.contentPanel);
         this.centerBar = new JPanel(null);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.centerBar.setBackground(ScreenProperties.getColor("White"));
         this.centerBar.add(this.outterBorder);
         this.createBlackBottomBar(true, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.createTopBar("", "", "", "", "", "", "");
         this.bottomBar.addActionListener(this.maximumDiscExceededAction);
         this.setBounds(0, 0, 1024, 768);
         this.add(this.centerBar);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.msg = new StringBuffer("* ").append("MaximumDiscExceededScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("MaximumDiscExceededScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         this.ownTimer.start();
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (this.currentLocale != Aem.getLocale() || this.currentTitleTypeID != Aem.getTitleTypeId()) {
            this.currentLocale = Aem.getLocale();
            this.currentTitleTypeID = Aem.getTitleTypeId();
            this.text1.setText(Aem.getString(4052));
            this.text2.setText(Aem.getString(4053));
            this.text3 = new JLabel(Aem.getString(4054));
            this.text5 = new JLabel(Aem.getString(4056));
            this.text6 = new JLabel(Aem.getString(4057));
            this.text4 = new JLabel(Aem.getString(4055));
            this.text7 = new JLabel(Aem.getString(4058));
            this.text7_1 = new JLabel(Aem.getString(4059));
            this.text7_1.setBounds(Aem.getString(4058).length() * this.charSize - 5, 220, 600, 20);
            this.text8 = new JLabel(Aem.getString(4060));
            this.text8_1 = new JLabel(Aem.getString(4061));
            this.text8_1.setBounds(Aem.getString(4060).length() * this.charSize - 5, 250, 600, 20);
            this.text8_2.setBounds(20 + (Aem.getString(4060).length() + Aem.getString(4061).length()) * this.charSize, 250, 600, 20);
            this.text9 = new JLabel(Aem.getString(4063));
            this.text9_1 = new JLabel(Aem.getString(4064));
            this.text9_1.setBounds(Aem.getString(4063).length() * this.charSize, 280, 600, 20);
            this.text10 = new JLabel(Aem.getString(4065));
            int firstMax = Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getFirstTimeMax();
            this.dvdString = new StringBuffer(firstMax > 1 ? firstMax + " " + Aem.getString(4067) : firstMax + " " + Aem.getString(4066));
            this.text3_1.setText(this.dvdString.toString());
            this.text3_1.setBounds(40 + Aem.getString(4054).length() * this.charSize + 3, 80, 100, 20);
            int repeatMax = Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getRegularUserMax();
            this.dvdString = new StringBuffer(repeatMax > 1 ? repeatMax + " " + Aem.getString(4067) : repeatMax + " " + Aem.getString(4066));
            this.text6_1.setText(this.dvdString.toString());
            this.text6_1.setBounds(40 + Aem.getString(4057).length() * this.charSize - 2, 160, 100, 20);
            this.text4.setBounds(35 + (Aem.getString(4057).length() + this.dvdString.length()) * this.charSize, 160, 600, 20);
            this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, true, false, "", "", "", "", Aem.getString(5701), "Continue");
         }

         this.msg = new StringBuffer("* ").append("MaximumDiscExceededScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("MaximumDiscExceededScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var6);
      }
   }

   private void createTitlePanel() {
      JLabel ops = new JLabel(ScreenProperties.getImage("Error.Icon.Ops"));
      ops.setText("  " + Aem.getString(4023));
      ops.setFont(ScreenProperties.getFont("ErrorWarningScreenTitle"));
      ops.setBounds(0, 0, 350, 55);
      ops.setHorizontalAlignment(2);
      ops.setBackground(ScreenProperties.getColor("White"));
      ops.setForeground(ScreenProperties.getColor("Red"));
      ops.setOpaque(true);
      ops.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
      this.titlePanel = new JPanel(null);
      this.titlePanel.setBounds(15, 15, 350, 55);
      this.titlePanel.setBackground(ScreenProperties.getColor("White"));
      this.titlePanel.add(ops);
   }

   public void createContent() {
      this.contentPanel = new JPanel(null);
      this.contentPanel.setBounds(20, 80, 600, 370);
      this.contentPanel.setBackground(ScreenProperties.getColor("White"));
      this.text1 = new JLabel(Aem.getString(4052));
      this.text1.setBackground(ScreenProperties.getColor("White"));
      this.text1.setForeground(ScreenProperties.getColor("Red"));
      this.text1.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text1.setBounds(0, 10, 600, 20);
      JLabel arrow1 = new JLabel(ScreenProperties.getImage("RedArrowWhite.Logo.Icon"));
      arrow1.setBackground(ScreenProperties.getColor("White"));
      arrow1.setBounds(0, 50, 40, 20);
      this.text2 = new JLabel(Aem.getString(4053));
      this.text2.setBackground(ScreenProperties.getColor("White"));
      this.text2.setForeground(ScreenProperties.getColor("Black"));
      this.text2.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text2.setBounds(40, 50, 600, 20);
      this.text3 = new JLabel(Aem.getString(4054));
      this.text3.setBackground(ScreenProperties.getColor("White"));
      this.text3.setForeground(ScreenProperties.getColor("Black"));
      this.text3.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text3.setBounds(40, 80, Aem.getString(4054).length() * this.charSize, 20);
      int firstMax = Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getFirstTimeMax();
      this.dvdString = new StringBuffer(firstMax > 1 ? firstMax + " " + Aem.getString(4067) : firstMax + " " + Aem.getString(4066));
      this.text3_1 = new JLabel(this.dvdString.toString());
      this.text3_1.setBackground(ScreenProperties.getColor("White"));
      this.text3_1.setForeground(ScreenProperties.getColor("Red"));
      this.text3_1.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text3_1.setBounds(40 + Aem.getString(4054).length() * this.charSize + 3, 80, 100, 20);
      JLabel arrow2 = new JLabel(ScreenProperties.getImage("RedArrowWhite.Logo.Icon"));
      arrow2.setBackground(ScreenProperties.getColor("White"));
      arrow2.setBounds(0, 130, 40, 20);
      this.text5 = new JLabel(Aem.getString(4056));
      this.text5.setBackground(ScreenProperties.getColor("White"));
      this.text5.setForeground(ScreenProperties.getColor("Black"));
      this.text5.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text5.setBounds(40, 130, 600, 20);
      this.text6 = new JLabel(Aem.getString(4057));
      this.text6.setBackground(ScreenProperties.getColor("White"));
      this.text6.setForeground(ScreenProperties.getColor("Black"));
      this.text6.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text6.setBounds(40, 160, Aem.getString(4057).length() * this.charSize, 20);
      int repeatMax = Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getRegularUserMax();
      this.dvdString = new StringBuffer(repeatMax > 1 ? repeatMax + " " + Aem.getString(4067) : repeatMax + " " + Aem.getString(4066));
      this.text6_1 = new JLabel(this.dvdString.toString());
      this.text6_1.setBackground(ScreenProperties.getColor("White"));
      this.text6_1.setForeground(ScreenProperties.getColor("Red"));
      this.text6_1.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text6_1.setBounds(40 + Aem.getString(4057).length() * this.charSize - 2, 160, 100, 20);
      this.text4 = new JLabel(Aem.getString(4055));
      this.text4.setBackground(ScreenProperties.getColor("White"));
      this.text4.setForeground(ScreenProperties.getColor("Black"));
      this.text4.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text4.setBounds(100 + Aem.getString(4057).length() * this.charSize, 160, 600, 20);
      this.text7 = new JLabel(Aem.getString(4058));
      this.text7.setBackground(ScreenProperties.getColor("White"));
      this.text7.setForeground(ScreenProperties.getColor("Black"));
      this.text7.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text7.setBounds(0, 220, 600, 20);
      this.text7_1 = new JLabel(Aem.getString(4059));
      this.text7_1.setBackground(ScreenProperties.getColor("White"));
      this.text7_1.setForeground(ScreenProperties.getColor("Red"));
      this.text7_1.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text7_1.setBounds(Aem.getString(4058).length() * this.charSize - 5, 220, 600, 20);
      this.text8 = new JLabel(Aem.getString(4060));
      this.text8.setBackground(ScreenProperties.getColor("White"));
      this.text8.setForeground(ScreenProperties.getColor("Black"));
      this.text8.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text8.setBounds(0, 250, 600, 20);
      this.text8_1 = new JLabel(Aem.getString(4061));
      this.text8_1.setBackground(ScreenProperties.getColor("White"));
      this.text8_1.setForeground(ScreenProperties.getColor("Red"));
      this.text8_1.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text8_1.setBounds(Aem.getString(4060).length() * this.charSize - 5, 250, 600, 20);
      this.text8_2 = new JLabel(Aem.getString(4062));
      this.text8_2.setBackground(ScreenProperties.getColor("White"));
      this.text8_2.setForeground(ScreenProperties.getColor("Black"));
      this.text8_2.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text8_2.setBounds(20 + (Aem.getString(4060).length() + Aem.getString(4061).length()) * this.charSize, 250, 600, 20);
      this.text9 = new JLabel(Aem.getString(4063));
      this.text9.setBackground(ScreenProperties.getColor("White"));
      this.text9.setForeground(ScreenProperties.getColor("Black"));
      this.text9.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text9.setBounds(0, 280, 600, 20);
      this.text9_1 = new JLabel(Aem.getString(4064));
      this.text9_1.setBackground(ScreenProperties.getColor("White"));
      this.text9_1.setForeground(ScreenProperties.getColor("Red"));
      this.text9_1.setFont(ScreenProperties.getFont("MaximumDiscExceededScreen"));
      this.text9_1.setBounds(Aem.getString(4063).length() * this.charSize, 280, 600, 20);
      this.seperatorBar = this.createBorder(ScreenProperties.getColor("Black"), 0, 330, 600, 1);
      this.text10 = new JLabel(Aem.getString(4065));
      this.text10.setBackground(ScreenProperties.getColor("White"));
      this.text10.setForeground(ScreenProperties.getColor("Black"));
      this.text10.setFont(ScreenProperties.getFont("MovieSelectionTitle"));
      this.text10.setBounds(0, 340, 600, 20);
      this.contentPanel.add(this.text1);
      this.contentPanel.add(arrow1);
      this.contentPanel.add(this.text2);
      this.contentPanel.add(this.text3);
      this.contentPanel.add(this.text3_1);
      this.contentPanel.add(arrow2);
      this.contentPanel.add(this.text4);
      this.contentPanel.add(this.text5);
      this.contentPanel.add(this.text6);
      this.contentPanel.add(this.text6_1);
      this.contentPanel.add(this.text7);
      this.contentPanel.add(this.text7_1);
      this.contentPanel.add(this.text8);
      this.contentPanel.add(this.text8_1);
      this.contentPanel.add(this.text8_2);
      this.contentPanel.add(this.text9);
      this.contentPanel.add(this.text9_1);
      this.contentPanel.add(this.seperatorBar);
      this.contentPanel.add(this.text10);
   }

   private void createBorder() {
      this.outterBorder = new JPanel(null);
      this.outterBorder.setBorder(new LineBorder(ScreenProperties.getColor("Gray"), 5));
      this.outterBorder.setBounds(180, 80, 664, 470);
      this.outterBorder.setBackground(ScreenProperties.getColor("White"));
      this.outterBorder.setLayout(null);
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

   public class MaximumDiscExceededAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("MaximumDiscExceededScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(MaximumDiscExceededScreen.mainAction, MaximumDiscExceededScreen.this.ownTimer)) {
                  MaximumDiscExceededScreen.this.ownTimer.stop();
                  if (this.cmd[0].equals("Continue")) {
                     MaximumDiscExceededScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "MaximumDiscExceededScreen");
         } catch (Throwable var5) {
            Log.error(var5, "MaximumDiscExceededScreen");
         }
      }
   }
}
