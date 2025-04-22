package net.dvdplay.userop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemException;
import net.dvdplay.aem.CarouselException;
import net.dvdplay.aem.CreditCardThread;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoException;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.aemcomm.Comm;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.RCSet;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.DiscIndex;
import net.dvdplay.inventory.DiscIndexItem;
import net.dvdplay.inventory.Inventory;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.request.Request;
import net.dvdplay.request.RequestFactory;
import net.dvdplay.task.QueueJob;

public class RentOp {
   private DiscRental mDiscRental = new DiscRental();
   private CreditCardThread mCreditCardThread = new CreditCardThread();
   private boolean mProcessPaymentSuceeded = false;
   private boolean mDiscIsDispensed = false;

   public boolean isDiscDispensed() {
      return this.mDiscIsDispensed;
   }

   public int getDiscCount() {
      return this.mDiscRental.getDiscItemListCount();
   }

   public DiscItem getDiscItem(int aIndex) {
      return this.mDiscRental.getDiscItem(aIndex);
   }

   public Date getTransactionTime() {
      return this.mDiscRental.getTransactionTime();
   }

   public BigDecimal getSubTotal() {
      return this.mDiscRental.getSubTotal();
   }

   public BigDecimal getTaxAmount() {
      return this.mDiscRental.getTaxAmount();
   }

   public BigDecimal getGrandTotal() {
      return this.mDiscRental.getGrandTotal();
   }

   public String getEmailAddr() {
      return this.mDiscRental.getEmailAddr();
   }

   public void setEmailAddr(String aEmailAddr) {
      this.mDiscRental.setEmailAddr(aEmailAddr);
   }

   public void rentDisc(int aDiscDetailId) throws RentOpException {
      try {
         this.mDiscRental.addDiscItem(new DiscItem(Aem.getDisc(aDiscDetailId), 1));
      } catch (AemException var3) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "AemException caught: " + var3.getMessage());
         throw new RentOpException("Error creating Disc object");
      }
   }

   public void buyDisc(int aDiscDetailId) throws RentOpException {
      try {
         this.mDiscRental.addDiscItem(new DiscItem(Aem.getDisc(aDiscDetailId), 2));
      } catch (AemException var3) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "AemException caught: " + var3.getMessage());
         throw new RentOpException("Error creating Disc object");
      }
   }

   public void removeDisc(int aIndex) {
      this.mDiscRental.removeDiscItem(aIndex);
   }

   public void removePromoCode() {
      this.mDiscRental.setPromo(null, new BigDecimal(0.0));
   }

   public boolean hasValidPromoCode() {
      return this.mDiscRental.getPromoCode() != null;
   }

   public String getPromoCodeValue() {
      return this.mDiscRental.getPromoValue().setScale(Aem.getCurrencyFractionalDigits(), 4).toString();
   }

   public String getPromoCodeValueApplied() {
      return this.mDiscRental.getPromoValueApplied().setScale(Aem.getCurrencyFractionalDigits(), 4).toString();
   }

   public void setVerification(int aVerificationTypeId, String aVerificationString) {
      this.mDiscRental.setVerificationTypeId(aVerificationTypeId);
      this.mDiscRental.setVerificationString(aVerificationString);
   }

   public void setPaymentCard(int aPaymentCardTypeId) {
      this.mDiscRental.setPaymentCardTypeId(aPaymentCardTypeId);
   }

   public int getPaymentCardTypeId() {
      return this.mDiscRental.getPaymentCardTypeId();
   }

   public void dispenseDisc(int aIndex) throws RentOpException {
      int lTitleDetailId = this.mDiscRental.getDiscItem(aIndex).getTitleDetailId();
      int lPriceOptionId = this.mDiscRental.getDiscItem(aIndex).getPriceOptionId();
      int lOperationId = this.mDiscRental.getDiscItem(aIndex).getOperationId();
      boolean lDispensed = false;
      Aem.getDiscIndex();
      ArrayList lList = DiscIndex.getDiscIndexItemListByTitleDetailId(lTitleDetailId);

      try {
         this.dispenseDiscItem(this.mDiscRental.getDiscItem(aIndex));
      } catch (Exception var12) {
         Aem.logDetailMessage(DvdplayLevel.INFO, var12.getMessage());
         Aem.logDetailMessage(DvdplayLevel.INFO, "Failed to dispense DiscDetailId " + this.mDiscRental.getDiscItem(aIndex).getDiscDetailId());

         for (int i = 1; i < lList.size(); i++) {
            if (Aem.inQuiesceMode()) {
               throw new RentOpException("Quiesce Mode! Aborting dispense disc");
            }

            DiscIndexItem lDiscIndexItem = (DiscIndexItem)lList.get(i);
            if (lDiscIndexItem.getDiscStatusId() == 3
               && lDiscIndexItem.getPriceOptionId() == lPriceOptionId
               && (lOperationId != 1 || lDiscIndexItem.isMarkedForRent())
               && (lOperationId != 2 || lDiscIndexItem.isMarkedForSale())) {
               int lDiscDetailId = lDiscIndexItem.getDiscDetailId();

               try {
                  this.dispenseDiscItem(new DiscItem(Aem.getDisc(lDiscDetailId), lOperationId));
                  this.mDiscRental.setDispensedDiscDetailId(aIndex, lDiscDetailId);
                  lDispensed = true;
                  break;
               } catch (Exception var11) {
                  Aem.logDetailMessage(DvdplayLevel.INFO, var11.getMessage());
                  Aem.logDetailMessage(DvdplayLevel.INFO, "Failed to dispense DiscDetailId " + lDiscDetailId);
               }
            }
         }

         if (!lDispensed) {
            throw new RentOpException("Dispense disc failed");
         }
      }
   }

   private void dispenseDiscItem(DiscItem aDiscItem) throws RentOpException {
      int lSlot = aDiscItem.getSlot();
      Servo lServo = ServoFactory.getInstance();

      try {
         Aem.setDiscActive();
         this.mDiscIsDispensed = false;
         Aem.logSummaryMessage("Dispensing disc (" + aDiscItem.getGroupCode() + "," + aDiscItem.getDiscCode() + ") from slot " + aDiscItem.getSlot() + " ...");
         if (lServo.goToSlot(lSlot) == 1) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "No Disc in slot " + lSlot + ".");
            Aem.createDiscMissing(aDiscItem.getDisc());
            Aem.addDiscMissingQueueJob();
            Aem.removeDisc(aDiscItem.getDiscDetailId(), aDiscItem.getTitleDetailId(), aDiscItem.getDiscCode(), aDiscItem.getGroupCode(), true);
            DOMData.save();
            lServo.setServoParameters();
            throw new NoDiscInSlotException("No Disc in slot " + lSlot + ".");
         } else {
            lServo.ejectDisc(1);
            if (lServo.isDiscInSlot()) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Disc was not dispensed.");
               throw new RentOpException("Dispense disc failed");
            } else {
               this.mDiscIsDispensed = true;
               switch (aDiscItem.getOperationId()) {
                  case 1:
                     Aem.logSummaryMessage(
                        "Disc rented: (" + aDiscItem.getGroupCode() + "," + aDiscItem.getDiscCode() + ") from slot " + aDiscItem.getSlot() + "."
                     );
                     aDiscItem.setDiscStatusId(1);
                     aDiscItem.setSlot(0);
                     Aem.removeDisc(aDiscItem.getDiscDetailId(), aDiscItem.getTitleDetailId(), aDiscItem.getDiscCode(), aDiscItem.getGroupCode(), true);
                     break;
                  case 2:
                     Aem.logSummaryMessage(
                        "Disc sold: (" + aDiscItem.getGroupCode() + "," + aDiscItem.getDiscCode() + ") from slot " + aDiscItem.getSlot() + "."
                     );
                     aDiscItem.setDiscStatusId(2);
                     aDiscItem.setSlot(0);
                     Aem.removeDisc(aDiscItem.getDiscDetailId(), aDiscItem.getTitleDetailId(), aDiscItem.getDiscCode(), aDiscItem.getGroupCode(), true);
                     break;
                  default:
                     throw new RentOpException("Invalid OperationId: " + aDiscItem.getOperationId());
               }

               lServo.setServoParameters();
               DOMData.save();
            }
         }
      } catch (CarouselException var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "CarouselException caught: " + var7.getMessage());
         throw new RentOpException("Dispense disc failed");
      } catch (ServoException var8) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "ServoException caught: " + var8.getMessage());
         if (lServo.getCarousel().isCarouselAtSlot(lSlot) && this.clearSlot()) {
            if (!lServo.isDiscInSlot()) {
               this.mDiscIsDispensed = true;
               switch (aDiscItem.getOperationId()) {
                  case 1:
                     Aem.logSummaryMessage(
                        "Disc rented: (" + aDiscItem.getGroupCode() + "," + aDiscItem.getDiscCode() + ") from slot " + aDiscItem.getSlot() + "."
                     );
                     aDiscItem.setDiscStatusId(1);
                     aDiscItem.setSlot(0);
                     break;
                  case 2:
                     Aem.logSummaryMessage(
                        "Disc sold: (" + aDiscItem.getGroupCode() + "," + aDiscItem.getDiscCode() + ") from slot " + aDiscItem.getSlot() + "."
                     );
                     aDiscItem.setDiscStatusId(2);
                     aDiscItem.setSlot(0);
                     break;
                  default:
                     throw new RentOpException("Invalid OperationId: " + aDiscItem.getOperationId());
               }

               Aem.removeDisc(aDiscItem.getDiscDetailId(), aDiscItem.getTitleDetailId(), aDiscItem.getDiscCode(), aDiscItem.getGroupCode(), true);
               DOMData.save();
               lServo.setServoParameters();
               return;
            }

            Inventory.incDispenseFailureCount(aDiscItem.getDiscDetailId());
         }

         throw new RentOpException("Dispense disc failed");
      } catch (Exception var9) {
         throw new RentOpException(var9.getMessage());
      }
   }

   public void processPayment() throws RentOpException {
      Aem.logSummaryMessage("Processing payment ...");
      if (!Aem.isStandAlone()) {
         try {
            String req = this.mDiscRental.getProcessPaymentPacket();
            Aem.logDetailMessage(DvdplayLevel.FINER, req);
            String requestRet = Comm.sendRequest(req);
            Request lRequest = RequestFactory.makeRequest(requestRet);
            int lResult = Integer.parseInt(lRequest.getParameterValueAsString("Result"));
            if (lResult != 0) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "ProcessPayment request failed");
               String lMsg = lRequest.getParameterValueAsString("Message");
               if (lResult == -1000) {
                  throw new MaxDiscsAllowedException(lMsg);
               }

               if (lResult == -1006) {
                  throw new MaxDiscsAllowedException(lMsg);
               }

               if (lResult == 9005) {
                  throw new PromoCodeException("promo not associated with user");
               }

               if (lResult == -1008) {
                  throw new ProcessPaymentValidationException("error: validation failed");
               }

               if (lResult == -1009) {
                  throw new ProcessPaymentDisabledCardException("error: payent card disabled");
               }

               throw new RentOpException(lResult, lMsg);
            }

            int lTransactionId = Integer.parseInt(lRequest.getParameterValueAsString("TransactionId"));
            String lEmailAddress = lRequest.getParameterValueAsString("EmailAddr");
            Aem.logDetailMessage(DvdplayLevel.FINE, "ProcessPayment was successful: " + lTransactionId + " " + lEmailAddress);
            Aem.logSummaryMessage("Payment card accepted");
            this.logCCInfo();
            this.mDiscRental.setTransactionId(lTransactionId);
            this.mDiscRental.setEmailAddr(lEmailAddress);
            this.mProcessPaymentSuceeded = true;
         } catch (ProcessPaymentDisabledCardException var7) {
            throw var7;
         } catch (MaxDiscsAllowedException var8) {
            throw new MaxDiscsAllowedException(var8.getCode(), var8.getMessage());
         } catch (PromoCodeException var9) {
            throw new PromoCodeException(var9.getMessage());
         } catch (RentOpException var10) {
            throw new RentOpException(var10.getCode(), var10.getMessage());
         } catch (ProcessPaymentValidationException var11) {
            throw var11;
         } catch (DvdplayException var12) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, "ProcessPayment failed");
            throw var12;
         }
      } else {
         this.mDiscRental.setTransactionId(1234);
         this.mDiscRental.setEmailAddr("JoeSmith@aol.com");
      }
   }

   public void checkPromoCode(String aPromoCode) throws RentOpException, DvdplayException {
      Aem.logSummaryMessage("Checking PromoCode " + aPromoCode);

      try {
         if (!Aem.isStandAlone()) {
            String lrequeststring = this.mDiscRental.getCheckPromoCodePacket(aPromoCode);
            Aem.logDetailMessage(DvdplayLevel.FINER, lrequeststring);
            Request lRequest = RequestFactory.makeRequest(Comm.sendRequest(lrequeststring));
            int lResult = Integer.parseInt(lRequest.getParameterValueAsString("RESULT"));
            if (lResult != 0) {
               Aem.logDetailMessage(DvdplayLevel.ERROR, "CheckPromoCode request failed");
               String lMsg = lRequest.getParameterValueAsString("RESULT_MESSAGE");
               throw new RentOpException(lResult, lMsg);
            }

            int lPromoResult = Integer.parseInt(lRequest.getParameterValueAsString("PROMO_STATUS"));
            if (lPromoResult != 0) {
               throw new RentOpException(lPromoResult, "PromoCode not valid");
            }

            String lPromoValue = lRequest.getParameterValueAsString("PROMO_VALUE");
            Aem.logSummaryMessage("PromoCode " + aPromoCode + "(" + Aem.getCurrencySymbol() + lPromoValue + ").");
            this.mDiscRental.setPromo(aPromoCode, new BigDecimal(lPromoValue));
         } else {
            this.mDiscRental.setPromo(aPromoCode, new BigDecimal(1.0));
         }
      } catch (RentOpException var7) {
         throw new RentOpException(var7.getCode(), var7.getMessage());
      } catch (DvdplayException var8) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "CheckPromoCode failed");
         throw new DvdplayException(var8.getMessage());
      }
   }

   public boolean clearSlot() {
      Servo lServo = ServoFactory.getInstance();
      return !lServo.isDiscJammed() ? true : lServo.reseatDisc();
   }

   public boolean discTaken() {
      Servo lServo = ServoFactory.getInstance();
      return lServo.isDiscTaken();
   }

   public void clearDiscItems() {
      this.mDiscRental.clearDiscItems();
      this.mProcessPaymentSuceeded = false;
      Aem.clearPollResponses();
   }

   public void startCreditCardReader() {
      Aem.logSummaryMessage("Starting credit card reader.");
      new CreditCardThread(true).start();
   }

   public void stopCreditCardReader() {
      Aem.logSummaryMessage("Stopping credit card reader.");
      CreditCardThread.stopCardRead();
      this.mDiscRental.setTrack(null);
   }

   public boolean isCreditCardReaderDone() throws RentOpException {
      if (CreditCardThread.readDone) {
         Aem.logSummaryMessage("Finished credit card read.");
         this.mDiscRental.setTrack(CreditCardThread.getCreditCardTrack());
         if (CreditCardThread.stopped) {
            return true;
         } else if (this.mDiscRental.getTrack() == null) {
            throw new RentOpException("Bad card swipe");
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private void logCCInfo() {
      String ldp = this.mDiscRental.getTrack();

      try {
         DataPacketComposer ldpc = new DataPacketComposer();
         RCSet lRCSet = ldpc.rcDeMarshal(ldp);
         String lTrack1 = lRCSet.getFieldValue(0, 0);
         StringTokenizer lstrtok = new StringTokenizer(lTrack1, "^");
         String lcc = lstrtok.nextToken();
         String lname = lstrtok.nextToken();
         lcc = lcc.substring(1);
         lcc = lcc.substring(0, lcc.length() - 4).replaceAll(".", "x") + lcc.substring(lcc.length() - 4);
         Aem.logSummaryMessage("Payment card is " + lcc);
         Aem.logSummaryMessage("Card holder is " + lname);
      } catch (Exception var8) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "logCCInfo Exception caught: " + var8.getMessage());
      }
   }

   public void calculateFinalTotals() {
      this.mDiscRental.calculateTotals(true);
   }

   public synchronized void createQueueJob() {
      if (this.getDiscCount() > 0) {
         if (!this.mProcessPaymentSuceeded) {
            this.clearDiscItems();
         } else {
            this.mDiscRental.calculateTotals(true, true);
            this.mDiscRental.mNvPairSet = this.mDiscRental.getConfirmDispenseNvPairSet();
            Aem.mQueue.addQueueJob(new QueueJob(new DiscRental(this.mDiscRental)), true);
            this.clearDiscItems();
         }
      }
   }
}
