package net.dvdplay.base;

import java.awt.Button;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.LocaleIndexItem;
import net.dvdplay.logger.Log;
import net.dvdplay.models.AEMContent;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.view.BottomBarModel;
import net.dvdplay.view.TopBarModel;

public abstract class AbstractContentBar extends JPanel {
   protected ScreenProperties sProperty = new ScreenProperties();
   protected TopBarModel topBar;
   protected BottomBarModel bottomBar;
   protected JPanel centerBar;
   protected static ActionListener mainAction;
   protected Button popUpLeftButton;
   protected Button popUpRightButton;
   protected Panel popUp;
   protected static AEMContent aemContent;
   public String[] cmd;
   public StringBuffer msg;
   protected Locale currentLocale;
   protected static Timer timer = new Timer(30000, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
         if (!AbstractContentBar.aemContent.getCurrentScreen().equals(DvdplayBase.MAIN_SCREEN)) {
            AbstractContentBar.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.TIME_OUT_SCREEN));
            AbstractContentBar.timer.stop();
         }
      }
   });


   public AbstractContentBar() throws DvdplayException {
      try {
         if (aemContent == null) {
            aemContent = new AEMContent();
         }
      } catch (ExceptionInInitializerError var3) {
         Log.warning(var3, "[AbstractContentBar] ");
         System.err.println("[AbstractContentBar] " + var3.toString());
         var3.printStackTrace(System.err);
         throw new DvdplayException("AEM fails");
      } catch (Exception var4) {
         System.out.println("Error " + var4.toString());
         var4.printStackTrace(System.out);
      }
   }

   protected TopBarModel createTopBar() {
      String button1Text = "";
      String button2Text = "";
      String button3Text = "";
      String localeString2 = "";
      String localeString3 = "";
      int numLocales = Aem.getLocaleIndexSize();
      button1Text = Aem.getString(1001);
      ArrayList localeList = new ArrayList();

      for (int i = 0; i < numLocales; i++) {
         if (Aem.getLocalIndexItem(i).getLocaleId() != Aem.getLocaleId()) {
            localeList.add(Aem.getLocalIndexItem(i));
         }
      }

      if (numLocales > 1) {
         button2Text = ((LocaleIndexItem)localeList.get(0)).getDisplayLanguage();
         localeString2 = Integer.toString(((LocaleIndexItem)localeList.get(0)).getLocaleId());
         if (numLocales > 2) {
            button3Text = ((LocaleIndexItem)localeList.get(1)).getDisplayLanguage();
            localeString3 = Integer.toString(((LocaleIndexItem)localeList.get(1)).getLocaleId());
         }
      }

      if (this.topBar == null) {
         if (numLocales > 2) {
            this.topBar = new TopBarModel("", button1Text, "Help", button2Text, "Language " + localeString2, button3Text, "Language " + localeString3);
         } else if (numLocales > 1) {
            this.topBar = new TopBarModel("", "", "", button1Text, "Help", button2Text, "Language " + localeString2);
         } else {
            this.topBar = new TopBarModel("", "", "", "", "", button1Text, "Help");
         }
      } else if (numLocales > 2) {
         this.topBar.setProperty("", button1Text, "Help", button2Text, "Language " + localeString2, button3Text, "Language " + localeString3);
      } else if (numLocales > 1) {
         this.topBar.setProperty("", "", "", button1Text, "Help", button2Text, "Language " + localeString2);
      } else {
         this.topBar.setProperty("", "", "", "", "", button1Text, "Help");
      }

      return this.topBar;
   }

   protected void createTopBar(String title, String left, String leftAction, String center, String centerAction, String right, String rightAction) {
      this.topBar = new TopBarModel(title, left, left, center, centerAction, right, rightAction);
   }

   protected void createBlackBottomBar(
      boolean isLogoButton,
      boolean displayPromoCode,
      String leftButton,
      String leftButtonActionString,
      String centerButton,
      String centerButtonActionString,
      String rightButton,
      String rightButtonActionString
   ) {
      this.bottomBar = new BottomBarModel(
         ScreenProperties.getColor("Black"),
         true,
         isLogoButton,
         displayPromoCode,
         leftButton,
         leftButtonActionString,
         centerButton,
         centerButtonActionString,
         rightButton,
         rightButtonActionString
      );
   }

   protected JPanel createBorder(Color borderColor, int x, int y, int width, int height) {
      JPanel emptyBorder = new JPanel();
      emptyBorder.setBorder(new LineBorder(borderColor));
      emptyBorder.setBounds(x, y, width, height);
      emptyBorder.setBackground(ScreenProperties.getColor("White"));
      emptyBorder.setLayout(null);
      return emptyBorder;
   }

   public void update(String prevScreen, String currScreen, String data) {
   }

   protected void setCurrentLocale(Locale lLocale) {
      this.currentLocale = lLocale;
   }

   protected boolean isCurrentLocale() {
      return this.currentLocale == Aem.getLocale();
   }

   public void addActionListener(ActionListener l) {
      this.bottomBar.addActionListener(l);
      this.topBar.addActionListener(l);
   }

   public void removeActionListener(ActionListener l) {
      this.bottomBar.removeActionListener(l);
      this.topBar.removeActionListener(l);
   }
}
