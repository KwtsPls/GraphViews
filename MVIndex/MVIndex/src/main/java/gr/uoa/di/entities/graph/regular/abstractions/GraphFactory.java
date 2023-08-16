package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.graph.regular.term.Term;
import gr.uoa.di.entities.helpStructures.tuples.TriFunction;

public interface GraphFactory<N extends AbstractionForNode<N, T>, T extends AbstractionForTriple<N, T>, G extends AbstractionForGraph<N, T, G>> {
	static <N extends AbstractionForNode<N, T>, T extends AbstractionForTriple<N, T>, G extends AbstractionForGraph<N, T, G>> GraphFactory<N, T, G> create(
			Supplier<G> graphSupplier, TriFunction<N, Term, N, T> tripleFunction, Function<Object, N> varFunction,
			BiFunction<Integer, Object, N> constantFunction) {
		return new _GraphFactory<N, T, G>(graphSupplier, tripleFunction, varFunction, constantFunction);
	}
	
	public GraphConstructor<N,T,G> getGraphConstructor(Dictionary dict);
}
