package net.dvdplay.screen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.SimpleRemoveDiscs;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.DiscInventoryTableModel;
import net.dvdplay.models.SortingColumnModel;
import net.dvdplay.view.KeyboardAssembler;

public class SimpleRemoveDiscsPanel extends AbstractToolsPanel {
   private JPanel discDisplay;
   private JPanel statusDisplay;
   private JPanel buttonPanel;
   private JPanel searchBy;
   private JPanel centerPanel;
   SimpleRemoveDiscs simpleRemoveDiscs;
   ActionListener al;
   ActionListener simpleRemoveDiscsAction;
   ActionListener toolsAction;
   DiscInventoryTableModel ditm;
   SortingColumnModel scm;
   JTable jt;
   JRadioButton all;
   JRadioButton markedForRemoval;
   JRadioButton unknownDisc;

   public SimpleRemoveDiscsPanel() {
      try {
         this.setLayout(null);
         this.al = new AbstractToolsPanel.ActionTools();
         this.simpleRemoveDiscsAction = new SimpleRemoveDiscsPanel.SimpleRemoveDiscsAction();
         this.ditm = new DiscInventoryTableModel();
         this.simpleRemoveDiscs = new SimpleRemoveDiscs(this.al, this, this.simpleRemoveDiscsAction, this.ditm);
         this.scm = new SortingColumnModel();
         this.buttonPanel = this.createButtonPanel();
         this.discDisplay = this.createDiscDisplayPanel();
         this.statusDisplay = this.createStatusDisplayPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 0);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.centerPanel = new JPanel(new FlowLayout());
         this.centerPanel.add(this.buttonPanel);
         this.ditm.refresh("markedForRemoval");
         this.ditm.selectAll();
         this.discDisplay.setBounds(10, 10, 490, 510);
         this.centerPanel.setBounds(505, 200, 150, 200);
         this.statusDisplay.setBounds(660, 10, 320, 500);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.discDisplay);
         this.add(this.statusDisplay);
         this.add(this.centerPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[SimpleRemoveDiscsPanel] " + var2.toString(), var2);
      }
   }

   public JPanel createButtonPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1));
      JButton remove = this.createButton("Remove", "SimpleRemoveDiscs remove", this.simpleRemoveDiscsAction);
      JButton refresh = this.createButton("Refresh", "SimpleRemoveDiscs refresh", this.simpleRemoveDiscsAction);
      JButton close = this.createButton("Exit Tools", "SimpleRemoveDiscs close", this.simpleRemoveDiscsAction);
      temp.add(remove);
      temp.add(refresh);
      temp.add(close);
      return temp;
   }

   public JPanel createSearchBy() {
      JPanel temp = new JPanel(new GridLayout(0, 1));
      temp.setBorder(new TitledBorder("Search By"));
      this.all = new JRadioButton("All");
      this.all.setActionCommand("RemoveDiscs all");
      this.all.setEnabled(false);
      this.markedForRemoval = new JRadioButton("Marked for Removal");
      this.markedForRemoval.setActionCommand("SimpleRemoveDiscs markedForRemoval");
      this.markedForRemoval.addActionListener(this.simpleRemoveDiscsAction);
      this.markedForRemoval.setEnabled(true);
      this.markedForRemoval.setSelected(true);
      this.unknownDisc = new JRadioButton("Unknown Disc");
      this.unknownDisc.setActionCommand("SimpleRemoveDiscs unknownDisc");
      this.unknownDisc.setEnabled(false);
      ButtonGroup group = new ButtonGroup();
      group.add(this.all);
      group.add(this.markedForRemoval);
      group.add(this.unknownDisc);
      temp.add(this.all);
      temp.add(this.markedForRemoval);
      temp.add(this.unknownDisc);
      return temp;
   }

   public JPanel createDiscDisplayPanel() {
      JPanel temp = new JPanel();
      this.scm = new SortingColumnModel(this.ditm);
      this.jt = new JTable(this.scm);
      this.scm.addMouseListenerToHeaderInTable(this.jt);
      this.jt.createDefaultColumnsFromModel();
      this.jt.setAutoResizeMode(0);
      this.jt.setRowSelectionAllowed(true);
      this.jt.setColumnSelectionAllowed(true);
      this.jt.setEditingColumn(0);
      this.jt.setRowHeight(20);
      JScrollPane jsp = new JScrollPane(this.jt);
      this.jt.setPreferredScrollableViewportSize(new Dimension(450, 470));
      temp.add(jsp);
      return temp;
   }

   public JPanel createStatusDisplayPanel() {
      JPanel temp = new JPanel();
      JTextArea status = new JTextArea(29, 20);
      status.setEditable(false);
      status.setWrapStyleWord(true);
      JScrollPane jsp = new JScrollPane(status, 20, 30);
      temp.add(jsp);
      return temp;
   }

   public void addActionListener(ActionListener l) {
      this.toolsAction = l;
   }

   private class SimpleRemoveDiscsAction implements ActionListener {
      private SimpleRemoveDiscsAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         SimpleRemoveDiscsPanel.this.command = ae.getActionCommand();
         SimpleRemoveDiscsPanel.this.data = "";
         Aem.logDetailMessage(DvdplayLevel.FINER, "[SimpleRemoveDiscsPanel] Action " + SimpleRemoveDiscsPanel.this.command);
         if (SimpleRemoveDiscsPanel.this.command.equals("SimpleRemoveDiscs remove")) {
            ArrayList removeList = SimpleRemoveDiscsPanel.this.ditm.getSelected();

            for (int i = removeList.size() - 1; i >= 0; i--) {
               int item = (Integer)removeList.get(i);
               String discId = SimpleRemoveDiscsPanel.this.ditm.getValueAt(item, 2).toString();
               data += discId + ",";
            }

            if (!removeList.isEmpty()) {
               SimpleRemoveDiscsPanel.this.al
                  .actionPerformed(new ActionEvent(this, 1001, "SimpleRemoveDiscs remove markedForRemoval," + SimpleRemoveDiscsPanel.this.data));
            }
         }

         if (SimpleRemoveDiscsPanel.this.command.equals("SimpleRemoveDiscs refresh")) {
            SimpleRemoveDiscsPanel.this.ditm.refresh("markedForRemoval");
         }

         if (SimpleRemoveDiscsPanel.this.command.equals("SimpleRemoveDiscs close")) {
            SimpleRemoveDiscsPanel.this.toolsAction.actionPerformed(new ActionEvent(this, 1001, "close"));
         }
      }
   }
}
