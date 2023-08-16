package useCases.java.gr.uoa.di.usecases.constants.dbpedia;

import java.io.File;
import java.io.IOException;

import gr.uoa.di.entities.dictionary.Dictionary;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.dataVault.object.trie.TrieVault;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.patterns.RNDAccessPatterns;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.gateways.csv.CsvWriter;
import main.java.gr.uoa.di.entities.viewSelection.materialization.TrieIndexForMaterialization;
import useCases.java.gr.uoa.di.usecases.constants.experiments.ConstantForExperiments;
import useCases.java.gr.uoa.di.usecases.constants.materialization.AbstractSharableConstants;
import useCases.java.gr.uoa.di.usecases.constants.materialization.Constants;

public class DBPediaPrimordialConstants extends AbstractSharableConstants implements Constants, ConstantForExperiments {
	//
	private String availableSizeString;
	final private String supportString;
	final private int support;
	private static final String CACHE_COUNT = "constants/cacheCount";
	private final String ROOT;
	private final String STATISTICS = "statistics";

	public static DBPediaPrimordialConstants create(String rootLocation, int support) {
		return new DBPediaPrimordialConstants(rootLocation, support);
	}

	//
	private DBPediaPrimordialConstants(String rootLocation, int support) {
		this.supportString = "support" + Integer.toString(support);
		this.support = support;
		this.ROOT = rootLocation;
	}

	private static final String CLEANED_PATTERNS = "2.dBpediaCleanedPatterns";

	private static final String TRIE_FILE = "primordial/4.trieIndex";

	@Override
	public RNDAccessPatterns getCleanedPatternMap(Dictionary dict) throws IOException {
		return RNDAccessPatterns.create(index(CLEANED_PATTERNS, supportString), data(CLEANED_PATTERNS, supportString),
				dict);
	}

	@Override
	public void serializeTrie(TrieIndexForMaterialization trie, Dictionary dict) throws IOException {
		try (TrieVault vault = TrieVault.create(file(TRIE_FILE, supportString, availableSizeString), dict)) {
			vault.serialize(trie);
		}

	}

	// Materialized Views index
	@Override
	public TrieIndexForMaterialization deserializeTrie(Dictionary dict) throws IOException {
		try (TrieVault vault = TrieVault.create(file(TRIE_FILE, supportString, availableSizeString), dict)) {
			return vault.deserialize();
		}
	}

	@Override
	public boolean trieIndexExists() {
		return new File(file(TRIE_FILE, supportString, availableSizeString)).exists();
	}

	@Override
	public boolean allowsOnlyPrimordialViews() {
		return true;
	}

	@Override
	public int getMinimumSupport() {
		return support;
	}

	@Override
	public String getSupportString() {
		return supportString;
	}

	@Override
	protected String getCacheCountFile() {
		return CACHE_COUNT;
	}

	@Override
	protected String getRoot() {
		return ROOT;
	}

	@Override
	public CsvWriter getCsvWriter() throws IOException {
		return CsvWriter.create(getCsvFile());
	}

	@Override
	public CsvWriter getCsvWriter(boolean append) throws IOException {
		return CsvWriter.create(getCsvFile(), append);
	}

	@Override
	public boolean existsCsvFile() {
		return new File(getCsvFile()).exists();
	}

	@Override
	public String getCsvFile() {
		return file("primordial/" + STATISTICS, supportString, availableSizeString) + ".csv";
	}

}
