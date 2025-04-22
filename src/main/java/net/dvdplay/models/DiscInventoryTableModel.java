package net.dvdplay.models;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import net.dvdplay.hardware.RemoveDiscs;

public class DiscInventoryTableModel extends AbstractTableModel {
   String[] titles = new String[]{
      "", "Slot", "Disc ID", "Group Code", "Disc Code", "Title", "Date Updated", "Marked for Rent", "Marked for Sale", "Marked for Removal"
   };
   Class[] types;
   RemoveDiscs removeDiscs;
   Vector discContent;
   int numRow;

   public DiscInventoryTableModel() {
      this.types = new Class[]{
         Boolean.class,
              Integer.class,
              Integer.class,
              String.class,
              String.class,
              String.class,
              String.class,
              String.class,
              String.class,
              String.class,
      };
      this.numRow = 0;
      this.removeDiscs = new RemoveDiscs();
      this.discContent = new Vector();
   }

   public int getRowCount() {
      return this.numRow;
   }

   public int getColumnCount() {
      return this.titles.length;
   }

   public String getColumnName(int c) {
      return this.titles[c];
   }

   public Class getColumnClass(int c) {
      return this.types[c];
   }

   public Object getValueAt(int r, int c) {
      Object[] temp = (Object[])this.discContent.get(r);
      return temp[c];
   }

   public boolean isCellEditable(int row, int col) {
      return col == 0;
   }

   public void setValueAt(Object value, int row, int col) {
      Object[] temp = (Object[])this.discContent.get(row);
      if (temp[col] instanceof Boolean oldValue) {
         try {
             temp[col] = oldValue;
            this.discContent.setElementAt(temp, row);
         } catch (NumberFormatException var7) {
         }
      }
   }

   public void selectAll() {
      System.out.println("Enabling ... ");

      for (int i = 0; i < this.numRow; i++) {
         Object[] temp = (Object[])this.discContent.get(i);
         temp[0] = true;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public void unSelectAll() {
      for (int i = 0; i < this.numRow; i++) {
         Object[] temp = (Object[])this.discContent.get(i);
         temp[0] = false;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public ArrayList<Integer> getSelected() {
      ArrayList<Integer> list = new ArrayList<>();

      for (int i = 0; i < this.numRow; i++) {
         Object[] temp = (Object[])this.discContent.get(i);
         Boolean bool = (Boolean)temp[0];
         if (bool) {
            list.add(i);
         }
      }

      return list;
   }

   public void removeRow(int row) {
      if (row > -1) {
         this.discContent.remove(row);
         this.numRow--;
         this.fireTableRowsDeleted(row, row);
      }
   }

   public void removeRowByID(int id) {
      int pos = -1;
      if (this.numRow > 0) {
         for (int i = 0; i < this.numRow && pos == -1; i++) {
            Object[] temp = (Object[])this.discContent.get(i);
            Integer idFromContent = (Integer)temp[i];
            if (idFromContent == id) {
               this.discContent.remove(i);
               pos = i;
            }
         }

         this.numRow--;
         this.fireTableRowsDeleted(pos, pos);
      }
   }

   public void refresh(String typeOfDisc) {
      Object[][] temp = RemoveDiscs.getInventory(typeOfDisc);
      this.discContent = new Vector();

      for (int i = 0; i < temp.length; i++) {
         if (temp[i][0] != null) {
            this.discContent.add(i, temp[i]);
         }
      }

      this.numRow = this.discContent.size();
      this.fireTableDataChanged();
   }
}
