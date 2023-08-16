package main.java.gr.uoa.di.entities.viewSelection._steps;

import main.java.gr.uoa.di.entities.cypherViews.CypherBenefitModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Step4_ViewCleaning {

    public static void cleanIndexes(){
        try {
            File myObj = new File("tmp/index_256_1000.csv");
            Scanner myReader = new Scanner(myObj);
            CypherBenefitModel bnftModel = new CypherBenefitModel("bolt://localhost:7687", "neo4j", "equator-alpine-mobile-panic-lorenzo-1219");
            char del=6;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String arr[] = data.split(""+del+"");
                System.out.println("Deleting index: " + arr[0]);
                bnftModel.deletionQuery(arr[2]);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
