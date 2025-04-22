package net.dvdplay.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import net.dvdplay.ui.ScreenProperties;

public class TopBarModel extends JPanel {
   JButton button1;
   JButton button2;
   JButton button3;
   JButton robotButton;
   int titleSize;
   int button1Size;
   int button2Size;
   int button3Size;
   JLabel title;
   Color panelBackground;
   Color titleColor;
   Color buttonForeground;
   Color buttonBackground;
   Color buttonBorderColor;
   Font buttonFont;
   Font titleFont;
   Border buttonBorder;
   ImageIcon cartIcon;
   ImageIcon cartPressedIcon;

   public TopBarModel(
      String titleText, String button3Text, String button3Action, String button2Text, String button2Action, String button1Text, String button1Action
   ) {
      this.init();
      this.setLayout(null);
      this.setBackground(this.panelBackground);
      this.setBounds(0, 0, 1024, 60);
      this.setProperty(titleText, button3Text, button3Action, button2Text, button2Action, button1Text, button1Action);
      this.add(this.robotButton);
      if (titleText != "") {
         this.add(this.title);
      }

      if (button1Text != "") {
         this.add(this.button1);
      }

      if (button2Text != "") {
         this.add(this.button2);
      }

      if (button3Text != "") {
         this.add(this.button3);
      }
   }

   private void init() {
      this.panelBackground = ScreenProperties.getColor("Red");
      this.titleColor = ScreenProperties.getColor("White");
      this.titleFont = ScreenProperties.getFont("TopBarTitle");
      this.buttonBackground = ScreenProperties.getColor("Red");
      this.buttonBorderColor = ScreenProperties.getColor("White");
      this.buttonBorder = new LineBorder(ScreenProperties.getColor("White"));
      this.buttonFont = ScreenProperties.getFont("TopBarButton");
      this.buttonForeground = ScreenProperties.getColor("White");
      this.cartIcon = ScreenProperties.getImage("Cart.Icon");
      this.cartPressedIcon = ScreenProperties.getImage("Cart.Pressed.Icon");
   }

   public void setProperty(
      String titleText, String button3Text, String button3Action, String button2Text, String button2Action, String button1Text, String button1Action
   ) {
      int length = 20;
      this.titleSize = titleText.length() * 20;
      this.button1Size = button1Text.length() * length;
      this.button2Size = button2Text.length() * length;
      this.button3Size = button3Text.length() * length;
      this.removeAll();
      this.setRobotButton();
      if (titleText != "") {
         this.setTitle(titleText);
      }

      if (button1Text != "") {
         this.setButton1(button1Text, button1Action);
      }

      if (button2Text != "") {
         this.setButton2(button2Text, button2Action);
      }

      if (button3Text != "") {
         this.setButton3(button3Text, button3Action);
      }

      if (titleText != "") {
         this.add(this.title);
      }

      if (button1Text != "") {
         this.add(this.button1);
      }

      if (button2Text != "") {
         this.add(this.button2);
      }

      if (button3Text != "") {
         this.add(this.button3);
      }

      this.repaint();
   }

   public void setTitle(String lTitle) {
      if (this.title == null) {
         this.title = new JLabel();
      }

      this.title.setText(lTitle);
      this.title.setFont(this.titleFont);
      this.title.setHorizontalAlignment(2);
      this.title.setForeground(this.titleColor);
      this.title.setBounds(40, 10, this.titleSize, 40);
   }

   public void setRobotButton() {
      if (this.robotButton == null) {
         this.robotButton = new JButton();
      }

      this.robotButton.setText(null);
      this.robotButton.setBounds(2, 2, 2, 2);
      this.robotButton.setBackground(this.buttonBackground);
      this.robotButton.setForeground(this.buttonBackground);
      this.robotButton.setBorder(null);
      this.robotButton.setPressedIcon(null);
      this.robotButton.setActionCommand("RobotClicking");
   }

   public void setButton1(String lText, String actionCommand) {
      if (this.button1 == null) {
         this.button1 = new JButton();
      }

      this.button1.setText(lText);
      this.button1.setBackground(this.buttonBackground);
      this.button1.setBorder(this.buttonBorder);
      this.button1.setFont(this.buttonFont);
      this.button1.setForeground(this.buttonForeground);
      this.button1.setHorizontalTextPosition(2);
      this.button1.setVerticalTextPosition(0);
      this.button1.setBounds(1010 - this.button1Size - 10, 10, this.button1Size, 40);
      this.button1.setFocusPainted(false);
      this.button1.setActionCommand(actionCommand);
   }

   public void setButton2(String lText, String actionCommand) {
      if (this.button2 == null) {
         this.button2 = new JButton();
      }

      this.button2.setText(lText);
      this.button2.setBackground(this.buttonBackground);
      this.button2.setBorder(this.buttonBorder);
      this.button2.setFont(this.buttonFont);
      this.button2.setForeground(this.buttonForeground);
      this.button2.setHorizontalTextPosition(2);
      this.button2.setVerticalTextPosition(0);
      this.button2.setBounds(1010 - this.button1Size - this.button2Size - 20, 10, this.button2Size, 40);
      this.button2.setFocusPainted(false);
      this.button2.setActionCommand(actionCommand);
   }

   public void setButton3(String lText, String actionCommand) {
      if (this.button3 == null) {
         this.button3 = new JButton();
      }

      if (actionCommand.equals("Cart")) {
         this.button3.setIcon(this.cartIcon);
         this.button3.setPressedIcon(this.cartPressedIcon);
         this.button3Size += 30;
         this.button3.setHorizontalAlignment(0);
      } else {
         this.button3.setIcon(null);
      }

      this.button3.setText(lText);
      this.button3.setBackground(this.buttonBackground);
      this.button3.setBorder(this.buttonBorder);
      this.button3.setFont(this.buttonFont);
      this.button3.setForeground(this.buttonForeground);
      this.button3.setBounds(1010 - this.button1Size - this.button2Size - this.button3Size - 30, 10, this.button3Size, 40);
      this.button3.setFocusPainted(false);
      this.button3.setActionCommand(actionCommand);
   }

   public void addActionListener(ActionListener l) {
      this.listenerList
         .add(
            ActionListener.class,
            l
         );
      if (this.robotButton != null) {
         for (int i = 0; i < this.robotButton.getActionListeners().length; i++) {
            ActionListener t = this.robotButton.getActionListeners()[i];
            this.robotButton.removeActionListener(t);
         }

         this.robotButton.addActionListener(l);
      }

      if (this.button1 != null) {
         for (int i = 0; i < this.button1.getActionListeners().length; i++) {
            ActionListener t = this.button1.getActionListeners()[i];
            this.button1.removeActionListener(t);
         }

         this.button1.addActionListener(l);
      }

      if (this.button2 != null) {
         for (int i = 0; i < this.button2.getActionListeners().length; i++) {
            ActionListener t = this.button2.getActionListeners()[i];
            this.button2.removeActionListener(t);
         }

         this.button2.addActionListener(l);
      }

      if (this.button3 != null) {
         for (int i = 0; i < this.button3.getActionListeners().length; i++) {
            ActionListener t = this.button3.getActionListeners()[i];
            this.button3.removeActionListener(t);
         }

         this.button3.addActionListener(l);
      }
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
      if (this.button1 != null) {
         this.button1.removeActionListener(l);
      }

      if (this.button2 != null) {
         this.button2.removeActionListener(l);
      }

      if (this.button3 != null) {
         this.button3.removeActionListener(l);
      }
   }
}
