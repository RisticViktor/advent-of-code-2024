package org.example.day4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static util.Utils.writeToAFile;


public class Main{
    public static char[][] charArray;
    /*
     * directionArray are initial for the direction.
     * E - East, W - West, N - North S - South
     * Q - NW, R - NE, Z - SW, C - SE
     */
    public static char[] directionArray = "EWNSQRZC".toCharArray();
    public static int totalLength = 0;
    public static final String INPUT_FILE = "inputs/day-4/input.txt";


    public static void initialiseArray(){
        try{
            FileReader f = new FileReader(INPUT_FILE);
            BufferedReader reader = new BufferedReader(f);
            // Get total length
            while (reader.readLine() != null){
                totalLength++;
            }
            f.close();
            reader.close();
        }catch(IOException e){
            System.out.println("Cannot read file");
            System.exit(0);
        }
        charArray = new char[totalLength][];

        try {
            FileReader f = new FileReader(INPUT_FILE);
            BufferedReader reader = new BufferedReader(f);
            int y = 0;
            while (y < totalLength) {
                char[] line = reader.readLine().toCharArray();
                charArray[y++] = line;
            }
            f.close();
            reader.close();
        }catch(IOException e){
            System.out.println("Cannot read file");
            System.exit(0);
        }
    }

    public static boolean outOfBounds(int row, int col){

        return (row < 0 || row > charArray[0].length-1 || col < 0 || col > totalLength-1);
    }

    public static int[] nextCheckPoint(int startRow, int startCol, char direction){

        return switch (direction) {
            case 'E' -> {
                if (outOfBounds(startRow, startCol + 1)) yield new int[]{-1};
                yield new int[]{startRow, startCol + 1}; // East
            }
            case 'W' -> {
                if (outOfBounds(startRow, startCol - 1)) yield new int[]{-1};
                yield new int[]{startRow, startCol - 1}; // West
            }
            case 'N' -> {
                if (outOfBounds(startRow - 1, startCol)) yield new int[]{-1};
                yield new int[]{startRow - 1, startCol}; // North
            }
            case 'S' -> {
                if (outOfBounds(startRow + 1, startCol)) yield new int[]{-1};
                yield new int[]{startRow + 1, startCol}; // South
            }
            case 'Q' -> {
                if (outOfBounds(startRow - 1, startCol - 1)) yield new int[]{-1};
                yield new int[]{startRow - 1, startCol - 1}; // North West
            }
            case 'R' -> {
                if (outOfBounds(startRow - 1, startCol + 1)) yield new int[]{-1};
                yield new int[]{startRow - 1, startCol + 1}; // North East
            }
            case 'Z' -> {
                if (outOfBounds(startRow + 1, startCol - 1)) yield new int[]{-1};
                yield new int[]{startRow + 1, startCol - 1}; // South West
            }
            case 'C' -> {
                if (outOfBounds(startRow + 1, startCol + 1)) yield new int[]{-1};
                yield new int[]{startRow + 1, startCol + 1}; // South East
            }
            default -> new int[]{-1};
        };
    }

    public static int countXMAS(int charPos, int row, int col, char direction){
        if (charPos == 0){

            if (charArray[row][col] == 'X'){

                int count = 0;
                for (char d: directionArray){
                    int[] coordinate = nextCheckPoint(row, col, d);
                    if (!(coordinate[0] == -1)){
                        count += countXMAS(charPos+1, coordinate[0], coordinate[1], d);
                    }
                }
                return count;
            }
        } else if (charPos == 1){
            if (charArray[row][col] == 'M'){
                int[] coordinate = nextCheckPoint(row, col, direction);
                if (coordinate[0] == -1) return 0;
                return countXMAS(charPos+1, coordinate[0], coordinate[1], direction);
            } else {
                return 0;
            }
        } else if (charPos == 2){
            if (charArray[row][col] == 'A'){
                int[] coordinate = nextCheckPoint(row, col, direction);
                if (coordinate[0] == -1) return 0;
                return countXMAS(charPos+1, coordinate[0], coordinate[1], direction);
            } else {
                return 0;
            }
        } else if (charPos == 3){
            if (charArray[row][col] == 'S'){
                return 1;
            } else {
                return 0;
            }
        }

        return 0;
    }


    public static boolean countMAS(int row, int col){
        if (charArray[row][col] == 'A'){
            int[] topLeft = nextCheckPoint(row, col, 'Q');
            int[] bottomRight = nextCheckPoint(row, col, 'C');
            if (topLeft[0] == -1 || bottomRight[0] == -1){
                return false;
            } else {
                if (charArray[topLeft[0]][topLeft[1]] == 'M'){
                    if (charArray[bottomRight[0]][bottomRight[1]] != 'S') return false;
                } else if (charArray[topLeft[0]][topLeft[1]] == 'S'){
                    if (charArray[bottomRight[0]][bottomRight[1]] != 'M') return false;
                } else {
                    return false;
                }
            }
            int[] topRight = nextCheckPoint(row, col, 'R');
            int[] bottomLeft = nextCheckPoint(row, col, 'Z');
            if (topRight[0] == -1 || bottomLeft[0] == -1){
                return false;
            } else {
                if (charArray[topRight[0]][topRight[1]] == 'M'){
                    if (charArray[bottomLeft[0]][bottomLeft[1]] != 'S') return false;
                } else if (charArray[topRight[0]][topRight[1]] == 'S'){
                    if (charArray[bottomLeft[0]][bottomLeft[1]] != 'M') return false;
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    public static void main(String[] args){
        System.out.println("\nPROCESS STARTS\n");

        initialiseArray();
        int totalCountXMAS = 0;
        int totalCountMAS = 0;

        for (int col = 0; col < charArray.length; col++){
            for (int row = 0; row < charArray[col].length; row++){
                totalCountXMAS += countXMAS(0, row, col, 'd');
                if (countMAS(row, col)) totalCountMAS++;
            }
        }
        writeToAFile("outputs/day-4/part-1/output.txt",String.valueOf(totalCountXMAS));
        writeToAFile("outputs/day-4/part-2/output.txt",String.valueOf(totalCountMAS));

        System.out.println("\nPROCESS FINISHED\n");

    }
}
