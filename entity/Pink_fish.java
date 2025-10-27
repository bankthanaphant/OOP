package entity;

import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.Gamepanel;

public class Pink_fish extends Fish {

   private final Random rng = new Random();
   private double px, py; // ตำแหน่งแบบ double (กัน bias การปัดเศษ)
   private double vx, vy; // ความเร็วแบบเวกเตอร์
   private double maxSpeed; // เพดานความเร็วรวม
   private double wanderAngle; // มุมสำหรับ wander
   private double waveT; // เวลา/เฟสสำหรับคลื่น
   private final double wavePhase; // เฟสคลื่นสุ่มต่อปลา
   private final double baseJitter; // สุ่มความเร็วพื้นฐานต่อปลา

   public Pink_fish(int x, int y, int size, int speed, int dirX) {
      super(x, y, size, speed, dirX);
      this.scaleX = 2;
      this.scaleY = 2;

      // ตั้งทิศเริ่ม
      this.direction = (dirX < 0) ? "left" : "right";

      // ตำแหน่งเริ่มแบบ double
      this.px = x;
      this.py = y;

      // สปีดพื้นฐาน + สุ่มเล็กน้อยต่อปลา
      this.baseJitter = (rng.nextDouble() * 0.6) - 0.3; // -0.3..0.3
      this.maxSpeed = Math.max(1.6, speed + baseJitter);

      // ให้เริ่มวิ่งตามทิศทางที่รับมา
      this.vx = (dirX < 0 ? -1.0 : 1.0) * maxSpeed * (0.7 + rng.nextDouble() * 0.6);
      this.vy = (rng.nextDouble() - 0.5) * 0.3;

      // สุ่มเฟสคลื่นและมุมเริ่ม
      this.wavePhase = rng.nextDouble() * Math.PI * 2.0;
      this.waveT = rng.nextDouble() * Math.PI * 2.0;
      this.wanderAngle = rng.nextDouble() * Math.PI * 2.0;
      this.solidArea = new Rectangle();
      this.solidArea.x = 15;
      this.solidArea.y = 12; 
      this.solidArea.width = Math.max(20, getDrawW() - 30);
      this.solidArea.height = Math.max(16, getDrawH() - 24);
      loadPinkSprites(); // โหลดรูปเฉพาะของ Pink_fish
   }

   private void loadPinkSprites() {
      try {
         this.left1 = ImageIO.read(getClass().getResourceAsStream("/Image/pink_left1.png"));
         this.left2 = ImageIO.read(getClass().getResourceAsStream("/Image/pink_left2.png"));
         this.right1 = ImageIO.read(getClass().getResourceAsStream("/Image/pink_right1.png"));
         this.right2 = ImageIO.read(getClass().getResourceAsStream("/Image/pink_right2.png"));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void update(Gamepanel gp) {
      // 1) Wander steering (สุ่มมุมนิด ๆ ต่อเฟรม แล้วค่อย ๆ เปลี่ยนทิศทาง)
      wanderAngle += (rng.nextDouble() - 0.5) * 0.25;
      double steerX = Math.cos(wanderAngle) * 0.06;
      double steerY = Math.sin(wanderAngle) * 0.06;

      vx += steerX;
      vy += steerY;

      // 2) คลื่นเบา ๆ (รู้สึกเหมือนน้ำไหล)
      waveT += 0.07;
      vy += Math.sin(waveT + wavePhase) * 0.12;

      // 3) จำกัดความเร็วรวม (normalize ให้ไม่พุ่ง)
      double spd = Math.hypot(vx, vy);
      double cap = maxSpeed;
      if (spd > cap) {
         vx = vx / spd * cap;
         vy = vy / spd * cap;
      }

      // 4) อัปเดตตำแหน่ง
      px += vx;
      py += vy;
      this.x = (int) Math.round(px);
      this.y = (int) Math.round(py);

      // 5) อัปเดตทิศของสไปร์ตตาม vx จริง
      this.direction = (vx < 0) ? "left" : "right";

      // 7) แอนิเมชันครีบ
      spriteCounter = (spriteCounter + 1) % 24;
      if (spriteCounter == 0)
         spriteNum = (spriteNum == 1) ? 2 : 1;
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
