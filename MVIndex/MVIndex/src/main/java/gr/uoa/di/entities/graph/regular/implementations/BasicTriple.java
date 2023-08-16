package gr.uoa.di.entities.graph.regular.implementations;

import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForNode;
import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForTriple;
import gr.uoa.di.entities.graph.regular.term.Term;

public class BasicTriple extends AbstractionForTriple<BasicNode,BasicTriple>{

	BasicTriple(AbstractionForNode<BasicNode, BasicTriple> subject, Term predicate,
			AbstractionForNode<BasicNode, BasicTriple> object) {
		super(subject, predicate, object);
	}

	@Override
	public BasicTriple getThis() {
		return this;
	}

}
