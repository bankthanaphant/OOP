package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class entity {
   protected int x;
   protected int y;
   protected int speed;
   protected BufferedImage left1, left2, right1, right2;
   protected String direction;

   protected int spriteCounter = 0;
   protected int spriteNum = 1;

   protected Rectangle solidArea;

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public int getSpeed() {
      return speed;
   }

   public String getDirection() {
      return direction;
   }

   public Rectangle getSolidArea() {
      return solidArea;
   }

   public int getSpriteCounter() {
      return spriteCounter;
   }

   public int getSpriteNum() {
      return spriteNum;
   }
}