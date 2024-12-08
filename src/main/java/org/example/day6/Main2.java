package org.example.day6;
import java.io.*;
import java.util.*;

import static util.Utils.writeToAFile;

public class Main2 {
    private static int gridSize = 0;
    private static Guard guard;
    private static final Map<Integer, List<Integer>> obstacles = new HashMap<>();
    private static int possibleObstacles = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("\nPROCESS STARTED\n");

        List<String> inputLines = readFile("inputs/day-6/input.txt");

        for (String line : inputLines) {
            for (int j = 0; j < line.length(); j++) {
                Coordinate vec = new Coordinate(j, gridSize);

                if (line.charAt(j) == '^') {
                    guard = new Guard(vec, '^', new ArrayList<>());
                }

                if (line.charAt(j) == '#') {
                    obstacles.computeIfAbsent(vec.x, k -> new ArrayList<>()).add(vec.y);
                }
            }
            gridSize++;
        }

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Guard clonedGuard = new Guard(guard);
                Map<Integer, List<Integer>> clonedObstacles = cloneObstacles(obstacles);

                if (placeObstacle(new Coordinate(x, y), clonedObstacles)) {
                    try {
                        while (move(clonedGuard, clonedObstacles)) {
                        }
                    } catch (Exception e) {
                        possibleObstacles++;
                    }
                }
            }
        }

        writeToAFile("outputs/day-6/part-2/output.txt",String.valueOf(possibleObstacles));

        System.out.println("\nPROCESS FINISHED\n");

    }

    private static boolean isOutOfBounds(Coordinate coord) {
        return coord.x < 0 || coord.x >= gridSize || coord.y < 0 || coord.y >= gridSize;
    }

    private static boolean containsObstacle(Coordinate coord, Map<Integer, List<Integer>> obs) {
        return obs.containsKey(coord.x) && obs.get(coord.x).contains(coord.y);
    }

    private static Coordinate getNext(Guard g) {
        return switch (g.dir) {
            case '^' -> new Coordinate(g.coord.x, g.coord.y - 1);
            case '>' -> new Coordinate(g.coord.x + 1, g.coord.y);
            case 'v' -> new Coordinate(g.coord.x, g.coord.y + 1);
            case '<' -> new Coordinate(g.coord.x - 1, g.coord.y);
            default -> throw new IllegalStateException("Invalid direction");
        };
    }

    private static boolean detectLoop(Guard g) {
        return g.path.size() > 2 * Math.pow(gridSize, 2);
    }

    private static void turn(Guard g) {
        switch (g.dir) {
            case '^': g.dir = '>'; break;
            case '>': g.dir = 'v'; break;
            case 'v': g.dir = '<'; break;
            case '<': g.dir = '^'; break;
        }
    }

    private static void recordPath(Guard g) {
        g.path.add(new Coordinate(g.coord.x, g.coord.y));
    }

    private static boolean move(Guard g, Map<Integer, List<Integer>> obs) {
        recordPath(g);
        Coordinate next = getNext(g);

        if (isOutOfBounds(next)) {
            return false;
        }

        if (detectLoop(g)) {
            throw new RuntimeException("Loop detected!");
        }

        if (containsObstacle(next, obs)) {
            turn(g);
        } else {
            g.coord = next;
        }

        return true;
    }

    private static boolean placeObstacle(Coordinate coord, Map<Integer, List<Integer>> obs) {
        if (coord.equals(guard.coord) || containsObstacle(coord, obs)) {
            return false;
        }

        obs.computeIfAbsent(coord.x, k -> new ArrayList<>()).add(coord.y);
        return true;
    }

    private static Map<Integer, List<Integer>> cloneObstacles(Map<Integer, List<Integer>> original) {
        Map<Integer, List<Integer>> clone = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : original.entrySet()) {
            clone.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return clone;
    }

    private static List<String> readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        return lines;
    }

    private static class Coordinate {
        int x, y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class Guard {
        Coordinate coord;
        char dir;
        List<Coordinate> path;

        Guard(Coordinate coord, char dir, List<Coordinate> path) {
            this.coord = coord;
            this.dir = dir;
            this.path = path;
        }

        Guard(Guard other) {
            this.coord = new Coordinate(other.coord.x, other.coord.y);
            this.dir = other.dir;
            this.path = new ArrayList<>(other.path);
        }
    }
}


