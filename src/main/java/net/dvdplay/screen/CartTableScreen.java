package net.dvdplay.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.base.DvdplayBase;
import net.dvdplay.hardware.AuthorizingPaymentCard;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class CartTableScreen extends AbstractContentBar {
   String[] headers = new String[6];
   Icon tempIcon;
   JPanel centerBar;
   JPanel headerPanel;
   JPanel outterBorder;
   JPanel totalAmountBorder;
   JPanel orderPanel;
   JPanel secondTable;
   ActionListener cartTableAction;
   JLabel subtotalText;
   JLabel subtotal;
   JLabel taxText;
   JLabel tax;
   JLabel totalText;
   JLabel total;
   JLabel promoCodeText;
   JLabel promoCode;
   JLabel invalidPromoCode;
   JPanel totalBorder;
   JPanel whiteBorder;
   JPanel invalidPromoCodePanel;
   JLabel[] headerText;
   JButton promoCodeButton;
   JButton iconLabel;
   JButton promoCodeRemove;
   String addString;
   String nextScreen;
   String originalPointBefore;
   TextToRows ttr;
   JLabel[] note;

   public CartTableScreen(String prevScreen, String currScreen, String data) {
      try {
         this.cartTableAction = new CartTableScreen.CartTableAction();
         this.originalPointBefore = "";
         this.setCurrentLocale(Aem.getLocale());
         this.tempIcon = ScreenProperties.getImage("Cart.Icon.Trash");
         this.importTitle();
         this.setLayout(null);
         this.setBackground(Color.WHITE);
         this.centerBar = new JPanel();
         this.centerBar.setLayout(null);
         this.centerBar.setBackground(Color.WHITE);
         this.headerPanel = new JPanel();
         this.CreateHeaderPanel();
         this.outterBorder = this.createBorder(ScreenProperties.getColor("Red"), 30, 40, 970, 500);
         this.totalAmountBorder = this.createBorder(ScreenProperties.getColor("Red"), 30, 420, 970, 120);
         this.CreateDiscOrder();
         this.CreatePromoCodePanel();
         this.CreateTotalPanel();
         this.CreateFootnoteLabel();
         this.msg = new StringBuffer(Aem.getString(1015)).append(" ").append(Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleType());
         this.createBlackBottomBar(true, false, Aem.getString(1012), "Back", this.msg.toString(), "Add", Aem.getString(5301), "CheckOut");
         this.createTopBar(Aem.getString(1011), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.topBar.addActionListener(this.cartTableAction);
         this.bottomBar.addActionListener(this.cartTableAction);
         this.centerBar.add(this.orderPanel);
         this.centerBar.add(this.headerPanel);
         this.centerBar.add(this.totalAmountBorder);
         this.centerBar.add(this.outterBorder);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.setBounds(0, 0, 1024, 768);
         this.add(this.centerBar);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.msg = new StringBuffer("* ").append("CartTableScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("CartTableScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (prevScreen.equals("DvdDescriptionScreen") || prevScreen.equals("GameDescriptionScreen") || prevScreen.equals("MovieSelectionScreen")) {
            this.originalPointBefore = prevScreen;
         }

         AuthorizingPaymentCard.resetVerificationRetry();
         this.CreatePromoCodePanel();
         this.CreateDiscOrder();
         if (!this.isCurrentLocale()) {
            this.setCurrentLocale(Aem.getLocale());
            this.ttr = new TextToRows(Aem.getString(5315), 140);

            for (int i = 0; i < this.ttr.getRowCount(); i++) {
               this.note[i].setText(this.ttr.getRow(i));
            }

            this.subtotalText.setText(Aem.getString(5311));
            this.taxText.setText(Aem.getString(5312));
            this.totalText.setText(Aem.getString(5313));
         }

         this.subtotal.setText(AbstractContentBar.aemContent.getSubTotal());
         this.tax.setText(AbstractContentBar.aemContent.getTax());
         this.total.setText(AbstractContentBar.aemContent.getTotal());
         this.importTitle();

         for (int i = 0; i < this.headers.length; i++) {
            this.headerText[i].setText(this.headers[i]);
         }

         this.msg = new StringBuffer(Aem.getString(1015)).append(" ").append(Aem.getTitleTypeIndexItemByTitleTypeId(Aem.getTitleTypeId()).getTitleType());
         if (AbstractContentBar.aemContent.getCartItemCount() == 4) {
            this.bottomBar
               .setProperty(
                  ScreenProperties.getColor("Black"), true, true, false, Aem.getString(1012), "Back", this.msg.toString(), "", Aem.getString(5301), "CheckOut"
               );
         } else if (AbstractContentBar.aemContent.getCartItemCount() <= 0) {
            this.bottomBar
               .setProperty(
                  ScreenProperties.getColor("Black"), true, true, false, Aem.getString(1012), "Back", this.msg.toString(), "Add", Aem.getString(5301), ""
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
                  this.msg.toString(),
                  "Add",
                  Aem.getString(5301),
                  "CheckOut"
               );
         }

         this.topBar.setProperty(Aem.getString(1011), "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.bottomBar.addActionListener(this.cartTableAction);
         this.msg = new StringBuffer("* ").append("CartTableScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("CartTableScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void CreateFootnoteLabel() {
      this.note = new JLabel[3];

      for (int i = 0; i < this.note.length; i++) {
         this.note[i] = this.createDiscOrderLabel("", ScreenProperties.getColor("Black"), 40, 550 + i * 15, 970, 20, ScreenProperties.getFont("CartScreenNote"));
         this.centerBar.add(this.note[i]);
      }

      this.ttr = new TextToRows(Aem.getString(5315), 140);

      for (int i = 0; i < this.ttr.getRowCount(); i++) {
         this.note[i].setText(this.ttr.getRow(i));
      }
   }

   private void CreatePromoCodePanel() {
      if (this.secondTable == null) {
         this.secondTable = this.createBorder(ScreenProperties.getColor("Red"), 30, 361, 970, 60);
         this.promoCodeRemove = new JButton(Aem.getString(6501));
         this.promoCodeRemove.setBackground(ScreenProperties.getColor("Orange"));
         this.promoCodeRemove.setForeground(ScreenProperties.getColor("White"));
         this.promoCodeRemove.setBounds(860, 10, 80, 35);
         this.promoCodeRemove.setFont(ScreenProperties.getFont("CartScreenDescription"));
         this.promoCodeRemove.setActionCommand("RemovePromoCode");
         this.promoCodeRemove.addActionListener(this.cartTableAction);
         this.promoCodeRemove.setFocusPainted(false);
         this.promoCodeRemove.setBorder(new EtchedBorder());
         this.promoCodeText = this.createDiscOrderLabel(
            Aem.getString(5314) + ":", ScreenProperties.getColor("Orange"), 20, 15, 400, 30, ScreenProperties.getFont("CartScreenDescription")
         );
         this.promoCode = this.createDiscOrderLabel(
            AbstractContentBar.aemContent.getPromoCodeValue(),
            ScreenProperties.getColor("Orange"),
            755,
            10,
            100,
            30,
            ScreenProperties.getFont("CartScreenDescription")
         );
         this.secondTable.add(this.promoCodeText);
         this.secondTable.add(this.promoCode);
         this.secondTable.add(this.promoCodeRemove);
         this.centerBar.add(this.secondTable);
      }

      if (this.promoCodeButton == null) {
         ImageIcon ii = ScreenProperties.getImage("Cart.Icon.PromoCode");
         this.promoCodeButton = new JButton(ii);
         ii.setImageObserver(this.promoCodeButton);
         this.promoCodeButton.setBounds(60, 500, ScreenProperties.getInt("PromoCode.Logo.Width") + 21, ScreenProperties.getInt("PromoCode.Logo.Height") + 10);
         this.promoCodeButton.setBackground(ScreenProperties.getColor("White"));
         this.promoCodeButton.setOpaque(true);
         this.promoCodeButton.setFocusPainted(false);
         this.promoCodeButton.setBorder(BorderFactory.createEtchedBorder(0, ScreenProperties.getColor("White"), ScreenProperties.getColor("White")));
         this.promoCodeButton.setActionCommand("PromoCode");
         this.promoCodeButton.addActionListener(this.cartTableAction);
         this.add(this.promoCodeButton);
      }

      if (AbstractContentBar.aemContent.hasValidPromoCode()) {
         this.secondTable.setVisible(true);
         this.promoCodeText.setText(Aem.getString(5314) + ":");
         this.promoCodeRemove = new JButton(Aem.getString(6501));
         this.promoCode.setText(AbstractContentBar.aemContent.getPromoCodeValue());
         this.promoCodeButton.setVisible(false);
      } else {
         this.secondTable.setVisible(false);
         this.promoCodeButton.setVisible(true);
      }
   }

   private void CreateTotalPanel() {
      this.subtotalText = this.createDiscOrderLabel(Aem.getString(5311) + ":", 620, 10, 100, 30);
      this.subtotal = this.createDiscOrderLabel(AbstractContentBar.aemContent.getSubTotal(), 760, 10, 100, 30);
      this.taxText = this.createDiscOrderLabel(Aem.getString(5312) + ":", 620, 40, 100, 30);
      this.tax = this.createDiscOrderLabel(AbstractContentBar.aemContent.getTax(), 760, 40, 100, 30);
      this.totalText = this.createDiscOrderLabel(Aem.getString(5313) + ":", 0, 5, 100, 30);
      this.total = this.createDiscOrderLabel(AbstractContentBar.aemContent.getTotal(), 140, 5, 100, 30);
      this.totalBorder = this.createBorder(ScreenProperties.getColor("Red"), 620, 80, 350, 40);
      this.whiteBorder = this.createBorder(ScreenProperties.getColor("White"), 611, 80, 10, 39);
      this.totalBorder.add(this.totalText);
      this.totalBorder.add(this.total);
      this.totalAmountBorder.add(this.whiteBorder);
      this.totalAmountBorder.add(this.totalBorder);
      this.totalAmountBorder.add(this.subtotalText);
      this.totalAmountBorder.add(this.taxText);
      this.totalAmountBorder.add(this.subtotal);
      this.totalAmountBorder.add(this.tax);
   }

   private void CreateHeaderPanel() {
      Hashtable ht = new Hashtable();
      ht.put("0", "20");
      ht.put("1", "370");
      ht.put("2", "500");
      ht.put("3", "640");
      ht.put("4", "760");
      ht.put("5", "860");
      this.headerPanel.setBackground(ScreenProperties.getColor("Red"));
      this.headerPanel.setLayout(null);
      this.headerPanel.setBounds(30, 40, 970, 40);
      this.headerText = new JLabel[this.headers.length];

      for (int i = 0; i < this.headers.length; i++) {
         this.headerText[i] = new JLabel(this.headers[i]);
         this.headerText[i].setFont(ScreenProperties.getFont("CartScreenTitle"));
         this.headerText[i].setForeground(ScreenProperties.getColor("White"));
         this.headerPanel.add(this.headerText[i]);
         if (i == 0) {
            this.headerText[i].setBounds(20, 0, 220, 40);
         } else {
            String dd = (String)ht.get("" + i);
            this.headerText[i].setBounds(Integer.parseInt(dd), 0, 120, 40);
         }
      }
   }

   private void CreateDiscOrder() {
      if (this.orderPanel == null) {
         this.orderPanel = new JPanel();
         this.orderPanel.setLayout(null);
         this.orderPanel.setBackground(ScreenProperties.getColor("White"));
      } else {
         this.orderPanel.removeAll();
      }

      for (int i = 0; i < AbstractContentBar.aemContent.getCartItemCount(); i++) {
         Aem.logDetailMessage(
            DvdplayLevel.INFO,
            "DiscOrder : [Title]"
               + AbstractContentBar.aemContent.getTitle(i)
               + " [Rent/Buy]"
               + AbstractContentBar.aemContent.getRentBuy(i)
               + " [Due Date]"
               + AbstractContentBar.aemContent.getDueDate(i)
               + " [DueTime]"
               + AbstractContentBar.aemContent.getDueTime(i)
               + " [Price]"
               + AbstractContentBar.aemContent.getPrice(i)
         );
         this.orderPanel.add(this.createDiscOrderLabel(AbstractContentBar.aemContent.getTitle(i), 0, i * 50, 320, 40));
         this.orderPanel.add(this.createDiscOrderLabel(AbstractContentBar.aemContent.getRentBuy(i), 370, i * 50, 120, 40));
         if (AbstractContentBar.aemContent.getRentBuy(i).equals("Rent")) {
            this.orderPanel.add(this.createDiscOrderLabel(AbstractContentBar.aemContent.getDueDate(i), 465, i * 50, 140, 40));
            this.orderPanel.add(this.createDiscOrderLabel(AbstractContentBar.aemContent.getDueTime(i), 620, i * 50, 120, 40));
         }

         this.orderPanel.add(this.createDiscOrderLabel(AbstractContentBar.aemContent.getPrice(i), 740, i * 50, 120, 40));
         this.iconLabel = new JButton(this.tempIcon);
         this.iconLabel.setBackground(ScreenProperties.getColor("White"));
         this.iconLabel.setBorder(null);
         this.iconLabel.setBounds(830, i * 50, 70, 35);
         this.iconLabel.setActionCommand("Remove " + i);
         this.iconLabel.addActionListener(this.cartTableAction);
         this.orderPanel.add(this.iconLabel);
         this.orderPanel.setBounds(50, 90, 940, 200);
      }
   }

   public JLabel createDiscOrderLabel(String text, int x, int y, int width, int height) {
      JLabel titleText = new JLabel(text);
      titleText.setFont(ScreenProperties.getFont("CartScreenDescription"));
      titleText.setBounds(x, y, width, height);
      return titleText;
   }

   public JLabel createDiscOrderLabel(String text, Color textColor, int x, int y, int width, int height, Font font) {
      JLabel titleText = new JLabel(text);
      titleText.setForeground(textColor);
      titleText.setFont(font);
      titleText.setBounds(x, y, width, height);
      return titleText;
   }

   private void importTitle() {
      if (Aem.isBuyDisabled()) {
         this.headers[1] = "";
      } else {
         this.headers[1] = Aem.getString(5306);
      }

      this.headers[0] = Aem.getString(5305);
      this.headers[2] = Aem.getString(5307);
      this.headers[3] = Aem.getString(5309);
      this.headers[4] = Aem.getString(5308);
      this.headers[5] = Aem.getString(5310);
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

   public class CartTableAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("CartTableScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(CartTableScreen.mainAction)) {
                  if (this.cmd[0].equals(DvdplayBase.HELP)) {
                     CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.HELP_MAIN_SCREEN));
                  } else if (this.cmd[0].equals(DvdplayBase.BACK)) {
                     CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, CartTableScreen.this.originalPointBefore));
                  } else if (this.cmd[0].equals(DvdplayBase.START_OVER)) {
                     CartTableScreen.aemContent.reset();
                     CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.MAIN_SCREEN));
                  } else if (this.cmd[0].equals(DvdplayBase.CHECKOUT)) {
                     boolean hasAdult = false;
                     CartTableScreen.this.nextScreen = DvdplayBase.CART_TABLE_SCREEN;
                     if (hasAdult) {
                        CartTableScreen.this.msg = new StringBuffer(DvdplayBase.MUST_BE_18_SCREEN)
                           .append(" Rent,")
                           .append(CartTableScreen.this.nextScreen)
                           .append(",")
                           .append(this.cmd[1]);
                        CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, CartTableScreen.this.msg.toString()));
                     } else {
                        CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "SwipePaymentCardScreen"));
                     }
                  } else if (this.cmd[0].equals("Add")) {
                     CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MovieSelectionScreen"));
                  } else if (this.cmd[0].equals("RemovePromoCode")) {
                     CartTableScreen.aemContent.removePromoCode();
                     CartTableScreen.this.update(CartTableScreen.this.originalPointBefore, DvdplayBase.CART_TABLE_SCREEN, "");
                     CartTableScreen.this.repaint();
                  } else if (this.cmd[0].equals("Remove")) {
                     CartTableScreen.aemContent.removeOrder(Integer.parseInt(this.cmd[1]));
                     CartTableScreen.this.update(CartTableScreen.this.originalPointBefore, DvdplayBase.CART_TABLE_SCREEN, "");
                     CartTableScreen.this.repaint();
                  } else if (this.cmd[0].equals("PromoCode")) {
                     CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.PROMO_CODE_SCREEN));
                  } else if (this.cmd[0].equals("Cart")) {
                     CartTableScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, DvdplayBase.CART_TABLE_SCREEN));
                  }
               }
            }
         } catch (Exception e) {
            Log.warning(e, DvdplayBase.CART_TABLE_SCREEN);
         } catch (Throwable e) {
            Log.error(e, DvdplayBase.CART_TABLE_SCREEN);
         }
      }
   }
}
