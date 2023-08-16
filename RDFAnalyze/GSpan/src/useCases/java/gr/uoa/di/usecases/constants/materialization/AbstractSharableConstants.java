package useCases.java.gr.uoa.di.usecases.constants.materialization;

import java.io.File;
import java.io.IOException;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.helpStructures.iterators.ClosableIterator;
import interfaceAdapters.java.gr.uoa.di.entities.viewSelection.hierarchy.HierarchyVault;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.formatters.gSpan.serialization.GSpanDeserializer;
import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.patterns.RNDAccessPatterns;
import main.java.gr.uoa.di.entities.graph.PatternVS;
import main.java.gr.uoa.di.entities.viewSelection.hierarchy.PatternHierarchy;

public abstract class AbstractSharableConstants extends AbstractGlobalConstants implements ConstantsSharable {

	private static final String PATTERNS_DATA = "1.dBpediaPatterns";
	private static final String GRAPH_GROUPS = "1.dBpediaGraphGroups";
	private static final String HIERARCHY_VAULT = "2.hierarchyVault.json";
	//

	protected abstract String getSupportString();

	@Override
	final public boolean frequentPatternsExist() {
		return new File(getPatternLocation()).exists();
	}

	@Override
	abstract public int getMinimumSupport();

	abstract protected String getCacheCountFile();

	@Override
	final public RNDAccessPatterns getGroupedQueryMap(Dictionary dict) throws IOException {
		return RNDAccessPatterns.create(index(GRAPH_GROUPS, getSupportString()), data(GRAPH_GROUPS, getSupportString()),
				dict);
	}

	@Override
	final public File getGSpanFormatter() {
		return new File(data(GRAPH_GROUPS, getSupportString()) + "3");
	}

	@Override
	final public String getPatternLocation() {
		return data(PATTERNS_DATA, getSupportString());
	}

	@Override
	final public ClosableIterator<PatternVS> getPatternMap(Dictionary dict) throws IOException {
		GSpanDeserializer deserializer = GSpanDeserializer.create(dict);

		return deserializer.deSerializeCollection(getPatternLocation());
	}

	// Trie Serialization
	@Override
	final public void serializeHierarchy(PatternHierarchy hierarchy, Dictionary dict) throws IOException {
		try (HierarchyVault vault = HierarchyVault.create(file(HIERARCHY_VAULT, getSupportString()), dict)) {
			vault.serialize(hierarchy);
		}
	}

	@Override
	final public boolean existHieararchy() {
		return new File(file(HIERARCHY_VAULT, getSupportString())).exists();
	}

	@Override
	final public PatternHierarchy deserializeHierarchy(Dictionary dict) throws IOException {
		try (HierarchyVault vault = HierarchyVault.create(file(HIERARCHY_VAULT, getSupportString()), dict)) {
			return vault.deserialize();
		}
	}

}
