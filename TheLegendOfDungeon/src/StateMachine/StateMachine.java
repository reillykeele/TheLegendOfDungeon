/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMachine;

import Main.GamePanel;
import Main.InputManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class StateMachine {
    
    public static final int MENUSTATE = 0;
    public static final int PLAYSTATE = 1;
    
    //CHANGE TO ARRAY NOT ARRAYLIST
    private ArrayList<State> states;
    
    private int currState;
    
    public StateMachine() {
        states = new ArrayList<State>();
        
        states.add(new MenuState(this));
        states.add(new PlayState(this));
        
        loadState(currState);
        
//        loadState();
    }
    
    public void loadState(int state) {
        currState = state;
        states.get(currState).resetState();
    }
    
    public void update(){ 
        processInput();
        states.get(currState).update();
    }
    
    public void update(double deltaTime) {
        processInput();
        states.get(currState).update(deltaTime);
    }
    
    public void render(Graphics2D g) {
        states.get(currState).render(g);
    }
    
    public void keyPressed(int k) {
        states.get(currState).keyPressed(k);
    }
    
    public void keyReleased(int k) {
        states.get(currState).keyReleased(k);
    }

    public void processInput() {
        states.get(currState).processInput();
    }
}
