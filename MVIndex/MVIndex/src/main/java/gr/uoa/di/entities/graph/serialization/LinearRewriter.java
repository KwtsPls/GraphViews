package gr.uoa.di.entities.graph.serialization;

import java.util.Iterator;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.dictionary.Variable;
import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForGraph;
import gr.uoa.di.entities.graph.regular.abstractions.Node;
import gr.uoa.di.entities.graph.regular.abstractions.Triple;


public class LinearRewriter {

	public static LinearizedQuery linearize(AbstractionForGraph<?,?,?> graphSimple) {
		AbstractionForGraph<?,?,?> query = graphSimple;
		query.resetForSerialization();
		LinearizedQuery serializedForm = new LinearizedQuery();
		// int varCounter = 0;
		Iterator<Variable> iter = Dictionary.getVarIdIterator();
		//		
		Node anchor = query.getAnchorNode();
		anchor.setVarEnumeration2(iter);
		serializedForm.add(anchor.getLabel());
		visit(anchor, serializedForm, iter);
		graphSimple.reArrangeVarNodes();
		return serializedForm;
	}

	private static void visit(Node node, LinearizedQuery serializedForm, Iterator<Variable> iter) {
		node.visit();
		for (Triple edge : node.getOutgoingTriples()) {
			if (edge.isVisited())
				continue;
			//
			edge.visit();
			edge.getPredicate().setVarEnumeration2(iter);
			edge.getObject().setVarEnumeration2(iter);
			//
			serializedForm.add(edge.getPredicate().getLabel());
			serializedForm.add(edge.getObject().getLabel());
			if (!edge.getObject().isVisited())
				visit(edge.getObject(), serializedForm, iter);
		}
		boolean noIncomingEdge = true;
		for (Triple edge : node.getIncomingTriples()) {
			if (edge.isVisited())
				continue;
			//
			if (noIncomingEdge) {
				serializedForm.add(Dictionary.invEdgeMark);
				noIncomingEdge = false;
			}
			edge.visit();
			edge.getPredicate().setVarEnumeration2(iter);
			edge.getSubject().setVarEnumeration2(iter);
			//
			serializedForm.add(edge.getPredicate().getLabel());
			serializedForm.add(edge.getSubject().getLabel());
			//
			if (!edge.getSubject().isVisited())
				visit(edge.getSubject(), serializedForm, iter);

		}
		serializedForm.add(Dictionary.endEdgeMark);
	}

}
