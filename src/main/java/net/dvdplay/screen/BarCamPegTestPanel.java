package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.BarCamPegTest;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.KeyboardAssembler;

public class BarCamPegTestPanel extends AbstractToolsPanel {
   JPanel commandsPanel;
   JPanel dialogsPanel;
   JPanel barCodeIlluminationPanel;
   JPanel picturePanel;
   JPanel optionsPanel;
   JPanel resultsPanel;
   JPanel statusPanel;
   JPanel buttonPanel;
   BarCamPegTest barCamPegTest;
   ActionListener al;
   ActionListener barCamPegTestAction;
   JCheckBox saveImages;
   JCheckBox stopOnSuccess;
   JTextField barcodeIlluminationLevelField;
   JTextField anglesField;

   public BarCamPegTestPanel() {
      try {
         this.setLayout(null);
         this.al = new AbstractToolsPanel.ActionTools();
         this.barCamPegTest = new BarCamPegTest(this.al, this);
         this.barCamPegTestAction = new BarCamPegTestPanel.BarCamPegTestAction();
         this.commandsPanel = this.createCommandsPanel();
         this.dialogsPanel = this.createDialogPanel();
         this.barCodeIlluminationPanel = this.createBarCodeIlluminationPanel();
         this.picturePanel = this.createPicturePanel();
         this.optionsPanel = this.createOptionsPanel();
         this.resultsPanel = this.createResultsPanel();
         this.buttonPanel = this.createButtonPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.commandsPanel.setBounds(10, 10, 180, 300);
         this.dialogsPanel.setBounds(10, 320, 180, 200);
         this.barCodeIlluminationPanel.setBounds(810, 230, 170, 160);
         this.picturePanel.setBounds(200, 10, 590, 400);
         this.optionsPanel.setBounds(810, 10, 170, 100);
         this.resultsPanel.setBounds(810, 120, 170, 100);
         this.buttonPanel.setBounds(420, 470, 800, 40);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.commandsPanel);
         this.add(this.dialogsPanel);
         this.add(this.barCodeIlluminationPanel);
         this.add(this.picturePanel);
         this.add(this.optionsPanel);
         this.add(this.resultsPanel);
         this.add(this.buttonPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[BarCamPegTestPanel] " + var2.toString(), var2);
      }
   }

   private JPanel createCommandsPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Commands"));
      JLabel deviceLabel = new JLabel("Device : ");
      JComboBox device = new JComboBox<>(BarCamPegTest.getDevice());
      JLabel anglesLabel = new JLabel("Angles : ");
      this.anglesField = new JTextField(BarCamPegTest.getAngles(), 10);
      this.anglesField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      JButton acquire = this.createButton("Acquire", "BarCamPegTest commandsAcquire", this.barCamPegTestAction);
      JCheckBox continuousScan = new JCheckBox("Continuous scan");
      continuousScan.setEnabled(false);
      temp.add(deviceLabel);
      temp.add(device);
      temp.add(anglesLabel);
      temp.add(this.anglesField);
      temp.add(acquire);
      temp.add(continuousScan);
      return temp;
   }

   private JPanel createDialogPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Dialogs"));
      JButton videoFormat = this.createButton("Video Format", "BarCamPegTest videoFormat");
      JButton videoSource = this.createButton("Video Source", "BarCamPegTest videoSource");
      JButton videoDisplay = this.createButton("Video Display", "BarCamPegTest videoDisplay");
      videoDisplay.setEnabled(false);
      JButton videoCompression = this.createButton("Video Compression", "BarCamPegTest videoCompression");
      temp.add(videoFormat);
      temp.add(videoSource);
      temp.add(videoDisplay);
      temp.add(videoCompression);
      return temp;
   }

   private JPanel createPicturePanel() {
      JPanel temp = new JPanel();
      temp.setBorder(new TitledBorder("Picture"));
      JLabel picture = new JLabel();
      picture.setBackground(Color.WHITE);
      picture.setPreferredSize(new Dimension(570, 380));
      this.pictureLabel = picture;
      temp.add(picture);
      return temp;
   }

   private JPanel createOptionsPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Options"));
      this.saveImages = new JCheckBox("Save Images", BarCamPegTest.getSaveImages());
      this.saveImages.addActionListener(this.barCamPegTestAction);
      this.saveImages.setEnabled(false);
      this.stopOnSuccess = new JCheckBox("Stop On Success", BarCamPegTest.getStopOnSuccess());
      this.stopOnSuccess.addActionListener(this.barCamPegTestAction);
      this.stopOnSuccess.setEnabled(false);
      temp.add(this.saveImages);
      temp.add(this.stopOnSuccess);
      return temp;
   }

   private JPanel createButtonPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new FlowLayout());
      JButton save = this.createButton("Save Settings", "save", this.barCamPegTestAction);
      temp.add(save);
      return temp;
   }

   private JPanel createResultsPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 2));
      temp.setBorder(new TitledBorder("Results"));
      JLabel code1Label = new JLabel("Code1");
      JTextField code1 = new JTextField("");
      code1.setEditable(false);
      this.textFieldHash.put("code1", code1);
      JLabel code2Label = new JLabel("Code2");
      JTextField code2 = new JTextField("");
      code2.setEditable(false);
      this.textFieldHash.put("code2", code2);
      temp.add(code1Label);
      temp.add(code1);
      temp.add(code2Label);
      temp.add(code2);
      return temp;
   }

   private JPanel createBarCodeIlluminationPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Barcode Illumination"));
      JLabel barcodeIlluminationLevelLabel = this.createLabel("Level(0-255): ");
      barcodeIlluminationLevelLabel.setBounds(0, 0, 120, 30);
      this.barcodeIlluminationLevelField = new JTextField();
      this.barcodeIlluminationLevelField.setText(String.valueOf(BarCamPegTest.getBarcodeIlluminationLevel()));
      this.barcodeIlluminationLevelField.setBounds(125, 0, 50, 30);
      this.barcodeIlluminationLevelField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      JPanel levelPanel = new JPanel(null);
      levelPanel.add(barcodeIlluminationLevelLabel);
      levelPanel.add(this.barcodeIlluminationLevelField);
      JButton barcodeIlluminationLevelOn = this.createButton("On", "BarCamPegTest barcodeIlluminationLevelOn", this.barCamPegTestAction);
      JButton barcodeIlluminationLevelOff = this.createButton("Off", "BarCamPegTest barcodeIlluminationLevelOff");
      temp.add(levelPanel);
      temp.add(barcodeIlluminationLevelOn);
      temp.add(barcodeIlluminationLevelOff);
      return temp;
   }

   private class BarCamPegTestAction implements ActionListener {
      private BarCamPegTestAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         BarCamPegTestPanel.this.command = ae.getActionCommand();
         BarCamPegTestPanel.this.data = "";

         Aem.logDetailMessage(DvdplayLevel.FINER, "[BadSlotsPanel] Action " + command);
          switch (command) {
              case "save" -> {
                  data += saveImages.isSelected() ? "saveImages:true," : "saveImages:false,";
                  data += stopOnSuccess.isSelected() ? "stopOnSuccess:true" : "stopOnSuccess:false";
                  al.actionPerformed(new ActionEvent(this, 1001, "BarCamPegTest save " + data));
              }
              case "BarCamPegTest barcodeIlluminationLevelOn" -> {
                  data = barcodeIlluminationLevelField.getText();
                  al.actionPerformed(new ActionEvent(this, 1001, "BarCamPegTest barcodeIlluminationLevelOn " + data));
              }
              case "BarCamPegTest commandsAcquire" -> {
                  data = anglesField.getText();
                  al.actionPerformed(new ActionEvent(this, 1001, "BarCamPegTest commandsAcquire " + data));
              }
          }
      }
   }
}
