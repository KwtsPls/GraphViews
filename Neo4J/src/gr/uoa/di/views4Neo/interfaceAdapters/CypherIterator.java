package gr.uoa.di.views4Neo.interfaceAdapters;

import java.util.Iterator;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.graph.regular.implementations.BasicGraph;
import gr.uoa.di.entities.graph.regular.implementations.BasicGraphFactory;
import gr.uoa.di.interfaceAdapters.iterators.dbPedia.DBPediaGraphQueryIterator;
import gr.uoa.di.interfaceAdapters.workloads.JenaGraphQueryIterator;

public class CypherIterator implements Iterator<String> {
	JenaGraphQueryIterator<BasicGraph> iter;

	public static void main(String[] args) {
		Dictionary dict = Dictionary.create();
		try (JenaGraphQueryIterator<BasicGraph> iter = DBPediaGraphQueryIterator.create(dict,
				BasicGraphFactory.create());) {
			CypherIterator cypherIterator = new CypherIterator(iter);
			while (cypherIterator.hasNext()) {
				System.out.println(cypherIterator.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	CypherIterator(JenaGraphQueryIterator<BasicGraph> iter) {
		this.iter = iter;
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public String next() {
		BasicGraph graph = iter.next();
		return graph == null ? null : new CyperQueryTranslator(graph).toString();
	}

}
