package net.dvdplay.screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;

public class AboutCompanyScreen extends AbstractContentBar {
   ActionListener aboutCompanyAction;
   JPanel descPanel;
   JLabel[] text;
   int descriptionId;
   int nameId;
   int oppositeNameId;
   JLabel franchiseLogo;
   JLabel dvdplayLogo;
   private JEditorPane jep;
   private StringBuffer path = null;
   private Locale currentLocale;
   private String originalPointBeforeAbout = null;

   public AboutCompanyScreen(String prevScreen, String currScreen, String data) {
      this.currentLocale = Aem.getLocale();
      this.path = new StringBuffer("file:\\").append("c:\\aem\\content\\html\\aboutpage\\").append(Aem.getLocaleId()).append("\\index.html");

      try {
         this.aboutCompanyAction = new AboutCompanyScreen.AboutCompanyAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.jep = new JEditorPane();
         this.jep.setEditable(false);
         this.jep.setBounds(50, 80, 900, 600);

         try {
            this.jep.setPage(this.path.toString());
         } catch (Exception var5) {
            this.msg = new StringBuffer("[").append("AboutCompanyScreen").append("]").append(var5.toString());
            Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
         }

         this.createBlackBottomBar(false, false, "", "", Aem.getString(1012), "Back", "", "");
         this.createTopBar(Aem.getString(1007), "", "", "", "", Aem.getString(1004), "StartOver");
         this.bottomBar.addActionListener(this.aboutCompanyAction);
         this.topBar.addActionListener(this.aboutCompanyAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.jep);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("AboutCompanyScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("AboutCompanyScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var6);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         if (!prevScreen.equals("AboutCompanyScreen") && !prevScreen.equals("TimeOutScreen")) {
            this.originalPointBeforeAbout = prevScreen;
         }

         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (this.currentLocale != Aem.getLocale()) {
            this.currentLocale = Aem.getLocale();
            this.path = new StringBuffer("file:\\").append("c:\\aem\\content\\html\\aboutpage\\").append(Aem.getLocaleId()).append("\\index.html");

            try {
               this.jep.setPage(this.path.toString());
            } catch (Exception var5) {
               this.msg = new StringBuffer("[").append("AboutCompanyScreen").append("]").append(var5.toString());
               Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString());
            }

            this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", Aem.getString(1012), "Back", "", "");
            this.topBar.setProperty(Aem.getString(1007), "", "", "", "", Aem.getString(1004), "StartOver");
         }

         this.msg = new StringBuffer("* ").append("AboutCompanyScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var6) {
         this.msg = new StringBuffer("[").append("AboutCompanyScreen").append("]").append(var6.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var6);
      }
   }

   public void addActionListener(ActionListener l) {
      this.listenerList
         .add(
            ActionListener.class,
            l
         );
      AbstractContentBar.mainAction = l;
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
   }

   public class AboutCompanyAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("AboutCompanyScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(AboutCompanyScreen.mainAction)) {
                  if (this.cmd[0].equals("StartOver")) {
                     if (Aem.needToReinitialize() && AboutCompanyScreen.aemContent.getPrevScreen().equals("ReturnThankYouScreen")) {
                        AboutCompanyScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "InitializingAEMScreen"));
                     } else {
                        AboutCompanyScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                     }
                  } else if (this.cmd[0].equals("Back")) {
                     AboutCompanyScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, AboutCompanyScreen.this.originalPointBeforeAbout));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "AboutCompanyScreen");
         } catch (Throwable var5) {
            Log.error(var5, "AboutCompanyScreen");
         }
      }
   }
}
