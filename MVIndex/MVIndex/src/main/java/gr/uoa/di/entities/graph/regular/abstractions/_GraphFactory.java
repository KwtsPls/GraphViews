package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.graph.regular.term.Term;
import gr.uoa.di.entities.helpStructures.tuples.TriFunction;

class _GraphFactory<N extends AbstractionForNode<N,T>,T extends AbstractionForTriple<N,T>,G extends AbstractionForGraph<N,T,G>> implements GraphFactory<N,T,G> {
	
	_GraphFactory(Supplier<G> graphSupplier, TriFunction<N, Term, N, T> tripleFunction,
			Function<Object, N> varFunction, BiFunction<Integer, Object, N> constantFunction) {
		super();
		this.graphSupplier = graphSupplier;
		this.tripleFunction = tripleFunction;
		this.varFunction = varFunction;
		this.constantFunction = constantFunction;
	}

	private Supplier<G> graphSupplier;
	
	private TriFunction<N,Term,N,T> tripleFunction;
	
	private Function<Object,N> varFunction;
	
	private BiFunction<Integer,Object,N> constantFunction;
	
	@Override
	public GraphConstructor<N,T,G> getGraphConstructor(Dictionary dict2) {
		return new _GraphConstructor(dict2);
	}
	
	private class _GraphConstructor implements GraphConstructor<N,T,G> {
		private Dictionary dict;
		private HashMap<Object, N> nodeIndex=new HashMap<Object, N> ();
		private G graph;

		private _GraphConstructor(Dictionary dict2) {
			this.dict = dict2;
			this.graph= graphSupplier.get();
		}
		
		private N getNodeFromJObject(Object term) {
			return nodeIndex.compute(term, (x,y)->{
				if(y!=null) return y;
				if(term.toString().startsWith("?")) {
					N node=varFunction.apply(term);
					graph.addNode(node);
					return node;
				}else {
					N node = null;
					try {
						node = constantFunction.apply(dict.getIdOfConstant(term), term);
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
					graph.addNode(node);
					return node;
				}
			});
		}
		
		private N getNodeFromInt(int nodeId) {
			return getNodeFromInt(nodeId,null);
		}
		
		private N getNodeFromInt(int nodeId,Consumer<N> consumer) {
			Object term = dict.getContantOfId(nodeId);
			return nodeIndex.compute(term, (x,y)->{
				if(y!=null) return y;
				N node;
				if(term.toString().startsWith("?")) {
					node=varFunction.apply(term);
					node.setLabel(nodeId);					
				}else {
					node=constantFunction.apply(nodeId, term);
				}
				if(consumer!=null)
					consumer.accept(node);
				graph.addNode(node);
				return node;
			});
		}
		
		
		
		private Term getTermFromJObject(Object term) {
			return nodeIndex.compute(term, (x,y)->{
				if(y!=null) return y;
				if(term.toString().startsWith("?")) {
					return varFunction.apply(term);
				}else {
					try {
						return constantFunction.apply(dict.getIdOfConstant(term), term);
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
					return null;
				}
			}).getTerm();
		}
		
		protected Term getTermFromInt(int termId) {
			Object term = dict.getContantOfId(termId);
			return nodeIndex.compute(term, (x,y)->{
				if(y!=null) return y;
				if(term.toString().startsWith("?")) {
					return varFunction.apply(term);
				}else {
					return constantFunction.apply(termId, term);
				}
			}).getTerm();
		}

		@Override
		public void addTripleFromJObjects(Object subject, Object predicate, Object object) {
			addTriple(getNodeFromJObject(subject),getTermFromJObject(predicate),getNodeFromJObject(object));
		}
		
		@Override
		public T addTripleFromInt(int subjectId, int predicateId, int objectId) {			
			return addTriple(getNodeFromInt(subjectId),getTermFromInt(predicateId),getNodeFromInt(objectId));
		}
		
		private T addTriple(N subject, Term predicate, N object) {
			T triple= tripleFunction.apply(subject, predicate, object);
			subject.addTriple(triple);
			object.addTriple(triple);
			return triple;
		}

		@Override
		public G getGraphQuery() {
			graph.sortGraph();
			return graph;
		}

		
	}

}
