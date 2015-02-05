import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Toshiyuki Shimanuki on 15/01/07.
 */
public class Minesweeper {

    enum Status {
        Playing,
        GameOver,
        Finish
    }
    private static int row, col, mine;
    public static int volume;
    public static ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
    private static Status status;
    public static Minesweeper instance = new Minesweeper();

    private Minesweeper() { }
    
    public void setSetting(int row, int col, int mine) {
        this.row = row;
        this.col = col;
        this.mine = mine;
        volume = row * col - mine;
        status = Status.Playing;

        ArrayList<Integer> mineList = getMineList();

        for(int i=0; i<row; i++) {
            ArrayList<Cell> rows = new ArrayList<>();
            for (int j=0; j<col; j++) {
                int k = j*row + i;
                rows.add(new Cell(j, i, (mineList.contains(k) ? Cell.Type.Mine : Cell.Type.Safe)));
            }
            cells.add(rows);
        }

        for(int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                cells.get(i).get(j).initialize();
            }
        }
    }

    public static int getRow() {
        return row;
    }

    public static int getCol() {
        return col;
    }
    
    public static Minesweeper getInstance() {
        return instance;
    }

    public void printBoard() {
        int r = 0;
        int length = String.valueOf(row-1).length();

        System.out.printf(" %" + length + "s", " ");

        for(int i=0; i<col; i++) {
            System.out.printf("%c ", (char)(i+97));
        }
        System.out.println("");

        for(ArrayList<Cell> rows : cells) {
            if(length > 0) {
                System.out.printf("%" + length + "d ", r++);
            }
            else {
                System.out.printf("%d ", r++);
            }
            
            for(Cell cell : rows) {
                System.out.printf("%s ", cell);
            }
            System.out.println("");
        }
    }

    public void markCell(String cellPoint) {
        int[] point = translatePoint(cellPoint);
        Cell cell = cells.get(point[1]).get(point[0]);
        cell.mark();
    }

    public void openCell(String cellPoint) {
        int[] point = translatePoint(cellPoint);
        Cell cell = cells.get(point[1]).get(point[0]);
        cell.open();

        if(cell.isMine()) {
            status = Status.GameOver;
        }
        else if(volume == 0) {
            status = Status.Finish;
        }
    }

    public boolean isGameOver() {
        return status == Status.GameOver;
    }

    public boolean isGameFinish() {
        return status == Status.Finish;
    }
    
    // @return int{col, row}
    private int[] translatePoint(String point) {
        Pattern p = Pattern.compile("(^[a-z])(\\d+)$");
        
        Matcher m = p.matcher(point);
        m.find();
        int col = (int) m.group(1).charAt(0) - 97;
        int row = Integer.parseInt(m.group(2));

        return new int[]{col, row};
    }

    private ArrayList<Integer> getMineList() {
        ArrayList<Integer> tmp = new ArrayList<>();
        ArrayList<Integer> mineList = new ArrayList<>();
        for(int i=0; i<row*col; i++) {
            tmp.add(i);
        }
        Collections.shuffle(tmp);
        for(int i=0; i<mine; i++) {
            mineList.add(tmp.get(i));
        }
        return mineList;
    }
}
