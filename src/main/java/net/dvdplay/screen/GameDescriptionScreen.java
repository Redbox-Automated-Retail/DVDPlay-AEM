package net.dvdplay.screen;

import java.awt.Color;
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
import javax.swing.JTextPane;
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

public class GameDescriptionScreen extends AbstractContentBar {
   JLabel poster;
   JLabel title;
   JLabel category;
   JLabel developer;
   JLabel rating;
   JLabel players;
   JLabel rentalPrice;
   JLabel usedPrice;
   JLabel dueBack;
   JLabel developerSubject;
   JLabel playersSubject;
   JLabel ratingSubject;
   JLabel rentalPriceSubject;
   JLabel usedPriceSubject;
   JLabel dueBackSubject;
   JLabel lateFeeSubject;
   JLabel lateFee;
   Label statusLabel;
   JTextPane description;
   JPanel centerBar;
   JPanel seperatorBar1;
   JPanel descriptionPanel;
   Color backgroundColor;
   Color titleColor;
   Color descriptionColor;
   Font titleFont;
   Font descriptionFont;
   Font subjectFont;
   ActionListener gameDescriptionAction;
   int currentTitleId;
   ImageIcon icon;
   Image oldIcon;
   Image newImage;
   File f;
   String temp;
   String buyButtonActionCommand;
   boolean disableRentPurchase = false;

   public GameDescriptionScreen(String prevScreen, String currScreen, String data) {
      try {
         this.gameDescriptionAction = new GameDescriptionScreen.GameDescriptionAction();
         this.currentTitleId = 0;
         this.centerBar = new JPanel(null);
         this.init();
         this.createContent();
         this.createStatusLabel(0);
         this.setLayout(null);
         this.setBackground(this.backgroundColor);
         this.centerBar.setBackground(this.backgroundColor);
         this.createBlackBottomBar(
            true,
            false,
            Aem.getString(1012),
            "Back",
            Aem.getString(1009) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
            "BuyGame",
            Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
            "RentGame"
         );
         this.createTopBar(Aem.getString(5006), Aem.getString(1011), "Cart", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.topBar.addActionListener(this.gameDescriptionAction);
         this.bottomBar.addActionListener(this.gameDescriptionAction);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.add(this.centerBar);
         this.add(this.topBar);
         this.add(this.bottomBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("GameDescriptionScreen").append(" from ").append(prevScreen);
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
         if (DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr5().compareTo("") != 0) {
            JLabel var10 = this.title;
            StringBuffer var14 = new StringBuffer();
            Aem.getDiscIndex();
            var14 = var14.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getOriginalTitle()).append(" (");
            Aem.getDiscIndex();
            var10.setText(var14.append(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr5().trim()).append(")").toString());
         } else {
            JLabel var11 = this.title;
            Aem.getDiscIndex();
            var11.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getOriginalTitle());
         }

         this.category.setText(this.generateCategory(this.currentTitleId));
         this.descriptionPanel.removeAll();
         Aem.getDiscIndex();
         this.temp = DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getDescription();
         TextToRows ttr = new TextToRows(this.temp.replaceAll("\\\\n", ""), 70);

         for (int i = 0; i < ttr.getRowCount() && i < 14; i++) {
            JLabel ff = new JLabel(ttr.getRow(i));
            ff.setFont(new Font("Arial", 0, 16));
            ff.setForeground(this.descriptionColor);
            ff.setBackground(this.backgroundColor);
            ff.setBounds(0, i * 18, 550, 18);
            this.descriptionPanel.add(ff);
         }

         JLabel var12 = this.developer;
         Aem.getDiscIndex();
         var12.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr4());
         this.developerSubject.setText(Aem.getString(5202) + ": ");
         this.developer.setBounds(460 + Aem.getString(5202).length() * 9, 120, 700, 20);
         var12 = this.players;
         Aem.getDiscIndex();
         var12.setText(DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getAttr3());
         this.ratingSubject.setText(Aem.getString(5210) + ": ");
         this.rating.setText(this.generateRating(this.currentTitleId));
         this.rating.setBounds(460 + Aem.getString(5210).length() * 9, 140, 500, 20);

         try {
            Aem.getDiscIndex();
            PricingItem lPricingItem = Aem.getPricing(
               DiscIndex.getDiscIndexItemByTitleDetailId(this.currentTitleId).getPriceOptionId(), AbstractContentBar.aemContent.getTransactionTime()
            );
            this.rentalPrice.setText(Aem.getCurrencySymbol() + lPricingItem.getRentalPrice().setScale(Aem.getCurrencyFractionalDigits(), 4).toString());
            this.rentalPriceSubject.setText(Aem.getString(5212) + ": ");
            this.usedPrice.setText(Aem.getCurrencySymbol() + lPricingItem.getUsedPrice().setScale(Aem.getCurrencyFractionalDigits(), 4).toString());
            this.usedPriceSubject.setText(Aem.getString(5214) + ": ");
            this.lateFee.setText(Aem.getCurrencySymbol() + lPricingItem.getLateRentalPrice().setScale(Aem.getCurrencyFractionalDigits(), 4).toString());
            this.lateFeeSubject.setText(Aem.getString(5204));
            this.dueBack.setText(lPricingItem.getDueDateLong(AbstractContentBar.aemContent.getTransactionTime()));
            this.dueBackSubject.setText(Aem.getString(5213) + ": ");
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
            this.dueBackSubject.setBounds(460, 530, 200, 20);
            this.dueBack.setBounds(630, 530, 500, 20);
            this.lateFeeSubject.setBounds(460, 550, 200, 20);
            this.lateFee.setBounds(630, 550, 500, 20);
         } else {
            this.buyButtonActionCommand = Aem.getString(1009) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular();
            this.usedPrice.setVisible(true);
            this.usedPriceSubject.setVisible(true);
            this.dueBackSubject.setBounds(460, 550, 200, 20);
            this.dueBack.setBounds(630, 550, 500, 20);
            this.lateFeeSubject.setBounds(460, 570, 200, 20);
            this.lateFee.setBounds(630, 570, 500, 20);
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
            label61:
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
                              "BuyGame",
                              Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                              "RentGame"
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
                              "RentGame"
                           );
                     }
                     break label61;
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
                           "BuyGame",
                           Aem.getString(1008) + " " + Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleTypeSingular(),
                           ""
                        );
                     break label61;
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
            this.topBar.setProperty(Aem.getString(5006), Aem.getString(1011), "Cart", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         } else {
            this.topBar.setProperty(Aem.getString(5006), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         }

         this.msg = new StringBuffer("* ").append("GameDescriptionScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var9) {
         this.msg = new StringBuffer("[").append("GameDescriptionScreen").append("]").append(var9.toString());
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
         lCategory = lCategory + ", ";
         StringBuffer var5 = new StringBuffer().append(lCategory);
         Aem.getDiscIndex();
         lCategory = var5.append(Aem.getGenreName(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre2Id())).toString();
      }

      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre3Id() != 0) {
         lCategory = lCategory + ", ";
         StringBuffer var6 = new StringBuffer().append(lCategory);
         Aem.getDiscIndex();
         lCategory = var6.append(Aem.getGenreName(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getGenre3Id())).toString();
      }

      return lCategory;
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
            lRating = var10000.append(Aem.getRating(var10001, DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem1Id())).toString();
            lRating = lRating + " (";
            var10000 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            lRating = var10000.append(Aem.getRatingSystem(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem1Id()).trim()).toString();
            lRating = lRating + ")";
         }
      }

      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating2Id() != 0) {
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem2Id() != 0) {
            lRating = lRating + ", ";
            StringBuffer var15 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            int var19 = DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating2Id();
            Aem.getDiscIndex();
            lRating = var15.append(Aem.getRating(var19, DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem2Id())).toString();
            lRating = lRating + " (";
            var15 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            lRating = var15.append(Aem.getRatingSystem(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem2Id()).trim()).toString();
            lRating = lRating + ")";
         }
      }

      Aem.getDiscIndex();
      if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating3Id() != 0) {
         Aem.getDiscIndex();
         if (DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem3Id() != 0) {
            lRating = lRating + ", ";
            StringBuffer var17 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            int var20 = DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRating3Id();
            Aem.getDiscIndex();
            lRating = var17.append(Aem.getRating(var20, DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem3Id())).toString();
            lRating = lRating + " (";
            var17 = new StringBuffer().append(lRating);
            Aem.getDiscIndex();
            lRating = var17.append(Aem.getRatingSystem(DiscIndex.getDiscIndexItemByTitleDetailId(lCurrentTitleId).getRatingSystem3Id()).trim()).toString();
            lRating = lRating + ")";
         }
      }

      return lRating;
   }

   public void createContent() {
      this.seperatorBar1 = this.createBorder(ScreenProperties.getColor("Red"), 460, 500, 450, 2);
      this.poster = new JLabel();
      this.poster.setBounds(90, 60, 340, 490);
      this.poster.setBackground(this.backgroundColor);
      this.poster.setBorder(new EtchedBorder(this.descriptionColor, this.descriptionColor));
      this.title = new JLabel();
      this.title.setFont(this.titleFont);
      this.title.setForeground(this.titleColor);
      this.title.setBackground(this.backgroundColor);
      this.title.setBounds(460, 30, 700, 40);
      this.category = new JLabel();
      this.category.setFont(this.subjectFont);
      this.category.setForeground(this.descriptionColor);
      this.category.setBackground(this.backgroundColor);
      this.category.setBounds(460, 70, 700, 20);
      this.players = new JLabel();
      this.players.setFont(this.subjectFont);
      this.players.setForeground(this.descriptionColor);
      this.players.setBackground(this.backgroundColor);
      this.players.setBounds(460, 90, 200, 20);
      this.description = new JTextPane();
      this.description.setForeground(this.descriptionColor);
      this.description.setBackground(this.backgroundColor);
      this.description.setBounds(460, 180, 500, 260);
      this.developerSubject = new JLabel(Aem.getString(5202));
      this.developerSubject.setFont(this.subjectFont);
      this.developerSubject.setForeground(this.titleColor);
      this.developerSubject.setBackground(this.backgroundColor);
      this.developerSubject.setBounds(460, 120, 700, 20);
      this.developer = new JLabel();
      this.developer.setFont(this.subjectFont);
      this.developer.setForeground(this.descriptionColor);
      this.developer.setBackground(this.backgroundColor);
      this.developer.setBounds(630, 120, 700, 20);
      this.ratingSubject = new JLabel(Aem.getString(5210));
      this.ratingSubject.setBackground(this.backgroundColor);
      this.ratingSubject.setForeground(this.titleColor);
      this.ratingSubject.setFont(this.subjectFont);
      this.ratingSubject.setBounds(460, 140, 200, 20);
      this.rating = new JLabel();
      this.rating.setBackground(this.backgroundColor);
      this.rating.setForeground(this.descriptionColor);
      this.rating.setFont(this.subjectFont);
      this.rating.setBounds(630, 140, 500, 20);
      this.descriptionPanel = new JPanel(null);
      this.descriptionPanel.setBounds(460, 170, 550, 270);
      this.descriptionPanel.setBackground(ScreenProperties.getColor("White"));
      this.rentalPrice = new JLabel();
      this.rentalPriceSubject = new JLabel(Aem.getString(5212));
      this.rentalPriceSubject.setBackground(this.backgroundColor);
      this.rentalPriceSubject.setForeground(this.descriptionColor);
      this.rentalPriceSubject.setFont(this.subjectFont);
      this.rentalPriceSubject.setBounds(460, 510, 150, 20);
      this.rentalPrice = new JLabel();
      this.rentalPrice.setBackground(this.backgroundColor);
      this.rentalPrice.setForeground(this.descriptionColor);
      this.rentalPrice.setFont(this.subjectFont);
      this.rentalPrice.setBounds(630, 510, 350, 20);
      this.usedPriceSubject = new JLabel(Aem.getString(5214));
      this.usedPriceSubject.setBackground(this.backgroundColor);
      this.usedPriceSubject.setForeground(this.descriptionColor);
      this.usedPriceSubject.setFont(this.subjectFont);
      this.usedPriceSubject.setBounds(460, 530, 200, 20);
      this.usedPrice = new JLabel();
      this.usedPrice.setBackground(this.backgroundColor);
      this.usedPrice.setForeground(this.descriptionColor);
      this.usedPrice.setFont(this.subjectFont);
      this.usedPrice.setBounds(630, 530, 500, 20);
      this.dueBackSubject = new JLabel(Aem.getString(5213));
      this.dueBackSubject.setFont(this.subjectFont);
      this.dueBackSubject.setForeground(this.descriptionColor);
      this.dueBackSubject.setBackground(this.backgroundColor);
      this.dueBackSubject.setBounds(460, 550, 200, 20);
      this.dueBack = new JLabel();
      this.dueBack.setFont(this.subjectFont);
      this.dueBack.setForeground(ScreenProperties.getColor("Red"));
      this.dueBack.setBackground(this.backgroundColor);
      this.dueBack.setBounds(630, 550, 500, 20);
      this.lateFeeSubject = new JLabel(Aem.getString(5204));
      this.lateFeeSubject.setBackground(this.backgroundColor);
      this.lateFeeSubject.setForeground(this.descriptionColor);
      this.lateFeeSubject.setFont(this.subjectFont);
      this.lateFeeSubject.setBounds(460, 570, 200, 20);
      this.lateFee = new JLabel();
      this.lateFee.setBackground(this.backgroundColor);
      this.lateFee.setForeground(this.descriptionColor);
      this.lateFee.setFont(this.subjectFont);
      this.lateFee.setBounds(630, 570, 500, 20);
      this.centerBar.add(this.poster);
      this.centerBar.add(this.title);
      this.centerBar.add(this.category);
      this.centerBar.add(this.players);
      this.centerBar.add(this.rating);
      this.centerBar.add(this.ratingSubject);
      this.centerBar.add(this.developerSubject);
      this.centerBar.add(this.developer);
      this.centerBar.add(this.seperatorBar1);
      this.centerBar.add(this.rentalPrice);
      this.centerBar.add(this.rentalPriceSubject);
      this.centerBar.add(this.descriptionPanel);
      if (!Aem.isBuyDisabled()) {
         this.centerBar.add(this.usedPrice);
         this.centerBar.add(this.usedPriceSubject);
      }

      this.centerBar.add(this.lateFeeSubject);
      this.centerBar.add(this.lateFee);
      this.centerBar.add(this.dueBackSubject);
      this.centerBar.add(this.dueBack);
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

   public class GameDescriptionAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("GameDescriptionScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(GameDescriptionScreen.mainAction)) {
                  if (this.cmd[0].equals("BuyGame")) {
                     if (Aem.isRentalDisabled()) {
                        GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4043,GameDescriptionScreen "));
                     } else {
                        AEMContent var10000 = GameDescriptionScreen.aemContent;
                        Aem.getDiscIndex();
                        var10000.addPurchase(
                           DiscIndex.getDiscIndexItemByTitleDetailId(GameDescriptionScreen.aemContent.getCurrentDvdDescriptionDiscId()).getDiscDetailId()
                        );
                        GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                     }
                  } else if (this.cmd[0].equals("RentGame")) {
                     if (Aem.isRentalDisabled()) {
                        GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4043,GameDescriptionScreen "));
                     } else {
                        AEMContent var6 = GameDescriptionScreen.aemContent;
                        Aem.getDiscIndex();
                        var6.addRental(
                           DiscIndex.getDiscIndexItemByTitleDetailId(GameDescriptionScreen.aemContent.getCurrentDvdDescriptionDiscId()).getDiscDetailId()
                        );
                        GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                     }
                  } else if (this.cmd[0].equals("Cart")) {
                     GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                  } else if (this.cmd[0].equals("Back")) {
                     GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MovieSelectionScreen"));
                  } else if (this.cmd[0].equals("Help")) {
                     GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  } else if (this.cmd[0].equals("StartOver")) {
                     GameDescriptionScreen.aemContent.reset();
                     GameDescriptionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                  }
               }
            }
         } catch (Exception var4) {
            Log.warning(var4, "GameDescriptionScreen");
         } catch (Throwable var5) {
            Log.error(var5, "GameDescriptionScreen");
         }
      }
   }
}
