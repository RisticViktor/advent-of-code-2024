package org.example.day7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.Utils.writeToAFile;

public class Main {

    public static void main(String[] args) {
        System.out.println("\nPROCESS STARTS\n");

        String inputFile = "inputs/day-7/input.txt";

//        PART 1
        solution(inputFile, "outputs/day-7/part-1/output.txt", false);
//        PART 2
        solution(inputFile, "outputs/day-7/part-2/output.txt", true);

        System.out.println("\nPROCESS FINISHED");
    }

    private static void solution(String inputFile, String outputFile, boolean isPart2){

        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String line;
            long totalCalibrationResult = 0;
            while((line = br.readLine()) != null){
                String[] parts = line.split(": ");
                long testValue = Long.parseLong(parts[0]);
                String[] numbers = parts[1].split(" ");

                List<String> expressions = generateExpressions(numbers, isPart2);

                for (String expr : expressions) {
                    if (evaluateExpression(expr) == testValue) {
                        totalCalibrationResult += testValue;
                        break;
                    }
                }
            }
            writeToAFile(outputFile, String.valueOf(totalCalibrationResult));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            if(isPart2){
                System.out.println("PART 2 - PROCESS FINISHED");
            }
            else{
                System.out.println("PART 1 - PROCESS FINISHED");
            }

        }
    }

    private static List<String> generateExpressions(String[] numbers, boolean isPart2) {
        List<String> expressions = new ArrayList<>();
        generateExpressionsHelper(numbers, 1, numbers[0], expressions, isPart2);
        return expressions;
    }

    private static void generateExpressionsHelper(String[] numbers, int index, String currentExpression, List<String> expressions, boolean isPart2) {
        if (index == numbers.length) {
            expressions.add(currentExpression);
            return;
        }

        generateExpressionsHelper(numbers, index + 1, currentExpression + " + " + numbers[index], expressions, isPart2);
        generateExpressionsHelper(numbers, index + 1, currentExpression + " * " + numbers[index], expressions, isPart2);

        if(isPart2){
            generateExpressionsHelper(numbers, index + 1, currentExpression + " || " + numbers[index], expressions, isPart2);
        }
    }

    private static long evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        long result = Long.parseLong(tokens[0]);

        for (int i = 1; i < tokens.length; i += 2) {
            int num = Integer.parseInt(tokens[i + 1]);
            switch (tokens[i]) {
                case "+" -> result += num;
                case "*" -> result *= num;
                case "||" -> result = Long.parseLong(result + tokens[i + 1]);
            }
        }

        return result;
    }

}
