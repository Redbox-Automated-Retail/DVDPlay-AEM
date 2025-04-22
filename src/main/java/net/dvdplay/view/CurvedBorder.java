package net.dvdplay.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

public class CurvedBorder extends AbstractBorder {
   private Color wallColor = Color.gray;
   private int sinkLevel = 10;

   public CurvedBorder() {
   }

   public CurvedBorder(int lSinkLevel) {
      this.sinkLevel = lSinkLevel;
   }

   public CurvedBorder(Color lWall) {
      this.wallColor = lWall;
   }

   public CurvedBorder(int sinkLevel, Color wall) {
      this.sinkLevel = sinkLevel;
      this.wallColor = wall;
   }

   public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
      g.setColor(this.getWallColor());
      g.drawRoundRect(x, y, w - 1, h - 1, this.sinkLevel, this.sinkLevel);
   }

   public Insets getBorderInsets(Component c) {
      return new Insets(this.sinkLevel, this.sinkLevel, this.sinkLevel, this.sinkLevel);
   }

   public Insets getBorderInsets(Component c, Insets i) {
      i.left = i.right = i.bottom = i.top = this.sinkLevel;
      return i;
   }

   public boolean isBorderOpaque() {
      return true;
   }

   public int getSinkLevel() {
      return this.sinkLevel;
   }

   public Color getWallColor() {
      return this.wallColor;
   }
}
