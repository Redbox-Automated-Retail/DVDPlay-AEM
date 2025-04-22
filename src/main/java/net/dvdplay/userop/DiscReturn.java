package net.dvdplay.userop;

import java.util.logging.Level;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.communication.RDataSetFieldValues;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.Inventory;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.request.Request;
import net.dvdplay.request.RequestFactory;
import net.dvdplay.request.SynchRequest;
import net.dvdplay.task.QueueJob;
import net.dvdplay.util.Util;

public class DiscReturn extends DiscAction {
   public DiscReturn() {
      this.mQueueJobId = 2;
   }

   public DiscReturn(DiscReturn aDiscReturn) {
      super(aDiscReturn);
      this.mQueueJobId = 2;
   }

   public DiscReturn(NvPairSet aNvPairSet) {
      super(aNvPairSet);
      this.mQueueJobId = 2;
   }

   public void queueExecute() throws DiscActionException {
      try {
         if (!Aem.isStandAlone()) {
            String lrequeststring = new SynchRequest(Aem.getAemId(), "2.0", "DISC_RETURNED", this.mNvPairSet).getAsXmlString();
            Aem.logDetailMessage(DvdplayLevel.FINER, lrequeststring);
            String lRet = Comm.sendRequest(lrequeststring, 300000);
            Request lRequest = RequestFactory.makeRequest(lRet);
            String lRequestId = this.mNvPairSet.getNvPair("RequestId").getValueAsString();
            String lServerRequestId = lRequest.getParameterValueAsString("RequestId");
            if (!lRequestId.equals(lServerRequestId)) {
               throw new DvdplayException("Incorrect RequestId. Expected: " + lRequestId + ", Recieved: " + lServerRequestId);
            }

            int lResult = Integer.parseInt(lRequest.getParameterValueAsString("Result"));
            if (lResult != 0) {
               Aem.logDetailMessage(Level.WARNING, "DiscReturn request failed");
               String lMsg = lRequest.getParameterValueAsString("Message");
               throw new DiscActionException(lMsg);
            }

            RCSet lResultRCSet = lRequest.getParameterValueAsRCSet("Response");
            RCSet lDiscRCSet = lRequest.getParameterValueAsRCSet("DiscRecord");
            RCSet lTitleRCSet = lRequest.getParameterValueAsRCSet("TitleRecord");
            RCSet lDiscReturnItemsRCSet = this.mNvPairSet.getNvPair("DiscItem").getValueAsRCSet();
            if (lResultRCSet.rowCount() != lDiscReturnItemsRCSet.rowCount()) {
               throw new DiscActionException("Number of discs returned do not match number of results!");
            }

            for (int i = 0; i < lResultRCSet.rowCount(); i++) {
               int lDiscResult = Integer.parseInt(lResultRCSet.getFieldValue(i, lResultRCSet.getFieldIndex("Result")));
               if (lDiscResult != 0) {
                  String lDiscMessage = lResultRCSet.getFieldValue(i, lResultRCSet.getFieldIndex("Message"));
                  Aem.logDetailMessage(Level.WARNING, "Response index " + i + ": " + lDiscMessage);
                  if (lDiscResult <= -100 && lDiscResult > -200) {
                     Aem.logDetailMessage(Level.WARNING, "Not going to retry");
                     lDiscReturnItemsRCSet.deleteRow(i);
                  }
               } else {
                  RCSet lTmpDiscRCSet = new RCSet(DvdplayBase.DISC_DETAIL_FIELD_NAMES, DvdplayBase.DISC_DETAIL_FIELD_TYPES);
                  int lTmpTitleDetailId = Integer.parseInt(lDiscRCSet.getRow(i).getValue(lTmpDiscRCSet.getFieldIndex("TitleDetailId")));
                  lTmpDiscRCSet.addRow(lDiscRCSet.getRow(i));

                  for (int j = 0; j < lTitleRCSet.rowCount(); j++) {
                     if (!lTitleRCSet.isDeleted(j)) {
                        RCSet lTmpTitleRCSet = new RCSet(DvdplayBase.TITLE_DETAIL_FIELD_NAMES, DvdplayBase.TITLE_DETAIL_FIELD_TYPES);
                        if (Integer.parseInt(lTitleRCSet.getFieldValue(j, lTitleRCSet.getFieldIndex("TitleDetailId"))) == lTmpTitleDetailId) {
                           lTmpTitleRCSet.addRow(lTitleRCSet.getRow(j));
                        }

                        Inventory.updateTitle(lTmpTitleRCSet);
                     }
                  }

                  Inventory.updateDisc(lTmpDiscRCSet);
                  lDiscReturnItemsRCSet.deleteRow(i);
               }
            }

            Aem.getPosters();
            RCSet newRCSet = new RCSet(DvdplayBase.DISC_RETURN_DISCITEM_FIELD_NAMES, DvdplayBase.DISC_RETURN_DISCITEM_FIELD_TYPES);

            for (int ix = 0; ix < lDiscReturnItemsRCSet.rowCount(); ix++) {
               if (!lDiscReturnItemsRCSet.isDeleted(ix)) {
                  newRCSet.addRow(lDiscReturnItemsRCSet.getRow(ix));
               }
            }

            if (newRCSet.rowCount() == 0) {
               return;
            }

            NvPairSet newNvPairSet = new NvPairSet();
            newNvPairSet.add(new NvPair("RequestId", "int", lRequestId));
            newNvPairSet.add(new NvPair("DiscItem", "RCSet", newRCSet));
            this.mNvPairSet = newNvPairSet;
            Aem.mQueue.addQueueJob(new QueueJob(new DiscReturn(this)), true);
            Aem.logDetailMessage(DvdplayLevel.WARNING, "Some disc returns failed");
         } else {
            RCSet lDiscReturnRCSet = this.mNvPairSet.getNvPair("DiscItem").getValueAsRCSet();
            RCSet lRCSet = new RCSet(DvdplayBase.DISC_DETAIL_FIELD_NAMES, DvdplayBase.DISC_DETAIL_FIELD_TYPES);

            for (int ixx = 0; ixx < lDiscReturnRCSet.rowCount(); ixx++) {
               if (!lDiscReturnRCSet.isDeleted(ixx)) {
                  String lDiscCode = lDiscReturnRCSet.getFieldValue(ixx, lDiscReturnRCSet.getFieldIndex("DiscCode"));
                  String lGroupCode = lDiscReturnRCSet.getFieldValue(ixx, lDiscReturnRCSet.getFieldIndex("GroupCode"));
                  String lSlot = lDiscReturnRCSet.getFieldValue(ixx, lDiscReturnRCSet.getFieldIndex("Slot"));
                  int lIndex = -1;

                  for (int jx = 0; jx < DOMData.mDiscDetailData.rowCount(); jx++) {
                     if (!DOMData.mDiscDetailData.isDeleted(jx)) {
                        String lTmpDiscCode = DOMData.mDiscDetailData.getFieldValue(jx, DOMData.mDiscDetailData.getFieldIndex("DiscCode"));
                        String lTmpGroupCode = DOMData.mDiscDetailData.getFieldValue(jx, DOMData.mDiscDetailData.getFieldIndex("GroupCode"));
                        String lTmpDiscDetailId = DOMData.mDiscDetailData.getFieldValue(jx, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"));
                        if (lTmpDiscCode.compareTo(lDiscCode) == 0 && lTmpGroupCode.compareTo(lGroupCode) == 0 && Integer.parseInt(lTmpDiscDetailId) > 0) {
                           lIndex = jx;
                           break;
                        }
                     }
                  }

                  if (lIndex >= 0) {
                     RDataSetFieldValues lRDSFV = DOMData.mDiscDetailData.getRow(lIndex);
                     lRDSFV.setValue(DOMData.mDiscDetailData.getFieldIndex("DiscStatusId"), Integer.toString(3));
                     lRDSFV.setValue(DOMData.mDiscDetailData.getFieldIndex("Slot"), lSlot);
                     lRCSet.addRow(lRDSFV);
                  }
               }
            }

            if (lRCSet.rowCount() > 0) {
               Aem.updateDisc(lRCSet);
            }
         }
      } catch (DvdplayException var17) {
         Aem.logDetailMessage(Level.WARNING, var17.getMessage());
         throw new DiscActionException("DiscReturn.queueExceute failed");
      }
   }

   public NvPairSet getDiscReturnedNvPairSet() {
      NvPairSet lNvSet = new NvPairSet();
      String[] lRCSetValues = new String[DvdplayBase.DISC_RETURN_DISCITEM_FIELD_NAMES.length];

      try {
         new DataPacketComposer();
         RCSet lRCSet = new RCSet(DvdplayBase.DISC_RETURN_DISCITEM_FIELD_NAMES, DvdplayBase.DISC_RETURN_DISCITEM_FIELD_TYPES);

          for (Object o : this.mDiscItemList) {
              DiscItem lDiscItem = (DiscItem) o;
              lRCSetValues[0] = lDiscItem.getDiscCode();
              lRCSetValues[1] = lDiscItem.getGroupCode();
              lRCSetValues[2] = String.valueOf(lDiscItem.getSlot());
              lRCSetValues[3] = Util.dateToString(lDiscItem.getDTUpdated());
              lRCSet.addRow(lRCSetValues);
          }

         lNvSet.add(new NvPair("RequestId", "int", Integer.toString(Aem.getNextIndex())));
         lNvSet.add(new NvPair("DiscItem", "RCSet", lRCSet));
         return lNvSet;
      } catch (DvdplayException var7) {
         throw new DvdplayException(var7.getMessage());
      }
   }
}
