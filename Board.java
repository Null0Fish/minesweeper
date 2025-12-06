import processing.core.PApplet;

public class Board {

    // contains all the tiles that make up a board of minesweeper
    private final Tile[][] tiles;
    // the total amount of mines in a game
    private final int mines;
    // true if the mines have been set, false otherwise
    public boolean minesSet = false;
    // size of the board
    private final int size;
    /**
     * precondition: mines < width * height
     * @param size the size (in tiles) of the minesweeper board (board is size x size)
     * @param mines the total amount of mines present on the board
     */
    public Board(int size, int mines){
        this.mines = mines;
        tiles = new Tile[size][size];
        for(int c = 0; c < size; c++)
            for(int r = 0; r < size; r++)
                tiles[r][c] = new Tile(false);

        this.size = size;
    }

    /**
     * @return true if a tile that is a mine has been opened, otherwise false
     */
    public boolean isLost () {
        for(Tile[] row: tiles)
            for(Tile tile: row)
                if(tile.isMine() && tile.isOpen())
                    return true;
        return false;
    }

    /**
     * @return true if every non-mines tile has been opened, false if a non-mine has not been opened
     */
    public boolean isWon () {
        for(Tile[] row: tiles)
            for(Tile tile: row)
                if (tile.isClosed() && !tile.isMine())
                    return false;
        return true;
    }

    /**
     * sets mines on the board, no mine may be at (r, c) or near (r, c)
     * @param r the row of the first opened tile
     * @param c the col of the first opened tile
     */
    public void setMines (int r, int c) {
        int minesLeft = mines;
        while(minesLeft > 0){
            int row = (int) (Math.random() * size);
            int col = (int) (Math.random() * size);
            // mines cannot be in a 1 tile radius of (r, c)
            if((col != c || row != r) && !tiles[row][col].isMine() && (Math.abs(r-row) != 1
                    && Math.abs(r-row) != 0) || (Math.abs(c-col) != 1 && Math.abs(c-col) != 0)){
                minesLeft--;
                tiles[row][col] = new Tile(true);
            }
        }
        for(int col = 0; col < tiles.length; col++)
            for(int row = 0; row < tiles[0].length; row++)
                tiles[row][col].setNearMines(getNearMines(row, col));
    }

    /**
     * @param r the tow in tiles of a tile
     * @param c the col in tiles of a tile
     * @return the number of mines surrounding  the tile at (r, c) in a 1 tile radius
     */
    private int getNearMines (int r, int c) {
        int nearMines = 0;
        int len = tiles.length;
        for(int row = r-1; row < r+2; row++)
            for(int col = c -1; col < c +2; col++)
                if(row > -1 && row < len && col > -1 && col < len && tiles[row][col].isMine())
                    nearMines++;
        if(tiles[r][c].isMine())
            return nearMines-1;
        return nearMines;
    }

    /**
     * @param r the tow in tiles of a tile
     * @param c the col in tiles of a tile
     * @return the number of flags surrounding  the tile at (r, c) in a 1 tile radius
     */
    private int getNearFlags (int r, int c) {
        int nearFlags = 0;
        int len = tiles.length;
        for(int row = r-1; row < r+2; row++)
            for(int col = c -1; col < c +2; col++)
                if(row > -1 && row < len && col > -1 && col < len && tiles[row][col].isFlagged())
                    nearFlags++;
        if(tiles[r][c].isFlagged())
            return nearFlags-1;
        return nearFlags;
    }


    /**
     * opens a tile at (r, c)
     * @param r the tow in tiles of a tile
     * @param c the col in tiles of a tile
     */
    public void reveal (int r, int c) {
        if(!isValid(r, c))
            return;
        if (!tiles[r][c].isClosed())
            return;
        tiles[r][c].open();
        if(tiles[r][c].isMine())
            return;
        if(tiles[r][c].getNearMines() == 0)
            for(int row = r-1; row < r+2; row++)
                for(int col = c-1; col < c+2; col++)
                    reveal(row, col);
    }

    /**
     * attempts to reveal/sweep a tile based on the tile's state
     * @param r row in tiles
     * @param c col in tiles
     */
    public void open(int r, int c){
        if(tiles[r][c].isFlagged())
            return;
        if(tiles[r][c].isClosed())
            reveal(r,c);
        else
            sweep(r,c);
    }

    /**
     * toggles the flag of a tile at (r, c), opened tiles will not be flagged
     * @param r the tow in tiles of a tile
     * @param c the col in tiles of a tile
     */
    public void flag (int r, int c) {
        tiles[r][c].toggleFlag();
    }

    /**
     * opens all near tiles of a tile at (r, c) if that tile's near flags equal that tile's near mines
     * @param r row of a tile
     * @param c col of a tile
     */
    public void sweep (int r, int c) {
        if(getNearFlags(r,c)==tiles[r][c].getNearMines())
            for(int row = r -1; row < r +2; row++)
                for(int col = c-1; col < c+2; col++)
                    reveal(row, col);
    }

    /**
     * opens all tiles
     */
    public void revealAll () {
        for(Tile[] row: tiles)
            for(Tile tile: row)
                if(tile.isMine()) tile.open();
    }

    /**
     * @return number of mines minus flags placed, the amount of flags left to place; can be negative
     */
    public int getFlags (){
        int flags = 0;
        for(Tile[] row: tiles)
            for(Tile tile: row)
                if(tile.isFlagged())
                    flags++;
        return mines-flags;
    }
    /**
     * draws board onto a PApplet
     * @param p the PApplet that will be drawn on
     */
    public void draw (PApplet p) {
        for(int r = 0; r < tiles.length; r++){
            for(int c = 0; c < tiles[r].length; c++){
                Tile tile = tiles[r][c];
                if(!tile.isOpen()) {
                    if ((r + c) % 2 == 0) p.fill(90, 180, 90);
                     else p.fill(115, 195, 108);
                } else {
                    if ((r + c) % 2 == 0) p.fill(196, 170, 140);
                    else p.fill(194, 155, 108);
                }
                int n = 400/size;
                double scale = (double) 1/size;
                int offset =  (int) Math.round(scale * 200);
                // draws the tile in correct color
                p.square(c * n + offset, r * n + offset + 100, Math.round(scale * 400+1));
                // displays the tile's near mines if it has been opened and is not a mine
                // tiles that have a nearMines() of 0 do not display 0
                if(tile.isOpen() && !tile.isMine() && tile.getNearMines()!=0) {
                    int mines = tile.getNearMines();
                    if(mines == 1) p.fill(50, 50, 255);
                    else if(mines == 2) p.fill(30, 100, 50);
                    else if(mines == 3) p.fill(255, 0, 0);
                    else if(mines == 4) p.fill(102, 51, 153);
                    else p.fill(0);
                    p.textSize((int) (200 * scale) + 1);
                    p.text(tile.getNearMines(), c * n + offset, r * n + offset + 100);
                }
                // displays any flags as orange circles
                if(tile.isFlagged()) {
                    p.fill(255, 165, 0);
                    p.circle(c*n+offset, r*n+offset + 100, (int) (scale *200));
                }
                // displays any opened mines as red circles
                if(tile.isMine() && tile.isOpen()) {
                    // tile is mine, red
                    p.fill(140, 0, 0);
                    p.circle(c * n + offset, r * n + offset + 100, (int) (scale * 200));
                }
            }
        }
    }

    /**
     * return is a tile exists in tiles
     * @param r a row in tiles
     * @param c a col in tiles
     * @return true if it exists false otherwise
     */
    private boolean isValid(int r, int c){
        return r > -1 && c > -1  && r < tiles.length && c < tiles.length;
    }

    /**
     * gets size of board
     * @return size of board
     */
    public int getSize() {
        return size;
    }
}