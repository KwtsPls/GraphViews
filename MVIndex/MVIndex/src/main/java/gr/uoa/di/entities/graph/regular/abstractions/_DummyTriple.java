package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.Set;
import java.util.function.Function;

import gr.uoa.di.entities.graph.Printable;
import gr.uoa.di.entities.graph.regular.term.Term;

class _DummyTriple implements Triple{

	private Node subject;
	private Term predicate;
	private Node object;
	
	public _DummyTriple( Node subject, Term predicate,  Node object) {
		this.subject = subject;;
		this.predicate =predicate;
		this.object = object;
	}

	
	@Override
	public Node getSubject() {
		return subject;
	}	

	@Override
	public Term getPredicate() {
		return predicate;
	}

	
	@Override
	public Node getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return toCompactString();
	}

	@Override
	public boolean isVisited() {
		return false;
	}

	@Override
	public void visit() {
	}
	
	@Override
	public int getPredicateLabel() {
		return predicate.getLabel();
	}
	
	@Override
	public int getEdgeId() {
		return 0;
	}

	
	@Override
	public int getVarCount() {
		return 0;
	}

	
	@Override
	public void resetForSerialization() {
	}
	
	@Override
	public Set<Node> getVariables() {
		return null;
	}

	@Override
	public String print(Function<Printable, String> function) {
		return new StringBuilder("(").append(function.apply(subject)).append(',').append(function.apply(predicate))
				.append(',').append(function.apply(object)).append(')').toString();
	}


}
