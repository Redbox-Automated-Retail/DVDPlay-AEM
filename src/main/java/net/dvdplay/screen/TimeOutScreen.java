package net.dvdplay.screen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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

public class TimeOutScreen extends AbstractContentBar {
   ActionListener timeOutAction;
   JPanel outterBorder;
   JPanel centerBar;
   JPanel buttonPanel;
   JPanel titlePanel;
   JPanel contentPanel;
   JLabel label1;
   JButton yes;
   JButton no;
   Locale currentLocale;
   static Timer ownTimer = new Timer(30000, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
         try {
            if (!Aem.needToReinitialize() || (!AbstractContentBar.aemContent.getPrevScreen().equals(DvdplayBase.RETURN_THANK_YOU_SCREEN) && !AbstractContentBar.aemContent.getPrevScreen().equals(DvdplayBase.HELP_MAIN_SCREEN) && !AbstractContentBar.aemContent.getPrevScreen().equals(DvdplayBase.HELP_ANSWER_SCREEN))) {
               if (AbstractContentBar.aemContent.getPrevScreen().equals(DvdplayBase.PAYMENT_CARD_APPROVED_SCREEN) || AbstractContentBar.aemContent.getPrevScreen().equals(DvdplayBase.EMAIL_SCREEN)) {
                  AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, "DeliveringDVDScreen 0"));
               } else {
                  AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.TIME_OUT_SCREEN));
               }
            } else {
               AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.INITIALIZING_AEM_SCREEN));
            }
            TimeOutScreen.ownTimer.stop();
         } catch (Exception e) {
            Log.warning(e, DvdplayBase.TIME_OUT_SCREEN);
         } catch (Throwable t) {
            Log.error(t, DvdplayBase.TIME_OUT_SCREEN);
         }
      }
   });


   public TimeOutScreen(String prevScreen, String currScreen, String data) {
      try {
         this.currentLocale = Aem.getLocale();
         this.timeOutAction = new TimeOutScreen.TimeOutAction();
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         this.createBorder();
         this.createButtonPanel();
         this.createTitlePanel();
         this.createContentPanel();
         this.outterBorder.add(this.titlePanel);
         this.outterBorder.add(this.contentPanel);
         this.outterBorder.add(this.buttonPanel);
         this.centerBar = new JPanel(null);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.centerBar.setBackground(ScreenProperties.getColor("White"));
         this.centerBar.add(this.outterBorder);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar("", "", "", "", "", "", "");
         this.setBounds(0, 0, 1024, 768);
         this.add(this.centerBar);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.msg = new StringBuffer("* ").append("TimeOutScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("TimeOutScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (this.currentLocale != Aem.getLocale()) {
            this.label1.setText(Aem.getString(4001));
            this.currentLocale = Aem.getLocale();
         }

         AbstractContentBar.timer.stop();
         ownTimer.start();
         this.msg = new StringBuffer("* ").append("TimeOutScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("TimeOutScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   private void createTitlePanel() {
      JLabel promoCode = new JLabel(ScreenProperties.getImage("TimeOut.Icon"));
      promoCode.setBounds(5, 5, 97, 115);
      promoCode.setBackground(ScreenProperties.getColor("White"));
      promoCode.setOpaque(true);
      promoCode.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
      this.titlePanel = new JPanel();
      this.titlePanel.setBounds(5, 5, 150, 120);
      this.titlePanel.setBackground(ScreenProperties.getColor("White"));
      this.titlePanel.add(promoCode);
   }

   private void createContentPanel() {
      this.label1 = new JLabel(Aem.getString(4001));
      this.label1.setFont(ScreenProperties.getFont("TimeOutScreen"));
      this.label1.setHorizontalAlignment(0);
      this.label1.setBounds(10, 5, 600, 30);
      this.contentPanel = new JPanel(null);
      this.contentPanel.setBackground(ScreenProperties.getColor("White"));
      this.contentPanel.setBounds(10, 190, 600, 150);
      this.contentPanel.add(this.label1);
   }

   private void createButtonPanel() {
      this.yes = new JButton(Aem.getString(6209));
      this.yes.setBackground(ScreenProperties.getColor("White"));
      this.yes.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
      this.yes.setFont(ScreenProperties.getFont("TopBarButton"));
      this.yes.setForeground(ScreenProperties.getColor("Black"));
      this.yes.setHorizontalTextPosition(2);
      this.yes.setVerticalTextPosition(0);
      this.yes.setBounds(150, 10, 100, 60);
      this.yes.setActionCommand("Yes");
      this.yes.setFocusPainted(false);
      this.yes.addActionListener(this.timeOutAction);
      this.no = new JButton(Aem.getString(6210));
      this.no.setBackground(ScreenProperties.getColor("White"));
      this.no.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
      this.no.setFont(ScreenProperties.getFont("TopBarButton"));
      this.no.setForeground(ScreenProperties.getColor("Black"));
      this.no.setHorizontalTextPosition(2);
      this.no.setVerticalTextPosition(0);
      this.no.setBounds(10, 10, 100, 60);
      this.no.setActionCommand("No");
      this.no.setFocusPainted(false);
      this.no.addActionListener(this.timeOutAction);
      this.buttonPanel = new JPanel(null);
      this.buttonPanel.setBackground(ScreenProperties.getColor("White"));
      this.buttonPanel.add(this.yes);
      this.buttonPanel.add(this.no);
      this.buttonPanel.setBounds(380, 370, 260, 80);
   }

   private void createBorder() {
      this.outterBorder = new JPanel(null);
      this.outterBorder.setBorder(new LineBorder(ScreenProperties.getColor("Gray"), 5));
      this.outterBorder.setBounds(180, 80, 664, 470);
      this.outterBorder.setBackground(ScreenProperties.getColor("White"));
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

   public class TimeOutAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("TimeOutScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(TimeOutScreen.mainAction, TimeOutScreen.ownTimer)) {
                  TimeOutScreen.ownTimer.stop();
                  if (this.cmd[0].equals("Yes")) {
                     TimeOutScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, TimeOutScreen.aemContent.getPrevScreen()));
                  }

                  if (this.cmd[0].equals("No")) {
                     if (!TimeOutScreen.aemContent.getPrevScreen().equals("PaymentCardApprovedScreen")
                        && !TimeOutScreen.aemContent.getPrevScreen().equals("EmailScreen")) {
                        if (!Aem.needToReinitialize()
                           || !TimeOutScreen.aemContent.getPrevScreen().equals("ReturnThankYouScreen")
                              && !TimeOutScreen.aemContent.getPrevScreen().equals("AboutCompanyScreen")
                              && !TimeOutScreen.aemContent.getPrevScreen().equals("HelpMainScreen")
                              && !TimeOutScreen.aemContent.getPrevScreen().equals("HelpAnswerScreen")) {
                           TimeOutScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                        } else {
                           TimeOutScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                        }
                     } else {
                        TimeOutScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "DeliveringDVDScreen 0"));
                     }
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "TimeOutScreen");
         } catch (Throwable var5) {
            Log.error(var5, "TimeOutScreen");
         }
      }
   }
}
