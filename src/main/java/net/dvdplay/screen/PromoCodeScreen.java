package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.userop.RentOpException;
import net.dvdplay.view.KeyboardAssembler;
import net.dvdplay.view.Utility;

public class PromoCodeScreen extends AbstractContentBar {
   JTextField promoCode;
   JLabel promoCodeLabel;
   JLabel row1;
   JLabel row2;
   JPanel outterBox;
   JPanel panel1;
   KeyboardAssembler ka;
   JButton clear;
   JButton backspace;
   ActionListener keyboardAction;
   ActionListener promoCodeAction;
   ImageIcon ii;
   String promoCodeEntered = "";

   public PromoCodeScreen(String prevScreen, String currScreen, String data) {
      try {
         this.promoCodeAction = new PromoCodeScreen.PromoCodeAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.createPanel1();
         this.row1 = Utility.createLabel(
            Aem.getString(7002),
            85,
            180,
            900,
            20,
            ScreenProperties.getColor("White"),
            ScreenProperties.getColor("Black"),
            ScreenProperties.getFont("AddEmailLabel")
         );
         this.row2 = Utility.createLabel(
            Aem.getString(7003),
            85,
            205,
            900,
            20,
            ScreenProperties.getColor("White"),
            ScreenProperties.getColor("Black"),
            ScreenProperties.getFont("AddEmailLabel")
         );
         this.outterBox = new JPanel();
         this.outterBox.setBackground(ScreenProperties.getColor("Gray"));
         this.outterBox.setBounds(80, 240, 880, 385);
         this.outterBox.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
         this.outterBox.setLayout(null);
         this.promoCode = new JTextField();
         this.promoCode.setText("  ");
         this.promoCode.setBounds(13, 8, 490, 70);
         this.promoCode.setBorder(new LineBorder(ScreenProperties.getColor("Black")));
         this.promoCode.setFont(ScreenProperties.getFont("CustomerKeyboardEntry"));
         this.clear = this.createButton(Aem.getString(6501), 513, 8, 140, 70);
         this.clear.setActionCommand("Clear");
         this.clear.addActionListener(new PromoCodeScreen.ActionKeyboard());
         this.backspace = this.createButton(Aem.getString(6502), 663, 8, 205, 70);
         this.backspace.setActionCommand("Backspace");
         this.backspace.addActionListener(new PromoCodeScreen.ActionKeyboard());
         this.ka = new KeyboardAssembler("Customer", "", 85, 70);
         this.ka.setBounds(13, 83, 860, 300);
         this.ka.addActionListener(new PromoCodeScreen.ActionKeyboard());
         this.outterBox.add(this.ka);
         this.ka.setVisible(true);
         this.outterBox.add(this.ka);
         this.outterBox.add(this.promoCode);
         this.outterBox.add(this.clear);
         this.outterBox.add(this.backspace);
         this.add(this.row1);
         this.add(this.row2);
         this.add(this.outterBox);
         this.add(this.panel1);
         this.createBlackBottomBar(true, false, Aem.getString(1012), "Back", Aem.getString(5701), "Continue", "", "");
         this.createTopBar(Aem.getString(7001), "", "", "", "", Aem.getString(1001), "Help");
         this.bottomBar.addActionListener(this.promoCodeAction);
         this.topBar.addActionListener(this.promoCodeAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("PromoCodeScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PromoCodeScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.promoCode.setText("  " + AbstractContentBar.aemContent.getIntermediatePromoCode());
         this.row1.setText(Aem.getString(7002));
         this.row2.setText(Aem.getString(7003));
         this.bottomBar
            .setProperty(ScreenProperties.getColor("Black"), true, true, false, Aem.getString(1012), "Back", Aem.getString(5701), "Continue", "", "");
         this.topBar.setProperty(Aem.getString(7001), "", "", "", "", Aem.getString(1001), "Help");
         this.msg = new StringBuffer("* ").append("PromoCodeScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("PromoCodeScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void createPanel1() {
      this.panel1 = new JPanel();
      this.panel1.setBackground(ScreenProperties.getColor("White"));
      this.panel1.setBounds(70, 80, 900, 80);
      this.ii = ScreenProperties.getImage("Enter.Icon.PromoCode");
      this.promoCodeLabel = new JLabel(this.ii);
      this.ii.setImageObserver(this.promoCodeLabel);
      this.promoCodeLabel.setBounds(0, 0, ScreenProperties.getInt("PromoCode.Logo.Width") - 17, ScreenProperties.getInt("PromoCode.Logo.Height") + 10);
      this.promoCodeLabel.setBackground(ScreenProperties.getColor("White"));
      this.promoCodeLabel.setOpaque(true);
      this.promoCodeLabel.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
      this.panel1.add(this.promoCodeLabel);
      JPanel tt = this.createBorder(
         ScreenProperties.getColor("Red"), ScreenProperties.getInt("PromoCode.Logo.Width") - 70, ScreenProperties.getInt("PromoCode.Logo.Height") - 17, 800, 2
      );
      this.panel1.add(tt);
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
         timer.stop();
         timer.start();
         PromoCodeScreen.this.msg = new StringBuffer("[")
            .append("PromoCodeScreen")
            .append("]")
            .append(" Customer Pressed [")
            .append(ae.getActionCommand())
            .append("]");
         Aem.logDetailMessage(DvdplayLevel.INFO, PromoCodeScreen.this.msg.toString());
         if (Objects.equals(ae.getActionCommand(), "Clear")) {
            PromoCodeScreen.this.promoCode.setText("  ");
         } else if (Objects.equals(ae.getActionCommand(), "Backspace")) {
            if (PromoCodeScreen.this.promoCode.getText().length() > 2) {
               try {
                  PromoCodeScreen.this.promoCode.setText(PromoCodeScreen.this.promoCode.getText(0, PromoCodeScreen.this.promoCode.getText().length() - 1));
               } catch (BadLocationException var3) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] Exception : " + var3.toString());
               }
            }
         } else if (PromoCodeScreen.this.promoCode.getText().length() <= 20) {
            PromoCodeScreen.this.promoCode.setText(PromoCodeScreen.this.promoCode.getText() + ae.getActionCommand());
         }

         PromoCodeScreen.aemContent
            .setIntermediatePromoCode(PromoCodeScreen.this.promoCode.getText().substring(2));
      }
   }

   private class PromoCodeAction extends BaseActionListener {
      private PromoCodeAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("PromoCodeScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(PromoCodeScreen.mainAction)) {
                  PromoCodeScreen.this.promoCodeEntered = PromoCodeScreen.this.promoCode.getText();
                  PromoCodeScreen.this.promoCodeEntered = PromoCodeScreen.this.promoCodeEntered.substring(2);
                  if (this.cmd[0].equals("Continue")) {
                     if (!PromoCodeScreen.this.promoCodeEntered.isEmpty()) {
                        try {
                           PromoCodeScreen.aemContent.checkPromoCode(PromoCodeScreen.this.promoCodeEntered);
                           PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen 0"));
                        } catch (RentOpException var4) {
                           switch (var4.getCode()) {
                              case 9000:
                                 Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] No Such PromoCode");
                                 PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4012,CartTableScreen "));
                                 break;
                              case 9001:
                                 Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] Promo Invalid");
                                 PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4012,CartTableScreen "));
                                 break;
                              case 9002:
                                 Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] Promo Used Already");
                                 PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4012,CartTableScreen "));
                                 break;
                              case 9003:
                                 Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] Promo Too Early");
                                 PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4012,CartTableScreen "));
                                 break;
                              case 9004:
                                 Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] Promo Expired");
                                 PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4012,CartTableScreen "));
                                 break;
                              case 9005:
                                 Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] Promo Wrong Target");
                                 PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4012,CartTableScreen "));
                                 break;
                              default:
                                 Aem.logDetailMessage(DvdplayLevel.WARNING, "[PromoCodeScreen] Default request error: " + var4.getCode());
                                 PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4011,CartTableScreen "));
                           }
                        } catch (DvdplayException var5) {
                           PromoCodeScreen.aemContent.setInvalidPromoCodeEntered(true);
                           Aem.logDetailMessage(DvdplayLevel.ERROR, "[PromoCodeScreen] Default Request Error");
                           PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4011,CartTableScreen "));
                        }

                        PromoCodeScreen.this.msg = new StringBuffer("[")
                           .append("PromoCodeScreen")
                           .append("]")
                           .append(" PromoCode : [")
                           .append(PromoCodeScreen.this.promoCodeEntered)
                           .append("]");
                        Aem.logSummaryMessage(PromoCodeScreen.this.msg.toString());
                     }
                  } else if (this.cmd[0].equals("Back")) {
                     PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen 0"));
                  } else if (this.cmd[0].equals("Help")) {
                     PromoCodeScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  }
               }
            }
         } catch (Exception var6) {
            Log.warning(var6, "PromoCodeScreen");
         } catch (Throwable var7) {
            Log.error(var7, "PromoCodeScreen");
         }
      }
   }
}
