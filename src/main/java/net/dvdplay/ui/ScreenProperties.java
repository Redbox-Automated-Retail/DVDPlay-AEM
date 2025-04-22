package net.dvdplay.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import net.dvdplay.logger.Log;

public class ScreenProperties {
   static Hashtable<String, Integer> ht;
   static Hashtable color;
   static Hashtable font;
   static Hashtable img;
   static Hashtable icon;
   Image image = null;
   ImageIcon imgIcon;
   byte[] imageBytes = null;
   Toolkit toolkit = Toolkit.getDefaultToolkit();

   public ScreenProperties() {
      this.processImage();
      this.processHt();
      processFont();
      processColor();
   }

   public static int getInt(String key) {
      if (ht.containsKey(key)) {
         Integer temp = (Integer)ht.get(key);
         return temp;
      } else {
         return -9999;
      }
   }

   public static String getString(String key) {
      return ht.containsKey(key) ? String.valueOf(ht.get(key)) : null;
   }

   public static Font getFont(String key) {
      return font.containsKey(key) ? (Font)font.get(key) : null;
   }

   public static Color getColor(String key) {
      return color.containsKey(key) ? (Color)color.get(key) : null;
   }

   public static ImageIcon getImage(String key) {
      try {
         if (icon.containsKey(key)) {
            return (ImageIcon)icon.get(key);
         }
      } catch (Exception var2) {
         Log.warning(var2, "[ScreenProperties] Error Getting Image");
      }

      return null;
   }

   private ImageIcon preFetchImage(String fileName) {
      this.image = null;
      this.imageBytes = null;

      try {
         if (fileName == null) {
            throw new Exception("Missing image filename");
         } else {
            if (this.toolkit == null) {
               Log.warning("toolkit is null!");
            }

            URL url = this.getClass().getResource("/img/" + fileName);
            if (url != null) {
               this.image = this.toolkit.getImage(url);
               this.imgIcon = new ImageIcon(this.image);
               return this.imgIcon;
            } else {
               throw new Exception("Image File " + fileName + " Not Found");
            }
         }
      } catch (Exception var3) {
         Log.warning("[ScreenProperties] " + var3.toString());
         return null;
      }
   }

   private void processImage() {
      img = new Hashtable();
      String path = "";
      img.put("BlackArrowOrangeLeft.Icon", new String(path + "blackArrowOrangeLeft.gif"));
      img.put("RedArrowWhite.Logo.Icon", new String(path + "arrowRedonWhite.gif"));
      img.put("GrayArrowStartScreen", new String(path + "grayArrowStartScreen.gif"));
      img.put("OrangeArrowDown.Icon", new String(path + "orangeArrowDown.gif"));
      img.put("OrangeArrowLeft.Icon", new String(path + "orangeArrowLeft.gif"));
      img.put("OrangeArrowRight.Icon", new String(path + "orangeArrowRight.gif"));
      img.put("OrangeArrowUp.Icon", new String(path + "orangeArrowUp.gif"));
      img.put("WhiteArrowonRed.Logo.Icon", new String(path + "arrowWhite.gif"));
      img.put("WhiteArrowonBlack.Logo.Icon", new String(path + "whiteLeftArrowOnRed.gif"));
      img.put("RedDownArrowonWhite.Logo.Icon", new String(path + "downArrowRedOnWhite.gif"));
      img.put("RedUpArrowonWhite.Logo.Icon", new String(path + "upArrowRedOnWhite.gif"));
      img.put("RedLeftArrowonBlack.Logo.Icon", new String(path + "leftArrowRedOnBlack.gif"));
      img.put("RedRightArrowonBlack.Logo.Icon", new String(path + "rightArrowRedOnBlack.gif"));
      img.put("DownRedArrow.Logo.Icon", new String(path + "arrowTrans.gif"));
      img.put("RedArrowOnBlack.Logo.Icon", new String(path + "arrowRedOnBlack.gif"));
      img.put("LeftRedArrow.Logo.Icon", new String(path + "arrowTrans.gif"));
      img.put("ReturnMovie.Animated.Gif", new String(path + "ReturnDVD.gif"));
      img.put("ProgressBar.Animated.Gif", new String(path + "arrows.gif"));
      img.put("SwipeCard.Animated.Gif", new String(path + "swipeCreditCard.gif"));
      img.put("RemoveDVD.Animated.Gif", new String(path + "takeDVD.gif"));
      img.put("UnableToRecognizeMovie.Animated.Gif", new String(path + "ReturnDVD.gif"));
      img.put("Help.Icon.Question", new String(path + "question.gif"));
      img.put("Help.Icon.Answer", new String(path + "answer.gif"));
      img.put("Cart.Icon.Error", new String(path + "oops.gif"));
      img.put("Cart.Icon.RemovePromoCode", new String(path + "promoCode.gif"));
      img.put("Error.Icon.Ops", new String(path + "oops.gif"));
      img.put("TimeOut.Icon", new String(path + "clock.gif"));
      img.put("Hidden.Pressed.dvdplay", new String(path + "dvdPlayPressed.jpg"));
      img.put("Hidden.Pressed.mcdonald", new String(path + "mcdonaldPressed.gif"));
      img.put("Status.Red", new String(path + "redCircle.gif"));
      img.put("Status.Green", new String(path + "greenCircle.gif"));
      img.put("Status.White", new String(path + "whiteCircle.gif"));
      img.put("Status.Yellow", new String(path + "yellowCircle.gif"));
      img.put("Status.Blue", new String(path + "blueCircle.gif"));
      img.put("Cart.Icon.PromoCode", new String(path + "promoCode.gif"));
      img.put("Help.Icon.QuestionPressed", new String(path + "questionPressed.gif"));
      img.put("Cart.Icon.Trash", new String(path + "removeMovie2.gif"));
      img.put("Cart.Icon", new String(path + "cartOnRed.gif"));
      img.put("Cart.Pressed.Icon", new String(path + "cartOnOrange.gif"));
      img.put("DvdPlay.Logo.IconOnRed.dvdplay", new String(path + "logoRed.gif"));
      img.put("DvdPlay.Logo.IconOnBlack.dvdplay", new String(path + "logoBlack.gif"));
      img.put("DvdPlay.Logo.IconOnRed.mcdonald", new String(path + "mcdonaldlogoRed.gif"));
      img.put("DvdPlay.Logo.IconOnBlack.mcdonald", new String(path + "mcdonaldlogoBlack.gif"));
      img.put("Enter.Icon.PromoCode", new String(path + "EnterPromoCode.gif"));
      img.put("PromoCode.Logo.IconOnWhite", new String(path + "promoCodeWhite.gif"));
      img.put("PromoCode.Logo.IconOnRed", new String(path + "PromoCodeMainScreen.gif"));
      img.put("PromoCode.Logo.IconOnBlack", new String(path + "promoCodeBlack.gif"));
      img.put("Default.BigPoster.NotFound", new String(path + "PosterNotFound.jpg"));
      img.put("Default.SmallPoster.NotFound", new String(path + "PosterNotFoundSmall.jpg"));
      img.put("Default.Trailer.NotFound", new String(path + "TrailerNotFound.gif"));
      img.put("SwipeCard.Secure.Gif", new String(path + "SecurePayment.jpg"));

      try {
         icon = new Hashtable();
         String key = "";
         String value = "";
         Enumeration en = img.keys();

         while (en.hasMoreElements()) {
            try {
               key = (String)en.nextElement();
               value = (String)img.get(key);
               icon.put(key, this.preFetchImage(value));
            } catch (Exception var6) {
               Log.warning("[ScreenProperties] Error Preloading Image " + value);
            }
         }
      } catch (Exception var7) {
         Log.warning(var7, "[ScreenProperties] Error Preloading Image");
      }
   }

   private void processHt() {
      ht = new Hashtable<>();
      ht.put("Video.Height", 570);
      ht.put("Timeout", 2000);
      ht.put("PromoCode.Logo.LocationX", 840);
      ht.put("PromoCode.Logo.LocationY", 2);
      ht.put("PromoCode.Logo.Width", 157);
      ht.put("PromoCode.Logo.Height", 70);
      ht.put("DvdPlay.Logo.LocationX", 30);
      ht.put("DvdPlay.Logo.LocationY", 5);
      ht.put("DvdPlay.Logo.Width", 153);
      ht.put("DvdPlay.Logo.Height", 66);
      ht.put("PromoCode.Logo.LocationX", 840);
      ht.put("PromoCode.Logo.LocationY", 5);
      ht.put("PromoCode.Logo.Width", 157);
      ht.put("PromoCode.Logo.Height", 70);
      ht.put("Button.Home.LocationX", 800);
      ht.put("Button.Home.LocationY", 50);
      ht.put("Button.Home.Width", 170);
      ht.put("Button.Home.Height", 50);
      ht.put("Button.Movies.LocationX", 800);
      ht.put("Button.Movies.LocationY", 650);
      ht.put("Button.Movies.Width", 170);
      ht.put("Button.Movies.Height", 50);
      ht.put("Button.Languages.LocationX", 630);
      ht.put("Button.Languages.LocationY", 650);
      ht.put("Button.Languages.Width", 170);
      ht.put("Button.Languages.Height", 50);
      ht.put("Button.BottomBar.LocationX", 10);
      ht.put("Button.BottomBar.LocationY", 650);
      ht.put("Button.BottomBar.Width", 1260);
      ht.put("Button.BottomBar.Height", 120);
      ht.put("Image.Poster.Width", 120);
      ht.put("Image.Poster.Height", 180);
   }

   private static void processFont() {
      font = new Hashtable();
      font.put("TopBarButton", new Font("Arial", 0, 18));
      font.put("TopBarTitle", new Font("Arial", 0, 18));
      font.put("BottomBarButton", new Font("Arial", 0, 18));
      font.put("CenterBarButton", new Font("Arial", 1, 16));
      font.put("ComingSoon", new Font("Arial", 1, 16));
      font.put("DvdDescriptionTitle", new Font("Arial", 1, 20));
      font.put("DvdDecriptionDescription", new Font("Arial", 0, 10));
      font.put("DvdDescriptionSubject", new Font("Arial", 1, 14));
      font.put("MovieSelectionTitle", new Font("Arial", 1, 12));
      font.put("MovieSelectionStatus", new Font("Arial", 1, 12));
      font.put("CartScreenTitle", new Font("Arial", 1, 16));
      font.put("CartScreenDescription", new Font("Arial", 1, 15));
      font.put("CustomerKeyboard", new Font("Arial", 1, 30));
      font.put("ZipCodeEntry", new Font("Arial", 1, 10));
      font.put("AdminKeyboard", new Font("Arial", 1, 14));
      font.put("CustomerKeyboardEntry", new Font("Arial", 1, 23));
      font.put("AddEmailLabel", new Font("Arial", 1, 22));
      font.put("AddEmailPrivacy", new Font("Arial", 1, 14));
      font.put("CartScreenNote", new Font("Arial", 1, 13));
      font.put("HelpQuestionFont", new Font("Arial", 1, 20));
      font.put("HelpAnswerQuestion", new Font("Arial", 1, 20));
      font.put("HelpAnswerAnswer", new Font("Arial", 0, 19));
      font.put("ReturnMovieTitle", new Font("Arial", 1, 20));
      font.put("ReturnMovieText", new Font("Arial", 0, 18));
      font.put("ReturnMovieThankYou", new Font("Arial", 1, 26));
      font.put("MainScreenRentNow", new Font("Arial", 1, 14));
      font.put("DVDDescriptionStatus", new Font("Arial", 1, 18));
      font.put("SwipePaymentText1", new Font("Arial", 1, 22));
      font.put("SwipePaymentText2", new Font("Arial", 1, 18));
      font.put("SwipePaymentText3", new Font("Arial", 1, 16));
      font.put("SwipePaymentText4", new Font("Arial", 0, 16));
      font.put("SwipePaymentText5", new Font("Arial", 1, 16));
      font.put("PaymentApproved", new Font("Arial", 1, 20));
      font.put("PaymentApprovedTitle", new Font("Arial", 1, 22));
      font.put("DeliveringDVD", new Font("Arial", 1, 30));
      font.put("RemoveDVDText", new Font("Arial", 1, 20));
      font.put("ErrorWarningScreenTitle", new Font("Arial", 1, 23));
      font.put("ErrorWarningScreen", new Font("Arial", 1, 18));
      font.put("TimeOutScreen", new Font("Arial", 1, 20));
      font.put("RentalAgreementTitle", new Font("Arial", 1, 14));
      font.put("RentalAgreementScreen", new Font("Arial", 1, 12));
      font.put("PromoCodeDescriptionScreen", new Font("Arial", 1, 18));
      font.put("MaximumDiscExceededScreen", new Font("Arial", 1, 16));
      font.put("PollScreenHeader", new Font("Arial", 0, 18));
      font.put("PollScreenQuestion", new Font("Arial", 1, 22));
      font.put("PollScreenAnswer", new Font("Arial", 0, 20));
      font.put("PollScreenButton", new Font("Arial", 1, 16));
      font.put("UpdatingScreenHeader", new Font("Arial", 1, 24));
      font.put("UpdatingScreenMessage", new Font("Arial", 1, 20));
   }

   private static void processColor() {
      color = new Hashtable();
      color.put("Red", new Color(204, 0, 0));
      color.put("Orange", new Color(255, 153, 0));
      color.put("ComingSoon", new Color(255, 204, 0));
      color.put("Black", new Color(0, 0, 0));
      color.put("White", new Color(255, 255, 255));
      color.put("Green", Color.GREEN);
      color.put("Gray", Color.GRAY);
   }
}
