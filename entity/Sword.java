package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Gamepanel;

public class Sword extends Fish{
    public Sword(int x, int y, int size, int speed, int dirX){
        super(x, y, size, speed, dirX);
        this.scaleX = 3;
        this.scaleY = 3;
        this.direction = (dirX < 0) ? "left" : "right";
        this.solidArea = new Rectangle();
        this.solidArea.x = 15;
        this.solidArea.y = 12; 
        this.solidArea.width = Math.max(20, getDrawW() - 30);
        this.solidArea.height = Math.max(16, getDrawH() - 24);
        loadSwordSprites();
    }
    public void update(Gamepanel gp){
        super.update(gp);
    }


    private void loadSwordSprites() {
      try {
         this.left1 = ImageIO.read(getClass().getResourceAsStream("/Image/Sword_left1.png"));
         this.left2 = ImageIO.read(getClass().getResourceAsStream("/Image/Sword_left2.png"));
         this.right1 = ImageIO.read(getClass().getResourceAsStream("/Image/Sword_right1.png"));
         this.right2 = ImageIO.read(getClass().getResourceAsStream("/Image/Sword_right2.png"));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public void draw(Graphics2D g2) {
      BufferedImage img = getCurrentImage();
      int dw = this.getDrawW(), dh = this.getDrawH();
      if (img != null) {
         g2.drawImage(img, this.x, this.y, dw, dh, null);
      } else {
         g2.fillRect(this.x, this.y, dw, dh);
      }
      drawSolidArea(g2) ;
   }
   
   public void drawSolidArea(Graphics2D g2) {
      if (solidArea != null) {
         g2.setColor(Color.RED);
         g2.drawRect(x + solidArea.x,y + solidArea.y,solidArea.width,solidArea.height);
      }
   }
}
