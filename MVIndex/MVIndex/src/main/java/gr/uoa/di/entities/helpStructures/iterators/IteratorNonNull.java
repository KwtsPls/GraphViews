package gr.uoa.di.entities.helpStructures.iterators;

import java.util.Iterator;

public interface IteratorNonNull<C> extends Iterator<C>{
	public static <C> IteratorNonNull<C> create(Iterator<C> iterator){
		return new _IteratorNonNull<C>(iterator);
		
	}
}
