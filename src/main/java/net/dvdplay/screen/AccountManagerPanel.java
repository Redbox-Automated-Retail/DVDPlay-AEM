package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.Login;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.AccountManagerTableModel;
import net.dvdplay.view.KeyboardAssembler;

public class AccountManagerPanel extends AbstractToolsPanel {
   ActionListener accountManagerAction;
   ActionListener al;
   AccountManagerTableModel amt;
   JPanel entryPanel;
   JPanel buttonPanel;
   JPanel username;
   JPanel accountsDisplayPanel;
   JButton addNew;
   JButton update;
   JButton delete;
   JPasswordField password;
   JPasswordField newPassword;
   JPasswordField retypePassword;
   JComboBox roles;
   JLabel message1;
   JLabel message2;
   JLabel roleTitle;
   JLabel passwordTitle;
   JLabel newPasswordTitle;
   JLabel retypePasswordTitle;
   JTable jt;
   String currentlyUpdating;
   JPanel rolePanel;
   JPanel passwordPanel;
   JPanel newPasswordPanel;
   JPanel retypePasswordPanel;

   public AccountManagerPanel() {
      try {
         this.setLayout(null);
         this.accountManagerAction = new AccountManagerPanel.AccountManagerAction();
         this.focusableTextField = new ArrayList();
         this.amt = new AccountManagerTableModel();
         this.al = new AbstractToolsPanel.ActionTools();
         this.createButtonPanel();
         this.createEntryPanel();
         this.createAccountsDisplayPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.accountsDisplayPanel.setBounds(10, 10, 500, 500);
         this.buttonPanel.setBounds(500, 490, 450, 30);
         this.entryPanel.setBounds(500, 160, 340, 300);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.entryPanel);
         this.add(this.buttonPanel);
         this.add(this.accountsDisplayPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[AccountManagerPanel] " + var2.toString(), var2);
      }
   }

   private void createButtonPanel() {
      this.buttonPanel = new JPanel();
      this.buttonPanel.setLayout(new GridLayout(0, 3));
      this.addNew = this.createButton("Add New", "Add New", this.accountManagerAction);
      this.update = this.createButton("Change Password", "changePassword", this.accountManagerAction);
      this.delete = this.createButton("Delete", "Delete", this.accountManagerAction);
      this.buttonPanel.add(this.addNew);
      this.buttonPanel.add(this.update);
      this.buttonPanel.add(this.delete);
   }

   private void createEntryPanel() {
      this.entryPanel = new JPanel();
      String[] roleList = new String[]{"Admin", "UserAdmin", "Operator", "StoreOwner"};
      this.rolePanel = new JPanel();
      this.roleTitle = new JLabel("Role : ");
      this.roleTitle.setFont(this.labelFont);
      this.roleTitle.setBorder(this.labelBorder);
      this.roleTitle.setHorizontalAlignment(4);
      this.roleTitle.setPreferredSize(new Dimension(170, 20));
      this.roles = new JComboBox<>(roleList);
      this.rolePanel.add(this.roleTitle);
      this.rolePanel.add(this.roles);
      this.password = new JPasswordField();
      this.password.setColumns(8);
      this.passwordPanel = new JPanel();
      this.passwordTitle = new JLabel("Password : ");
      this.passwordTitle.setFont(this.labelFont);
      this.passwordTitle.setBorder(this.labelBorder);
      this.passwordTitle.setHorizontalAlignment(4);
      this.passwordTitle.setPreferredSize(new Dimension(170, 20));
      this.passwordPanel.add(this.passwordTitle);
      this.passwordPanel.add(this.password);
      this.newPassword = new JPasswordField();
      this.newPassword.setColumns(8);
      this.newPassword.setEnabled(false);
      this.newPasswordPanel = new JPanel();
      this.newPasswordTitle = new JLabel("New Password : ");
      this.newPasswordTitle.setFont(this.labelFont);
      this.newPasswordTitle.setBorder(this.labelBorder);
      this.newPasswordTitle.setHorizontalAlignment(4);
      this.newPasswordTitle.setPreferredSize(new Dimension(170, 20));
      this.newPasswordPanel.add(this.newPasswordTitle);
      this.newPasswordPanel.add(this.newPassword);
      this.retypePassword = new JPasswordField();
      this.retypePassword.setColumns(8);
      this.retypePassword.setEnabled(false);
      this.retypePasswordPanel = new JPanel();
      this.retypePasswordTitle = new JLabel("Retype Password : ");
      this.retypePasswordTitle.setFont(this.labelFont);
      this.retypePasswordTitle.setBorder(this.labelBorder);
      this.retypePasswordTitle.setHorizontalAlignment(4);
      this.retypePasswordTitle.setPreferredSize(new Dimension(170, 20));
      this.retypePasswordPanel.add(this.retypePasswordTitle);
      this.retypePasswordPanel.add(this.retypePassword);
      this.username = this.createRow("User Name : ", 8, "username");
      this.focusableTextField.add("username");
      this.focusableTextField.add("password");
      this.message1 = new JLabel();
      this.message1.setForeground(Color.RED);
      this.message2 = new JLabel();
      this.message2.setForeground(Color.RED);
      this.entryPanel.add(this.rolePanel);
      this.entryPanel.add(this.username);
      this.entryPanel.add(this.passwordPanel);
      this.entryPanel.add(this.newPasswordPanel);
      this.entryPanel.add(this.retypePasswordPanel);
      this.entryPanel.add(this.message1);
      this.entryPanel.add(this.message2);
   }

   public void createAccountsDisplayPanel() {
      this.accountsDisplayPanel = new JPanel();
      this.jt = new JTable(this.amt);
      this.jt.setAutoResizeMode(0);
      this.jt.setRowSelectionAllowed(true);
      this.jt.setEditingColumn(0);
      this.jt.setRowHeight(20);
      JScrollPane jsp = new JScrollPane(this.jt);
      this.jt.setPreferredScrollableViewportSize(new Dimension(450, 470));
      this.accountsDisplayPanel.add(jsp);
   }

   private class AccountManagerAction implements ActionListener {
      private AccountManagerAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         AccountManagerPanel.this.command = ae.getActionCommand();
         AccountManagerPanel.this.data = "";
         Aem.logDetailMessage(DvdplayLevel.FINER, "[AccountManagerPanel] Action " + AccountManagerPanel.this.command);
         AccountManagerPanel.this.message1.setText("");
         AccountManagerPanel.this.message2.setText("");
         if (AccountManagerPanel.this.command.equals("Add New")) {
            AccountManagerPanel.this.data = AccountManagerPanel.this.roles.getSelectedItem()
               + ","
               + ((JTextField)AccountManagerPanel.this.textFieldHash.get("username")).getText()
               + ","
               + AccountManagerPanel.this.password.getPassword();
            Object[] temp = new Object[]{
               false,
               ((JTextField)AccountManagerPanel.this.textFieldHash.get("username")).getText(),
                    (String) AccountManagerPanel.this.roles.getSelectedItem()
            };
            if (((String)temp[1]).length() > 0 && ((String)temp[2]).length() > 0) {
               Aem.logDetailMessage(DvdplayLevel.FINER, "[AccountManagerPanel] Add User " + temp[1] + " with " + temp[2]);
               AccountManagerPanel.this.amt.addValue(temp);
               AccountManagerPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "Login addNew " + AccountManagerPanel.this.data));
            } else {
               AccountManagerPanel.this.message1.setText("Wrong format of Username/Password");
            }

            ((JTextField)AccountManagerPanel.this.textFieldHash.get("username")).setText("");
            AccountManagerPanel.this.password.setText("");
         }

         if (AccountManagerPanel.this.command.equals("Delete")) {
            ArrayList selected = AccountManagerPanel.this.amt.getSelected();
            if (selected.size() <= 1 && selected.size() >= 1) {
               AccountManagerPanel.this.currentlyUpdating = (String)selected.get(0);
               Aem.logDetailMessage(DvdplayLevel.FINER, "[AccountManagerPanel] Delete User " + AccountManagerPanel.this.currentlyUpdating);
               AccountManagerPanel.this.amt.removeRowByUserName(AccountManagerPanel.this.currentlyUpdating);
               Login.delete(AccountManagerPanel.this.currentlyUpdating);
            } else {
               AccountManagerPanel.this.message1.setText("Please Select Only One account");
            }
         }

         if (AccountManagerPanel.this.command.equals("changePassword")) {
            ArrayList selected = AccountManagerPanel.this.amt.getSelected();
            if (selected.size() <= 1 && selected.size() >= 1) {
               AccountManagerPanel.this.currentlyUpdating = (String)selected.get(0);
               Aem.logDetailMessage(DvdplayLevel.FINER, "[AccountManagerPanel] Update User Pasword" + AccountManagerPanel.this.currentlyUpdating);
               AccountManagerPanel.this.message1.setText("Please type the New password for user " + AccountManagerPanel.this.currentlyUpdating);
               AccountManagerPanel.this.message2.setText(" and then hit Update button");
               AccountManagerPanel.this.newPassword.setEnabled(true);
               AccountManagerPanel.this.retypePassword.setEnabled(true);
               AccountManagerPanel.this.newPassword.grabFocus();
               AccountManagerPanel.this.update.setText("Update");
               AccountManagerPanel.this.update.setActionCommand("Update");
            } else {
               AccountManagerPanel.this.message1.setText("Please Select Only One account");
            }
         }

         if (AccountManagerPanel.this.command.equals("Update")) {
            String pass1 = new String(AccountManagerPanel.this.newPassword.getPassword());
            String pass2 = new String(AccountManagerPanel.this.retypePassword.getPassword());
            if (pass1.equals(pass2)) {
               Login.update(AccountManagerPanel.this.currentlyUpdating + "," + pass1 + "," + pass2);
               Aem.logDetailMessage(DvdplayLevel.FINER, "[AccountManagerPanel] Update User " + AccountManagerPanel.this.currentlyUpdating);
               AccountManagerPanel.this.newPassword.setText("");
               AccountManagerPanel.this.retypePassword.setText("");
               AccountManagerPanel.this.amt.unSelectAll();
               AccountManagerPanel.this.update.setText("Change Password");
               AccountManagerPanel.this.update.setActionCommand("changePassword");
            } else {
               AccountManagerPanel.this.message1.setText("Password doesn't match, please try again");
               AccountManagerPanel.this.newPassword.grabFocus();
               AccountManagerPanel.this.newPassword.setSelectionStart(0);
               AccountManagerPanel.this.retypePassword.setSelectionStart(0);
               AccountManagerPanel.this.newPassword.setSelectionEnd(AccountManagerPanel.this.newPassword.getPassword().length);
               AccountManagerPanel.this.retypePassword.setSelectionEnd(AccountManagerPanel.this.retypePassword.getPassword().length);
               AccountManagerPanel.this.update.setText("Update");
               AccountManagerPanel.this.update.setActionCommand("Update");
            }
         }
      }
   }
}
