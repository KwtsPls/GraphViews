package gr.uoa.di.entities.graph.regular.implementations;

import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForGraph;

public class BasicGraph extends AbstractionForGraph<BasicNode,BasicTriple,BasicGraph>{

	public BasicGraph() {
		super();
	}

	@Override
	public BasicGraph getThis() {
		return this;
	}
}
