package main.java.gr.uoa.di.entities.cypherViews;

import main.java.gr.uoa.di.entities.graph.PatternVS;
import org.apache.jena.atlas.lib.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URI;

public class PatternToQueryConverter {

    int viewCounter;

    public PatternToQueryConverter(){
        viewCounter=0;
    }

    public CypherQuery convert(PatternVS pattern){
        CypherQuery cypherQuery = new CypherQuery(viewCounter);
        String query = pattern.toFullString();
        Pattern p = Pattern.compile("\\((\\(*(?:[^)(]*|\\([^)]*\\))*\\)*)\\)");
        Matcher matcher = p.matcher(query);

        //Iterate through every triplet
        while( matcher.find() ) {
            //Separate the (s,p,o) triples into tokens
            List<String> triple = Arrays.asList(matcher.group(1).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            Pair<String, String> subject = parseSubject(triple.get(0));
            Pair<String, String> predicate = parsePredicate(triple.get(1));
            Pair<String, String> object = parseObject(triple.get(2));
            cypherQuery.addTriple(subject,predicate,object);
        }
        viewCounter++;
        return cypherQuery;
    }


    //Method to parse the object part of the triplet (object,predicate.subject)
    Pair<String,String> parseSubject(String object){
        String result=null;
        String type=null;
        //Object is a variable
        if(object.startsWith(".") || object.startsWith("_") || object.startsWith("?")){
            result = object.replaceAll("[._?]","");
            type = "VAR";
        }
        //Object is a uri
        else{
            result = "\""+object+"\"";
            type = "URI";
        }

        return new Pair<>(result,type);
    }

    //Method to parse the predicate part of the triplet (object,predicate.subject)
    Pair<String,String> parsePredicate(String predicate){
        String result=null;
        String type="RELATIONSHIP";

        //Predicate is a relationship
        if(Objects.equals(predicate,"http://dbpedia.org/property/redirect")){
            predicate = "http://dbpedia.org/ontology/wikipageredirects";
        }
        else if(Objects.equals(predicate,"http://www.w3.org/1999/02/22-rdf-syntax-ns#type")){
            type="LABEL";
        }

        result="`"+predicate+"`";;
        return new Pair<>(result,type);
    }

    //Method to parse the predicate part of the triplet (object,predicate.subject)
    Pair<String,String> parseObject(String subject){
        String result=null;
        String type=null;

        //Subject is a variable
        if(subject.startsWith(".") || subject.startsWith("_") || subject.startsWith("?")){
            result = subject.replaceAll("[._?]","");
            type = "VAR";
        }
        //Subject is a value
        else{
            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(subject);
            while (m.find()){
                result = m.group(1);
            }

            result = Objects.requireNonNullElse(result, subject);

            //check if object is value or uri
            try {
                URI u = new URI(result);
                type="URI";

                if (!u.isAbsolute()){
                    type="VALUE";
                }

            } catch (URISyntaxException e) {
                type="VALUE";
            }
            result = "\""+result+"\"";
        }

        return new Pair<>(result,type);
    }
}
