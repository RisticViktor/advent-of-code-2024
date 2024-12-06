package org.example.day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.Utils.writeToAFile;

public class Main {

    public static void main(String[] args) {
        System.out.println("\nPROCESS STARTS\n");

        String inputFile = "inputs/day-3/input.txt";

//        PART 1
        regexSolutionPart1(inputFile, "outputs/day-3/part-1/output-regex.txt");

//        PART 2
        regexSolutionPart2(inputFile, "outputs/day-3/part-2/output-regex.txt");

        System.out.println("\nPROCESS FINISHED");

    }

    private static void regexSolutionPart1(String inputFile, String outputFile) {
        try {
            String corruptedInput = Files.readString(Paths.get(inputFile));

            String regex = "mul\\((\\d+),(\\d+)\\)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(corruptedInput);

            Integer totalSum = 0;
            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int product = x * y;
                totalSum += product;
            }

            writeToAFile(outputFile, String.valueOf(totalSum));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("REGEX PART 1 - PROCESS FINISHED");
        }
    }

    private static void regexSolutionPart2(String inputFile, String outputFile) {
        try {
            String corruptedInput = Files.readString(Paths.get(inputFile));

            Pattern instructionPattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");
            Matcher matcher = instructionPattern.matcher(corruptedInput);

            int totalSum = 0;
            boolean isMultiplicationEnabled = true;

            while (matcher.find()) {
                String match = matcher.group();

                if (match.equals("do()")) {
                    isMultiplicationEnabled = true;
                } else if (match.equals("don't()")) {
                    isMultiplicationEnabled = false;
                } else if (match.startsWith("mul") && isMultiplicationEnabled) {
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    totalSum += x * y;
                }
            }

            writeToAFile(outputFile, String.valueOf(totalSum));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("REGEX PART 2 - PROCESS FINISHED");
        }
    }
}
