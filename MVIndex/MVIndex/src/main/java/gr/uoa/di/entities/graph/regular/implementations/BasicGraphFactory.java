package gr.uoa.di.entities.graph.regular.implementations;

import gr.uoa.di.entities.graph.regular.abstractions.GraphFactory;
import gr.uoa.di.entities.graph.regular.term.Term;

public interface BasicGraphFactory extends GraphFactory<BasicNode, BasicTriple, BasicGraph> {

	static GraphFactory<BasicNode, BasicTriple, BasicGraph> create() {

		return GraphFactory.create(() -> new BasicGraph(), (x, y, z) -> new BasicTriple(x, y, z),
				var -> new BasicNode(Term.getVarTerm(var)), (id,var)->new BasicNode(Term.getConstantTerm(id, var)));
	}

}
