package main.java.gr.uoa.di.entities.cypherViews;

import main.java.gr.uoa.di.entities.viewSelection.edgeRewriting.TripleMap;

import java.util.List;

public class CypherIndex {
    String viewName;
    String edge;
    String deletionQuery;
    List<TripleMap> map;
    Double benefit;

    public CypherIndex(String viewName,String edge,String deletionQuery,List<TripleMap> map,Double benefit){
        this.viewName = viewName;
        this.edge = edge;
        this.deletionQuery = deletionQuery;
        this.map = map;
        this.benefit = benefit;
    }

    @Override
    public String toString(){
        String s=null;
        char del=6;
        String mapString=null;
        for(TripleMap m:map){
            mapString = m.toFullString()+"&&&";
        }
        s=viewName+del+edge+del+deletionQuery+del+mapString+del+benefit;
        return s;
    }

}
