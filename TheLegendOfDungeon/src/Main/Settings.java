/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author ReillyKeele
 */
public enum Settings {

    MUTED(false);

    private boolean b;

    Settings(Boolean b) {
        this.b = b;
    }

    public boolean isTrue() {
        return b;
    }

    public void setTrue(boolean b) {
        this.b = b;
    }

}
