package main.java.gr.uoa.di.entities.cypherViews;

import com.github.jsonldjava.utils.Obj;
import gr.uoa.di.entities.graph.regular.abstractions.Node;
import org.apache.jena.atlas.lib.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CypherQuery {

    List<String> cypherPatterns;
    LinkedHashSet<String> headVars;
    LinkedHashSet<String> whereClauseComponents;
    int varCounter;
    String viewName;
    String viewPattern;
    String replacedEdge;

    public CypherQuery(){
        cypherPatterns = new LinkedList<>();
        headVars = new LinkedHashSet<>();
        whereClauseComponents = new LinkedHashSet<>();
        varCounter=0;
    }

    public CypherQuery(int viewName){
        cypherPatterns = new LinkedList<>();
        headVars = new LinkedHashSet<>();
        whereClauseComponents = new LinkedHashSet<>();
        varCounter=0;
        this.viewName="View_"+viewName;
    }

    public String getReplacedEdge(){
        return this.replacedEdge;
    }
    public String getViewName() { return this.viewName; }
    public String getViewPattern() { return viewPattern; }

    public void addTriple(Pair<String,String> subject, Pair<String,String> predicate, Pair<String,String> object){
        String strSubject = subject.getLeft();
        String subjectType = subject.getRight();
        String strPredicate = predicate.getLeft();
        String predicateType = predicate.getRight();
        String strObject = object.getLeft();
        String objectType = object.getRight();

        //Subject of triple (s,p,o) is a variable
        if(Objects.equals(subjectType,"VAR")){
            if (!cypherPatterns.contains(makeNode(strSubject)))
                cypherPatterns.add(makeNode(strSubject));
            headVars.add(strSubject);

            //Predicate is a relationship create an edge
            if(Objects.equals(predicateType,"RELATIONSHIP")){
                if(Objects.equals(objectType,"URI") || Objects.equals(objectType,"VALUE")){
                    cypherPatterns.add(makeEdge(strSubject, "c" + varCounter, strObject,strPredicate,objectType.toLowerCase()));
                    //Add the second node to the return clause
                    headVars.add("c" + varCounter);
                    varCounter++;
                }
                //Object is a var, create a simple edge
                else{
                    cypherPatterns.add(makeEdge(strSubject, strObject, strPredicate));
                    headVars.add(strObject);
                }
            }
            //Predicate is type - ask for subject's labels
            else{
                if(Objects.equals(objectType,"VAR"))
                    headVars.add(makeLabel(strSubject));
                else
                    cypherPatterns.add(makeLabel(strSubject,strObject));
            }

        }
        //Subject is a uri
        else{
            String uriVar = "c" + varCounter;
            varCounter++;
            headVars.add(uriVar);

            //Predicate is a relationship create an edge
            if(Objects.equals(predicateType,"RELATIONSHIP")){
                if(Objects.equals(objectType,"URI") || Objects.equals(objectType,"VALUE")){
                    cypherPatterns.add(makeEdgeURI(uriVar,strSubject, "c" + varCounter, strObject,strPredicate,objectType.toLowerCase()));
                    //Add the second node to the return clause
                    headVars.add("c" + varCounter);
                    varCounter++;
                }
                //Object is a var, create a simple edge
                else{
                    cypherPatterns.add(makeEdgeURI(uriVar,strObject,strSubject,strPredicate));
                    headVars.add(strObject);
                }
            }
            //Predicate is type - ask for subject's labels
            else{
                headVars.add(makeLabel(strSubject));
            }

        }

    }


    //Helper method to add an edge between a var node and a uri node
    String makeEdgeURI(String uriVar,String var,String uri,String edge){
        return "("+uriVar+":Resource {uri:"+uri+"})"+"-[:"+edge+"]->"+ "("+var+")";
    }

    //Helper method to add an edge between a var node and a uri/value node
    String makeEdgeURI(String uriVar1,String uri,String uriVar2,String value,String edge,String type){
        return "("+uriVar1+":Resource {uri:"+uri+"})"+"-[:"+edge+"]->"+"("+uriVar2+":Resource {"+type+":"+value+"})";
    }

    //Helper method to add an edge between a var node and a uri/value node
    String makeEdge(String var,String uriVar,String value,String edge,String type){
        return "("+var+")"+"-[:"+edge+"]->"+"("+uriVar+":Resource {"+type+":"+value+"})";
    }

    //Helper method to add an edge between two var nodes
    String makeEdge(String var1,String var2,String edge){
        return "("+var1+")"+"-[:"+edge+"]->"+"("+var2+")";
    }

    //Helper method to add a node to MATCH clause
    String makeNode(String var){
        return "("+var+")";
    }

    //Helper method to add a node with a uri to MATCH clause
    String makeNode(String var,String uri){
        return "("+var+" {uri:"+uri+"})";
    }

    //Helper method to create a label query
    String makeLabel(String var){
        return "labels("+var+")";
    }

    //Helper method to create a specific label query
    String makeLabel(String var,String label) { return "("+var+":"+label.replaceAll("\"","`")+")"; }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("MATCH ");
        output.append(String.join(",", cypherPatterns));
        if(!whereClauseComponents.isEmpty()){
            output.append(" WHERE ");
            output.append(String.join(" AND \n",whereClauseComponents));
        }
        output.append(" RETURN ");
        output.append(String.join(",", headVars));
        return output.toString();
    }

    //Method to break an edge pattern into a list of arguments
    List<String> createEdge(String e){
        List<String> triple =  new ArrayList<String>();

        //Get the name of the relationship
        Matcher m1 = Pattern.compile("\\[(.*?)\\]").matcher(e);
        String relName=null;
        while (m1.find()) {
            relName = m1.group(1);
        }
        if(relName!=null) {
            String[] arrRelName = relName.split(":", 2);
            triple.add(arrRelName[1]);
        }

        //Get the two nodes of the pattern
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(e);
        while (m.find()) {
            triple.add(m.group(1));
        }

        return triple;
    }

    //Initialize the vars used to create the view queries
    public void initView(String edge){
        List<String> triple = createEdge(edge);
        //No edges in pattern - node label will be used as view
        if(triple.size()!=3){
            this.viewPattern = "(x0:"+viewName+")";
            this.replacedEdge = null;
        }
        else{
            this.viewPattern = "(" + triple.get(1) + ")-[r:"+viewName+"]->("+triple.get(2)+")";
            this.replacedEdge = "(" + triple.get(1) + ")-[:"+triple.get(0)+"]->("+triple.get(2)+")";
        }
    }

    //Method to return the cypher query using the current view
    public String toStringUsingView(){
        String result = this.toString();
        if(replacedEdge==null)
            return result.replace("MATCH (x0)", "MATCH (x0:" + viewName + ")");
        else
            return result.replace(replacedEdge,viewPattern);
    }

    //Method to return a query creation a view
    public String viewCreationQuery(){
        return (replacedEdge==null)?labelViewCreationQuery():edgeViewCreationQuery();
    }

    //Method to return a query that creates a label on the start of the pattern
    public String labelViewCreationQuery(){
        StringBuilder output = new StringBuilder("MATCH ");
        output.append(String.join(",", cypherPatterns));
        if(!whereClauseComponents.isEmpty()){
            output.append(" WHERE ");
            output.append(String.join(" AND \n",whereClauseComponents));
        }
        output.append(" SET x0:"+viewName);
        output.append(" RETURN labels(x0)");
        return output.toString();
    }

    //Method to return a query that creates an edge index
    public String edgeViewCreationQuery(){
        StringBuilder output = new StringBuilder("MATCH ");
        output.append(String.join(",", cypherPatterns));
        if(!whereClauseComponents.isEmpty()){
            output.append(" WHERE ");
            output.append(String.join(" AND \n",whereClauseComponents));
        }
        output.append(" MERGE " + viewPattern);
        output.append(" RETURN r");
        return output.toString();
    }

    //Method to delete the label used as a view for the current pattern
    public String viewDeletionQuery(){
        return (replacedEdge==null)?labelViewDeletionQuery():edgeViewDeletionQuery();
    }

    //Method to delete the label used as a view for the current pattern
    public String labelViewDeletionQuery(){
        return "MATCH (x0:"+viewName+") REMOVE x0:"+viewName +" RETURN labels(x0)";
    }

    //Method to delete the edge used as a view for the current pattern
    public String edgeViewDeletionQuery(){
        return "MATCH (x0)-[r:"+viewName+"]->(x1) DELETE r RETURN x0";
    }

    //Method to return the size of the view
    public String viewCountQuery(){
        return "MATCH (x0:"+viewName+") RETURN COUNT(x0)";
    }

    //Method to check if a view exists
    public String viewExistsQuery(){
        return "MATCH (x0:"+viewName+") " + " WITH x0 " + " LIMIT 5 " + " MATCH (x0:"+viewName+") " + " RETURN count(x0)";
    }

    //Method to return a query that creates a label on the start of the pattern
    public String genericViewCreationQuery(){
        String viewCreation = this.viewCreationQuery();
        return viewCreation.replaceAll(" \\{.*\\}","");
    }

}
