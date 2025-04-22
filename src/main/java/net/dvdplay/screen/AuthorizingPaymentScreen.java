package net.dvdplay.screen;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.inventory.PlayListItem;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ScreenProperties;

public class AuthorizingPaymentScreen extends AbstractContentBar {
   JLabel text1;
   JLabel text2;
   JLabel advertisement;
   JButton aGif;
   ImageIcon ii;
   ImageIcon icon;
   String ad;
   Image tempIcon;
   Image newImage;
   File f;
   PlayListItem lPlayListItem;
   ActionListener authorizingPaymentAction;

   public AuthorizingPaymentScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.authorizingPaymentAction = new AuthorizingPaymentScreen.AuthorizingPaymentAction();
         this.text1 = new JLabel(Aem.getString(5502));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text1.setHorizontalAlignment(2);
         this.text1.setBounds(560, 400, 500, 30);
         this.text2 = new JLabel(Aem.getString(5503));
         this.text2.setForeground(ScreenProperties.getColor("Red"));
         this.text2.setBackground(ScreenProperties.getColor("White"));
         this.text2.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text2.setHorizontalAlignment(2);
         this.text2.setBounds(560, 440, 500, 20);
         this.advertisement = new JLabel(new ImageIcon());
         this.advertisement.setBackground(ScreenProperties.getColor("White"));
         this.advertisement.setBorder(new LineBorder(ScreenProperties.getColor("Black"), 3));
         this.advertisement.setBounds(120, 110, 380, 510);
         this.ii = ScreenProperties.getImage("ProgressBar.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(560, 320, 288, 36);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setPressedIcon(this.ii);
         this.aGif.setBorder(null);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar(Aem.getString(5501), "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.text1);
         this.add(this.text2);
         this.add(this.advertisement);
         this.add(this.aGif);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("AuthorizingPaymentScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("AuthorizingPaymentScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.text1.setText(Aem.getString(5502));
         this.text2.setText(Aem.getString(5503));
         this.lPlayListItem = Aem.getNextStaticPlayListItem();
         if (this.lPlayListItem == null) {
            this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
         } else {
            this.ad = this.lPlayListItem.getFilePath();
            this.f = new File(this.ad);
            if (this.f.exists()) {
               this.msg = new StringBuffer("[").append("Advertisement").append("]").append(this.ad);
               Aem.logSummaryMessage(this.msg.toString());
               this.icon = new ImageIcon(this.ad);
            } else {
               this.msg = new StringBuffer("]").append("Advertisement").append("]").append(this.ad).append(" not found, use default image");
               Aem.logSummaryMessage(this.msg.toString());
               this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
            }
         }

         this.tempIcon = this.icon.getImage();
         this.newImage = this.tempIcon.getScaledInstance(380, 510, 0);
         this.advertisement.setIcon(new ImageIcon(this.newImage));
         this.topBar.setProperty(Aem.getString(5501), "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("AuthorizingPaymentScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("AuthorizingPaymentScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class AuthorizingPaymentAction extends BaseActionListener {
      private AuthorizingPaymentAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, AuthorizingPaymentScreen.mainAction);
      }
   }
}
