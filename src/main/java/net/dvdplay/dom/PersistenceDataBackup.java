package net.dvdplay.dom;

import net.dvdplay.communication.RCSet;

public class PersistenceDataBackup extends PersistenceData {
   protected PersistenceData mMasterPData;

   public PersistenceDataBackup(String[] aNames, String[] aTypes, String aFilename, String aDisplayName, PersistenceData lMasterData) {
      super(aNames, aTypes, aFilename, aDisplayName, true, DOMData.mCheckSumData2);
      this.mMasterPData = lMasterData;
   }

   public PersistenceDataBackup(String[] aNames, String[] aTypes, String aFilename, String aDisplayName, RCSet aRCSet, PersistenceData lMasterData) {
      super(aNames, aTypes, aFilename, aDisplayName, aRCSet, true, DOMData.mCheckSumData2);
      this.mMasterPData = lMasterData;
   }

   public synchronized void save(boolean aDoLogging) {
      for (int i = 0; i < this.rowCount(); i++) {
         this.deleteRow(i);
      }

      for (int i = 0; i < this.mMasterPData.rowCount(); i++) {
         if (!this.mMasterPData.isDeleted(i)) {
            this.addRow(this.mMasterPData.getRow(i));
         }
      }

      super.save(aDoLogging);
   }
}
