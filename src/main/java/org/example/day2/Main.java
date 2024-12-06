package org.example.day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static util.Utils.writeToAFile;

public class Main {

    public static void main(String[] args) {
        System.out.println("\nPROCESS STARTS\n");

        String inputFile = "inputs/day-2/input.txt";

//        PART 1 solutions
        iterativeSolutionPart1(inputFile, "outputs/day-2/part-1/output-iterative.txt");
        stackSolutionPart1(inputFile, "outputs/day-2/part-1/output-stack.txt");

//        PART 2 solutions
        modifiedIterativeSolutionPart2(inputFile, "outputs/day-2/part-2/output-iterative-modified.txt");

        System.out.println("\nPROCESS FINISHED");
    }



    private static void modifiedIterativeSolutionPart2(String inputFile, String outputFile) {
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String line;
            int totalValidReports = 0;
            while((line = br.readLine()) != null){
                List<Integer> report = Arrays.stream(line.split("\\s+"))
                        .map(Integer::valueOf).
                        toList();
                if(reportIsValidIterative(report) || canBecomeValidByRemoving1(report)){
                    totalValidReports++;
                }

            }
            writeToAFile(outputFile, String.valueOf(totalValidReports));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("MODIFIED ITERATIVE PART 2 - PROCESS FINISHED");
        }
    }

    private static boolean canBecomeValidByRemoving1(List<Integer> report) {
        for (int i = 0; i < report.size(); i++) {
            List<Integer> modifiedLevels = new ArrayList<>(report);
            modifiedLevels.remove(i);
            if (reportIsValidIterative(modifiedLevels)) return true;
        }
        return false;
    }


    private static void stackSolutionPart1(String inputFile, String outputFile) {
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String line;
            int totalValidReports = 0;
            while((line = br.readLine()) != null){
                List<Integer> report = Arrays.stream(line.split("\\s+"))
                        .map(Integer::valueOf).
                        toList();
                if(reportIsValidStack(report)){
                    totalValidReports++;
                }

            }
            writeToAFile(outputFile, String.valueOf(totalValidReports));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("STACK PART 1 - PROCESS FINISHED");
        }
    }

    private static boolean reportIsValidStack(List<Integer> report) {
        if (report.size() < 2) return false;

        Deque<Integer> stack = new ArrayDeque<>();
        boolean increasing = report.get(1) > report.get(0);
        stack.push(report.get(0));

        for(int i = 1; i < report.size(); i++){
            int a = report.get(i);
            int b = stack.peek();
            int difference = Math.abs(a - b);
            if(difference < 1 || difference > 3) return false;
            if((increasing && a < b) || (!increasing && a > b)) return false;

            stack.push(a);
        }

        return true;
    }

    private static void iterativeSolutionPart1(String inputFile, String outputFile){
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String line;
            int totalValidReports = 0;
            while((line = br.readLine()) != null){
                List<Integer> report = Arrays.stream(line.split("\\s+"))
                        .map(Integer::valueOf).
                        toList();
                if(reportIsValidIterative(report)){
                    totalValidReports++;
                }

            }
            writeToAFile(outputFile, String.valueOf(totalValidReports));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("ITERATIVE PART 1 - PROCESS FINISHED");
        }
    }

    private static boolean reportIsValidIterative(List<Integer> report) {
        if(report.size() < 2) return false;

        boolean isIncreasing = report.get(1) > report.get(0);
        for(int i = 0; i < report.size() - 1; i++){

            int a = report.get(i + 1);
            int b = report.get(i);

            int absDifference = Math.abs(a - b);
            if(absDifference < 1 || absDifference > 3) return false;
            if(isIncreasing && a < b) return false;
            if(!isIncreasing && a > b) return false;

        }
        return true;
    }
}
