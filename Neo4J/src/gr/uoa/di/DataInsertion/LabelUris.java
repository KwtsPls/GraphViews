package gr.uoa.di.DataInsertion;

import gr.uoa.di.DataInsertion.parser.Tokenizer;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LabelUris {
    public static void main( String[] args ) throws Exception {
        FileWriter labeled_uris = new FileWriter("E:/labeled_uris.csv");
        HashMap<String, List<String>> map = new HashMap<>();
        FileInputStream inputStream = null;
        Scanner sc = null;
        char del=6;

        //Load a map with the labeled data
        System.out.println("Loading labels...");
        try {
            inputStream = new FileInputStream("E:/labels");
            sc = new Scanner(inputStream, StandardCharsets.UTF_8);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Tokenizer tokenizer = new Tokenizer(line);
                List<String> cur = map.get(tokenizer.getSubject());
                if(cur==null)
                    cur = new ArrayList<>();
                cur.add(tokenizer.getObject());
                map.put(tokenizer.getSubject(),cur);
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

        //Write nodes and the labels into the new file
        System.out.println("Creating labeled uris...");
        try {
            inputStream = new FileInputStream("E:/uri_combined.csv");
            sc = new Scanner(inputStream, StandardCharsets.UTF_8);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String arr[] = line.split(""+del,2);
                String key = arr[0];

                //Get the labels of the current uri
                List<String> cur = map.get(key);
                if(cur==null)
                    labeled_uris.write(line+"\n");
                else{
                    String buffer = line;
                    for(String label:cur){
                        buffer += ";" + label;
                    }
                    labeled_uris.write(buffer+"\n");
                    map.remove(key);
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

        //close file
        labeled_uris.close();
    }
}
