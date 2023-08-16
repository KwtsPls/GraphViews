package main.java.gr.uoa.di.entities.viewSelection.hierarchy;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import gr.uoa.di.entities.helpStructures.tuples.MyPair;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.patterns.RNDAccessPatterns;
import main.java.gr.uoa.di.entities.graph.PatternVS;
import main.java.gr.uoa.di.entities.viewSelection.edgeRewriting.TripleMap;

public interface PatternHierarchy {

	public static __PatTrie create(Iterator<PatternVS> patterns, RNDAccessPatterns groupedQueries) {
		return new __PatTrie(patterns, groupedQueries);
	}

	public static PatternHierarchy create(__PatTrie trie) {
		return new _PatternHierarchy(trie);
	}

	int getPatternCount();

	void removeAutomorphisms();

	void removeNonClosedPatterns(double tolerance);

	void normalizeSupport();

	void forEachPatternContainedPatterns(BiConsumer<PatternVS, Iterator<MyPair<PatternVS, List<TripleMap>>>> consumer);

}
