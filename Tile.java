public class Tile {
    private final int CLOSED = 0;
    private final int FLAGGED = 1;
    private final int OPEN = 2;
    // the state of a tile
    private int state = CLOSED;
    final private boolean hasMine;
    private int nearMines;

    public Tile(boolean hasMine){
        this.hasMine = hasMine;
        nearMines = 0;
    }

    /**
     * @return true if a given tile is flagged, false if it is not
     */
    public boolean isFlagged () {
        return state == FLAGGED;
    }

    /**
     * @return true if the tile has been opened otherwise false
     */
    public boolean isOpen () {
        return state == OPEN;
    }

    /**
     * @return true if a tile has not been opened otherwise false
     */
    public boolean isClosed () {
        return state == CLOSED;
    }

    /**
     * @return true if a tile is a mine false if it is not a mine
     */
    public boolean isMine () {
        return hasMine;
    }

    /**
     * @return the amount of mines, as an int, surrounding the tile in a 1 tile radius
     */
    public int getNearMines () {
        return nearMines;
    }

    /**
     * as the name implies this toggles if the tile is flagged
     */
    public void toggleFlag () {
        if(state == FLAGGED)
            state = CLOSED;
        else if(state == CLOSED)
            state = FLAGGED;
    }

    /**
     * sets the amount of mines near the tile
     * @param mines the mines surrounding the tile in a 1 tile radius
     */
    public void setNearMines (int mines) {
        nearMines = mines;
    }

    /**
     * opens the tile
     */
    public void open () {
        if(state != FLAGGED && state == CLOSED)
            state = OPEN;
    }
}