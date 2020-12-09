package battleship;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static final int SIZE = 10;
    private static final char SHIP_SYMBOL = 'O';
    private static final char HIT_SYMBOL = 'X';
    private static final char MISSED_SYMBOL = 'M';
    private static char[][] field;
    private static Map<Character, Integer> letters;


    static {
        letters = new HashMap<>();
        Character first = 'A';
        for (int i = 0; i < SIZE; i++) {
            letters.put(first, i);
            first++;
        }
    }

    public static void main(String[] args) {
        start();
    }

    public static void start(){
        scanner = new Scanner(System.in);
        field = new char[SIZE][SIZE];
        initField();
        printField();
        locateShips();
        startTheGame();
    }

    public static void initField() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = '~';
            }
        }
    }

    public static void locateShips(){
        locateAircraft();
        locateBattleship();
        locateSubmarine();
        locateCruiser();
        locateDestroyer();
    }

    public static void startTheGame() {
        System.out.println("The game starts!");
        takeAShot();
        printField();
    }

    private static void takeAShot() {
        System.out.println("Take a shot!");
        String target = scanner.next();
        int row = getIndexRow(target);
        int col = getIndexCol(target);
        boolean ok = row >= 0 && row < SIZE && col >= 0 && col < SIZE;

        while (!ok) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            target = scanner.next();
            row = getIndexRow(target);
            col = getIndexCol(target);
            ok = row >= 0 && row < SIZE && col >= 0 && col < SIZE;
        }

        if (field[row][col] == SHIP_SYMBOL) {
            System.out.println("You hit a ship!");
            field[row][col] = HIT_SYMBOL;
        } else {
            System.out.println("You missed!");
            field[row][col] = MISSED_SYMBOL;
        }
    }

    public static void locateDestroyer() {
        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        takePosition(2);
        printField();
    }

    public static void locateCruiser() {
        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        takePosition(3);
        printField();
    }

    public static void locateSubmarine() {
        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        takePosition(3);
        printField();
    }

    public static void locateBattleship() {
        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        takePosition(4);
        printField();
    }

    public static void locateAircraft(){
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        takePosition(5);
        printField();
    }

    private static void takePosition(int length){
        Position position = takeFromIO();
        boolean positionIsOk = false;

        while (!positionIsOk) {
            // check position correctness
            if (!isInBoundaries(position)) {
                System.out.println("Error! Out of field. Try again:");
                position = takeFromIO();
            } else if (!isLinear(position)) {
                System.out.println("Error! Ship is not linear! Try again:");
                position = takeFromIO();
            } else if(isCrossedOrClose(position)) {
                System.out.println("Error! Wrong ship location! Try again:");
                position = takeFromIO();
            } else if (!isRightLength(position, length)) {
                System.out.println("Error! Wrong ship length! Try again:");
                position = takeFromIO();
            } else  {
                positionIsOk = true;
                putShip(position);
            }
        }
    }

    private static Position takeFromIO() {
        String startPosition = scanner.next();
        String endPosition = scanner.next();
        int startRow = Math.min(getIndexRow(startPosition), getIndexRow(endPosition));
        int startCol = Math.min(getIndexCol(startPosition), getIndexCol(endPosition));
        int endRow = Math.max(getIndexRow(startPosition), getIndexRow(endPosition));
        int endCol = Math.max(getIndexCol(startPosition), getIndexCol(endPosition));
        return new Position(startRow, startCol, endRow, endCol);
    }

    private static void putShip(Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                field[i][j] = SHIP_SYMBOL;
            }
        }
    }

    private static boolean isRightLength(Position position, int length) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        if (startRow == endRow) {
            return Math.abs(endCol - startCol) + 1 == length;
        } else if (startCol == endCol) {
            return Math.abs(endRow - startRow) + 1 == length;
        }
        return false;
    }

    private static boolean isInBoundaries(Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        return startRow >= 0 && startRow < SIZE &&
                endRow >= 0 && endRow < SIZE &&
                startCol >= 0 && startCol < SIZE &&
                endCol >= 0 && endCol < SIZE;
    }

    private static boolean isCrossedOrClose(Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        boolean crossedOrClose = false;
        for (int i = startRow - 1; !crossedOrClose && i <= endRow + 1; i++) {
            for (int j = startCol - 1; !crossedOrClose &&  j <= endCol + 1; j++) {
                if (i >= 0 && i < SIZE && j >=0 && j < SIZE && field[i][j] == SHIP_SYMBOL) {
                    crossedOrClose = true;
                }
            }
        }
        return crossedOrClose;
    }

    private static boolean isLinear(Position position) {
        return position.getStartRow() == position.getEndRow() ||
                position.getStartCol() == position.getEndCol();
    }

    private static int getIndexRow(String position){
        char letter = position.charAt(0);
        return letters.containsKey(letter) ? letters.get(position.charAt(0)) : -1;
    }

    private static int getIndexCol(String position){
        return Integer.parseInt(position.substring(1)) - 1;
    }

    private static void printField() {
        for (int i = 0; i < SIZE + 1; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i == 0) {
                    if (j == 0) {
                        System.out.print("  ");
                    }
                    System.out.print((j + 1) + " ");
                } else {
                    if (j == 0) {
                        System.out.print((char) ('A' + i - 1) + " ");
                    }
                    System.out.print(field[i - 1][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
