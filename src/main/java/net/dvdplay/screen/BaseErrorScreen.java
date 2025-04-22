package net.dvdplay.screen;

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

public abstract class BaseErrorScreen extends AbstractContentBar {
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
            AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, BaseErrorScreen.nextScreen + DvdplayBase.SPACE_STRING + BaseErrorScreen.passingData));
            ownTimer.stop();
            Aem.logDetailMessage(DvdplayLevel.FINE, "ErrorScreen timeout");
         } catch (Exception e) {
            Log.warning(e, DvdplayBase.ERROR_SCREEN);
         } catch (Throwable t) {
            System.err.println(DvdplayBase.ERROR_SCREEN + t.getMessage());
            t.printStackTrace(System.err);
         }
      }
   });


   public void createTitlePanel() {
      JLabel ops = new JLabel(ScreenProperties.getImage("Error.Icon.Ops"));
      ops.setBounds(5, 5, 50, 50);
      ops.setBackground(ScreenProperties.getColor("White"));
      ops.setForeground(ScreenProperties.getColor("White"));
      ops.setOpaque(true);
      ops.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
      this.titlePanel = new JPanel();
      this.titlePanel.setBounds(5, 5, 150, 55);
      this.titlePanel.setBackground(ScreenProperties.getColor("White"));
      this.titlePanel.add(ops);
   }

   public void createContentPanel(String contentString) {
      this.contentPanel = new JPanel(null);
      this.contentPanel.setBackground(ScreenProperties.getColor("White"));
      this.contentPanel.setBounds(30, 110, 620, 200);
      this.err = new TextToRows("", 60);
   }

   public void createButtonPanel() {
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

   public void createBorder() {
      this.outterBorder = new JPanel(null);
      this.outterBorder.setBorder(new LineBorder(ScreenProperties.getColor("Gray"), 5));
      this.outterBorder.setBounds(180, 80, 664, 470);
      this.outterBorder.setBackground(ScreenProperties.getColor("White"));
      this.outterBorder.setLayout(null);
   }

   public void showFranchiseeInfo() {
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
      AbstractContentBar.mainAction = l;
   }

   public class ErrorAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("ErrorScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               BaseErrorScreen.this.ownTimer.stop();
               if (this.cmd[0].equals("Okay")) {
                  BaseErrorScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, BaseErrorScreen.nextScreen + " " + BaseErrorScreen.passingData));
               }
            }
         } catch (Exception e) {
            Log.warning(e, "ErrorScreen");
         } catch (Throwable e) {
            System.err.println("ErrorScreen" + e.getMessage());
            e.printStackTrace(System.err);
         }
      }
   }
}
