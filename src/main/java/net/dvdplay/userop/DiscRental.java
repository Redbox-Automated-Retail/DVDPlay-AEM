package net.dvdplay.userop;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.request.Request;
import net.dvdplay.request.RequestFactory;
import net.dvdplay.request.SynchRequest;
import net.dvdplay.util.Util;

public class DiscRental extends DiscAction {
   private int mTransactionId;
   private String mEmailAddr;
   private String mTrack;
   private BigDecimal mSubTotal;
   private BigDecimal mTaxAmount;
   private BigDecimal mGrandTotal;
   private String mPromoCode;
   private BigDecimal mPromoValue;
   private int mPaymentCardTypeId;
   private int mVerificationTypeId;
   private String mVerificationString;
   public Date mTransactionTime;
   private BigDecimal mPromoValueApplied;

   public BigDecimal getPromoValueApplied() {
      return this.mPromoValueApplied;
   }

   public void setPromoValueApplied(BigDecimal aPromoValueApplied) {
      this.mPromoValueApplied = aPromoValueApplied;
   }

   public int getVerificationTypeId() {
      return this.mVerificationTypeId;
   }

   public void setVerificationTypeId(int mVerificationTypeId) {
      this.mVerificationTypeId = mVerificationTypeId;
   }

   public String getVerificationString() {
      return this.mVerificationString;
   }

   public void setVerificationString(String aVerificationString) {
      this.mVerificationString = aVerificationString;
   }

   public Date getTransactionTime() {
      return this.mTransactionTime;
   }

   public int getTransactionId() {
      return this.mTransactionId;
   }

   public void setTransactionId(int aTransactionId) {
      this.mTransactionId = aTransactionId;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public String getEmailAddr() {
      return this.mEmailAddr;
   }

   public void setEmailAddr(String aEmailAddr) {
      Aem.logSummaryMessage("Email address is " + aEmailAddr);
      this.mEmailAddr = aEmailAddr;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public String getTrack() {
      return this.mTrack;
   }

   public void setTrack(String aTrack) {
      this.mTrack = aTrack;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public BigDecimal getSubTotal() {
      return this.mSubTotal;
   }

   private void setSubTotal(BigDecimal aSubTotal) {
      this.mSubTotal = aSubTotal;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public BigDecimal getTaxAmount() {
      return this.mTaxAmount;
   }

   private void setTaxAmount(BigDecimal aTaxAmount) {
      this.mTaxAmount = aTaxAmount;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public BigDecimal getGrandTotal() {
      return this.mGrandTotal;
   }

   private void setGrandTotal(BigDecimal aGrandTotal) {
      this.mGrandTotal = aGrandTotal;
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public String getPromoCode() {
      return this.mPromoCode;
   }

   public BigDecimal getPromoValue() {
      return this.mPromoValue;
   }

   public void setPromo(String aPromoCode, BigDecimal aPromoValue) {
      this.mPromoCode = aPromoCode;
      this.mPromoValue = aPromoValue;
      this.calculateTotals(false);
      this.mDTUpdated = new Date(System.currentTimeMillis());
   }

   public Date getDTUpdated() {
      return this.mDTUpdated;
   }

   public int getPaymentCardTypeId() {
      return this.mPaymentCardTypeId;
   }

   public void setPaymentCardTypeId(int aPaymentCardTypeId) {
      this.mPaymentCardTypeId = aPaymentCardTypeId;
   }

   public void calculateTotals(boolean aIsConfirm) {
      this.calculateTotals(aIsConfirm, false);
   }

   public void calculateTotals(boolean aIsConfirm, boolean aLogValues) {
      this.mSubTotal = new BigDecimal(0.0);
      this.mTaxAmount = new BigDecimal(0.0);
      this.mGrandTotal = new BigDecimal(0.0);

      for (int i = 0; i < this.mDiscItemList.size(); i++) {
         DiscItem lDiscItem = (DiscItem)this.mDiscItemList.get(i);
         if (lDiscItem.isDispensed() || !aIsConfirm) {
            if (lDiscItem.getOperationId() == 1) {
               this.mSubTotal = this.mSubTotal.add(Aem.getPricing(lDiscItem.getDisc().getPriceOptionId(), this.mTransactionTime).getRentalPrice());
            } else {
               this.mSubTotal = this.mSubTotal.add(Aem.getPricing(lDiscItem.getDisc().getPriceOptionId(), this.mTransactionTime).getUsedPrice());
            }
         }
      }

      if (this.mSubTotal.compareTo(this.mPromoValue) < 0) {
         this.mPromoValueApplied = this.mSubTotal;
         this.mSubTotal = new BigDecimal(0.0);
      } else {
         this.mPromoValueApplied = this.mPromoValue;
         this.mSubTotal = this.mSubTotal.subtract(this.mPromoValue);
      }

      this.mTaxAmount = this.mSubTotal.multiply(Aem.getTaxRate().divide(new BigDecimal(100.0), 4, 4));
      this.mSubTotal = this.mSubTotal.setScale(Aem.getCurrencyFractionalDigits(), 4);
      this.mTaxAmount = this.mTaxAmount.setScale(Aem.getCurrencyFractionalDigits(), 0);
      this.mGrandTotal = this.mSubTotal.add(this.mTaxAmount);
      if (aIsConfirm && aLogValues) {
         Aem.logSummaryMessage("Subtotal is " + this.mSubTotal);
         Aem.logSummaryMessage("Tax amount is " + this.mTaxAmount);
         Aem.logSummaryMessage("Grandtotal is " + this.mGrandTotal);
      }
   }

   public void setDispensedDiscDetailId(int aIndex, int aDiscDetailId) {
      ((DiscItem)this.mDiscItemList.get(aIndex)).setDispensedDiscDetailId(aDiscDetailId);
   }

   public void addDiscItem(DiscItem aDiscItem) throws DiscRentalException {
      if (this.getDiscItemListCount() >= this.mDiscItemListMaxCapacity) {
         throw new DiscActionException("DiscItemList at maximum capacity: " + this.mDiscItemListMaxCapacity);
      } else if (this.mDiscItemList.indexOf(aDiscItem) != -1) {
         throw new DiscActionException("Disc (" + aDiscItem.getGroupCode() + "," + aDiscItem.getDiscCode() + ") already in DiscItemList");
      } else {
         this.mDiscItemList.add(aDiscItem);
         this.mDTUpdated = new Date(System.currentTimeMillis());
         if (this.mTransactionTime == null) {
            this.mTransactionTime = new Date();
         }

         this.calculateTotals(false);
      }
   }

   public DiscItem removeDiscItem(DiscItem aDiscItem) throws DiscRentalException {
      DiscItem lDiscItem = super.removeDiscItem(aDiscItem);
      this.calculateTotals(false);
      return lDiscItem;
   }

   public DiscItem removeDiscItem(int aIndex) throws DiscRentalException {
      DiscItem lDiscItem = super.removeDiscItem(aIndex);
      this.calculateTotals(false);
      return lDiscItem;
   }

   public void clearDiscItems() {
      super.clearDiscItems();
      this.mTransactionId = 0;
      this.mEmailAddr = null;
      this.mTrack = null;
      this.mSubTotal = new BigDecimal(0.0);
      this.mTaxAmount = new BigDecimal(0.0);
      this.mGrandTotal = new BigDecimal(0.0);
      this.mPromoCode = null;
      this.mPromoValue = new BigDecimal(0.0);
      this.mTransactionTime = null;
      this.mVerificationTypeId = 0;
      this.mVerificationString = "";
      this.mPaymentCardTypeId = 0;
      this.mPromoValueApplied = new BigDecimal(0.0);
   }

   public void queueExecute() throws DiscActionException {
      if (!Aem.isStandAlone()) {
         try {
            String lrequeststring = new SynchRequest(Aem.getAemId(), "2.0", "CONFIRM_DISPENSE", this.mNvPairSet).getAsXmlString();
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
                  Aem.logDetailMessage(Level.WARNING, "ConfirmDispense request failed");
                  String lMsg = lRequest.getParameterValueAsString("Message");
                  throw new DvdplayException(lMsg);
               }
            }
         } catch (DvdplayException var8) {
            Aem.logDetailMessage(Level.WARNING, var8.getMessage());
            throw new DiscActionException("DiscRental.queueExceute failed");
         }
      }
   }

   public NvPairSet getConfirmDispenseNvPairSet() {
      String[] lLineitemValues = new String[DvdplayBase.CONFIRM_DISPENSE_LINEITEM_FIELD_NAMES.length];
      NvPairSet lNvSet = new NvPairSet();

      try {
         lNvSet.add(new NvPair("SubTotal", "BigDecimal", this.mSubTotal.toString()));
         lNvSet.add(new NvPair("TaxAmount", "BigDecimal", this.mTaxAmount.toString()));
         lNvSet.add(new NvPair("GrandTotal", "BigDecimal", this.mGrandTotal.toString()));
         lNvSet.add(new NvPair("DTUpdated", "Date", Util.dateToString(this.mDTUpdated)));
         lNvSet.add(new NvPair("TransactionId", "int", String.valueOf(this.mTransactionId)));
         lNvSet.add(new NvPair("EmailAddr", "String", this.mEmailAddr));
         lNvSet.add(new NvPair("RequestId", "int", Integer.toString(Aem.getNextIndex())));
         lNvSet.add(new NvPair("LocaleId", "int", Integer.toString(Aem.getLocaleId())));
         RCSet lLineitemRCSet = new RCSet(DvdplayBase.CONFIRM_DISPENSE_LINEITEM_FIELD_NAMES, DvdplayBase.CONFIRM_DISPENSE_LINEITEM_FIELD_TYPES);

         for (int i = 0; i < this.mDiscItemList.size(); i++) {
            DiscItem lDiscItem = (DiscItem)this.mDiscItemList.get(i);
            if (lDiscItem.getDiscStatusId() == 3) {
               lLineitemValues[0] = String.valueOf(i + 1);
               lLineitemValues[1] = String.valueOf(lDiscItem.getDiscDetailId());
               lLineitemValues[2] = lDiscItem.getDispensedDiscDetailId();
               lLineitemRCSet.addRow(lLineitemValues);
            }
         }

         if (lLineitemRCSet.rowCount() != 0) {
            lNvSet.add(new NvPair("LineItem", "RCSet", lLineitemRCSet));
         }

         if (this.getPromoCode() != null) {
            RCSet lPromoRCSet = new RCSet(DvdplayBase.PROCESS_PAYMENT_PROMOITEM_FIELD_NAMES, DvdplayBase.PROCESS_PAYMNET_PROMOITEM_FIELD_TYPES);
            String[] lPromoitem = new String[]{this.mPromoCode, this.mPromoValueApplied.toString()};
            lPromoRCSet.addRow(lPromoitem);
            lNvSet.add(new NvPair("Promo", "RCSet", lPromoRCSet));
         }

         int lPollListCount = Aem.createPollList();
         if (lPollListCount > 0) {
            RCSet lPollRCSet = new RCSet(DvdplayBase.CONFIRM_DISPENSE_POLLITEM_FIELD_NAMES, DvdplayBase.CONFIRM_DISPENSE_POLLITEM_FIELD_TYPES);

            for (int ix = 0; ix < lPollListCount; ix++) {
               String[] lPollResponse = new String[]{
                  Integer.toString(Aem.getPollIndexItem(ix).getPollID()), Integer.toString(Aem.getPollIndexItem(ix).getPollResponse())
               };
               lPollRCSet.addRow(lPollResponse);
            }

            lNvSet.add(new NvPair("Poll", "RCSet", lPollRCSet));
         }

         return lNvSet;
      } catch (DvdplayException var10) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var10.getMessage());
         throw new DvdplayException("getConfirmDispenseNvPairSet failed.");
      }
   }

   public String getCheckPromoCodePacket(String aPromoCode) {
      NvPairSet lNvSet = new NvPairSet();

      try {
         lNvSet.add(new NvPair("PROMO_CODE", "String", aPromoCode));
         lNvSet.add(new NvPair("REQUEST_ID", "int", Integer.toString(Aem.getNextIndex())));
         lNvSet.add(new NvPair("KIOSK_TIME", "String", Util.dateToString(this.mTransactionTime, "yyyy-MM-dd HH:mm:ss.SSS")));
         return new SynchRequest(Aem.getAemId(), "2.0", "CHECK_PROMO_CODE", lNvSet).getAsXmlString();
      } catch (DvdplayException var4) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "getCheckPromoCodePacket: " + var4.getMessage());
         throw new DvdplayException("getCheckPromoCodePacket failed.");
      }
   }

   public String getProcessPaymentPacket() {
      String[] lLineitemValues = new String[DvdplayBase.PROCESS_PAYMENT_LINEITEM_FIELD_NAMES.length];
      NvPairSet lNvSet = new NvPairSet();

      try {
         lNvSet.add(new NvPair("SubTotal", "BigDecimal", this.mSubTotal.toString()));
         lNvSet.add(new NvPair("TaxAmount", "BigDecimal", this.mTaxAmount.toString()));
         lNvSet.add(new NvPair("GrandTotal", "BigDecimal", this.mGrandTotal.toString()));
         lNvSet.add(new NvPair("DTUpdated", "Date", Util.dateToString(this.mDTUpdated)));
         lNvSet.add(new NvPair("RequestId", "int", Integer.toString(Aem.getNextIndex())));
         lNvSet.add(new NvPair("PaymentCardTypeID", "int", Integer.toString(this.mPaymentCardTypeId)));
         lNvSet.add(new NvPair("LocaleId", "int", Integer.toString(Aem.getLocaleId())));
         lNvSet.add(new NvPair("TransactionTime", "Date", Util.dateToString(this.mTransactionTime)));
         DataPacketComposer lComposer = new DataPacketComposer();
         RCSet lTrackRCSet = lComposer.rcDeMarshal(this.mTrack);
         lNvSet.add(new NvPair("Track", "RCSet", lTrackRCSet));
         RCSet lLineitemRCSet = new RCSet(DvdplayBase.PROCESS_PAYMENT_LINEITEM_FIELD_NAMES, DvdplayBase.PROCESS_PAYMENT_LINEITEM_FIELD_TYPES);

         for (int i = 0; i < this.mDiscItemList.size(); i++) {
            DiscItem lDiscItem = (DiscItem)this.mDiscItemList.get(i);
            lLineitemValues[lLineitemRCSet.getFieldIndex("LineItemId")] = String.valueOf(i + 1);
            lLineitemValues[lLineitemRCSet.getFieldIndex("DiscDetailId")] = String.valueOf(lDiscItem.getDiscDetailId());
            if (lDiscItem.getOperationId() == 1) {
               lLineitemValues[lLineitemRCSet.getFieldIndex("Price")] = Aem.getPricing(lDiscItem.getPriceOptionId(), this.mTransactionTime)
                  .getRentalPrice()
                  .toString();
            } else {
               lLineitemValues[lLineitemRCSet.getFieldIndex("Price")] = Aem.getPricing(lDiscItem.getPriceOptionId(), this.mTransactionTime)
                  .getUsedPrice()
                  .toString();
            }

            lLineitemValues[lLineitemRCSet.getFieldIndex("OperationId")] = String.valueOf(lDiscItem.getOperationId());
            lLineitemValues[lLineitemRCSet.getFieldIndex("DueTime")] = Util.dateToString(
               Aem.getPricing(lDiscItem.getPriceOptionId(), this.mTransactionTime).getDueDate(this.mTransactionTime)
            );
            lLineitemRCSet.addRow(lLineitemValues);
         }

         lNvSet.add(new NvPair("LineItem", "RCSet", lLineitemRCSet));
         if (this.mPromoCode != null) {
            RCSet lPromoRCSet = new RCSet(DvdplayBase.PROCESS_PAYMENT_PROMOITEM_FIELD_NAMES, DvdplayBase.PROCESS_PAYMNET_PROMOITEM_FIELD_TYPES);
            String[] lPromoitem = new String[]{this.mPromoCode, this.mPromoValueApplied.toString()};
            lPromoRCSet.addRow(lPromoitem);
            lNvSet.add(new NvPair("Promo", "RCSet", lPromoRCSet));
         }

         if (this.mVerificationTypeId != 0) {
            RCSet lVerificationRCSet = new RCSet(DvdplayBase.PROCESS_PAYMENT_PARAMITEM_FIELD_NAMES, DvdplayBase.PROCESS_PAYMENT_PARAMITEM_FIELD_TYPES);
            String[] VerificationItem = new String[]{Integer.toString(this.mVerificationTypeId), this.mVerificationString};
            lVerificationRCSet.addRow(VerificationItem);
            lNvSet.add(new NvPair("VerificationParam", "RCSet", lVerificationRCSet));
         }

         return new SynchRequest(Aem.getAemId(), "2.0", "PROCESS_PAYMENT", lNvSet).getAsXmlString();
      } catch (DvdplayException var10) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, var10.getMessage());
         throw new DvdplayException("getProcessPaymentPacket failed");
      }
   }

   public DiscRental() {
      this.mDiscItemListMaxCapacity = 4;
      this.mTransactionId = 0;
      this.mEmailAddr = null;
      this.mTrack = null;
      this.mSubTotal = new BigDecimal(0.0);
      this.mTaxAmount = new BigDecimal(0.0);
      this.mGrandTotal = new BigDecimal(0.0);
      this.mPromoCode = null;
      this.mPromoValue = new BigDecimal(0.0);
      this.mTransactionTime = null;
      this.mPaymentCardTypeId = 0;
      this.mVerificationTypeId = 0;
      this.mVerificationString = "";
      this.mPromoValueApplied = new BigDecimal(0.0);
      this.mQueueJobId = 1;
   }

   public DiscRental(DiscRental aDiscRental) {
      super(aDiscRental);
      this.mDiscItemListMaxCapacity = aDiscRental.mDiscItemListMaxCapacity;
      this.mTransactionId = aDiscRental.mTransactionId;
      this.mEmailAddr = aDiscRental.mEmailAddr;
      this.mTrack = aDiscRental.mTrack;
      this.mSubTotal = aDiscRental.mSubTotal;
      this.mTaxAmount = aDiscRental.mTaxAmount;
      this.mGrandTotal = aDiscRental.mGrandTotal;
      this.mPromoCode = aDiscRental.mPromoCode;
      this.mPromoValue = aDiscRental.mPromoValue;
      this.mTransactionTime = aDiscRental.mTransactionTime;
      this.mPaymentCardTypeId = aDiscRental.mPaymentCardTypeId;
      this.mVerificationTypeId = aDiscRental.mVerificationTypeId;
      this.mVerificationString = aDiscRental.mVerificationString;
      this.mPromoValueApplied = aDiscRental.mPromoValueApplied;
      this.mQueueJobId = 1;
   }

   public DiscRental(NvPairSet aNvPairSet) {
      super(aNvPairSet);
      this.mQueueJobId = 1;
   }
}
