package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.List;

import gr.uoa.di.entities.graph.Printable;
import gr.uoa.di.entities.graph.regular.term.Term;

public interface Node extends Term,Printable{

	 public void sortEdges();

	 public boolean isVisited();

	 public void visit();

	 public boolean hasOnlyConstantPredicates();

	 public void resetForSerialization();

	 public int getTripleCount();

	 public List<? extends Triple> getIncomingTriples();

	 public List<? extends Triple> getOutgoingTriples();

	 public int getPositionInGraph();

	 public void setPositionInGraph(int positionInGraph);
	
	 public int countEdges();

}
