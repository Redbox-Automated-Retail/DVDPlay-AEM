package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;

public class RemoveDVDScreen extends AbstractContentBar {
   JLabel text1;
   JLabel text2;
   JLabel text3;
   JLabel text4;
   JLabel text5;
   JButton aGif;
   String movieName;
   String dueTime;
   String dueDate;
   String rentBuy;
   String t6007;
   ImageIcon ii;
   int loc;
   int discId;
   ActionListener removeDVDAction;

   public RemoveDVDScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.setCurrentLocale(Aem.getLocale());
         this.removeDVDAction = new RemoveDVDScreen.RemoveDVDAction();
         this.t6007 = Aem.getString(6007);
         this.text1 = new JLabel(Aem.getString(6001));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("RemoveDVDText"));
         this.text1.setHorizontalAlignment(2);
         this.text1.setBounds(150, 280, 400, 30);
         this.add(this.text1);
         this.text4 = new JLabel(Aem.getString(6005));
         this.text4.setForeground(ScreenProperties.getColor("Black"));
         this.text4.setBackground(ScreenProperties.getColor("White"));
         this.text4.setFont(ScreenProperties.getFont("RemoveDVDText"));
         this.text4.setHorizontalAlignment(2);
         this.text4.setBounds(150, 400, 400, 20);
         this.add(this.text4);
         this.text2 = new JLabel(Aem.getString(6006));
         this.text2.setForeground(ScreenProperties.getColor("Black"));
         this.text2.setBackground(ScreenProperties.getColor("White"));
         this.text2.setFont(ScreenProperties.getFont("RemoveDVDText"));
         this.text2.setBounds(150, 370, 400, 20);
         this.add(this.text2);
         this.text3 = new JLabel();
         this.text3.setForeground(ScreenProperties.getColor("Red"));
         this.text3.setBackground(ScreenProperties.getColor("White"));
         this.text3.setFont(ScreenProperties.getFont("RemoveDVDText"));
         this.text3.setBounds(150, 400, 400, 20);
         this.add(this.text3);
         this.text2.setVisible(false);
         this.text3.setVisible(false);
         this.text5 = new JLabel(Aem.getString(6002));
         this.text5.setForeground(ScreenProperties.getColor("Red"));
         this.text5.setBackground(ScreenProperties.getColor("White"));
         this.text5.setFont(ScreenProperties.getFont("RemoveDVDText"));
         this.text5.setHorizontalAlignment(2);
         this.text5.setBounds(150, 430, 400, 20);
         this.add(this.text5);
         this.ii = ScreenProperties.getImage("RemoveDVD.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(580, 230, 241, 291);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setPressedIcon(this.ii);
         this.add(this.aGif);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar(this.t6007, "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("RemoveDVDScreen").append(" from ").append(prevScreen);
         Log.info(this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("RemoveDVDScreen").append("]").append(var5.toString());
         Log.error(var5, this.msg.toString());
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (!this.isCurrentLocale()) {
            this.text1.setText(Aem.getString(6001));
            this.text5.setText(Aem.getString(6002));
            this.t6007 = Aem.getString(6007);
            this.setCurrentLocale(Aem.getLocale());
         }

         this.discId = Integer.parseInt(data);
         this.rentBuy = AbstractContentBar.aemContent.getRentBuy(this.discId);
         this.movieName = AbstractContentBar.aemContent.getTitle(this.discId);
         this.dueTime = AbstractContentBar.aemContent.getDueTime(this.discId);
         this.dueDate = AbstractContentBar.aemContent.getDueDate(this.discId);
         if (this.rentBuy.compareTo("Rent") == 0) {
            this.text4.setText(Aem.getString(6005));
            this.text4.setVisible(true);
            this.text2.setVisible(false);
            this.text3.setVisible(false);
         }

         if (this.rentBuy.compareTo("Buy") == 0) {
            this.text2.setText(Aem.getString(6006));
            this.text3.setText(this.movieName);
            this.text3.setVisible(true);
            this.text2.setVisible(true);
            this.text4.setVisible(false);
         }

         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", "", "");
         this.topBar.setProperty(this.t6007, "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("RemoveDVDScreen").append(" from ").append(prevScreen);
         Log.info(this.msg.toString());
         AbstractContentBar.timer.stop();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("RemoveDVDScreen").append("]").append(var5.toString());
         Log.error(var5, this.msg.toString());
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class RemoveDVDAction extends BaseActionListener {
      private RemoveDVDAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, RemoveDVDScreen.mainAction);
      }
   }
}
