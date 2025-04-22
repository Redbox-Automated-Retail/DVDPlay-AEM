package net.dvdplay.screen;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.CycleTest;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.KeyboardAssembler;

public class CycleTestPanel extends AbstractToolsPanel {
   private JPanel instructionDisplay;
   private JPanel buttonPanel;
   private JPanel statusDisplay;
   CycleTest cycleTest;
   CycleTestPanel.CycleTestAction cycleTestAction;
   JPanel slotOneOffset;
   JPanel startAtSlot;
   ActionListener al;
   JCheckBox stopOnError;
   JRadioButton forwardsBackwards;
   JRadioButton random;

   public CycleTestPanel() {
      try {
         this.setLayout(null);
         this.cycleTestAction = new CycleTestPanel.CycleTestAction();
         this.al = new AbstractToolsPanel.ActionTools();
         this.focusableTextField = new ArrayList();
         this.cycleTest = new CycleTest(this.al, this);
         this.buttonPanel = this.createButtonPanel();
         this.instructionDisplay = this.createInstructionDisplay();
         this.statusDisplay = this.createStatusDisplayPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.instructionDisplay.setBounds(10, 10, 600, 130);
         this.buttonPanel.setBounds(10, 150, 900, 80);
         this.statusDisplay.setBounds(10, 230, 960, 300);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.instructionDisplay);
         this.add(this.statusDisplay);
         this.add(this.buttonPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[CycleTestPanel] " + var2.toString(), var2);
      }
   }

   public JPanel createInstructionDisplay() {
      JPanel temp = new JPanel();
      String content = CycleTest.getInstruction();
      JTextArea instruction = new JTextArea(content, 5, 20);
      temp.setBorder(new TitledBorder("Instruction"));
      temp.add(instruction);
      return temp;
   }

   public JPanel createButtonPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 4, 15, 5));
      JPanel initPanel = new JPanel(new GridLayout(0, 1));
      JPanel slotField = new JPanel(new GridLayout(2, 1));
      JPanel cycleMode = new JPanel(new GridLayout(0, 1));
      JPanel button = new JPanel(new GridLayout(0, 2));
      JButton init = this.createButton("Init", "CycleTest init");
      initPanel.add(init);
      this.slotOneOffset = this.createRow("Slot one offset : ", 5, "slotOneOffset", CycleTest.getSlotOneOffset(), false);
      this.startAtSlot = this.createRow("Start at slot : ", 5, "startAtSlot", CycleTest.getStartAtSlot(), true);
      slotField.add(this.slotOneOffset);
      slotField.add(this.startAtSlot);
      this.focusableTextField.add("slotOneOffset");
      this.focusableTextField.add("startAtSlot");
      this.forwardsBackwards = new JRadioButton("Forwards/Backwards", CycleTest.getForwardBackward());
      this.forwardsBackwards.addActionListener(this.cycleTestAction);
      this.random = new JRadioButton("Random", CycleTest.getRandom());
      this.random.addActionListener(this.cycleTestAction);
      ButtonGroup cycleGroup = new ButtonGroup();
      cycleGroup.add(this.forwardsBackwards);
      cycleGroup.add(this.random);
      cycleMode.setBorder(new TitledBorder("Cycle Mode"));
      cycleMode.add(this.forwardsBackwards);
      cycleMode.add(this.random);
      JButton go = this.createButton("Go", "VerifyAndGo", this.cycleTestAction);
      go.setEnabled(false);
      JButton stop = this.createButton("Stop", "CycleTest stop");
      this.stopOnError = new JCheckBox("Stop on error");
      this.stopOnError.setActionCommand("stopOnError");
      button.add(go);
      button.add(stop);
      button.add(this.stopOnError);
      temp.add(initPanel);
      temp.add(slotField);
      temp.add(cycleMode);
      temp.add(button);
      return temp;
   }

   public JPanel createStatusDisplayPanel() {
      JPanel temp = new JPanel();
      JTextArea status = new JTextArea(15, 85);
      status.setEditable(false);
      status.setWrapStyleWord(true);
      JScrollPane jsp = new JScrollPane(status, 20, 30);
      temp.setBorder(new TitledBorder("Status"));
      temp.add(jsp);
      return temp;
   }

   private class CycleTestAction implements ActionListener {
      private CycleTestAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         String command = ae.getActionCommand();
         Aem.logDetailMessage(DvdplayLevel.FINER, "[CycleTestPanel] Action " + command);

         if (command.equals("VerifyAndGo")) {
            Object verifyResult = verifyEntry();
            if (verifyResult instanceof String) {
               data = ((JTextField) textFieldHash.get("startAtSlot")).getText() + ",";
               data += ((JTextField) textFieldHash.get("slotOneOffset")).getText() + ",";
               data += forwardsBackwards.isSelected() ? "true," : "false,";
               data += random.isSelected() ? "true," : "false,";
               data += stopOnError.isSelected() ? "true" : "false";

               al.actionPerformed(new ActionEvent(this, 1001, "CycleTest go " + data));
            }
         }
      }

   }
}
