/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.awt.event.KeyEvent;

/**
 *
 * @author 28783
 */
public class KeyHandler {

    public static final int NUM_KEYS = 16;

    public static boolean keyState[] = new boolean[NUM_KEYS];
    public static boolean prevKeyState[] = new boolean[NUM_KEYS];

    public static int UP = 0;
    public static int LEFT = 1;
    public static int DOWN = 2;
    public static int RIGHT = 3;
    public static int BUTTON1 = 4;
    public static int BUTTON2 = 5;
    public static int BUTTON3 = 6;
    public static int BUTTON4 = 7;
    public static int ENTER = 8;
    public static int ESCAPE = 9;

    public static void keySet(int i, boolean b) {
//        switch (i) {
//            case KeyEvent.VK_UP:
//                keyState[UP] = b;
//                break;
//            case KeyEvent.VK_LEFT:
//                keyState[LEFT] = b;
//                break;
//            case KeyEvent.VK_DOWN:
//                keyState[DOWN] = b;
//                break;
//            case KeyEvent.VK_RIGHT:
//                keyState[RIGHT] = b;
//                break;
//            case KeyEvent.VK_A:
//                keyState[BUTTON1] = b;
//                break;
//            case KeyEvent.VK_S:
//                keyState[BUTTON2] = b;
//                break;
//            case KeyEvent.VK_D:
//                keyState[BUTTON3] = b;
//                break;
//            case KeyEvent.VK_SPACE:
//                keyState[BUTTON4] = b;
//                break;
//            case KeyEvent.VK_ENTER:
//                keyState[ENTER] = b;
//                break;
//            case KeyEvent.VK_ESCAPE:
//                keyState[ESCAPE] = b;
//                break;
//            default:
//                break;
//        }

        if (i == KeyEvent.VK_UP) {
            keyState[UP] = b;
        }
        if (i == KeyEvent.VK_RIGHT) {
            keyState[RIGHT] = b;
        }
        if (i == KeyEvent.VK_DOWN) {
            keyState[DOWN] = b;
        }
        if (i == KeyEvent.VK_LEFT) {
            keyState[LEFT] = b;
        }
        if (i == KeyEvent.VK_A) {
            keyState[BUTTON1] = b;
        }
        if (i == KeyEvent.VK_S) {
            keyState[BUTTON2] = b;
        }
        if (i == KeyEvent.VK_ENTER) {
            keyState[ENTER] = b;
        }
        if (i == KeyEvent.VK_ESCAPE) {
            keyState[ESCAPE] = b;
        }
    }

    public static void update() {
        for (int i = 0; i < NUM_KEYS; i++) {
            prevKeyState[i] = keyState[i];
        }
    }

    public static boolean isPressed(int i) {
        return keyState[i] && !prevKeyState[i];
    }

    public static boolean anyKeyPress() {
        for (int i = 0; i < NUM_KEYS; i++) {
            if (keyState[i]) {
                return true;
            }
        }
        return false;
    }

}
