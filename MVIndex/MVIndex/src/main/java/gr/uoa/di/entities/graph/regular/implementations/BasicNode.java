package gr.uoa.di.entities.graph.regular.implementations;

import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForNode;
import gr.uoa.di.entities.graph.regular.term.Term;

//
public class BasicNode extends AbstractionForNode<BasicNode,BasicTriple>{

	BasicNode(Term term) {
		super(term);
	}

	@Override
	public BasicNode getThis() {
		return this;
	}

}
