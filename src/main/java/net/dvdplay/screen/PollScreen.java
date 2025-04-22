package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.logger.Log;
import net.dvdplay.poll.PollAnswer;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;
import net.dvdplay.view.BottomBarModel;
import net.dvdplay.view.TopBarModel;

public class PollScreen extends AbstractContentBar {
   ActionListener pollAction;
   String t6101;
   String t6102;
   String t6103;
   String t6104;
   String t6105;
   String passingData;
   JLabel pollHeader;
   JLabel[] pollQuestion;
   JLabel[] pollAnswers;
   JPanel[] pollAnswerPanel;
   JButton[] answerSelect;
   TextToRows ttr;
   boolean lastQuestion = false;
   int maxAnswerNum = 5;
   int numPoll = 0;
   String pollString = " Poll: ";
   String respondString = " Respond: ";
   protected Timer ownTimer = new Timer(15000, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
         try {
            AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, "ReturnThankYouScreen " + passingData));
            ownTimer.stop();
            Log.fine(DvdplayBase.POLL_SCREEN + pollString + AbstractContentBar.aemContent.getCurrentPollNum() + DvdplayBase.SPACE_STRING + DvdplayBase.TIME_OUT);
         } catch (Exception e) {
            Log.warning(e, DvdplayBase.POLL_SCREEN);
         } catch (Throwable t) {
            Log.error(t, DvdplayBase.POLL_SCREEN);
         }
      }
   });


   public PollScreen(String prevScreen, String currScreen, String data) {
      try {
         this.pollAction = new PollScreen.PollAction();
         this.setCurrentLocale(Aem.getLocale());
         this.t6101 = Aem.getString(6101);
         this.t6102 = Aem.getString(6102);
         this.t6103 = Aem.getString(6103);
         this.t6104 = Aem.getString(6104);
         this.t6105 = Aem.getString(6105);
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.createContent();
         this.bottomBar = new BottomBarModel(ScreenProperties.getColor("Black"), true, false, false, this.t6104, "Skip", "", "", "", "");
         this.topBar = new TopBarModel(this.t6101, "", "", "", "", "", "");
         this.bottomBar.addActionListener(this.pollAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.centerBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("PollScreen").append(" from ").append(prevScreen);
         Log.info(this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PollScreen").append("]").append(var5.toString());
         Log.error(var5, this.msg.toString());
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.passingData = data;
         if (!this.isCurrentLocale()) {
            this.setCurrentLocale(Aem.getLocale());
            this.t6101 = Aem.getString(6101);
            this.t6102 = Aem.getString(6102);
            this.t6103 = Aem.getString(6103);
            this.t6104 = Aem.getString(6104);
            this.t6105 = Aem.getString(6105);

            for (int i = 0; i < this.answerSelect.length; i++) {
               this.answerSelect[i].setText(this.t6103);
            }
         }

         if (AbstractContentBar.aemContent.getCurrentPollNum() == 0) {
            Log.debug("+++ First Poll");
            this.lastQuestion = false;
            this.numPoll = Aem.createPollList();
            AbstractContentBar.aemContent.setCurrentPollNum(1);
            this.pollHeader.setText(this.t6102);
         } else {
            Log.debug("+++ 2nd or other Poll");
            AbstractContentBar.aemContent.setCurrentPollNum(AbstractContentBar.aemContent.getCurrentPollNum() + 1);
         }

         if (AbstractContentBar.aemContent.getCurrentPollNum() == this.numPoll) {
            this.lastQuestion = true;
            this.pollHeader.setText(this.t6105);
            Log.debug("+++ Last Poll");
         }

         this.ttr = new TextToRows(Aem.getPollIndexItem(AbstractContentBar.aemContent.getCurrentPollNum() - 1).getPollText(), 70);
         if (this.ttr.getRowCount() > 1) {
            this.pollQuestion[0].setText(this.ttr.getRow(0));
            this.pollQuestion[1].setText(this.ttr.getRow(1));
         } else {
            this.pollQuestion[1].setText(this.ttr.getRow(0));
            this.pollQuestion[0].setText("");
         }

         ArrayList lList = Aem.getPollIndexItem(AbstractContentBar.aemContent.getCurrentPollNum() - 1).getPollAnwsers();

         for (int i = 0; i < lList.size(); i++) {
            this.pollAnswers[i].setText(((PollAnswer)lList.get(i)).getText());
            this.answerSelect[i].setActionCommand("Select " + ((PollAnswer)lList.get(i)).getOrderNum());
            this.pollAnswerPanel[i].setVisible(true);
         }

         for (int i = lList.size(); i < this.maxAnswerNum; i++) {
            this.pollAnswerPanel[i].setVisible(false);
         }

         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, this.t6104, "Skip", "", "", "", "");
         this.topBar.setProperty(this.t6101, "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("PollScreen").append(" from ").append(prevScreen);
         Log.info(this.msg.toString());
         AbstractContentBar.timer.stop();
         this.ownTimer.start();
      } catch (Exception var7) {
         this.msg = new StringBuffer("[").append("PollScreen").append("]").append(var7.toString());
         Log.error(var7, this.msg.toString());
      }
   }

   public void createContent() {
      JPanel[] p = new JPanel[]{new JPanel(null), null, null};
      p[0].setBackground(ScreenProperties.getColor("White"));
      p[0].setBounds(100, 50, 700, 30);
      this.pollHeader = new JLabel(this.t6102);
      this.pollHeader.setFont(ScreenProperties.getFont("PollScreenHeader"));
      this.pollHeader.setForeground(ScreenProperties.getColor("Black"));
      this.pollHeader.setBounds(0, 0, 800, 30);
      p[0].add(this.pollHeader);
      p[1] = new JPanel(null);
      p[1].setBackground(ScreenProperties.getColor("White"));
      p[1].setBounds(100, 130, 900, 100);
      this.pollQuestion = new JLabel[2];

      for (int i = 0; i < this.pollQuestion.length; i++) {
         this.pollQuestion[i] = new JLabel("");
         this.pollQuestion[i].setFont(ScreenProperties.getFont("PollScreenQuestion"));
         this.pollQuestion[i].setForeground(ScreenProperties.getColor("Red"));
         this.pollQuestion[i].setBounds(50, i * 40, 850, 40);
         this.pollQuestion[i].setVisible(true);
         p[1].add(this.pollQuestion[i]);
      }

      JPanel seperatorBar = this.createBorder(ScreenProperties.getColor("Black"), 0, 99, 850, 1);
      p[1].add(seperatorBar);
      p[2] = new JPanel(null);
      p[2].setBackground(ScreenProperties.getColor("White"));
      p[2].setBounds(100, 250, 700, 300);
      this.pollAnswers = new JLabel[5];
      this.pollAnswerPanel = new JPanel[5];
      this.answerSelect = new JButton[5];

      for (int i = 0; i < this.maxAnswerNum; i++) {
         this.pollAnswers[i] = new JLabel("");
         this.pollAnswers[i].setFont(ScreenProperties.getFont("PollScreenAnswer"));
         this.pollAnswers[i].setForeground(ScreenProperties.getColor("Black"));
         this.pollAnswers[i].setBackground(ScreenProperties.getColor("White"));
         this.pollAnswers[i].setHorizontalAlignment(2);
         this.pollAnswers[i].setBounds(150, 0, 550, 45);
         this.answerSelect[i] = new JButton(this.t6103);
         this.answerSelect[i].setBackground(ScreenProperties.getColor("Red"));
         this.answerSelect[i].setForeground(ScreenProperties.getColor("White"));
         this.answerSelect[i].setFont(ScreenProperties.getFont("PollScreenButton"));
         this.answerSelect[i].setBounds(0, 0, 120, 45);
         this.answerSelect[i].addActionListener(this.pollAction);
         this.pollAnswerPanel[i] = new JPanel();
         this.pollAnswerPanel[i].setBackground(ScreenProperties.getColor("White"));
         this.pollAnswerPanel[i].setBounds(50, 60 * i, 700, 60);
         this.pollAnswerPanel[i].add(this.answerSelect[i]);
         this.pollAnswerPanel[i].add(this.pollAnswers[i]);
         this.pollAnswerPanel[i].setVisible(false);
         p[2].add(this.pollAnswerPanel[i]);
      }

      this.centerBar = new JPanel(null);
      this.centerBar.setBackground(ScreenProperties.getColor("White"));
      this.centerBar.setBounds(0, 60, 1024, 630);

      for (int i = 0; i < p.length; i++) {
         this.centerBar.add(p[i]);
      }
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
      this.listenerList
         .add(
            ActionListener.class,
            l
         );
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
   }

   public class PollAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("PollScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(PollScreen.mainAction, PollScreen.this.ownTimer)) {
                  PollScreen.this.ownTimer.stop();
                  if (this.cmd[0].equals("Select")) {
                     Log.info(
                        "PollScreen"
                           + PollScreen.this.pollString
                           + Aem.getPollIndexItem(PollScreen.aemContent.getCurrentPollNum() - 1).getPollID()
                           + PollScreen.this.respondString
                           + Integer.parseInt(this.cmd[1])
                     );
                     Aem.getPollIndexItem(PollScreen.aemContent.getCurrentPollNum() - 1).setPollResponse(Integer.parseInt(this.cmd[1]));
                     if (PollScreen.this.lastQuestion) {
                        PollScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ReturnThankYouScreen " + PollScreen.this.passingData));
                     } else {
                        PollScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "PollScreen " + PollScreen.this.passingData));
                     }
                  } else if (this.cmd[0].equals("Skip")) {
                     PollScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ReturnThankYouScreen " + PollScreen.this.passingData));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "PollScreen");
         } catch (Throwable var5) {
            Log.error(var5, "PollScreen");
         }
      }
   }
}
