package org.example.day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static util.Utils.writeToAFile;

public class Main{
    public static void main(String[] args) {

        try {
            System.out.println("\nPROCESS STARTED\n");

            List<String> lines = Files.readAllLines(Path.of("inputs/day-5/input.txt"));

            String[] input = lines.toArray(new String[0]);

            Day5Solver solver = new Day5Solver();

            writeToAFile("outputs/day-5/part-1/output.txt",solver.solvePart1(input));
            writeToAFile("outputs/day-5/part-2/output.txt",solver.solvePart2(input));

            System.out.println("\nPROCESS FINISHED\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Day5Solver {

        public String solvePart1(String[] input) {
            InputData inputData = parseInput(input);
            Map<Integer, List<Integer>> reversedOrderingRules = inputData.reversedOrderingRules;
            List<List<Integer>> updates = inputData.updates;

            int sum = updates.stream()
                    .filter(pageNumbers -> isOrdered(pageNumbers, reversedOrderingRules))
                    .mapToInt(pageNumbers -> pageNumbers.get((pageNumbers.size() - 1) / 2))
                    .sum();


            System.out.println("PART 1 - PROCESS FINISHED");
            return String.valueOf(sum);
        }

        public String solvePart2(String[] input) {
            InputData inputData = parseInput(input);
            Map<Integer, List<Integer>> orderingRules = inputData.orderingRules;
            Map<Integer, List<Integer>> reversedOrderingRules = inputData.reversedOrderingRules;
            List<List<Integer>> updates = inputData.updates;

            int sum = 0;
            for (List<Integer> pageNumbers : updates) {
                if (!isOrdered(pageNumbers, reversedOrderingRules)) {
                    pageNumbers.sort((x, y) -> {
                        if (orderingRules.containsKey(x) && orderingRules.get(x).contains(y)) {
                            return -1;
                        }
                        if (orderingRules.containsKey(y) && orderingRules.get(y).contains(x)) {
                            return 1;
                        }
                        return 0;
                    });
                    sum += pageNumbers.get((pageNumbers.size() - 1) / 2);
                }
            }
            System.out.println("PART 2 - PROCESS FINISHED");
            return String.valueOf(sum);
        }

        private boolean isOrdered(List<Integer> pageNumbers, Map<Integer, List<Integer>> reversedOrderingRules) {
            Set<Integer> disallowedPageNumbers = new HashSet<>();
            for (int pageNumber : pageNumbers) {
                if (disallowedPageNumbers.contains(pageNumber)) {
                    return false;
                }
                if (reversedOrderingRules.containsKey(pageNumber)) {
                    disallowedPageNumbers.addAll(reversedOrderingRules.get(pageNumber));
                }
            }
            return true;
        }

        private InputData parseInput(String[] input) {
            Map<Integer, List<Integer>> orderingRules = new HashMap<>();
            Map<Integer, List<Integer>> reversedOrderingRules = new HashMap<>();
            List<List<Integer>> updates = new ArrayList<>();

            boolean instructions = true;
            for (String line : input) {
                if (line.trim().isEmpty()) {
                    instructions = false;
                    continue;
                }

                if (instructions) {
                    String[] ruleNums = line.split("\\|");
                    int from = Integer.parseInt(ruleNums[0]);
                    int to = Integer.parseInt(ruleNums[1]);

                    orderingRules.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                    reversedOrderingRules.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                } else {
                    List<Integer> update = Arrays.stream(line.split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    updates.add(update);
                }
            }

            return new InputData(orderingRules, reversedOrderingRules, updates);
        }

        private static class InputData {
            Map<Integer, List<Integer>> orderingRules;
            Map<Integer, List<Integer>> reversedOrderingRules;
            List<List<Integer>> updates;

            public InputData(Map<Integer, List<Integer>> orderingRules,
                             Map<Integer, List<Integer>> reversedOrderingRules,
                             List<List<Integer>> updates) {
                this.orderingRules = orderingRules;
                this.reversedOrderingRules = reversedOrderingRules;
                this.updates = updates;
            }
        }
    }
}

