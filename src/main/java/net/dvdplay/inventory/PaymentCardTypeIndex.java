package net.dvdplay.inventory;

import java.util.ArrayList;
import net.dvdplay.exception.DvdplayException;

public class PaymentCardTypeIndex {
   private static ArrayList mPaymentCardTypeItemList;

   public PaymentCardTypeIndex() {
      mPaymentCardTypeItemList = new ArrayList();
   }

   public static void clear() {
      mPaymentCardTypeItemList = new ArrayList();
   }

   public static int size() {
      return mPaymentCardTypeItemList.size();
   }

   public static void addPaymentCardTypeIndexItem(PaymentCardTypeIndexItem aPaymentCardTypeIndexItem) {
      for (int i = 0; i < mPaymentCardTypeItemList.size(); i++) {
         PaymentCardTypeIndexItem lPaymentCardTypeIndexItem = (PaymentCardTypeIndexItem)mPaymentCardTypeItemList.get(i);
         if (aPaymentCardTypeIndexItem.compareTo(lPaymentCardTypeIndexItem) == -1) {
            mPaymentCardTypeItemList.add(i, new PaymentCardTypeIndexItem(aPaymentCardTypeIndexItem));
            return;
         }
      }

      mPaymentCardTypeItemList.add(new PaymentCardTypeIndexItem(aPaymentCardTypeIndexItem));
   }

   public static PaymentCardTypeIndexItem getPaymentCardTypeIndexItem(int aIndex) throws DvdplayException {
      if (aIndex >= 0 && aIndex <= mPaymentCardTypeItemList.size()) {
         return (PaymentCardTypeIndexItem)mPaymentCardTypeItemList.get(aIndex);
      } else {
         throw new DvdplayException("getTitleTypeIndex: invalid index " + aIndex);
      }
   }
}
