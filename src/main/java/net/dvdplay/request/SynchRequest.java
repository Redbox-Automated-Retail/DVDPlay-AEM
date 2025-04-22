package net.dvdplay.request;

import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.exception.DvdplayException;

public class SynchRequest extends Request {
   int argsLoIndex = 0;
   int argsHiIndex = 0;
   private static final String NAME_SYNCH = "SYNCH";
   private static final String NAME_KIOSK = "KIOSK";
   private static final String NAME_TASK_NAME = "TASK_NAME";

   public SynchRequest(int kioskId, String version, String name, NvPairSet arguments) throws DvdplayException {
      super("SYNCH", "KIOSK", kioskId, version);

      try {
         super.setParameter("TASK_NAME", "STRING", name);
         this.argsLoIndex = super.size() - 1;
         super.addUserParameters(arguments);
         this.argsHiIndex = super.size() - 1;
      } catch (Exception var6) {
         throw new DvdplayException(4000, var6);
      }
   }

   public SynchRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         NvPair taskNamePair = payload.getNvPair("TASK_NAME");
         String taskName = taskNamePair.getValueAsString();
         super.setParameter("TASK_NAME", "STRING", taskName);
         this.argsLoIndex = super.size() - 1;
         int totElements = payload.size();
         NvPairSet userParms = new NvPairSet();

         for (int i = this.argsLoIndex + 1; i < totElements; i++) {
            NvPair aPair = payload.getNvPair(i);
            userParms.add(aPair);
         }

         super.addUserParameters(userParms);
         this.argsHiIndex = super.size() - 1;
      } catch (Exception var8) {
         throw new DvdplayException(4000, var8);
      }
   }

   public String getTaskName() throws DvdplayException {
      try {
         return super.getParameterValueAsString("TASK_NAME");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public NvPairSet getArguments() throws DvdplayException {
      try {
         return super.getParameters(this.argsLoIndex, this.argsHiIndex);
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }
}
