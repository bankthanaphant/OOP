package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import main.Gamepanel;
import main.KeyHandler;

public class player extends entity {
   private Gamepanel gamepanel;
   private KeyHandler keyHandler;
   private int score = 0;
   private int level = 1;
   private int exp = 0;
   private int expToNextLevel = 10;
   private int baseWidth = 70;
   private int baseHeight = 70;
   private double scale = 1.0;

   public player(Gamepanel gamepanel, KeyHandler keyHandler) {
      this.gamepanel = gamepanel;
      this.keyHandler = keyHandler;
      this.setDefaultValues();
      this.getPlayerImg();
      this.direction = "left";

      updateSize();
   }

   public void setDefaultValues() {
      this.x = (this.gamepanel.getScreenWidth() - this.gamepanel.getTileSize()) / 2;
      this.y = (this.gamepanel.getScreenHeight() - this.gamepanel.getTileSize()) / 2;
      this.speed = 6;
   }

   public void eatFish(Fish fish) {
      if (canEat(fish)) {
         this.score += fish.value;
         this.exp += fish.value;

         // เลเวลอัพ
         if (exp >= expToNextLevel) {
            levelUp();
         }

         System.out.println("Ate fish! Score: " + score + " EXP: " + exp + "/" + expToNextLevel);
      }
   }

   public boolean canEat(Fish fish) {
      return this.level >= fish.size;
   }

   private void levelUp() {
      this.level++;
      this.exp -= expToNextLevel;
      this.expToNextLevel = (int) (expToNextLevel * 1.5);
      gamepanel.playSE(2);
      updateSize();

      System.out.println("Level Up! Now level " + level + " Size: " + getCurrentWidth() + "x" + getCurrentHeight());
   }

   // ⭐⭐ อัพเดทขนาดและ solidArea ตามเลเวล ⭐⭐
   private void updateSize() {
      this.scale = 1.0 + (level - 1) * 0.5;

      // อัพเดท solidArea ให้เหมาะสมกับขนาดใหม่
      if (solidArea == null) {
         solidArea = new Rectangle();
      }

      solidArea.x = (int) (19 * scale); // offset จาก x
      solidArea.y = (int) (10 * scale); // offset จาก y
      solidArea.width = (int) (32 * scale);
      solidArea.height = (int) (40 * scale);
   }

   public int getCurrentWidth() {
      return (int) (baseWidth * scale);
   }

   public int getCurrentHeight() {
      return (int) (baseHeight * scale);
   }

   public int getPlayerSize() {
      return level;
   }

   public int getScore() {
      return score;
   }

   public int getLevel() {
      return level;
   }

   public int getExp() {
      return exp;
   }

   public int getExpToNextLevel() {
      return expToNextLevel;
   }

   public void getPlayerImg() {
      try {
         this.left1 = ImageIO.read(this.getClass().getResourceAsStream("/Image/walk_left1.png"));
         this.left2 = ImageIO.read(this.getClass().getResourceAsStream("/Image/walk_left2.png"));
         this.right1 = ImageIO.read(this.getClass().getResourceAsStream("/Image/walk_right1.png"));
         this.right2 = ImageIO.read(this.getClass().getResourceAsStream("/Image/walk_right2.png"));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void update() {
      int deltaX = 0;
      int deltaY = 0;

      if (this.keyHandler.upPressed) {
         --deltaY;
      }
      if (this.keyHandler.downPressed) {
         ++deltaY;
      }
      if (this.keyHandler.leftPressed) {
         this.direction = "left";
         --deltaX;
      }
      if (this.keyHandler.rightPressed) {
         this.direction = "right";
         ++deltaX;
      }

      if (deltaX != 0 || deltaY != 0) {
         double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
         double normalizedX = deltaX / length;
         double normalizedY = deltaY / length;

         int newX = (int) (this.x + normalizedX * this.speed);
         int newY = (int) (this.y + normalizedY * this.speed);

         this.x = clampXWithSolidArea(newX);
         this.y = clampYWithSolidArea(newY);
      }

      spriteCounter++;
      if (spriteCounter > 20) {
         spriteNum = (spriteNum == 1) ? 2 : 1;
         spriteCounter = 0;
      }
   }

   private int clampXWithSolidArea(int newX) {
      int solidAreaLeft = newX + solidArea.x;
      int solidAreaRight = solidAreaLeft + solidArea.width;
      int screenWidth = gamepanel.getScreenWidth();

      if (solidAreaLeft < 0) {
         return -solidArea.x;
      }

      if (solidAreaRight > screenWidth) {
         return screenWidth - solidArea.width - solidArea.x;
      }
      return newX;
   }

   private int clampYWithSolidArea(int newY) {
      int solidAreaTop = newY + solidArea.y;
      int solidAreaBottom = solidAreaTop + solidArea.height;
      int screenHeight = gamepanel.getScreenHeight();

      if (solidAreaTop < 0) {
         return -solidArea.y;
      }

      if (solidAreaBottom > screenHeight) {
         return screenHeight - solidArea.height - solidArea.y;
      }

      return newY;
   }

   public void draw(Graphics2D g2) {
      BufferedImage img = getCurrentImage();

      if (img != null) {
         g2.drawImage(img, this.x, this.y, getCurrentWidth(), getCurrentHeight(), null);
      }
      drawSolidArea(g2); // debug
      drawPlayerBounds(g2); // debug
   }

   private BufferedImage getCurrentImage() {
      switch (this.direction) {
         case "left":
            return (spriteNum == 1) ? left1 : left2;
         case "right":
            return (spriteNum == 1) ? right1 : right2;
         default:
            return right1;
      }
   }

   // วาด solidArea สำหรับดูพื้นที่ collision
   private void drawSolidArea(Graphics2D g2) {
      g2.setColor(Color.RED);
      g2.drawRect(
            this.x + solidArea.x,
            this.y + solidArea.y,
            solidArea.width,
            solidArea.height);
   }

   // ⭐⭐ วาดขอบเขตผู้เล่น (สำหรับดูขนาด) ⭐⭐
   private void drawPlayerBounds(Graphics2D g2) {
      g2.setColor(Color.GREEN);
      g2.drawRect(this.x, this.y, getCurrentWidth(), getCurrentHeight());
   }

   // ⭐⭐ เพิ่ม method สำหรับ ObjectManager ใช้ตรวจสอบขนาด ⭐⭐
   public int getDrawW() {
      return getCurrentWidth();
   }

   public int getDrawH() {
      return getCurrentHeight();
   }
}