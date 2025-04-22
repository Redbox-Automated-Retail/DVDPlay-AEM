package net.dvdplay.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import net.dvdplay.aem.Aem;
import net.dvdplay.ui.ScreenProperties;

public class KeyboardAssembler extends JPanel {
   private int inSetX = 0;
   private int inSetY = 0;
   private int keyWidth = 50;
   private int keyHeight = 40;
   private String[][][] adminKeyboardLayout = new String[][][]{
      {
            {"TAB", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "BACK", "<-", "->", "7", "8", "9", "0"},
            {"   ", "a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'", "\\", "ENTER", "Ctl-Alt-Del", "4", "5", "6", "-"},
            {"SHIFT", "z", "x", "c", "v", "b", "n", "m", ",", ".", "/", "SHIFT", "SPACE", "START", "1", "2", "3", "+"}
      },
      {
            {"TAB", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "BACK", "<-", "->", "&", "*", "(", ")"},
            {"   ", "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\"", "|", "ENTER", "Ctl-Alt-Del", "$", "%", "^", "_"},
            {"SHIFT", "Z", "X", "C", "V", "B", "N", "M", "<", ">", "?", "SHIFT", "SPACE", "START", "!", "@", "#", "="}
      }
   };
   private String[][][] customerKeyboardLayout = new String[][][]{
      {
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"A", "S", "D", "F", "G", "H", "J", "K", "L", "@"},
            {"Z", "X", "C", "V", "B", "N", "M", ".", "-", "_"}
      }
   };
   private JPanel content;
   private ArrayList[] buttonList;
   private JButton tempButton;
   private JSlider volume;
   private String[][][] keyboardLayout;
   private Font buttonFont;
   private Border buttonBorder;
   private static boolean upperCase = true;
   int width;
   int height;
   private int keyLayoutType;

   public KeyboardAssembler(String keyboardType, String _case, int lKeyWidth, int lKeyHeight) {
      this(keyboardType, _case, lKeyWidth, lKeyHeight, 0);
   }

   public KeyboardAssembler(String keyboardType, String _case, int lKeyWidth, int lKeyHeight, int adminRole) {
      this.setLayout(null);
      this.setBackground(ScreenProperties.getColor("Gray"));
      this.buttonList = new ArrayList[4];
      this.buttonList[0] = new ArrayList();
      this.buttonList[1] = new ArrayList();
      this.buttonList[2] = new ArrayList();
      this.buttonList[3] = new ArrayList();
      this.content = new JPanel();
      this.keyLayoutType = 0;
      if (keyboardType == "Customer") {
         this.keyboardLayout = this.customerKeyboardLayout;
         this.buttonFont = ScreenProperties.getFont("CustomerKeyboard");
         this.buttonBorder = new LineBorder(ScreenProperties.getColor("Black"));
         this.width = 860;
         this.height = 300;
      } else {
         this.keyboardLayout = this.adminKeyboardLayout;
         this.buttonFont = ScreenProperties.getFont("AdminKeyboard");
         this.buttonBorder = new LineBorder(Color.black, 1);
         this.width = 1024;
         this.height = 150;
         this.createVolumeSlider();
      }

      this.createKeyboard(keyboardType, "Lower", lKeyWidth, lKeyHeight, adminRole);
      this.add(this.content);
      this.setBounds(0, 0, this.width, this.height);
      this.setVisible(true);
   }

   public void shift() {
      this.keyLayoutType = this.keyLayoutType == 0 ? 1 : 0;

      for (int j = 0; j < this.keyboardLayout[this.keyLayoutType].length; j++) {
         for (int i = 0; i < this.keyboardLayout[this.keyLayoutType][j].length; i++) {
            JButton tmpB = (JButton)this.buttonList[j].get(i);
            tmpB.setText(this.keyboardLayout[this.keyLayoutType][j][i]);
            tmpB.setActionCommand(this.keyboardLayout[this.keyLayoutType][j][i]);
         }
      }
   }

   public void createKeyboard(String keyboardType, String keyboardCase, int lKeyWidth, int lKeyHeight, int adminRole) {
      this.keyWidth = lKeyWidth;
      this.keyHeight = lKeyHeight;
      int prevX = 0;

      for (int j = 0; j < this.keyboardLayout[this.keyLayoutType].length; j++) {
         int next = 0;
         prevX = 0;

         for (int i = 0; i < this.keyboardLayout[this.keyLayoutType][j].length; i++) {
            byte var10;
            if (this.keyboardLayout[this.keyLayoutType][j][i].length() > 2) {
               var10 = 1;
            } else {
               var10 = 0;
            }

            this.tempButton = this.createButton(
               this.keyboardLayout[this.keyLayoutType][j][i],
               this.inSetX + this.keyWidth * i,
               this.inSetY + this.keyHeight * j,
               this.keyWidth * 2,
               this.keyHeight
            );
            this.tempButton.setActionCommand(this.keyboardLayout[this.keyLayoutType][j][i]);
            this.tempButton.setBounds(prevX, this.inSetY + this.keyHeight * j, this.keyWidth * (var10 + 1), this.keyHeight);
            this.tempButton.setEnabled(true);
            this.buttonList[j].add(i, this.tempButton);
            if (!this.keyboardLayout[this.keyLayoutType][j][i].equals("   ")) {
               this.add(this.tempButton);
            }

            if (adminRole == 0) {
               if (this.keyboardLayout[this.keyLayoutType][j][i].equals("Ctl-Alt-Del")) {
                  this.tempButton.setEnabled(false);
               }

               if (this.keyboardLayout[this.keyLayoutType][j][i].equals("START")) {
                  this.tempButton.setEnabled(false);
               }
            } else if (adminRole == 2 && j != 0) {
               this.tempButton.setEnabled(false);
            }

            prevX += this.inSetX + this.keyWidth + this.keyWidth * var10;
         }
      }
   }

   private void createVolumeSlider() {
      this.volume = new JSlider(0, 0, 10, Aem.getVolume());
      this.volume.setMinorTickSpacing(2);
      this.volume.setMajorTickSpacing(4);
      this.volume.setPaintTicks(true);
      this.volume.setPaintLabels(true);
      this.volume.setLabelTable(this.volume.createStandardLabels(2));
      this.volume.setBounds(0, 40, 88, 40);
      this.add(this.volume);
   }

   private JButton createButton(String str, int x, int y, int width, int height) {
      JButton temp = new JButton(str);
      temp.setBounds(x, y, width, height);
      temp.setBackground(ScreenProperties.getColor("White"));
      temp.setFont(this.buttonFont);
      temp.setBorder(this.buttonBorder);
      temp.setFocusPainted(false);
      return temp;
   }

   public void addActionListener(ActionListener l) {
      this.listenerList
         .add(
            ActionListener.class,
            l
         );

      for (int j = 0; j < 4; j++) {
         for (int i = 0; i < this.buttonList[j].size(); i++) {
            this.tempButton = (JButton)this.buttonList[j].get(i);
            this.tempButton.addActionListener(l);
         }
      }
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );

      for (int j = 0; j < 4; j++) {
         for (int i = 0; i < this.buttonList[j].size(); i++) {
            this.tempButton = (JButton)this.buttonList[j].get(i);
            this.tempButton.removeActionListener(l);
         }
      }
   }

   public void addChangeListener(ChangeListener l) {
      this.volume.addChangeListener(l);
   }
}
