package main.java.gr.uoa.di.entities.cypherViews;

import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.neo4j.driver.summary.Plan;
import org.neo4j.driver.summary.ResultSummary;

import java.util.List;

public class CypherBenefitModel implements AutoCloseable{

    private final Driver driver;

    public CypherBenefitModel( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    //Method to calculate the benefit from creating a view on the given pattern
    /*Initial test for benefit function :
    *
    * Bnft = (cost(Q) - cost(Q'))/size , where
    *
    * Q = initial query
    * Q' = query executed using views
    * size = size of view on the database
    *
    * */
    public double benefit(CypherQuery query){
        double size = getEstimatedResult(query.toString());
        if(size > 150000 || size<2){
            return -1;
        }
        else{
            //Get the estimated cost of the query as is
            double a = cost(query.toString());

            //Create the view
            viewProcessing(query.viewCreationQuery());

            //Check if a view was actually created
            int sz = getViewSize(query.viewExistsQuery());
            if(sz==0) return -1;

            //Get the estimated cost of the query using a view
            double b = cost(query.toStringUsingView());

            //Clear the view
            viewProcessing(query.viewDeletionQuery());

            return (a-b)/size;
        }
    }

    //Second implementation for benefit function
    public double benefit2(CypherQuery query){
        double size_0 = cost2(query.toString());
        double size_1 = getEstimatedResult(query.toString());
        return (size_1==0.0)?(0.0):((size_0 - size_1)/size_1);
    }

    //Method to calculate the execution cost of a given query
    public double cost(String query){
        try ( Session session = driver.session() )
        {
            Double cost = session.readTransaction( tx ->
            {
                ResultSummary result = tx.run("EXPLAIN " + query).consume();
                return getTotalEstimatedRows(result.plan());
            });

            return cost;
        }
    }

    //Method to calculate the execution cost of a given query based on the first action of the planner
    double cost2(String query){
        try ( Session session = driver.session() )
        {
            Double cost = session.readTransaction( tx ->
            {
                ResultSummary result = tx.run("EXPLAIN " + query).consume();
                return getEstimatedInitialRows(result.plan());
            });

            return cost;
        }
    }

    //Method to get the initial edge to create the correct index
    public String getIndexEdge(String query){
        try ( Session session = driver.session() )
        {
            String s = session.readTransaction( tx ->
            {
                ResultSummary result = tx.run("EXPLAIN " + query).consume();
                return getInitialEdge(result.plan());
            });

            return s;
        }
    }

    //Method to calculate the total estimated rows from the given plan of the query recursively
    double getTotalEstimatedRows(Plan plan){
        double result = plan.arguments().get("EstimatedRows").asDouble();
        List<? extends Plan> l = plan.children();
        if (!l.isEmpty()) {
            for (Plan p : l) {
                result += getTotalEstimatedRows(p);
            }
        }
        return result;
    }

    //Method to calculate the total estimated rows from the given plan of the query recursively
    double getEstimatedInitialRows(Plan plan){
        double result = plan.arguments().get("EstimatedRows").asDouble();
        List<? extends Plan> l = plan.children();
        if (!l.isEmpty()) {
            for (Plan p : l) {
                return getEstimatedInitialRows(p);
            }
        }
        return result;
    }

    //Method to get the first part of the query that the planner calculated
    String getInitialEdge(Plan plan){
        Value result = plan.arguments().get("Details");
        List<? extends Plan> l = plan.children();
        if (!l.isEmpty()) {
            for (Plan p : l) {
                return getInitialEdge(p);
            }
        }
        return (result==null)?null:result.asString();
    }


    //Method to get the estimated result of a query
    public double getEstimatedResult(String query){
        try ( Session session = driver.session() )
        {
            double cost = session.readTransaction( tx ->
            {
                ResultSummary result = tx.run("EXPLAIN " + query).consume();
                return result.plan().arguments().get("EstimatedRows").asDouble();
            });

            return cost;
        }
    }

    //Method to get if the size of views is 0 or not
    int getViewSize(String query){
        try ( Session session = driver.session() )
        {
            int size = session.readTransaction( tx ->
            {
                Result result = tx.run(query);
                return result.single().get(0).asInt();
            });

            return size;
        }
    }

    //Method to create the views for the cost calculation
    public int viewProcessing(String query){
        try ( Session session = driver.session() )
        {
            return session.writeTransaction(tx ->
            {
                Result result = tx.run(query);
                result.consume();
                return 1;
            });
        }
    }

    //Method to get the initial edge to create the correct index
    public Long materializeIndex(String query){
        try ( Session session = driver.session() )
        {
            Long s = session.writeTransaction( tx ->
            {
                Result result = tx.run(query);
                return result.stream().count();
            });

            return s;
        }
    }

    //Method to calculate the execution cost of a given query based on the first action of the planner
    public String deletionQuery(String query){
        try ( Session session = driver.session() )
        {
            String res = session.writeTransaction( tx ->
            {
                ResultSummary result = tx.run(query).consume();
                return result.toString();
            });

            return res;
        }
        catch (ClientException ce){
            System.out.println("Bad input");
            return "Bad input";
        }
    }



    @Override
    public void close() throws Exception
    {
        driver.close();
    }

}
