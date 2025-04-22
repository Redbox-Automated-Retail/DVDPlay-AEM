package net.dvdplay.hardware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.DiscIndex;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.DiscInventoryTableModel;
import net.dvdplay.userop.RemoveOp;
import net.dvdplay.userop.RemoveOpException;
import net.dvdplay.util.Util;

public class SimpleRemoveDiscs extends AbstractHardwareThread {
   static ActionListener action;
   static ActionListener removeDiscsAction;
   static JComponent component;
   static RemoveOp mRemoveOp = new RemoveOp();
   static DiscInventoryTableModel ditm;

   public SimpleRemoveDiscs() {
   }

   public SimpleRemoveDiscs(ActionListener lAction, JComponent lComponent) {
      action = lAction;
      component = lComponent;
   }

   public SimpleRemoveDiscs(ActionListener lAction, JComponent lComponent, ActionListener lRemoveDiscsAction, DiscInventoryTableModel itm) {
      removeDiscsAction = lRemoveDiscsAction;
      action = lAction;
      component = lComponent;
      ditm = itm;
   }

   public static String remove(String data) {
      JPanel statusPanel = (JPanel)component.getComponentAt(680, 10);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      JPanel centerPanel = (JPanel)component.getComponentAt(505, 200);
      JPanel buttonPanel = (JPanel)centerPanel.getComponent(0);

      for (int z = 0; z < buttonPanel.getComponentCount(); z++) {
         JButton but = (JButton)buttonPanel.getComponent(z);
         but.setEnabled(false);
      }

      new Thread() {
         @Override
         public void run() {
            StringTokenizer lStrTok = new StringTokenizer(data, DvdplayBase.COMMA);
            String discContent = lStrTok.nextToken();
            int lNumToks = lStrTok.countTokens();
            int lCounter = 0;
            Servo lServo = ServoFactory.getInstance();
            for (int i = 0; i < lNumToks; i++) {
               String a = lStrTok.nextToken();
               try {
                  SimpleRemoveDiscs.mRemoveOp.addDisc(Integer.parseInt(a));
               } catch (DvdplayException e) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
               }
            }
            if (!lServo.isInitialized()) {
               textarea.setText(new StringBuffer().append(textarea.getText()).append("Initializing ...\n").toString());
               lServo.initialize();
            }
            textarea.setText(new StringBuffer().append(textarea.getText()).append("Removing discs ...\n").toString());
            int i2 = 0;
            while (i2 < SimpleRemoveDiscs.mRemoveOp.getDiscCount()) {
               lCounter++;
               try {
                  SimpleRemoveDiscs.mRemoveOp.dispenseDisc(i2);
                  textarea.setText(new StringBuffer().append(textarea.getText()).append(lCounter).append(": Disc ").append(SimpleRemoveDiscs.mRemoveOp.getDiscItem(i2).getDiscDetailId()).append(" Slot=").append(SimpleRemoveDiscs.mRemoveOp.getDiscItem(i2).getSlot()).append(" removed (").append(SimpleRemoveDiscs.mRemoveOp.getDiscItem(i2).getGroupCode()).append(DvdplayBase.COMMA).append(SimpleRemoveDiscs.mRemoveOp.getDiscItem(i2).getDiscCode()).append(")\n").toString());
               } catch (RemoveOpException e2) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, e2.getMessage());
                  textarea.setText(new StringBuffer().append(textarea.getText()).append(lCounter).append(": Disc ").append(SimpleRemoveDiscs.mRemoveOp.getDiscItem(i2).getDiscDetailId()).append(" Slot=").append(SimpleRemoveDiscs.mRemoveOp.getDiscItem(i2).getSlot()).append(" failed\n").toString());
                  int i3 = i2;
                  i2 = i3 - 1;
                  SimpleRemoveDiscs.mRemoveOp.removeDisc(i3);
                  lServo.reseatDisc();
               }
               i2++;
            }
            SimpleRemoveDiscs.mRemoveOp.createQueueJob();
            DOMData.save();
            Aem.runQueueJobs();
            SimpleRemoveDiscs.removeDiscsAction.actionPerformed(new ActionEvent(this, 1001, "RemoveDiscs refresh"));
            for (int z2 = 0; z2 < buttonPanel.getComponentCount(); z2++) {
               JButton but2 = (JButton) buttonPanel.getComponent(z2);
               but2.setEnabled(true);
            }
            ArrayList removeList = SimpleRemoveDiscs.ditm.getSelected();
            for (int i4 = removeList.size() - 1; i4 >= 0; i4--) {
               int item = ((Integer) removeList.get(i4)).intValue();
               SimpleRemoveDiscs.ditm.getValueAt(item, 1).toString();
               SimpleRemoveDiscs.ditm.removeRow(item);
            }
            SimpleRemoveDiscs.ditm.refresh(discContent);
         }
      }.start();
      return null;
   }

   public static Object[][] getInventory(String type) {
      Aem.getDiscIndex();
      int ind = DiscIndex.size();
      Object[][] inventory = new Object[ind][10];
      int i = 0;

      for (int j = 0; j < ind; j++) {
         if (type.equals("unknownDisc")) {
            Aem.getDiscIndex();
            if (DiscIndex.getDiscIndexItem(j).getDiscDetailId() > 0) {
               continue;
            }
         } else if (type.equals("markedForRemoval")) {
            Aem.getDiscIndex();
            if (!DiscIndex.getDiscIndexItem(j).isMarkedForRemoval()) {
               continue;
            }
         }

         inventory[i][0] = false;
         Object[] var10000 = inventory[i];
         Aem.getDiscIndex();
         var10000[1] = DiscIndex.getDiscIndexItem(j).getSlot();
         var10000 = inventory[i];
         Aem.getDiscIndex();
         var10000[2] = DiscIndex.getDiscIndexItem(j).getDiscDetailId();
         var10000 = inventory[i];
         Aem.getDiscIndex();
         var10000[3] = new String(DiscIndex.getDiscIndexItem(j).getGroupCode());
         var10000 = inventory[i];
         Aem.getDiscIndex();
         var10000[4] = new String(DiscIndex.getDiscIndexItem(j).getDiscCode());
         var10000 = inventory[i];
         Aem.getDiscIndex();
         var10000[5] = new String(DiscIndex.getDiscIndexItem(j).getOriginalTitle());
         var10000 = inventory[i];
         Aem.getDiscIndex();
         var10000[6] = new String(Util.dateToString(DiscIndex.getDiscIndexItem(j).getDTUpdated()));
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItem(j).isMarkedForRent()) {
            inventory[i][7] = new String("1");
         } else {
            inventory[i][7] = new String("0");
         }

         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItem(j).isMarkedForSale()) {
            inventory[i][8] = new String("1");
         } else {
            inventory[i][8] = new String("0");
         }

         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItem(j).isMarkedForRemoval()) {
            inventory[i][9] = new String("1");
         } else {
            inventory[i][9] = new String("0");
         }

         i++;
      }

      return inventory;
   }
}
