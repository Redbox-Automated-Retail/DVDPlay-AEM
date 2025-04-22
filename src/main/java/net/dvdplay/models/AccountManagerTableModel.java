package net.dvdplay.models;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class AccountManagerTableModel extends AbstractTableModel {
   String[] titles = new String[]{"", "UserName", "Role"};
   Class[] types;
   Vector<Object> accounts;

   public AccountManagerTableModel() {
      this.types = new Class[]{
         Boolean.class,
         String.class,
         String.class
      };
      this.accounts = new Vector();
   }

   public int getRowCount() {
      return this.accounts.size();
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
      Object[] temp = (Object[])this.accounts.get(r);
      return temp[c];
   }

   public boolean isCellEditable(int row, int col) {
      return col == 0;
   }

   public void setValueAt(Object value, int row, int col) {
      Object[] temp = (Object[])this.accounts.get(row);
      if (temp[col] instanceof Boolean oldValue) {
         try {
             temp[col] = oldValue;
            this.accounts.setElementAt(temp, row);
            this.fireTableCellUpdated(row, col);
         } catch (NumberFormatException ignored) {
         }
      }
   }

   public void addValue(Object[] value) {
      this.accounts.add(value);
      this.fireTableRowsInserted(this.accounts.size() - 1, this.accounts.size() - 1);
   }

   public void selectAll() {
      System.out.println("Enabling ... ");

      for (int i = 0; i < this.accounts.size(); i++) {
         Object[] temp = (Object[])this.accounts.get(i);
         temp[0] = true;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public void unSelectAll() {
      for (int i = 0; i < this.accounts.size(); i++) {
         Object[] temp = (Object[])this.accounts.get(i);
         temp[0] = false;
         this.fireTableCellUpdated(i, 0);
      }
   }

   public ArrayList<Object> getSelected() {
      ArrayList<Object> list = new ArrayList<>();

       for (Object account : this.accounts) {
           Object[] temp = (Object[]) account;
           Boolean bool = (Boolean) temp[0];
           if (bool) {
               list.add(temp[1]);
           }
       }

      return list;
   }

   public void removeRow(int row) {
      if (row > -1) {
         this.accounts.remove(row);
         this.fireTableRowsDeleted(row, row);
      }
   }

   public void removeRowByUserName(String username) {
      int pos = -1;
      if (!this.accounts.isEmpty()) {
         for (int i = 0; i < this.accounts.size() && pos == -1; i++) {
            if (this.getValueAt(i, 1).equals(username)) {
               this.accounts.remove(i);
               pos = i;
            }
         }

         this.fireTableRowsDeleted(pos, pos);
      }
   }
}
