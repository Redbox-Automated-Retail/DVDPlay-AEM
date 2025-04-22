package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class PromoCodeDescriptionScreen extends AbstractContentBar {
   ActionListener promoCodeDescriptionAction;
   JPanel panel1;
   JPanel panel2;
   JPanel panel3;
   JLabel label1_h;
   JLabel label2_h;
   JLabel label3_h;
   JLabel[] label1_1;
   JLabel[] label1_2;
   JLabel[] label1_3;
   JLabel[] label2;
   JLabel[] label3;
   String passingData = "";
   ImageIcon ii;
   JLabel promoCode;
   TextToRows ttr1;
   TextToRows ttr2;

   public PromoCodeDescriptionScreen(String prevScreen, String currScreen, String data) {
      try {
         this.promoCodeDescriptionAction = new PromoCodeDescriptionScreen.PromoCodeDescriptionAction();
         this.passingData = data;
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.createPanel1();
         this.createPanel2();
         this.createPanel3();
         this.panel1.setBounds(35, 80, 980, 80);
         this.panel2.setBounds(50, 140, 980, 100);
         this.panel3.setBounds(50, 240, 980, 100);
         this.createBlackBottomBar(true, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.createTopBar(Aem.getString(7004), "", "", "", "", "", "");
         this.bottomBar.addActionListener(this.promoCodeDescriptionAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.panel1);
         this.add(this.panel2);
         this.add(this.panel3);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("PromoCodeDescriptionScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PromoCodeDescriptionScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.createPanel2();
         this.createPanel3();
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, true, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.topBar.setProperty(Aem.getString(7004), "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("PromoCodeDescriptionScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PromoCodeDescriptionScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void createPanel1() {
      this.panel1 = new JPanel(null);
      this.panel1.setBackground(ScreenProperties.getColor("White"));
      this.ii = ScreenProperties.getImage("Enter.Icon.PromoCode");
      this.promoCode = new JLabel(this.ii);
      this.ii.setImageObserver(this.promoCode);
      this.promoCode.setBounds(0, 0, ScreenProperties.getInt("PromoCode.Logo.Width") - 17, ScreenProperties.getInt("PromoCode.Logo.Height") + 10);
      this.promoCode.setBackground(ScreenProperties.getColor("White"));
      this.promoCode.setOpaque(true);
      this.promoCode.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
      this.panel1.add(this.promoCode);
      JPanel tt = this.createBorder(
         ScreenProperties.getColor("Red"), ScreenProperties.getInt("PromoCode.Logo.Width") - 80, ScreenProperties.getInt("PromoCode.Logo.Height") - 17, 830, 2
      );
      this.panel1.add(tt);
   }

   public void createPanel2() {
      this.ttr1 = new TextToRows(Aem.getString(7005), 100);
      if (this.panel2 == null) {
         this.panel2 = new JPanel(null);
         this.panel2.setBackground(ScreenProperties.getColor("White"));
      } else {
         this.panel2.removeAll();
      }

      this.label2 = new JLabel[this.ttr1.getRowCount()];

      for (int i = 0; i < this.ttr1.getRowCount(); i++) {
         this.label2[i] = new JLabel(this.ttr1.getRow(i));
         this.label2[i].setForeground(ScreenProperties.getColor("Black"));
         this.label2[i].setBackground(ScreenProperties.getColor("White"));
         this.label2[i].setFont(ScreenProperties.getFont("PromoCodeDescriptionScreen"));
         this.label2[i].setBounds(0, i * 30 + 40, 950, 30);
         this.panel2.add(this.label2[i]);
      }
   }

   public void createPanel3() {
      this.ttr2 = new TextToRows(Aem.getString(7006), 100);
      if (this.panel3 == null) {
         this.panel3 = new JPanel(null);
         this.panel3.setBackground(ScreenProperties.getColor("White"));
      } else {
         this.panel3.removeAll();
      }

      this.label3 = new JLabel[this.ttr2.getRowCount()];

      for (int i = 0; i < this.ttr2.getRowCount(); i++) {
         this.label3[i] = new JLabel(this.ttr2.getRow(i));
         this.label3[i].setForeground(ScreenProperties.getColor("Black"));
         this.label3[i].setBackground(ScreenProperties.getColor("White"));
         this.label3[i].setFont(ScreenProperties.getFont("PromoCodeDescriptionScreen"));
         this.label3[i].setBounds(0, i * 30 + 40, 950, 30);
         this.panel3.add(this.label3[i]);
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

   public class PromoCodeDescriptionAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("PromoCodeDescriptionScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(PromoCodeDescriptionScreen.mainAction) && this.cmd[0].equals("Continue")) {
                  PromoCodeDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "PromoCodeDescriptionScreen");
         } catch (Throwable var5) {
            Log.error(var5, "PromoCodeDescriptionScreen");
         }
      }
   }
}
