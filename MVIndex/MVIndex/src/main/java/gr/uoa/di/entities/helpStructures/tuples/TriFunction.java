package gr.uoa.di.entities.helpStructures.tuples;

public interface TriFunction<V,W,Z,T> {
	
	T apply(V v, W w, Z z);

}
