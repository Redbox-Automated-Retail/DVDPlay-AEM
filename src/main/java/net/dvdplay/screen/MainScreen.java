package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.aemcontroller.Tools;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.inventory.DiscIndex;
import net.dvdplay.inventory.LocaleIndexItem;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.media.PlayListManager;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.view.BottomBarModel;
import net.dvdplay.view.Utility;

public class MainScreen extends AbstractContentBar {
   JButton returnMovie;
   JButton currentMovie;
   JButton[] select;
   JPanel contentBar;
   JPanel arrowsPanel;
   PlayListManager plm;
   ActionListener mainScreenAction;
   JPanel trailerContent;
   static int hiddenClicked = 0;
   int numTitleTypes;
   int numLocales;
   String button1Text;
   String button2Text;
   String button3Text;
   String localeString2;
   String localeString3;
   ArrayList localeList = new ArrayList();
   int locationY = 240;
   private Timer hiddenTimer = new Timer(3000, new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
         MainScreen.hiddenClicked = 0;
      }
   });


   public MainScreen(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.reset();
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.mainScreenAction = new MainScreen.MainScreenAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("Black"));
         this.createHiddenButton();
         UIManager.getDefaults().put("Button.select", ScreenProperties.getColor("Orange"));
         this.createContent();
         this.bottomBar = new BottomBarModel(ScreenProperties.getColor("Red"), true, true, true, "", "", "", "", "", "");
         this.topBar = this.createTopBar();
         this.bottomBar.addActionListener(this.mainScreenAction);
         this.topBar.addActionListener(this.mainScreenAction);
         this.add(this.contentBar);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("MainScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("MainScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         AbstractContentBar.aemContent.reset();
         this.locationY = 240;
         this.numTitleTypes = Aem.createTitleTypeList();

         for (int i = 0; i < this.numTitleTypes; i++) {
            if (!Aem.isInventoryForTitleType(Aem.getTitleTypeIndexItem(i).getTitleTypeId())) {
               this.select[i].setVisible(false);
            } else {
               this.select[i].setBounds(800, this.locationY, 200, 70);
               this.select[i].setVisible(true);
               this.locationY += 80;
            }
         }

         this.returnMovie.setBounds(800, this.locationY, 200, 70);
         AbstractContentBar.timer.stop();
         this.msg = new StringBuffer("* ").append("MainScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("MainScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public boolean isTrailerAvailable() {
      return this.plm != null;
   }

   public void startMovie() {
      if (this.isTrailerAvailable()) {
         this.plm.restart();
      }
   }

   public void stopMovie() {
      if (this.isTrailerAvailable()) {
         this.plm.stopPlay();
      }
   }

   public void reset() {
      AbstractContentBar.aemContent.reset();
   }

   public void updateLocale(int lLocaleId) {
      Aem.setLocale(lLocaleId);
      this.numTitleTypes = Aem.createTitleTypeList();

      for (int i = 0; i < this.numTitleTypes; i++) {
         this.select[i].setText(Aem.getTitleTypeIndexItem(i).getTitleType());
      }

      this.returnMovie.setText(Aem.getString(1003));
      this.currentMovie.setText(Aem.getString(1005));
      this.bottomBar.setProperty(ScreenProperties.getColor("Red"), true, true, true, "", "", "", "", "", "");
      this.updateTopBar();
   }

   private void createContent() {
      try {
         this.trailerContent = new JPanel();
         this.contentBar = new JPanel(null);
         this.contentBar.setBackground(ScreenProperties.getColor("Black"));
         this.contentBar.setBounds(0, 60, 1024, 630);
         this.plm = new PlayListManager(720, 480, ScreenProperties.getInt("Video.Height"));
         if (this.plm.playNext()) {
            this.trailerContent.add(this.plm);
            this.trailerContent.setBackground(ScreenProperties.getColor("Black"));
         } else {
            this.plm = null;
            ImageIcon icon = ScreenProperties.getImage("Default.Trailer.NotFound");
            Image oldIcon = icon.getImage();
            Image newImage = oldIcon.getScaledInstance(720, 480, 0);
            JLabel trailerNotFound = new JLabel(new ImageIcon(newImage));
            icon.setImageObserver(trailerNotFound);
            trailerNotFound.setBounds(10, 75, 720, 480);
            this.contentBar.add(trailerNotFound);
         }

         Icon tempIcon = ScreenProperties.getImage("RedRightArrowonBlack.Logo.Icon");
         Icon pressedIcon = ScreenProperties.getImage("OrangeArrowRight.Icon");
         Border curvedBorder = new LineBorder(ScreenProperties.getColor("White"));
         Icon rentMeIcon = ScreenProperties.getImage("BlackArrowOrangeLeft.Icon");
         if (this.plm != null) {
            this.currentMovie = this.plm.createRentMeButton();
            this.currentMovie.setIcon(rentMeIcon);
            this.currentMovie.setBorder(curvedBorder);
            this.currentMovie.setHorizontalTextPosition(4);
            this.currentMovie.setFocusPainted(false);
            this.currentMovie.setForeground(ScreenProperties.getColor("Black"));
            this.currentMovie.setBounds(800, 160, 200, 70);
            this.currentMovie.addActionListener(this.mainScreenAction);
            this.contentBar.add(this.currentMovie);
         }

         int locationY = 240;
         int numTitleTypes = Aem.createTitleTypeList();
         this.select = new JButton[numTitleTypes];

         for (int i = 0; i < numTitleTypes; i++) {
            this.select[i] = Utility.createTopBarButton(
               Aem.getString(1002) + " " + Aem.getTitleTypeIndexItem(i).getTitleTypeSingular(),
               tempIcon,
               pressedIcon,
               ScreenProperties.getColor("Black"),
               curvedBorder,
               ScreenProperties.getFont("CenterBarButton"),
               ScreenProperties.getColor("White")
            );
            this.select[i].setBounds(800, locationY, 200, 70);
            this.select[i].setActionCommand("Select " + Integer.toString(Aem.getTitleTypeIndexItem(i).getTitleTypeId()));
            this.select[i].addActionListener(this.mainScreenAction);
            this.select[i].setFocusPainted(false);
            this.contentBar.add(this.select[i]);
            locationY += 80;
         }

         this.returnMovie = Utility.createTopBarButton(
            Aem.getString(1003),
            tempIcon,
            pressedIcon,
            ScreenProperties.getColor("Black"),
            curvedBorder,
            ScreenProperties.getFont("CenterBarButton"),
            ScreenProperties.getColor("White")
         );
         this.returnMovie.setBounds(800, locationY, 200, 70);
         this.returnMovie.setActionCommand("ReturnMovie");
         this.returnMovie.addActionListener(this.mainScreenAction);
         this.returnMovie.setFocusPainted(false);
         this.contentBar.add(this.returnMovie);
         this.contentBar.add(this.trailerContent);
      } catch (Exception var8) {
         Log.warning(var8, "[MainScreen] createContent ");
      }
   }

   protected void updateTopBar() {
      this.button1Text = "";
      this.button2Text = "";
      this.button3Text = "";
      this.localeString2 = "";
      this.localeString3 = "";
      this.numLocales = Aem.getLocaleIndexSize();
      this.button1Text = Aem.getString(1001);
      this.localeList.clear();

      for (int i = 0; i < this.numLocales; i++) {
         if (Aem.getLocalIndexItem(i).getLocaleId() != Aem.getLocaleId()) {
            this.localeList.add(Aem.getLocalIndexItem(i));
         }
      }

      if (this.numLocales > 1) {
         this.button2Text = ((LocaleIndexItem)this.localeList.get(0)).getDisplayLanguage();
         this.localeString2 = Integer.toString(((LocaleIndexItem)this.localeList.get(0)).getLocaleId());
         if (this.numLocales > 2) {
            this.button3Text = ((LocaleIndexItem)this.localeList.get(1)).getDisplayLanguage();
            this.localeString3 = Integer.toString(((LocaleIndexItem)this.localeList.get(1)).getLocaleId());
         }
      }

      switch (this.localeList.size()) {
         case 1:
            this.topBar.setProperty("", "", "", this.button1Text, "Help", this.button2Text, "Language " + this.localeString2);
            break;
         case 2:
            this.topBar
               .setProperty(
                  "", this.button1Text, "Help", this.button2Text, "Language " + this.localeString2, this.button3Text, "Language " + this.localeString3
               );
            break;
         default:
            this.topBar.setProperty("", "", "", "", "", this.button1Text, "Help");
      }
   }

   private void createHiddenButton() {
      Icon grayArrow = ScreenProperties.getImage("GrayArrowStartScreen");
      JLabel arrowsGif1 = new JLabel(grayArrow);
      arrowsGif1.setBounds(0, 0, 23, 20);
      JLabel arrowsGif2 = new JLabel(grayArrow);
      arrowsGif2.setBounds(24, 0, 23, 20);
      JLabel arrowsGif3 = new JLabel(grayArrow);
      arrowsGif3.setBounds(48, 0, 23, 20);
      arrowsGif2.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
            MainScreen.hiddenClicked++;
            if (MainScreen.hiddenClicked == 3) {
               new Tools(AbstractContentBar.mainAction);
            }
            if (MainScreen.hiddenClicked == 1) {
               hiddenTimer.start();
            }
         }
      });
      arrowsGif2.setHorizontalAlignment(0);
      arrowsGif2.setVerticalAlignment(0);
      arrowsGif2.setBackground(ScreenProperties.getColor("Red"));
      this.arrowsPanel = new JPanel(null);
      this.arrowsPanel.setBounds(25, 20, 70, 20);
      this.arrowsPanel.setBackground(ScreenProperties.getColor("Red"));
      this.arrowsPanel.add(arrowsGif1);
      this.arrowsPanel.add(arrowsGif2);
      this.arrowsPanel.add(arrowsGif3);
      this.add(this.arrowsPanel);
      JButton toolsHiddenButton = new JButton();
      toolsHiddenButton.setBorder(null);
      toolsHiddenButton.setBackground(Color.RED);
      toolsHiddenButton.setBounds(1000, 501, 0, 0);
      toolsHiddenButton.setMnemonic(83);
      toolsHiddenButton.addActionListener(new MainScreen.HiddenAction());
      this.add(toolsHiddenButton);
      JButton exitHiddenButton = new JButton();
      exitHiddenButton.setBorder(null);
      exitHiddenButton.setBackground(Color.RED);
      exitHiddenButton.setBounds(1000, 500, 0, 0);
      exitHiddenButton.setMnemonic(88);
      exitHiddenButton.addActionListener(new MainScreen.ExitHiddenAction());
      this.add(exitHiddenButton);
   }

   public void addActionListener(ActionListener l) {
      AbstractContentBar.mainAction = l;
      this.bottomBar.addActionListener(l);
      this.topBar.addActionListener(l);
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
   }

   private class ExitHiddenAction implements ActionListener {
      private ExitHiddenAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         MainScreen.hiddenClicked++;
         if (MainScreen.hiddenClicked == 3) {
            Aem.logSummaryMessage("Exiting Aem (Alt-x) x 3");
            AemFactory.getInstance().exitApp(1);
            System.exit(0);
         }

         if (MainScreen.hiddenClicked == 1) {
            MainScreen.this.hiddenTimer.start();
         }
      }
   }

   private class HiddenAction implements ActionListener {
      private HiddenAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         MainScreen.hiddenClicked++;
         if (MainScreen.hiddenClicked == 3) {
            new Tools(MainScreen.mainAction);
         }

         if (MainScreen.hiddenClicked == 1) {
            MainScreen.this.hiddenTimer.start();
         }
      }
   }

   public class MainScreenAction extends BaseActionListener {
      int currentTitleTypeID = 0;

      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("MainScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(MainScreen.mainAction)) {
                  if (this.cmd[0].equals("Select")) {
                     if (this.cmd[1].compareTo(Integer.toString(3)) == 0) {
                        Aem.setTitleTypeId(3);
                     } else if (this.cmd[1].compareTo(Integer.toString(1)) == 0) {
                        Aem.setTitleTypeId(1);
                     }

                     MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MovieSelectionScreen"));
                  } else if (this.cmd[0].equals("ReturnMovie")) {
                     if (Aem.isReturnDisabled()) {
                        MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4042,MainScreen"));
                     } else if (Aem.isCarouselFull()) {
                        MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4021,MainScreen"));
                     } else {
                        MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ReturnMovieScreen"));
                     }
                  } else if (this.cmd[0].equals("CurrentMovie")) {
                     try {
                        Aem.getDiscIndex();
                        this.currentTitleTypeID = DiscIndex.getDiscIndexItemByTitleDetailId(Integer.parseInt(this.cmd[1])).getTitleTypeId();
                        MainScreen.aemContent.setCurrentDvdDescriptionDiscId(Integer.parseInt(this.cmd[1]));
                        Aem.setTitleTypeId(this.currentTitleTypeID);
                        switch (this.currentTitleTypeID) {
                           case 1:
                              MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "DvdDescriptionScreen"));
                              break;
                           case 3:
                              MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "GameDescriptionScreen"));
                        }
                     } catch (Exception var4) {
                        MainScreen.this.currentMovie.setVisible(false);
                        MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4010,MainScreen"));
                     }
                  } else if (this.cmd[0].equals("Help")) {
                     MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "Help"));
                  } else if (this.cmd[0].equals("Language")) {
                     Aem.setLocale(Integer.parseInt(this.cmd[1]));
                     MainScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "Main"));
                  }
               }
            }
         } catch (Exception var5) {
            Log.warning(var5, "MainScreen");
         } catch (Throwable var6) {
            Log.error(var6, "MainScreen");
         }
      }
   }
}
