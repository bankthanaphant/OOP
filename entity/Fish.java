package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.Gamepanel;

public class Fish extends entity {
   protected int size;
   protected int value;
   protected boolean isPredater;
   protected double scaleX = 1.0;
   protected double scaleY = 1.0;

   private int dirX = -1;
   private double fx, fy;
   private double waveT = Math.random() * Math.PI * 2;
   private double speedJitter = (Math.random() * 0.4) - 0.2;

   public Fish(int x, int y, int size, int speed, int dirX) {
      this.x = x;
      this.y = y;
      this.size = size;
      this.speed = speed;
      this.isPredater = false;
      this.value = Math.max(1, size); // แต้มพื้นฐานตามขนาด
      this.dirX = (dirX == 0 ? -1 : (dirX > 0 ? 1 : -1));
      this.direction = dirX < 0 ? "left" : "right";
      solidArea = new Rectangle();
      this.solidArea.x = 8;
      this.solidArea.y = 8;
      this.solidArea.width = Math.max(12, getDrawW() - 16);
      this.solidArea.height = Math.max(10, getDrawH() - 16);
      this.fx = this.x;
      this.fy = this.y;
      this.getFishImg();
   }

   public void update(Gamepanel gp) {
      // 1) เคลื่อนที่แกน X แบบปล่อยไหล (ไม่เด้งขอบ)
      double vx = dirX * (speed + speedJitter);
      fx += vx;

      // 2) ว่ายเป็นคลื่นในแนวตั้ง
      waveT += 0.08; // ปรับความถี่คลื่นได้
      fy += Math.sin(waveT) * (6 + size * 2) * 0.15;

      // 3) ซิงก์กลับมาเป็น int สำหรับวาด/ชน
      this.x = (int) Math.round(fx);
      this.y = (int) Math.round(fy);

      // 4) อัปเดตทิศสปริตตามทิศจริงของความเร็ว
      this.direction = (vx < 0) ? "left" : "right";

      // 6) แอนิเมชันครีบ
      spriteCounter = (spriteCounter + 1) % 40;
      if (spriteCounter == 0) {
         spriteNum = (spriteNum == 1) ? 2 : 1;
      }
   }

   public int getDrawW() {
      return (int) ((30 + size * 10) * scaleX);
   }

   public int getDrawH() {
      return (int) ((20 + size * 8) * scaleY);
   }

   public int getSize() {
      return size;
   }

   public int getValue() {
      return value;
   }

   public boolean isPredator() {
      return isPredater;
   }

   public double getScaleX() {
      return scaleX;
   }

   public double getScaleY() {
      return scaleY;
   }

   public void draw(Graphics2D g2) {
      BufferedImage img = getCurrentImage();

      if (img != null) {
         g2.drawImage(img, this.x, this.y, getDrawW(), getDrawH(), null);
      } else {
         g2.setColor(Color.CYAN);
         g2.fillRect(this.x, this.y, getDrawW(), getDrawH());
         g2.setColor(Color.BLUE);
         g2.drawRect(this.x, this.y, getDrawW(), getDrawH());
      }
      drawSolidArea(g2);
   }

   protected BufferedImage getCurrentImage() {
      switch (this.direction) {
         case "left":
            return (spriteNum == 1) ? left1 : left2;
         case "right":
            return (spriteNum == 1) ? right1 : right2;
         default:
            return right1;
      }
   }

   public void getFishImg() {
      try {
         this.left1 = ImageIO.read(this.getClass().getResourceAsStream("/Image/Fish_walk_left1.png"));
         this.left2 = ImageIO.read(this.getClass().getResourceAsStream("/Image/Fish_walk_left2.png"));
         this.right1 = ImageIO.read(this.getClass().getResourceAsStream("/Image/Fish_walk_right1.png"));
         this.right2 = ImageIO.read(this.getClass().getResourceAsStream("/Image/Fish_walk_right2.png"));
      } catch (Exception e) {
         System.err.println("Error loading fish images:");
         e.printStackTrace();
      }
   }

   public void drawSolidArea(Graphics2D g2) {
      if (solidArea != null) {
         g2.setColor(Color.RED);
         g2.drawRect(
               x + solidArea.x,
               y + solidArea.y,
               solidArea.width,
               solidArea.height);
      }
   }
}
