package net.dvdplay.screen;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.InventoryCheck;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.KeyboardAssembler;

public class InventoryCheckPanel extends AbstractToolsPanel {
   private JPanel statusDisplay;
   private JPanel buttonPanel;
   private InventoryCheck inventoryCheck;
   private ActionListener al;
   private ActionListener inventoryCheckAction;
   private JTextField fromSlotField;
   private JTextField toSlotField;

   public InventoryCheckPanel() {
      try {
         this.setLayout(null);
         this.al = new AbstractToolsPanel.ActionTools();
         this.inventoryCheck = new InventoryCheck(this.al, this);
         this.inventoryCheckAction = new InventoryCheckPanel.InventoryCheckPanelAction();
         this.focusableTextField = new ArrayList();
         this.buttonPanel = this.createButtonPanel();
         this.statusDisplay = this.createStatusDisplayPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.statusDisplay.setBounds(10, 10, 600, 500);
         this.buttonPanel.setBounds(620, 200, 130, 130);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.statusDisplay);
         this.add(this.buttonPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[InventoryCheckPanel] " + var2.toString(), var2);
      }
   }

   public JPanel createButtonPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1));
      JButton checkSlots = this.createButton("Check Slots", "InventoryCheck checkSlots", this.inventoryCheckAction);
      JLabel space = new JLabel("                   ");
      JButton checkDiscs = this.createButton("Check Discs", "InventoryCheck checkDiscs", this.inventoryCheckAction);
      JLabel fromSlotLabel = this.createLabel("From Slot: ");
      this.fromSlotField = new JTextField("1", 5);
      this.fromSlotField.setEditable(true);
      this.fromSlotField.setHorizontalAlignment(2);
      this.fromSlotField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      JLabel toSlotLabel = this.createLabel("To Slot: ");
      this.toSlotField = new JTextField("" + ServoFactory.getInstance().getNumSlots(), 5);
      this.toSlotField.setEditable(true);
      this.toSlotField.setHorizontalAlignment(2);
      this.toSlotField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      this.textFieldHash.put("toSlotField", this.toSlotField);
      this.textFieldHash.put("fromSlotField", this.fromSlotField);
      this.focusableTextField.add("fromSlotField");
      this.focusableTextField.add("toSlotField");
      temp.add(fromSlotLabel);
      temp.add(this.fromSlotField);
      temp.add(space);
      temp.add(toSlotLabel);
      temp.add(space);
      temp.add(this.toSlotField);
      temp.add(checkSlots);
      temp.add(space);
      temp.add(checkDiscs);
      return temp;
   }

   public JPanel createStatusDisplayPanel() {
      JPanel temp = new JPanel();
      JTextArea status = new JTextArea(30, 50);
      status.setEditable(false);
      status.setWrapStyleWord(true);
      JScrollPane jsp = new JScrollPane(status, 20, 30);
      temp.add(jsp);
      return temp;
   }

   private class InventoryCheckPanelAction implements ActionListener {
      private InventoryCheckPanelAction() {
      }

      public void actionPerformed(ActionEvent ev) {
         InventoryCheckPanel.this.command = ev.getActionCommand();
         InventoryCheckPanel.this.data = "";
         Aem.logDetailMessage(DvdplayLevel.FINER, "[InventoryCheckPanel] Action " + InventoryCheckPanel.this.command);
         if (InventoryCheckPanel.this.command.equals("InventoryCheck checkSlots")) {
            InventoryCheckPanel.this.al
               .actionPerformed(
                  new ActionEvent(
                     this,
                     1001,
                     "InventoryCheck checkSlots false,"
                        + InventoryCheckPanel.this.fromSlotField.getText()
                        + ","
                        + InventoryCheckPanel.this.toSlotField.getText()
                  )
               );
         }

         if (InventoryCheckPanel.this.command.equals("InventoryCheck checkDiscs")) {
            InventoryCheckPanel.this.al
               .actionPerformed(
                  new ActionEvent(
                     this,
                     1001,
                     "InventoryCheck checkSlots true,"
                        + InventoryCheckPanel.this.fromSlotField.getText()
                        + ","
                        + InventoryCheckPanel.this.toSlotField.getText()
                  )
               );
         }
      }
   }
}
