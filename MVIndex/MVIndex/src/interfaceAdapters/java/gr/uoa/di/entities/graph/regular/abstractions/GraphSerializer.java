package gr.uoa.di.entities.graph.regular.abstractions;

import com.google.gson.JsonSerializer;

public interface GraphSerializer<G extends AbstractionForGraph<?,?,G>> extends  JsonSerializer<G> {
	
	public static <	N extends AbstractionForNode<N,T>,
					T extends AbstractionForTriple<N,T>,
					G extends AbstractionForGraph<N,T,G>> 
	
	GraphSerializer<G>  create() {
		return new _GraphSerializer<N,T,G>();
	}
	

}
