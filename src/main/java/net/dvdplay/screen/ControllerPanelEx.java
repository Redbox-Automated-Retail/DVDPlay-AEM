package net.dvdplay.screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.ControllerEx;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.KeyboardAssembler;

public class ControllerPanelEx extends ControllerPanel {
   JPanel armMotorPanel;
   JPanel doorMotorPanel;
   JPanel rollerMotorPanel;
   JPanel allMotorsPanel;
   JPanel armMacrosPanel;
   JPanel doorMacrosPanel;
   JPanel lcdPanel;
   JPanel barcodeIlluminationPanel;
   JPanel statusBitsPanel;
   JPanel enablePanel;
   JPanel homeCarouselPanel;
   JPanel slotMovementPanel;
   JPanel servoMotorStatusPanel;
   JPanel moveCarouselPanel;
   ControllerEx controller;
   ActionListener controllerAction;
   ActionListener al;
   JTextField slotMovementNoField;
   JTextField moveCarouselPositionField;
   JTextField moveCarouselVelocityField;
   JTextField moveCarouselAccelerationField;
   JTextField barcodeIlluminationLevelField;
   JCheckBox serverMotorStatusUpdateContinuously;

   public ControllerPanelEx() {
      try {
         this.setLayout(null);
         this.al = new AbstractToolsPanel.ActionTools();
         this.controllerAction = new ControllerPanelEx.ControllerPanelAction();
         this.controller = new ControllerEx(this.al, this);
         this.focusableTextField = new ArrayList();
         this.armMotorPanel = this.createArmMotorPanel();
         this.doorMotorPanel = this.createDoorMotorPanel();
         this.rollerMotorPanel = this.createRollerMotorPanel();
         this.allMotorsPanel = this.createAllMotorsPanel();
         this.createArmMacrosPanel();
         this.createDoorMacrosPanel();
         this.lcdPanel = this.createLcdPanel();
         this.barcodeIlluminationPanel = this.createBarcodeIlluminationPanel();
         this.statusBitsPanel = this.createStatusBitsPanel();
         this.enablePanel = this.createEnablePanel();
         this.homeCarouselPanel = this.createHomeCarouselPanel();
         this.slotMovementPanel = this.createSlotMovementPanel();
         this.servoMotorStatusPanel = this.createServoMotorStatusPanel();
         this.moveCarouselPanel = this.createMoveCarouselPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.armMotorPanel.setBounds(10, 10, 150, 100);
         this.doorMotorPanel.setBounds(10, 110, 150, 100);
         this.rollerMotorPanel.setBounds(170, 10, 150, 200);
         this.allMotorsPanel.setBounds(170, 220, 150, 100);
         this.armMacrosPanel.setBounds(330, 10, 150, 200);
         this.doorMacrosPanel.setBounds(490, 10, 150, 250);
         this.lcdPanel.setBounds(640, 10, 150, 100);
         this.barcodeIlluminationPanel.setBounds(640, 110, 150, 150);
         this.statusBitsPanel.setBounds(800, 10, 180, 330);
         this.enablePanel.setBounds(10, 320, 310, 80);
         this.homeCarouselPanel.setBounds(10, 430, 310, 80);
         this.slotMovementPanel.setBounds(330, 260, 310, 140);
         this.servoMotorStatusPanel.setBounds(330, 400, 310, 110);
         this.moveCarouselPanel.setBounds(650, 340, 320, 170);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.armMotorPanel);
         this.add(this.doorMotorPanel);
         this.add(this.rollerMotorPanel);
         this.add(this.allMotorsPanel);
         this.add(this.armMacrosPanel);
         this.add(this.doorMacrosPanel);
         this.add(this.lcdPanel);
         this.add(this.barcodeIlluminationPanel);
         this.add(this.statusBitsPanel);
         this.add(this.enablePanel);
         this.add(this.homeCarouselPanel);
         this.add(this.slotMovementPanel);
         this.add(this.servoMotorStatusPanel);
         this.add(this.moveCarouselPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[ControllerPanelEx] " + var2.toString(), var2);
      }
   }

   private JPanel createArmMotorPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Arm Motor"));
      JButton armMotorOn = this.createButton("On", "ControllerEx armMotorOn");
      JButton armMotorOff = this.createButton("Off", "ControllerEx armMotorOff");
      temp.add(armMotorOn);
      temp.add(armMotorOff);
      return temp;
   }

   private JPanel createDoorMotorPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Door Motor"));
      JButton doorMotorOn = this.createButton("On", "ControllerEx doorMotorOn");
      JButton doorMotorOff = this.createButton("Off", "ControllerEx doorMotorOff");
      temp.add(doorMotorOn);
      temp.add(doorMotorOff);
      return temp;
   }

   private JPanel createRollerMotorPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Roller Motor"));
      JButton rollerMotorIn = this.createButton("In", "ControllerEx rollerMotorIn");
      JButton rollerMotorOut = this.createButton("Out", "ControllerEx rollerMotorOut");
      JButton rollerMotorBrakeOn = this.createButton("BrakeOn", "ControllerEx rollerMotorBrakeOn");
      JButton rollerMotorBrakeOff = this.createButton("BrakeOff", "ControllerEx rollerMotorBrakeOff");
      temp.add(rollerMotorIn);
      temp.add(rollerMotorOut);
      temp.add(rollerMotorBrakeOn);
      temp.add(rollerMotorBrakeOff);
      return temp;
   }

   private JPanel createAllMotorsPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("All Motors"));
      JButton allMotorsEnable = this.createButton("Enable", "ControllerEx allMotorEnable");
      JButton allMotorsDisable = this.createButton("Disable", "ControllerEx allMotorDisable");
      temp.add(allMotorsEnable);
      temp.add(allMotorsDisable);
      return temp;
   }

   private void createArmMacrosPanel() {
      this.armMacrosPanel = new JPanel(new GridLayout(0, 1, 5, 10));
      this.armMacrosPanel.setBorder(new TitledBorder("Arm Macros"));
      JButton armMacrosClear = this.createButton("Clear", "ControllerEx armMacrosClear");
      JButton armMacrosEject = this.createButton("Eject", "ControllerEx armMacrosEject");
      JButton armMacrosStatus = this.createButton("Status", "ControllerEx armMacrosStatus");
      JLabel armMacrosStatusLabel = this.createLabel("Status: ");
      JTextField armMacrosStatusField = new JTextField(this.controller.getArmMacrosSatus(), 7);
      JPanel statusPanel = new JPanel(new FlowLayout());
      armMacrosStatusField.setEditable(false);
      this.textFieldHash.put("armMacrosStatusField", armMacrosStatusField);
      statusPanel.add(armMacrosStatusLabel);
      statusPanel.add(armMacrosStatusField);
      this.armMacrosPanel.add(armMacrosClear);
      this.armMacrosPanel.add(armMacrosEject);
      this.armMacrosPanel.add(armMacrosStatus);
      this.armMacrosPanel.add(statusPanel);
   }

   private void createDoorMacrosPanel() {
      this.doorMacrosPanel = new JPanel(new GridLayout(0, 1, 5, 10));
      this.doorMacrosPanel.setBorder(new TitledBorder("Door Macros"));
      JButton doorMacrosOpen = this.createButton("Open", "ControllerEx doorMacrosOpen");
      JButton doorMacrosClamp = this.createButton("Clamp", "ControllerEx doorMacrosClamp");
      JButton doorMacrosClose = this.createButton("Close", "ControllerEx doorMacrosClose");
      JButton doorMacrosStatus = this.createButton("Status", "ControllerEx doorMacrosStatus");
      JLabel doorMacrosStatusLabel = this.createLabel("Status: ");
      JTextField doorMacrosStatusField = new JTextField(this.controller.getDoorMacrosSatus(), 7);
      doorMacrosStatusField.setEditable(false);
      this.textFieldHash.put("doorMacrosStatusField", doorMacrosStatusField);
      JPanel statusPanel = new JPanel(new FlowLayout());
      statusPanel.add(doorMacrosStatusLabel);
      statusPanel.add(doorMacrosStatusField);
      this.doorMacrosPanel.add(doorMacrosOpen);
      this.doorMacrosPanel.add(doorMacrosClamp);
      this.doorMacrosPanel.add(doorMacrosClose);
      this.doorMacrosPanel.add(doorMacrosStatus);
      this.doorMacrosPanel.add(statusPanel);
   }

   private JPanel createLcdPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("LCD"));
      JButton lcdPowerOn = this.createButton("Power On", "ControllerEx lcdPowerOn");
      JButton lcdPowerOff = this.createButton("Power Off", "ControllerEx lcdPowerOff", this.controllerAction);
      lcdPowerOff.addActionListener(new ControllerPanelEx.ControllerPanelAction());
      temp.add(lcdPowerOn);
      temp.add(lcdPowerOff);
      return temp;
   }

   private JPanel createBarcodeIlluminationPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1, 5, 10));
      temp.setBorder(new TitledBorder("Barcode Illumination"));
      JLabel barcodeIlluminationLevelLabel = this.createLabel("Level(0-255): ");
      this.barcodeIlluminationLevelField = new JTextField(this.controller.getBarcodeIlluminationLevel(), 3);
      this.barcodeIlluminationLevelField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      JPanel levelPanel = new JPanel(new BorderLayout());
      levelPanel.add(barcodeIlluminationLevelLabel, "West");
      levelPanel.add(this.barcodeIlluminationLevelField, "East");
      this.focusableTextField.add("barcodeIlluminationLevelField");
      this.textFieldHash.put("barcodeIlluminationLevelField", this.barcodeIlluminationLevelField);
      JButton barcodeIlluminationLevelOn = this.createButton("On", "ControllerEx barcodeIlluminationLevelOn", this.controllerAction);
      JButton barcodeIlluminationLevelOff = this.createButton("Off", "ControllerEx barcodeIlluminationLevelOff");
      temp.add(levelPanel);
      temp.add(barcodeIlluminationLevelOn);
      temp.add(barcodeIlluminationLevelOff);
      return temp;
   }

   private JPanel createStatusBitsPanel() {
      JPanel outside = new JPanel(new BorderLayout());
      outside.setBorder(new TitledBorder("Status Bit"));
      JPanel temp = new JPanel(new GridLayout(0, 2, 0, 5));
      JLabel statusBitLockLabel = this.createLabel("Lock : ", 4);
      JTextField statusBitLockField = new JTextField(this.controller.getStatusBitLock(), 3);
      JLabel statusBitOpenLabel = this.createLabel("Open : ", 4);
      JTextField statusBitOpenField = new JTextField(this.controller.getStatusBitOpen());
      JLabel statusBitClampedLabel = this.createLabel("Clamped : ", 4);
      JTextField statusBitOpenClampedField = new JTextField(this.controller.getStatusBitClamped());
      JLabel statusBitClosedLabel = this.createLabel("Closed : ", 4);
      JTextField statusBitClosedField = new JTextField(this.controller.getStatusBitClosed());
      JLabel statusBitCase1Label = this.createLabel("Case 1 : ", 4);
      JTextField statusBitCase1Field = new JTextField(this.controller.getStatusBitCase1());
      JLabel statusBitCase2Label = this.createLabel("Case 2 : ", 4);
      JTextField statusBitCase2Field = new JTextField(this.controller.getStatusBitCase2());
      JLabel statusBitLCDPowerLabel = this.createLabel("LCD Power : ", 4);
      JTextField statusBitLCDPowerField = new JTextField(this.controller.getStatusBitLCDPower());
      JLabel statusBitClearLabel = this.createLabel("Clear :  ", 4);
      JTextField statusBitClearField = new JTextField(this.controller.getStatusBitClear());
      JLabel statusBitEjectLabel = this.createLabel("Eject : ", 4);
      JTextField statusBitEjectField = new JTextField(this.controller.getStatusBitEject());
      JLabel statusBitCaseInLabel = this.createLabel("Case In : ", 4);
      JTextField statusBitCaseInField = new JTextField(this.controller.getStatusBitCaseIn());
      JLabel statusBitInputLabel = this.createLabel("Input : ", 4);
      JTextField statusBitInputField = new JTextField(this.controller.getStatusBitInput());
      JButton statusBitStatus = this.createButton("Get Status", "ControllerEx statusBitGet");
      statusBitLockField.setEditable(false);
      statusBitOpenField.setEditable(false);
      statusBitOpenClampedField.setEditable(false);
      statusBitClosedField.setEditable(false);
      statusBitCase1Field.setEditable(false);
      statusBitCase2Field.setEditable(false);
      statusBitLCDPowerField.setEditable(false);
      statusBitClearField.setEditable(false);
      statusBitEjectField.setEditable(false);
      statusBitCaseInField.setEditable(false);
      statusBitInputField.setEditable(false);
      statusBitLockField.setEditable(false);
      this.textFieldHash.put("statusBitLockField", statusBitLockField);
      this.textFieldHash.put("statusBitOpenField", statusBitOpenField);
      this.textFieldHash.put("statusBitOpenClampedField", statusBitOpenClampedField);
      this.textFieldHash.put("statusBitClosedField", statusBitClosedField);
      this.textFieldHash.put("statusBitCase1Field", statusBitCase1Field);
      this.textFieldHash.put("statusBitCase2Field", statusBitCase2Field);
      this.textFieldHash.put("statusBitLCDPowerField", statusBitLCDPowerField);
      this.textFieldHash.put("statusBitClearField", statusBitClearField);
      this.textFieldHash.put("statusBitEjectField", statusBitEjectField);
      this.textFieldHash.put("statusBitCaseInField", statusBitCaseInField);
      this.textFieldHash.put("statusBitInputField", statusBitInputField);
      temp.add(statusBitLockLabel);
      temp.add(statusBitLockField);
      temp.add(statusBitOpenLabel);
      temp.add(statusBitOpenField);
      temp.add(statusBitClampedLabel);
      temp.add(statusBitOpenClampedField);
      temp.add(statusBitClosedLabel);
      temp.add(statusBitClosedField);
      temp.add(statusBitCase1Label);
      temp.add(statusBitCase1Field);
      temp.add(statusBitCase2Label);
      temp.add(statusBitCase2Field);
      temp.add(statusBitLCDPowerLabel);
      temp.add(statusBitLCDPowerField);
      temp.add(statusBitClearLabel);
      temp.add(statusBitClearField);
      temp.add(statusBitEjectLabel);
      temp.add(statusBitEjectField);
      temp.add(statusBitCaseInLabel);
      temp.add(statusBitCaseInField);
      temp.add(statusBitInputLabel);
      temp.add(statusBitInputField);
      outside.add(temp, "North");
      outside.add(statusBitStatus, "Center");
      return outside;
   }

   private JPanel createEnablePanel() {
      JPanel temp = new JPanel(new GridLayout(0, 2, 5, 10));
      JPanel left = new JPanel(new GridLayout(0, 1, 5, 10));
      JPanel right = new JPanel(new BorderLayout());
      temp.setBorder(new TitledBorder("Enable"));
      JButton enableEnableServo = this.createButton("Enable Servo", "ControllerEx enableServoEnable");
      JButton enableDisableServo = this.createButton("Disable Servo", "ControllerEx disableServoEnable");
      this.buttonToBeDisable.put("enableDisableServo", enableDisableServo);
      enableDisableServo.setEnabled(false);
      left.add(enableEnableServo);
      left.add(enableDisableServo);
      JButton enableHardStop = this.createButton("Hard Stop", "ControllerEx enableHardStop");
      enableHardStop.setFont(this.boldButton);
      right.add(enableHardStop, "Center");
      temp.add(left);
      temp.add(right);
      return temp;
   }

   private JPanel createHomeCarouselPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 2, 5, 10));
      temp.setBorder(new TitledBorder("Home Carousel"));
      JPanel homeCarouselLight = new JPanel(new BorderLayout());
      homeCarouselLight.setPreferredSize(new Dimension(80, 50));
      homeCarouselLight.setMaximumSize(new Dimension(80, 50));
      homeCarouselLight.setBackground(Color.GREEN);
      JLabel homeCarouselStatus = new JLabel("Homing " + this.controller.getHomingCarouselStatus());
      homeCarouselStatus.setHorizontalAlignment(0);
      homeCarouselLight.add(homeCarouselStatus, "Center");
      JButton homeCarouselStart = this.createButton("Start", "ControllerEx homeCarouselStart");
      this.buttonToBeDisable.put("homeCarouselStart", homeCarouselStart);
      homeCarouselStart.setEnabled(false);
      temp.add(homeCarouselLight);
      temp.add(homeCarouselStart);
      return temp;
   }

   private JPanel createSlotMovementPanel() {
      JPanel temp = new JPanel();
      temp.setBorder(new TitledBorder("Slot movement"));
      JLabel slotMovementOffsetLabel = this.createLabel("Slot 1 offset: ");
      JTextField slotMovementOffsetField = new JTextField(this.controller.getSlotMovementOffset(), 5);
      slotMovementOffsetField.setEditable(false);
      slotMovementOffsetLabel.setHorizontalAlignment(4);
      JLabel slotMovementNoLabel = this.createLabel("Slot No: ");
      this.slotMovementNoField = new JTextField(this.controller.getSlotMovementNo(), 3);
      this.slotMovementNoField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      slotMovementNoLabel.setHorizontalAlignment(4);
      JPanel topPanel = new JPanel();
      topPanel.add(slotMovementOffsetLabel);
      topPanel.add(slotMovementOffsetField);
      topPanel.add(slotMovementNoLabel);
      topPanel.add(this.slotMovementNoField);
      this.textFieldHash.put("slotMovementNoField", this.slotMovementNoField);
      this.focusableTextField.add("slotMovementNoField");
      JLabel slotMovementBayLabel = this.createLabel("Bay: ");
      JTextField slotMovementBayValue = new JTextField(this.controller.getSlotMovementBay());
      slotMovementBayValue.setEditable(false);
      slotMovementBayLabel.setHorizontalAlignment(4);
      JLabel slotMovementBaySlotLabel = this.createLabel("Bay Slot: ");
      JTextField slotMovementBaySlotValue = new JTextField(this.controller.getSlotMovementSlot());
      slotMovementBaySlotValue.setEditable(false);
      slotMovementBaySlotLabel.setHorizontalAlignment(4);
      JLabel slotMovementPositionLabel = this.createLabel("Position: ");
      JTextField slotMovementPositionValue = new JTextField(this.controller.getSlotMovementPosition());
      slotMovementPositionValue.setEditable(false);
      slotMovementPositionLabel.setHorizontalAlignment(4);
      JPanel smallPanel = new JPanel(new GridLayout(0, 2));
      this.textFieldHash.put("slotMovementBayValue", slotMovementBayValue);
      this.textFieldHash.put("slotMovementBaySlotValue", slotMovementBaySlotValue);
      this.textFieldHash.put("slotMovementPositionValue", slotMovementPositionValue);
      smallPanel.add(slotMovementBayLabel);
      smallPanel.add(slotMovementBayValue);
      smallPanel.add(slotMovementBaySlotLabel);
      smallPanel.add(slotMovementBaySlotValue);
      smallPanel.add(slotMovementPositionLabel);
      smallPanel.add(slotMovementPositionValue);
      JButton slotMovementGo = this.createButton("Go", "ControllerEx slotMovementGo", this.controllerAction);
      this.buttonToBeDisable.put("slotMovementGo", slotMovementGo);
      slotMovementGo.setEnabled(false);
      JPanel bottomPanel = new JPanel(new GridLayout(0, 2));
      bottomPanel.add(smallPanel);
      bottomPanel.add(slotMovementGo);
      temp.add(topPanel);
      temp.add(bottomPanel);
      return temp;
   }

   private JPanel createServoMotorStatusPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 2));
      temp.setBorder(new TitledBorder("Servo Motor Status"));
      JPanel leftPanel = new JPanel();
      JLabel servoMotorStautsPositionLabel = this.createLabel("Position : ");
      JTextField servoMotorStatusPositionField = new JTextField(this.controller.getServoMotorStatusPosition(), 5);
      servoMotorStatusPositionField.setEditable(false);
      this.serverMotorStatusUpdateContinuously = new JCheckBox("Update Continuously");
      this.serverMotorStatusUpdateContinuously.setActionCommand("ControllerEx updateContinuously");
      this.serverMotorStatusUpdateContinuously.addActionListener(this.controllerAction);
      this.textFieldHash.put("servoMotorStatusPositionField", servoMotorStatusPositionField);
      this.focusableTextField.add("servoMotorStatusPositionField");
      leftPanel.add(servoMotorStautsPositionLabel);
      leftPanel.add(servoMotorStatusPositionField);
      leftPanel.add(this.serverMotorStatusUpdateContinuously);
      JPanel rightPanel = new JPanel(new GridLayout(0, 1, 5, 5));
      JButton servoMotorStatusGetPosition = this.createButton("Get Position", "ControllerEx servoMotorGetPosition");
      JButton servoMotorStatusClear = this.createButton("Clear", "ControllerEx servoMotorClear");
      rightPanel.add(servoMotorStatusGetPosition);
      rightPanel.add(servoMotorStatusClear);
      temp.add(leftPanel);
      temp.add(rightPanel);
      return temp;
   }

   private JPanel createMoveCarouselPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1));
      temp.setBorder(new TitledBorder("Move Carousel"));
      JLabel moveCarouselPositionLabel = this.createLabel("Position : ");
      moveCarouselPositionLabel.setHorizontalAlignment(4);
      this.moveCarouselPositionField = new JTextField(this.controller.getMoveCarouselPositon(), 3);
      JLabel moveCarouselVelocityLabel = this.createLabel("Velocity : ");
      moveCarouselVelocityLabel.setHorizontalAlignment(4);
      this.moveCarouselVelocityField = new JTextField(this.controller.getMoveCarouselVelocity(), 5);
      JLabel moveCarouselAccelerationLabel = this.createLabel("Acceleration : ");
      moveCarouselAccelerationLabel.setHorizontalAlignment(4);
      this.moveCarouselAccelerationField = new JTextField(this.controller.getMoveCarouselAcceleration(), 3);
      this.moveCarouselPositionField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      this.moveCarouselVelocityField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      this.moveCarouselAccelerationField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      this.textFieldHash.put("moveCarouselPositionField", this.moveCarouselPositionField);
      this.textFieldHash.put("moveCarouselVelocityField", this.moveCarouselVelocityField);
      this.textFieldHash.put("moveCarouselAccelerationField", this.moveCarouselAccelerationField);
      this.focusableTextField.add("moveCarouselPositionField");
      this.focusableTextField.add("moveCarouselVelocityField");
      this.focusableTextField.add("moveCarouselAccelerationField");
      JPanel fieldPanel = new JPanel(new GridLayout(0, 2, 5, 5));
      fieldPanel.add(moveCarouselPositionLabel);
      fieldPanel.add(this.moveCarouselPositionField);
      fieldPanel.add(moveCarouselVelocityLabel);
      fieldPanel.add(this.moveCarouselVelocityField);
      fieldPanel.add(moveCarouselAccelerationLabel);
      fieldPanel.add(this.moveCarouselAccelerationField);
      JButton moveCarouselPosition = this.createButton("Move to Position", "ControllerEx moveCarouselMove", this.controllerAction);
      JButton moveCarouselStartVelocity = this.createButton("Start Velocity", "ControllerEx moveCarouselVelocity", this.controllerAction);
      JButton moveCarouselStop = this.createButton("Stop", "ControllerEx moveCarouselStop");
      this.buttonToBeDisable.put("moveCarouselPosition", moveCarouselPosition);
      this.buttonToBeDisable.put("moveCarouselStartVelocity", moveCarouselStartVelocity);
      this.buttonToBeDisable.put("moveCarouselStop", moveCarouselStop);
      moveCarouselPosition.setEnabled(false);
      moveCarouselStartVelocity.setEnabled(false);
      moveCarouselStop.setEnabled(false);
      moveCarouselStop.setFont(this.boldButton);
      JPanel smallPanel = new JPanel(new GridLayout(0, 1, 5, 5));
      smallPanel.add(moveCarouselPosition);
      smallPanel.add(moveCarouselStartVelocity);
      JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 5, 5));
      buttonPanel.add(smallPanel);
      buttonPanel.add(moveCarouselStop);
      temp.add(fieldPanel);
      temp.add(buttonPanel);
      return temp;
   }

   public void showPowerOffDialog() {
      JDialog d = new JDialog();
      d.setSize(700, 150);
      JLabel l = new JLabel("Are you sure you want to turn off the LCD Power?  You won't be able to see the LCD display after you turn off the power?");
      d.getContentPane().setLayout(new BorderLayout());
      d.getContentPane().add(l, "Center");
      JButton yes = new JButton("Yes");
      yes.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            d.setVisible(false);
            d.dispose();
         }
      });

      JButton no = new JButton("No");
      no.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            d.setVisible(false);
            d.dispose();
         }
      });

      JPanel p = new JPanel();
      p.add(yes);
      p.add(no);
      d.getContentPane().add(p, "South");
      d.setBounds(50, 250, 700, 150);
      d.setVisible(true);
   }

   private class ControllerPanelAction implements ActionListener {
      private ControllerPanelAction() {
      }

      public void actionPerformed(ActionEvent ev) {
         ControllerPanelEx.this.command = ev.getActionCommand();
         ControllerPanelEx.this.data = "";
         Aem.logDetailMessage(DvdplayLevel.FINER, "[ControllerPanelEx] Action " + ControllerPanelEx.this.command);
         if (ControllerPanelEx.this.command.equals("ControllerEx lcdPowerOff")) {
            ControllerPanelEx.this.showPowerOffDialog();
         }

         if (ControllerPanelEx.this.command.equals("ControllerEx slotMovementGo")) {
            ControllerPanelEx.this.data = ControllerPanelEx.this.slotMovementNoField.getText();
            ControllerPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "ControllerEx slotMovementGo " + ControllerPanelEx.this.data));
         }

         if (ControllerPanelEx.this.command.equals("ControllerEx moveCarouselMove")) {
            ControllerPanelEx.this.data = ControllerPanelEx.this.moveCarouselPositionField.getText()
               + ","
               + ControllerPanelEx.this.moveCarouselVelocityField.getText()
               + ","
               + ControllerPanelEx.this.moveCarouselAccelerationField.getText();
            ControllerPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "ControllerEx moveCarouselMove " + ControllerPanelEx.this.data));
         }

         if (ControllerPanelEx.this.command.equals("ControllerEx moveCarouselVelocity")) {
            ControllerPanelEx.this.data = ControllerPanelEx.this.moveCarouselVelocityField.getText()
               + ","
               + ControllerPanelEx.this.moveCarouselAccelerationField.getText();
            ControllerPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "ControllerEx moveCarouselVelocity " + ControllerPanelEx.this.data));
         }

         if (ControllerPanelEx.this.command.equals("ControllerEx barcodeIlluminationLevelOn")) {
            ControllerPanelEx.this.data = ControllerPanelEx.this.barcodeIlluminationLevelField.getText();
            ControllerPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "ControllerEx barcodeIlluminationLevelOn " + ControllerPanelEx.this.data));
         }

         if (ControllerPanelEx.this.command.equals("ControllerEx updateContinuously")) {
            ControllerPanelEx.this.data = ControllerPanelEx.this.serverMotorStatusUpdateContinuously.isSelected() ? "true" : "false";
            ControllerPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "ControllerEx updateContinuously " + ControllerPanelEx.this.data));
         }
      }
   }
}
