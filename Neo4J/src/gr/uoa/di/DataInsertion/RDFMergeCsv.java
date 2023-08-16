package gr.uoa.di.DataInsertion;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;

public class RDFMergeCsv {

    public static void main( String[] args ) throws Exception{
        FileWriter uris = new FileWriter("E:/value_combined.csv");
        FileInputStream inputStream = null;
        Scanner sc = null;
        HashSet<String> uriSet = new HashSet<>();
        try {
            inputStream = new FileInputStream("E:/ThesisDataCsv/Batch13/values13.csv");
            sc = new Scanner(inputStream, StandardCharsets.UTF_8);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                uris.write(line+"\n");
                uriSet.add(line);
                // System.out.println(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

        try {
            inputStream = new FileInputStream("E:/values.csv");
            int i=0;
            sc = new Scanner(inputStream, StandardCharsets.UTF_8);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if(i>0) {
                    if (!uriSet.contains(line)) {
                        uris.write(line+"\n");
                    }
                }
                i++;
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

        uris.close();

    }
}
