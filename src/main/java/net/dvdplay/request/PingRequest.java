package net.dvdplay.request;

import net.dvdplay.communication.NvPair;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;

public class PingRequest extends Request {
   private static final String NAME_SENDER_ACK = "SENDER_ACK";
   private static final String NAME_SENDER_STATUS = "SENDER_STATUS";
   private static final String NAME_RECEIVER_REQUEST = "RECEIVER_REQUEST";
   private static final String NAME_PING = "PING";

   public PingRequest(int kioskId, String version, String receiverRequest, String senderStatus, RCSet senderAck) throws DvdplayException {
      super("PING", "KIOSK", kioskId, version);

      try {
         super.setParameter("RECEIVER_REQUEST", "STRING", receiverRequest);
         super.setParameter("SENDER_STATUS", "STRING", senderStatus);
         super.setParameter("SENDER_ACK", "RCSET", senderAck);
      } catch (Exception var7) {
         throw new DvdplayException(4000, var7);
      }
   }

   public PingRequest(int kioskId, String version) throws DvdplayException {
      super("PING", "KIOSK", kioskId, version);

      try {
         super.setParameter("RECEIVER_REQUEST", "STRING", "");
         super.setParameter("SENDER_STATUS", "STRING", "");
         super.setParameter("SENDER_ACK", "RCSET", new RCSet());
      } catch (Exception var4) {
         throw new DvdplayException(4000, var4);
      }
   }

   PingRequest(NvPairSet payload) throws DvdplayException {
      super(payload);

      try {
         NvPair receiverPair = payload.getNvPair("RECEIVER_REQUEST");
         String receiver = receiverPair.getValueAsString();
         super.setParameter("RECEIVER_REQUEST", "STRING", receiver);
         NvPair senderStatusPair = payload.getNvPair("SENDER_STATUS");
         String senderStatus = senderStatusPair.getValueAsString();
         super.setParameter("SENDER_STATUS", "STRING", senderStatus);
         NvPair senderAckPair = payload.getNvPair("SENDER_ACK");
         RCSet senderAck = senderAckPair.getValueAsRCSet();
         super.setParameter("SENDER_ACK", "RCSET", senderAck);
      } catch (Exception var8) {
         throw new DvdplayException(4000, var8);
      }
   }

   public String getReceiverRequest() throws DvdplayException {
      try {
         return super.getParameterValueAsString("RECEIVER_REQUEST");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public String getSenderStatus() throws DvdplayException {
      try {
         return super.getParameterValueAsString("SENDER_STATUS");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }

   public RCSet getSenderAck() throws DvdplayException {
      try {
         return super.getParameterValueAsRCSet("SENDER_ACK");
      } catch (Exception var2) {
         throw new DvdplayException(4000, var2);
      }
   }
}
