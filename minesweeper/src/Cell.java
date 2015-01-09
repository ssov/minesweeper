/**
 * Created by Toshiyuki Shimanuki on 15/01/07.
 */
public class Cell {

    enum Status {
        Open,
        Close,
        Mark
    }
    enum Type {
        Safe,
        Mine
    }
    private Status status;
    private Type type;
    private Integer x, y;
    private Integer mine;

    public Cell(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        status = Status.Close;
        mine = 0;
    }

    public void initialize() {
        boolean up = false,
                down = false;

        // check up
        if(y > 0) {
            up = true;
            countUpIsMine(x, y-1);
        }
        // check down
        if(y < Minesweeper.getRow() - 1) {
            down = true;
            countUpIsMine(x, y+1);
        }
        // check left
        if(x > 0) {
            countUpIsMine(x-1, y);
            if(up) {
                countUpIsMine(x-1, y-1);
            }
            if(down) {
                countUpIsMine(x-1, y+1);
            }
        }
        // check right
        if(x < Minesweeper.getCol() - 1) {
            countUpIsMine(x+1, y);
            if(up) {
                countUpIsMine(x+1, y-1);
            }
            if(down) {
                countUpIsMine(x+1, y+1);
            }
        }
    }

    private void countUpIsMine(int x, int y) {
        if(Minesweeper.cells.get(x).get(y).isMine()) mine++;
    }

    public boolean isMine() {
        return type == Type.Mine;
    }

    @Override
    public String toString() {
        switch(status) {
            case Close:
                return "?";
            case Mark:
                return "x";
            default:
                return mine.toString();
        }
    }

    public void open() {
        status = Status.Open;
    }

    public void mark() {
        status = Status.Mark;
    }
}
