package gr.uoa.di.entities.graph.regular.abstractions;

import com.google.gson.JsonDeserializer;

import gr.uoa.di.entities.dictionary.Dictionary;

public interface GraphDeSerializer<G extends AbstractionForGraph<?,?,G>> extends JsonDeserializer<G>{
	
	public static <	N extends AbstractionForNode<N,T>,
					T extends AbstractionForTriple<N,T>,
					G extends AbstractionForGraph<N,T,G>> 
	
	GraphDeSerializer<G>  create(GraphFactory<N,T,G> factory, Dictionary dict) {
		return new _GraphDeserializer<N,T,G>(factory,dict);
	}
	

}
