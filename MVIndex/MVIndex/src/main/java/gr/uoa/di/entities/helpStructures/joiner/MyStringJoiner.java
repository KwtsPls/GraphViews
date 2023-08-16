package gr.uoa.di.entities.helpStructures.joiner;

import java.util.Iterator;
import java.util.function.Function;

import gr.uoa.di.entities.helpStructures.iterators.IteratorNonNull;

public class MyStringJoiner {
	
	public static<C> String join(CharSequence delimiter,Function<C, String> preprocessing, Iterable<C> iterable) {
		return String.join(delimiter, getIterableString(iterable, preprocessing));
	}
	
	public static<C> String joinNonNull(CharSequence delimiter,Function<C, String> preprocessing, Iterable<C> iterable) {
		return String.join(delimiter,getIterableStringNonNull(iterable, preprocessing));
	}
	
	static private <C> Iterable<String> getIterableString(Iterable<C> iterable, Function<C, String> stringFunction) {
		return getIterableString(iterable.iterator(),stringFunction);
	}
	
	static private <C> Iterable<String> getIterableStringNonNull(Iterable<C> iterable, Function<C, String> stringFunction) {
		return getIterableStringNonNull(iterable.iterator(),stringFunction);
	}
	
	
	
	static private <C> Iterable<String> getIterableString(Iterator<C> iterator, Function<C, String> stringFunction) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {				
				return new InnerIterator<C>(iterator,stringFunction);
			}
		};
	}
	
	static private <C> Iterable<String> getIterableStringNonNull(Iterator<C> iterator, Function<C, String> stringFunction) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {				
				return IteratorNonNull.create(new InnerIterator<C>(iterator,stringFunction));
			}
		};
	}
	
	static private class InnerIterator<C> implements Iterator<String>{
		Iterator<C> innerIterator;
		Function<C, String> stringFunction;
		
		public InnerIterator(Iterator<C> iterator, Function<C, String> stringFunction) {
			this.innerIterator = iterator;
			this.stringFunction = stringFunction;
		}
		
		@Override
		public boolean hasNext() {
			return innerIterator.hasNext();
		}

		@Override
		public String next() {
			return stringFunction.apply(innerIterator.next());
		}
		
	}
}
