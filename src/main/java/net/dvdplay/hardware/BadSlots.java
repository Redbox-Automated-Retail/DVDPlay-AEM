package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JComponent;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.dom.DOMData;
import net.dvdplay.logger.DvdplayLevel;

public class BadSlots extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;

   public BadSlots() {
   }

   public BadSlots(ActionListener lAction, JComponent lComponent) {
      action = lAction;
      component = lComponent;
   }

   public static String remove(String data) {
      try {
         StringTokenizer stk = new StringTokenizer(data, ",");

         while (stk.hasMoreElements()) {
            Aem.removeBadSlot(Integer.parseInt(stk.nextToken()));
         }
      } catch (Exception var2) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "BadSlots remove: " + var2.getMessage());
      }

      return null;
   }

   public static String save(String data) {
      try {
         StringTokenizer stk = new StringTokenizer(data, ",");

         while (stk.hasMoreElements()) {
            StringTokenizer stk2 = new StringTokenizer(stk.nextToken(), ":");
            int a = Integer.parseInt(stk2.nextToken());
            int b = Integer.parseInt(stk2.nextToken());
            Aem.addBadSlot(a, b);
         }

         DOMData.save();
      } catch (Exception var5) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Badlot save: " + var5.getMessage());
      }

      return null;
   }

   public static Vector getBadEntry() {
      Vector entry = new Vector();
      int counter = 0;

      for (int i = 0; i < DOMData.mBadSlotData.rowCount(); i++) {
         if (!DOMData.mBadSlotData.isDeleted(i)) {
            Object[] def = new Object[]{
               false,
               DOMData.mBadSlotData.getFieldValue(i, DOMData.mBadSlotData.getFieldIndex("BadSlot")),
               DOMData.mBadSlotData.getFieldValue(i, DOMData.mBadSlotData.getFieldIndex("BadSlotId"))
            };
            entry.add(counter++, def);
         }
      }

      return entry;
   }
}
