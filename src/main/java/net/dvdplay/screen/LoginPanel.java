package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.util.EncryptString;
import net.dvdplay.view.KeyboardAssembler;

public class LoginPanel extends AbstractToolsPanel {
   ActionListener loginAction;
   ActionListener toolsAction;
   JPanel entryPanel;
   JPanel buttonPanel;
   JPanel messagePanel;
   JPasswordField password;
   JTextField username;
   JLabel messageField;
   JButton login;
   JButton cancel;
   Cursor busyCursor = new Cursor(3);
   String encryptedPassword = "";
   EncryptString encryptString;

   public LoginPanel() {
      try {
         this.setLayout(null);
         this.loginAction = new LoginPanel.LoginAction();
         this.focusableTextField = new ArrayList();
         this.encryptString = new EncryptString();
         this.createButtonPanel();
         this.createEntryPanel();
         this.createMessagePanel();
         UIManager.getDefaults().put("Button.select", ScreenProperties.getColor("Orange"));
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 0);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.buttonPanel.setBounds(510, 490, 300, 30);
         this.entryPanel.setBounds(350, 300, 300, 150);
         this.messagePanel.setBounds(250, 400, 500, 30);
         this.ka.setBounds(5, 555, 990, 120);
         this.add(this.messagePanel);
         this.add(this.entryPanel);
         this.add(this.buttonPanel);
         this.add(this.ka);
         this.username.grabFocus();
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "[LoginPanel] " + var2.toString(), var2);
      }
   }

   private void createButtonPanel() {
      this.buttonPanel = new JPanel();
      this.buttonPanel.setLayout(new GridLayout(0, 2));
      this.login = this.createButton("Login", "Login", this.loginAction);
      this.cancel = this.createButton("Cancel", "Cancel", this.loginAction);
      this.buttonPanel.add(this.login);
      this.buttonPanel.add(this.cancel);
   }

   private void createEntryPanel() {
      this.entryPanel = new JPanel();
      this.username = new JTextField();
      this.username.setColumns(8);
      this.username.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      JLabel usernameTitle = new JLabel("User Name : ");
      usernameTitle.setFont(this.labelFont);
      usernameTitle.setBorder(this.labelBorder);
      usernameTitle.setHorizontalAlignment(4);
      usernameTitle.setPreferredSize(new Dimension(170, 20));
      this.username.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent fe) {
            messageField.setText("");
         }

         public void focusLost(FocusEvent fe) {
         }
      });

      this.username.grabFocus();
      this.password = new JPasswordField();
      this.password.setColumns(8);
      this.password.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      this.password.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent fe) {
            messageField.setText("");
         }

         public void focusLost(FocusEvent fe) {
         }
      });
      JLabel passwordTitle = new JLabel("Password : ");
      passwordTitle.setFont(this.labelFont);
      passwordTitle.setBorder(this.labelBorder);
      passwordTitle.setHorizontalAlignment(4);
      passwordTitle.setPreferredSize(new Dimension(170, 20));
      this.focusableTextField.add("username");
      this.focusableTextField.add("password");
      this.textFieldHash.put("username", this.username);
      this.textFieldHash.put("password", this.password);
      this.entryPanel.add(usernameTitle);
      this.entryPanel.add(this.username);
      this.entryPanel.add(passwordTitle);
      this.entryPanel.add(this.password);
   }

   private void createMessagePanel() {
      this.messagePanel = new JPanel();
      this.messageField = new JLabel();
      this.messageField.setFont(new Font("Arial", 1, 13));
      this.messageField.setForeground(Color.RED);
      this.messageField.setBackground(Color.GRAY);
      this.messageField.setBounds(0, 0, 500, 30);
      this.messagePanel.add(this.messageField);
   }

   public void writeMessage(String msg) {
      this.messageField.setText(msg);
      this.setCursor(new Cursor(0));
   }

   public void disableLoginButton() {
      this.login.setEnabled(false);
   }

   public void addActionListener(ActionListener l) {
      this.toolsAction = l;
   }

   private class LoginAction implements ActionListener {
      private LoginAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            LoginPanel.this.messageField.setText("");
            LoginPanel.this.command = ae.getActionCommand();
            LoginPanel.this.data = "";
            if (LoginPanel.this.command.equals("Login")) {
               LoginPanel.this.setCursor(LoginPanel.this.busyCursor);
               JTextField t1 = (JTextField)LoginPanel.this.textFieldHash.get("username");
               LoginPanel.this.encryptedPassword = LoginPanel.this.encryptString.getEncryptedString(LoginPanel.this.password.getText());
               LoginPanel.this.data = t1.getText() + "," + LoginPanel.this.encryptedPassword;
               LoginPanel.this.toolsAction.actionPerformed(new ActionEvent(this, 1001, "Login " + LoginPanel.this.data));
            }

            if (LoginPanel.this.command.equals("Cancel")) {
               LoginPanel.this.toolsAction.actionPerformed(new ActionEvent(this, 1001, "closeWithoutRestart"));
            }
         } catch (Exception var3) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "[LoginPanel] " + var3.toString(), var3);
         }
      }
   }
}
