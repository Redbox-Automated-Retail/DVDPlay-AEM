package net.dvdplay.screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.BadSlots;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.BadSlotsTableModel;
import net.dvdplay.view.KeyboardAssembler;

public class BadSlotsPanel extends AbstractToolsPanel {
   private JPanel discDisplay;
   private JPanel buttonPanel;
   BadSlotsTableModel bstm;
   JTable jt;
   ActionListener badSlotsAction;
   ActionListener al;
   BadSlots badSlots;
   JButton newEntry;
   JButton delEntry;
   JButton save;

   public BadSlotsPanel() {
      try {
         this.setLayout(null);
         this.al = new AbstractToolsPanel.ActionTools();
         this.bstm = new BadSlotsTableModel();
         this.badSlots = new BadSlots(this.al, this);
         this.badSlotsAction = new BadSlotsPanel.BadSlotsAction();
         this.buttonPanel = this.createButtonPanel();
         this.discDisplay = this.createDiscDisplayPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.discDisplay.setBounds(10, 10, 500, 500);
         this.buttonPanel.setBounds(600, 480, 300, 40);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.discDisplay);
         this.add(this.buttonPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[BadSlotsPanel] " + var2.toString(), var2);
      }
   }

   public JPanel createButtonPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 3));
      this.newEntry = this.createButton("New Entry", "BadSlots newEntry", this.badSlotsAction);
      this.delEntry = this.createButton("Delete", "BadSlots delete", this.badSlotsAction);
      this.save = this.createButton("Set/Save", "BadSlots save", this.badSlotsAction);
      temp.add(this.newEntry);
      temp.add(this.delEntry);
      temp.add(this.save);
      return temp;
   }

   public JPanel createDiscDisplayPanel() {
      JPanel temp = new JPanel();
      this.jt = new JTable(this.bstm);
      this.jt.setAutoResizeMode(0);
      this.jt.setRowSelectionAllowed(true);
      this.jt.setColumnSelectionAllowed(true);
      this.jt.setEditingColumn(0);
      this.jt.setRowHeight(20);
      this.jt.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
            Point origin = me.getPoint();
            int row = BadSlotsPanel.this.jt.rowAtPoint(origin);
            int column = BadSlotsPanel.this.jt.columnAtPoint(origin);
            if (row == -1 || column == -1) {
               return;
            }
            BadSlotsPanel.this.atm = BadSlotsPanel.this.bstm;
            BadSlotsPanel.this.focusRow = row;
            BadSlotsPanel.this.focusCol = column;
            BadSlotsPanel.this.textFieldFocus = false;
         }
      });

      JScrollPane jsp = new JScrollPane(this.jt);
      this.jt.setPreferredScrollableViewportSize(new Dimension(450, 470));
      temp.add(jsp);
      return temp;
   }

   private class BadSlotsAction implements ActionListener {
      private BadSlotsAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         BadSlotsPanel.this.command = ae.getActionCommand();
         BadSlotsPanel.this.data = "";

         Aem.logDetailMessage(DvdplayLevel.FINER, "[BadSlotsPanel] Action " + BadSlotsPanel.this.command);
         if (BadSlotsPanel.this.command.equals("BadSlots newEntry")) {
            BadSlotsPanel.this.bstm.newEntry();
         }

         if (BadSlotsPanel.this.command.equals("BadSlots delete")) {
            ArrayList removeList = BadSlotsPanel.this.bstm.getSelected();

            for (int i = removeList.size() - 1; i >= 0; i--) {
               int item = (Integer)removeList.get(i);
               String entryId = BadSlotsPanel.this.bstm.getValueAt(item, 1).toString();
               BadSlotsPanel.this.bstm.removeRowByID(entryId);
               data += entryId + ",";
            }

            if (removeList.size() > 0) {
               BadSlotsPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "BadSlots remove " + BadSlotsPanel.this.data));
            }
         }

         if (BadSlotsPanel.this.command.equals("BadSlots save")) {
            Vector temp = BadSlotsPanel.this.bstm.getAllEntry();

             for (Object o : temp) {
                 Object[] tmpObject = (Object[]) o;
                 data += tmpObject[1] + ":" + tmpObject[2] + ",";
             }

            BadSlotsPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "BadSlots save " + BadSlotsPanel.this.data));
         }
      }
   }
}
