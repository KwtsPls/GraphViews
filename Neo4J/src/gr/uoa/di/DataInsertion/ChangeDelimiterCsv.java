package gr.uoa.di.DataInsertion;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;

public class ChangeDelimiterCsv {
    public static void main( String[] args ) throws Exception {
        char del = 6;
        FileWriter uris = new FileWriter("E:/value_combined_new_1.csv");
        uris.write("value:ID"+del+":LABEL\n");
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream("E:/value_combined.csv");
            sc = new Scanner(inputStream, StandardCharsets.UTF_8);
            int i=0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if(i>0) {
                    String arr[] = line.split("}Res");
                    uris.write(arr[0] + del + "Resource" + "\n");
                }else{
                    i++;
                }
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
