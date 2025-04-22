package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

import net.dvdplay.aem.*;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.dom.DOMData;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class InventoryCheck extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;

   public InventoryCheck(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   protected static ServoEx getServoEx() {
      Servo lServo = ServoFactory.getInstance();
      if (lServo instanceof ServoEx) {
         return (ServoEx)lServo;
      } else {
         throw new AemException("InventoryCheck: Incorrect servo type.");
      }
   }

   protected static Door getDoor() {
      return getServoEx().getDoor();
   }

   protected static Roller getRoller() {
      return getServoEx().getRoller();
   }

   public static String readBarCode() {
      BarCode lBarCode = new BarCode();
      Servo lServo = ServoFactory.getInstance();

      try {
         lServo.ejectDisc(0);

         while (!AbstractHardwareThread.mReturnOp.discAtDoor()) {
            Util.sleep(100);
         }

         try {
            lServo.mLight.on();
            lServo.readBarCode(lBarCode);
            lServo.mLight.off();
         } catch (Exception var3) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage());
            lServo.pullCaseInFromInspect();
            return null;
         }

         if (lBarCode.mCode1 != null && lBarCode.mCode2 != null) {
            lServo.intakeDisc(4000, true);
            return lBarCode.mCode1 != null && lBarCode.mCode2 != null ? lBarCode.mCode1 + "," + lBarCode.mCode2 : null;
         } else {
            lServo.pullCaseInFromInspect();
            return null;
         }
      } catch (DvdplayException var4) {
         lServo.releaseDisc();
         Aem.logDetailMessage(DvdplayLevel.ERROR, var4.getMessage());
         throw new DvdplayException("Error checking disc");
      }
   }

   public static void checkSlot(int aSlot, boolean aReadDisc) {
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 10);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      String[] lNames = new String[2];
      String[] lValues = new String[2];
      StringTokenizer lStrTok = null;
      Servo lServo = ServoFactory.getInstance();

      try {
         if (Aem.inQuiesceMode()) {
            throw new DvdplayException("Quiesce Mode! Abort");
         } else {
            boolean lDiscInSlot;
            if (lServo.goToSlot(aSlot) == 2) {
               lDiscInSlot = true;
            } else {
               lDiscInSlot = false;
            }

            lNames[0] = "Slot";
            lNames[1] = "DiscStatusId";
            lValues[0] = Integer.toString(aSlot);
            lValues[1] = Integer.toString(3);
            boolean lExpectDiscInSlot = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, lNames, lValues) >= 0;
            if (!lExpectDiscInSlot) {
               lNames[0] = "Slot";
               lNames[1] = "DiscStatusId";
               lValues[0] = Integer.toString(aSlot);
               lValues[1] = Integer.toString(9);
               lExpectDiscInSlot = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, lNames, lValues) >= 0;
            }

            if (lExpectDiscInSlot) {
               if (lDiscInSlot) {
                  if (aReadDisc) {
                     String lBarCode = readBarCode();
                     if (lBarCode == null) {
                        int lIndex = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, "Slot", Integer.toString(aSlot));
                        String lErrMsg = "Slot "
                           + aSlot
                           + ": Could not read barcode. Missing disc ("
                           + ""
                           + DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("GroupCode"))
                           + ","
                           + DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("DiscCode"))
                           + ")";
                        Aem.logSummaryMessage(lErrMsg);
                        textarea.setText(textarea.getText() + lErrMsg + "\n");
                        Aem.createDiscMissing(
                           Aem.getDisc(Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"))))
                        );
                        String lDiscCode = DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("DiscCode"));
                        String lGroupCode = DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("GroupCode"));
                        int lSlot = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("Slot")));
                        int lDiscDetailId = Integer.parseInt(
                           DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"))
                        );
                        int lTitleDetailId = Integer.parseInt(
                           DOMData.mDiscDetailData.getFieldValue(lIndex, DOMData.mDiscDetailData.getFieldIndex("TitleDetailId"))
                        );
                        if (lDiscCode.trim().length() != 0 && lGroupCode.trim().length() != 0) {
                           Aem.removeDisc(lDiscDetailId, lTitleDetailId, lDiscCode, lGroupCode, true);
                        } else {
                           Aem.removeUnknownDisc(lSlot);
                        }

                        DOMData.save();
                     } else {
                        lStrTok = new StringTokenizer(lBarCode, ",");
                        lNames = new String[3];
                        lValues = new String[3];
                        lNames[0] = "Slot";
                        lNames[1] = "GroupCode";
                        lNames[2] = "DiscCode";
                        lValues[0] = Integer.toString(aSlot);
                        lValues[1] = lStrTok.nextToken();
                        lValues[2] = lStrTok.nextToken();
                        if (Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, lNames, lValues) < 0) {
                           int var29 = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, "Slot", Integer.toString(aSlot));
                           String var26 = "Slot "
                              + aSlot
                              + ": Unknown disc found: ("
                              + lValues[1]
                              + ","
                              + lValues[2]
                              + "). "
                              + "Expected ("
                              + DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("GroupCode"))
                              + ","
                              + DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("DiscCode"))
                              + ")";
                           Aem.logSummaryMessage(var26);
                           textarea.setText(textarea.getText() + var26 + "\n");
                           Aem.createDiscMissing(
                              Aem.getDisc(Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"))))
                           );
                           String lDiscCode = DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("DiscCode"));
                           String lGroupCode = DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("GroupCode"));
                           int lSlot = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("Slot")));
                           int lDiscDetailId = Integer.parseInt(
                              DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"))
                           );
                           int lTitleDetailId = Integer.parseInt(
                              DOMData.mDiscDetailData.getFieldValue(var29, DOMData.mDiscDetailData.getFieldIndex("TitleDetailId"))
                           );
                           if (lDiscCode.trim().length() != 0 && lGroupCode.trim().length() != 0) {
                              Aem.removeDisc(lDiscDetailId, lTitleDetailId, lDiscCode, lGroupCode, true);
                           } else {
                              Aem.removeUnknownDisc(lSlot);
                           }

                           Aem.createDiscFound(lValues[1], lValues[2], aSlot);
                           DOMData.save();
                        } else {
                           textarea.setText(textarea.getText() + "Slot " + aSlot + ": ok\n");
                        }
                     }
                  } else {
                     textarea.setText(textarea.getText() + "Slot " + aSlot + ": ok\n");
                  }
               } else {
                  int var30 = Util.getRCSetIndexForFieldValue(DOMData.mDiscDetailData, "Slot", Integer.toString(aSlot));
                  String var27 = "Slot "
                     + aSlot
                     + ": Missing disc ("
                     + ""
                     + DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("GroupCode"))
                     + ","
                     + DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("DiscCode"))
                     + ")";
                  Aem.logSummaryMessage(var27);
                  textarea.setText(textarea.getText() + var27 + "\n");
                  Aem.createDiscMissing(
                     Aem.getDisc(Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId"))))
                  );
                  String lDiscCode = DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("DiscCode"));
                  String lGroupCode = DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("GroupCode"));
                  int lSlot = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("Slot")));
                  int lDiscDetailId = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("DiscDetailId")));
                  int lTitleDetailId = Integer.parseInt(DOMData.mDiscDetailData.getFieldValue(var30, DOMData.mDiscDetailData.getFieldIndex("TitleDetailId")));
                  if (lDiscCode.trim().length() != 0 && lDiscCode.trim().length() != 0) {
                     Aem.removeDisc(lDiscDetailId, lTitleDetailId, lDiscCode, lGroupCode, true);
                  } else {
                     Aem.removeUnknownDisc(lSlot);
                  }

                  DOMData.save();
               }
            } else if (lDiscInSlot) {
               if (aReadDisc) {
                  String var23 = readBarCode();
                  if (var23 == null) {
                     String var28 = "Slot " + aSlot + ": Could not read barcode.";
                     Aem.logSummaryMessage(var28);
                     textarea.setText(textarea.getText() + var28 + "\n");
                  } else {
                     lStrTok = new StringTokenizer(var23, ",");
                     String lGroupCode = lStrTok.nextToken();
                     String lDiscCode = lStrTok.nextToken();
                     Aem.logSummaryMessage("Slot " + aSlot + ": Unknown disc found: (" + lGroupCode + "," + lDiscCode + ")");
                     textarea.setText(textarea.getText() + "Slot " + aSlot + ": Unknown disc found: (" + lGroupCode + "," + lDiscCode + ")\n");
                     Aem.createDiscFound(lGroupCode, lDiscCode, aSlot);
                  }
               } else {
                  Aem.logSummaryMessage("Slot " + aSlot + ": Unknown disc found");
                  textarea.setText(textarea.getText() + "Slot " + aSlot + ": Unknown disc found\n");
                  Aem.createDiscFound("", "", aSlot);
               }
            } else {
               textarea.setText(textarea.getText() + "Slot " + aSlot + ": ok\n");
            }
         }
      } catch (DvdplayException var20) {
         textarea.setText(textarea.getText() + "Error\n");
         Aem.logDetailMessage(DvdplayLevel.ERROR, var20.getMessage());
         if (aReadDisc) {
            throw new DvdplayException("Check Disc failed");
         } else {
            throw new DvdplayException("Check Slots failed");
         }
      }
   }

   public static String checkSlots(String data) {
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 10);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);

      try {
         StringTokenizer lStrTok = new StringTokenizer(data, ",");
         boolean lCheckDisc = Boolean.valueOf(lStrTok.nextToken());
         int lFromSlot = Integer.parseInt(lStrTok.nextToken());
         int lToSlot = Integer.parseInt(lStrTok.nextToken());
         if (lFromSlot < 1) {
            lFromSlot = 1;
         }

         if (lToSlot > ServoFactory.getInstance().getNumSlots()) {
            lToSlot = ServoFactory.getInstance().getNumSlots();
         }

         if (lToSlot < lFromSlot) {
            return null;
         } else {
            int finalLFromSlot = lFromSlot;
            int finalLToSlot = lToSlot;
            Thread def = new Thread() {
               @Override
               public void run() {
                  String lName;
                  Aem lAem = AemFactory.getInstance();
                  Servo lServo = ServoFactory.getInstance();
                  try {
                     if (lCheckDisc) {
                        lName = "CheckDiscs";
                     } else {
                        lName = "CheckSlots";
                     }
                     Aem.logSummaryMessage("Starting " + lName + DvdplayBase.SPACE_STRING + finalLFromSlot + "-" + finalLToSlot);
                     textarea.setText(textarea.getText() + "Initializing ...\n");
                     lServo.initialize();
                     textarea.setText(textarea.getText() + "Starting " + lName + "\n");
                     Aem.setDiscActive();
                     for (int i = finalLFromSlot; i <= finalLToSlot; i++) {
                        InventoryCheck.checkSlot(i, lCheckDisc);
                     }
                  } catch (DvdplayException e) {
                     Aem.logDetailMessage(DvdplayLevel.ERROR, e.getMessage());
                  }
                  Aem.addDiscMissingQueueJob();
                  Aem.addDiscFoundQueueJob();
                  Aem.resetDiscActive();
                  if (Aem.inQuiesceMode()) {
                     lAem.exitApp(Aem.getQuiesceMode());
                  } else {
                     Aem.runQueueJobs();
                  }
               }
            };

            def.start();
            return null;
         }
      } catch (Exception var12) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var12.getMessage());
         return null;
      }
   }
}
