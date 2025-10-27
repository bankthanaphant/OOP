package system;

import java.util.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Fish;
import entity.player;
import main.Gamepanel;

public class ObjectManager {
    private final List<Fish> fishes = new ArrayList<>();

    private static final int DESPAWN_OFFSCREEN_FRAMES = 90; // อยู่นอกจอเกินนี้จะถูกลบ
    private final Map<Fish, Integer> offscreenCounters = new IdentityHashMap<>();

    public void add(Fish f) {
        fishes.add(f);
    }

    public int count() {
        return fishes.size();
    }

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
        if (fish.getSolidArea() == null || player.getSolidArea() == null) {
            return false;
        }

        Rectangle fishBounds = new Rectangle(
                fish.getX() + fish.getSolidArea().x,
                fish.getY() + fish.getSolidArea().y,
                fish.getSolidArea().width,
                fish.getSolidArea().height);

        Rectangle playerBounds = new Rectangle(
                player.getX() + player.getSolidArea().x,
                player.getY() + player.getSolidArea().y,
                player.getSolidArea().width,
                player.getSolidArea().height);

        return fishBounds.intersects(playerBounds);
    }

    private void handleCollision(Fish fish, player player, int fishIndex, Gamepanel gp) {
        if (player.canEat(fish)) {
            player.eatFish(fish);
            fishes.remove(fishIndex);
            offscreenCounters.remove(fish);
            gp.playSE(1);
            System.out.println("Player ate a fish! Size: " + fish.getSize());
        }
        // ปลากินผู้เล่นได้ (ถ้าเป็น predator หรือใหญ่กว่า) // Game Over หรือเสียชีวิต
        else if (fish.isPredator() || fish.getSize() > player.getPlayerSize()) {
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

        int left = f.getX();
        int right = f.getY() + drawW;
        int top = f.getY();
        int bottom = f.getY() + drawH;

        boolean overlapX = right >= 0 && left <= W;
        boolean overlapY = bottom >= 0 && top <= H;
        return overlapX && overlapY;
    }

    public List<Fish> getFishes() {
        return Collections.unmodifiableList(fishes);
    }

    public int getFishCount() {
        return fishes.size();
    }
}
