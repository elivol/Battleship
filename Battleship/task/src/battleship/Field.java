package battleship;

import java.util.Arrays;

public class Field {

    private final int SIZE = 10;
    private final int SHIPS_COUNT = 5;
    private int current = 0;
    private char[][] field;
    private Position[] positions;
    private int afloat;
    private int aliveShips = 5;

    public Field() {
        field = new char[SIZE][SIZE];
        positions = new Position[SHIPS_COUNT];
        afloat = 5 + 4 + 2 * 3 + 2;

        for (char[] row: field) {
            Arrays.fill(row, Symbols.FOG.getSymbol());
        }
    }

    public char getContentAt(int row, int col) {
        return field[row][col];
    }

    public void insert(int row, int col, char content) {
        field[row][col] = content;
        recountAliveShips(row, col);
    }

    public void insertShip(int startRow, int endRow, int startCol, int endCol) {
        if (current < SHIPS_COUNT) {
            positions[current] = new Position(startRow, startCol, endRow, endCol);
            current++;
        }
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                this.insert(i, j, Symbols.SHIP.getSymbol());
            }
        }
    }

    private void recountAliveShips(int row, int col) {
        Position pos = null;
        for (Position position: positions) {
            if (row >= position.getStartRow() && row <= position.getEndRow() &&
                    col >= position.getStartCol() && col <= position.getEndCol()) {
                pos = position;
                break;
            }
        }

        if (pos == null) {
            return;
        }

        for (int i = pos.getStartRow(); i <= pos.getEndRow(); i++) {
            for (int j = pos.getStartCol(); j <= pos.getEndCol(); j++) {
                if (field[i][j] != Symbols.HIT.getSymbol()) {
                    return;
                }
            }
        }
        aliveShips--;
    }

    public int getAliveShips() {
        return aliveShips;
    }

    public int getAfloat() {
        return afloat;
    }

    public void setAfloat(int afloat) {
        this.afloat = afloat;
    }

}
