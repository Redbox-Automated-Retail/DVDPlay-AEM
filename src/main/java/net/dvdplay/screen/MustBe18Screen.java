package net.dvdplay.screen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
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

public class MustBe18Screen extends AbstractContentBar {
   ActionListener mustBe18Action;
   JPanel outterBorder;
   JPanel centerBar;
   JPanel buttonPanel;
   JPanel titlePanel;
   JPanel contentPanel;
   JLabel label1;
   JLabel label2;
   JLabel label3;
   JLabel promoCode;
   JLabel title;
   JButton yes;
   JButton no;
   String type;
   String nextScreen;
   String remainingData;
   ImageIcon ii;

   public MustBe18Screen(String prevScreen, String currScreen, String data) {
      try {
         this.mustBe18Action = new MustBe18Screen.MustBe18Action();
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         this.centerBar = new JPanel();
         this.centerBar.setLayout(null);
         this.centerBar.setBackground(Color.WHITE);
         this.type = "Rent";
         this.createBorder();
         this.createButtonPanel();
         this.createTitlePanel();
         this.createContentPanel();
         this.centerBar = new JPanel();
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.centerBar.setBackground(ScreenProperties.getColor("White"));
         this.centerBar.add(this.outterBorder);
         this.createBlackBottomBar(true, false, "", "", "", "", "", "");
         this.createTopBar("", "", "", "", "", "", "");
         this.add(this.buttonPanel);
         this.add(this.titlePanel);
         this.add(this.contentPanel);
         this.add(this.centerBar);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         Aem.logDetailMessage(DvdplayLevel.INFO, "* MustBe18Screen from " + prevScreen);
      } catch (Exception var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[Mustbe18Screen] " + var5.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         StringTokenizer stk = new StringTokenizer(data, ",");
         this.type = stk.nextToken();
         this.nextScreen = stk.nextToken();
         if (stk.hasMoreTokens()) {
            this.remainingData = stk.nextToken();
         }

         if (this.type.equals("Rent")) {
            this.label1.setText(Aem.getString(6205));
            this.label2.setText(Aem.getString(6206));
         } else {
            this.label1.setText(Aem.getString(6203));
            this.label2.setText(Aem.getString(6204));
         }

         Aem.logDetailMessage(DvdplayLevel.INFO, "* MustBe18Screen from " + prevScreen);
      } catch (Exception var5) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[Mustbe18Screen] " + var5.toString(), var5);
      }
   }

   private void createTitlePanel() {
      this.ii = ScreenProperties.getImage("Cart.Icon.Error");
      this.promoCode = new JLabel(this.ii);
      this.promoCode.setBounds(5, 5, ScreenProperties.getInt("PromoCode.Logo.Width") + 17, ScreenProperties.getInt("PromoCode.Logo.Height") + 10);
      this.promoCode.setBackground(ScreenProperties.getColor("White"));
      this.promoCode.setOpaque(true);
      this.promoCode.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
      this.title = new JLabel(Aem.getString(6201));
      this.title.setFont(ScreenProperties.getFont("ErrorWarningScreenTitle"));
      this.title.setForeground(ScreenProperties.getColor("Red"));
      this.title.setBounds(135, 25, 150, 40);
      this.titlePanel = new JPanel();
      this.titlePanel.setBounds(190, 150, 300, 80);
      this.titlePanel.setBackground(ScreenProperties.getColor("White"));
      this.titlePanel.add(this.title);
      this.titlePanel.add(this.promoCode);
   }

   private void createContentPanel() {
      if (this.type.equals("Rent")) {
         this.label1 = new JLabel(Aem.getString(6205));
         this.label2 = new JLabel(Aem.getString(6206));
      } else {
         this.label1 = new JLabel(Aem.getString(6203));
         this.label2 = new JLabel(Aem.getString(6204));
      }

      this.label1.setFont(ScreenProperties.getFont("ErrorWarningScreen"));
      this.label1.setBounds(10, 5, 500, 30);
      this.label2.setFont(ScreenProperties.getFont("ErrorWarningScreen"));
      this.label2.setBounds(10, 45, 500, 30);
      this.label3 = new JLabel(Aem.getString(6202));
      this.label3.setFont(ScreenProperties.getFont("ErrorWarningScreen"));
      this.label3.setBounds(10, 100, 500, 40);
      this.contentPanel = new JPanel();
      this.contentPanel.setBackground(ScreenProperties.getColor("White"));
      this.contentPanel.setBounds(275, 300, 500, 150);
      this.contentPanel.add(this.label1);
      this.contentPanel.add(this.label2);
      this.contentPanel.add(this.label3);
   }

   private void createButtonPanel() {
      this.yes = new JButton(Aem.getString(6209));
      this.yes.setBackground(ScreenProperties.getColor("White"));
      this.yes.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
      this.yes.setFont(ScreenProperties.getFont("TopBarButton"));
      this.yes.setForeground(ScreenProperties.getColor("Black"));
      this.yes.setHorizontalTextPosition(2);
      this.yes.setVerticalTextPosition(0);
      this.yes.setBounds(10, 10, 100, 60);
      this.yes.setActionCommand("Yes");
      this.yes.addActionListener(this.mustBe18Action);
      this.no = new JButton(Aem.getString(6210));
      this.no.setBackground(ScreenProperties.getColor("White"));
      this.no.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
      this.no.setFont(ScreenProperties.getFont("TopBarButton"));
      this.no.setForeground(ScreenProperties.getColor("Black"));
      this.no.setHorizontalTextPosition(2);
      this.no.setVerticalTextPosition(0);
      this.no.setBounds(150, 10, 100, 60);
      this.no.setActionCommand("No");
      this.no.addActionListener(this.mustBe18Action);
      this.buttonPanel = new JPanel(null);
      this.buttonPanel.setBackground(ScreenProperties.getColor("White"));
      this.buttonPanel.add(this.yes);
      this.buttonPanel.add(this.no);
      this.buttonPanel.setBounds(550, 500, 260, 100);
   }

   private void createBorder() {
      this.outterBorder = new JPanel();
      this.outterBorder.setBorder(new LineBorder(ScreenProperties.getColor("Gray"), 5));
      this.outterBorder.setBounds(180, 80, 664, 470);
      this.outterBorder.setBackground(ScreenProperties.getColor("White"));
      this.outterBorder.setLayout(null);
   }

   public class MustBe18Action extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("MustBe18Screen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(MustBe18Screen.mainAction)) {
                  if (this.cmd[0].equals("Yes")) {
                     MustBe18Screen.mainAction
                        .actionPerformed(new ActionEvent(this, 1001, MustBe18Screen.this.nextScreen + " " + MustBe18Screen.this.remainingData));
                  } else if (this.cmd[0].equals("No")) {
                     MustBe18Screen.aemContent.setCurrentCategoryId(-1);
                     MustBe18Screen.mainAction.actionPerformed(new ActionEvent(this, 1001, MustBe18Screen.aemContent.getPrevScreen()));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "MustBe18Screen");
         } catch (Throwable var5) {
            Log.error(var5, "MustBe18Screen");
         }
      }
   }
}
