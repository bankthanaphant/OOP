package system;

import java.util.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Fish;
import entity.player;
import main.Gamepanel;

public class ObjectManager {
    public final List<Fish> fishes = new ArrayList<>();

    private static final int DESPAWN_OFFSCREEN_FRAMES = 90; // อยู่นอกจอเกินนี้จะถูกลบ
    private final Map<Fish, Integer> offscreenCounters = new IdentityHashMap<>();

    public void add(Fish f) { fishes.add(f); }
    public int count() { return fishes.size(); }

    public void updateAll(Gamepanel gp) {
        for (int i = fishes.size() - 1; i >= 0; --i) {
            Fish f = fishes.get(i);
            f.update(gp);

            if (checkCollision(f, gp.player)) {
                handleCollision(f, gp.player, i, gp);
            }

            boolean visible = isVisibleOnScreen(f, gp);

            if (visible) {
                offscreenCounters.remove(f);
            } else {
                int n = offscreenCounters.getOrDefault(f, 0) + 1;
                offscreenCounters.put(f, n);

                if (n > DESPAWN_OFFSCREEN_FRAMES) {
                    offscreenCounters.remove(f);
                    fishes.remove(i);
                }
            }
        }
    }

    private boolean checkCollision(Fish fish, player player) {
        if (fish.solidArea == null || player.solidArea == null) {
            return false;
        }
        
        Rectangle fishBounds = new Rectangle(
            fish.x + fish.solidArea.x,
            fish.y + fish.solidArea.y,
            fish.solidArea.width,
            fish.solidArea.height
        );
        
        Rectangle playerBounds = new Rectangle(
            player.x + player.solidArea.x,
            player.y + player.solidArea.y,
            player.solidArea.width,
            player.solidArea.height
        );
        
        return fishBounds.intersects(playerBounds);
    }

    private void handleCollision(Fish fish, player player, int fishIndex, Gamepanel gp) {
        if (player.canEat(fish)) {
            player.eatFish(fish);
            fishes.remove(fishIndex);
            offscreenCounters.remove(fish);
            gp.playSE(1);
            System.out.println("Player ate a fish! Size: " + fish.size);
        } 
        // ปลากินผู้เล่นได้ (ถ้าเป็น predator หรือใหญ่กว่า) // Game Over หรือเสียชีวิต
        else if (fish.isPredater || fish.size > player.getPlayerSize()) {
            System.out.println("Player was eaten by fish! Game Over");
            gp.setGameState(Gamepanel.Gamestate.GAME_OVER);
            gp.stopMusic();
            gp.playSE(3);
        }
    }

    public void drawAll(Graphics2D g2) {
        for (Fish f : fishes) {
            f.draw(g2);
        }
    }

    // === Helper ===
    private boolean isVisibleOnScreen(Fish f, Gamepanel gp) {
        int W = gp.getScreenWidth();
        int H = gp.getScreenHeight();

        // ใช้ขนาดจริงจาก Fish
        int drawW = f.getDrawW();
        int drawH = f.getDrawH();

        int left = f.x;
        int right = f.x + drawW;
        int top = f.y;
        int bottom = f.y + drawH;

        boolean overlapX = right >= 0 && left <= W;
        boolean overlapY = bottom >= 0 && top <= H;
        return overlapX && overlapY;
    }
}
