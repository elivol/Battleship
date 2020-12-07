package battleship;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static final int size = 10;
    private static int[][] field;
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
        field = new int[size][size];
        locateShips();
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

    }

    private static void takePosition(){
        String startPosition = scanner.next();
        String endPosition = scanner.next();

        int startRow = getIndexRow(startPosition);
        int startCol = getIndexCol(startPosition);
        int endRow = getIndexRow(endPosition);
        int endCol = getIndexCol(endPosition);
    }

    private static int getIndexRow(String position){
        return letters.get(position.charAt(0));
    }

    private static int getIndexCol(String position){
        return Integer.parseInt(position.substring(1));
    }
}
