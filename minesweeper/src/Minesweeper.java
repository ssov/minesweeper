import java.util.*;

/**
 * Created by Toshiyuki Shimanuki on 15/01/07.
 */
public class Minesweeper {

    enum Status {
        Playing,
        GameOver,
        Finish
    }
    private static int row, col, mine, volume;
    public static ArrayList<ArrayList<Cell>> cells = new ArrayList<>();
    private static Status status;
    public static Minesweeper instance = new Minesweeper();

    private Minesweeper() {
        row = 5;
        col = 5;
        mine = 5;
        volume = row * col - mine;
        status = Status.Playing;

        ArrayList<Integer> mineList = getMineList();

        for(int i=0; i<row; i++) {
            ArrayList<Cell> rows = new ArrayList<>();
            for (int j=0; j<col; j++) {
                int k = i*row + j;
                rows.add(new Cell(i, j, (mineList.contains(k) ? Cell.Type.Mine : Cell.Type.Safe)));
            }
            cells.add(rows);
        }

        for(int i=0; i<row; i++) {
            for (int j = 0; j < col; j++) {
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
        int row = 0;
        System.out.print("  ");
        for(int i=0; i<col; i++) {
            System.out.print((char)(i+97) + " ");
        }
        System.out.println("");

        for(ArrayList<Cell> rows : cells) {
            System.out.print(row++ + " ");

            for(Cell cell : rows) {
                System.out.print(cell);
                System.out.print(" ");
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

        volume--;
        if(cell.isMine()) {
            status = Status.GameOver;
        }

        if(volume == 0) {
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
        // 'a' = 97
        int col = (int)point.charAt(0) - 97;
        // '0' = 48
        int row = (int)point.charAt(1) - 48;

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
