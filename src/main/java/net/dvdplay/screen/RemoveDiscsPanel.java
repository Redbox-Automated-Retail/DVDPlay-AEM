package net.dvdplay.screen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.RemoveDiscs;
import net.dvdplay.models.DiscInventoryTableModel;
import net.dvdplay.models.SortingColumnModel;
import net.dvdplay.view.KeyboardAssembler;

public class RemoveDiscsPanel extends AbstractToolsPanel {
   private JPanel discDisplay;
   private JPanel statusDisplay;
   private JPanel buttonPanel;
   private JPanel searchBy;
   private JPanel centerPanel;
   RemoveDiscs removeDiscs;
   ActionListener al;
   ActionListener removeDiscsAction;
   DiscInventoryTableModel ditm;
   SortingColumnModel scm;
   JTable jt;
   JRadioButton all;
   JRadioButton markedForRemoval;
   JRadioButton unknownDisc;
   String discContent = "";

   public RemoveDiscsPanel() {
      this.setLayout(null);
      this.al = new AbstractToolsPanel.ActionTools();
      this.removeDiscsAction = new RemoveDiscsPanel.RemoveDiscsAction();
      this.ditm = new DiscInventoryTableModel();
      this.removeDiscs = new RemoveDiscs(this.al, this, this.removeDiscsAction, this.ditm);
      this.scm = new SortingColumnModel();
      this.buttonPanel = this.createButtonPanel();
      this.discDisplay = this.createDiscDisplayPanel();
      this.statusDisplay = this.createStatusDisplayPanel();
      this.searchBy = this.createSearchBy();
      this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
      this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
      this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
      this.centerPanel = new JPanel(new FlowLayout());
      this.centerPanel.add(this.searchBy);
      this.centerPanel.add(this.buttonPanel);
      this.discDisplay.setBounds(10, 10, 490, 510);
      this.centerPanel.setBounds(505, 10, 150, 500);
      this.statusDisplay.setBounds(660, 10, 320, 500);
      this.ka.setBounds(5, 530, 990, 120);
      this.add(this.discDisplay);
      this.add(this.statusDisplay);
      this.add(this.centerPanel);
      this.add(this.ka);
      this.setVisible(true);
   }

   public JPanel createButtonPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 1));
      JButton selectAll = this.createButton("Select All", "RemoveDiscs selectAll", this.removeDiscsAction);
      JButton unSelectAll = this.createButton("Unselect All", "RemoveDiscs unSelectAll", this.removeDiscsAction);
      JLabel space = new JLabel("                   ");
      JButton remove = this.createButton("Remove", "RemoveDiscs remove", this.removeDiscsAction);
      JButton refresh = this.createButton("Refresh", "RemoveDiscs refresh", this.removeDiscsAction);
      temp.add(selectAll);
      temp.add(unSelectAll);
      temp.add(space);
      temp.add(remove);
      temp.add(refresh);
      return temp;
   }

   public JPanel createSearchBy() {
      JPanel temp = new JPanel(new GridLayout(0, 1));
      temp.setBorder(new TitledBorder("Search By"));
      this.all = new JRadioButton("All");
      this.all.setActionCommand("RemoveDiscs all");
      this.all.addActionListener(this.removeDiscsAction);
      this.markedForRemoval = new JRadioButton("Marked for Removal");
      this.markedForRemoval.setActionCommand("RemoveDiscs markedForRemoval");
      this.markedForRemoval.addActionListener(this.removeDiscsAction);
      this.unknownDisc = new JRadioButton("Unknown Disc");
      this.unknownDisc.setActionCommand("RemoveDiscs unknownDisc");
      this.unknownDisc.addActionListener(this.removeDiscsAction);
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
      JTextArea status = new JTextArea(29, 25);
      status.setEditable(false);
      status.setWrapStyleWord(true);
      JScrollPane jsp = new JScrollPane(status, 20, 30);
      temp.add(jsp);
      return temp;
   }

   private class RemoveDiscsAction implements ActionListener {
      private RemoveDiscsAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         String command = ae.getActionCommand();
         String data = "";
         if (command.equals("RemoveDiscs all")) {
            RemoveDiscsPanel.this.discContent = "all";
            RemoveDiscsPanel.this.ditm.refresh("all");
         }

         if (command.equals("RemoveDiscs unknownDisc")) {
            RemoveDiscsPanel.this.discContent = "unknownDisc";
            RemoveDiscsPanel.this.ditm.refresh("unknownDisc");
         }

         if (command.equals("RemoveDiscs markedForRemoval")) {
            RemoveDiscsPanel.this.discContent = "markedForRemoval";
            RemoveDiscsPanel.this.ditm.refresh("markedForRemoval");
         }

         if (command.equals("RemoveDiscs selectAll")) {
            RemoveDiscsPanel.this.ditm.selectAll();
         }

         if (command.equals("RemoveDiscs unSelectAll")) {
            RemoveDiscsPanel.this.ditm.unSelectAll();
         }

         if (command.equals("RemoveDiscs remove")) {
            ArrayList removeList = RemoveDiscsPanel.this.ditm.getSelected();

            for (int i = removeList.size() - 1; i >= 0; i--) {
               int item = (Integer)removeList.get(i);
               String discId = RemoveDiscsPanel.this.ditm.getValueAt(item, 2).toString();
               data = data + discId + ",";
            }

            if (removeList.size() > 0) {
               RemoveDiscsPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "RemoveDiscs remove " + RemoveDiscsPanel.this.discContent + "," + data));
            }
         }

         if (command.equals("RemoveDiscs refresh")) {
            if (RemoveDiscsPanel.this.all.isSelected()) {
               RemoveDiscsPanel.this.ditm.refresh("all");
            } else if (RemoveDiscsPanel.this.unknownDisc.isSelected()) {
               RemoveDiscsPanel.this.ditm.refresh("unknownDisc");
            } else if (RemoveDiscsPanel.this.markedForRemoval.isSelected()) {
               RemoveDiscsPanel.this.ditm.refresh("markedForRemoval");
            }
         }
      }
   }
}
