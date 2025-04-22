package net.dvdplay.screen;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class DeliveringDVDScreen extends AbstractContentBar {
   JLabel text1;
   JLabel text2;
   JLabel advertisement;
   JLabel reminder1;
   JLabel reminder2;
   JLabel reminder2_1;
   JLabel reminder3;
   JLabel reminder4;
   JLabel reminder5;
   JPanel seperatorBar;
   JButton aGif;
   ImageIcon ii;
   ImageIcon icon;
   String movieName;
   String dueTime;
   String dueDate;
   String rentBuy;
   String t5901;
   int discId;
   Image tempIcon;
   Image newImage;
   File f;
   Locale currentLocale;
   TextToRows ttr;
   ActionListener deliveringDVDAction;

   public DeliveringDVDScreen(String prevScreen, String currScreen, String data) {
      try {
         this.currentLocale = Aem.getLocale();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.deliveringDVDAction = new DeliveringDVDScreen.DeliveringDVDAction();
         this.createReminder();
         this.text1 = new JLabel(Aem.getString(5902));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("DeliveringDVD"));
         this.text1.setHorizontalAlignment(2);
         this.text1.setBounds(560, 260, 500, 40);
         this.text2 = new JLabel(Aem.getString(5903));
         this.text2.setForeground(ScreenProperties.getColor("Red"));
         this.text2.setBackground(ScreenProperties.getColor("White"));
         this.text2.setFont(ScreenProperties.getFont("DeliveringDVD"));
         this.text2.setHorizontalAlignment(2);
         this.text2.setBounds(560, 300, 500, 40);
         this.seperatorBar = this.createBorder(ScreenProperties.getColor("Red"), 560, 380, 350, 1);
         this.reminder1.setVisible(true);
         this.reminder2.setVisible(true);
         this.reminder3.setVisible(true);
         this.reminder4.setVisible(true);
         this.reminder5.setVisible(true);
         this.advertisement = new JLabel(new ImageIcon());
         this.advertisement.setBackground(ScreenProperties.getColor("White"));
         this.advertisement.setBorder(new LineBorder(ScreenProperties.getColor("Black"), 3));
         this.advertisement.setBounds(120, 110, 380, 510);
         this.ii = ScreenProperties.getImage("ProgressBar.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(560, 180, 288, 36);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setPressedIcon(this.ii);
         this.aGif.setBorder(null);
         this.t5901 = Aem.getString(5901);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar(this.t5901, "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.text1);
         this.add(this.text2);
         this.add(this.reminder1);
         this.add(this.reminder2);
         this.add(this.reminder2_1);
         this.add(this.reminder3);
         this.add(this.reminder4);
         this.add(this.reminder5);
         this.add(this.seperatorBar);
         this.add(this.advertisement);
         this.add(this.aGif);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("DeliveringDVDScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("DeliveringDVDScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         AbstractContentBar.timer.stop();
         if (!this.isCurrentLocale()) {
            this.text1.setText(Aem.getString(5902));
            this.text2.setText(Aem.getString(5903));
            this.t5901 = Aem.getString(5901);
            this.setCurrentLocale(Aem.getLocale());
         }

         if (data.length() == 0) {
            data = "0";
         }

         this.discId = Integer.parseInt(data);
         this.rentBuy = AbstractContentBar.aemContent.getRentBuy(this.discId);
         this.movieName = AbstractContentBar.aemContent.getTitle(this.discId);
         this.dueTime = AbstractContentBar.aemContent.getDueTime(this.discId);
         this.dueDate = AbstractContentBar.aemContent.getDueDate(this.discId);
         if (this.rentBuy.compareTo("Rent") == 0) {
            this.reminder2.setText(this.dueDate);
            this.reminder4.setText(this.dueTime);
            this.reminder1.setText(Aem.getString(6003));
            this.reminder3.setText(Aem.getString(6004));
            this.reminder1.setVisible(true);
            this.reminder2.setVisible(true);
            this.reminder2_1.setVisible(false);
            this.reminder3.setVisible(true);
            this.reminder4.setVisible(true);
            this.reminder5.setVisible(true);
         } else {
            this.reminder1.setText(Aem.getString(6006));
            this.ttr = new TextToRows(this.movieName, 28);
            this.reminder2.setText(this.ttr.getRow(0));
            if (this.ttr.getRowCount() > 1) {
               this.reminder2_1.setText(this.ttr.getRow(1));
               this.reminder2_1.setVisible(true);
            }

            this.reminder2.setVisible(true);
            this.reminder3.setVisible(false);
            this.reminder4.setVisible(false);
            this.reminder5.setVisible(false);
         }

         this.f = new File(AbstractContentBar.aemContent.getPoster(Integer.parseInt(data)));
         if (this.f.exists()) {
            this.icon = new ImageIcon(AbstractContentBar.aemContent.getPoster(Integer.parseInt(data)));
         } else {
            Aem.logDetailMessage(DvdplayLevel.FINE, AbstractContentBar.aemContent.getPoster(Integer.parseInt(data)) + " not found, use default image");
            this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
         }

         this.tempIcon = this.icon.getImage();
         this.newImage = this.tempIcon.getScaledInstance(380, 510, 0);
         this.advertisement.setIcon(new ImageIcon(this.newImage));
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", "", "");
         this.topBar.setProperty(this.t5901, "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("DeliveringDVDScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("DeliveringDVDScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void createReminder() {
      this.reminder1 = new JLabel(Aem.getString(6003));
      this.reminder1.setForeground(ScreenProperties.getColor("Black"));
      this.reminder1.setBackground(ScreenProperties.getColor("White"));
      this.reminder1.setFont(ScreenProperties.getFont("DeliveringDVD"));
      this.reminder1.setBounds(560, 420, 500, 40);
      this.reminder1.setHorizontalAlignment(2);
      this.reminder1.setVisible(false);
      this.reminder2 = new JLabel();
      this.reminder2.setForeground(ScreenProperties.getColor("Red"));
      this.reminder2.setBackground(ScreenProperties.getColor("White"));
      this.reminder2.setFont(ScreenProperties.getFont("DeliveringDVD"));
      this.reminder2.setBounds(560, 460, 420, 40);
      this.reminder2.setHorizontalAlignment(2);
      this.reminder2.setVisible(false);
      this.reminder2_1 = new JLabel();
      this.reminder2_1.setForeground(ScreenProperties.getColor("Red"));
      this.reminder2_1.setBackground(ScreenProperties.getColor("White"));
      this.reminder2_1.setFont(ScreenProperties.getFont("DeliveringDVD"));
      this.reminder2_1.setBounds(560, 500, 420, 40);
      this.reminder2_1.setHorizontalAlignment(2);
      this.reminder2_1.setVisible(false);
      this.reminder3 = new JLabel(Aem.getString(6004));
      this.reminder3.setForeground(ScreenProperties.getColor("Black"));
      this.reminder3.setBackground(ScreenProperties.getColor("White"));
      this.reminder3.setFont(ScreenProperties.getFont("DeliveringDVD"));
      this.reminder3.setBounds(560, 510, 500, 40);
      this.reminder3.setHorizontalAlignment(2);
      this.reminder3.setVisible(false);
      this.reminder4 = new JLabel();
      this.reminder4.setForeground(ScreenProperties.getColor("Red"));
      this.reminder4.setBackground(ScreenProperties.getColor("White"));
      this.reminder4.setFont(ScreenProperties.getFont("DeliveringDVD"));
      this.reminder4.setHorizontalAlignment(2);
      this.reminder4.setBounds(560, 550, 500, 40);
      this.reminder4.setVisible(false);
      this.reminder5 = new JLabel(Aem.getString(6008));
      this.reminder5.setForeground(ScreenProperties.getColor("Red"));
      this.reminder5.setBackground(ScreenProperties.getColor("White"));
      this.reminder5.setFont(ScreenProperties.getFont("RemoveDVDText"));
      this.reminder5.setHorizontalAlignment(2);
      this.reminder5.setBounds(700, 550, 280, 40);
      this.reminder5.setVisible(false);
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class DeliveringDVDAction extends BaseActionListener {
      private DeliveringDVDAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, DeliveringDVDScreen.mainAction);
      }
   }
}
