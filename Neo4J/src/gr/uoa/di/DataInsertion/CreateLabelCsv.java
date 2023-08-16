package gr.uoa.di.DataInsertion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import gr.uoa.di.DataInsertion.parser.Tokenizer;
import org.apache.jena.base.Sys;

public class CreateLabelCsv {
    public static void main( String[] args ) throws Exception{
        FileWriter labels = new FileWriter("E:/labels");
        File directory = new File("E:/ThesisData");
        FileInputStream inputStream = null;
        Scanner sc = null;

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null) {
            for (File file : fList) {

                if (file.isDirectory()) {
                    String path = file.getAbsolutePath();
                    File batches = new File(path);
                    for (File batch : batches.listFiles()) {
                        try {
                            inputStream = new FileInputStream(batch.getAbsolutePath());
                            System.out.println("Processing: " + batch.getAbsolutePath());
                            sc = new Scanner(inputStream, StandardCharsets.UTF_8);
                            while (sc.hasNextLine()) {
                                String line = sc.nextLine();
                                Tokenizer tokenizer = new Tokenizer(line);
                                if (tokenizer.getType() == 3) {
                                    labels.write(line.toLowerCase() + "\n");
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
                    }
                }
            }
        }
        labels.close();
    }
}
