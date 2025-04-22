package net.dvdplay.models;

import java.util.Date;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.DiscIndex;
import net.dvdplay.logger.DvdplayLevel;

public class AEMContent extends AbstractHardwareThread {
   Help help;
   String currentScreen = "";
   String prevScreen = "";
   int helpQuestionNum = 1;
   int currentMovieSelectionPageNum = 1;
   int currentCategoryId = -1;
   int currentCategoryPageNum = 1;
   int helpPageNum = 1;
   int currMovieId = 1;
   int currentDvdDescriptionDiscId = 0;
   int currentPollNum = 0;
   boolean invalidPromoCodeEntered = false;
   String currentAbout = "DvdPlay";
   String originalPointBeforeAbout = "";
   String originalPointBeforeHelp = "";
   String originalHelpAnswerBackBeforeTimeout = "";
   String intermediateZipCode = "";
   String intermediatePromoCode = "";
   String intermediateEmail = "";

   public AEMContent() {
      this.help = new Help();
   }

   public void reset() {
      this.helpQuestionNum = 1510;
      this.currentMovieSelectionPageNum = 1;
      this.currentDvdDescriptionDiscId = 0;
      this.currentCategoryId = -1;
      this.currentCategoryPageNum = 1;
      this.helpPageNum = 1;
      this.currMovieId = 1;
      this.currentAbout = "DvdPlay";
      this.invalidPromoCodeEntered = false;
      this.setIntermediateEmailAddress("");
      this.setIntermediatePromoCode("");
      this.setIntermediateZipCode("");
      this.setCurrentPollNum(0);
   }

   public void setPrevScreen(String screen) {
      if (!screen.equals("TimeOutScreen") && !screen.equals("ErrorScreen")) {
         this.prevScreen = screen;
      }
   }

   public void setCurrentScreen(String screen) {
      if (!screen.equals("TimeOutScreen") && !screen.equals("ErrorScreen")) {
         this.currentScreen = screen;
      }
   }

   public void setHelpQuestionNum(int num) {
      this.helpQuestionNum = num;
   }

   public void setHelp(Help lHelp) {
      this.help = lHelp;
   }

   public void setHelpPageNum(int num) {
      this.helpPageNum = num;
   }

   public void setCurrMovieId(int num) {
      this.currMovieId = num;
   }

   public void setCurrentAbout(String com) {
      this.currentAbout = com;
   }

   public void setCurrentCategoryId(int i) {
      this.currentCategoryId = i;
   }

   public void setCurrentMovieSelectionPageNum(int num) {
      this.currentMovieSelectionPageNum = num;
   }

   public void setCurrentCategoryPageNum(int num) {
      this.currentCategoryPageNum = num;
   }

   public void setCurrentDvdDescriptionDiscId(int id) {
      this.currentDvdDescriptionDiscId = id;
   }

   public void setOriginalPointBeforeHelp(String org) {
      this.originalPointBeforeHelp = org;
   }

   public void setEmailAddress(String emailAddress) {
      AbstractHardwareThread.mRentOp.setEmailAddr(emailAddress);
   }

   public void setIntermediateEmailAddress(String emailAddress) {
      this.intermediateEmail = emailAddress;
   }

   public void setIntermediatePromoCode(String code) {
      this.intermediatePromoCode = code;
   }

   public void setIntermediateZipCode(String zc) {
      this.intermediateZipCode = zc;
   }

   public void setInvalidPromoCodeEntered(boolean choice) {
      this.invalidPromoCodeEntered = choice;
   }

   public void setOriginalPointBeforeAbout(String org) {
      if (!org.equals("TimeOutScreen") && !org.equals("ErrorScreen") && !org.equals("AboutCompanyScreen")) {
         this.originalPointBeforeAbout = org;
      }
   }

   public String getPrevScreen() {
      return this.prevScreen;
   }

   public String getCurrentScreen() {
      return this.currentScreen;
   }

   public Help getHelp() {
      return this.help;
   }

   public int getHelpQuestionNum() {
      return this.helpQuestionNum;
   }

   public int getHelpPageNum() {
      return this.helpPageNum;
   }

   public int getCurrMovieId() {
      return this.currMovieId;
   }

   public String getCurrentAbout() {
      return this.currentAbout;
   }

   public int getCurrentCategoryId() {
      return this.currentCategoryId;
   }

   public int getCurrentMovieSelectionPageNum() {
      return this.currentMovieSelectionPageNum;
   }

   public int getCurrentCategoryPageNum() {
      return this.currentCategoryPageNum;
   }

   public int getCartItemCount() {
      return AbstractHardwareThread.mRentOp.getDiscCount();
   }

   public int getCurrentDvdDescriptionDiscId() {
      return this.currentDvdDescriptionDiscId;
   }

   public String getOriginalPointBeforeHelp() {
      return this.originalPointBeforeHelp;
   }

   public String getOriginalHelpAnswerBackBeforeTimeout() {
      return this.originalHelpAnswerBackBeforeTimeout;
   }

   public String getOriginalPointBeforeAbout() {
      return this.originalPointBeforeAbout;
   }

   public void setOriginalHelpAnswerBackBeforeTimeout(String originalHelpAnswerBackBeforeTimeout) {
      if (!originalHelpAnswerBackBeforeTimeout.equals("TimeOutScreen")) {
         this.originalHelpAnswerBackBeforeTimeout = originalHelpAnswerBackBeforeTimeout;
      }
   }

   public String getIntermediatePromoCode() {
      return this.intermediatePromoCode;
   }

   public String getIntermediateZipCode() {
      return this.intermediateZipCode;
   }

   public String getIntermediateEmailAddress() {
      return this.intermediateEmail;
   }

   public String getEmail() {
      return AbstractHardwareThread.mRentOp.getEmailAddr();
   }

   public String getPromoValue() {
      return Aem.getCurrencySymbol() + AbstractHardwareThread.mRentOp.getPromoCodeValueApplied();
   }

   public void addPurchase(int discId) {
      try {
         AbstractHardwareThread.mRentOp.buyDisc(discId);
      } catch (DvdplayException var3) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
      }
   }

   public void addRental(int discId) {
      try {
         AbstractHardwareThread.mRentOp.rentDisc(discId);
      } catch (DvdplayException var3) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
      }
   }

   public void removeOrder(int id) {
      try {
         AbstractHardwareThread.mRentOp.removeDisc(id);
      } catch (DvdplayException var3) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
      }
   }

   public String getLateDays(int id) {
      return Integer.toString(
         Aem.getPricing(AbstractHardwareThread.mRentOp.getDiscItem(id).getPriceOptionId(), AbstractHardwareThread.mRentOp.getTransactionTime()).getLateDays()
      );
   }

   public String getLateRate(int id) {
      return Aem.getCurrencySymbol()
         + Aem.getPricing(AbstractHardwareThread.mRentOp.getDiscItem(id).getPriceOptionId(), AbstractHardwareThread.mRentOp.getTransactionTime())
            .getLateRentalPrice()
            .setScale(Aem.getCurrencyFractionalDigits(), 4)
            .toString();
   }

   public String getPoster(int id) {
      StringBuffer var10000 = new StringBuffer().append("c:\\aem\\content\\posters\\");
      Aem.getDiscIndex();
      return var10000.append(DiscIndex.getDiscIndexItemByTitleDetailId(AbstractHardwareThread.mRentOp.getDiscItem(id).getTitleDetailId()).getPoster())
         .toString();
   }

   public String getTitle(int id) {
      return Aem.getTitle(AbstractHardwareThread.mRentOp.getDiscItem(id).getTitleDetailId());
   }

   public String getRentBuy(int id) {
      return AbstractHardwareThread.mRentOp.getDiscItem(id).getOperationId() == 1 ? "Rent" : "Buy";
   }

   public String getDueTime(int id) {
      return AbstractHardwareThread.mRentOp.getDiscItem(id).getOperationId() == 1 ? Aem.getDueTimeAsString() : "";
   }

   public String getDueDate(int id) {
      return AbstractHardwareThread.mRentOp.getDiscItem(id).getOperationId() == 1
         ? Aem.getPricing(AbstractHardwareThread.mRentOp.getDiscItem(id).getPriceOptionId(), AbstractHardwareThread.mRentOp.getTransactionTime())
            .getDueDateShort(AbstractHardwareThread.mRentOp.getTransactionTime())
         : "";
   }

   public String getPrice(int id) {
      return AbstractHardwareThread.mRentOp.getDiscItem(id).getOperationId() == 1
         ? Aem.getCurrencySymbol()
            + Aem.getPricing(AbstractHardwareThread.mRentOp.getDiscItem(id).getPriceOptionId(), AbstractHardwareThread.mRentOp.getTransactionTime())
               .getRentalPrice()
               .setScale(Aem.getCurrencyFractionalDigits(), 4)
               .toString()
         : Aem.getCurrencySymbol()
            + Aem.getPricing(AbstractHardwareThread.mRentOp.getDiscItem(id).getPriceOptionId(), AbstractHardwareThread.mRentOp.getTransactionTime())
               .getUsedPrice()
               .setScale(Aem.getCurrencyFractionalDigits(), 4)
               .toString();
   }

   public Date getTransactionTime() {
      return AbstractHardwareThread.mRentOp.getTransactionTime();
   }

   public String getSubTotal() {
      return Aem.getCurrencySymbol() + AbstractHardwareThread.mRentOp.getSubTotal().toString();
   }

   public String getTax() {
      return Aem.getCurrencySymbol() + AbstractHardwareThread.mRentOp.getTaxAmount().toString();
   }

   public String getTotal() {
      return Aem.getCurrencySymbol() + AbstractHardwareThread.mRentOp.getGrandTotal().toString();
   }

   public void calculateFinalTotals() {
      AbstractHardwareThread.mRentOp.calculateFinalTotals();
   }

   public void checkPromoCode(String aPromoCode) {
      AbstractHardwareThread.mRentOp.checkPromoCode(aPromoCode);
   }

   public boolean hasValidPromoCode() {
      return AbstractHardwareThread.mRentOp.hasValidPromoCode();
   }

   public void removePromoCode() {
      AbstractHardwareThread.mRentOp.removePromoCode();
   }

   public String getPromoCodeValue() {
      return "- " + Aem.getCurrencySymbol() + AbstractHardwareThread.mRentOp.getPromoCodeValue();
   }

   public String getPromoCodeValueApplied() {
      return Aem.getCurrencySymbol() + AbstractHardwareThread.mRentOp.getPromoCodeValueApplied();
   }

   public void setPaymentCardTypeId(int id) {
      AbstractHardwareThread.mRentOp.setPaymentCard(id);
   }

   public void setVerification(int id, String code) {
      AbstractHardwareThread.mRentOp.setVerification(id, code);
   }

   public int getVerificationTypeId() {
      return Aem.getVerificationType(AbstractHardwareThread.mRentOp.getPaymentCardTypeId());
   }

   public void stopCreditCardReader() {
      AbstractHardwareThread.mRentOp.stopCreditCardReader();
   }

   public int getDiscCount() {
      return AbstractHardwareThread.mRentOp.getDiscCount();
   }

   public void createRentalQueueJob() {
      AbstractHardwareThread.mRentOp.createQueueJob();
   }

   public int getCurrentPollNum() {
      return this.currentPollNum;
   }

   public void setCurrentPollNum(int currentPollNum) {
      this.currentPollNum = currentPollNum;
   }
}
