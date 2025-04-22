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
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;
import net.dvdplay.ui.Util;

public class DiscNotBelongScreen extends AbstractContentBar {
   JLabel opsLabel;
   JLabel title;
   JPanel franchiseInfoPanel;
   JButton aGif;
   int maxRowCount = 8;
   JLabel[] text;
   ImageIcon ii;
   TextToRows ttr;
   ActionListener discNotBelongAction;

   public DiscNotBelongScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.setCurrentLocale(Aem.getLocale());
         this.discNotBelongAction = new DiscNotBelongScreen.DiscNotBelongAction();
         this.opsLabel = new JLabel(ScreenProperties.getImage("Error.Icon.Ops"));
         this.opsLabel.setBackground(ScreenProperties.getColor("White"));
         this.opsLabel.setBounds(100, 180, 50, 50);
         this.title = new JLabel(Aem.getString(4041));
         this.title.setForeground(ScreenProperties.getColor("Red"));
         this.title.setBackground(ScreenProperties.getColor("White"));
         this.title.setFont(ScreenProperties.getFont("ReturnMovieTitle"));
         this.title.setBounds(160, 190, 500, 35);
         this.add(this.title);
         this.ttr = new TextToRows(Aem.getString(4037), 40);
         this.text = new JLabel[this.maxRowCount];

         for (int i = 0; i < this.maxRowCount; i++) {
            this.text[i] = new JLabel(i >= this.ttr.getRowCount() ? "" : this.ttr.getRow(i));
            this.text[i].setForeground(ScreenProperties.getColor("Black"));
            this.text[i].setBackground(ScreenProperties.getColor("White"));
            this.text[i].setFont(ScreenProperties.getFont("ReturnMovieTitle"));
            this.text[i].setBounds(160, 250 + i * 30, 500, 25);
            this.add(this.text[i]);
         }

         this.franchiseInfoPanel = Util.getFranchiseInfoPanel();
         this.franchiseInfoPanel.setBounds(160, 450, 500, 150);
         this.franchiseInfoPanel.setVisible(true);
         this.ii = ScreenProperties.getImage("UnableToRecognizeMovie.Animated.Gif");
         this.aGif = new JButton(this.ii);
         this.ii.setImageObserver(this.aGif);
         this.aGif.setBounds(580, 230, 241, 291);
         this.aGif.setBackground(ScreenProperties.getColor("White"));
         this.aGif.setPressedIcon(this.ii);
         this.aGif.setBorder(null);
         this.add(this.opsLabel);
         this.add(this.aGif);
         this.add(this.franchiseInfoPanel);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar(Aem.getString(4041), "", "", "", "", "", "");
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("DiscNotBelongScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("DiscNotBelongScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.timer.stop();
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (!this.isCurrentLocale()) {
            this.title.setText(Aem.getString(4041));
            this.ttr = new TextToRows(Aem.getString(4037), 30);

            for (int i = 0; i < this.ttr.getRowCount(); i++) {
               this.text[i].setText(this.ttr.getRow(i));
            }

            Util.updateFranchiseInfoPanel(this.franchiseInfoPanel);
            this.topBar.setProperty(Aem.getString(4041), "", "", "", "", "", "");
         }

         this.msg = new StringBuffer("* ").append("DiscNotBelongScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("DiscNotBelongScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class DiscNotBelongAction extends BaseActionListener {
      private DiscNotBelongAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, DiscNotBelongScreen.mainAction);
      }
   }
}
