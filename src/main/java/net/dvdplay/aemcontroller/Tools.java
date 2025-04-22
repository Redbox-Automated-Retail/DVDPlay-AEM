package net.dvdplay.aemcontroller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoException;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.hardware.Login;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.screen.BadSlotsPanel;
import net.dvdplay.screen.BarCamPegTestPanel;
import net.dvdplay.screen.CardReaderHIDPanel;
import net.dvdplay.screen.ControllerPanel;
import net.dvdplay.screen.ControllerPanelFactory;
import net.dvdplay.screen.CycleTestPanel;
import net.dvdplay.screen.InventoryCheckPanel;
import net.dvdplay.screen.LoginPanel;
import net.dvdplay.screen.OperatorPanel;
import net.dvdplay.screen.RemoveDiscsPanel;
import net.dvdplay.screen.ServoParamsPanel;
import net.dvdplay.screen.ServoParamsPanelFactory;
import net.dvdplay.screen.SimpleRemoveDiscsPanel;
import net.dvdplay.screen.SlotCalPanel;
import net.dvdplay.screen.SlotCalPanelFactory;
import net.dvdplay.ui.ScreenProperties;

public class Tools extends JFrame {
   int role;
   JTabbedPane jtp;
   String command;
   String action;
   String data;
   StringTokenizer stk;
   final JDialog d = new JDialog();
   JLabel l;
   JButton yes;
   JButton no;
   ActionListener lAppAl;
   LoginPanel login;
   ScreenProperties sProperty = new ScreenProperties();
   boolean killAll;

   public Tools(ActionListener appAL) {
      super("DVDPlay AEM Tool Manager");
      this.setUndecorated(true);

      try {
         this.lAppAl = appAL;
         if (!Aem.isToolsRunning()) {
            Aem.setToolsRunning(true);
            Container content = this.getContentPane();
            content.setBackground(Color.BLACK);
            content.setLayout(new FlowLayout(1, 20, 20));
            this.login = new LoginPanel();
            this.jtp = new JTabbedPane();
            this.jtp.addTab("Login", null, this.login, "Login");
            this.login.addActionListener(new ToolsAction());
            this.jtp.setPreferredSize(new Dimension(990, 730));
            this.jtp.setBackground(Color.BLUE);
            this.jtp.setForeground(Color.WHITE);
            content.add(this.jtp);
            addWindowListener(new WindowAdapter() {
               public void windowClosing(WindowEvent we) {
                  Tools.this.setVisible(true);
                  if (!Tools.this.jtp.getSelectedComponent().getClass().getName().equals("net.dvdplay.screen.LoginPanel")) {
                     Tools.this.killAll = false;
                     Tools.this.exitPopup(Tools.this.lAppAl);
                  } else if (Tools.this.lAppAl != null) {
                     Tools.this.lAppAl.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.INITIALIZING_AEM_SCREEN));
                  } else {
                     AemFactory.getInstance().exitApp(1);
                  }
                  Aem.setLastRobotClickReceived(System.currentTimeMillis());
                  Aem.setLastLogReceived(System.currentTimeMillis());
                  Tools.this.resetToolsRunning();
               }
            });

            this.setSize(1024, 768);
            this.setLocation(0, 0);
            this.setResizable(false);
            this.setVisible(true);
         }
      } catch (Exception var3) {
         Log.summary(var3, "Tools");
      }
   }

   private void success(int role) {
      try {
         this.setCursor(new Cursor(0));
         this.jtp.removeTabAt(0);
         if (Aem.isRolePrivileged(44, role)) {
            OperatorPanel operator = new OperatorPanel();
            this.jtp.addTab("Operator", null, operator, "Operator");
            operator.addActionListener(new ToolsAction());
         }

         if (Aem.isRolePrivileged(54, role)) {
            SimpleRemoveDiscsPanel simpleRemoveDiscs = new SimpleRemoveDiscsPanel();
            simpleRemoveDiscs.addActionListener(new ToolsAction());
            this.jtp.addTab("Simple Remove Discs", null, simpleRemoveDiscs, "SimpleRemoveDiscs");
         }

         if (Aem.isRolePrivileged(46, role)) {
            RemoveDiscsPanel removeDiscs = new RemoveDiscsPanel();
            this.jtp.addTab("Remove Discs", null, removeDiscs, "RemoveDiscs");
         }

         if (Aem.isRolePrivileged(48, role)) {
            InventoryCheckPanel inventoryCheck = new InventoryCheckPanel();
            this.jtp.addTab("Inventory Check", null, inventoryCheck, "InventoryCheck");
         }

         if (Aem.isRolePrivileged(51, role)) {
            BadSlotsPanel badSlots = new BadSlotsPanel();
            this.jtp.addTab("Bad Slots", null, badSlots, "BadSlots");
         }

         if (Aem.isRolePrivileged(53, role)) {
            SlotCalPanel slotCal = SlotCalPanelFactory.getInstance();
            this.jtp.addTab("Slot Cal.", null, slotCal, "SlotCalEx");
         }

         if (Aem.isRolePrivileged(43, role)) {
            ServoParamsPanel servoParams = ServoParamsPanelFactory.getInstance();
            this.jtp.addTab("ServoParam", null, servoParams, "ServoParams");
         }

         if (Aem.isRolePrivileged(45, role)) {
            ControllerPanel controller = ControllerPanelFactory.getInstance();
            this.jtp.addTab("Controller", null, controller, "Controller");
         }

         if (Aem.isRolePrivileged(50, role)) {
            CardReaderHIDPanel cardReaderHID = new CardReaderHIDPanel();
            this.jtp.addTab("Card Reader HID", null, cardReaderHID, "CardReaderHID");
         }

         if (Aem.isRolePrivileged(52, role)) {
            BarCamPegTestPanel barCamPegTest = new BarCamPegTestPanel();
            this.jtp.addTab("BarCamPegTest", null, barCamPegTest, "BarCamPegTest");
         }

         if (Aem.isRolePrivileged(49, role)) {
            CycleTestPanel cycleTest = new CycleTestPanel();
            this.jtp.addTab("Cycle Test", null, cycleTest, "CycleTest");
         }
      } catch (Exception var3) {
         Log.summary(var3, "Tools.success");
      }
   }

   private void fail(int error) {
      switch (error) {
         case 0:
            this.login.writeMessage("Wrong username/password combination, please try again!");
            Aem.logSummaryMessage("Login Failed");
            break;
         case 1:
            this.login.writeMessage("Cannot start tools due to a servo error");
            this.login.disableLoginButton();
            Aem.logSummaryMessage("Cannot start tools due to a servo error");
      }
   }

   public static void main(String[] args) {
      AbstractHardwareThread.init();
      Aem.initializeLog();

      try {
         new Tools(null);
      } catch (Throwable e) {
         Log.summary(e, "Tools.main");
      }
   }

   private void resetToolsRunning() {
      Aem.setToolsRunning(false);
      ServoParamsPanelFactory.reset();
      ControllerPanelFactory.reset();
      SlotCalPanelFactory.reset();
   }

   public void exitPopup(ActionListener appAl) {
      this.setVisible(true);
      this.yes = new JButton("Yes");
      this.no = new JButton("No");
      this.l = new JLabel("Any unsaved data will be removed permanently, do you want to proceed to exit?");
      this.l.setHorizontalAlignment(0);
      this.d.setSize(500, 150);
      this.d.getContentPane().setLayout(new BorderLayout());
      this.d.getContentPane().add(this.l, "Center");
      this.yes.setActionCommand("Yes");
      this.no.setActionCommand("No");
      this.no.addActionListener(new Al(this, appAl));
      this.yes.addActionListener(new Al(this, appAl));
      JPanel p = new JPanel();
      p.add(this.yes);
      p.add(this.no);
      this.d.getContentPane().add(p, "South");
      this.d.setBounds(50, 250, 700, 150);
      this.setVisible(true);
      this.d.setVisible(true);
   }

   private class ToolsAction implements ActionListener {
      private ToolsAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            Servo lServo = ServoFactory.getInstance();

            try {
               lServo.initServo();
            } catch (ServoException var4) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, var4.getMessage(), var4);
            }

            Tools.this.command = ae.getActionCommand();
            Tools.this.stk = new StringTokenizer(Tools.this.command, " ");
            Tools.this.action = Tools.this.stk.nextToken();
            Tools.this.data = "";
            Aem.logSummaryMessage("[Tools] Action " + Tools.this.action + " Data " + Tools.this.data);
            if (Tools.this.stk.hasMoreTokens()) {
               Tools.this.data = Tools.this.stk.nextToken();
            }

            if (Tools.this.command.equals("closeWithoutRestart")) {
               Aem.setLastRobotClickReceived(System.currentTimeMillis());
               Aem.setLastLogReceived(System.currentTimeMillis());
               if (Tools.this.lAppAl == null) {
                  AemFactory.getInstance().exitApp(1);
               }

               Tools.this.resetToolsRunning();
               Tools.this.setVisible(false);
            }

            if (Tools.this.command.equals("close")) {
               Tools.this.killAll = false;
               Tools.this.exitPopup(Tools.this.lAppAl);
            }

            if (Tools.this.command.equals("closeAll")) {
               Tools.this.killAll = true;
               Tools.this.exitPopup(Tools.this.lAppAl);
            }

            if (Tools.this.action.equals("Login")) {
               Tools.this.role = Login.authenticate(Tools.this.data);
               if (lServo.getServoModuleStatus()) {
                  if (Tools.this.role >= 0) {
                     Tools.this.success(Tools.this.role);
                     Aem.logSummaryMessage("Starting Tools.");
                     Aem.logSummaryMessage("Login Success with Role " + Tools.this.role);
                  } else {
                     Tools.this.fail(0);
                  }
               } else {
                  Tools.this.fail(1);
               }
            }
         } catch (Exception var5) {
            Log.summary(var5, "ToolsAction");
         }
      }
   }

   class Al implements ActionListener {
      private final ActionListener val$appAl;
      private final Tools this$0;

      Al(Tools this$0, ActionListener val$appAl) {
         this.this$0 = this$0;
         this.val$appAl = val$appAl;
      }

      public void actionPerformed(ActionEvent ae) {
         if (ae.getActionCommand().equals("Yes")) {
            Aem.logSummaryMessage("Exiting Tools.");
            if (this.this$0.killAll) {
               AemFactory.getInstance().exitApp(1);
               return;
            }
            if (this.val$appAl != null) {
               this.val$appAl.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.INITIALIZING_AEM_SCREEN));
               this.this$0.setVisible(false);
            } else {
               AemFactory.getInstance().exitApp(1);
            }
            Aem.setLastRobotClickReceived(System.currentTimeMillis());
            Aem.setLastLogReceived(System.currentTimeMillis());
            this.this$0.resetToolsRunning();
            this.this$0.d.setVisible(false);
            this.this$0.d.dispose();
         }
         if (ae.getActionCommand().equals("No")) {
            this.this$0.setVisible(true);
            this.this$0.d.setVisible(false);
            this.this$0.d.dispose();
         }
      }
   }
}
