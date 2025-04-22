package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ScreenProperties;

public class UpdatingScreen extends AbstractContentBar {
   JLabel text1;
   JLabel text2;
   JLabel aGif;
   ImageIcon ii;
   ActionListener updatingAction;

   public UpdatingScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setCurrentLocale(Aem.getLocale());
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.updatingAction = new UpdatingScreen.UpdatingAction();
         this.createContent();
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar("", "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.centerBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("UpdatingScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("UpdatingScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         if (this.isCurrentLocale()) {
            this.text1.setText("Updating");
            this.text2.setText("Please wait ...");
         }

         this.msg = new StringBuffer("* ").append("UpdatingScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("UpdatingScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   private void createContent() {
      this.text1 = new JLabel(Aem.getString(1021));
      this.text1.setForeground(ScreenProperties.getColor("Red"));
      this.text1.setBackground(ScreenProperties.getColor("White"));
      this.text1.setFont(ScreenProperties.getFont("UpdatingScreenHeader"));
      this.text1.setHorizontalAlignment(0);
      this.text1.setBounds(0, 300, 1024, 30);
      this.text2 = new JLabel(Aem.getString(1022));
      this.text2.setForeground(ScreenProperties.getColor("Black"));
      this.text2.setBackground(ScreenProperties.getColor("White"));
      this.text2.setFont(ScreenProperties.getFont("UpdatingScreenMessage"));
      this.text2.setHorizontalAlignment(0);
      this.text2.setBounds(0, 340, 1024, 30);
      this.ii = ScreenProperties.getImage("ProgressBar.Animated.Gif");
      this.aGif = new JLabel(this.ii);
      this.ii.setImageObserver(this.aGif);
      this.aGif.setBounds(0, 245, 1024, 36);
      this.aGif.setBackground(ScreenProperties.getColor("White"));
      this.aGif.setOpaque(true);
      this.aGif.setBorder(null);
      this.aGif.setHorizontalAlignment(0);
      this.centerBar = new JPanel(null);
      this.centerBar.setBackground(ScreenProperties.getColor("White"));
      this.centerBar.setBounds(0, 60, 1024, 630);
      this.centerBar.add(this.text1);
      this.centerBar.add(this.text2);
      this.centerBar.add(this.aGif);
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class UpdatingAction extends BaseActionListener {
      private UpdatingAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, UpdatingScreen.mainAction);
      }
   }
}
