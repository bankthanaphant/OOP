package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entity.player;
import system.ObjectManager;
import system.Spawner;
import system.UIManager;

public class Gamepanel extends JPanel implements Runnable {
   private int originalTilesize = 16;
   private int scale = 4;
   private int tileSize;
   private int maxScreenCol;
   private int maxScreenRow;
   private int screenWidth;
   private int screenHeight;
   public player player;

   public enum Gamestate {
      PLAYING, GAME_OVER, PAUSED, WIN
   }

   private BufferedImage backgroundImage;
   private Gamestate currentGameState = Gamestate.PLAYING;
   private UIManager uiManager;
   private int winScore = 60;
   private int fps;
   private KeyHandler keyH;
   private Thread gThread;
   private ObjectManager objectManager;
   private Spawner spawner;
   private Sound music, soundeffect;

   public Gamepanel() {
      this.tileSize = this.originalTilesize * this.scale;
      this.maxScreenCol = 16;
      this.maxScreenRow = 12;
      this.screenWidth = this.tileSize * this.maxScreenCol;
      this.screenHeight = this.tileSize * this.maxScreenRow;
      this.fps = 60;
      this.keyH = new KeyHandler();
      this.player = new player(this, this.keyH);
      this.objectManager = new ObjectManager();
      this.spawner = new Spawner(objectManager);
      this.setPreferredSize(new Dimension(this.screenWidth, this.screenHeight));
      this.music = new Sound();
      this.soundeffect = new Sound();
      this.setBackground(Color.BLACK);
      this.setDoubleBuffered(true);
      this.addKeyListener(this.keyH);
      this.setFocusable(true);
      this.playMusic(0);
      this.uiManager = new UIManager(this);
      try {
         backgroundImage = ImageIO.read(getClass().getResourceAsStream("/Image/orig_big.png"));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public int getScreenWidth() {
      return this.screenWidth;
   }

   public int getScreenHeight() {
      return this.screenHeight;
   }

   public int getTileSize() {
      return this.tileSize;
   }

   public Gamestate getGameState() {
      return currentGameState;
   }

   public void setGameState(Gamestate state) {
      this.currentGameState = state;
   }

   public void restartGame() {
      this.player = new player(this, this.keyH);
      this.objectManager = new ObjectManager();
      this.spawner = new Spawner(objectManager);
      this.currentGameState = Gamestate.PLAYING;
      this.playMusic(0);
   }

   public void stratgThread() {
      this.gThread = new Thread(this);
      this.gThread.start();
   }

   public void run() {
      double var1 = (double) (1000000000 / this.fps);
      double var3 = 0.0;
      long var5 = System.nanoTime();
      long var9 = 0L;
      int var11 = 0;

      while (this.gThread != null) {
         long var7 = System.nanoTime();
         var3 += (double) (var7 - var5) / var1;
         var9 += var7 - var5;
         var5 = var7;
         if (var3 >= 1.0) {
            this.update();
            this.repaint();
            --var3;
            ++var11;
         }

         if (var9 >= 1000000000L) {
            System.out.println("FPS: " + var11);
            var11 = 0;
            var9 = 0L;
         }
      }
   }

   private void checkWinCondition() {
      if (player.getScore() >= winScore) {
         setGameState(Gamestate.WIN);
         stopMusic();
         playSE(4);
      }
   }

   public void update() {
      if (currentGameState == Gamestate.PLAYING) {
         this.player.update();
         spawner.update(this);
         objectManager.updateAll(this);
         checkWinCondition();
      } else if (currentGameState == Gamestate.GAME_OVER) {
         if (keyH.rPressed) {
            restartGame();
         }
      } else if (currentGameState == Gamestate.WIN) {
         if (keyH.rPressed) {
            restartGame();
         }
      }
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
      objectManager.drawAll(g2);
      this.player.draw(g2);
      if (currentGameState == Gamestate.PLAYING) {
         uiManager.drawPlayingUI(g2, player);
      } else if (currentGameState == Gamestate.GAME_OVER) {
         uiManager.drawGameOverScreen(g2, player);
      } else if (currentGameState == Gamestate.WIN) {
         uiManager.drawWinScreen(g2, player, winScore);
      }
      g2.dispose();
   }

   public void playMusic(int i) {
      music.setFile(i);
      music.play();
      music.loop();
   }

   public void stopMusic() {
      music.stop();
   }

   public void playSE(int i) {
      soundeffect.setFile(i);
      soundeffect.play();
   }
}
