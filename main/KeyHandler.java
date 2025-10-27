package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
   public boolean upPressed;
   public boolean downPressed;
   public boolean leftPressed;
   public boolean rightPressed;
   public boolean rPressed ;

   public KeyHandler() {
   }

   public void keyTyped(KeyEvent var1) {
   }

   public void keyPressed(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 87 || var2 == 38) {
         this.upPressed = true;
      }

      if (var2 == 83 || var2 == 40) {
         this.downPressed = true;
      }

      if (var2 == 65 || var2 == 37) {
         this.leftPressed = true;
      }

      if (var2 == 68 || var2 == 39) {
         this.rightPressed = true;
      }
      if (var2 == 82) { this.rPressed = true; }

   }

   public void keyReleased(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 87 || var2 == 38) {
         this.upPressed = false;
      }

      if (var2 == 83 || var2 == 40) {
         this.downPressed = false;
      }

      if (var2 == 65 || var2 == 37) {
         this.leftPressed = false;
      }

      if (var2 == 68 || var2 == 39) {
         this.rightPressed = false;
      }
      if (var2 == 82) { this.rPressed = false; }
   }
}
