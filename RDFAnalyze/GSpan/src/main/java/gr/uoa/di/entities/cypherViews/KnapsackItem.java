package main.java.gr.uoa.di.entities.cypherViews;

import main.java.gr.uoa.di.entities.viewSelection.edgeRewriting.TripleMap;

import java.util.List;

public class KnapsackItem {
    double benefit;
    int cost;
    CypherQuery query;
    List<TripleMap> mappings;

    public KnapsackItem(double benefit,int cost,CypherQuery query,List<TripleMap> mappings){
        this.benefit = benefit;
        this.cost = cost;
        this.query = query;
        this.mappings = mappings;
    }

    public KnapsackItem(){
        this.benefit=0.0;
        this.cost=0;
    }

    public CypherQuery getQuery() {
        return query;
    }

    public double getBenefit() {
        return benefit;
    }

    public int getCost() {
        return cost;
    }

    public List<TripleMap> getMappings() {
        return mappings;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
    }
}
