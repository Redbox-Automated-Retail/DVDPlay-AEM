package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ZipCodeScreen extends AbstractContentBar {
   JTextField zipCode;
   JLabel row1;
   JLabel privacy;
   JPanel outterBox;
   KeyboardAssembler ka;
   JButton clear;
   JButton backspace;
   ActionListener keyboardAction;
   ActionListener zipCodeAction;
   String passingData;
   String zipCodeEntered;

   public ZipCodeScreen(String prevScreen, String currScreen, String data) {
      try {
         this.zipCodeAction = new ZipCodeScreen.ZipCodeAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.row1 = Utility.createLabel(
            Aem.getString(5802),
            85,
            120,
            900,
            20,
            ScreenProperties.getColor("White"),
            ScreenProperties.getColor("Black"),
            ScreenProperties.getFont("AddEmailLabel")
         );
         this.outterBox = new JPanel();
         this.outterBox.setBackground(ScreenProperties.getColor("Gray"));
         this.outterBox.setBounds(80, 180, 880, 385);
         this.outterBox.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
         this.outterBox.setLayout(null);
         this.zipCode = new JTextField();
         this.zipCode.setBounds(13, 8, 490, 70);
         this.zipCode.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
         this.zipCode.setFont(ScreenProperties.getFont("ZipCodeEntry"));
         this.clear = this.createButton(Aem.getString(6501), 513, 8, 140, 70);
         this.clear.setActionCommand("Clear");
         this.clear.addActionListener(new ZipCodeScreen.ActionKeyboard());
         this.backspace = this.createButton(Aem.getString(6502), 663, 8, 205, 70);
         this.backspace.setActionCommand("Backspace");
         this.backspace.addActionListener(new ZipCodeScreen.ActionKeyboard());
         this.ka = new KeyboardAssembler("Customer", "", 85, 70, 2);
         this.ka.setBounds(13, 83, 860, 300);
         this.ka.addActionListener(new ZipCodeScreen.ActionKeyboard());
         this.outterBox.add(this.ka);
         this.ka.setVisible(true);
         this.outterBox.add(this.ka);
         this.outterBox.add(this.zipCode);
         this.outterBox.add(this.clear);
         this.outterBox.add(this.backspace);
         this.add(this.row1);
         this.add(this.outterBox);
         this.createBlackBottomBar(false, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.createTopBar(Aem.getString(5801), "", "", "", "", "", "");
         this.bottomBar.addActionListener(this.zipCodeAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("ZipCodeScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ZipCodeScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.zipCode.setText("  ");
         this.row1.setText(Aem.getString(5802));
         this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", Aem.getString(5701), "Continue");
         this.topBar.setProperty(Aem.getString(5801), "", "", "", "", "", "");
         this.msg = new StringBuffer("* ").append("ZipCodeScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("ZipCodeScreen").append("]").append(var5.toString());
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
            ZipCodeScreen.timer.stop();
            ZipCodeScreen.timer.start();
            ZipCodeScreen.this.msg = new StringBuffer("[")
               .append("ZipCodeScreen")
               .append("]")
               .append(" Customer Pressed [")
               .append(ae.getActionCommand())
               .append("]");
            Aem.logDetailMessage(Level.INFO, ZipCodeScreen.this.msg.toString());
            if (ae.getActionCommand().equals("Clear")) {
               ZipCodeScreen.this.zipCode.setText("  ");
            } else if (ae.getActionCommand().equals("Backspace")) {
               if (ZipCodeScreen.this.zipCode.getText().length() > 2) {
                  try {
                     ZipCodeScreen.this.zipCode.setText(ZipCodeScreen.this.zipCode.getText(0, ZipCodeScreen.this.zipCode.getText().length() - 1));
                  } catch (BadLocationException var4) {
                     ZipCodeScreen.this.msg = new StringBuffer("[").append("ZipCodeScreen").append("]").append(" Exception").append(var4.toString());
                     Aem.logDetailMessage(DvdplayLevel.WARNING, ZipCodeScreen.this.msg.toString());
                  }
               }
            } else if (ZipCodeScreen.this.zipCode.getText().length() <= 22) {
               ZipCodeScreen.this.zipCode.setText(ZipCodeScreen.this.zipCode.getText() + ae.getActionCommand());
            }
         } catch (Exception var5) {
            Log.warning(var5, "ZipCodeScreen");
         } catch (Throwable var6) {
            Log.error(var6, "ZipCodeScreen");
         }
      }
   }

   private class ZipCodeAction extends BaseActionListener {
      private ZipCodeAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("ZipCodeScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(ZipCodeScreen.mainAction)) {
                  ZipCodeScreen.this.zipCodeEntered = ZipCodeScreen.this.zipCode.getText();
                  ZipCodeScreen.this.zipCodeEntered = ZipCodeScreen.this.zipCodeEntered.substring(2);
                  if (this.cmd[0].equals("Continue") && ZipCodeScreen.this.zipCode.getText().length() > 2) {
                     if (ZipCodeScreen.this.zipCodeEntered.length() == 2) {
                        ZipCodeScreen.aemContent.setVerification(ZipCodeScreen.aemContent.getVerificationTypeId(), "");
                     } else {
                        ZipCodeScreen.aemContent.setVerification(ZipCodeScreen.aemContent.getVerificationTypeId(), ZipCodeScreen.this.zipCodeEntered);
                        Aem.logDetailMessage(DvdplayLevel.WARNING, "ZipCode : [" + ZipCodeScreen.this.zipCodeEntered + "]");
                     }

                     ZipCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "AuthorizingPaymentScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "ZipCodeScreen");
         } catch (Throwable var5) {
            Log.error(var5, "ZipCodeScreen");
         }
      }
   }
}
