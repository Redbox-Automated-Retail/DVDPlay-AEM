package net.dvdplay.aem;

import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.logger.DvdplayLevel;

public class CreditCardThread extends Thread {
   private static String mCreditCard = null;
   private static CreditCard mCC;
   public static boolean readDone = false;
   public static boolean stopped = false;
   public static boolean mStripSentinels;
   private static final int STARTED = 1;
   private static final int STOPPING = 2;
   private static final int STOPPED = 3;
   private static int mReadStatus = 3;

   public CreditCardThread() {
      readDone = false;
      stopped = false;
      mStripSentinels = true;
      mCC = new CreditCard();
   }

   public CreditCardThread(boolean aStripSentinels) {
      readDone = false;
      stopped = false;
      mStripSentinels = aStripSentinels;
      mCC = new CreditCard();
   }

   private synchronized boolean tryStart() {
      if (mReadStatus == 3) {
         mReadStatus = 1;
         return true;
      } else {
         return false;
      }
   }

   private static synchronized boolean tryStop() {
      if (mReadStatus == 1) {
         mReadStatus = 2;
         return true;
      } else {
         return false;
      }
   }

   public void run() {
      if (this.tryStart()) {
         try {
            mCC = new CreditCard();
            CreditCardReader.startCardRead(mCC, mStripSentinels);
            mReadStatus = 3;
            String[] lNames = new String[]{"Track"};
            String[] lTypes = new String[]{"String"};
            RCSet lRCSet = new RCSet(lNames, lTypes);
            String[] lValues = new String[1];
            if (mCC.mTrack1 != null) {
               lValues[0] = mCC.mTrack1;
               lRCSet.addRow(lValues);
               Aem.logDetailMessage(DvdplayLevel.FINE, "Track1 is " + mCC.mTrack1);
            }

            if (mCC.mTrack2 != null) {
               lValues[0] = mCC.mTrack2;
               lRCSet.addRow(lValues);
               Aem.logDetailMessage(DvdplayLevel.FINE, "Track2 is " + mCC.mTrack2);
            }

            if (mCC.mTrack3 != null) {
               lValues[0] = mCC.mTrack3;
               lRCSet.addRow(lValues);
               Aem.logDetailMessage(DvdplayLevel.FINE, "Track3 is " + mCC.mTrack3);
            }

            if (lRCSet.rowCount() != 0) {
               NvPairSet lNvPairSet = new NvPairSet();
               lNvPairSet.add(new NvPair("Track", "RCSet", lRCSet));
               DataPacketComposer lDPC = new DataPacketComposer();
               mCreditCard = lDPC.nvMarshal(lNvPairSet);
               Aem.logDetailMessage(DvdplayLevel.FINE, mCreditCard);
            } else {
               Aem.logDetailMessage(DvdplayLevel.FINE, "Bad swipe.");
               mCreditCard = null;
            }

            readDone = true;
         } catch (Exception var7) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var7.getMessage());
            mCreditCard = null;
         }
      }
   }

   public static void stopCardRead() {
      if (tryStop()) {
         CreditCardReader.stopCardRead();
         stopped = true;
         mCreditCard = "";
      }
   }

   public static String getCreditCardTrack() {
      return mCreditCard;
   }
}
