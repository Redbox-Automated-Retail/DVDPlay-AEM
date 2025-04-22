package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.view.KeyboardAssembler;
import net.dvdplay.view.Utility;

public class EmailScreen extends AbstractContentBar {
   JTextField email;
   JLabel privacy;
   JLabel row1;
   JLabel row2;
   JPanel outterBox;
   KeyboardAssembler ka;
   JButton clear;
   JButton backspace;
   ActionListener keyboardAction;
   ActionListener emailAction;

   public EmailScreen(String prevScreen, String currScreen, String data) {
      try {
         this.emailAction = new EmailScreen.EmailAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.row1 = Utility.createLabel(
            Aem.getString(5702),
            85,
            120,
            900,
            20,
            ScreenProperties.getColor("White"),
            ScreenProperties.getColor("Black"),
            ScreenProperties.getFont("AddEmailLabel")
         );
         this.row2 = Utility.createLabel(
            Aem.getString(5703),
            85,
            145,
            900,
            20,
            ScreenProperties.getColor("White"),
            ScreenProperties.getColor("Black"),
            ScreenProperties.getFont("AddEmailLabel")
         );
         this.privacy = Utility.createLabel(
            Aem.getString(5704),
            85,
            600,
            900,
            20,
            ScreenProperties.getColor("White"),
            ScreenProperties.getColor("Black"),
            ScreenProperties.getFont("AddEmailPrivacy")
         );
         this.outterBox = new JPanel();
         this.outterBox.setBackground(ScreenProperties.getColor("Gray"));
         this.outterBox.setBounds(80, 180, 880, 385);
         this.outterBox.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
         this.outterBox.setLayout(null);
         this.email = new JTextField();
         this.email.setBounds(13, 8, 490, 70);
         this.email.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
         this.email.setFont(ScreenProperties.getFont("CustomerKeyboardEntry"));
         this.clear = this.createButton(Aem.getString(6501), 513, 8, 140, 70);
         this.clear.addActionListener(new EmailScreen.ActionKeyboard());
         this.clear.setActionCommand("Clear");
         this.backspace = this.createButton(Aem.getString(6502), 663, 8, 205, 70);
         this.backspace.addActionListener(new EmailScreen.ActionKeyboard());
         this.backspace.setActionCommand("Backspace");
         this.ka = new KeyboardAssembler("Customer", "", 85, 70);
         this.ka.setBounds(13, 83, 860, 300);
         this.ka.addActionListener(new EmailScreen.ActionKeyboard());
         this.outterBox.add(this.ka);
         this.ka.setVisible(true);
         this.outterBox.add(this.ka);
         this.outterBox.add(this.email);
         this.outterBox.add(this.clear);
         this.outterBox.add(this.backspace);
         this.add(this.row1);
         this.add(this.row2);
         this.add(this.outterBox);
         this.add(this.privacy);
         this.createBlackBottomBar(false, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.createTopBar(Aem.getString(6503), "", "", "", "", "", "");
         this.bottomBar.addActionListener(this.emailAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("EmailScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("EmailScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.email.setText(AbstractContentBar.aemContent.getIntermediateEmailAddress());
         this.row1.setText(Aem.getString(5702));
         this.row2.setText(Aem.getString(5703));
         this.privacy.setText(Aem.getString(5704));
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.topBar.setProperty(Aem.getString(6503), "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("EmailScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("EmailScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   private JButton createButton(String str, int x, int y, int width, int height) {
      JButton temp = new JButton(str);
      temp.setBounds(x, y, width, height);
      temp.setBackground(ScreenProperties.getColor("White"));
      temp.setFont(ScreenProperties.getFont("CustomerKeyboard"));
      temp.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
      return temp;
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

   private class ActionKeyboard implements ActionListener {
      private ActionKeyboard() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            EmailScreen.timer.stop();
            EmailScreen.timer.start();
            Aem.logDetailMessage(Level.INFO, "[EmailScreen] Customer Pressed [" + ae.getActionCommand() + "]");
            if (Objects.equals(ae.getActionCommand(), "Clear")) {
               EmailScreen.this.email.setText("");
            } else if (Objects.equals(ae.getActionCommand(), "Backspace")) {
               if (!EmailScreen.this.email.getText().isEmpty()) {
                  try {
                     EmailScreen.this.email.setText(EmailScreen.this.email.getText(0, EmailScreen.this.email.getText().length() - 1));
                  } catch (BadLocationException var4) {
                     EmailScreen.this.msg = new StringBuffer("[").append("EmailScreen").append("]").append(var4.toString());
                     Aem.logDetailMessage(DvdplayLevel.WARNING, EmailScreen.this.msg.toString(), var4);
                  }
               }
            } else if (EmailScreen.this.email.getText().length() <= 80) {
               EmailScreen.this.email.setText(EmailScreen.this.email.getText() + ae.getActionCommand());
            }

            EmailScreen.aemContent.setIntermediateEmailAddress(EmailScreen.this.email.getText());
         } catch (Exception var5) {
            Log.warning(var5, "EmailScreen");
         } catch (Throwable var6) {
            System.err.println("EmailScreen" + var6.getMessage());
            var6.printStackTrace(System.err);
         }
      }
   }

   private class EmailAction extends BaseActionListener {
      private EmailAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("EmailScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(EmailScreen.mainAction) && this.cmd[0].equals("Continue")) {
                  EmailScreen.aemContent.setEmailAddress(EmailScreen.this.email.getText());
                  EmailScreen.this.msg = new StringBuffer("EMAIL : ").append("[").append(EmailScreen.this.email.getText()).append("]");
                  Aem.logSummaryMessage(EmailScreen.this.msg.toString());
                  EmailScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "DeliveringDVDScreen 0"));
               }
            }
         } catch (Exception e) {
            Log.warning(e, "EmailScreen");
         } catch (Throwable e) {
            Log.error(e, "EmailScreen");
         }
      }
   }
}
