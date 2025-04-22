package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import net.dvdplay.aem.Aem;
import net.dvdplay.logger.DvdplayLevel;

public class AEMStartupErrorScreen extends JPanel {
   JLabel text1;
   JLabel text2;
   JButton ok1;
   JButton ok2;
   JButton ok3;
   JButton ok4;
   String cmdStr = "";
   ActionListener hiddenAction;
   private Timer hiddenTimer = new Timer(3000, ae -> AEMStartupErrorScreen.this.cmdStr = "");



   public AEMStartupErrorScreen() {
      try {
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         UIManager.getDefaults().put("Button.select", Color.WHITE);
         this.hiddenAction = new AEMStartupErrorScreen.HiddenAction();
         this.text1 = new JLabel("ERROR");
         this.text1.setForeground(Color.RED);
         this.text1.setBackground(Color.WHITE);
         this.text1.setFont(new Font("Arial", 1, 20));
         this.text1.setHorizontalAlignment(0);
         this.text1.setBounds(0, 360, 1024, 30);
         this.text2 = new JLabel("We're sorry, our system is temporarily unavailable.  Please try back later.");
         this.text2.setForeground(Color.RED);
         this.text2.setBackground(Color.WHITE);
         this.text2.setFont(new Font("Arial", 1, 20));
         this.text2.setHorizontalAlignment(0);
         this.text2.setBounds(0, 390, 1024, 30);
         this.ok1 = new JButton();
         this.ok1.setBounds(0, 0, 200, 200);
         this.ok1.setSize(200, 200);
         this.ok1.setForeground(Color.WHITE);
         this.ok1.setBackground(Color.WHITE);
         this.ok1.setBorder(null);
         this.ok1.setActionCommand("1");
         this.ok1.addActionListener(this.hiddenAction);
         this.ok2 = new JButton();
         this.ok2.setBounds(824, 0, 200, 200);
         this.ok2.setSize(200, 200);
         this.ok2.setForeground(Color.WHITE);
         this.ok2.setBackground(Color.WHITE);
         this.ok2.setBorder(null);
         this.ok2.setActionCommand("2");
         this.ok2.addActionListener(this.hiddenAction);
         this.ok3 = new JButton();
         this.ok3.setBounds(0, 568, 200, 200);
         this.ok3.setSize(200, 200);
         this.ok3.setForeground(Color.WHITE);
         this.ok3.setBackground(Color.WHITE);
         this.ok3.setBorder(null);
         this.ok3.setActionCommand("3");
         this.ok3.addActionListener(this.hiddenAction);
         this.ok4 = new JButton();
         this.ok4.setBounds(824, 568, 200, 200);
         this.ok4.setSize(200, 200);
         this.ok4.setForeground(Color.WHITE);
         this.ok4.setBackground(Color.WHITE);
         this.ok4.setBorder(null);
         this.ok4.setActionCommand("4");
         this.ok4.addActionListener(this.hiddenAction);
         this.add(this.text1);
         this.add(this.text2);
         this.add(this.ok1);
         this.add(this.ok2);
         this.add(this.ok3);
         this.add(this.ok4);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         Aem.logDetailMessage(DvdplayLevel.INFO, "* AEMStartupErrorScreen");
      } catch (Exception var2) {
         var2.printStackTrace(System.err);
      }
   }

   private class HiddenAction implements ActionListener {
      private HiddenAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         AEMStartupErrorScreen.this.cmdStr = AEMStartupErrorScreen.this.cmdStr + ae.getActionCommand();
         if (AEMStartupErrorScreen.this.cmdStr.equals("1234")) {
            Aem.logDetailMessage(DvdplayLevel.FINE, "[AEMStartupErrorScreen] Cmd ExitScreen");
            System.exit(0);
         }

         if (AEMStartupErrorScreen.this.cmdStr.length() == 1) {
            AEMStartupErrorScreen.this.hiddenTimer.start();
         }
      }
   }
}
