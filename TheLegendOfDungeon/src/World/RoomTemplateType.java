/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

/**
 *
 * @author 28783
 */
public enum RoomTemplateType {

    //15 possibilities
    //4 c 1
    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3),
    //4 c 2
    NORTH_EAST(4),
    NORTH_SOUTH(5),
    NORTH_WEST(6),
    EAST_SOUTH(7),
    EAST_WEST(8),
    SOUTH_WEST(9),
    //4 c 3
    NORTH_EAST_SOUTH(10),
    NORTH_EAST_WEST(11),
    NORTH_SOUTH_WEST(12),
    EAST_SOUTH_WEST(13),
    //4 c 4
    NORTH_EAST_SOUTH_WEST(14);

    public final int i;

    RoomTemplateType(int i) {
        this.i = i;
    }

    public int getIndex() {
        return i;
    }

}
