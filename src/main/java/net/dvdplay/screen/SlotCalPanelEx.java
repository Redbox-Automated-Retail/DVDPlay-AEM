package net.dvdplay.screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.SlotCalEx;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.SlotCalTableModel;
import net.dvdplay.view.KeyboardAssembler;
import net.dvdplay.view.Utility;

public class SlotCalPanelEx extends SlotCalPanel {
   private JPanel discDisplay;
   private JPanel calculatedValuePanel;
   private JPanel buttonPanel;
   private JPanel confirmationPanel;
   private JPanel statusPanel;
   private SlotCalTableModel sctm;
   private JTable jt;
   private JTextField slotNum;
   private ActionListener al;
   private ActionListener slotCalAction;
   private SlotCalEx slotCal;
   private boolean allChecked = false;
   private boolean listInited = false;
   static int g = 0;
   private final ButtonGroup group = new ButtonGroup();
   public TableCellRenderer myrenderer = new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         if (sctm.getListItem(row)) {
            comp.setForeground(Color.blue);
            comp.setIgnoreRepaint(true);
         } else {
            comp.setForeground(Color.black);
            comp.setIgnoreRepaint(false);
         }
         return comp;
      }
   };



   public SlotCalPanelEx() {
      try {
         this.setLayout(null);
         this.sctm = new SlotCalTableModel();
         this.al = new AbstractToolsPanel.ActionTools();
         this.slotCalAction = new SlotCalPanelEx.SlotCalAction();
         this.slotCal = new SlotCalEx(this.al, this);
         this.discDisplay = this.createDiscDisplayPanel();
         this.buttonPanel = this.createButtonPanel();
         this.calculatedValuePanel = this.createCalculatedValuePanel();
         this.statusPanel = Utility.createStatusDisplayPanel("Status", 7, 30);
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.discDisplay.setBounds(10, 10, 500, 500);
         this.buttonPanel.setBounds(550, 350, 350, 150);
         this.calculatedValuePanel.setBounds(550, 10, 350, 130);
         this.statusPanel.setBounds(550, 180, 350, 150);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.discDisplay);
         this.add(this.buttonPanel);
         this.add(this.calculatedValuePanel);
         this.add(this.statusPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[SlotCalPanelEx] " + var2.toString(), var2);
      }
   }

   public JPanel createButtonPanel() {
      JPanel buttonPanel = new JPanel(new GridLayout(0, 2));
      JButton toggleCheckAll = this.createButton("Un/Check All", "toggleCheckAll", this.slotCalAction);
      JButton goToSlot = this.createButton("GoToSlot", "goToSlot", this.slotCalAction);
      this.slotNum = new JTextField("1");
      this.slotNum.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      this.slotNum.setEditable(true);
      this.textFieldHash.put("slotNum", this.slotNum);
      JButton calibrate = this.createButton("Calibrate", "calibrate", this.slotCalAction);
      JButton adjust = this.createButton("Adjust", "adjust", this.slotCalAction);
      JButton reset = this.createButton("Reset", "reset", this.slotCalAction);
      JButton refresh = this.createButton("Refresh", "refresh", this.slotCalAction);
      JLabel space = new JLabel("                   ");
      JButton saveToServer = this.createButton("Save To Server", "saveToServer", this.slotCalAction);
      JButton saveToDisk = this.createButton("Save To Disk", "saveToDisk", this.slotCalAction);
      buttonPanel.add(goToSlot);
      buttonPanel.add(this.slotNum);
      buttonPanel.add(toggleCheckAll);
      buttonPanel.add(calibrate);
      buttonPanel.add(reset);
      buttonPanel.add(adjust);
      buttonPanel.add(refresh);
      buttonPanel.add(space);
      buttonPanel.add(saveToDisk);
      buttonPanel.add(saveToServer);
      return buttonPanel;
   }

   public JPanel createCalculatedValuePanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1));
      temp.setBorder(new TitledBorder("Calculated Values"));
      JPanel pwmValue = this.createRow("PWM Value : ", 10, "pmwValue", SlotCalEx.getPWMValue(), true);
      JPanel toolOffset = this.createRow("Tool Offset : ", 10, "toolOffset", SlotCalEx.getToolOffset(), true);
      JPanel slot1Adjustment = this.createRow("Slot 1 Adjustment : ", 10, "slot1Adjustment", SlotCalEx.getSlot1Adjustment(), true);
      JPanel radioButtonPanel = new JPanel();
      JRadioButton button1 = new JRadioButton("1");
      button1.setActionCommand("1");
      button1.setSelected(true);
      JRadioButton button2 = new JRadioButton("2");
      button2.setActionCommand("2");
      JRadioButton button3 = new JRadioButton("3");
      button3.setActionCommand("3");
      this.group.add(button1);
      this.group.add(button2);
      this.group.add(button3);
      radioButtonPanel.add(button1);
      radioButtonPanel.add(button2);
      radioButtonPanel.add(button3);
      temp.add(pwmValue);
      temp.add(toolOffset);
      temp.add(slot1Adjustment);
      temp.add(radioButtonPanel);
      return temp;
   }

   public JPanel createDiscDisplayPanel() {
      if (!this.listInited) {
         this.sctm.initList();
         this.listInited = true;
      }

      JPanel temp = new JPanel();
      this.jt = new JTable() {
         public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 4) {
               return myrenderer;
            }
            return super.getCellRenderer(row, column);
         }
      };

      this.jt.setAutoResizeMode(0);
      this.jt.setRowSelectionAllowed(true);
      this.jt.setEditingColumn(0);
      this.jt.setRowHeight(20);
      this.jt.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
            Point origin = me.getPoint();
            int row = jt.rowAtPoint(origin);
            int column = jt.columnAtPoint(origin);
            if (row == -1 || column == -1) {
               return;
            }
            atm = sctm;
            focusRow = row;
            focusCol = column;
            textFieldFocus = false;
         }
      });
      JScrollPane jsp = new JScrollPane(this.jt);
      this.jt.setPreferredScrollableViewportSize(new Dimension(450, 470));
      temp.add(jsp);
      return temp;
   }

   private class SlotCalAction implements ActionListener {
      private SlotCalAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         SlotCalPanelEx.this.command = ae.getActionCommand();
         SlotCalPanelEx.this.data = "";
         Aem.logDetailMessage(DvdplayLevel.FINER, "[SlotCalPanelEx] Action " + SlotCalPanelEx.this.command);
         if (SlotCalPanelEx.this.command.startsWith("save")) {
            for (int i = 0; i < SlotCalPanelEx.this.sctm.getRowCount(); i++) {
               Object tt = SlotCalPanelEx.this.sctm.getValueAt(i, SlotCalEx.beenCalibrateFull ? 4 : 3);
               SlotCalPanelEx.this.sctm.setValueAt(tt, i, 3);
               data += tt.toString() + ",";
            }

            SlotCalPanelEx.this.sctm.resetList();
            SlotCalPanelEx.this.sctm.fireTableDataChanged();
            SlotCalPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "SlotCalEx " + SlotCalPanelEx.this.command + " " + SlotCalPanelEx.this.data));
         } else if (SlotCalPanelEx.this.command.equals("goToSlot")) {
            JTextField slotNum = (JTextField)SlotCalPanelEx.this.textFieldHash.get("slotNum");
//            SlotCalPanelEx.this.slotCal;
            SlotCalEx.goToSlot(SlotCalPanelEx.this.sctm, slotNum.getText());
         } else if (SlotCalPanelEx.this.command.equals("calibrate")) {
            JTextField pmwValue = (JTextField)SlotCalPanelEx.this.textFieldHash.get("pmwValue");
            JTextField toolOffset = (JTextField)SlotCalPanelEx.this.textFieldHash.get("toolOffset");
//            SlotCalPanelEx.this.slotCal;
            SlotCalEx.calibrate(SlotCalPanelEx.this.sctm, pmwValue.getText(), toolOffset.getText(), SlotCalPanelEx.this.group.getSelection().getActionCommand());
         } else if (SlotCalPanelEx.this.command.equals("adjust")) {
            JTextField slot1Adjustment = (JTextField)SlotCalPanelEx.this.textFieldHash.get("slot1Adjustment");
//            SlotCalPanelEx.this.slotCal;
            SlotCalEx.adjust(SlotCalPanelEx.this.sctm, slot1Adjustment.getText());
         } else if (SlotCalPanelEx.this.command.equals("reset")) {
            if (JOptionPane.showConfirmDialog(null, "Reset all values to default? Are you sure?", "Aem!", 0) == 0) {
               for (int i = 0; i < SlotCalPanelEx.this.sctm.getRowCount(); i++) {
                  Object tt = SlotCalPanelEx.this.sctm.getValueAt(i, 2);
                  SlotCalPanelEx.this.sctm.setValueAt(tt, i, 3);
                  SlotCalPanelEx.this.sctm.setValueAt("---", i, 4);
                  data += tt.toString() + ",";
               }

               SlotCalPanelEx.this.sctm.resetList();
               SlotCalPanelEx.this.sctm.fireTableDataChanged();
               SlotCalPanelEx.this.al.actionPerformed(new ActionEvent(this, 1001, "SlotCalEx reset " + SlotCalPanelEx.this.data));
            }
         } else if (SlotCalPanelEx.this.command.equals("refresh")) {
//            SlotCalPanelEx.this.slotCal;
            SlotCalEx.refresh(SlotCalPanelEx.this.sctm);
         } else if (SlotCalPanelEx.this.command.equals("toggleCheckAll")) {
            if (SlotCalPanelEx.this.allChecked) {
               SlotCalPanelEx.this.sctm.unSelectAll();
               SlotCalPanelEx.this.allChecked = false;
            } else {
               SlotCalPanelEx.this.sctm.selectAll();
               SlotCalPanelEx.this.allChecked = true;
            }
         }
      }
   }
}
