package gr.uoa.di.views4Neo.interfaceAdapters;

import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.base.Sys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CypherView {
    String viewName;
    String edge;
    String deleteQuery;
    List<List<NaiveTriple>> mappings;
    Double benefit;
    int materialized;

    public CypherView(String line,int isMaterialized){
        char controlChar = 6;
        String del = ""+controlChar;
        String[] arr = line.split(del);
        this.viewName = arr[0];
        this.edge = arr[1];
        this.deleteQuery = arr[2];
        String mappingsString = arr[3];
        mappings = new ArrayList<>();

        //Split the mappings
        String[] mappingsArray  = mappingsString.split("&&&");
        for(String mapping:mappingsArray){
            List<NaiveTriple> list = new ArrayList<>();
            String[] singleMappings = mapping.split("\\s+(?=(?:\\b\"\\b|\"[^\"]*\"|[^\"])*$)");
            for(String single:singleMappings){
                String value = single.split("->",2)[1];

                Pattern p = Pattern.compile("\\((\\(*(?:[^)(]*|\\([^)]*\\))*\\)*)\\)");
                Matcher matcher = p.matcher(value);

                //Iterate through every triplet
                String subject=null;
                String predicate=null;
                String object=null;
                while( matcher.find() ) {
                    //Separate the (s,p,o) triples into tokens
                    List<String> triple = Arrays.asList(matcher.group(1).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                    subject = parseSubject(triple.get(0));
                    predicate = parsePredicate(triple.get(1));
                    object = parseObject(triple.get(2));
                }
                NaiveTriple naiveTriple = new NaiveTriple(subject,predicate,object);
                list.add(naiveTriple);
            }
            mappings.add(list);
        }

        this.benefit = Double.parseDouble(arr[4]);
        this.materialized = isMaterialized;
    }

    //Method to parse the object part of the triplet (object,predicate.subject)
    String parseSubject(String object){
        String result=null;
        if(object.startsWith(".") || object.startsWith("_") || object.startsWith("?"))
            result = object.replaceAll("[._?]","");
        else
            result = "\""+object+"\"";

        return result;
    }

    //Method to parse the predicate part of the triplet (object,predicate.subject)
    String parsePredicate(String predicate){
        String result=null;

        //Predicate is a relationship
        if(Objects.equals(predicate,"http://dbpedia.org/property/redirect")){
            predicate = "http://dbpedia.org/ontology/wikipageredirects";
        }

        result="`"+predicate+"`";;
        return result;
    }

    //Method to parse the predicate part of the triplet (object,predicate.subject)
    String parseObject(String subject){
        String result=null;

        //Subject is a variable
        if(subject.startsWith(".") || subject.startsWith("_") || subject.startsWith("?")){
            result = subject.replaceAll("[._?]","");
        }
        //Subject is a value
        else{
            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(subject);
            while (m.find()){
                result = m.group(1);
            }

            result = Objects.requireNonNullElse(result, subject);
            result = "\""+result+"\"";
        }

        return result;
    }

    //Check if this view can be used for a given query
    int isContained(List<NaiveTriple> triples){
        for(List<NaiveTriple> list:mappings){
            int count=0;
            for(NaiveTriple t1:list){
                boolean exists=false;
                for(NaiveTriple t2:triples){
                    if(t1.isSame(t2)==1){
                        count++;
                        exists=true;
                        break;
                    }
                }
                if(!exists)
                    break;
            }
            if(count == list.size())
                return 1;
        }
        return 0;
    }


    public String getViewName(){ return this.viewName; }
    public String getEdge(){ return this.edge; }
    public Double getBenefit() { return this.benefit; }
    public List<List<NaiveTriple>> getMappings() { return this.mappings; }
    public int getMaterialized() {return this.materialized; }
    public void setMaterialized(int m) {this.materialized=m;}


    @Override
    public String toString(){
        String mappingsString = "";
        for(List<NaiveTriple> list:mappings){
            for(NaiveTriple triple:list){
                mappingsString += triple.toString();
            }
        }

        return viewName+","+edge+","+deleteQuery+","+mappingsString+","+benefit;
    }

}
