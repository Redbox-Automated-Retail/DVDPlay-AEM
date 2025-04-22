package net.dvdplay.models;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import net.dvdplay.hardware.SlotCalEx;

public class SlotCalTableModel extends AbstractTableModel {
   String[] titles = new String[]{"-", "Slot No", "Calculated", "Current", "New"};
   Class[] types;
   Vector slot;
   int numRow;
   SlotCalEx slotCal;
   ArrayList selList;

   public SlotCalTableModel() {
      this.types = new Class[]{
              Boolean.class,
              Integer.class,
              String.class,
              String.class,
              String.class
      };
      this.numRow = 0;
      this.slot = new Vector();
      this.slotCal = new SlotCalEx();
      this.selList = new ArrayList();
      this.slot = SlotCalEx.getSlots();
   }

   public int getRowCount() {
      return this.slot.size();
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
      Object[] temp = (Object[])this.slot.get(r);
      return temp[c];
   }

   public boolean isCellEditable(int row, int col) {
      return col == 0;
   }

   public void setValueAt(Object value, int row, int col) {
      Object[] temp = (Object[])this.slot.get(row);
      if (temp[col] instanceof String) {
         temp[col] = value;
      } else if (temp[col] instanceof Boolean oldValue) {
         try {
             temp[col] = oldValue;
            this.slot.setElementAt(temp, row);
         } catch (NumberFormatException var6) {
         }
      }
   }

   public ArrayList getSelected() {
      ArrayList list = new ArrayList();

      for (int i = 0; i < this.slot.size(); i++) {
         Object[] temp = (Object[])this.slot.get(i);
         Boolean bool = (Boolean)temp[0];
         if (bool) {
            list.add(i);
         }
      }

      return list;
   }

   public void selectAll() {
      for (int i = 0; i < this.slot.size(); i++) {
         Object[] temp = (Object[])this.slot.get(i);
         temp[0] = true;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public void unSelectAll() {
      for (int i = 0; i < this.slot.size(); i++) {
         Object[] temp = (Object[])this.slot.get(i);
         temp[0] = false;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public void initList() {
      for (int i = 0; i < this.slot.size(); i++) {
         this.selList.add(false);
      }
   }

   public void resetList() {
      for (int i = 0; i < this.slot.size(); i++) {
         this.selList.set(i, false);
      }
   }

   public boolean getListItem(int index) {
      return Boolean.valueOf(String.valueOf(this.selList.get(index))) == true;
   }

   public void setListItem(int index, boolean value) {
      this.selList.set(index, value);
   }
}
