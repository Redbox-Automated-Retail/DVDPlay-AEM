package net.dvdplay.screen;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.ServoParamsEx;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.KeyboardAssembler;
import net.dvdplay.view.Utility;

public class ServoParamsPanelEx extends ServoParamsPanel {
   JPanel sharedParametersPanel;
   JPanel servoParametersUnderThresholdPanel;
   JPanel servoParametersOverThresholdPanel;
   JPanel miscParamsPanel;
   JPanel buttonPanel;
   JPanel statusPanel;
   ActionListener al;
   ServoParamsPanelEx.ServoParamsAction servoParamsAction;
   ServoParamsEx servoParams;
   JRadioButton reflectiveSensor;
   JRadioButton interruptSensor;

   public ServoParamsPanelEx() {
      try {
         this.setLayout(null);
         ServoParamsEx.init();
         this.al = new AbstractToolsPanel.ActionTools();
         this.servoParamsAction = new ServoParamsPanelEx.ServoParamsAction();
         this.servoParams = new ServoParamsEx(this.al, this);
         this.focusableTextField = new ArrayList();
         this.buttonPanel = this.createButtonPanel();
         this.sharedParametersPanel = this.createSharedParametersPanel();
         this.servoParametersUnderThresholdPanel = this.createServoParametersUnderThresholdPanel();
         this.servoParametersOverThresholdPanel = this.createServoParametersOverThresholdPanel();
         this.miscParamsPanel = this.createMiscParamsPanel();
         this.statusPanel = Utility.createStatusDisplayPanel("Status", 10, 18);
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.sharedParametersPanel.setBounds(10, 10, 320, 130);
         this.servoParametersUnderThresholdPanel.setBounds(10, 150, 320, 340);
         this.servoParametersOverThresholdPanel.setBounds(340, 150, 320, 340);
         this.miscParamsPanel.setBounds(340, 10, 320, 130);
         this.buttonPanel.setBounds(350, 490, 800, 40);
         this.statusPanel.setBounds(670, 10, 280, 320);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.sharedParametersPanel);
         this.add(this.servoParametersUnderThresholdPanel);
         this.add(this.servoParametersOverThresholdPanel);
         this.add(this.buttonPanel);
         this.add(this.miscParamsPanel);
         this.add(this.statusPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[ServoParamsPanelEx] " + var2.toString(), var2);
      }
   }

   private JPanel createSharedParametersPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 1));
      temp.setBorder(new TitledBorder("Shared Parameters"));
      JPanel slotOneOffset = this.createRow("Slot One Offset : ", 12, "slotOneOffset", ServoParamsEx.getSlotOneOffset(), true);
      JPanel inputStep = this.createRow("Input Step : ", 12, "inputStep", ServoParamsEx.getInputStep(), true);
      JPanel outputStep = this.createRow("Output Step : ", 12, "outputStep", ServoParamsEx.getOutputStep(), true);
      JPanel discThreshold = this.createRow("Disc Threshold : ", 12, "discThreshold", ServoParamsEx.getDiscThreshold(), true);
      this.focusableTextField.add("slotOneOffset");
      this.focusableTextField.add("inputStep");
      this.focusableTextField.add("outputStep");
      this.focusableTextField.add("discThreshold");
      temp.add(slotOneOffset);
      temp.add(inputStep);
      temp.add(outputStep);
      temp.add(discThreshold);
      return temp;
   }

   private JPanel createButtonPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new FlowLayout());
      JButton set = this.createButton("Set Parameters", "verifyAndSet", this.servoParamsAction);
      JButton saveToDisk = this.createButton("Save To Disk", "verifyAndSaveToDisk", this.servoParamsAction);
      JButton saveToServer = this.createButton("Save To Server", "verifyAndSaveToServer", this.servoParamsAction);
      temp.add(set);
      temp.add(saveToDisk);
      temp.add(saveToServer);
      return temp;
   }

   private JPanel createServoParametersUnderThresholdPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 1));
      temp.setBorder(new TitledBorder("Servo Parameters Under Threshold"));
      JPanel kp = this.createRow("Kp : ", 12, "kpUnder", ServoParamsEx.getKpUnder(), true);
      JPanel kd = this.createRow("Kd : ", 12, "kdUnder", ServoParamsEx.getKdUnder(), true);
      JPanel ki = this.createRow("Ki : ", 12, "kiUnder", ServoParamsEx.getKiUnder(), true);
      JPanel integrationLimit = this.createRow("Integration Limit : ", 12, "integrationLimitUnder", ServoParamsEx.getIntegrationLimitUnder(), true);
      JLabel space1 = new JLabel("              ");
      JPanel outputLimit = this.createRow("Output Limit : ", 12, "outputLimitUnder", ServoParamsEx.getOutputLimitUnder(), true);
      JPanel currentLimit = this.createRow("Current Limit : ", 12, "currentLimitUnder", ServoParamsEx.getCurrentLimitUnder(), true);
      JPanel positionErrorLimit = this.createRow("Position Error Limit : ", 12, "positionErrorLimitUnder", ServoParamsEx.getPositionErrorLimitUnder(), true);
      JLabel space2 = new JLabel("              ");
      JPanel servoRate = this.createRow("Servo Rate : ", 12, "servoRateUnder", ServoParamsEx.getServoRateUnder(), true);
      JPanel deadbandComp = this.createRow("Deadband Comp : ", 12, "deadbandCompUnder", ServoParamsEx.getDeadbandCompUnder(), true);
      JLabel space3 = new JLabel("              ");
      JPanel velocity = this.createRow("Velocity : ", 12, "velocityUnder", ServoParamsEx.getVelocityUnder(), true);
      JPanel acceleration = this.createRow("Accerleration : ", 12, "accelerationUnder", ServoParamsEx.getAccelerationUnder(), true);
      this.focusableTextField.add("kpUnder");
      this.focusableTextField.add("kdUnder");
      this.focusableTextField.add("kiUnder");
      this.focusableTextField.add("integrationLimitUnder");
      this.focusableTextField.add("outputLimitUnder");
      this.focusableTextField.add("currentLimitUnder");
      this.focusableTextField.add("positionErrorLimitUnder");
      this.focusableTextField.add("servoRateUnder");
      this.focusableTextField.add("deadbandCompUnder");
      this.focusableTextField.add("velocityUnder");
      this.focusableTextField.add("accelerationUnder");
      temp.add(kp);
      temp.add(kd);
      temp.add(ki);
      temp.add(integrationLimit);
      temp.add(space1);
      temp.add(outputLimit);
      temp.add(currentLimit);
      temp.add(positionErrorLimit);
      temp.add(space2);
      temp.add(servoRate);
      temp.add(deadbandComp);
      temp.add(space3);
      temp.add(velocity);
      temp.add(acceleration);
      return temp;
   }

   private JPanel createServoParametersOverThresholdPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 1));
      temp.setBorder(new TitledBorder("Servo Parameters Over Threshold"));
      JPanel kp = this.createRow("Kp : ", 12, "kpOver", ServoParamsEx.getKpOver(), true);
      JPanel kd = this.createRow("Kd : ", 12, "kdOver", ServoParamsEx.getKdOver(), true);
      JPanel ki = this.createRow("Ki : ", 12, "kiOver", ServoParamsEx.getKiOver(), true);
      JPanel integrationLimit = this.createRow("Integration Limit : ", 12, "integrationLimitOver", ServoParamsEx.getIntegrationLimitOver(), true);
      JLabel space1 = new JLabel("             ");
      JPanel outputLimit = this.createRow("Output Limit : ", 12, "outputLimitOver", ServoParamsEx.getOutputLimitOver(), true);
      JPanel currentLimit = this.createRow("Current Limit : ", 12, "currentLimitOver", ServoParamsEx.getCurrentLimitOver(), true);
      JPanel positionErrorLimit = this.createRow("Position Error Limit : ", 12, "positionErrorLimitOver", ServoParamsEx.getPositionErrorLimitOver(), true);
      JLabel space2 = new JLabel("             ");
      JPanel servoRate = this.createRow("Servo Rate : ", 12, "servoRateOver", ServoParamsEx.getServoRateOver(), true);
      JPanel deadbandComp = this.createRow("Deadband Comp : ", 12, "deadbandCompOver", ServoParamsEx.getDeadbandCompOver(), true);
      JLabel space3 = new JLabel("             ");
      JPanel velocity = this.createRow("Velocity : ", 12, "velocityOver", ServoParamsEx.getVelocityOver(), true);
      JPanel acceleration = this.createRow("Accerleration : ", 12, "accelerationOver", ServoParamsEx.getAccelerationOver(), true);
      this.focusableTextField.add("kpOver");
      this.focusableTextField.add("kdOver");
      this.focusableTextField.add("kiOver");
      this.focusableTextField.add("integrationLimitOver");
      this.focusableTextField.add("outputLimitOver");
      this.focusableTextField.add("currentLimitOver");
      this.focusableTextField.add("positionErrorLimitOver");
      this.focusableTextField.add("servoRateOver");
      this.focusableTextField.add("deadbandCompOver");
      this.focusableTextField.add("velocityOver");
      this.focusableTextField.add("accelerationOver");
      temp.add(kp);
      temp.add(kd);
      temp.add(ki);
      temp.add(integrationLimit);
      temp.add(space1);
      temp.add(outputLimit);
      temp.add(currentLimit);
      temp.add(positionErrorLimit);
      temp.add(space2);
      temp.add(servoRate);
      temp.add(deadbandComp);
      temp.add(space3);
      temp.add(velocity);
      temp.add(acceleration);
      return temp;
   }

   private JPanel createMiscParamsPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new BoxLayout(temp, 1));
      temp.setBorder(new TitledBorder("Misc. Params"));
      JPanel armEjectWaitTime = this.createRow("ArmEjectWaitTime : ", 12, "armEjectWaitTime", ServoParamsEx.getArmEjectWaitTime(), true);
      JPanel servoMoveToOffsetWaitTime = this.createRow(
         "ServoMoveToOffsetWaitTime : ", 12, "servoMoveToOffsetWaitTime", ServoParamsEx.getServoMoveToOffsetWaitTime(), true, new Font("Arial", 0, 12)
      );
      JPanel servoMoveToOffsetTimeout = this.createRow(
         "ServoMoveToOffsetTimeout : ", 12, "servoMoveToOffsetTimeout", ServoParamsEx.getServoMoveToOffsetTimeout(), true, new Font("Arial", 0, 12)
      );
      this.focusableTextField.add("armEjectWaitTime");
      this.focusableTextField.add("servoMoveToOffsetWaitTime");
      this.focusableTextField.add("servoMoveToOffsetTimeout");
      temp.add(armEjectWaitTime);
      temp.add(servoMoveToOffsetWaitTime);
      temp.add(servoMoveToOffsetTimeout);
      ButtonGroup sensorGroup = new ButtonGroup();
      sensorGroup.add(this.reflectiveSensor);
      sensorGroup.add(this.interruptSensor);
      return temp;
   }

   private class ServoParamsAction implements ActionListener {
      private ServoParamsAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         ServoParamsPanelEx.this.command = ae.getActionCommand();
         ServoParamsPanelEx.this.data = "";
         Aem.logDetailMessage(DvdplayLevel.FINER, "[ServoParamsPanelEx] Action " + ServoParamsPanelEx.this.command);
         if (ServoParamsPanelEx.this.command.startsWith("verifyAnd")) {
            Object verifyResult = ServoParamsPanelEx.this.verifyEntry();
            if (verifyResult instanceof String) {
               ServoParamsPanelEx.this.data = (String)verifyResult;
               if (ServoParamsPanelEx.this.command.startsWith("verifyAndSave")) {
                  ServoParamsPanelEx.this.data = this.gatherSaveData();
                  ServoParamsPanelEx.this.al
                     .actionPerformed(new ActionEvent(this, 1001, "ServoParamsEx " + ServoParamsPanelEx.this.command + " " + ServoParamsPanelEx.this.data));
               } else if (ServoParamsPanelEx.this.command.equals("verifyAndSet")) {
                  ServoParamsPanelEx.this.data = this.gatherSetData();
                  ServoParamsPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "ServoParamsEx set " + ServoParamsPanelEx.this.data));
               }
            }
         }
      }

      private String gatherSetData() {
         new String();
         String temp = this.getValueFromHash("kpUnder") + "," + this.getValueFromHash("kdUnder") + ",";
         temp = temp
            + this.getValueFromHash("kiUnder")
            + ","
            + this.getValueFromHash("integrationLimitUnder")
            + ","
            + this.getValueFromHash("outputLimitUnder")
            + ",";
         temp = temp
            + this.getValueFromHash("currentLimitUnder")
            + ","
            + this.getValueFromHash("positionErrorLimitUnder")
            + ","
            + this.getValueFromHash("servoRateUnder")
            + ",";
         temp = temp
            + this.getValueFromHash("deadbandCompUnder")
            + ","
            + this.getValueFromHash("velocityUnder")
            + ","
            + this.getValueFromHash("accelerationUnder")
            + ",";
         temp = temp + this.getValueFromHash("kpOver") + "," + this.getValueFromHash("kdOver") + ",";
         temp = temp
            + this.getValueFromHash("kiOver")
            + ","
            + this.getValueFromHash("integrationLimitOver")
            + ","
            + this.getValueFromHash("outputLimitOver")
            + ",";
         temp = temp
            + this.getValueFromHash("currentLimitOver")
            + ","
            + this.getValueFromHash("positionErrorLimitOver")
            + ","
            + this.getValueFromHash("servoRateOver")
            + ",";
         return temp
            + this.getValueFromHash("deadbandCompOver")
            + ","
            + this.getValueFromHash("velocityOver")
            + ","
            + this.getValueFromHash("accelerationOver")
            + ",";
      }

      private String gatherSaveData() {
         new String();
         String temp = this.getValueFromHash("slotOneOffset") + "," + this.getValueFromHash("inputStep") + "," + this.getValueFromHash("outputStep") + ",";
         temp = temp + this.getValueFromHash("discThreshold") + "," + this.getValueFromHash("kpUnder") + "," + this.getValueFromHash("kdUnder") + ",";
         temp = temp
            + this.getValueFromHash("kiUnder")
            + ","
            + this.getValueFromHash("integrationLimitUnder")
            + ","
            + this.getValueFromHash("outputLimitUnder")
            + ",";
         temp = temp
            + this.getValueFromHash("currentLimitUnder")
            + ","
            + this.getValueFromHash("positionErrorLimitUnder")
            + ","
            + this.getValueFromHash("servoRateUnder")
            + ",";
         temp = temp
            + this.getValueFromHash("deadbandCompUnder")
            + ","
            + this.getValueFromHash("velocityUnder")
            + ","
            + this.getValueFromHash("accelerationUnder")
            + ",";
         temp = temp + this.getValueFromHash("kpOver") + "," + this.getValueFromHash("kdOver") + ",";
         temp = temp
            + this.getValueFromHash("kiOver")
            + ","
            + this.getValueFromHash("integrationLimitOver")
            + ","
            + this.getValueFromHash("outputLimitOver")
            + ",";
         temp = temp
            + this.getValueFromHash("currentLimitOver")
            + ","
            + this.getValueFromHash("positionErrorLimitOver")
            + ","
            + this.getValueFromHash("servoRateOver")
            + ",";
         temp = temp
            + this.getValueFromHash("deadbandCompOver")
            + ","
            + this.getValueFromHash("velocityOver")
            + ","
            + this.getValueFromHash("accelerationOver")
            + ",";
         return temp
            + this.getValueFromHash("armEjectWaitTime")
            + ","
            + this.getValueFromHash("servoMoveToOffsetWaitTime")
            + ","
            + this.getValueFromHash("servoMoveToOffsetTimeout")
            + ",";
      }

      private String getValueFromHash(String key) {
         JTextField jtf = (JTextField)ServoParamsPanelEx.this.textFieldHash.get(key);
         return jtf.getText();
      }
   }
}
