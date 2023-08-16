package gr.uoa.di.views4Neo.interfaceAdapters;

import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.neo4j.driver.summary.Plan;
import org.neo4j.driver.summary.ResultSummary;

import java.util.List;

public class Neo4jConnection implements AutoCloseable{

    private final Driver driver;

    public Neo4jConnection( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    //Method to calculate the execution cost of a given query based on the first action of the planner
    double PlanCost(String query){
        try ( Session session = driver.session() )
        {
            Double cost = session.readTransaction( tx ->
            {
                ResultSummary result = tx.run("EXPLAIN " + query).consume();
                return getPlanCostR(result.plan());
            });

            return cost;
        }
        catch (ClientException ce){
            System.out.println("Bad input");
            return 0.0;
        }
    }

    //Method to calculate the total estimated rows from the given plan of the query recursively
    double getPlanCostR(Plan plan){
        double result = plan.arguments().get("EstimatedRows").asDouble();
        List<? extends Plan> l = plan.children();
        if (!l.isEmpty()) {
            for (Plan p : l) {
                result += getPlanCostR(p);
            }
        }
        return result;
    }


    //Method to execute a given query and return the first row of results
    public String executeQuery(String query){
        try ( Session session = driver.session() )
        {
            String msg = session.readTransaction( tx ->
            {
                Result result = tx.run(query);
                return result.next().get(0).asString();
            });

            return msg;
        }
        catch (NoSuchRecordException re){
            return null;
        }
        catch (ClientException ce){
            System.out.println("Bad input");
            return null;
        }

    }

    //Method to execute a given query and return the first row of results
    public String executeIndexQuery(String query){
        try ( Session session = driver.session() )
        {
            String msg = session.writeTransaction( tx ->
            {
                Result result = tx.run(query);
                return result.next().get(0).asString();
            });

            return msg;
        }
        catch (NoSuchRecordException re){
            return null;
        }
        catch (ClientException ce){
            System.out.println("Bad input");
            return null;
        }

    }

    public void runQuery(String query){
        try ( Session session = driver.session() )
        {
            try(Transaction tx =  session.beginTransaction())
            {
                Result result = tx.run(query);
                tx.commit();
                return;
            }
        }
        catch (NoSuchRecordException re){
            return;
        }
        catch (ClientException ce){
            System.out.println("Bad input");
            return;
        }
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }
}
