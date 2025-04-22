package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ApplicationInitializing extends JFrame {
   JPanel bottomBar;
   JPanel topBar;
   JLabel text1;
   JLabel text2;
   JLabel aGif;

   public ApplicationInitializing() {
      JPanel content = (JPanel)this.getContentPane();
      content.setLayout(null);
      content.setBackground(Color.WHITE);
      this.setUndecorated(true);
      this.text1 = new JLabel("AEM Initializing");
      this.text1.setForeground(new Color(204, 0, 0));
      this.text1.setBackground(Color.WHITE);
      this.text1.setFont(new Font("Arial", 1, 20));
      this.text1.setHorizontalAlignment(0);
      this.text1.setBounds(0, 360, 1024, 30);
      this.text2 = new JLabel("Please Wait ...");
      this.text2.setForeground(new Color(204, 0, 0));
      this.text2.setBackground(Color.WHITE);
      this.text2.setFont(new Font("Arial", 1, 20));
      this.text2.setHorizontalAlignment(0);
      this.text2.setBounds(0, 390, 1024, 30);
      this.bottomBar = new JPanel();
      this.bottomBar.setBounds(0, 690, 1024, 78);
      this.bottomBar.setBackground(Color.BLACK);
      this.topBar = new JPanel();
      this.topBar.setBounds(0, 0, 1024, 60);
      this.topBar.setBackground(new Color(204, 0, 0));
      content.add(this.bottomBar);
      content.add(this.topBar);
      content.add(this.text1);
      content.add(this.text2);
      this.setVisible(true);
      this.setExtendedState(6);
   }
}
