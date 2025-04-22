package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;

public class PaymentCardApprovedScreen extends AbstractContentBar {
   ActionListener paymentCardApprovedAction;
   JPanel centerBar;
   JPanel redLine;
   JLabel text1;
   JLabel text2;
   JLabel text3;
   JLabel text4;
   JLabel emailAddr;
   String t5601;
   String t5602;
   String t5603;
   String t5604;
   String t5605;
   String t5606;
   String t5608;
   static Timer ownTimer = new Timer(5000, new ActionListener() {
      private StringBuffer lMsg;

      public void actionPerformed(ActionEvent evt) {
         try {
            PaymentCardApprovedScreen.ownTimer.stop();
            if (AbstractContentBar.aemContent.getEmail().trim().isEmpty()) {
               AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.EMAIL_SCREEN));
               this.lMsg = new StringBuffer(DvdplayBase.PAYMENT_CARD_APPROVED_SCREEN).append(" going to EmailScreen");
            } else {
               AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, "DeliveringDVDScreen 0"));
               this.lMsg = new StringBuffer(DvdplayBase.PAYMENT_CARD_APPROVED_SCREEN).append(" going to DeliveringDVDScreen");
            }
            Aem.logDetailMessage(DvdplayLevel.FINE, this.lMsg.toString());
         } catch (Exception e) {
            Log.warning(e, DvdplayBase.PAYMENT_CARD_APPROVED_SCREEN);
         } catch (Throwable t) {
            Log.error(t, DvdplayBase.PAYMENT_CARD_APPROVED_SCREEN);
         }
      }
   });


   public PaymentCardApprovedScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.paymentCardApprovedAction = new PaymentCardApprovedScreen.PaymentCardApprovedAction();
         this.centerBar = new JPanel(null);
         this.centerBar.setBackground(ScreenProperties.getColor("White"));
         this.t5601 = Aem.getString(5601);
         this.t5602 = Aem.getString(5602);
         this.t5603 = Aem.getString(5603);
         this.t5604 = Aem.getString(5604);
         this.text1 = new JLabel(this.t5602);
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("PaymentApprovedTitle"));
         this.text1.setHorizontalAlignment(0);
         this.text1.setBounds(0, 170, 1024, 30);
         this.text2 = new JLabel(this.t5603);
         this.text2.setForeground(ScreenProperties.getColor("Black"));
         this.text2.setBackground(ScreenProperties.getColor("White"));
         this.text2.setFont(ScreenProperties.getFont("PaymentApproved"));
         this.text2.setHorizontalAlignment(0);
         this.text2.setBounds(0, 210, 1024, 30);
         this.text3 = new JLabel(this.t5604);
         this.text3.setForeground(ScreenProperties.getColor("Black"));
         this.text3.setBackground(ScreenProperties.getColor("White"));
         this.text3.setFont(ScreenProperties.getFont("PaymentApproved"));
         this.text3.setHorizontalAlignment(0);
         this.text3.setBounds(0, 250, 1024, 30);
         this.text4 = new JLabel();
         this.text4.setForeground(ScreenProperties.getColor("Black"));
         this.text4.setBackground(ScreenProperties.getColor("White"));
         this.text4.setFont(ScreenProperties.getFont("PaymentApproved"));
         this.text4.setHorizontalAlignment(0);
         this.text4.setBounds(0, 380, 1024, 35);
         this.redLine = this.createBorder(ScreenProperties.getColor("Red"), 250, 360, 600, 2);
         this.emailAddr = new JLabel(AbstractContentBar.aemContent.getEmail());
         this.emailAddr.setForeground(ScreenProperties.getColor("Red"));
         this.emailAddr.setBackground(ScreenProperties.getColor("White"));
         this.emailAddr.setFont(ScreenProperties.getFont("PaymentApproved"));
         this.emailAddr.setHorizontalAlignment(0);
         this.emailAddr.setBounds(0, 310, 1024, 35);
         this.centerBar.add(this.text1);
         this.centerBar.add(this.emailAddr);
         this.centerBar.add(this.text3);
         this.centerBar.add(this.text2);
         this.centerBar.add(this.text4);
         this.centerBar.add(this.redLine);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.createBlackBottomBar(false, false, "", "", "", "", Aem.getString(5607), "EditEmail");
         this.createTopBar(this.t5601, "", "", "", "", "", "");
         this.bottomBar.addActionListener(this.paymentCardApprovedAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.centerBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("PaymentCardApprovedScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PaymentCardApprovedScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         AbstractContentBar.timer.stop();
         this.text4.setText(Aem.getString(5605) + " " + AbstractContentBar.aemContent.getPromoValue() + " " + Aem.getString(5606));
         this.text1.setText(Aem.getString(5602));
         this.text3.setText(Aem.getString(5604));
         this.emailAddr.setText(AbstractContentBar.aemContent.getEmail());
         this.text2.setText(Aem.getString(5603) + AbstractContentBar.aemContent.getTotal() + Aem.getString(5608));
         if (AbstractContentBar.aemContent.getEmail().trim().equals("")) {
            this.emailAddr.setVisible(false);
            this.text3.setVisible(false);
         } else {
            this.emailAddr.setVisible(true);
            this.text3.setVisible(true);
         }

         if (AbstractContentBar.aemContent.hasValidPromoCode()) {
            this.text4.setVisible(true);
            this.redLine.setVisible(true);
         } else {
            this.text4.setVisible(false);
            this.redLine.setVisible(false);
         }

         this.topBar.setProperty(Aem.getString(5601), "", "", "", "", "", "");
         if (AbstractContentBar.aemContent.getEmail().trim().equals("")) {
            this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", "", "");
         } else {
            this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", Aem.getString(5607), "EditEmail");
         }

         this.msg = new StringBuffer("* ").append("PaymentCardApprovedScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         ownTimer.stop();
         ownTimer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PaymentCardApprovedScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
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

   public class PaymentCardApprovedAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("PaymentCardApprovedScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(PaymentCardApprovedScreen.mainAction, PaymentCardApprovedScreen.ownTimer)) {
                  PaymentCardApprovedScreen.ownTimer.stop();
                  if (this.cmd[0].equals("EditEmail")) {
                     PaymentCardApprovedScreen.aemContent.setIntermediateEmailAddress(PaymentCardApprovedScreen.aemContent.getEmail());
                     PaymentCardApprovedScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "EmailScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "PaymentCardApprovedScreen");
         } catch (Throwable var5) {
            Log.error(var5, "PaymentCardApprovedScreen");
         }
      }
   }
}
