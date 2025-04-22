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
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class PushingDiscAllTheWayScreen extends AbstractContentBar {
   JLabel opsLabel;
   JLabel title;
   JButton aGif;
   JLabel[] text;
   ImageIcon ii;
   TextToRows ttr;
   ActionListener pushingDiscAllTheWayAction;

   public PushingDiscAllTheWayScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.pushingDiscAllTheWayAction = new PushingDiscAllTheWayScreen.PushingDiscAllTheWayAction();
         this.opsLabel = new JLabel(ScreenProperties.getImage("Error.Icon.Ops"));
         this.opsLabel.setBackground(ScreenProperties.getColor("White"));
         this.opsLabel.setBounds(100, 230, 50, 50);
         this.title = new JLabel(Aem.getString(4035));
         this.title.setForeground(ScreenProperties.getColor("Red"));
         this.title.setBackground(ScreenProperties.getColor("White"));
         this.title.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.title.setBounds(160, 240, 500, 35);
         this.add(this.title);
         this.ttr = new TextToRows(Aem.getString(4038), 30);
         this.text = new JLabel[this.ttr.getRowCount()];

         for (int i = 0; i < this.ttr.getRowCount(); i++) {
            this.text[i] = new JLabel(this.ttr.getRow(i));
            this.text[i].setForeground(ScreenProperties.getColor("Black"));
            this.text[i].setBackground(ScreenProperties.getColor("White"));
            this.text[i].setFont(ScreenProperties.getFont("ReturnMovieTitle"));
            this.text[i].setBounds(160, 300 + i * 30, 500, 25);
            this.add(this.text[i]);
         }

         this.ii = ScreenProperties.getImage("UnableToRecognizeMovie.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(580, 230, 241, 291);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setPressedIcon(this.ii);
         this.aGif.setBorder(null);
         this.add(this.opsLabel);
         this.add(this.aGif);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar(Aem.getString(4035), "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("PushingDiscAllTheWayScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PushingDiscAllTheWayScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.title.setText(Aem.getString(4035));

         for (int i = 0; i < this.ttr.getRowCount(); i++) {
            this.remove(this.text[i]);
         }

         this.ttr = new TextToRows(Aem.getString(4038), 30);
         this.text = new JLabel[this.ttr.getRowCount()];

         for (int i = 0; i < this.ttr.getRowCount(); i++) {
            this.text[i] = new JLabel(this.ttr.getRow(i));
            this.text[i].setForeground(ScreenProperties.getColor("Black"));
            this.text[i].setBackground(ScreenProperties.getColor("White"));
            this.text[i].setFont(ScreenProperties.getFont("ReturnMovieTitle"));
            this.text[i].setBounds(160, 300 + i * 30, 500, 25);
            this.add(this.text[i]);
         }

         this.topBar.setProperty(Aem.getString(4035), "", "", "", "", "", "");
         this.msg = new StringBuffer("[").append("PushingDiscAllTheWayScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("PushingDiscAllTheWayScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var6);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class PushingDiscAllTheWayAction extends BaseActionListener {
      private PushingDiscAllTheWayAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, PushingDiscAllTheWayScreen.mainAction);
      }
   }
}
