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

import net.dvdplay.aem.*;
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

public class RemoveDiscs extends AbstractHardwareThread {
   static ActionListener action;
   static ActionListener removeDiscsAction;
   static JComponent component;
   static RemoveOp mRemoveOp = new RemoveOp();
   static DiscInventoryTableModel ditm;
   private static boolean mServoInitialized = false;

   public RemoveDiscs() {
   }

   public RemoveDiscs(ActionListener lAction, JComponent lComponent) {
      action = lAction;
      component = lComponent;
   }

   public RemoveDiscs(ActionListener lAction, JComponent lComponent, ActionListener lRemoveDiscsAction, DiscInventoryTableModel itm) {
      removeDiscsAction = lRemoveDiscsAction;
      action = lAction;
      component = lComponent;
      ditm = itm;
   }

   protected static ServoEx getServoEx() {
      Servo lServo = ServoFactory.getInstance();
      if (lServo != null && lServo instanceof ServoEx) {
         return (ServoEx)lServo;
      } else {
         throw new AemException("RemoveDiscs: Incorrect servo type.");
      }
   }

   protected static Roller getRoller() {
      return ServoFactory.getInstance().getRoller();
   }

   public static String remove(String data) {
      JPanel statusPanel = (JPanel)component.getComponentAt(680, 10);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      JPanel centerPanel = (JPanel)component.getComponentAt(520, 10);
      JPanel buttonPanel = (JPanel)centerPanel.getComponent(1);

      for (int z = 0; z < 5; z++) {
         if (z != 2) {
            JButton but = (JButton)buttonPanel.getComponent(z);
            but.setEnabled(false);
         }
      }

      Aem.logSummaryMessage("Removing discs ...");
      Aem.logDetailMessage(DvdplayLevel.FINE, "Removing " + data);
      new Thread() {
         @Override
         public void run() {
            StringTokenizer lStrTok = new StringTokenizer(data, DvdplayBase.COMMA);
            String discContent = lStrTok.nextToken();
            int lNumToks = lStrTok.countTokens();
            int lCounter = 0;
            Aem lAem = AemFactory.getInstance();
            for (int i = 0; i < lNumToks; i++) {
               String a = lStrTok.nextToken();
               try {
                  RemoveDiscs.mRemoveOp.addDisc(Integer.parseInt(a));
               } catch (DvdplayException e) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, e.getMessage());
               }
            }
            Servo lServo = ServoFactory.getInstance();
            if (!RemoveDiscs.mServoInitialized) {
               textarea.setText(new StringBuffer().append(textarea.getText()).append("Initializing ...\n").toString());
               lServo.initialize();
               boolean unused = RemoveDiscs.mServoInitialized = true;
            }
            textarea.setText(new StringBuffer().append(textarea.getText()).append("Removing discs ...\n").toString());
            Aem.setDiscActive();
            int i2 = 0;
            while (i2 < RemoveDiscs.mRemoveOp.getDiscCount()) {
               lCounter++;
               try {
               } catch (RemoveOpException e2) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, e2.getMessage());
                  String lMsg = "Disc " + RemoveDiscs.mRemoveOp.getDiscItem(i2).getDiscDetailId() + " Slot=" + RemoveDiscs.mRemoveOp.getDiscItem(i2).getSlot() + " failed";
                  Aem.logDetailMessage(DvdplayLevel.INFO, lMsg);
                  textarea.setText(new StringBuffer().append(textarea.getText()).append(lCounter).append(": ").append(lMsg).append("\n").toString());
                  int i3 = i2;
                  i2 = i3 - 1;
                  RemoveDiscs.mRemoveOp.removeDisc(i3);
                  Roller lRoller = RemoveDiscs.getRoller();
                  if (lRoller != null) {
                     Roller.stop();
                  }
               }
               if (Aem.inQuiesceMode()) {
                  throw new RemoveOpException("Quiesce Mode! Aborting.");
               }
               RemoveDiscs.mRemoveOp.dispenseDisc(i2);
               String lMsg2 = "Disc " + RemoveDiscs.mRemoveOp.getDiscItem(i2).getDiscDetailId() + " Slot=" + RemoveDiscs.mRemoveOp.getDiscItem(i2).getSlot() + " removed (" + RemoveDiscs.mRemoveOp.getDiscItem(i2).getGroupCode() + DvdplayBase.COMMA + RemoveDiscs.mRemoveOp.getDiscItem(i2).getDiscCode() + ")";
               Aem.logDetailMessage(DvdplayLevel.INFO, lMsg2);
               textarea.setText(textarea.getText() + lCounter + ": " + lMsg2 + "\n");
               i2++;
            }
            RemoveDiscs.mRemoveOp.createQueueJob();
            DOMData.save();
            Aem.resetDiscActive();
            if (Aem.inQuiesceMode()) {
               lAem.exitApp(Aem.getQuiesceMode());
            } else {
               Aem.runQueueJobs();
            }
            RemoveDiscs.removeDiscsAction.actionPerformed(new ActionEvent(this, 1001, "RemoveDiscs refresh"));
            for (int z2 = 0; z2 < 5; z2++) {
               if (z2 != 2) {
                  JButton but2 = (JButton) buttonPanel.getComponent(z2);
                  but2.setEnabled(true);
               }
            }
            ArrayList removeList = RemoveDiscs.ditm.getSelected();
            for (int i4 = removeList.size() - 1; i4 >= 0; i4--) {
               int item = ((Integer) removeList.get(i4)).intValue();
               RemoveDiscs.ditm.getValueAt(item, 2).toString();
               RemoveDiscs.ditm.removeRow(item);
            }
            RemoveDiscs.ditm.refresh(discContent);
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
            if (DiscIndex.getDiscIndexItem(j).getTitleDetailId() != 0) {
               continue;
            }
         } else if (type.equals("markedForRemoval")) {
            Aem.getDiscIndex();
            if (!DiscIndex.getDiscIndexItem(j).isMarkedForRemoval()) {
               continue;
            }
         }

         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItem(j).getSlot() != 0) {
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
      }

      return inventory;
   }
}
