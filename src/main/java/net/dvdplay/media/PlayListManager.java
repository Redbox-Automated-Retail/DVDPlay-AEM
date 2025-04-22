package net.dvdplay.media;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.Beans;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.bean.playerbean.MediaPlayer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.inventory.PlayListItem;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.view.Utility;

public class PlayListManager extends JPanel implements ControllerListener {
   JPanel jp;
   JButton rentMe = new JButton();
   MediaPlayer mplayer;
   Component visual = null;
   Iterator it;
   String currentMovies;
   PlayListItem currentPlayListItem;
   boolean noTrailer = false;
   ImageIcon icon;
   Image oldIcon;
   Image newImage;
   String mMediaFile = null;
   URL mUrl = null;
   final String fileStr = "file:";
   int displayHeight = 0;
   int displayWidth = 0;
   int videoHeight = 0;
   int videoWidth = 0;

   public PlayListManager(int _displayWidth, int _displayHeight, int _videoHeight) {
      Manager.setHint(3, true);

      try {
         ClassLoader cl = this.getClass().getClassLoader();
         this.mplayer = (MediaPlayer)Beans.instantiate(cl, "javax.media.bean.playerbean.MediaPlayer");
      } catch (ClassNotFoundException e) {
         System.out.println("Class Not Found Exception : " + e);
      } catch (IOException e) {
         System.out.println("IO Exception : " + e);
      }

      this.displayHeight = _displayHeight;
      this.displayWidth = _displayWidth;
      this.videoHeight = _videoHeight;
      this.setBackground(Color.BLACK);
   }

   public boolean playNext() {
      try {
         if ((this.currentPlayListItem = Aem.getNextMediaPlayListItem()) == null) {
            this.noTrailer = true;
            return false;
         }

         AEMGui.screenMonitor.stop();
         AEMGui.screenMonitor.start();
         this.currentMovies = this.currentPlayListItem.getFilePath();
         Aem.logSummaryMessage("[TRAILER] " + this.currentMovies);
         this.openFile(this.currentMovies);
         this.noTrailer = false;
         switch (this.currentPlayListItem.getButtonMode()) {
            case 0:
               this.rentMe.setVisible(false);
               break;
            case 1:
               this.rentMe.setText(Aem.getString(1005));
               this.rentMe.setActionCommand("CurrentMovie " + this.getCurrentMovieId());
               this.rentMe.setVisible(true);
               this.rentMe.setFocusable(true);
               break;
            case 2:
               this.rentMe.setText(Aem.getString(1020));
               this.rentMe.setActionCommand("");
               this.rentMe.setFocusable(false);
               this.rentMe.setVisible(true);
               break;
            default:
               this.rentMe.setVisible(false);
         }

         this.rentMe.repaint(800, 160, 200, 70);
         Aem.logSummaryMessage("[RENTABLE] " + this.currentMovies + ":" + this.currentPlayListItem.isRentable());
         this.jp.setBackground(Color.BLACK);
         this.jp.setVisible(true);
      } catch (Exception e) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[PlayListManager] " + e, e);
      }

      return true;
   }

   public boolean isRentable() {
      Aem.logSummaryMessage("[RENTABLE] " + this.currentMovies + ":" + this.currentPlayListItem.isRentable());
      return !this.noTrailer && this.currentPlayListItem.isRentable();
   }

   public int getCurrentMovieId() {
      return this.currentPlayListItem.getMediaId();
   }

   public JButton createRentMeButton() {
      if (!this.noTrailer) {
         ImageIcon pressedIcon = ScreenProperties.getImage("OrangeArrowLeft.Icon");
         this.rentMe = Utility.createTopBarButton(
            "", null, pressedIcon, ScreenProperties.getColor("ComingSoon"), null, ScreenProperties.getFont("ComingSoon"), ScreenProperties.getColor("Black")
         );
         switch (this.currentPlayListItem.getButtonMode()) {
            case 0:
               this.rentMe.setVisible(false);
               break;
            case 1:
               this.rentMe.setText(Aem.getString(1005));
               this.rentMe.setActionCommand("CurrentMovie " + this.getCurrentMovieId());
               this.rentMe.setVisible(true);
               break;
            case 2:
               this.rentMe.setText(Aem.getString(1020));
               this.rentMe.setActionCommand("");
               this.rentMe.setVisible(true);
               this.rentMe.setFocusable(false);
               break;
            default:
               this.rentMe.setVisible(false);
         }
      } else {
         this.rentMe.setVisible(false);
      }

      return this.rentMe;
   }

   public void openFile(String filename) {
      this.mMediaFile = "file:" + filename;
      this.mUrl = null;

      try {
         try {
            if ((this.mUrl = new URL(this.mMediaFile)) == null) {
               Log.error("Can't build URL for " + this.mMediaFile);
               Aem.logDetailMessage(DvdplayLevel.ERROR, "Can't build URL for " + this.mMediaFile);
               return;
            }

            this.mplayer.setMediaLocation(this.mUrl.toString());
         } catch (MalformedURLException var4) {
            Log.error(var4, "Malformed URL Exception:");
         } catch (IOException var5) {
            Log.error(var5, "IO Exception:");
         }

         if (this.mplayer != null) {
            if (this.jp != null) {
               this.remove(this.jp);
            }

            this.jp = new JPanel();
            this.jp.setLayout(null);
            this.jp.setBackground(ScreenProperties.getColor("Black"));
            this.mplayer.addControllerListener(this);
            this.mplayer.realize();
            this.add(this.jp);
         }
      } catch (Exception var6) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[PlayListManager] " + var6.toString(), var6);
      }
   }

   public void stopPlay() {
      this.mplayer.stop();
      Log.info("PlayListManager.stopPlay Stopped");
   }

   public void restart() {
      if (!this.noTrailer) {
         this.mplayer.start();
         Log.info("PlayListManager.restart Started");
      }

      this.setVisible(true);
   }

   public void controllerUpdate(ControllerEvent ce) {
      try {
         new BigDecimal(0.0);
         if (ce instanceof RealizeCompleteEvent) {
            this.mplayer.prefetch();
         } else if (ce instanceof PrefetchCompleteEvent) {
            if ((this.visual = this.mplayer.getVisualComponent()) != null) {
               Dimension size = this.visual.getPreferredSize();
               BigDecimal bdWidth = new BigDecimal((double)size.width);
               BigDecimal bdHeight = new BigDecimal((double)size.height);
               BigDecimal e = bdWidth.divide(bdHeight, 2, 4);
               this.videoHeight = ScreenProperties.getInt("Video.Height");
               this.videoWidth = Math.round(e.floatValue() * this.videoHeight);
               if (this.videoWidth > 764) {
                  this.videoWidth = 764;
                  this.videoHeight = Math.round(this.videoWidth / e.floatValue());
               }

               int locationX = (824 - this.videoWidth) / 2;
               int locationY = (630 - this.videoHeight) / 2;
               Aem.logDetailMessage(
                  DvdplayLevel.FINER,
                  "[PlayListManager] Calculated Video Size : " + this.videoWidth + " x " + this.videoHeight + " Location : " + locationX + "," + locationY
               );
               this.visual.setBounds(locationX, locationY, this.videoWidth, this.videoHeight);
               Aem.logDetailMessage(
                  DvdplayLevel.FINER,
                  "[PlayListManager] Actual Video Size : "
                     + this.visual.getSize().getWidth()
                     + " x "
                     + this.visual.getSize().getHeight()
                     + " Location : "
                     + this.visual.getX()
                     + ","
                     + this.visual.getY()
               );
               this.visual.repaint(locationX, locationY, this.videoWidth, this.videoHeight);
               this.jp.add(this.visual);
            }

            this.jp.repaint();
            this.jp.validate();
            if (!AEMGui.isActionListenerWorking() && AEMGui.isCurrentScreen("MainScreen")) {
               this.mplayer.start();
               Log.info("PlayListManager.controllerUpdate started");
            }
         } else if (ce instanceof EndOfMediaEvent) {
            this.mplayer.stop();
            Log.info("PlayListManager.controllerUpdate stopped");
            this.playNext();
         }
      } catch (Exception var8) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "[PlayListManager] " + var8.toString(), var8);
      }
   }

   public int getYLocation() {
      return (this.displayHeight - this.videoHeight) / 2;
   }
}
