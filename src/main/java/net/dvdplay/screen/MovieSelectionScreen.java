package net.dvdplay.screen;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import net.dvdplay.aem.Aem;
import net.dvdplay.base.AbstractContentBar;
import net.dvdplay.base.BaseActionListener;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.inventory.DiscIndex;
import net.dvdplay.inventory.InventoryException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.logger.Log;
import net.dvdplay.ui.ScreenProperties;
import net.dvdplay.ui.TextToRows;

public class MovieSelectionScreen extends AbstractContentBar {
   ActionListener movieSelectionAction;
   JPanel centerBar;
   JPanel posterPanel;
   JPanel categoryPanel;
   JPanel[] posterHolder;
   int pageNumber;
   int curCategoryPageNumber;
   JButton[] categoryButton;
   JButton[] labelButton1;
   JButton[] labelButton2;
   JButton[] posterButton;
   JPanel[] statusLabel;
   int curCategoryId;
   int movieListSize;
   int rowCount;
   int remainCol;
   int remainRow;
   int pageNeeded;
   int colIndex;
   int rowIndex;
   int index;
   int categoryListSize;
   int startingIndex;
   int genreSequenceId;
   int posterMaxRow = 3;
   int posterMaxCol = 5;
   int categoryMaxRow = 8;
   int categoryMaxCol = 1;
   int curCategoryPageNum = 1;
   int categoryPageCount = 0;
   boolean lastPage = false;
   String genrename;
   String tempId;
   String finalButtonText;
   TextToRows ttr;
   ImageIcon icon;
   File f;
   Image tempIcon;
   Image newImage;

   public MovieSelectionScreen(String prevScreen, String currScreen, String data) {
      try {
         this.pageNumber = AbstractContentBar.aemContent.getCurrentMovieSelectionPageNum();
         this.curCategoryId = AbstractContentBar.aemContent.getCurrentCategoryId();
         this.movieSelectionAction = new MovieSelectionScreen.MovieSelectionAction();
         this.setLayout(null);
         this.setBackground(ScreenProperties.getColor("White"));
         this.centerBar = new JPanel();
         this.posterPanel = new JPanel(null);
         this.categoryPanel = new JPanel(null);
         this.posterHolder = new JPanel[this.posterMaxCol * this.posterMaxRow];
         this.categoryButton = new JButton[this.categoryMaxRow * this.categoryMaxCol];
         this.labelButton1 = new JButton[this.posterMaxRow * this.posterMaxCol];
         this.labelButton2 = new JButton[this.posterMaxRow * this.posterMaxCol];
         this.posterButton = new JButton[this.posterMaxRow * this.posterMaxCol];
         this.statusLabel = new JPanel[this.posterMaxRow * this.posterMaxCol];
         this.createCategoryPanel();
         this.posterPanel.setBackground(ScreenProperties.getColor("White"));
         this.posterPanel.setBounds(310, 30, 710, 585);
         this.categoryPanel.setBounds(30, 30, 250, 600);
         this.categoryPanel.setBackground(ScreenProperties.getColor("White"));
         if (this.curCategoryId == -2) {
            this.genrename = Aem.getString(6303);
         } else if (this.curCategoryId == -1) {
            this.genrename = Aem.getString(6301);
         } else if (this.curCategoryId == -3) {
            this.genrename = Aem.getString(6302);
         } else {
            this.genrename = Aem.getGenreName(this.curCategoryId);
         }

         this.createTopBar(this.genrename, Aem.getString(1011), "Cart", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         this.topBar.addActionListener(this.movieSelectionAction);
         this.centerBar.add(this.posterPanel);
         this.centerBar.add(this.categoryPanel);
         this.centerBar.setBackground(ScreenProperties.getColor("White"));
         this.centerBar.setLayout(null);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.createBlackBottomBar(true, false, "", "", Aem.getString(1012), "Back", Aem.getString(1010) + " " + Aem.getGenreName(this.curCategoryId), "More");
         this.bottomBar.addActionListener(this.movieSelectionAction);
         this.add(this.bottomBar);
         this.add(this.topBar);
         this.add(this.centerBar);
         this.setBounds(0, 0, 1024, 768);
         this.setVisible(true);
         this.msg = new StringBuffer("* ").append("MovieSelectionScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("MovieSelectionScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   public void update(String prevScreen, String currScreen, String data) {
      try {
         AbstractContentBar.aemContent.setPrevScreen(prevScreen);
         AbstractContentBar.aemContent.setCurrentScreen(currScreen);
         if (data.equals("0")) {
            this.pageNumber = 1;
            this.curCategoryId = -1;
         } else {
            this.pageNumber = AbstractContentBar.aemContent.getCurrentMovieSelectionPageNum();
            this.curCategoryId = AbstractContentBar.aemContent.getCurrentCategoryId();
         }

         this.movieSelectionAction = new MovieSelectionScreen.MovieSelectionAction();
         this.centerBar.removeAll();
         this.posterPanel.removeAll();
         this.categoryPanel.removeAll();
         this.cleanup(this.statusLabel);
         this.cleanup(this.labelButton1);
         this.cleanup(this.labelButton2);
         this.cleanup(this.categoryButton);
         this.cleanup(this.posterButton);
         this.categoryButton = new JButton[this.categoryMaxRow * this.categoryMaxCol];
         this.labelButton1 = new JButton[this.posterMaxRow * this.posterMaxCol];
         this.labelButton2 = new JButton[this.posterMaxRow * this.posterMaxCol];
         this.posterButton = new JButton[this.posterMaxRow * this.posterMaxCol];
         this.statusLabel = new JPanel[this.posterMaxRow * this.posterMaxCol];
         this.createCategoryPanel();
         this.posterPanel.setBackground(ScreenProperties.getColor("White"));
         this.posterPanel.setBounds(310, 30, 710, 585);
         this.categoryPanel.setBounds(30, 30, 250, 600);
         this.categoryPanel.setBackground(ScreenProperties.getColor("White"));
         if (this.createPosterPanel()) {
            if (this.pageNumber > 1) {
               this.bottomBar
                  .setProperty(
                     ScreenProperties.getColor("Black"),
                     true,
                     false,
                     false,
                     "",
                     "",
                     Aem.getString(1012),
                     "Back",
                     Aem.getString(1010) + " " + Aem.getGenreName(this.curCategoryId),
                     "More"
                  );
            } else {
               this.bottomBar
                  .setProperty(
                     ScreenProperties.getColor("Black"),
                     true,
                     false,
                     false,
                     "",
                     "",
                     "",
                     "",
                     Aem.getString(1010) + " " + Aem.getGenreName(this.curCategoryId),
                     "More"
                  );
            }
         } else if (this.pageNumber > 1) {
            this.bottomBar
               .setProperty(
                  ScreenProperties.getColor("Black"),
                  true,
                  false,
                  false,
                  "",
                  "",
                  Aem.getString(1012),
                  "Back",
                  Aem.getString(1010) + " " + Aem.getGenreName(this.curCategoryId),
                  "More"
               );
         } else {
            this.bottomBar.setProperty(ScreenProperties.getColor("Black"), true, false, false, "", "", "", "", "", "");
         }

         if (this.curCategoryId == -2) {
            this.genrename = Aem.getString(6303);
         } else if (this.curCategoryId == -1) {
            this.genrename = Aem.getString(6301);
         } else if (this.curCategoryId == -3) {
            this.genrename = Aem.getString(6302);
         } else {
            this.genrename = Aem.getGenreName(this.curCategoryId);
         }

         if (AbstractContentBar.aemContent.getCartItemCount() > 0) {
            this.topBar.setProperty(this.genrename, Aem.getString(1011), "Cart", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         } else {
            this.topBar.setProperty(this.genrename, "", "", Aem.getString(1001), "Help", Aem.getString(1004), "StartOver");
         }

         this.centerBar.add(this.posterPanel);
         this.centerBar.add(this.categoryPanel);
         this.centerBar.setBackground(ScreenProperties.getColor("White"));
         this.centerBar.setLayout(null);
         this.centerBar.setBounds(0, 60, 1024, 630);
         this.msg = new StringBuffer("* ").append("MovieSelectionScreen").append(" from ").append(prevScreen);
         Aem.logDetailMessage(DvdplayLevel.INFO, this.msg.toString());
         AbstractContentBar.timer.stop();
         AbstractContentBar.timer.start();
      } catch (Exception var5) {
         this.msg = new StringBuffer("[").append("MovieSelectionScreen").append("]").append(var5.toString());
         Aem.logDetailMessage(DvdplayLevel.ERROR, this.msg.toString(), var5);
      }
   }

   private boolean createPosterPanel() {
      this.lastPage = false;
      if (this.curCategoryId == -1) {
         this.movieListSize = Aem.createStreetDatePickList(30);
      } else if (this.curCategoryId == -3) {
         this.movieListSize = Aem.createTopPickList(15);
      } else if (this.curCategoryId == -2) {
         this.movieListSize = Aem.createSortTitlePickList(999);
      } else {
         this.movieListSize = Aem.createGenrePickList(this.curCategoryId, 9999);
      }

      if (this.movieListSize > 0) {
         this.rowCount = this.movieListSize % this.posterMaxCol == 0 ? this.movieListSize / this.posterMaxCol : this.movieListSize / this.posterMaxCol + 1;
         this.remainCol = this.movieListSize % this.posterMaxCol == 0 ? this.posterMaxCol : this.movieListSize % this.posterMaxCol;
         this.remainRow = this.rowCount % this.posterMaxRow == 0 ? this.posterMaxRow : this.rowCount % this.posterMaxRow;
         BigDecimal rowCountBD = new BigDecimal((double)this.rowCount);
         BigDecimal posterMaxRowBD = new BigDecimal((double)this.posterMaxRow);
         BigDecimal a = rowCountBD.divide(posterMaxRowBD, 0);
         this.pageNeeded = a.toBigInteger().intValue();
         this.colIndex = this.posterMaxCol;
         this.rowIndex = this.posterMaxRow;
         if (this.pageNumber == this.pageNeeded) {
            this.rowIndex = this.remainRow;
            this.lastPage = true;
         }

         Aem.logDetailMessage(
            DvdplayLevel.FINE,
            "Total "
               + this.movieListSize
               + " rowCount "
               + this.rowCount
               + " pageNumber "
               + this.pageNumber
               + " pageNeeded "
               + this.pageNeeded
               + " RemainCol "
               + this.remainCol
               + " remainRow "
               + this.remainRow
         );

         for (int i = 0; i < this.rowIndex; i++) {
            if (i == this.rowIndex - 1 && this.lastPage) {
               this.colIndex = this.remainCol;
            }

            for (int j = 0; j < this.colIndex; j++) {
               if (j + i * this.posterMaxCol + (this.pageNumber - 1) * this.posterMaxCol * this.posterMaxRow < this.movieListSize) {
                  int tempId = j + i * this.posterMaxCol + (this.pageNumber - 1) * this.posterMaxCol * this.posterMaxRow;
                  if (this.curCategoryId == -1) {
                     this.posterHolder[j + i * this.rowIndex] = this.createPosterHolder(
                        Aem.getStreetDatePick(tempId).getSmallPoster(),
                        Aem.getStreetDatePick(tempId).getOriginalTitle(),
                        j + i * 5,
                        Aem.getStreetDatePick(tempId).getTitleDetailId(),
                        100,
                        140,
                        Aem.getStreetDatePick(tempId).getDiscStatusId()
                     );
                  } else if (this.curCategoryId == -3) {
                     this.posterHolder[j + i * this.rowIndex] = this.createPosterHolder(
                        Aem.getTopPick(tempId).getSmallPoster(),
                        Aem.getTopPick(tempId).getOriginalTitle(),
                        j + i * 5,
                        Aem.getTopPick(tempId).getTitleDetailId(),
                        100,
                        140,
                        Aem.getTopPick(tempId).getDiscStatusId()
                     );
                  } else if (this.curCategoryId == -2) {
                     this.posterHolder[j + i * this.rowIndex] = this.createPosterHolder(
                        Aem.getSortTitlePick(tempId).getSmallPoster(),
                        Aem.getSortTitlePick(tempId).getOriginalTitle(),
                        j + i * 5,
                        Aem.getSortTitlePick(tempId).getTitleDetailId(),
                        100,
                        140,
                        Aem.getSortTitlePick(tempId).getDiscStatusId()
                     );
                  } else {
                     this.posterHolder[j + i * this.rowIndex] = this.createPosterHolder(
                        Aem.getGenrePick(tempId).getSmallPoster(),
                        Aem.getGenrePick(tempId).getOriginalTitle(),
                        j + i * 5,
                        Aem.getGenrePick(tempId).getTitleDetailId(),
                        100,
                        140,
                        Aem.getGenrePick(tempId).getDiscStatusId()
                     );
                  }

                  this.posterHolder[j + i * this.rowIndex].setBounds(10 + j * 140, i * 195 + 10, 100, 170);
                  this.posterPanel.add(this.posterHolder[j + i * this.rowIndex]);
               }
            }
         }
      } else {
         this.lastPage = true;
      }

      return !this.lastPage;
   }

   private void createCategoryPanel() {
      this.categoryButton = new JButton[8];
      this.curCategoryPageNum = AbstractContentBar.aemContent.getCurrentCategoryPageNum();
      this.categoryListSize = Aem.createGenreList();
      this.index = 7;
      this.startingIndex = 0;
      this.categoryPageCount = (this.categoryListSize + 3) / 7;
      int categoryLastPageRemainCol = (this.categoryListSize + 3) % 7;
      if (categoryLastPageRemainCol > 0) {
         this.categoryPageCount++;
      }

      if (this.categoryPageCount == this.curCategoryPageNum && categoryLastPageRemainCol != 0) {
         this.index = categoryLastPageRemainCol;
      }

      Aem.logDetailMessage(
         DvdplayLevel.FINE,
         "Displaying Category "
            + this.curCategoryId
            + " curCatPgNum "
            + this.curCategoryPageNum
            + " CategoryPageCount "
            + this.categoryPageCount
            + " categorylastPageRemainCol "
            + categoryLastPageRemainCol
      );
      if (this.categoryListSize > 0) {
         if (this.curCategoryPageNum == 1) {
            this.categoryButton[0] = new JButton();
            this.categoryButton[0].setText(Aem.getString(6301));
            if (-1 == this.curCategoryId) {
               this.categoryButton[0].setBackground(ScreenProperties.getColor("Red"));
               this.categoryButton[0].setForeground(ScreenProperties.getColor("White"));
            } else {
               this.categoryButton[0].setBackground(ScreenProperties.getColor("White"));
               this.categoryButton[0].setForeground(ScreenProperties.getColor("Black"));
            }

            this.categoryButton[0].setBorder(new LineBorder(ScreenProperties.getColor("Black")));
            this.categoryButton[0].setFont(ScreenProperties.getFont("TopBarButton"));
            this.categoryButton[0].setHorizontalTextPosition(2);
            this.categoryButton[0].setVerticalTextPosition(0);
            this.categoryButton[0].setFocusPainted(false);
            this.categoryButton[0].setBounds(10, 10, 235, 60);
            this.categoryButton[0].setActionCommand("ChangeCategory -1");
            this.categoryButton[0].addActionListener(this.movieSelectionAction);
            this.categoryPanel.add(this.categoryButton[0]);
            this.categoryButton[1] = new JButton();
            this.categoryButton[1].setText(Aem.getString(6303));
            if (-2 == this.curCategoryId) {
               this.categoryButton[1].setBackground(ScreenProperties.getColor("Red"));
               this.categoryButton[1].setForeground(ScreenProperties.getColor("White"));
            } else {
               this.categoryButton[1].setBackground(ScreenProperties.getColor("White"));
               this.categoryButton[1].setForeground(ScreenProperties.getColor("Black"));
            }

            this.categoryButton[1].setBorder(new LineBorder(ScreenProperties.getColor("Black")));
            this.categoryButton[1].setFont(ScreenProperties.getFont("TopBarButton"));
            this.categoryButton[1].setHorizontalTextPosition(2);
            this.categoryButton[1].setVerticalTextPosition(0);
            this.categoryButton[1].setBounds(10, 80, 235, 60);
            this.categoryButton[1].setFocusPainted(false);
            this.categoryButton[1].setActionCommand("ChangeCategory -2");
            this.categoryButton[1].addActionListener(this.movieSelectionAction);
            this.categoryPanel.add(this.categoryButton[1]);
            this.categoryButton[2] = new JButton();
            this.categoryButton[2].setText(Aem.getString(6302));
            if (-3 == this.curCategoryId) {
               this.categoryButton[2].setBackground(ScreenProperties.getColor("Red"));
               this.categoryButton[2].setForeground(ScreenProperties.getColor("White"));
            } else {
               this.categoryButton[2].setBackground(ScreenProperties.getColor("White"));
               this.categoryButton[2].setForeground(ScreenProperties.getColor("Black"));
            }

            this.categoryButton[2].setBorder(new LineBorder(ScreenProperties.getColor("Black")));
            this.categoryButton[2].setFont(ScreenProperties.getFont("TopBarButton"));
            this.categoryButton[2].setHorizontalTextPosition(2);
            this.categoryButton[2].setVerticalTextPosition(0);
            this.categoryButton[2].setFocusPainted(false);
            this.categoryButton[2].setBounds(10, 150, 235, 60);
            this.categoryButton[2].setActionCommand("ChangeCategory -3");
            this.categoryButton[2].addActionListener(this.movieSelectionAction);
            this.categoryPanel.add(this.categoryButton[2]);
            this.startingIndex = 3;
         }

         for (int i = this.startingIndex; i < this.index; i++) {
            if (this.curCategoryPageNum == 1) {
               this.genreSequenceId = i - 3;
            } else if (this.curCategoryPageNum == 2) {
               this.genreSequenceId = i + 4;
            } else {
               this.genreSequenceId = i + (this.curCategoryPageNum - 2) * 7 + 4;
            }

            Aem.logDetailMessage(
               DvdplayLevel.FINE, "curCategoryPageNum : " + this.curCategoryPageNum + ", GenreSequenceID : " + this.genreSequenceId + ", Index : " + i
            );
            this.tempId = Aem.getGenre(this.genreSequenceId).getGenreName();
            int catId = Aem.getGenre(this.genreSequenceId).getGenreId();
            this.categoryButton[i] = new JButton();
            this.categoryButton[i].setText(this.tempId);
            if (catId == this.curCategoryId) {
               this.categoryButton[i].setBackground(ScreenProperties.getColor("Red"));
               this.categoryButton[i].setForeground(ScreenProperties.getColor("White"));
            } else {
               this.categoryButton[i].setBackground(ScreenProperties.getColor("White"));
               this.categoryButton[i].setForeground(ScreenProperties.getColor("Black"));
            }

            this.categoryButton[i].setBorder(new LineBorder(ScreenProperties.getColor("Black")));
            this.categoryButton[i].setFont(ScreenProperties.getFont("TopBarButton"));
            this.categoryButton[i].setHorizontalTextPosition(2);
            this.categoryButton[i].setFocusPainted(false);
            this.categoryButton[i].setVerticalTextPosition(0);
            this.categoryButton[i].setBounds(10, 10 + i * 70, 235, 60);
            this.categoryButton[i].setActionCommand("ChangeCategory " + catId);
            this.categoryButton[i].addActionListener(this.movieSelectionAction);
            this.categoryPanel.add(this.categoryButton[i]);
         }

         this.finalButtonText = "";
         if (this.categoryListSize > 4) {
            this.categoryButton[this.index] = new JButton();
            this.finalButtonText = Aem.getString(1010);
            this.categoryButton[this.index].setIcon(ScreenProperties.getImage("RedDownArrowonWhite.Logo.Icon"));
            this.categoryButton[this.index].setPressedIcon(ScreenProperties.getImage("OrangeArrowDown.Icon"));
            this.categoryButton[this.index].setText(Aem.getString(1010));
            this.categoryButton[this.index].setBackground(ScreenProperties.getColor("White"));
            this.categoryButton[this.index].setBorder(new LineBorder(ScreenProperties.getColor("Black")));
            this.categoryButton[this.index].setFont(ScreenProperties.getFont("TopBarButton"));
            this.categoryButton[this.index].setForeground(ScreenProperties.getColor("Black"));
            this.categoryButton[this.index].setHorizontalTextPosition(2);
            this.categoryButton[this.index].setVerticalTextPosition(0);
            this.categoryButton[this.index].setFocusPainted(false);
            this.categoryButton[this.index].setBounds(10, 10 + this.index * 70, 235, 60);
            this.categoryButton[this.index].setActionCommand("MoreCategory");
            this.categoryButton[this.index].addActionListener(this.movieSelectionAction);
            this.categoryPanel.add(this.categoryButton[this.index]);
         }
      }
   }

   private void cleanup(JButton[] button) {
      for (int i = 0; i < button.length; i++) {
         button[i] = null;
      }
   }

   private void cleanup(JLabel[] label) {
      for (int i = 0; i < label.length; i++) {
         label[i] = null;
      }
   }

   private void cleanup(JPanel[] label) {
      for (int i = 0; i < label.length; i++) {
         label[i] = null;
      }
   }

   private JPanel createPosterHolder(String poster, String str, int id, int discId, int width, int height, int status) {
      JPanel tempPanel = new JPanel(null);
      this.ttr = new TextToRows(str, 16);
      this.labelButton1[id] = this.createTitleButton(this.ttr.getRow(0), discId);
      this.labelButton1[id].setBounds(0, height, width + 10, 15);
      if (this.ttr.getRowSize() > 0) {
         this.labelButton2[id] = this.createTitleButton(this.ttr.getRow(1), discId);
         this.labelButton2[id].setBounds(0, height + 15, width + 10, 15);
         tempPanel.add(this.labelButton2[id]);
      }

      this.f = new File("c:\\aem\\content\\posters\\" + poster);
      if (this.f.exists()) {
         this.icon = new ImageIcon("c:\\aem\\content\\posters\\" + poster);
      } else {
         Aem.logDetailMessage(DvdplayLevel.FINE, "c:\\aem\\content\\posters\\" + poster + " not found, use default image");
         this.icon = ScreenProperties.getImage("Default.SmallPoster.NotFound");
      }

      this.posterButton[id] = new JButton(this.icon);
      this.posterButton[id].setBorder(BorderFactory.createLineBorder(ScreenProperties.getColor("Black")));
      this.posterButton[id].setBounds(0, 0, width, height);
      this.posterButton[id].setActionCommand("DvdDescription " + discId);
      this.posterButton[id].addActionListener(this.movieSelectionAction);
      this.posterButton[id].setFocusPainted(false);
      this.createStatusLabel(status, id);
      tempPanel.setBackground(ScreenProperties.getColor("White"));
      tempPanel.add(this.labelButton1[id]);
      tempPanel.add(this.statusLabel[id]);
      tempPanel.add(this.posterButton[id]);
      tempPanel.setVisible(true);
      return tempPanel;
   }

   private JButton createTitleButton(String str, int id) {
      JButton temp = new JButton(str);
      temp.setVisible(true);
      temp.setBorder(new LineBorder(ScreenProperties.getColor("White")));
      temp.setBackground(ScreenProperties.getColor("White"));
      temp.setFont(ScreenProperties.getFont("MovieSelectionTitle"));
      temp.setHorizontalTextPosition(0);
      temp.setHorizontalAlignment(0);
      temp.setVerticalTextPosition(1);
      temp.setForeground(ScreenProperties.getColor("Black"));
      temp.setActionCommand("DvdDescription " + id);
      temp.setFocusPainted(false);
      temp.addActionListener(this.movieSelectionAction);
      return temp;
   }

   public void createStatusLabel(int status, int id) {
      this.statusLabel[id] = new JPanel();
      new String();
      if (status == 1) {
         this.statusLabel[id].setBackground(ScreenProperties.getColor("Red"));
         String statusText = Aem.getString(6403);
         JLabel d = new JLabel(statusText);
         d.setHorizontalAlignment(0);
         d.setForeground(ScreenProperties.getColor("White"));
         d.setFont(ScreenProperties.getFont("MovieSelectionStatus"));
         d.setBounds(0, 0, 100, 20);
         d.setVisible(true);
         this.statusLabel[id].add(d);
         this.statusLabel[id].setBounds(0, 0, 100, 20);
         this.posterButton[id].setBounds(0, 20, 100, 120);
         this.statusLabel[id].setVisible(true);
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

   public class MovieSelectionAction extends BaseActionListener {
      public void actionPerformed(ActionEvent ae) {
         try {
            this.setScreenName("MovieSelectionScreen");
            super.actionPerformed(ae);
            if (this.isCurrentScreenAction) {
               this.logCommandFlow();
               if (!this.checkRobotAndLogoClick(MovieSelectionScreen.mainAction)) {
                  if (this.cmd[0].equals("More")) {
                     if (MovieSelectionScreen.aemContent.getCurrentMovieSelectionPageNum() == MovieSelectionScreen.this.pageNeeded) {
                        MovieSelectionScreen.aemContent.setCurrentMovieSelectionPageNum(1);
                     } else {
                        MovieSelectionScreen.aemContent
                           .setCurrentMovieSelectionPageNum(MovieSelectionScreen.aemContent.getCurrentMovieSelectionPageNum() + 1);
                     }

                     MovieSelectionScreen.aemContent.setCurrentCategoryId(MovieSelectionScreen.this.curCategoryId);
                     MovieSelectionScreen.this.update("MovieSelectionScreen", "MovieSelectionScreen", "");
                     MovieSelectionScreen.this.repaint();
                  } else if (this.cmd[0].equals("Back")) {
                     if (MovieSelectionScreen.aemContent.getCurrentMovieSelectionPageNum() > 1) {
                        MovieSelectionScreen.aemContent
                           .setCurrentMovieSelectionPageNum(MovieSelectionScreen.aemContent.getCurrentMovieSelectionPageNum() - 1);
                        MovieSelectionScreen.this.update("MainScreen", "MovieSelectionScreen", "");
                        MovieSelectionScreen.this.repaint();
                     } else {
                        MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, MovieSelectionScreen.aemContent.getPrevScreen()));
                     }
                  } else if (this.cmd[0].equals("StartOver")) {
                     MovieSelectionScreen.aemContent.reset();
                     MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MainScreen"));
                  } else if (this.cmd[0].equals("Help")) {
                     MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "HelpMainScreen"));
                  } else if (this.cmd[0].equals("ChangeCategory")) {
                     if (Integer.parseInt(this.cmd[1]) == 10) {
                        MovieSelectionScreen.aemContent.setCurrentCategoryId(10);
                        MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "MustBe18Screen Browse,MovieSelectionScreen"));
                     } else {
                        MovieSelectionScreen.aemContent.setCurrentCategoryId(Integer.parseInt(this.cmd[1]));
                        MovieSelectionScreen.aemContent.setCurrentMovieSelectionPageNum(1);
                        MovieSelectionScreen.this.update("MovieSelectionScreen", "MovieSelectionScreen", "");
                        MovieSelectionScreen.this.repaint();
                     }
                  } else if (this.cmd[0].equals("MoreCategory")) {
                     MovieSelectionScreen.aemContent.setCurrentCategoryId(MovieSelectionScreen.this.curCategoryId);
                     if (MovieSelectionScreen.this.curCategoryPageNum == MovieSelectionScreen.this.categoryPageCount) {
                        MovieSelectionScreen.aemContent.setCurrentCategoryPageNum(1);
                     } else {
                        MovieSelectionScreen.aemContent.setCurrentCategoryPageNum(MovieSelectionScreen.aemContent.getCurrentCategoryPageNum() + 1);
                     }

                     MovieSelectionScreen.this.update("MovieSelectionScreen", "MovieSelectionScreen", "");
                     MovieSelectionScreen.this.repaint();
                  } else if (this.cmd[0].equals("DvdDescription")) {
                     try {
                        Aem.getDiscIndex();
                        Aem.getPricing(DiscIndex.getDiscIndexItemByTitleDetailId(Integer.parseInt(this.cmd[1])).getPriceOptionId()).getRentalPrice();
                        MovieSelectionScreen.aemContent.setCurrentDvdDescriptionDiscId(Integer.parseInt(this.cmd[1]));
                        if (3 == Aem.getTitleTypeId()) {
                           MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "GameDescriptionScreen"));
                        } else {
                           MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "DvdDescriptionScreen"));
                        }
                     } catch (InventoryException var4) {
                        MovieSelectionScreen.aemContent.setCurrentDvdDescriptionDiscId(Integer.parseInt(this.cmd[1]));
                        MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4010,DvdDescriptionScreen"));
                     } catch (DvdplayException var5) {
                        MovieSelectionScreen.aemContent.setCurrentDvdDescriptionDiscId(Integer.parseInt(this.cmd[1]));
                        MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "ErrorScreen 4010,MovieSelectionScreen"));
                     }
                  } else if (this.cmd[0].equals("Cart")) {
                     MovieSelectionScreen.mainAction.actionPerformed(new ActionEvent(this, 1001, "CartTableScreen"));
                  }
               }
            }
         } catch (Exception var6) {
            Log.warning(var6, "MovieSelectionScreen");
         } catch (Throwable var7) {
            Log.error(var7, "MovieSelectionScreen");
         }
      }
   }
}
