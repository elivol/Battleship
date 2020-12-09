package battleship;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static final int size = 10;
    private static char[][] field;
    private static Map<Character, Integer> letters;

    static {
        letters = new HashMap<>();
        Character first = 'A';
        for (int i = 0; i < size; i++) {
            letters.put(first, i);
            first++;
        }
    }

    public static void main(String[] args) {
        start();
    }

    public static void start(){
        scanner = new Scanner(System.in);
        field = new char[size][size];
        initField();
        printField();
        locateShips();
    }

    public static void initField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = '~';
            }
        }
    }

    public static void locateShips(){
        locateAircraft();
//        locateBattleship();
//        locateSubmarine();
//        locateCruiser();
//        locateDestroyer();
    }

    public static void locateAircraft(){
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        takePosition();
        printField();
    }

    private static void takePosition(){
        Position position = takeFromIO();
        boolean positionIsOk = false;

        while (!positionIsOk) {
            // check position correctness
            if (isInBoundaries(position)) {
                System.out.println("Error! Try again:");
                position = takeFromIO();
            } else if (isLinear(position)) {
                System.out.println("Error! Try again:");
                position = takeFromIO();
            } else if(!isCrossedOrClose(position)) {
                System.out.println("Error! Try again:");
                position = takeFromIO();
            } else  {
                putShip(position);
            }
        }
    }

    private static Position takeFromIO() {
        String startPosition = scanner.next();
        String endPosition = scanner.next();
        int startRow = getIndexRow(startPosition);
        int startCol = getIndexCol(startPosition);
        int endRow = getIndexRow(endPosition);
        int endCol = getIndexCol(endPosition);
        return new Position(startRow, startCol, endRow, endCol);
    }

    private static void putShip(Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                field[i][j] = '0';
            }
        }
    }

    private static boolean isInBoundaries(Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        return startRow >= 0 && startRow < size &&
                endRow >= 0 && endRow < size &&
                startCol >= 0 && startCol < size &&
                endCol >= 0 && endCol < size;
    }

    private static boolean isCrossedOrClose(Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        boolean crossedOrClose = false;
        for (int i = startRow - 1; !crossedOrClose && i <= endRow + 1; i++) {
            for (int j = startCol - 1; !crossedOrClose &&  j <= endCol + 1; j++) {
                if (i >= 0 && i < size && j >=0 && j < size && field[i][j] == '0') {
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
        return letters.get(position.charAt(0));
    }

    private static int getIndexCol(String position){
        return Integer.parseInt(position.substring(1));
    }

    private static void printField() {
        for (int i = 0; i < size + 1; i++) {
            for (int j = 0; j < size; j++) {
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
