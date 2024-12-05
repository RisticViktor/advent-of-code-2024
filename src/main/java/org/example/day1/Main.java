package org.example.day1;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static util.Utils.writeToAFile;

public class Main {



    public static void main(String[] args) {

        String inputFile = "inputs/day-1/input.txt";

//        PART 1 solutions
        minHeapSolutionPart1(inputFile, "outputs/day-1/part-1/output-heap.txt");
        divideAndConquerSolutionPart1(inputFile, "outputs/day-1/part-1/output-divide-conquer.txt");

//        PART 2 solutions
        hashMapSolutionPart2(inputFile, "outputs/day-1/part-2/output-hash-map.txt");

        System.out.println("PROCESS FINISHED");
    }

    private static void hashMapSolutionPart2(String inputFile, String outputfile) {
        List<Integer> list = new ArrayList<>();
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                int leftNumber = Integer.parseInt(parts[0]);
                int rightNumber = Integer.parseInt(parts[1]);

                list.add(leftNumber);
                frequencyMap.put(rightNumber, frequencyMap.getOrDefault(rightNumber, 0) + 1);
            }

            long totalSum = 0;
            for(Integer value: list){
                totalSum += (long) value * frequencyMap.getOrDefault(value,0);
            }

            writeToAFile(outputfile, String.valueOf(totalSum));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void minHeapSolutionPart1(String inputFile, String outputFile){
        PriorityQueue<Integer> heap1 = new PriorityQueue<>();
        PriorityQueue<Integer> heap2 = new PriorityQueue<>();

       try( BufferedReader br = new BufferedReader(new FileReader(inputFile))){
           String line;
           while((line = br.readLine()) != null){
               String[] parts = line.split("\\s+");
               heap1.add(Integer.parseInt(parts[0]));
               heap2.add(Integer.parseInt(parts[1]));
           }

           int sum = 0;
           while(!heap1.isEmpty() && !heap2.isEmpty()){
               int min1 = heap1.poll();
               int min2 = heap2.poll();
               sum += Math.abs(min1 - min2);
           }
           writeToAFile(outputFile, String.valueOf(sum));
       } catch (IOException e) {
           writeToAFile(outputFile, e.getMessage());
       }
    }

//    Multithreading solution
    private static void divideAndConquerSolutionPart1(String inputFile, String outputFile){
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                list1.add(Integer.parseInt(parts[0]));
                list2.add(Integer.parseInt(parts[1]));
            }

            Collections.sort(list1);
            Collections.sort(list2);

            int numThreads = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            int chunkSize = list1.size() / numThreads;
            List<Future<Integer>> futures = new ArrayList<>();

            for (int i = 0; i < numThreads; i++) {
                int start = i * chunkSize;
                int end = (i == numThreads - 1) ? list1.size() : (i + 1) * chunkSize;

                List<Integer> sublist1 = list1.subList(start, end);
                List<Integer> sublist2 = list2.subList(start, end);

                futures.add(executor.submit(() -> {
                    int sum = 0;
                    for (int j = 0; j < sublist1.size(); j++) {
                        sum += Math.abs(sublist1.get(j) - sublist2.get(j));
                    }
                    return sum;
                }));
            }

            int totalSum = 0;
            for (Future<Integer> future : futures) {
                totalSum += future.get();
            }
            executor.shutdown();

            writeToAFile(outputFile, String.valueOf(totalSum));

        }
        catch (IOException e) {
            writeToAFile(outputFile, e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
