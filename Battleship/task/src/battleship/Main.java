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
    private static Field field1;
    private static Field field2;

    public static void main(String[] args) {
        start();
    }

    public static void start(){
        scanner = new Scanner(System.in);
        field1 = new Field();
        field2 = new Field();

        System.out.println("Player 1, place your ships on the game field\n");
        printField(field1, false);
        locateShips(field1);
        printPassTurn();

        System.out.println("Player 2, place your ships on the game field\n");
        printField(field2, false);
        locateShips(field2);
        printPassTurn();

        startTheGame();
    }

    public static void locateShips(Field field){
        for (Ships ship : Ships.values()) {
            System.out.printf("\nEnter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getLength());
            Position position = takePosition(field, ship.getLength());
            insertShip(field, position);
            printField(field, false);
        }

    }

    public static void startTheGame() {
        boolean firstTurn = false;

        while (Math.min(field1.getAfloat(), field2.getAfloat()) > 0) {
            firstTurn = !firstTurn;
            int player = firstTurn ? 1 : 2;

            printField(firstTurn ? field2 : field1, true);
            System.out.println("---------------------");
            printField(firstTurn ? field1 : field2, false);
            System.out.printf("\nPlayer %d it's your turn:", player);

            int shipsBeforeShot = (firstTurn ? field2 : field1).getAliveShips();
            boolean hit = takeAShot(firstTurn ? field2 : field1);

            if (Math.min(field1.getAfloat(), field2.getAfloat()) > 0) {
                if (hit) {
                    if (shipsBeforeShot > (firstTurn ? field2 : field1).getAliveShips()) {
                        System.out.println("\nYou sank a ship!");
                    } else {
                        System.out.println("\nYou hit a ship!");
                    }
                } else {
                    System.out.println("\nYou missed!");
                }
                printPassTurn();
            }
        }

        System.out.printf("Player %d, you sank the last ship. You won. Congratulations!", firstTurn ? 1 : 2);
    }

    private static boolean takeAShot(Field field) {
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

        if (field.getContentAt(row, col) == Symbols.SHIP.getSymbol() ||
                field.getContentAt(row, col) == Symbols.HIT.getSymbol()) {
            int number = field.getContentAt(row, col) == Symbols.SHIP.getSymbol() ? 1 : 0;
            field.setAfloat(field.getAfloat() - number);
            field.insert(row, col, Symbols.HIT.getSymbol());
            return true;
        } else {
            field.insert(row, col, Symbols.MISSED.getSymbol());
            return false;
        }
    }

    private static Position takePosition(Field field, int length){
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
            } else if(isCrossedOrClose(field, position)) {
                System.out.println("Error! Wrong ship location! Try again:");
                position = takeFromIO();
            } else if (!isRightLength(position, length)) {
                System.out.println("Error! Wrong ship length! Try again:");
                position = takeFromIO();
            } else  {
                positionIsOk = true;
            }
        }
        return position;
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

    private static void insertShip(Field field, Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        field.insertShip(startRow, endRow, startCol, endCol);
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

    private static boolean isCrossedOrClose(Field field, Position position) {
        int startRow = position.getStartRow();
        int startCol = position.getStartCol();
        int endRow = position.getEndRow();
        int endCol = position.getEndCol();

        boolean crossedOrClose = false;
        for (int i = startRow - 1; !crossedOrClose && i <= endRow + 1; i++) {
            for (int j = startCol - 1; !crossedOrClose &&  j <= endCol + 1; j++) {
                if (i >= 0 && i < SIZE && j >=0 && j < SIZE && field.getContentAt(i, j) == Symbols.SHIP.getSymbol()) {
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

    public static void printField(Field field, boolean hideShips) {
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
                    System.out.print((hideShips && field.getContentAt(i - 1, j) == Symbols.SHIP.getSymbol() ?
                            Symbols.FOG.getSymbol() : field.getContentAt(i - 1, j)) +
                            " ");
                }
            }
            System.out.println();
        }
    }

    private static void printPassTurn() {
        System.out.println("Press Enter and pass the move to another player\n...");
        scanner.nextLine();
        scanner.nextLine();
        System.out.println();
    }
}
