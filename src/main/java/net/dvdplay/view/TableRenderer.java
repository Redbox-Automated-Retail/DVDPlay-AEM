package net.dvdplay.view;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableRenderer extends DefaultTableCellRenderer {
   double threshold;

   public TableRenderer() {
      this.setHorizontalAlignment(4);
      this.setHorizontalTextPosition(4);
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
      if (value instanceof Boolean) {
      }

      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
   }
}
