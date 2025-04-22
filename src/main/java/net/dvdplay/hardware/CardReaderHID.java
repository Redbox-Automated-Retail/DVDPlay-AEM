package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import net.dvdplay.aem.CreditCardThread;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.RCSet;
import net.dvdplay.util.Util;

public class CardReaderHID extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   static CreditCardThread mCreditCardThread = new CreditCardThread();

   public CardReaderHID(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public static String cancelSwipe(String data) {
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 10);
      JTextField status = (JTextField)statusPanel.getComponent(0);
      JPanel resultPanel = (JPanel)component.getComponentAt(10, 80);
      JScrollPane jsp = (JScrollPane)resultPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      JPanel buttonPanel = (JPanel)component.getComponentAt(620, 500);
      JButton startSwipe = (JButton)buttonPanel.getComponent(0);
      JButton cancelSwipe = (JButton)buttonPanel.getComponent(1);
      CreditCardThread.stopCardRead();
      textarea.setText("Cancelled.\n");
      status.setText("Cancelled");
      startSwipe.setEnabled(true);
      cancelSwipe.setEnabled(false);
      return "Dah";
   }

   public static String startSwipe(String data) {
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 10);
      JTextField status = (JTextField)statusPanel.getComponent(0);
      JPanel resultPanel = (JPanel)component.getComponentAt(10, 80);
      JScrollPane jsp = (JScrollPane)resultPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      JPanel buttonPanel = (JPanel)component.getComponentAt(620, 500);
      JButton startSwipe = (JButton)buttonPanel.getComponent(0);
      JButton cancelSwipe = (JButton)buttonPanel.getComponent(1);
      Thread def = new Thread() {
         @Override
         public void run() {
            startSwipe.setEnabled(false);
            cancelSwipe.setEnabled(true);
            if (Boolean.parseBoolean(data)) {
               new CreditCardThread(true).start();
            } else {
               new CreditCardThread(false).start();
            }
            status.setText("Swipe card ... ");
            textarea.setText("");
            while (!CreditCardThread.readDone) {
               Util.sleep(100);
            }
            if (CreditCardThread.stopped) {
               return;
            }
            if (CreditCardThread.getCreditCardTrack() == null) {
               textarea.setText("Bad swipe.");
            } else {
               try {
                  DataPacketComposer lComposer = new DataPacketComposer();
                  RCSet lTrackRCSet = lComposer.rcDeMarshal(CreditCardThread.getCreditCardTrack());
                  for (int i = 0; i < lTrackRCSet.rowCount(); i++) {
                     textarea.setText(textarea.getText() + "track" + (i + 1) + "=");
                     textarea.setText(textarea.getText() + lTrackRCSet.getFieldValue(i, 0) + "\n");
                  }
                  status.setText("Card read.");
               } catch (Exception e) {
                  status.setText("Can not read card.");
               }
            }
            startSwipe.setEnabled(true);
            cancelSwipe.setEnabled(false);
         }
      };

      def.start();
      return null;
   }
}
