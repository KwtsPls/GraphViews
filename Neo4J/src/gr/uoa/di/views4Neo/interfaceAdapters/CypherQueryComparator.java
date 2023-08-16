package gr.uoa.di.views4Neo.interfaceAdapters;

import org.apache.jena.base.Sys;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

import static java.lang.String.valueOf;

public class CypherQueryComparator {

    public static void main(String[] args){
        long n=0;
        BigInteger a = new BigInteger("0");
        BigInteger b = new BigInteger("0");
        Neo4jConnection connection = new Neo4jConnection("bolt://localhost:7687", "neo4j", "equator-alpine-mobile-panic-lorenzo-1219");

        try {
            File replaced = new File("outputs/replaced.txt");
            File rewritten = new File("outputs/rewritten.txt");
            Scanner replacedReader = new Scanner(replaced);
            Scanner rewrittenReader = new Scanner(rewritten);

            while (replacedReader.hasNextLine()) {
                String query = replacedReader.nextLine();
                int cost = (int)connection.PlanCost(query);
                String scost = valueOf(cost);
                a = a.add(new BigInteger(scost));
                n++;
            }

            while (rewrittenReader.hasNextLine()) {
                String query = rewrittenReader.nextLine();
                int cost = (int)connection.PlanCost(query);
                String scost = valueOf(cost);
                b = b.add(new BigInteger(scost));
            }

            replacedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Number of rewritten queries : " + n);
        System.out.println("Estimated rows of queries : " + a);
        System.out.println("Estimated rows of queries using views : " + b);

    }

}
