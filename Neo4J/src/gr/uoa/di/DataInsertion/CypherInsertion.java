package gr.uoa.di.DataInsertion;

import com.google.common.cache.LoadingCache;
import gr.uoa.di.DataInsertion.parser.Tokenizer;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.base.Sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Objects;


public class CypherInsertion
{

    public static void main( String[] args ) throws Exception
    {
        //Delimiter for csv file
        char del=6;

        //Create the csv files
        FileWriter uris = new FileWriter("E:/ThesisDataCsv/Batch13/uris13.csv");
        uris.write("uri:ID"+del+":LABEL\n");
        FileWriter values = new FileWriter("E:/ThesisDataCsv/Batch13/values13.csv");
        values.write("value:ID"+del+":LABEL\n");
        FileWriter rels = new FileWriter("E:/ThesisDataCsv/Batch13/rels13.csv");
        rels.write(":START_ID"+del+":END_ID"+del+":TYPE\n");

        HashSet<String> uriSet = new HashSet<>();
        HashSet<String> valueSet = new HashSet<>();

        File dir = new File("E:/ThesisData/Batch13");
        File[] directoryListing = dir.listFiles();
        assert directoryListing != null;
        for (File child : directoryListing) {
            System.out.println("Processing: "+child.getName());
            try (BufferedReader br = new BufferedReader(new FileReader(child))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.charAt(0) != '#') {
                        Tokenizer tokenizer = new Tokenizer(line);
                        if (tokenizer.getType() != 3) {
                            String rel = tokenizer.packageTriple(del);
                            rels.write(rel);

                            String uri = tokenizer.getSubject();
                            if (!uriSet.contains(uri)) {
                                uriSet.add(uri);
                                uris.write(uri +del+"Resource\n");
                            }

                            if (tokenizer.getType() == 1) {
                                String uri2 = tokenizer.getObject();
                                if (!uriSet.contains(uri2)) {
                                    uriSet.add(uri2);
                                    uris.write(uri2 +del+"Resource\n");
                                }
                            } else {
                                String value = tokenizer.getObject();
                                if (!valueSet.contains(value)) {
                                    valueSet.add(value);
                                    values.write(value +del+"Resource\n");
                                }
                            }
                        }
                    }
                }
            }
        }

        uris.close();
        values.close();
        rels.close();
    }
}