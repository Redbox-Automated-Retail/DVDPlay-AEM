package net.dvdplay.aemcontroller;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aem.Servo;
import net.dvdplay.aem.ServoFactory;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.AbstractHardwareThread;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.hardware.AuthorizingPaymentCard;
import net.dvdplay.hardware.DeliveringDVD;
import net.dvdplay.hardware.DetectingDiscIn;
import net.dvdplay.hardware.HelpMain;
import net.dvdplay.hardware.IdentifyingDisc;
import net.dvdplay.hardware.InitializingAEM;
import net.dvdplay.hardware.Main;
import net.dvdplay.hardware.PushingDiscAllTheWay;
import net.dvdplay.hardware.RemoveDVD;
import net.dvdplay.hardware.ReturnError;
import net.dvdplay.hardware.ReturningMovie;
import net.dvdplay.hardware.SwipePaymentCard;
import net.dvdplay.hardware.UnableToRecognizeMovie;
import net.dvdplay.hardware.Updating;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.screen.AEMStartupErrorScreen;
import net.dvdplay.screen.ApplicationInitializing;
import net.dvdplay.screen.DvdplayScreenException;
import net.dvdplay.screen.InitializingAEMScreen;
import net.dvdplay.screen.MainScreen;
import net.dvdplay.task.HeartBeatThread;
import net.dvdplay.util.Util;

public class AEMGui extends JFrame {
   AbstractContentBar tempBar;
   MainScreen mainScreen;
   InitializingAEMScreen initializingAEMScreen;
   Container content;
   private static final Object mActionPerformedKey = new Object();
   static String currentScreen;
   String nextScreen;
   String previousScreen;
   Hashtable reusableObject;
   static AbstractHardwareThread currThread;
   private static boolean mIsActionListenerWorking = false;
   String data = new String();
   String action = new String();
   private final String classPath = "net.dvdplay.screen.";
   Class c = null;
   Class[] args;
   boolean updatable;
   boolean showPageStatus;
   CommandMap cm;
   Object[] obj;
   Cursor handCursor;
   Cursor busyCursor;
   ArrayList<String> screenList;
   ApplicationInitializing appInit;
   private static int mArg1 = 0;
   public static boolean mHttps = true;
   public static Timer screenMonitor = new Timer(300000, evt -> {
      Log.error("*** SCREEN ALERT *** Setting Screen Error ... ");
      AemFactory.getInstance();
      Aem.setScreenError();
      AEMGui.screenMonitor.stop();
   });
   public static Timer EQHeartbeat = new Timer(60000, evt -> {
      AemFactory.getInstance();
      Aem.setLastLogReceived(System.currentTimeMillis());
      AEMGui.EQHeartbeat.stop();
      AEMGui.EQHeartbeat.start();
   });


   public static void main(String[] args) {
      Aem.initializeLog();

      try {
         if (args.length >= 1) {
            if (args[0].equals("http")) {
               mHttps = false;
               args[0] = "1";
            }

            if (args[0].equals("-standalone")) {
               try {
                  BufferedReader lIn = new BufferedReader(new InputStreamReader(System.in));
                  System.out.print("Enter login: ");
                  String lLogin = lIn.readLine();
                  System.out.print("Enter password: ");
                  String lPass = lIn.readLine();
                  if (lLogin.equals("login") && lPass.equals("pass")) {
                     Aem.setStandAloneMode();
                  } else {
                     System.out.println("Invalid login");
                     System.exit(1);
                  }
               } catch (Exception e) {
                  System.err.println(e.getMessage());
                  e.printStackTrace(System.err);
                  System.exit(1);
               }
            } else {
               Log.debug("Arg0 = " + args[0]);
               mArg1 = Integer.parseInt(args[0]);
            }
         }

         AEMGui aem = new AEMGui();
         aem.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
               AemFactory.getInstance().exitApp(1);
            }
         });

      } catch (Throwable e) {
         System.err.println("AEMGui.main" + e.getMessage());
         e.printStackTrace(System.err);
         System.err.flush();
         Log.summary(e, "AEMGui.main");
      }
   }

   public AEMGui() {
      super("AEM Application");
      this.args = new Class[]{
              String.class,
              String.class,
              String.class
      };
      this.updatable = false;
      this.showPageStatus = true;
      this.cm = new CommandMap();
      this.obj = new Object[3];
      this.handCursor = new Cursor(12);
      this.busyCursor = new Cursor(3);
      this.setUndecorated(true);
      this.content = this.getContentPane();
      this.reusableObject = new Hashtable();
      this.setCursor(this.handCursor);
      this.content.setLayout(null);
      this.setBackground(Color.WHITE);

      try {
         Log.summary(" ");
         Log.summary("++++++++ ++++++++ AEM App starting up ... ");
         this.appInit = new ApplicationInitializing();
         this.appInit.setBounds(0, 0, 1024, 768);
         this.appInit.setVisible(true);
         Log.info("[AEMGui.aemgui] Completed putting up 1st page");
         AbstractHardwareThread.init();
         Util.sleep(mArg1 * 1000);
         currentScreen = "InitializingAEMScreen";
         this.initializingAEMScreen = new InitializingAEMScreen("InitializingAEMScreen", "InitializingAEMScreen", "");
         Log.info("[AEMGui.aemgui] Completed constructing backend object");
         InitializingAEM iaem = new InitializingAEM(new AEMGui.ActionAEM(), (JComponent)this.content);
         this.tempBar = this.initializingAEMScreen;
         Log.info("[AEMGui.aemgui] Completed constructing 2nd page");
         if (this.showPage(currentScreen, this.content)) {
            currThread = iaem;
            iaem.start();
            Log.info("[AEMGui.aemgui] Initializing Hardware");
         }
      } catch (DvdplayException var4) {
         Log.error(var4, "[AEMGui.aemgui] AEM startup exception.");
         if (this.appInit != null) {
            this.appInit.setVisible(false);
            this.appInit = null;
         }

         this.content.removeAll();
         this.content.add(new AEMStartupErrorScreen());
         this.setBounds(0, 0, 1024, 768);
         this.repaint();
         this.setVisible(true);
         this.showPageStatus = false;

         try {
            HeartBeatThread.stopSendPings();
            Servo lServo = ServoFactory.getInstance();
            lServo.initServo();
            lServo.lcdOn();
            lServo.killAppWatchdog();
         } catch (Exception var3) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, var3.getMessage(), var3);
         }
      }
   }

   public boolean showPage(String lCurrentScreen, Container lContent) {
      ActionListener aemGUIAction = new AEMGui.ActionAEM();
      if (Objects.equals(lCurrentScreen, "InitializingAEMScreen")) {
         this.content.add(this.tempBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.appInit.setVisible(false);
         this.appInit = null;
         Log.info("[AEMGui.aemgui] Completed putting up 2nd page");
      }

      this.mainScreen = new MainScreen("MainScreen", "MainScreen", "");
      Util.sleep(1000);
      this.initialize();
      SwingUtilities.invokeLater(() -> {
         for (String screenName : screenList) {
            try {
               String model = "net.dvdplay.screen." + screenName;
               Class<?> screenClass = Class.forName(model);

               Object[] constructorArgs = new Object[]{"", "", ""};
               Constructor<?> ctor = screenClass.getConstructor(args);

               AbstractContentBar tempBar = (AbstractContentBar) ctor.newInstance(constructorArgs);
               reusableObject.put(screenName, tempBar);
               tempBar.addActionListener(aemGUIAction);

            } catch (Exception e) {
               Aem.logDetailMessage(DvdplayLevel.WARNING, "[Pre-Building Error] " + e);
            }
         }
      });

      CommandMap var10000 = this.cm;
      AemFactory.getInstance();
      var10000.init(Aem.getFranchiseID());
      Log.info("[AEMGui.aemgui] Completed pre-building screen objects");
      this.mainScreen.stopMovie();
      this.mainScreen.addActionListener(new AEMGui.ActionAEM());
      return true;
   }

   private void setCurrentScreenAndThread(String aScreen, AbstractHardwareThread aThread) {
      currentScreen = aScreen;
      currThread = aThread;
   }

   public static boolean isCurrentThread(AbstractHardwareThread aThread) {
      return aThread == currThread;
   }

   public static boolean isCurrentScreen(String aScreen) {
      return aScreen.equals(currentScreen);
   }

   public static void initEQHeartbeat() {
      AemFactory.getInstance();
      Aem.setLastLogReceived(System.currentTimeMillis());
      EQHeartbeat.start();
   }

   public static boolean isActionListenerWorking() {
      return mIsActionListenerWorking;
   }

   private void initialize() {
      this.screenList = new ArrayList<>();
      this.screenList.add("AboutCompanyScreen");
      this.screenList.add("AuthorizingPaymentScreen");
      this.screenList.add("CartTableScreen");
      this.screenList.add("DeliveringDVDScreen");
      this.screenList.add("DiscNotBelongScreen");
      this.screenList.add("DvdDescriptionScreen");
      this.screenList.add("EmailScreen");
      this.screenList.add("ErrorScreen");
      this.screenList.add("GameDescriptionScreen");
      this.screenList.add("HelpAnswerScreen");
      this.screenList.add("HelpMainScreen");
      this.screenList.add("IdentifyingMovieScreen");
      this.screenList.add("MaximumDiscExceededScreen");
      this.screenList.add("MovieSelectionScreen");
      this.screenList.add("MustBe18Screen");
      this.screenList.add("PaymentCardApprovedScreen");
      this.screenList.add("PollScreen");
      this.screenList.add("PromoCodeDescriptionScreen");
      this.screenList.add("PromoCodeScreen");
      this.screenList.add("PushingDiscAllTheWayScreen");
      this.screenList.add("RemoveDVDScreen");
      this.screenList.add("RentalAgreementScreen");
      this.screenList.add("ReturnErrorScreen");
      this.screenList.add("ReturningMovieScreen");
      this.screenList.add("ReturnMovieScreen");
      this.screenList.add("ReturnThankYouScreen");
      this.screenList.add("SwipePaymentCardScreen");
      this.screenList.add("TimeOutScreen");
      this.screenList.add("UnableToRecognizeMovieScreen");
      this.screenList.add("UpdatingScreen");
      this.screenList.add("ZipCodeScreen");
   }

   public class ActionAEM implements ActionListener {
      String model = null;
      final String STRING_CREATED = "Created ";
      final String STRING_AEM_MSG = "ActionAEM";
      final String STRING_ACTION = " Action: ";
      final String STRING_NEXT = " Next: ";
      final String STRING_CURR = "Curr: ";
      final String STRING_INVALID_CURR = " Invalid CurrentScreen";
      final String STRING_INVALID_NEXT = " Invalid NextScreen";
      final String STRING_DATA = " Data: ";
      Constructor ctor;
      StringTokenizer stk = null;
      StringBuffer msg = null;

      public void actionPerformed(ActionEvent ae) {
         synchronized (AEMGui.mActionPerformedKey) {
            AEMGui.mIsActionListenerWorking = true;

            try {
               if (AEMGui.currentScreen == null) {
                  throw new DvdplayScreenException(" Action: " + AEMGui.this.action + " Invalid CurrentScreen");
               }

               this.stk = new StringTokenizer(ae.getActionCommand(), " ");
               AEMGui.this.action = this.stk.nextToken();
               AEMGui.this.data = "";
               AemFactory.getInstance();
               Aem.setLastRobotClickReceived(System.currentTimeMillis());
               if (!AEMGui.this.action.equals("RobotClicking")) {
                  if (this.stk.hasMoreTokens()) {
                     AEMGui.this.data = this.stk.nextToken();
                  }

                  AEMGui.this.nextScreen = AEMGui.this.cm.getNextScreen(AEMGui.currentScreen, AEMGui.this.action);
                  Aem.logDetailMessage(
                     DvdplayLevel.INFO,
                     "Curr: " + AEMGui.currentScreen + " Action: " + AEMGui.this.action + " Next: " + AEMGui.this.nextScreen + " Data: " + AEMGui.this.data
                  );
                  AEMGui.this.setCursor(AEMGui.this.busyCursor);

                  try {
                     AEMGui.this.previousScreen = AEMGui.currentScreen;
                     if (AEMGui.currThread != null) {
                        this.msg = new StringBuffer("Requested ").append(AEMGui.currThread).append(" to stop.");
                        Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
                     }

                     AEMGui.this.setCurrentScreenAndThread(null, null);
                     if (AEMGui.this.previousScreen.equals("MainScreen") && !AEMGui.this.nextScreen.equals("MainScreen")) {
                        AEMGui.this.mainScreen.stopMovie();
                        AEMGui.this.content.remove(AEMGui.this.mainScreen);
                     }

                     AEMGui.screenMonitor.stop();
                     if (AEMGui.this.nextScreen.equals("MainScreen")) {
                        if (AEMGui.this.mainScreen.isTrailerAvailable()) {
                           AEMGui.screenMonitor.start();
                        }

                        if (AEMGui.this.previousScreen.equals("InitializingAEMScreen")) {
                           AEMGui.this.initializingAEMScreen = null;
                           AEMGui.this.content.removeAll();
                           AEMGui.this.content.add(AEMGui.this.mainScreen);
                           AEMGui.this.mainScreen.update("MainScreen", "MainScreen", "");
                           AEMGui.this.mainScreen.startMovie();
                        } else if (AEMGui.this.previousScreen.equals("MainScreen")) {
                           AEMGui.this.mainScreen.updateLocale(Integer.parseInt(AEMGui.this.data));
                        } else {
                           AEMGui.this.content.remove(AEMGui.this.tempBar);
                           AEMGui.this.mainScreen.startMovie();
                           AEMGui.this.mainScreen.update("MainScreen", "MainScreen", "");
                           AEMGui.this.content.add(AEMGui.this.mainScreen);
                        }
                     } else {
                        AEMGui.screenMonitor.start();
                        if (!AEMGui.this.previousScreen.equals("MainScreen")) {
                           AEMGui.this.content.remove(AEMGui.this.tempBar);
                        }

                        if (AEMGui.this.reusableObject.containsKey(AEMGui.this.nextScreen)) {
                           AEMGui.this.updatable = true;
                        } else {
                           AEMGui.this.updatable = false;
                           this.model = "net.dvdplay.screen." + AEMGui.this.nextScreen;
                           AEMGui.this.c = Class.forName(this.model);
                        }

                        if (AEMGui.this.updatable) {
                           AEMGui.this.tempBar = (AbstractContentBar)AEMGui.this.reusableObject.get(AEMGui.this.nextScreen);
                           AEMGui.this.tempBar.update(AEMGui.this.previousScreen, AEMGui.this.nextScreen, AEMGui.this.data);
                        } else {
                           Log.info("Not Updatable Screen " + AEMGui.this.nextScreen + " from " + AEMGui.this.previousScreen);
                           AEMGui.this.obj[0] = AEMGui.this.previousScreen;
                           AEMGui.this.obj[1] = AEMGui.this.nextScreen;
                           AEMGui.this.obj[2] = AEMGui.this.data;
                           this.ctor = AEMGui.this.c.getConstructor(AEMGui.this.args);
                           AEMGui.this.tempBar = (AbstractContentBar)this.ctor.newInstance(AEMGui.this.obj);
                           AEMGui.this.reusableObject.put(AEMGui.this.nextScreen, AEMGui.this.tempBar);
                           AEMGui.this.tempBar.addActionListener(this);
                        }

                        AEMGui.this.content.add(AEMGui.this.tempBar);
                     }

                     AEMGui.this.content.repaint();
                     this.switchScreensAndThreads();
                  } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                           InvocationTargetException | InstantiationException e) {
                     Log.summary(e, "ActionAEM");
                  }
               }
            } catch (DvdplayScreenException e) {
               Log.warning("ActionAEM: " + e.getMessage());
               this.resetCurrentScreen();
            } catch (Exception e) {
               Aem.setScreenError();
               Log.warning(e, "ActionAEM");
               this.resetCurrentScreen();
            } catch (Throwable e) {
               Aem.setScreenError();
               Log.warning(e, "ActionAEM");
               System.err.println("ActionAEM" + e.getMessage());
               e.printStackTrace(System.err);
               this.resetCurrentScreen();
            } finally {
               AEMGui.mIsActionListenerWorking = false;
            }
         }
      }

      private void resetCurrentScreen() {
         if (AEMGui.currentScreen == null) {
            AEMGui.this.nextScreen = AEMGui.this.previousScreen;
            this.switchScreensAndThreads();
         }
      }

      private void switchScreensAndThreads() {
         AbstractHardwareThread lnextThread = null;
         if (AEMGui.this.nextScreen.equals("MainScreen")) {
            lnextThread = new Main(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("ReturnMovieScreen")) {
            lnextThread = new DetectingDiscIn(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("IdentifyingMovieScreen")) {
            lnextThread = new IdentifyingDisc(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("ReturningMovieScreen")) {
            lnextThread = new ReturningMovie(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("SwipePaymentCardScreen")) {
            lnextThread = new SwipePaymentCard(this, AEMGui.this.tempBar, AEMGui.this.data);
         } else if (AEMGui.this.nextScreen.equals("AuthorizingPaymentScreen")) {
            lnextThread = new AuthorizingPaymentCard(this, AEMGui.this.tempBar, AEMGui.this.data);
         } else if (AEMGui.this.nextScreen.equals("DeliveringDVDScreen")) {
            lnextThread = new DeliveringDVD(this, AEMGui.this.tempBar, AEMGui.this.data);
         } else if (AEMGui.this.nextScreen.equals("RemoveDVDScreen")) {
            lnextThread = new RemoveDVD(this, AEMGui.this.tempBar, AEMGui.this.data);
         } else if (AEMGui.this.nextScreen.equals("UnableToRecognizeMovieScreen") || AEMGui.this.nextScreen.equals("DiscNotBelongScreen")) {
            lnextThread = new UnableToRecognizeMovie(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("InitializingAEMScreen")) {
            lnextThread = new InitializingAEM(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("PushingDiscAllTheWayScreen")) {
            lnextThread = new PushingDiscAllTheWay(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("ReturnErrorScreen")) {
            lnextThread = new ReturnError(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("HelpMainScreen")) {
            lnextThread = new HelpMain(this, AEMGui.this.tempBar);
         } else if (AEMGui.this.nextScreen.equals("UpdatingScreen")) {
            lnextThread = new Updating(this, AEMGui.this.tempBar);
         }

         AEMGui.this.setCurrentScreenAndThread(AEMGui.this.nextScreen, lnextThread);
         if (lnextThread != null) {
            AEMGui.currThread.start();
            this.msg = new StringBuffer("Created ").append(AEMGui.currThread).append(" ").append("(").append(AEMGui.currentScreen).append(")");
            AemFactory.getInstance();
            Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         }

         AEMGui.this.setCursor(AEMGui.this.handCursor);
      }
   }
}
