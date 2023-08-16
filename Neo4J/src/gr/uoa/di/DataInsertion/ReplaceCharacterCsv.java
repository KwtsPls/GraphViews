package gr.uoa.di.DataInsertion;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ReplaceCharacterCsv {
    public static void main( String[] args ) throws Exception {
        FileWriter uris = new FileWriter("E:/value_combined_clean.csv");
        char del=6;
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream("E:/value_combined_new.csv");
            sc = new Scanner(inputStream, StandardCharsets.UTF_8);
            int i=0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                line = line.replaceAll("\"","`");
                String arr[] = line.split(""+del);
                String buffer;
                if(arr[0].length()>32000)
                    buffer = arr[0].substring(0,32000) + del + "Resource\n";
                else
                    buffer = line+"\n";
                uris.write(buffer);
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
