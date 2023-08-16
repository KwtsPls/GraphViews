package useCases.java.gr.uoa.di.usecases.constants.materialization;

import java.io.IOException;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.interfaceAdapters.workloads.JenaGraphQueryIterator;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.patterns.RNDAccessPatterns;
import main.java.gr.uoa.di.entities.graph.GraphVS;
import main.java.gr.uoa.di.entities.gspan.infrequentLabelRemoval.GSPanPreprocessor;

public interface ConstantsGlobal {

	boolean dictionaryExists();

	// Get Query Iterator based on a Jena parser instance
	JenaGraphQueryIterator<GraphVS> getJenaQueryIterator(Dictionary dict);

	// Contains all the queries encoded in GSPanForm
	RNDAccessPatterns getQueryMap(Dictionary dict) throws IOException;

	// Serialized/DeSerializes a dictionary to a file
	void serializeDictionary(Dictionary dict) throws IOException;

	Dictionary deserializeDictionary() throws IOException;

	// Serializing/DeSerializing the GSPanPreprocessor
	GSPanPreprocessor deserializeGSPanPreprocessor() throws IOException;

	void serializeGSPanPreprocessor(GSPanPreprocessor preprocessor) throws IOException;

	//
	RNDAccessPatterns getQueriesForTesting(Dictionary dict) throws IOException;

	void deleteQueriesForTesting();

	boolean queriesForTestingExist();

}
