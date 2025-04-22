package net.dvdplay.screen;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.Operator;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.KeyboardAssembler;

public class OperatorPanel extends AbstractToolsPanel {
   JPanel generalSettingPanel;
   JPanel buttonPanel;
   JPanel serverSettingsPanel;
   JPanel contactInfoPanel;
   JPanel statusPanel;
   JPanel statusLightPanel;
   ActionListener al;
   ActionListener operatorAction;
   ActionListener toolsAction;
   Operator operator;
   JLabel hardwareError;
   JLabel queueBusy;
   JLabel serverConnection;

   public OperatorPanel() {
      try {
         this.setLayout(null);
         this.al = new AbstractToolsPanel.ActionTools();
         this.operatorAction = new OperatorPanel.OperatorAction();
         this.operator = new Operator(this.al, this);
         this.focusableTextField = new ArrayList();
         this.generalSettingPanel = this.createGeneralSettingPanel();
         this.buttonPanel = this.createButtonPanel();
         this.serverSettingsPanel = this.createServerSettingsPanel();
         this.contactInfoPanel = this.createContactInfoPanel();
         this.statusPanel = this.createStatusPanel();
         this.statusLightPanel = this.createStatusLightPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.generalSettingPanel.setBounds(10, 10, 800, 210);
         this.contactInfoPanel.setBounds(10, 230, 420, 230);
         this.serverSettingsPanel.setBounds(500, 230, 410, 80);
         this.buttonPanel.setBounds(300, 470, 800, 40);
         this.ka.setBounds(5, 530, 990, 120);
         this.statusPanel.setBounds(500, 350, 410, 100);
         this.statusLightPanel.setBounds(820, 20, 150, 35);
         this.add(this.generalSettingPanel);
         this.add(this.buttonPanel);
         this.add(this.serverSettingsPanel);
         this.add(this.contactInfoPanel);
         this.add(this.ka);
         this.add(this.statusPanel);
         this.add(this.statusLightPanel);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[OperatorPanel] " + var2.toString(), var2);
      }
   }

   private JPanel createGeneralSettingPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 1));
      temp.setBorder(new TitledBorder("General Setting"));
      JPanel kioskID = this.createRow("AEM ID : ", 20, "aemID", Operator.getAemID(), false);
      JPanel versionNo = this.createRow("Version No. : ", 20, "versioNo", Operator.getVersionNo(), false);
      JPanel serverAddress = this.createRow("Server Address : ", 55, "serverAddress", Operator.getServerAddress(), false);
      JPanel ftpAddress = this.createRow("FTP Address : ", 55, "ftpAddress", Operator.getFtpAddress(), false);
      JPanel dueTimeOfDay = this.createRow("Due Time Of Day : ", 20, "dueTimeOfDay", Operator.getDayTimeOfDay(), false);
      JPanel shutdownTime = this.createRow("Shutdown Time : ", 20, "shutDownTime", Operator.getShutDownTime(), false);
      this.focusableTextField.add("aemID");
      this.focusableTextField.add("serverAddress");
      this.focusableTextField.add("ftpAddress");
      temp.add(kioskID);
      temp.add(versionNo);
      temp.add(new JLabel("                                "));
      temp.add(serverAddress);
      temp.add(ftpAddress);
      temp.add(new JLabel("                                   "));
      temp.add(dueTimeOfDay);
      temp.add(shutdownTime);
      return temp;
   }

   private JPanel createButtonPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new FlowLayout());
      JButton closeAll = this.createButton("Exit App & Tools", "closeAll", this.operatorAction);
      JButton closeApp = this.createButton("Exit Tools", "close", this.operatorAction);
      JButton test = this.createButton("Test Server Connection", "testServerConnection", this.operatorAction);
      temp.add(closeAll);
      temp.add(closeApp);
      temp.add(test);
      return temp;
   }

   private JPanel createServerSettingsPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 1));
      temp.setBorder(new TitledBorder("Server Settings"));
      JPanel taxRate = this.createRow("Tax Rate : ", 20, "taxRate", Operator.getTaxRate(), false);
      JPanel noChargeTimeMins = this.createRow("NoChargeTimeMins : ", 20, "noChargeTimeMins", Operator.getNoChargeTimeMins(), false);
      temp.add(taxRate);
      temp.add(noChargeTimeMins);
      return temp;
   }

   private JPanel createStatusPanel() {
      JPanel temp = new JPanel();
      JTextArea status = new JTextArea(5, 35);
      status.setEditable(false);
      status.setWrapStyleWord(true);
      JScrollPane jsp = new JScrollPane(status, 20, 30);
      temp.add(jsp);
      return temp;
   }

   private JPanel createStatusLightPanel() {
      JPanel temp = new JPanel();
      this.hardwareError = new JLabel();
      this.hardwareError.setHorizontalAlignment(0);
      this.hardwareError.setVerticalAlignment(0);
      this.hardwareError.setBounds(0, 0, 50, 50);
      this.serverConnection = new JLabel();
      this.serverConnection.setHorizontalAlignment(0);
      this.serverConnection.setVerticalAlignment(0);
      this.serverConnection.setBounds(50, 0, 50, 50);
      this.queueBusy = new JLabel();
      this.queueBusy.setHorizontalAlignment(0);
      this.queueBusy.setVerticalAlignment(0);
      this.queueBusy.setBounds(100, 0, 50, 50);
      temp.add(this.hardwareError);
      temp.add(this.serverConnection);
      temp.add(this.queueBusy);
      Operator.startIndicator(this.hardwareError, this.queueBusy, this.serverConnection);
      return temp;
   }

   private JPanel createContactInfoPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 1));
      temp.setBorder(new TitledBorder("Contact Information"));
      JPanel name = this.createRow("Name : ", 20, "name", Operator.getContactName(), false);
      JPanel address = this.createRow("Address : ", 20, "address", Operator.getAddress(), false);
      JPanel address2 = this.createRow("Address (line 2) : ", 20, "address2", Operator.getAddress2(), false);
      JPanel city = this.createRow("City, State ZipCode : ", 20, "city", Operator.getCityStateZip(), false);
      JPanel email = this.createRow("Email : ", 20, "Email", Operator.getEmail(), false);
      JPanel helpPhone = this.createRow("Help Phone : ", 20, "helpPhone", Operator.getHelpPhone(), false);
      temp.add(name);
      temp.add(address);
      temp.add(address2);
      temp.add(city);
      temp.add(email);
      temp.add(helpPhone);
      return temp;
   }

   public void addActionListener(ActionListener l) {
      this.toolsAction = l;
   }

   private class OperatorAction implements ActionListener {
      private OperatorAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         OperatorPanel.this.command = ae.getActionCommand();
         OperatorPanel.this.data = "";
         Aem.logDetailMessage(DvdplayLevel.FINE, "[OperatorPanel] Action " + OperatorPanel.this.command);
         if (OperatorPanel.this.command.equals("testServerConnection")) {
            Object result = OperatorPanel.this.verifyStringEntry("serverAddress,ftpAddress");
            if (result instanceof String) {
               OperatorPanel.this.data = ((JTextField)OperatorPanel.this.textFieldHash.get("serverAddress")).getText()
                  + ","
                  + ((JTextField)OperatorPanel.this.textFieldHash.get("ftpAddress")).getText();
               OperatorPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "Operator testServerConnection " + OperatorPanel.this.data));
            }
         }

         if (OperatorPanel.this.command.equals("save")) {
            Object result = OperatorPanel.this.verifyStringEntry("serverAddress,ftpAddress");
            if (result instanceof String) {
               OperatorPanel.this.data = ((JTextField)OperatorPanel.this.textFieldHash.get("aemID")).getText()
                  + ","
                  + ((JTextField)OperatorPanel.this.textFieldHash.get("serverAddress")).getText()
                  + ","
                  + ((JTextField)OperatorPanel.this.textFieldHash.get("ftpAddress")).getText();
               OperatorPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "Operator save " + OperatorPanel.this.data));
            }
         }

         if (OperatorPanel.this.command.equals("reload")) {
            OperatorPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "Operator reload"));
         }

         if (OperatorPanel.this.command.equals("closeAll")) {
            OperatorPanel.this.toolsAction.actionPerformed(new ActionEvent(this, 1001, "closeAll"));
         }

         if (OperatorPanel.this.command.equals("close")) {
            OperatorPanel.this.toolsAction.actionPerformed(new ActionEvent(this, 1001, "close"));
         }
      }
   }
}
