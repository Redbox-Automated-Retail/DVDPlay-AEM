package net.dvdplay.screen;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.view.KeyboardAssembler;

public class ErrorLogPanel extends AbstractToolsPanel {
   JPanel sharedParametersPanel;
   JPanel servoParametersUnderThresholdPanel;
   JPanel servoParametersOverThresholdPanel;
   JPanel miscParamsPanel;
   JPanel buttonPanel;

   public ErrorLogPanel() {
      this.setLayout(null);
      this.buttonPanel = this.createButtonPanel();
      this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
      this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
      this.buttonPanel.setBounds(490, 470, 100, 40);
      this.ka.setBounds(5, 530, 990, 120);
      this.add(this.buttonPanel);
      this.add(this.ka);
      this.setVisible(true);
   }

   private JPanel createButtonPanel() {
      JPanel temp = new JPanel();
      temp.setLayout(new FlowLayout());
      JButton save = this.createButton("Send Log", "ErrorLog sendLog");
      temp.add(save);
      return temp;
   }
}
