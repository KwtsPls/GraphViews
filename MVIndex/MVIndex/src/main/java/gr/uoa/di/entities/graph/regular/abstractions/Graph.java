package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.Iterator;

import gr.uoa.di.entities.graph.Printable;

public interface Graph extends Printable{
	Iterator<? extends Triple> getEdgeIterator();

}
