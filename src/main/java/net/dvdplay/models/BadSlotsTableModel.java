package net.dvdplay.models;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import net.dvdplay.hardware.BadSlots;

public class BadSlotsTableModel extends AbstractTableModel {
   String[] titles = new String[]{"", "Bad Slot", "Bad Slot ID"};
   Class[] types;
   BadSlots badSlots;
   Vector badSlot;
   int numRow;

   public BadSlotsTableModel() {
      this.types = new Class[]{
              Boolean.class,
              String.class,
              String.class
      };
      this.numRow = 0;
      this.badSlots = new BadSlots();
      this.badSlot = BadSlots.getBadEntry();
      this.numRow = this.badSlot.size();
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
      Object[] temp = (Object[])this.badSlot.get(r);
      return temp[c];
   }

   public boolean isCellEditable(int row, int col) {
      return true;
   }

   public void setValueAt(Object value, int row, int col) {
      Object[] temp = (Object[])this.badSlot.get(row);
      if (temp[col] instanceof Boolean oldValue) {
         try {
             temp[col] = oldValue;
            this.badSlot.setElementAt(temp, row);
            this.fireTableCellUpdated(row, col);
         } catch (NumberFormatException var6) {
         }
      } else if (temp[col] instanceof String) {
         temp[col] = (String)value;
         this.fireTableCellUpdated(row, col);
      }
   }

   public void selectAll() {
      System.out.println("Enabling ... ");

      for (int i = 0; i < this.numRow; i++) {
         Object[] temp = (Object[])this.badSlot.get(i);
         temp[0] = true;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public void newEntry() {
      Object[] temp = new Object[this.getColumnCount()];
      temp[0] = false;
      temp[1] = "";
      temp[2] = "";
      this.badSlot.add(temp);
      System.out.println("Here");
      this.fireTableRowsInserted(this.numRow, this.numRow);
      this.numRow++;
   }

   public void unSelectAll() {
      for (int i = 0; i < this.numRow; i++) {
         Object[] temp = (Object[])this.badSlot.get(i);
         temp[0] = false;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public ArrayList getSelected() {
      ArrayList list = new ArrayList();

      for (int i = 0; i < this.numRow; i++) {
         Object[] temp = (Object[])this.badSlot.get(i);
         Boolean bool = (Boolean)temp[0];
         if (bool) {
            list.add(i);
         }
      }

      return list;
   }

   public void removeRowByID(String id) {
      int pos = -1;
      if (this.numRow > 0) {
         for (int i = 0; i < this.numRow && pos == -1; i++) {
            Object[] temp = (Object[])this.badSlot.get(i);
            if (temp[1].equals(id)) {
               this.badSlot.remove(i);
               pos = i;
            }
         }

         this.numRow--;
         this.fireTableRowsDeleted(pos, pos);
      }
   }

   public Vector getAllEntry() {
      return this.badSlot;
   }

   public void refresh(String typeOfDisc) {
      this.badSlot.removeAllElements();
      this.badSlot = BadSlots.getBadEntry();
      this.numRow = this.badSlot.size();
      this.fireTableDataChanged();
   }
}
