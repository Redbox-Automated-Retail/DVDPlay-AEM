package net.dvdplay.userop;

import net.dvdplay.aem.Aem;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.request.Request;
import net.dvdplay.request.RequestFactory;
import net.dvdplay.request.SynchRequest;
import net.dvdplay.task.QueueJob;
import net.dvdplay.util.Util;

public class DiscRemove extends DiscAction {
   public DiscRemove() {
      this.mQueueJobId = 3;
   }

   public DiscRemove(DiscRemove aDiscRemove) {
      super(aDiscRemove);
      this.mQueueJobId = 3;
   }

   public DiscRemove(NvPairSet aNvPairSet) {
      super(aNvPairSet);
      this.mQueueJobId = 3;
   }

   public void queueExecute() throws DiscActionException {
      try {
         if (!Aem.isStandAlone()) {
            String lrequeststring = new SynchRequest(Aem.getAemId(), "2.0", "DISC_REMOVED", this.mNvPairSet).getAsXmlString();
            Aem.logDetailMessage(DvdplayLevel.FINER, lrequeststring);
            Request lRequest = RequestFactory.makeRequest(Comm.sendRequest(lrequeststring, 300000));
            String lRequestId = this.mNvPairSet.getNvPair("RequestId").getValueAsString();
            String lServerRequestId = lRequest.getParameterValueAsString("RequestId");
            if (!lRequestId.equals(lServerRequestId)) {
               throw new DvdplayException("Incorrect RequestId. Expected: " + lRequestId + ", Recieved: " + lServerRequestId);
            }

            int lResult = Integer.parseInt(lRequest.getParameterValueAsString("Result"));
            if (lResult != 0) {
               String lMessage = lRequest.getParameterValueAsString("Message");
               Aem.logDetailMessage(DvdplayLevel.ERROR, lMessage);
               throw new DiscActionException("DiscRemove request failed");
            }

            RCSet lResultRCSet = lRequest.getParameterValueAsRCSet("Response");
            RCSet lDiscRemovedItemsRCSet = this.mNvPairSet.getNvPair("DiscItem").getValueAsRCSet();
            if (lResultRCSet.rowCount() != lDiscRemovedItemsRCSet.rowCount()) {
               throw new DiscActionException("Number of discs removed do not match number of results!");
            }

            for (int i = 0; i < lResultRCSet.rowCount(); i++) {
               if (Integer.parseInt(lResultRCSet.getFieldValue(i, lResultRCSet.getFieldIndex("Result"))) != 0) {
                  Aem.logDetailMessage(
                     DvdplayLevel.WARNING, "Response index " + i + ": " + lResultRCSet.getFieldValue(i, lResultRCSet.getFieldIndex("Message"))
                  );
               } else {
                  lDiscRemovedItemsRCSet.deleteRow(i);
               }
            }

            RCSet newRCSet = new RCSet(DvdplayBase.DISC_REMOVE_DISCITEM_FIELD_NAMES, DvdplayBase.DISC_REMOVE_DISCITEM_FIELD_TYPES);

            for (int ix = 0; ix < lDiscRemovedItemsRCSet.rowCount(); ix++) {
               if (!lDiscRemovedItemsRCSet.isDeleted(ix)) {
                  newRCSet.addRow(lDiscRemovedItemsRCSet.getRow(ix));
               }
            }

            if (newRCSet.rowCount() == 0) {
               return;
            }

            NvPairSet newNvPairSet = new NvPairSet();
            newNvPairSet.add(new NvPair("RequestId", "int", lRequestId));
            newNvPairSet.add(new NvPair("DiscItem", "RCSet", newRCSet));
            this.mNvPairSet = newNvPairSet;
            Aem.mQueue.addQueueJob(new QueueJob(new DiscRemove(this)), true);
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Some disc removed failed");
         }
      } catch (DvdplayException var12) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var12.getMessage());
         throw new DiscActionException("DiscRemove.queueExecute failed");
      }
   }

   public NvPairSet getDiscRemovedNvPairSet() throws RemoveOpException {
      NvPairSet lNvSet = new NvPairSet();
      String[] lRCSetValues = new String[DvdplayBase.DISC_REMOVE_DISCITEM_FIELD_NAMES.length];
      if (this.mDiscItemList.isEmpty()) {
         throw new RemoveOpException("No disc items in DiscItemList");
      } else {
         try {
            RCSet lRCSet = new RCSet(DvdplayBase.DISC_REMOVE_DISCITEM_FIELD_NAMES, DvdplayBase.DISC_REMOVE_DISCITEM_FIELD_TYPES);

             for (Object o : this.mDiscItemList) {
                 DiscItem lDiscItem = (DiscItem) o;
                 lRCSetValues[0] = String.valueOf(lDiscItem.getDiscDetailId());
                 lRCSetValues[1] = lDiscItem.getDiscCode();
                 lRCSetValues[2] = lDiscItem.getGroupCode();
                 lRCSetValues[3] = Util.dateToString(lDiscItem.getDTUpdated());
                 lRCSet.addRow(lRCSetValues);
             }

            lNvSet.add(new NvPair("RequestId", "int", Integer.toString(Aem.getNextIndex())));
            lNvSet.add(new NvPair("DiscItem", "RCSet", lRCSet));
            return lNvSet;
         } catch (DvdplayException var6) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var6.getMessage());
            throw new RemoveOpException("getDiscRemovedNvPairSet failed");
         }
      }
   }
}
