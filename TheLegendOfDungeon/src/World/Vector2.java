/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

/**
 *
 * @author ReillyKeele
 */
public class Vector2 {

    private int x, y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRow() {
        return x;
    }

    public int getCol() {
        return y;
    }

    public void setRow(int r) {
        this.x = r;
    }

    public void setCol(int c) {
        this.y = c;
    }

    public boolean Equals(Vector2 o) {
        return (this.x == o.getX() && this.y == o.getY());
    }

    @Override
    public String toString() {
        return "[" + getX() + ", " + getY() + "]";
    }
}
