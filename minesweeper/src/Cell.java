import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        invokeMethod("countUpIsMine");
    }

    private void neighboringCell(Method method) {
        method.setAccessible(true);
        boolean up = false,
                down = false;

        // check up
        if(y > 0) {
            up = true;
            invokeNeighboringCell(method, x, y - 1);
        }
        // check down
        if(y < Minesweeper.getRow() - 1) {
            down = true;
            invokeNeighboringCell(method, x, y + 1);
        }
        // check left
        if(x > 0) {
            invokeNeighboringCell(method, x - 1, y);
            if(up) {
                invokeNeighboringCell(method, x - 1, y - 1);
            }
            if(down) {
                invokeNeighboringCell(method, x - 1, y + 1);
            }
        }
        // check right
        if(x < Minesweeper.getCol() - 1) {
            invokeNeighboringCell(method, x + 1, y);
            if(up) {
                invokeNeighboringCell(method, x + 1, y - 1);
            }
            if(down) {
                invokeNeighboringCell(method, x + 1, y + 1);
            }
        }
    }

    private void invokeNeighboringCell(Method method, int x, int y) {
        try {
            method.invoke(this, x, y);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void countUpIsMine(int x, int y) {
        if(Minesweeper.cells.get(y).get(x).isMine()) mine++;
    }
    
    public boolean isMine() {
        return type == Type.Mine;
    }

    @Override
    public String toString() {
        if(type == Type.Mine && status == Status.Open) {
            return "M";
        }
        else {
            switch (status) {
                case Close:
                    return "?";
                case Mark:
                    return "x";
                default:
                    return mine.toString();
            }
        }
    }

    public void open() {
        if(status == Status.Close) {
            status = Status.Open;
            if(mine == 0) invokeMethod("openNeighboringCell");
            Minesweeper.volume--;
        }
    }
    
    private void invokeMethod(String methodName) {
        try {
            Method m = this.getClass().getDeclaredMethod(methodName, int.class, int.class);
            neighboringCell(m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    private void openNeighboringCell(int x, int y) {
        try {
            Method open = this.getClass().getDeclaredMethod("open");
            open.invoke(Minesweeper.cells.get(y).get(x));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void mark() {
        if (status == Status.Mark) {
            status = Status.Close;
        }
        else {
            status = Status.Mark;
        }
    }
}
