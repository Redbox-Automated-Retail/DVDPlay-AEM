package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ScreenProperties;

public class InitializingAEMScreen extends AbstractContentBar {
   ActionListener initializingAEMAction;
   JLabel text1;
   JLabel text2;
   JLabel aGif;
   ImageIcon ii;

   public InitializingAEMScreen(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.initializingAEMAction = new InitializingAEMScreen.InitializingAEMAction();
         this.text1 = new JLabel(Aem.getString(1017));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("RemoveDVDText"));
         this.text1.setHorizontalAlignment(0);
         this.text1.setBounds(0, 360, 1024, 30);
         this.text2 = new JLabel(Aem.getString(1018));
         this.text2.setForeground(ScreenProperties.getColor("Red"));
         this.text2.setBackground(ScreenProperties.getColor("White"));
         this.text2.setFont(ScreenProperties.getFont("RemoveDVDText"));
         this.text2.setHorizontalAlignment(0);
         this.text2.setBounds(0, 390, 1024, 30);
         this.ii = ScreenProperties.getImage("ProgressBar.Animated.Gif");
         this.aGif = new JLabel(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(0, 310, 1024, 36);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setOpaque(true);
         this.aGif.setBorder(null);
         this.aGif.setHorizontalAlignment(0);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar("", "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.text1);
         this.add(this.text2);
         this.add(this.aGif);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("InitializingAEMScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("InitializingAEMScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         this.text1.setText(Aem.getString(1017));
         this.text2.setText(Aem.getString(1018));
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", "", "");
         this.topBar.setProperty("", "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("InitializingAEMScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("InitializingAEMScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class InitializingAEMAction extends BaseActionListener {
      private InitializingAEMAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, InitializingAEMScreen.mainAction);
      }
   }
}
