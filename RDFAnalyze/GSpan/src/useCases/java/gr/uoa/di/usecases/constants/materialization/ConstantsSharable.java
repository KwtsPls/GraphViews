package useCases.java.gr.uoa.di.usecases.constants.materialization;

import java.io.File;
import java.io.IOException;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.helpStructures.iterators.ClosableIterator;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.patterns.RNDAccessPatterns;
import main.java.gr.uoa.di.entities.graph.PatternVS;
import main.java.gr.uoa.di.entities.viewSelection.hierarchy.PatternHierarchy;

public interface ConstantsSharable extends ConstantsGlobal {

	boolean frequentPatternsExist();

	int getMinimumSupport();

	RNDAccessPatterns getGroupedQueryMap(Dictionary dict) throws IOException;

	File getGSpanFormatter();

	String getPatternLocation();

	ClosableIterator<PatternVS> getPatternMap(Dictionary dict) throws IOException;

	// Hierarchy Related Constants
	void serializeHierarchy(PatternHierarchy hierarchy, Dictionary dict) throws IOException;

	boolean existHieararchy();

	PatternHierarchy deserializeHierarchy(Dictionary dict) throws IOException;

}
