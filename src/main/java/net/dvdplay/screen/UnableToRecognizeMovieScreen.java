package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;
import net.dvdplay.ui.Util;

public class UnableToRecognizeMovieScreen extends AbstractContentBar {
   ActionListener unableToRecognizeMovieAction;
   JLabel opsLabel;
   JLabel text1;
   JLabel text2;
   JLabel text3;
   JLabel text4;
   JLabel text5;
   JLabel text6;
   JLabel text7;
   JLabel text8;
   JButton aGif;
   JPanel franchiseInfoPanel;
   JLabel[] text;
   ImageIcon ops;
   ImageIcon ii;
   int locationY = 0;
   TextToRows ttr;

   public UnableToRecognizeMovieScreen(String prevScreen, String currScreen, String data) {
      try {
         this.unableToRecognizeMovieAction = new UnableToRecognizeMovieScreen.UnableToRecognizeMovieAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.opsLabel = new JLabel(ScreenProperties.getImage("Error.Icon.Ops"));
         this.opsLabel.setBackground(ScreenProperties.getColor("White"));
         this.opsLabel.setBounds(100, 150, 50, 50);
         this.locationY = 160;
         this.text1 = new JLabel(Aem.getString(4023));
         this.text1.setForeground(ScreenProperties.getColor("Red"));
         this.text1.setBackground(ScreenProperties.getColor("White"));
         this.text1.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text1.setBounds(160, 160, 500, 35);
         this.text2 = new JLabel(Aem.getString(4024));
         this.text2.setForeground(ScreenProperties.getColor("Red"));
         this.text2.setBackground(ScreenProperties.getColor("White"));
         this.text2.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.text2.setBounds(100, 220, 500, 35);
         this.ttr = new TextToRows(Aem.getString(4025), 40);
         this.locationY = (630 - this.ttr.getRowCount() * 20) / 2;
         this.text = new JLabel[this.ttr.getRowCount()];

         for (int i = 0; i < this.ttr.getRowCount(); i++) {
            this.text[i] = new JLabel(this.ttr.getRow(i));
            this.text[i].setForeground(ScreenProperties.getColor("Black"));
            this.text[i].setBackground(ScreenProperties.getColor("White"));
            this.text[i].setFont(ScreenProperties.getFont("ReturnMovieTitle"));
            this.locationY += 25;
            this.text[i].setBounds(100, this.locationY, 500, 25);
            this.add(this.text[i]);
         }

         this.locationY += 40;
         this.franchiseInfoPanel = Util.getFranchiseInfoPanel();
         this.franchiseInfoPanel.setBounds(100, this.locationY, 500, 150);
         this.franchiseInfoPanel.setVisible(true);
         this.ii = ScreenProperties.getImage("UnableToRecognizeMovie.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(580, 230, 241, 291);
         this.aGif.setBorder(null);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setPressedIcon(this.ii);
         this.add(this.opsLabel);
         this.add(this.text1);
         this.add(this.text2);
         this.add(this.franchiseInfoPanel);
         this.add(this.aGif);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar(Aem.getString(5009), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.topBar.addActionListener(this.unableToRecognizeMovieAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("UnableToRecognizeMovieScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("UnableToRecognizeMovieScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.text1.setText(Aem.getString(4023));
         this.text2.setText(Aem.getString(4024));
         this.ttr = new TextToRows(Aem.getString(4025), 40);

         for (int i = 0; i < this.ttr.getRowCount(); i++) {
            this.text[i].setText(this.ttr.getRow(i));
         }

         Util.updateFranchiseInfoPanel(this.franchiseInfoPanel);
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", "", "");
         this.topBar.setProperty(Aem.getString(5009), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.msg = new StringBuffer("* ").append("UnableToRecognizeMovieScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("UnableToRecognizeMovieScreen").append("]").append(var5.toString());
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

   public class UnableToRecognizeMovieAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("UnableToRecognizeMovieScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(UnableToRecognizeMovieScreen.mainAction)) {
                  if (this.cmd[0].equals("Help")) {
                     UnableToRecognizeMovieScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  } else if (this.cmd[0].equals("StartOver")) {
                     UnableToRecognizeMovieScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "UnableToRecognizeMovieScreen");
         } catch (Throwable var5) {
            Log.error(var5, "UnableToRecognizeMovieScreen");
         }
      }
   }
}
