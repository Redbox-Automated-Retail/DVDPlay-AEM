package net.dvdplay.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class Utility {
   public static JButton createTopBarButton(String text, Icon lIcon, Icon pressedIcon, Color lBackground, Border lBorder, Font lFont, Color lForeground) {
      JButton temp = new JButton(text, lIcon);
      temp.setBackground(lBackground);
      temp.setBorder(lBorder);
      temp.setFont(lFont);
      temp.setForeground(lForeground);
      temp.setFocusPainted(false);
      if (pressedIcon != null) {
         temp.setPressedIcon(pressedIcon);
      }

      temp.setHorizontalTextPosition(2);
      temp.setVerticalTextPosition(0);
      return temp;
   }

   public static JLabel createSeperatorBar(int length, char text, Color textColor, Color backgroundColor) {
      String temp = new String();

      for (int i = 0; i < length; i++) {
         temp = temp + text;
      }

      JLabel tempLabel = new JLabel(temp);
      tempLabel.setForeground(textColor);
      tempLabel.setBackground(backgroundColor);
      return tempLabel;
   }

   public static JLabel createLabel(String text, int x, int y, int width, int height, Color background, Color foreground, Font font) {
      JLabel temp = new JLabel(text);
      temp.setBounds(x, y, width, height);
      temp.setBackground(background);
      temp.setForeground(foreground);
      temp.setFont(font);
      return temp;
   }

   public static boolean isInt(String temp) {
      try {
         int ttInt = Integer.parseInt(temp);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isEmpty(String temp) {
      return temp.length() == 0;
   }

   public static JPanel createStatusDisplayPanel(String msg, int rows, int cols) {
      JPanel temp = new JPanel();
      temp.setBorder(new TitledBorder(msg));
      JTextArea status = new JTextArea(rows, cols);
      status.setEditable(false);
      status.setWrapStyleWord(true);
      status.setAutoscrolls(true);
      JScrollPane jsp = new JScrollPane(status, 20, 30);
      jsp.setAutoscrolls(true);
      temp.add(jsp);
      return temp;
   }

   public static void statAnnouncer(JTextArea jt, String msg) {
      jt.append(msg + "\n");
      jt.setCaretPosition(jt.getDocument().getLength());
   }
}
