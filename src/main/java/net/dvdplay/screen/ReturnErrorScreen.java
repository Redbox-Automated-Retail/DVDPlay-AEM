package net.dvdplay.screen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class ReturnErrorScreen extends BaseErrorScreen {
   JLabel[] errLabel;
   int errId;
   static String nextScreen;
   String[] dataSplit;
   ActionListener returnErrorAction;

   public ReturnErrorScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         this.returnErrorAction = new ReturnErrorScreen.ReturnErrorAction();
         this.createBorder();
         this.createTitlePanel();
         this.createContentPanel(Aem.getString(4033));
         this.outterBorder.add(this.titlePanel);
         this.outterBorder.add(this.contentPanel);
         this.centerBar = new JPanel(null);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.centerBar.setBackground(ScreenProperties.getColor("White"));
         this.centerBar.add(this.outterBorder);
         this.createBlackBottomBar(false, false, "", "", "", "", "", "");
         this.createTopBar("", "", "", "", "", "", "");
         this.setBounds(0, 0, 1024, 768);
         this.add(this.centerBar);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.msg = new StringBuffer("* ").append("ReturnErrorScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ReturnErrorScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.err = new TextToRows(Aem.getString(4033), 60);
         this.contentPanel.removeAll();
         this.errLabel = new JLabel[this.err.getRowCount()];

         for (int i = 0; i < this.err.getRowCount(); i++) {
            this.errLabel[i] = new JLabel(this.err.getRow(i));
            this.errLabel[i].setFont(ScreenProperties.getFont("TimeOutScreen"));
            this.errLabel[i].setHorizontalAlignment(2);
            this.errLabel[i].setBounds(0, 5 + i * 30, 530, 25);
            this.contentPanel.add(this.errLabel[i]);
         }

         this.msg = new StringBuffer("* ").append("ReturnErrorScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ReturnErrorScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
   }

   private class ReturnErrorAction extends BaseActionListener {
      private ReturnErrorAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         super.actionPerformed(ae, ReturnErrorScreen.mainAction);
      }
   }
}
