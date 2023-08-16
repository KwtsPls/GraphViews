package useCases.java.gr.uoa.di.usecases.constants.materialization;

import java.io.IOException;

import gr.uoa.di.entities.dictionary.Dictionary;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.patterns.RNDAccessPatterns;
import main.java.gr.uoa.di.entities.viewSelection.materialization.TrieIndexForMaterialization;

public interface Constants extends ConstantsGlobal, ConstantsSharable {

	@Override
	RNDAccessPatterns getGroupedQueryMap(Dictionary dict) throws IOException;

	RNDAccessPatterns getCleanedPatternMap(Dictionary dict) throws IOException;

	// Trie
	void serializeTrie(TrieIndexForMaterialization trie, Dictionary dict) throws IOException;

	TrieIndexForMaterialization deserializeTrie(Dictionary dict) throws IOException;

	boolean trieIndexExists();

	// Set<Integer> deserializeBenefitedQueryIds() throws IOException;

	// Getting View-Materialization-related metadata
	@Override
	int getMinimumSupport();

	boolean allowsOnlyPrimordialViews();

}
