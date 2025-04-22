package net.dvdplay.ui;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TextToRows {
   private String text;
   private int rowSize = 80;
   private ArrayList rowList = new ArrayList();

   public TextToRows() {
   }

   public TextToRows(String lText) {
      this.text = lText;
      this.ProcessText();
   }

   public TextToRows(String lText, int lRowSize) {
      this.text = lText;
      this.rowSize = lRowSize;
      this.ProcessText();
   }

   public void ProcessText() {
      String tmpBuffer = new String();
      new String();
      if (this.text.length() <= this.rowSize) {
         this.rowList.add(this.text);
      } else {
         StringTokenizer stk = new StringTokenizer(this.text, " ");

         while (stk.hasMoreTokens()) {
            String tmpStr = stk.nextToken();
            if (!tmpStr.equals("\n") && !tmpStr.equals("\\n") && !tmpStr.endsWith("\\n")) {
               if (tmpBuffer.length() + tmpStr.length() <= this.rowSize) {
                  tmpBuffer = tmpBuffer + tmpStr + " ";
               } else {
                  this.rowList.add(tmpBuffer);
                  tmpBuffer = tmpStr + " ";
               }
            } else {
               tmpStr = tmpStr.substring(0, tmpStr.length() - 2);
               tmpBuffer = tmpBuffer + tmpStr;
               this.rowList.add(tmpBuffer);
               tmpBuffer = "";
            }
         }

         this.rowList.add(tmpBuffer);
      }
   }

   public ArrayList getLabelList() {
      return this.rowList;
   }

   public String getRow(int i) {
      return i < this.getRowCount() ? (String)this.rowList.get(i) : null;
   }

   public int getRowCount() {
      return this.rowList.size();
   }

   public int getRowSize() {
      return this.rowSize;
   }

   public void setRowSize(int lRowSize) {
      this.rowSize = lRowSize;
   }

   public void setText(String lText) {
      this.text = lText;
   }
}
