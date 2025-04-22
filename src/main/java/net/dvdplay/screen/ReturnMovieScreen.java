package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.view.TopBarModel;

public class ReturnMovieScreen extends AbstractContentBar {
   ActionListener returnMovieAction;
   JLabel text1;
   JLabel text2;
   JLabel text3;
   JLabel text4;
   JButton aGif;
   ImageIcon ii;

   public ReturnMovieScreen(String prevScreen, String currScreen, String data) {
      try {
         this.returnMovieAction = new ReturnMovieScreen.ReturnMovieAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.text1 = new JLabel(Aem.getString(6601));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text1.setBounds(150, 320, 400, 30);
         this.text2 = new JLabel(Aem.getString(6602));
         this.text2.setForeground(ScreenProperties.getColor("Black"));
         this.text2.setBackground(ScreenProperties.getColor("White"));
         this.text2.setFont(ScreenProperties.getFont("ReturnMovieText"));
         this.text2.setBounds(150, 360, 400, 20);
         this.text3 = new JLabel(Aem.getString(6603));
         this.text3.setForeground(ScreenProperties.getColor("Black"));
         this.text3.setBackground(ScreenProperties.getColor("White"));
         this.text3.setFont(ScreenProperties.getFont("ReturnMovieText"));
         this.text3.setBounds(150, 390, 400, 20);
         this.ii = ScreenProperties.getImage("ReturnMovie.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(580, 230, 241, 291);
         this.aGif.setBorder(null);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setPressedIcon(this.ii);
         this.add(this.aGif);
         this.add(this.text1);
         this.add(this.text2);
         this.add(this.text3);
         this.createBlackBottomBar(true, false, "", "", "", "", "", "");
         this.topBar = new TopBarModel(Aem.getString(5002), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.topBar.addActionListener(this.returnMovieAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("ReturnMovieScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ReturnMovieScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.text1.setText(Aem.getString(6601));
         this.text2.setText(Aem.getString(6602));
         this.text3.setText(Aem.getString(6603));
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, true, false, "", "", "", "", "", "");
         this.topBar.setProperty(Aem.getString(5002), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
         this.msg = new StringBuffer("* ").append("ReturnMovieScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ReturnMovieScreen").append("]").append(var5.toString());
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

   public class ReturnMovieAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("ReturnMovieScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(ReturnMovieScreen.mainAction)) {
                  if (this.cmd[0].equals("StartOver")) {
                     ReturnMovieScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                  } else if (this.cmd[0].equals("Help")) {
                     ReturnMovieScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "ReturnMovieScreen");
         } catch (Throwable var5) {
            Log.error(var5, "ReturnMovieScreen");
         }
      }
   }
}
