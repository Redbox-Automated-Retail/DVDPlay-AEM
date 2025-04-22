package net.dvdplay.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;

public class Util {
   public static JPanel getFranchiseInfoPanel() {
      JPanel franchiseInfoPanel = new JPanel(null);
      JLabel[] text = new JLabel[6];
      franchiseInfoPanel.setBackground(ScreenProperties.getColor("White"));

      for (int i = 0; i < text.length; i++) {
         text[i] = new JLabel();
         text[i].setForeground(ScreenProperties.getColor(i == 0 ? "Red" : "Black"));
         text[i].setBackground(ScreenProperties.getColor("White"));
         text[i].setFont(ScreenProperties.getFont("TimeOutScreen"));
         text[i].setBounds(0, i * 25, 500, 25);
         franchiseInfoPanel.add(text[i]);
      }

      updateFranchiseInfoPanel(franchiseInfoPanel);
      return franchiseInfoPanel;
   }

   public static void updateFranchiseInfoPanel(JPanel aFranchiseInfoPanel) {
      JLabel[] lText = new JLabel[6];

      for (int i = 0; i < aFranchiseInfoPanel.getComponentCount(); i++) {
         lText[i] = (JLabel)aFranchiseInfoPanel.getComponent(i);
      }

      lText[0].setText(Aem.getString(2012));
      lText[1].setText(Aem.getString(2013));
      if (Aem.getString(2014).trim().equals("")) {
         lText[2].setVisible(false);
         lText[3].setBounds(0, 50, 500, 25);
         lText[4].setBounds(0, 75, 500, 25);
         lText[5].setBounds(0, 100, 500, 25);
      } else {
         lText[2].setText(Aem.getString(2014));
         lText[3].setBounds(0, 75, 500, 25);
         lText[4].setBounds(0, 100, 500, 25);
         lText[5].setBounds(0, 150, 500, 25);
      }

      lText[3].setText(Aem.getString(2015));
      lText[4].setText(Aem.getString(2016));
      lText[5].setText(Aem.getString(2017));
   }
}
