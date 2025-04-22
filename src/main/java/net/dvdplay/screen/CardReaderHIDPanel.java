package net.dvdplay.screen;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractToolsPanel;
import net.dvdplay.hardware.CardReaderHID;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.view.KeyboardAssembler;

public class CardReaderHIDPanel extends AbstractToolsPanel {
   private JPanel statusDisplay;
   private JPanel buttonPanel;
   private JPanel resultDisplay;
   private ActionListener al;
   private ActionListener cardReaderHIDAction;
   JCheckBox stripSentinels;
   CardReaderHID cardReaderHID;

   public CardReaderHIDPanel() {
      try {
         this.setLayout(null);
         this.al = new AbstractToolsPanel.ActionTools();
         this.cardReaderHIDAction = new CardReaderHIDPanel.CardReaderHIDAction();
         this.cardReaderHID = new CardReaderHID(this.al, this);
         this.buttonPanel = this.createButtonPanel();
         this.statusDisplay = this.createStatusDisplayPanel();
         this.resultDisplay = this.createResultDisplayPanel();
         this.ka = new KeyboardAssembler("admin", "Upper", 44, 40, 1);
         this.ka.addActionListener(new AbstractToolsPanel.KeyTools());
         this.ka.addChangeListener(new AbstractToolsPanel.VolumeTools());
         this.statusDisplay.setBounds(10, 10, 400, 60);
         this.resultDisplay.setBounds(10, 80, 700, 400);
         this.buttonPanel.setBounds(550, 480, 400, 40);
         this.ka.setBounds(5, 530, 990, 120);
         this.add(this.statusDisplay);
         this.add(this.resultDisplay);
         this.add(this.buttonPanel);
         this.add(this.ka);
         this.setVisible(true);
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[CardReaderHIDPanel] " + var2.toString(), var2);
      }
   }

   public JPanel createButtonPanel() {
      JPanel temp = new JPanel(new GridLayout(0, 3, 15, 15));
      JButton startSwipe = this.createButton("Start Swipe", "startSwipe", this.cardReaderHIDAction);
      JButton cancelSwipe = this.createButton("Cancel Swipe", "CardReaderHID cancelSwipe");
      cancelSwipe.setEnabled(false);
      this.stripSentinels = new JCheckBox("Strip sentinels");
      temp.add(startSwipe);
      temp.add(cancelSwipe);
      temp.add(this.stripSentinels);
      return temp;
   }

   public JPanel createStatusDisplayPanel() {
      JPanel temp = new JPanel();
      JTextField status = new JTextField(20);
      status.setEditable(false);
      temp.setBorder(new TitledBorder("Status"));
      temp.add(status);
      return temp;
   }

   public JPanel createResultDisplayPanel() {
      JPanel temp = new JPanel();
      JTextArea status = new JTextArea(20, 50);
      status.setEditable(false);
      status.setWrapStyleWord(true);
      JScrollPane jsp = new JScrollPane(status);
      temp.setBorder(new TitledBorder("Result"));
      temp.add(jsp);
      return temp;
   }

   private class CardReaderHIDAction implements ActionListener {
      private CardReaderHIDAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         CardReaderHIDPanel.this.command = ae.getActionCommand();
         Aem.logDetailMessage(DvdplayLevel.FINER, "[CardReaderHIDPanel] Action " + CardReaderHIDPanel.this.command);
         if (CardReaderHIDPanel.this.command.equals("startSwipe")) {
            String data = CardReaderHIDPanel.this.stripSentinels.isSelected() ? "true" : "false";
            CardReaderHIDPanel.this.al.actionPerformed(new ActionEvent(this, 1001, "CardReaderHID startSwipe " + data));
         }
      }
   }
}
