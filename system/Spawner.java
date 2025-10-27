package system;

import java.util.Random;

import entity.Fish;
import entity.Pink_fish;
import entity.Sword;
import main.Gamepanel;

public class Spawner {
    private final ObjectManager om;
    private final Random rand = new Random();
    private int tick = 0;

    public Spawner(ObjectManager om) {
        this.om = om;
    }

    public void update(Gamepanel gp) {
        tick++;
        if (tick % 40 == 0 && om.count() < 25) {
            boolean fromLeft = Math.random() < 0.5;
            int y = 100 + (int)(Math.random() * (gp.getScreenHeight() - 200)); // ไม่อยู่ขอบล่าง
            int size = 1;
            int x = fromLeft ? -20 : gp.getScreenWidth() - 30; // เริ่มจากใกล้ขอบมากขึ้น
            int dirX = fromLeft ? 1 : -1;
            int baseSpeed = Math.max(1, 5 - size); // เช่น ปลาใหญ่ช้า
            double randomFactor = 0.8 + Math.random() * 0.4; // 0.8x–1.2x
            int speed = (int)Math.round(baseSpeed * randomFactor);
            int roll = rand.nextInt(3);
            Fish f ;
            switch (roll) {
                case 0:
                    f = new Fish(x, y, size, speed, dirX);
                    break;
                case 1:
                    size+= 1 ;
                    f = new Pink_fish(x, y, size, speed, dirX);
                    break ;
                case 2:
                    size+= 2 ;
                    f = new Sword(x, y, size, speed, dirX) ;
                    break ;
                default:
                    f = new Fish(x, y, size, speed, dirX);
                    break;
            }
            om.add(f);
        }
    }
}