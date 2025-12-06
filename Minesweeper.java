import processing.core.PApplet;

public class Minesweeper extends PApplet {

    // a timer for a game of minesweeper
    private final Timer timer = new Timer(this);
    // the board for a game of minesweeper
    private Board board = new Board(8,8);

    public void settings() {
        size(400, 500);
    }

    public void setup() {
        textAlign(CENTER, CENTER);
        rectMode(CENTER);
        noStroke();
        background(152, 190, 100);
    }

    public void draw() {
        background(0, 150, 150);
        fill(0, 0, 0);
        textSize(30);
        text("Time: " + timer.getTime()/1000, 100, 50);
        text("Flags: " + board.getFlags(), 300, 50);
        board.draw(this);
        if(board.isWon() || board.isLost()){
            timer.end();
            textSize(30);
            fill(135, 206, 235, 150);
            rect(200, 250, 200, 200, 40);
            fill(0, 0, 0, 175);
            if(board.isLost()) {
                text("YOU LOST!\n Time: " + timer.getTime() / 1000 + "\n 'r' to replay", 200, 250);
                board.revealAll();
            }
            else
                text("YOU WON!\n Time: "+ timer.getTime()/1000 + "\n 'r' to replay", 200,250);
        }
    }

    public void mouseClicked () {
        if(mouseY  > 100 && !board.isWon() && !board.isLost() && !timer.isPaused()) {
            int r = (mouseY-100)/(400/board.getSize());
            int c = mouseX/(400/board.getSize());
            if(!board.minesSet) {
                board.setMines(r, c);
                board.minesSet = true;
            }
            if(timer.getTime() == 0)
                timer.start();
            if (mouseButton == RIGHT)
                board.flag(r,c);
            if(mouseButton == LEFT)
                board.open(r,c);
        }
    }

    public void keyPressed() {
        // resets game
        if(key == 'r')
            reset(board.getSize());
        // can only pause when the game has started
        if(key == 'p' && timer.getTime() != 0)
            timer.pause();
        int size = board.getSize();
        if(keyCode == UP) {
            for(int i = size+1; i <= 50; i++)
                if (400%i == 0) {
                    reset(i);
                    break;
                }
        }
        if(keyCode == DOWN) {
            for (int i = size - 1; i >= 5; i--)
                if (400 % i == 0) {
                    reset(i);
                    break;
                }
        }
    }

    /**
     * resets a game of minesweeper with a board of size x size
     * @param size size of the board
     */

    private void reset(int size){
        timer.reset();
        // makes the appropriate-sih amount of mines based on board size, mines will always be even
        int mines = ((2*size)/5) * ((2*size)/5);
        mines = (mines+mines%2);
        board = new Board(size, mines);
    }
}