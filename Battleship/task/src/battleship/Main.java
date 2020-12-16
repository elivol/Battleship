package battleship;

import java.util.Scanner;

enum Symbols {
    FOG('~'),
    SHIP('O'),
    HIT('X'),
    MISSED('M');

    private final char symbol;

    Symbols(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}

enum Ships {
    AIRCRAFT("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final String name;
    private final int length;

    Ships(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }
}

public class Main {

    private static final int SIZE = 10;
    private static Scanner scanner;
    private static int shipPieces = 5 + 4 + 2 * 3 + 2;
    private static char[][] field;

    public static void main(String[] args) {
        start();
    }

    public static void start(){
        scanner = new Scanner(System.in);
        field = new char[SIZE][SIZE];
        initField();
        printField(false);
        locateShips();
        startTheGame();
    }

    public static void initField() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = Symbols.FOG.getSymbol();
            }
        }
    }

    public static void locateShips(){
        for (Ships ship : Ships.values()) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getLength());
            takePosition(ship.getLength());
            printField(false);
        }

    }

    public static void startTheGame() {
        System.out.println("The game starts!");
        printField(true);
        System.out.println("Take a shot!\n");

        while (shipPieces > 0) {
            boolean hit = takeAShot();
            if (shipPieces > 0) {
                printField(true);
                if (hit) {
                    System.out.println("\nYou hit a ship! Try again:\n");
                } else {
                    System.out.println("\nYou missed! Try again:\n");
                }
            }
        }
        printField(false);
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    private static boolean takeAShot() {
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

        if (field[row][col] == Symbols.SHIP.getSymbol() || field[row][col] == Symbols.HIT.getSymbol()) {
            shipPieces -= field[row][col] == Symbols.SHIP.getSymbol() ? 1 : 0;
            field[row][col] = Symbols.HIT.getSymbol();
            return true;
        } else {
            field[row][col] = Symbols.MISSED.getSymbol();
            return false;
        }
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
                field[i][j] = Symbols.SHIP.getSymbol();
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
                if (i >= 0 && i < SIZE && j >=0 && j < SIZE && field[i][j] == Symbols.SHIP.getSymbol()) {
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
        return (int) letter - 'A';
    }

    private static int getIndexCol(String position){
        return Integer.parseInt(position.substring(1)) - 1;
    }

    public static void printField(boolean hideShips) {
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
                    System.out.print((hideShips && field[i - 1][j] == Symbols.SHIP.getSymbol() ?
                            Symbols.FOG.getSymbol() : field[i - 1][j]) +
                            " ");
                }
            }
            System.out.println();
        }
    }
}
