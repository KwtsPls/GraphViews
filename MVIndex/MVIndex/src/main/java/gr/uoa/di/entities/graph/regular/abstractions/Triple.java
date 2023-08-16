package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.Set;

import gr.uoa.di.entities.graph.Printable;
import gr.uoa.di.entities.graph.regular.term.Term;

public interface Triple extends Printable {

	public static Triple createDummy(Node subject, Term predicate, Node object) {
		return new _DummyTriple(subject, predicate, object);
	}

	public Node getSubject();

	public Term getPredicate();

	public Node getObject();

	public boolean isVisited();

	public void visit();

	public int getPredicateLabel();

	public int getEdgeId();

	public int getVarCount();

	public void resetForSerialization();

	public Set<? extends Node> getVariables();

}
