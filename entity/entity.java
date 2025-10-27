package entity;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class entity {
   public int x;
   public int y;
   public int speed;
   public BufferedImage left1, left2, right1, right2;
   public String direction;

   public int spriteCounter = 0 ;
   public int spriteNum = 1 ;

   public Rectangle solidArea ;
    public int getSolidAreaX() {
      return x + solidArea.x;
   }
   
   public int getSolidAreaY() {
      return y + solidArea.y;
   }
   
   public int getSolidAreaWidth() {
      return solidArea.width;
   }
   
   public int getSolidAreaHeight() {
      return solidArea.height;
   }
}