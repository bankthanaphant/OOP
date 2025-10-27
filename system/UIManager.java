package system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import entity.player;
import main.Gamepanel;

public class UIManager {
    private Gamepanel gp;

    public UIManager(Gamepanel gp) {
        this.gp = gp;
    }

    public void drawPlayingUI(Graphics2D g2, player player) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Score: " + player.score, 10, 20);
        g2.drawString("Level: " + player.level, 10, 40);
        g2.drawString("EXP: " + player.exp + "/" + player.expToNextLevel, 10, 60);
        g2.drawString("Size: " + player.getPlayerSize(), 10, 80);
    }

    public void drawGameOverScreen(Graphics2D g2, player player) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        String gameOverText = "GAME OVER";
        int textWidth = g2.getFontMetrics().stringWidth(gameOverText);
        g2.drawString(gameOverText, (gp.getWidth() - textWidth) / 2, gp.getHeight() / 2 - 50);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String scoreText = "Final Score: " + player.score;
        int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
        g2.drawString(scoreText, (gp.getWidth() - scoreWidth) / 2, gp.getHeight() / 2);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String pressKeyText = "Press R to restart";
        int pressKeyWidth = g2.getFontMetrics().stringWidth(pressKeyText);
        g2.drawString(pressKeyText, (gp.getWidth() - pressKeyWidth) / 2, gp.getHeight() / 2 + 50);
    }

    public void drawWinScreen(Graphics2D g2, player player, int winScore) {
        // วาด overlay สีเขียวโปร่งใส (ความรู้สึกชนะ)
        g2.setColor(new Color(0, 255, 0, 80));
        g2.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        // ข้อความชนะ
        g2.setColor(Color.GREEN);
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        String winText = "VICTORY!";
        int textWidth = g2.getFontMetrics().stringWidth(winText);
        g2.drawString(winText, (gp.getWidth() - textWidth) / 2, gp.getHeight() / 2 - 80);

        // เหตุผลที่ชนะ
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String reasonText = "";
        if (player.score >= winScore) {
            reasonText = "Reached " + winScore + " points!";
        }
        int reasonWidth = g2.getFontMetrics().stringWidth(reasonText);
        g2.drawString(reasonText, (gp.getWidth() - reasonWidth) / 2, gp.getHeight() / 2 - 20);

        // สถิติ
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Final Score: " + player.score, gp.getWidth() / 2 - 100, gp.getHeight() / 2 + 20);
        g2.drawString("Final Level: " + player.level, gp.getWidth() / 2 - 100, gp.getHeight() / 2 + 50);

        // ข้อความเริ่มใหม่
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String restartText = "Press R to play again";
        int restartWidth = g2.getFontMetrics().stringWidth(restartText);
        g2.drawString(restartText, (gp.getWidth() - restartWidth) / 2, gp.getHeight() / 2 + 100);

        // เอฟเฟกต์พิเศษ (optional)
        drawVictoryEffects(g2);
    }

    private void drawVictoryEffects(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("CONGRATULATIONS!", gp.getWidth() / 2 - 120, gp.getHeight() / 2 + 140);
    }
}
