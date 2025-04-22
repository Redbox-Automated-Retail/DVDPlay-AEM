package net.dvdplay.userop;

import java.util.logging.Level;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.Inventory;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.request.Request;
import net.dvdplay.request.RequestFactory;
import net.dvdplay.request.SynchRequest;
import net.dvdplay.task.QueueJob;

public class DiscFound extends DiscReturn {
   public DiscFound() {
      this.mQueueJobId = 5;
   }

   public DiscFound(DiscFound aDiscFound) {
      super(aDiscFound);
      this.mQueueJobId = 5;
   }

   public DiscFound(NvPairSet aNvPairSet) {
      super(aNvPairSet);
      this.mQueueJobId = 5;
   }

   public NvPairSet getDiscFoundNvPairSet() {
      return super.getDiscReturnedNvPairSet();
   }

   public void queueExecute() throws DiscActionException {
      try {
         if (!Aem.isStandAlone()) {
            String lrequeststring = new SynchRequest(Aem.getAemId(), "2.0", "DISC_FOUND", this.mNvPairSet).getAsXmlString();
            Aem.logDetailMessage(DvdplayLevel.FINER, lrequeststring);
            String lRet = Comm.sendRequest(lrequeststring, 300000);
            String lRequestId = this.mNvPairSet.getNvPair("RequestId").getValueAsString();
            Request lRequest = RequestFactory.makeRequest(lRet);
            String lServerRequestId = lRequest.getParameterValueAsString("RequestId");
            if (!lRequestId.equals(lServerRequestId)) {
               throw new DvdplayException("Incorrect RequestId. Expected: " + lRequestId + ", Recieved: " + lServerRequestId);
            } else {
               int lResult = Integer.parseInt(lRequest.getParameterValueAsString("Result"));
               if (lResult != 0) {
                  Aem.logDetailMessage(Level.WARNING, "DiscFound request failed");
                  String lMsg = lRequest.getParameterValueAsString("Message");
                  throw new DiscActionException(lMsg);
               } else {
                  RCSet lResultRCSet = lRequest.getParameterValueAsRCSet("Response");
                  RCSet lDiscRCSet = lRequest.getParameterValueAsRCSet("DiscRecord");
                  RCSet lTitleRCSet = lRequest.getParameterValueAsRCSet("TitleRecord");
                  RCSet lDiscReturnItemsRCSet = this.mNvPairSet.getNvPair("DiscItem").getValueAsRCSet();
                  if (lResultRCSet.rowCount() != lDiscReturnItemsRCSet.rowCount()) {
                     throw new DiscActionException("Number of discs found do not match number of results!");
                  } else {
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

                     if (newRCSet.rowCount() != 0) {
                        NvPairSet newNvPairSet = new NvPairSet();
                        newNvPairSet.add(new NvPair("RequestId", "int", lRequestId));
                        newNvPairSet.add(new NvPair("DiscItem", "RCSet", newRCSet));
                        this.mNvPairSet = newNvPairSet;
                        Aem.mQueue.addQueueJob(new QueueJob(new DiscReturn(this)), true);
                        Aem.logDetailMessage(DvdplayLevel.WARNING, "Some disc found failed");
                     }
                  }
               }
            }
         }
      } catch (DvdplayException var17) {
         Aem.logDetailMessage(Level.WARNING, var17.getMessage());
         throw new DiscActionException("DiscReturn.queueExceute failed");
      }
   }
}
