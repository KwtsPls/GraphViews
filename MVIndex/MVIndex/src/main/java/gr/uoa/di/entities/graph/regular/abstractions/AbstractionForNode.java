package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.dictionary.Variable;
import gr.uoa.di.entities.graph.Printable;
import gr.uoa.di.entities.graph.regular.helpers.ComparatorsForGraph;
import gr.uoa.di.entities.graph.regular.term.Term;

public abstract class AbstractionForNode<N extends AbstractionForNode<N,T>,T extends AbstractionForTriple<N,T>> implements  Node{

	private List<T> incomingEdges = null;
	private List<T> outgoingEdges = null;
	private boolean isChecked = false;
	private int positionInGraph;
	private Term term;

	public AbstractionForNode(Term term) {
		this.term = term;
	}

	public abstract N getThis();
	
	@Override
	public void sortEdges() {
		if (outgoingEdges != null)
			outgoingEdges.sort(ComparatorsForGraph._tripleComparator);
		if (incomingEdges != null)
			incomingEdges.sort(ComparatorsForGraph._tripleComparator);

	}

	
	@Override
	public boolean isVisited() {
		return isChecked;
	}

	
	@Override
	public void visit() {
		isChecked = true;
	}

	
	@Override
	public boolean hasOnlyConstantPredicates() {
		if (incomingEdges != null) {
			for (T edge : incomingEdges) {
				if (edge.getPredicate().isVariable())
					return false;
			}
		}
		if (outgoingEdges != null) {
			for (T edge : outgoingEdges) {
				if (edge.getPredicate().isVariable())
					return false;
			}
		}
		return true;
	}

	
	@Override
	public boolean isVariable() {
		return term.isVariable();
	}
	
	@Override
	public boolean isLabeledVariable() {
		return term.isLabeledVariable();
	}

	
	@Override
	public boolean isConstant() {
		return term.isConstant();
	}
	
	@Override
	public int getLabel() {
		return term.getLabel();
	}
	
	@Override
	public Object getItem() {
		return term.getItem();
	}

	
	@Override
	public Term getTerm() {
		return term;
	}

	
	@Override
	public void setVarEnumeration2(Iterator<Variable> iter) {
		term.setVarEnumeration2(iter);
	}

	@Override
	public void resetForSerialization() {
		isChecked = false;
		if(term.isVariable())
			term.setLabel(Dictionary.variableLabel);
	}

	
	@Override
	public int getTripleCount() {
		return (incomingEdges == null ? 0 : incomingEdges.size()) + (outgoingEdges == null ? 0 : outgoingEdges.size());
	}
	
	@Override
	public List<T> getIncomingTriples() {
		if (incomingEdges == null)
			return Collections.emptyList();
		return incomingEdges;
	}
	
	
	@Override
	public List<T> getOutgoingTriples() {
		if (outgoingEdges == null)
			return Collections.emptyList();
		return outgoingEdges;
	}
	
	@Override
	public int getPositionInGraph() {
		return positionInGraph;
	}

	
	@Override
	public void setPositionInGraph(int positionInGraph) {
		this.positionInGraph = positionInGraph;
	}

	
	void addTriple(AbstractionForTriple<N,T> triple2) {
		T triple = triple2.getThis();
		if (this == triple.getSubject()) {
			if (outgoingEdges == null) {
				outgoingEdges = new ArrayList<>();
			}
			outgoingEdges.add(triple);
		} else {
			if (incomingEdges == null) {
				incomingEdges = new ArrayList<>();
			}
			incomingEdges.add(triple);
		}
	}
	
	@Override
	public String print(Function<Printable, String> function) {		
		return function.apply(term);
	}
	
	@Override
	public String toString() {
		return toCompactString();
	}

	@Override
	public void setLabel(int nodeId) {
		term.setLabel(nodeId);
	}
	
	@Override
	public int countEdges() {	
		return (incomingEdges!=null?incomingEdges.size():0)+(outgoingEdges!=null?outgoingEdges.size():0);
	}
}
