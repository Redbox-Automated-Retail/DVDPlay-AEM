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
import net.dvdplay.models.Error;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class ErrorScreen extends AbstractContentBar {
   ActionListener errorAction;
   JLabel text3;
   JLabel text4;
   JLabel text5;
   JLabel text6;
   JLabel text7;
   JLabel text8;
   JPanel outterBorder;
   JPanel centerBar;
   JPanel buttonPanel;
   JPanel titlePanel;
   JPanel contentPanel;
   JPanel franchiseeInfoPanel;
   JLabel[] errLabel;
   JButton okay;
   TextToRows err;
   int errId;
   static String nextScreen;
   String[] dataSplit;
   static String passingData = "";
   Error error;
   Locale currentLocale;
   int currentErrorID;
   protected Timer ownTimer = new Timer(DvdplayBase.ERROR_SCREEN_TIME_OUT, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
         try {
            if (!Aem.needToReinitialize() || !AbstractContentBar.aemContent.getPrevScreen().equals(DvdplayBase.RETURN_THANK_YOU_SCREEN)) {
               AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, ErrorScreen.nextScreen + DvdplayBase.SPACE_STRING + ErrorScreen.passingData));
            } else {
               AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.INITIALIZING_AEM_SCREEN));
            }
            ownTimer.stop();
            Aem.logDetailMessage(DvdplayLevel.FINE, "ErrorScreen timeout");
         } catch (Exception e) {
            Log.warning(e, DvdplayBase.ERROR_SCREEN);
         } catch (Throwable t) {
            Log.error(t, DvdplayBase.ERROR_SCREEN);
         }
      }
   });


   public ErrorScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setCurrentLocale(Aem.getLocale());
         this.error = new Error();
         this.errorAction = new ErrorScreen.ErrorAction();
         this.errId = 4039;
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         this.createBorder();
         this.createButtonPanel();
         this.createTitlePanel();
         this.createContentPanel();
         this.showFranchiseeInfo();
         this.outterBorder.add(this.titlePanel);
         this.outterBorder.add(this.contentPanel);
         this.outterBorder.add(this.buttonPanel);
         this.outterBorder.add(this.franchiseeInfoPanel);
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
         this.msg = new StringBuffer("* ").append("ErrorScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ErrorScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         passingData = "";

         try {
            this.dataSplit = data.split(",");
            this.errId = Integer.parseInt(this.dataSplit[0]);
            nextScreen = this.dataSplit[1];
            if (nextScreen.equals("DeliveringDVDScreen")) {
               if (Integer.parseInt(this.dataSplit[2]) < AbstractContentBar.aemContent.getDiscCount() - 1) {
                  passingData = "" + (Integer.parseInt(this.dataSplit[2]) + 1);
               } else {
                  nextScreen = "ReturnThankYouScreen";
                  passingData = "Rent";
               }
            }
         } catch (Exception var5) {
            this.errId = 0;
         }

         if (this.currentLocale != Aem.getLocale() || this.currentErrorID != this.errId) {
            this.err = new TextToRows(this.error.getErrorById(this.errId), 50);
            this.contentPanel.removeAll();
            this.errLabel = new JLabel[this.err.getRowCount()];

            for (int i = 0; i < this.err.getRowCount(); i++) {
               this.errLabel[i] = new JLabel(this.err.getRow(i));
               this.errLabel[i].setFont(ScreenProperties.getFont("TimeOutScreen"));
               this.errLabel[i].setHorizontalAlignment(2);
               this.errLabel[i].setBounds(0, 5 + i * 30, 500, 25);
               this.contentPanel.add(this.errLabel[i]);
            }

            if (this.errId != 4021 && this.errId != 4042 && this.errId != 4037 && this.errId != 4044) {
               this.franchiseeInfoPanel.setVisible(false);
            } else {
               this.franchiseeInfoPanel.setVisible(true);
               this.text3.setText(Aem.getString(2012));
               this.text4.setText(Aem.getString(2013));
               if (Aem.getString(2014).trim().equals("")) {
                  this.text5.setVisible(false);
                  this.text6.setBounds(0, 40, 500, 25);
                  this.text7.setBounds(0, 60, 500, 25);
                  this.text8.setBounds(0, 80, 500, 25);
               } else {
                  this.text5.setText(Aem.getString(2014));
                  this.text6.setBounds(0, 60, 500, 25);
                  this.text7.setBounds(0, 80, 500, 25);
                  this.text8.setBounds(0, 100, 500, 25);
                  this.text5.setVisible(true);
               }

               this.text6.setText(Aem.getString(2015));
               this.text7.setText(Aem.getString(2016));
               this.text8.setText(Aem.getString(2017));
            }

            this.currentLocale = Aem.getLocale();
            this.currentErrorID = this.errId;
         }

         this.msg = new StringBuffer("* ").append("ErrorScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         this.ownTimer.stop();
         this.ownTimer.start();
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("ErrorScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString());
      }
   }

   private void createTitlePanel() {
      JLabel ops = new JLabel(ScreenProperties.getImage("Error.Icon.Ops"));
      ops.setBounds(5, 5, 50, 50);
      ops.setBackground(ScreenProperties.getColor("White"));
      ops.setForeground(ScreenProperties.getColor("White"));
      ops.setOpaque(true);
      ops.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
      this.titlePanel = new JPanel(null);
      this.titlePanel.setBounds(5, 5, 150, 55);
      this.titlePanel.setBackground(ScreenProperties.getColor("White"));
      this.titlePanel.add(ops);
   }

   private void createContentPanel() {
      this.contentPanel = new JPanel(null);
      this.contentPanel.setBackground(ScreenProperties.getColor("White"));
      this.contentPanel.setBounds(30, 110, 620, 180);
      this.err = new TextToRows(this.error.getErrorById(this.errId), 58);
      this.errLabel = new JLabel[this.err.getRowCount()];

      for (int i = 0; i < this.err.getRowCount(); i++) {
         this.errLabel[i] = new JLabel(this.err.getRow(i));
         this.errLabel[i].setFont(ScreenProperties.getFont("TimeOutScreen"));
         this.errLabel[i].setHorizontalAlignment(2);
         this.errLabel[i].setBounds(0, i * 30, 600, 25);
         this.contentPanel.add(this.errLabel[i]);
      }
   }

   private void createButtonPanel() {
      this.okay = new JButton(Aem.getString(4003));
      this.okay.setBackground(ScreenProperties.getColor("White"));
      this.okay.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
      this.okay.setFont(ScreenProperties.getFont("TopBarButton"));
      this.okay.setForeground(ScreenProperties.getColor("Black"));
      this.okay.setHorizontalTextPosition(2);
      this.okay.setVerticalTextPosition(0);
      this.okay.setBounds(150, 10, 100, 60);
      this.okay.setFocusPainted(false);
      this.okay.setActionCommand("Okay");
      this.okay.addActionListener(this.errorAction);
      this.buttonPanel = new JPanel(null);
      this.buttonPanel.setBackground(ScreenProperties.getColor("White"));
      this.buttonPanel.add(this.okay);
      this.buttonPanel.setBounds(380, 370, 260, 80);
   }

   private void createBorder() {
      this.outterBorder = new JPanel(null);
      this.outterBorder.setBorder(new LineBorder(ScreenProperties.getColor("Gray"), 5));
      this.outterBorder.setBounds(180, 80, 664, 470);
      this.outterBorder.setBackground(ScreenProperties.getColor("White"));
      this.outterBorder.setLayout(null);
   }

   private void showFranchiseeInfo() {
      if (this.franchiseeInfoPanel == null) {
         this.franchiseeInfoPanel = new JPanel(null);
         this.franchiseeInfoPanel.setBackground(ScreenProperties.getColor("White"));
         this.text3 = new JLabel();
         this.text3.setForeground(ScreenProperties.getColor("Red"));
         this.text3.setBackground(ScreenProperties.getColor("White"));
         this.text3.setFont(ScreenProperties.getFont("TimeOutScreen"));
         this.text3.setBounds(0, 0, 500, 25);
         this.franchiseeInfoPanel.add(this.text3);
         this.text4 = new JLabel();
         this.text4.setForeground(ScreenProperties.getColor("Black"));
         this.text4.setBackground(ScreenProperties.getColor("White"));
         this.text4.setFont(ScreenProperties.getFont("TimeOutScreen"));
         this.text4.setBounds(0, 20, 500, 25);
         this.franchiseeInfoPanel.add(this.text4);
         this.text5 = new JLabel();
         this.text5.setForeground(ScreenProperties.getColor("Black"));
         this.text5.setBackground(ScreenProperties.getColor("White"));
         this.text5.setFont(ScreenProperties.getFont("TimeOutScreen"));
         this.text5.setBounds(0, 40, 500, 25);
         this.franchiseeInfoPanel.add(this.text5);
         this.text6 = new JLabel();
         this.text6.setForeground(ScreenProperties.getColor("Black"));
         this.text6.setBackground(ScreenProperties.getColor("White"));
         this.text6.setFont(ScreenProperties.getFont("TimeOutScreen"));
         this.text6.setBounds(0, 60, 500, 25);
         this.franchiseeInfoPanel.add(this.text6);
         this.text7 = new JLabel();
         this.text7.setForeground(ScreenProperties.getColor("Black"));
         this.text7.setBackground(ScreenProperties.getColor("White"));
         this.text7.setFont(ScreenProperties.getFont("TimeOutScreen"));
         this.text7.setBounds(0, 80, 500, 25);
         this.franchiseeInfoPanel.add(this.text7);
         this.text8 = new JLabel();
         this.text8.setForeground(ScreenProperties.getColor("Black"));
         this.text8.setBackground(ScreenProperties.getColor("White"));
         this.text8.setFont(ScreenProperties.getFont("TimeOutScreen"));
         this.text8.setBounds(0, 100, 500, 25);
         this.franchiseeInfoPanel.add(this.text8);
         this.franchiseeInfoPanel.setBounds(30, 300, 500, 125);
         this.franchiseeInfoPanel.setVisible(false);
      }

      this.text3.setText(Aem.getString(2012));
      this.text4.setText(Aem.getString(2013));
      if (Aem.getString(2014).trim().equals("")) {
         this.text5.setVisible(false);
         this.text6.setBounds(0, 40, 500, 25);
         this.text7.setBounds(0, 60, 500, 25);
         this.text8.setBounds(0, 80, 500, 25);
      } else {
         this.text5.setText(Aem.getString(2014));
         this.text6.setBounds(0, 60, 500, 25);
         this.text7.setBounds(0, 80, 500, 25);
         this.text8.setBounds(0, 120, 500, 25);
      }

      this.text6.setText(Aem.getString(2015));
      this.text7.setText(Aem.getString(2016));
      this.text8.setText(Aem.getString(2017));
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

   public class ErrorAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("ErrorScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(ErrorScreen.mainAction, ErrorScreen.this.ownTimer)) {
                  ErrorScreen.this.ownTimer.stop();
                  if (!Aem.needToReinitialize()
                     || !ErrorScreen.aemContent.getPrevScreen().equals("ReturnThankYouScreen")
                        && !ErrorScreen.aemContent.getPrevScreen().equals("AboutCompanyScreen")) {
                     if (this.cmd[0].equals("Okay")) {
                        ErrorScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, ErrorScreen.nextScreen + " " + ErrorScreen.passingData));
                     }
                  } else {
                     ErrorScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                  }
               }
            }
         } catch (Exception e) {
            Log.warning(e, "ErrorScreen");
         } catch (Throwable e) {
            Log.error(e, "ErrorScreen");
         }
      }
   }
}
