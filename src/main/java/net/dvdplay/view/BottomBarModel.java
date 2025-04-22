package net.dvdplay.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.ui.ScreenProperties;

public class BottomBarModel extends JPanel {
   JLabel logo;
   JButton leftButton;
   JButton centerButton;
   JButton rightButton;
   JButton promoCode;
   JButton leftestButton;
   int rightButtonSize;
   int centerButtonSize;
   int leftButtonSize;
   int leftestButtonSize;
   Color panelBackground;
   Color buttonForeground;
   Color buttonBackground;
   Color buttonBorderColor;
   Font buttonFont;
   Border buttonBorder;
   Border grayButtonBorder;
   Icon tempIcon;
   Icon pressedIcon;
   int locationX = 1000;
   int length;
   int extra;
   ActionListener mainAction;

   public BottomBarModel(
      Color lPanelBackground,
      boolean showLogo,
      boolean isLogoButtonboolean,
      boolean showPromoCode,
      String leftButtonText,
      String leftButtonActionString,
      String centerButtonText,
      String centerButtonActionString,
      String rightButtonText,
      String rightButtonActionString
   ) {
      this.init();
      this.panelBackground = lPanelBackground;
      this.setLayout(null);
      this.setBackground(this.panelBackground);
      this.setBounds(0, 690, 1024, 78);
      this.setProperty(
         lPanelBackground,
         showLogo,
         isLogoButtonboolean,
         showPromoCode,
         leftButtonText,
         leftButtonActionString,
         centerButtonText,
         centerButtonActionString,
         rightButtonText,
         rightButtonActionString
      );
      if (showLogo) {
         this.add(this.logo);
      }

      if (showPromoCode) {
         this.add(this.promoCode);
      }

      if (leftButtonText != "") {
         this.add(this.leftButton);
      }

      if (centerButtonText != "") {
         this.add(this.centerButton);
      }

      if (rightButtonText != "") {
         this.add(this.rightButton);
      }
   }

   private void init() {
      this.buttonBackground = ScreenProperties.getColor("Black");
      this.buttonBorderColor = ScreenProperties.getColor("White");
      this.buttonBorder = new LineBorder(ScreenProperties.getColor("White"));
      this.grayButtonBorder = new LineBorder(ScreenProperties.getColor("Gray"));
      this.buttonFont = ScreenProperties.getFont("BottomBarButton");
      this.buttonForeground = ScreenProperties.getColor("White");
      this.locationX = 1000;
   }

   public void setProperty(
      Color lPanelBackground,
      boolean showLogo,
      boolean isLogoAButton,
      boolean showPromoCode,
      String leftButtonText,
      String leftButtonTextActionString,
      String centerButtonText,
      String centerButtonTextActionString,
      String rightButtonText,
      String rightButtonTextActionString
   ) {
      this.length = 20;
      this.locationX = 1000;
      this.rightButtonSize = rightButtonText.length() * this.length;
      this.centerButtonSize = centerButtonText.length() * this.length;
      this.leftButtonSize = leftButtonText.length() * this.length;
      this.removeAll();
      if (showLogo) {
         this.setLogo(showLogo, isLogoAButton);
      }

      if (showPromoCode) {
         this.setPromoCode(showPromoCode);
      }

      if (rightButtonText != "") {
         this.setrightButton(rightButtonText, rightButtonTextActionString);
      }

      if (centerButtonText != "") {
         this.setcenterButton(centerButtonText, centerButtonTextActionString);
      }

      if (leftButtonText != "") {
         this.setleftButton(leftButtonText, leftButtonTextActionString);
      }

      if (showLogo) {
         this.add(this.logo);
      }

      if (showPromoCode) {
         this.add(this.promoCode);
      }

      if (leftButtonText != "") {
         this.add(this.leftButton);
      }

      if (centerButtonText != "") {
         this.add(this.centerButton);
      }

      if (rightButtonText != "") {
         this.add(this.rightButton);
      }
   }

   public void setrightButton(String lText, String action) {
      this.extra = 0;
      if (this.rightButton == null) {
         this.rightButton = new JButton();
      }

      if (!action.equals("Back") && !action.equals("BackHelp") && !action.equals("MoreHelp")) {
         this.tempIcon = ScreenProperties.getImage("RedRightArrowonBlack.Logo.Icon");
         this.pressedIcon = ScreenProperties.getImage("OrangeArrowRight.Icon");
         this.rightButton.setHorizontalTextPosition(2);
      } else {
         this.tempIcon = ScreenProperties.getImage("RedLeftArrowonBlack.Logo.Icon");
         this.pressedIcon = ScreenProperties.getImage("OrangeArrowLeft.Icon");
         this.rightButton.setHorizontalTextPosition(4);
         if (action.equals("MoreHelp")) {
            this.tempIcon = ScreenProperties.getImage("RedRightArrowonBlack.Logo.Icon");
            this.pressedIcon = ScreenProperties.getImage("OrangeArrowRight.Icon");
            this.rightButton.setHorizontalTextPosition(2);
         }

         this.extra = 25;
      }

      this.locationX = this.locationX - (this.rightButtonSize + this.extra);
      this.rightButton.setText(lText);
      this.rightButton.setBackground(this.buttonBackground);
      this.rightButton.setFont(this.buttonFont);
      this.rightButton.setFocusPainted(false);
      this.rightButton.setBounds(this.locationX, 10, this.rightButtonSize + this.extra, 50);
      this.rightButton.setForeground(this.buttonForeground);
      if (action.length() > 0) {
         this.rightButton.setActionCommand(action);
         this.rightButton.setEnabled(true);
         this.rightButton.setIcon(this.tempIcon);
         this.rightButton.setPressedIcon(this.pressedIcon);
         this.rightButton.setBorder(this.buttonBorder);
      } else {
         this.rightButton.setEnabled(false);
         this.rightButton.setIcon(null);
         this.rightButton.setBorder(this.grayButtonBorder);
      }
   }

   public void setcenterButton(String lText, String action) {
      this.extra = 0;
      if (this.centerButton == null) {
         this.centerButton = new JButton();
         this.centerButton.setBackground(this.buttonBackground);
         this.centerButton.setFont(this.buttonFont);
         this.centerButton.setFocusPainted(false);
      }

      if (!action.equals("Back") && !action.equals("BackHelp") && !action.equals("MoreHelp")) {
         this.tempIcon = ScreenProperties.getImage("RedRightArrowonBlack.Logo.Icon");
         this.pressedIcon = ScreenProperties.getImage("OrangeArrowRight.Icon");
         this.centerButton.setHorizontalTextPosition(2);
      } else {
         this.tempIcon = ScreenProperties.getImage("RedLeftArrowonBlack.Logo.Icon");
         this.pressedIcon = ScreenProperties.getImage("OrangeArrowLeft.Icon");
         this.centerButton.setHorizontalTextPosition(4);
         this.extra = 25;
      }

      this.locationX = this.locationX - (this.centerButtonSize + this.extra + 10);
      this.centerButton.setText(lText);
      this.centerButton.setBounds(this.locationX, 10, this.centerButtonSize + this.extra, 50);
      this.centerButton.setForeground(this.buttonForeground);
      if (action.length() > 0) {
         this.centerButton.setEnabled(true);
         this.centerButton.setIcon(this.tempIcon);
         this.centerButton.setActionCommand(action);
         this.centerButton.setPressedIcon(this.pressedIcon);
         this.centerButton.setBorder(this.buttonBorder);
      } else {
         this.centerButton.setEnabled(false);
         this.centerButton.setIcon(null);
         this.centerButton.setBorder(this.grayButtonBorder);
      }
   }

   public void setleftButton(String lText, String action) {
      this.extra = 0;
      if (this.leftButton == null) {
         this.leftButton = new JButton();
      }

      if (action.equals("Back")) {
         this.tempIcon = ScreenProperties.getImage("RedLeftArrowonBlack.Logo.Icon");
         this.pressedIcon = ScreenProperties.getImage("OrangeArrowLeft.Icon");
         this.leftButton.setHorizontalTextPosition(4);
         this.extra = 20;
      } else if (!action.equals("About") && !action.equals("Skip")) {
         this.tempIcon = ScreenProperties.getImage("RedRightArrowonBlack.Logo.Icon");
         this.pressedIcon = ScreenProperties.getImage("OrangeArrowRight.Icon");
         this.leftButton.setHorizontalTextPosition(2);
      } else {
         this.tempIcon = null;
         this.pressedIcon = null;
      }

      this.locationX = this.locationX - (this.leftButtonSize + this.extra + 10);
      this.leftButton.setText(lText);
      this.leftButton.setBackground(this.buttonBackground);
      this.leftButton.setFont(this.buttonFont);
      this.leftButton.setFocusPainted(false);
      this.leftButton.setBounds(this.locationX, 10, this.leftButtonSize + this.extra, 50);
      if (action.length() > 0) {
         this.leftButton.setEnabled(true);
         this.leftButton.setActionCommand(action);
         this.leftButton.setForeground(this.buttonForeground);
         this.leftButton.setPressedIcon(this.pressedIcon);
         this.leftButton.setIcon(this.tempIcon);
         this.leftButton.setBorder(this.buttonBorder);
      } else {
         this.leftButton.setEnabled(false);
         this.leftButton.setForeground(this.buttonForeground);
         this.leftButton.setIcon(null);
         this.leftButton.setBorder(this.grayButtonBorder);
      }
   }

   public void setleftestButton(String lText, String action) {
      if (this.leftestButton == null) {
         this.leftestButton = new JButton();
      }

      this.tempIcon = ScreenProperties.getImage("RedRightArrowonBlack.Logo.Icon");
      this.pressedIcon = ScreenProperties.getImage("OrangeArrowRight.Icon");
      if (action.equals("About")) {
         this.tempIcon = null;
         this.pressedIcon = null;
      }

      this.locationX = this.locationX - (this.leftestButtonSize + 10);
      this.leftestButton.setHorizontalTextPosition(2);
      this.leftestButton.setText(lText);
      this.leftestButton.setBackground(this.buttonBackground);
      this.leftestButton.setFont(this.buttonFont);
      this.leftestButton.setFocusPainted(false);
      this.leftestButton.setBounds(this.locationX, 10, this.leftestButtonSize, 50);
      if (action.length() > 0) {
         this.leftestButton.setEnabled(true);
         this.leftestButton.setActionCommand(action);
         this.leftestButton.setForeground(this.buttonForeground);
         this.leftestButton.setPressedIcon(this.pressedIcon);
         this.leftestButton.setIcon(this.tempIcon);
         this.leftestButton.setBorder(this.buttonBorder);
      } else {
         this.leftestButton.setEnabled(false);
         this.leftestButton.setForeground(this.buttonForeground);
         this.leftestButton.setIcon(null);
         this.leftestButton.setBorder(this.grayButtonBorder);
      }
   }

   public void setLogo(boolean aShow, boolean aIsLogoButton) {
      if (aShow) {
         this.tempIcon = ScreenProperties.getImage("DvdPlay.Logo.IconOnBlack." + Aem.getString(2021));
         if (this.panelBackground.getRGB() == -3407872) {
            this.tempIcon = ScreenProperties.getImage("DvdPlay.Logo.IconOnRed." + Aem.getString(2021));
         }

         if (this.logo == null) {
            this.logo = new JLabel(this.tempIcon);
            this.logo.setOpaque(true);
            this.logo
               .setBounds(
                  ScreenProperties.getInt("DvdPlay.Logo.LocationX"),
                  ScreenProperties.getInt("DvdPlay.Logo.LocationY"),
                  ScreenProperties.getInt("DvdPlay.Logo.Width"),
                  ScreenProperties.getInt("DvdPlay.Logo.Height")
               );
            if (aIsLogoButton) {
               this.logo.addMouseListener(new MouseAdapter() {
                  public void mouseClicked(MouseEvent me) {
                     mainAction.actionPerformed(new ActionEvent(this, 1001, "DVDPlayLogo 0"));
                  }
               });

            }
         }
      }

      this.logo.setVisible(aShow);
   }

   public void setPromoCode(boolean show) {
      if (show) {
         this.tempIcon = ScreenProperties.getImage("PromoCode.Logo.IconOnBlack");
         if (this.panelBackground.getRGB() == -3407872) {
            this.tempIcon = ScreenProperties.getImage("PromoCode.Logo.IconOnRed");
         }

         this.promoCode = new JButton("", this.tempIcon);
         this.promoCode.setOpaque(true);
         this.promoCode
            .setBounds(
               ScreenProperties.getInt("PromoCode.Logo.LocationX"),
               ScreenProperties.getInt("PromoCode.Logo.LocationY"),
               ScreenProperties.getInt("PromoCode.Logo.Width"),
               ScreenProperties.getInt("PromoCode.Logo.Height")
            );
         this.promoCode.setBorder(BorderFactory.createEtchedBorder(0, this.panelBackground, this.panelBackground));
         this.promoCode.setActionCommand("PromoCodeDescriptionScreen");
      } else {
         this.promoCode = null;
      }
   }

   public void addActionListener(ActionListener l) {
      this.listenerList
         .add(
            ActionListener.class,
            l
         );
      this.mainAction = l;
      if (this.rightButton != null) {
         for (int i = 0; i < this.rightButton.getActionListeners().length; i++) {
            ActionListener t = this.rightButton.getActionListeners()[i];
            this.rightButton.removeActionListener(t);
         }

         this.rightButton.addActionListener(l);
      }

      if (this.centerButton != null) {
         for (int i = 0; i < this.centerButton.getActionListeners().length; i++) {
            ActionListener t = this.centerButton.getActionListeners()[i];
            this.centerButton.removeActionListener(t);
         }

         this.centerButton.addActionListener(l);
      }

      if (this.leftButton != null) {
         for (int i = 0; i < this.leftButton.getActionListeners().length; i++) {
            ActionListener t = this.leftButton.getActionListeners()[i];
            this.leftButton.removeActionListener(t);
         }

         this.leftButton.addActionListener(l);
      }

      if (this.leftestButton != null) {
         for (int i = 0; i < this.leftestButton.getActionListeners().length; i++) {
            ActionListener t = this.leftestButton.getActionListeners()[i];
            this.leftestButton.removeActionListener(t);
         }

         this.leftestButton.addActionListener(l);
      }

      if (this.promoCode != null) {
         this.promoCode.addActionListener(l);
      }
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
      if (this.rightButton != null) {
         this.rightButton.removeActionListener(l);
      }

      if (this.centerButton != null) {
         this.centerButton.removeActionListener(l);
      }

      if (this.leftButton != null) {
         this.leftButton.removeActionListener(l);
      }

      if (this.promoCode != null) {
         this.promoCode.removeActionListener(l);
      }
   }
}
