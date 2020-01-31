/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StateMachine;

import Main.InputManager;
import java.awt.Graphics2D;

public class State {

    protected StateMachine sm;

    public State(StateMachine sm) {
        this.sm = sm;
    }

    public void resetState() {

    }

    public void update() {
        
    }
    
    public void update(double deltaTime) {
        
    }

    public void render(Graphics2D g) {

    }

    public void keyPressed(int k) {

    }

    public void keyReleased(int k) {

    }

    void processInput() {
        
    }
}
