package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    public static void writeToAFile(String outputFile, String payload){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write(payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
