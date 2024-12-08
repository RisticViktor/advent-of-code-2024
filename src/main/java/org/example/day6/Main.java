package org.example.day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static util.Utils.writeToAFile;

public class Main{

    public static void main(String[] args) {
        try {
            System.out.println("\nPROCESS STARTED\n");

            List<String> lines = Files.readAllLines(Path.of("inputs/day-6/input.txt"));

            String[] input = lines.toArray(new String[0]);

            Day6Solver solver = new Day6Solver();

            writeToAFile("outputs/day-6/part-1/output.txt",solver.solvePart1(input));

            System.out.println("\nPROCESS FINISHED\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Day6Solver {

        public String solvePart1(String[] input) {
            GuardTracker tracker = new GuardTracker(input);
            int explored = tracker.patrol();
            return Integer.toString(explored);
        }

        static class GuardTracker {
            private final char[][] grid;
            private final boolean[][] obstacles;
            private final Set<Coordinate> explored = new HashSet<>();
            private Guard guard;

            private final Set<Coordinate> empty = new HashSet<>();
            private Coordinate shenanigan;

            public GuardTracker(String[] input) {
                int rows = input.length;
                int cols = input[0].length();
                obstacles = new boolean[rows][cols];

                grid = new char[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        char value = input[i].charAt(j);
                        grid[i][j] = value;

                        Coordinate coordinate = new Coordinate(i, j);
                        switch (value) {
                            case '.' -> empty.add(coordinate);
                            case '#' -> obstacles[i][j] = true;
                            case '^' -> setGuard(coordinate, Direction.UP);
                            case '>' -> setGuard(coordinate, Direction.RIGHT);
                            case 'v', 'V' -> setGuard(coordinate, Direction.DOWN);
                            case '<' -> setGuard(coordinate, Direction.LEFT);
                            default -> throw new IllegalArgumentException("Unexpected character in input");
                        }
                    }
                }

                if (guard == null) {
                    throw new IllegalArgumentException("Guard not found in input");
                }
            }

            private enum Direction {
                UP, RIGHT, DOWN, LEFT
            }

            public int patrol() {
                Coordinate next = getNext();
                while (next != null) {
                    if (obstacles[next.x][next.y] || next.equals(shenanigan)) {
                        setGuard(guard.position, turnRight(guard.orientation));
                    } else {
                        setGuard(next, guard.orientation);
                    }
                    next = getNext();
                }
                return explored.size();
            }


            private Coordinate getNext() {
                Coordinate next = switch (guard.orientation) {
                    case UP -> new Coordinate(guard.position.x - 1, guard.position.y);
                    case RIGHT -> new Coordinate(guard.position.x, guard.position.y + 1);
                    case DOWN -> new Coordinate(guard.position.x + 1, guard.position.y);
                    case LEFT -> new Coordinate(guard.position.x, guard.position.y - 1);
                };

                if (next.x < 0 || next.x >= grid.length || next.y < 0 || next.y >= grid[0].length) {
                    return null;
                }

                return next;
            }

            private Direction turnRight(Direction current) {
                return Direction.values()[(current.ordinal() + 1) % 4];
            }

            private void setGuard(Coordinate position, Direction orientation) {
                guard = new Guard(position, orientation);
                explored.add(position);
            }

            private static class Coordinate {
                final int x, y;

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
                final Coordinate position;
                final Direction orientation;

                Guard(Coordinate position, Direction orientation) {
                    this.position = position;
                    this.orientation = orientation;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    Guard guard = (Guard) o;
                    return position.equals(guard.position) && orientation == guard.orientation;
                }

                @Override
                public int hashCode() {
                    return Objects.hash(position, orientation);
                }
            }
        }
    }

}