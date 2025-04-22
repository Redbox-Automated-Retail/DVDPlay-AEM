package net.dvdplay.hardware;

import java.awt.event.ActionListener;
import java.util.Random;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemException;
import net.dvdplay.aem.Arm;
import net.dvdplay.aem.Door;
import net.dvdplay.aem.Roller;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoEx;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.util.Util;

public class CycleTest extends AbstractHardwareThread {
   static ActionListener action;
   static JComponent component;
   static boolean isStopped;
   static boolean isStopOnError;

   public CycleTest(ActionListener lAction, JComponent lComponent) {
      component = lComponent;
      action = lAction;
   }

   public static String getSlotOneOffset() {
      return Integer.toString(getServo().getServoOffset());
   }

   public static String getStartAtSlot() {
      return "1";
   }

   public static boolean getForwardBackward() {
      return true;
   }

   public static boolean getRandom() {
      return false;
   }

   public static String getInstruction() {
      return "Before you start the cycle test: \n 1. Remove all discs from the carousel and the intake slot. \n 2. Choose Init. This will home the carousel, set the eject arm and lock the intake slot.\n 3. Insert the test discs all the way into the intake slot.\n 4. Set the slot one offset and start at slot, then choose Go.";
   }

   public static String init(String data) {
      JPanel resultPanel = (JPanel)component.getComponentAt(10, 150);
      JPanel initPanel = (JPanel)resultPanel.getComponent(0);
      JButton init = (JButton)initPanel.getComponent(0);
      JPanel buttonPanel = (JPanel)resultPanel.getComponent(3);
      JButton go = (JButton)buttonPanel.getComponent(0);
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 240);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      Thread def = new Thread() {
         @Override
         public void run() {
            init.setEnabled(false);
            try {
               textarea.setText("Initializing hardware ...\n");
               CycleTest.getServo().initialize();
            } catch (DvdplayException e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
               textarea.setText(textarea.getText() + "ERROR: " + e.getMessage() + "\n");
            }
            init.setEnabled(true);
            go.setEnabled(true);
         }
      };

      def.start();
      return null;
   }

   public static void discOut() {
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 240);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      if (!isStopped) {
         textarea.setText(textarea.getText() + "Eject disc\n");
         Servo var10000 = getServo();
         getServo();
         var10000.ejectDisc(2);
      }
   }

   public static void discIn() {
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 240);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      if (!isStopped) {
         textarea.setText(textarea.getText() + "Intake disc\n");
         getServo().intakeDisc(4000, false);
         Util.sleep(500);
      }
   }

   public static void testSlot(int aSlot) {
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 240);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      textarea.setText(textarea.getText() + "Going to slot " + aSlot + "\n");

      try {
         getServoEx().getArm();
         Arm.clear();
         getServoEx().getDoor();
         Door.close();
         getServo().goToSlot(aSlot);
      } catch (DvdplayException e) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, e.getMessage());
         textarea.setText(textarea.getText() + "ERROR: " + e.getMessage() + "\n");
         throw new DvdplayException(e.getMessage());
      }

      if (!isStopped) {
         discIn();
         if (!isStopped) {
            discOut();
         }
      }
   }

   public static String go(String data) {
      JPanel resultPanel = (JPanel)component.getComponentAt(10, 150);
      JPanel buttonPanel = (JPanel)resultPanel.getComponent(3);
      JButton go = (JButton)buttonPanel.getComponent(0);
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 240);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      StringTokenizer lStrTok = new StringTokenizer(data, ",");
      int llSlot = Integer.parseInt(lStrTok.nextToken());
      getServo().setServoOffset(Integer.parseInt(lStrTok.nextToken()));
      boolean lFBmode = Boolean.parseBoolean(lStrTok.nextToken());
      lStrTok.nextToken();
      boolean lStopOnError = Boolean.parseBoolean(lStrTok.nextToken());
      isStopped = false;
      isStopOnError = lStopOnError;
      Thread def = new Thread() {
         @Override
         public void run() {
            int lSlot = llSlot;
            go.setEnabled(false);

            try {
               while (true) {
                  if (lFBmode) {
                     for (int i = lSlot; i <= CycleTest.getServo().getNumSlots() && !CycleTest.isStopped; i++) {
                        CycleTest.testSlot(i);
                     }

                     lSlot = 1;

                     for (int i = CycleTest.getServo().getNumSlots() - 1; i > 0 && !CycleTest.isStopped; i--) {
                        CycleTest.testSlot(i);
                     }
                  } else {
                     if (CycleTest.isStopped) {
                        break;
                     }

                     CycleTest.testSlot(new Random(System.currentTimeMillis()).nextInt(CycleTest.getServo().getNumSlots() - 1) + 1);
                  }

                  if (CycleTest.isStopped) {
                     try {
                        CycleTest.getServoEx().getRoller();
                        Roller.stop();
                        CycleTest.getServoEx().getDoor();
                        Door.open();
                        CycleTest.getServoEx().getArm();
                        Arm.clear();
                     } catch (DvdplayException var5) {
                     }

                     textarea.setText(textarea.getText() + "CycleTest Stopped" + "\n\n");
                     break;
                  }
               }
            } catch (DvdplayException e) {
               try {
                  CycleTest.getServoEx().getRoller();
                  Roller.stop();
                  CycleTest.getServoEx().getDoor();
                  Door.open();
                  CycleTest.getServoEx().getArm();
                  Arm.clear();
               } catch (DvdplayException ignored) {
               }

               Aem.logDetailMessage(DvdplayLevel.ERROR, e.getMessage());
               textarea.setText(textarea.getText() + "ERROR: " + e.getMessage() + "\n");
            }

            go.setEnabled(true);
         }
      };
      def.start();
      return null;
   }

   public static String stop(String data) {
      isStopped = true;
      JPanel resultPanel = (JPanel)component.getComponentAt(10, 150);
      JPanel buttonPanel = (JPanel)resultPanel.getComponent(3);
      JButton go = (JButton)buttonPanel.getComponent(0);
      JPanel statusPanel = (JPanel)component.getComponentAt(10, 240);
      JScrollPane jsp = (JScrollPane)statusPanel.getComponent(0);
      JViewport jvp = (JViewport)jsp.getComponent(0);
      JTextArea textarea = (JTextArea)jvp.getComponent(0);
      go.setEnabled(true);
      return null;
   }

   protected static Servo getServo() {
      return ServoFactory.getInstance();
   }

   protected static ServoEx getServoEx() {
      Servo lServo = getServo();
      if (lServo instanceof ServoEx) {
         return (ServoEx)lServo;
      } else {
         throw new AemException("CycleTest: downcast servo failed.");
      }
   }

   protected static Door getDoor() {
      return getServoEx().getDoor();
   }

   protected static Roller getRoller() {
      return getServoEx().getRoller();
   }
}
