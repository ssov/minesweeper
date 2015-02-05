import java.util.Scanner;

/**
 * Created by Toshiyuki Shimanuki on 15/01/07.
 */
public class Game {
    public static void main(String[] args) {
        Minesweeper minesweeper = Minesweeper.getInstance();
        boolean status = true;
        Scanner scanner = new Scanner(System.in);
        String cell, mode;
        int row, col, mine;

        System.out.println("ゲーム設定を行います。");
        
        System.out.print("横 (2 ~ 26): ");
        col = scanner.nextInt();
        
        System.out.print("縦 (2 ~ 128): ");
        row = scanner.nextInt();

        System.out.printf("地雷の数 (1 ~ %d): ", row*col-1);
        mine = scanner.nextInt();

        minesweeper.setSetting(row, col, mine);
        System.out.println("ゲームを開始します。");

        while(status) {
            System.out.println("==============");
            minesweeper.printBoard();

            System.out.println("選択する列・行を入力してください:");
            cell = scanner.next();

            System.out.println("開くならoを、地雷チェックはxを入力してください:");
            mode = scanner.next();

            if(mode.equals("o")) {
                minesweeper.openCell(cell);
            }
            else if(mode.equals("x")) {
                minesweeper.markCell(cell);
            }

            if(minesweeper.isGameOver()) {
                status = false;
                System.out.println("ゲームオーバー");
            }
            else if(minesweeper.isGameFinish()) {
                status = false;
                System.out.println("ゲームクリア");
            }
        }
        minesweeper.printBoard();
    }
}