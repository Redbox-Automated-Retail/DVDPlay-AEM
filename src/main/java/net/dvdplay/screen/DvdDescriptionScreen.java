package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.DiscIndex;
import net.dvdplay.inventory.PricingItem;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.models.AEMContent;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class DvdDescriptionScreen extends AbstractContentBar {
   JLabel poster;
   JLabel title;
   JLabel category;
   JLabel duration;
   JLabel starring;
   JLabel director;
   JLabel rating;
   JLabel releaseYear;
   JLabel rentalPrice;
   JLabel usedPrice;
   JLabel starringSubject;
   JLabel directorSubject;
   JLabel ratingSubject;
   JLabel releaseYearSubject;
   JLabel rentalPriceSubject;
   JLabel usedPriceSubject;
   JLabel subtitledInSubject;
   JLabel subtitledIn;
   JLabel dubbedInSubject;
   JLabel dubbedIn;
   JLabel title2;
   JLabel dueBack;
   JLabel dueBackSubject;
   JLabel lateFeeSubject;
   JLabel lateFee;
   Label statusLabel;
   JTextArea description;
   JPanel centerBar;
   JPanel seperatorBar1;
   JPanel descriptionPanel;
   Color backgroundColor;
   Color titleColor;
   Color descriptionColor;
   Font titleFont;
   Font descriptionFont;
   Font subjectFont;
   ActionListener dvdDescriptionAction;
   ImageIcon icon;
   File f;
   int currentTitleId;
   Image oldIcon;
   Image newImage;
   String temp;
   String buyButtonActionCommand;
   boolean disableRentPurchase = false;

   public DvdDescriptionScreen(String prevScreen, String currScreen, String data) {
      try {
         this.setCurrentLocale(Aem.getLocale());
         this.dvdDescriptionAction = new DvdDescriptionScreen.DvdDescriptionAction();
         this.init();
         this.centerBar = new JPanel(null);
         this.createContent();
         this.currentTitleId = 0;
         this.setLayout(null);
         this.setBackground(this.backgroundColor);
         this.createBlackBottomBar(
            true,
            false,
            Aem.getString(1012),
            "Back",
            Aem.getString(1009) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
            "BuyMovie",
            Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
            "RentMovie"
         );
         this.createTopBar(Aem.getString(5005), Aem.getString(1011), "Cart", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.topBar.addActionListener(this.dvdDescriptionAction);
         this.bottomBar.addActionListener(this.dvdDescriptionAction);
         this.centerBar.setBackground(this.backgroundColor);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.add(this.centerBar);
         this.add(this.topBar);
         this.add(this.bottomBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("DvdDescriptionScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("DvdDescriptionScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         this.disableRentPurchase = false;
         this.currentTitleId = AbstractContentBar.aemContent.getCurrentDvdDescriptionDiscId();
         StringBuffer var10003 = new StringBuffer().append("c:\\aem\\content\\posters\\");
         Aem.getDiscIndex();
         this.f = new File(var10003.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getPoster()).toString());
         if (this.f.exists() && this.f.isFile()) {
            var10003 = new StringBuffer().append("c:\\aem\\content\\posters\\");
            Aem.getDiscIndex();
            this.icon = new ImageIcon(var10003.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getPoster()).toString());
         } else {
            Level var10000 = DvdplayLevel.FINE;
            StringBuffer var10001 = new StringBuffer().append("c:\\aem\\content\\posters\\");
            Aem.getDiscIndex();
            Aem.logDetailMessage(
               var10000,
               var10001.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getPoster()).append(" not found, use default image").toString()
            );
            this.icon = ScreenProperties.getImage("Default.BigPoster.NotFound");
         }

         this.oldIcon = this.icon.getImage();
         this.newImage = this.oldIcon.getScaledInstance(340, 490, 0);
         this.poster.setIcon(new ImageIcon(this.newImage));
         Aem.getDiscIndex();
         this.createStatusLabel(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getDiscStatusId());
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr5().trim().equals("")) {
            JLabel var10 = this.title;
            Aem.getDiscIndex();
            var10.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getOriginalTitle());
         } else {
            JLabel var11 = this.title;
            StringBuffer var18 = new StringBuffer();
            Aem.getDiscIndex();
            var18 = var18.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getOriginalTitle()).append(" (");
            Aem.getDiscIndex();
            var11.setText(var18.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr5()).append(")").toString());
         }

         Aem.getDiscIndex();
         String var12 = DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getTranslatedTitle();
         Aem.getDiscIndex();
         if (var12.equalsIgnoreCase(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getOriginalTitle())) {
            this.title2.setVisible(false);
         } else {
            JLabel var13 = this.title2;
            Aem.getDiscIndex();
            var13.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getTranslatedTitle());
            this.title2.setVisible(true);
         }

         this.subtitledIn.setBounds(460 + Aem.getString(5206).length() * 9, 190, 700, 20);
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr1().trim().equals("")) {
            this.subtitledInSubject.setVisible(false);
         } else {
            JLabel var14 = this.subtitledInSubject;
            StringBuffer var20 = new StringBuffer().append(Aem.getString(5206)).append(": ");
            Aem.getDiscIndex();
            var14.setText(var20.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr1()).toString());
            this.subtitledInSubject.setVisible(false);
         }

         this.category.setText(this.generateCategory(this.currentTitleId));
         JLabel var15 = this.duration;
         Aem.getDiscIndex();
         var15.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr6());
         Aem.getDiscIndex();
         this.temp = DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getDescription();
         TextToRows ttr = new TextToRows(this.temp.replaceAll("\\\\n", ""), 70);
         this.descriptionPanel.removeAll();

         for (int i = 0; i < ttr.getRowCount() && i < 14; i++) {
            JLabel ff = new JLabel(ttr.getRow(i));
            ff.setFont(new Font("Arial", 0, 16));
            ff.setForeground(this.descriptionColor);
            ff.setBackground(this.backgroundColor);
            ff.setBounds(0, i * 18, 550, 18);
            this.descriptionPanel.add(ff);
         }

         JLabel var16 = this.starring;
         Aem.getDiscIndex();
         var16.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr3());
         this.starringSubject.setText(Aem.getString(5208) + ": ");
         this.starring.setBounds(460 + Aem.getString(5208).length() * 9, 130, 450, 20);
         JLabel var17 = this.director;
         Aem.getDiscIndex();
         var17.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr4());
         this.directorSubject.setText(Aem.getString(5209) + ": ");
         this.director.setBounds(460 + Aem.getString(5209).length() * 9, 150, 350, 20);
         this.rating.setText(this.generateRating(this.currentTitleId));
         this.ratingSubject.setText(Aem.getString(5210) + ": ");
         this.rating.setBounds(460 + Aem.getString(5210).length() * 9, 170, 240, 20);

         try {
            Aem.getDiscIndex();
            PricingItem lPricingItem = Aem.getPricing(
               DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getPriceOptionId(), AbstractContentBar.aemContent.getTransactionTime()
            );
            this.rentalPrice.setText(Aem.getCurrencySymbol() + lPricingItem.getRentalPrice().setScale(Aem.getCurrencyFractionalDigits(), 4).toString());
            this.rentalPriceSubject.setText(Aem.getString(5212) + ": ");
            this.dueBack.setText(lPricingItem.getDueDateLong(AbstractContentBar.aemContent.getTransactionTime()));
            this.dueBackSubject.setText(Aem.getString(5213) + ": ");
            this.lateFee.setText(Aem.getCurrencySymbol() + lPricingItem.getLateRentalPrice().setScale(Aem.getCurrencyFractionalDigits(), 4).toString());
            this.lateFeeSubject.setText(Aem.getString(5204));
            this.usedPrice.setText(Aem.getCurrencySymbol() + lPricingItem.getUsedPrice().setScale(Aem.getCurrencyFractionalDigits(), 4).toString());
            this.usedPriceSubject.setText(Aem.getString(5214) + ": ");
         } catch (DvdplayException var8) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, var8.getMessage());
            this.rentalPrice.setText("");
            this.rentalPriceSubject.setText(Aem.getString(5212) + ": ");
            this.dueBack.setText("");
            this.dueBackSubject.setText(Aem.getString(5213) + ": ");
            this.lateFee.setText("");
            this.lateFeeSubject.setText(Aem.getString(5204));
            this.usedPrice.setText("");
            this.usedPriceSubject.setText(Aem.getString(5214) + ": ");
            this.disableRentPurchase = true;
         }

         if (Aem.isBuyDisabled()) {
            this.buyButtonActionCommand = "";
            this.usedPrice.setVisible(false);
            this.usedPriceSubject.setVisible(false);
            this.dueBackSubject.setBounds(460, 530, 150, 20);
            this.dueBack.setBounds(630, 530, 350, 20);
            this.lateFeeSubject.setBounds(460, 550, 150, 20);
            this.lateFee.setBounds(630, 550, 350, 20);
         } else {
            this.buyButtonActionCommand = Aem.getString(1009) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular();
            this.usedPrice.setVisible(true);
            this.usedPriceSubject.setVisible(true);
            this.dueBackSubject.setBounds(460, 550, 150, 20);
            this.dueBack.setBounds(630, 550, 350, 20);
            this.lateFeeSubject.setBounds(460, 570, 150, 20);
            this.lateFee.setBounds(630, 570, 350, 20);
         }

         if (this.disableRentPurchase) {
            this.bottomBar
               .setProperty(
                  ScreenProperties.getColor("Black"),
                  true,
                  true,
                  false,
                  Aem.getString(1012),
                  "Back",
                  this.buyButtonActionCommand,
                  "",
                  Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                  ""
               );
         } else {
            label71:
            if (AbstractContentBar.aemContent.getCartItemCount() < 4) {
               Aem.getDiscIndex();
               if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).isMarkedForRent()) {
                  Aem.getDiscIndex();
                  if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getDiscStatusId() == 3) {
                     Aem.getDiscIndex();
                     if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).isMarkedForSale()) {
                        this.bottomBar
                           .setProperty(
                              ScreenProperties.getColor("Black"),
                              true,
                              true,
                              false,
                              Aem.getString(1012),
                              "Back",
                              this.buyButtonActionCommand,
                              "BuyMovie",
                              Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                              "RentMovie"
                           );
                     } else {
                        this.bottomBar
                           .setProperty(
                              ScreenProperties.getColor("Black"),
                              true,
                              true,
                              false,
                              Aem.getString(1012),
                              "Back",
                              this.buyButtonActionCommand,
                              "",
                              Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                              "RentMovie"
                           );
                     }
                     break label71;
                  }
               }

               Aem.getDiscIndex();
               if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).isMarkedForSale()) {
                  Aem.getDiscIndex();
                  if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getDiscStatusId() == 3) {
                     this.bottomBar
                        .setProperty(
                           ScreenProperties.getColor("Black"),
                           true,
                           true,
                           false,
                           Aem.getString(1012),
                           "Back",
                           this.buyButtonActionCommand,
                           "BuyMovie",
                           Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                           ""
                        );
                     break label71;
                  }
               }

               this.bottomBar
                  .setProperty(
                     ScreenProperties.getColor("Black"),
                     true,
                     true,
                     false,
                     Aem.getString(1012),
                     "Back",
                     this.buyButtonActionCommand,
                     "",
                     Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                     ""
                  );
            } else {
               this.bottomBar
                  .setProperty(
                     ScreenProperties.getColor("Black"),
                     true,
                     true,
                     false,
                     Aem.getString(1012),
                     "Back",
                     this.buyButtonActionCommand,
                     "",
                     Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                     ""
                  );
            }
         }

         if (AbstractContentBar.aemContent.getCartItemCount() > 0) {
            this.topBar.setProperty(Aem.getString(5005), Aem.getString(1011), "Cart", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         } else {
            this.topBar.setProperty(Aem.getString(5005), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         }

         this.msg = new StringBuffer("* ").append("DvdDescriptionScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var9) {
         this.msg = new StringBuffer("[").append("DvdDescriptionScreen").append("]").append("]").append(var9.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var9);
      }
   }

   private void init() {
      this.backgroundColor = ScreenProperties.getColor("White");
      this.titleColor = ScreenProperties.getColor("Red");
      this.descriptionColor = ScreenProperties.getColor("Black");
      this.titleFont = ScreenProperties.getFont("DvdDescriptionTitle");
      this.descriptionFont = ScreenProperties.getFont("DvdDescriptionDescription");
      this.subjectFont = ScreenProperties.getFont("DvdDescriptionSubject");
   }

   private String generateRating(int lCurrentTitleId) {
      String lRating = "";
      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating1Id() != 0) {
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem1Id() != 0) {
            StringBuffer var10000 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            int var10001 = DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating1Id();
            Aem.getDiscIndex();
            lRating = var10000.append(Aem.getRating(var10001, DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem1Id()).trim())
               .toString();
            lRating = lRating + "(";
            var10000 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            lRating = var10000.append(Aem.getRatingSystem(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem1Id())).toString();
            lRating = lRating + ") ";
         }
      }

      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating2Id() != 0) {
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem2Id() != 0) {
            lRating = lRating + ",";
            StringBuffer var15 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            int var19 = DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating2Id();
            Aem.getDiscIndex();
            lRating = var15.append(Aem.getRating(var19, DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem2Id()).trim()).toString();
            lRating = lRating + "(";
            var15 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            lRating = var15.append(Aem.getRatingSystem(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem2Id())).toString();
            lRating = lRating + ") ";
         }
      }

      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating3Id() != 0) {
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem3Id() != 0) {
            lRating = lRating + ",";
            StringBuffer var17 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            int var20 = DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating3Id();
            Aem.getDiscIndex();
            lRating = var17.append(Aem.getRating(var20, DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem3Id()).trim()).toString();
            lRating = lRating + "(";
            var17 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            lRating = var17.append(Aem.getRatingSystem(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem3Id())).toString();
            lRating = lRating + ")";
         }
      }

      return lRating;
   }

   private String generateCategory(int lCurrentTitleId) {
      String lCategory = "";
      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre1Id() != 0) {
         StringBuffer var10000 = new StringBuffer().append(lCategory);
         Aem.getDiscIndex();
         lCategory = var10000.append(Aem.getGenreName(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre1Id())).toString();
      }

      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre2Id() != 0) {
         lCategory = lCategory + " / ";
         StringBuffer var5 = new StringBuffer().append(lCategory);
         Aem.getDiscIndex();
         lCategory = var5.append(Aem.getGenreName(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre2Id())).toString();
      }

      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre3Id() != 0) {
         lCategory = lCategory + " / ";
         StringBuffer var6 = new StringBuffer().append(lCategory);
         Aem.getDiscIndex();
         lCategory = var6.append(Aem.getGenreName(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre3Id())).toString();
      }

      return lCategory;
   }

   public void createContent() {
      this.poster = new JLabel(new ImageIcon());
      this.poster.setBounds(90, 60, 340, 490);
      this.poster.setBackground(this.backgroundColor);
      this.poster.setBorder(new EtchedBorder(this.descriptionColor, this.descriptionColor));
      this.title = new JLabel();
      this.title.setFont(this.titleFont);
      this.title.setForeground(this.titleColor);
      this.title.setBackground(this.backgroundColor);
      this.title.setBounds(460, 30, 700, 30);
      this.title2 = new JLabel();
      this.title2.setFont(this.subjectFont);
      this.title2.setForeground(this.descriptionColor);
      this.title2.setBackground(this.backgroundColor);
      this.title2.setBounds(460, 60, 700, 20);
      this.title2.setVisible(false);
      this.category = new JLabel();
      this.category.setFont(this.subjectFont);
      this.category.setForeground(this.descriptionColor);
      this.category.setBackground(this.backgroundColor);
      this.category.setBounds(460, 80, 700, 20);
      this.duration = new JLabel();
      this.duration.setFont(this.subjectFont);
      this.duration.setForeground(this.descriptionColor);
      this.duration.setBackground(this.backgroundColor);
      this.duration.setBounds(460, 100, 700, 20);
      this.starringSubject = new JLabel(Aem.getString(5208));
      this.starringSubject.setBackground(this.backgroundColor);
      this.starringSubject.setForeground(this.titleColor);
      this.starringSubject.setFont(this.subjectFont);
      this.starringSubject.setPreferredSize(new Dimension(100, 20));
      this.starringSubject.setBounds(460, 130, Aem.getString(5208).length() * 9, 20);
      this.starring = new JLabel();
      this.starring.setBackground(this.backgroundColor);
      this.starring.setForeground(this.descriptionColor);
      this.starring.setFont(this.subjectFont);
      this.starring.setSize(new Dimension(520, 20));
      this.starring.setBounds(460 + Aem.getString(5208).length() * 15, 130, 450, 20);
      this.directorSubject = new JLabel(Aem.getString(5209));
      this.directorSubject.setBackground(this.backgroundColor);
      this.directorSubject.setForeground(this.titleColor);
      this.directorSubject.setFont(this.subjectFont);
      this.directorSubject.setBounds(460, 150, 520, 20);
      this.director = new JLabel();
      this.director.setBackground(this.backgroundColor);
      this.director.setForeground(this.descriptionColor);
      this.director.setFont(this.subjectFont);
      this.director.setBounds(460 + Aem.getString(5209).length() * 9, 150, 350, 20);
      this.ratingSubject = new JLabel(Aem.getString(5210));
      this.ratingSubject.setBackground(this.backgroundColor);
      this.ratingSubject.setForeground(this.titleColor);
      this.ratingSubject.setFont(this.subjectFont);
      this.ratingSubject.setBounds(460, 170, 520, 20);
      this.rating = new JLabel();
      this.rating.setBackground(this.backgroundColor);
      this.rating.setForeground(this.descriptionColor);
      this.rating.setFont(this.subjectFont);
      this.rating.setBounds(460 + Aem.getString(5210).length() * 9, 170, 240, 20);
      this.subtitledInSubject = new JLabel(Aem.getString(5206));
      this.subtitledInSubject.setBackground(this.backgroundColor);
      this.subtitledInSubject.setForeground(this.titleColor);
      this.subtitledInSubject.setFont(this.subjectFont);
      this.subtitledInSubject.setBounds(460, 190, 700, 20);
      this.subtitledIn = new JLabel();
      this.subtitledIn.setFont(this.subjectFont);
      this.subtitledIn.setForeground(this.descriptionColor);
      this.subtitledIn.setBackground(this.backgroundColor);
      this.subtitledIn.setBounds(460 + Aem.getString(5206).length() * 9, 190, 700, 20);
      this.descriptionPanel = new JPanel(null);
      this.descriptionPanel.setBounds(460, 225, 550, 270);
      this.descriptionPanel.setBackground(ScreenProperties.getColor("White"));
      this.seperatorBar1 = this.createBorder(ScreenProperties.getColor("Red"), 460, 500, 450, 1);
      this.rentalPriceSubject = new JLabel(Aem.getString(5212));
      this.rentalPriceSubject.setBackground(this.backgroundColor);
      this.rentalPriceSubject.setForeground(this.descriptionColor);
      this.rentalPriceSubject.setFont(this.subjectFont);
      this.rentalPriceSubject.setBounds(460, 510, 150, 20);
      this.rentalPrice = new JLabel();
      this.rentalPrice.setBackground(this.backgroundColor);
      this.rentalPrice.setForeground(this.descriptionColor);
      this.rentalPrice.setFont(this.descriptionFont);
      this.rentalPrice.setBounds(630, 510, 350, 20);
      this.usedPriceSubject = new JLabel(Aem.getString(5214));
      this.usedPriceSubject.setBackground(this.backgroundColor);
      this.usedPriceSubject.setForeground(this.descriptionColor);
      this.usedPriceSubject.setFont(this.subjectFont);
      this.usedPriceSubject.setBounds(460, 530, 150, 20);
      this.usedPrice = new JLabel();
      this.usedPrice.setBackground(this.backgroundColor);
      this.usedPrice.setForeground(this.descriptionColor);
      this.usedPrice.setFont(this.descriptionFont);
      this.usedPrice.setBounds(630, 530, 350, 20);
      this.dueBackSubject = new JLabel(Aem.getString(5213));
      this.dueBackSubject.setBackground(this.backgroundColor);
      this.dueBackSubject.setForeground(this.descriptionColor);
      this.dueBackSubject.setFont(this.subjectFont);
      this.dueBackSubject.setBounds(460, 550, 150, 20);
      this.dueBack = new JLabel();
      this.dueBack.setBackground(this.backgroundColor);
      this.dueBack.setForeground(ScreenProperties.getColor("Red"));
      this.dueBack.setFont(this.subjectFont);
      this.dueBack.setBounds(630, 550, 350, 20);
      this.lateFeeSubject = new JLabel(Aem.getString(5204));
      this.lateFeeSubject.setBackground(this.backgroundColor);
      this.lateFeeSubject.setForeground(this.descriptionColor);
      this.lateFeeSubject.setFont(this.subjectFont);
      this.lateFeeSubject.setBounds(460, 570, 150, 20);
      this.lateFee = new JLabel();
      this.lateFee.setBackground(this.backgroundColor);
      this.lateFee.setForeground(this.descriptionColor);
      this.lateFee.setFont(this.descriptionFont);
      this.lateFee.setBounds(630, 570, 350, 20);
      this.centerBar.add(this.poster);
      this.centerBar.add(this.title);
      this.centerBar.add(this.title2);
      this.centerBar.add(this.subtitledInSubject);
      this.centerBar.add(this.subtitledIn);
      this.centerBar.add(this.category);
      this.centerBar.add(this.duration);
      this.centerBar.add(this.descriptionPanel);
      this.centerBar.add(this.starring);
      this.centerBar.add(this.starringSubject);
      this.centerBar.add(this.director);
      this.centerBar.add(this.directorSubject);
      this.centerBar.add(this.rating);
      this.centerBar.add(this.ratingSubject);
      this.centerBar.add(this.seperatorBar1);
      this.centerBar.add(this.rentalPrice);
      this.centerBar.add(this.rentalPriceSubject);
      this.centerBar.add(this.dueBackSubject);
      this.centerBar.add(this.dueBack);
      this.centerBar.add(this.lateFeeSubject);
      this.centerBar.add(this.lateFee);
      if (!Aem.isBuyDisabled()) {
         this.centerBar.add(this.usedPrice);
         this.centerBar.add(this.usedPriceSubject);
      }
   }

   public void createStatusLabel(int status) {
      if (this.statusLabel != null) {
         this.centerBar.remove(this.statusLabel);
      }

      this.statusLabel = new Label();
      if (status == 1) {
         new String();
         this.statusLabel.setBackground(ScreenProperties.getColor("Red"));
         String statusText = Aem.getString(6403);
         this.statusLabel.setText(statusText);
         this.statusLabel.setAlignment(1);
         this.statusLabel.setForeground(ScreenProperties.getColor("White"));
         this.statusLabel.setBounds(90, 60, 340, 35);
         this.statusLabel.setFont(ScreenProperties.getFont("DVDDescriptionStatus"));
         this.statusLabel.setVisible(true);
         this.centerBar.add(this.statusLabel);
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

   public class DvdDescriptionAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("DvdDescriptionScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(DvdDescriptionScreen.mainAction)) {
                  if (this.cmd[0].equals("BuyMovie")) {
                     if (Aem.isRentalDisabled()) {
                        DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4043,DvdDescriptionScreen "));
                     } else {
                        AEMContent aemContent = DvdDescriptionScreen.aemContent;
                        Aem.getDiscIndex();
                        aemContent.addPurchase(
                           DiscIndex.getDiscIndexItemByTitleDetailId(DvdDescriptionScreen.aemContent.getCurrentDvdDescriptionDiscId()).getDiscDetailId()
                        );
                        DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                     }
                  } else if (this.cmd[0].equals("RentMovie")) {
                     if (Aem.isRentalDisabled()) {
                        DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4043,DvdDescriptionScreen "));
                     } else {
                        AEMContent aemContent = DvdDescriptionScreen.aemContent;
                        Aem.getDiscIndex();
                        aemContent.addRental(
                           DiscIndex.getDiscIndexItemByTitleDetailId(DvdDescriptionScreen.aemContent.getCurrentDvdDescriptionDiscId()).getDiscDetailId()
                        );
                        DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                     }
                  } else if (this.cmd[0].equals("Cart")) {
                     DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                  } else if (this.cmd[0].equals("Back")) {
                     DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MovieSelectionScreen"));
                  } else if (this.cmd[0].equals("Help")) {
                     DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  } else if (this.cmd[0].equals("StartOver")) {
                     DvdDescriptionScreen.aemContent.reset();
                     DvdDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "DvdDescriptionScreen");
         } catch (Throwable var5) {
            Log.error(var5, "DvdDescriptionScreen");
         }
      }
   }
}
