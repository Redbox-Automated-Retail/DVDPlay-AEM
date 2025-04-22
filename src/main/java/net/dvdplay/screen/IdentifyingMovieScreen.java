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

public class IdentifyingMovieScreen extends AbstractContentBar {
   JLabel text1;
   JLabel text2;
   JButton aGif;
   JLabel adGif;
   JLabel advertisement;
   ImageIcon icon;
   ImageIcon ii;
   String ad;
   File f;
   Image tempIcon;
   Image newImage;
   PlayListItem lPlayListItem;
   ActionListener identifyingMovieAction;

   public IdentifyingMovieScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.identifyingMovieAction = new IdentifyingMovieScreen.IdentifyingMovieAction();
         this.text1 = new JLabel(Aem.getString(6701));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text1.setHorizontalAlignment(2);
         this.text1.setBounds(560, 400, 500, 30);
         this.text2 = new JLabel(Aem.getString(6702));
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
         this.createBlackBottomBar(true, false, "", "", "", "", "", "");
         this.createTopBar(Aem.getString(5003), "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.text1);
         this.add(this.text2);
         this.add(this.advertisement);
         this.add(this.aGif);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("IdentifyingMovieScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("IdentifyingMovieScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.text1.setText(Aem.getString(6701));
         this.text2.setText(Aem.getString(6702));
         this.lPlayListItem = Aem.getNextStaticPlayListItem();
         if (this.lPlayListItem == null) {
            this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
         } else {
            this.ad = this.lPlayListItem.getFilePath();
            this.f = new File(this.ad);
            if (this.f.exists()) {
               Aem.logSummaryMessage("[ADVERTISEMENT] " + this.ad);
               this.icon = new ImageIcon(this.ad);
            } else {
               Aem.logSummaryMessage("[ADVERTISEMENT] " + this.ad + " not found, use default image");
               this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
            }
         }

         this.tempIcon = this.icon.getImage();
         this.newImage = this.tempIcon.getScaledInstance(380, 510, 0);
         this.advertisement.setIcon(new ImageIcon(this.newImage));
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, true, false, "", "", "", "", "", "");
         this.topBar.setProperty(Aem.getString(5003), "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("IdentifyingMovieScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("IdentifyingMovieScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class IdentifyingMovieAction extends BaseActionListener {
      private IdentifyingMovieAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, IdentifyingMovieScreen.mainAction);
      }
   }
}
