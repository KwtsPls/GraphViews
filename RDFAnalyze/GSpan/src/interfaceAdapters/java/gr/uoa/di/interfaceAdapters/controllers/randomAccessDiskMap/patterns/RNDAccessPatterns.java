package interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.patterns;

import java.io.IOException;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.interfaceAdapters.controllers.randomAccessDiskMap.RNDAccessMap;
import interfaceAdapters.java.gr.uoa.di.entities.graph.GraphVsDeserializer;
import interfaceAdapters.java.gr.uoa.di.entities.graph.GraphVsSerializer;
import main.java.gr.uoa.di.entities.graph.GraphVS;
import main.java.gr.uoa.di.entities.graph.PatternVS;

public class RNDAccessPatterns

		extends RNDAccessMap<Integer, PatternVS> {

	private Dictionary dict;

	private RNDAccessPatterns(String indexFile, String dataFile, Dictionary dict) throws IOException {
		this.dict = dict;
		super.initialize(indexFile, dataFile, Integer.class, PatternVS.class);

	}

	@Override
	public void registerTypeAdapters() {
		GraphVsSerializer serializer = GraphVsSerializer.create();
		GraphVsDeserializer deSerializer = GraphVsDeserializer.create(dict);
		this.getBuilder().registerTypeAdapter(GraphVS.class, serializer);
		this.getBuilder().registerTypeAdapter(GraphVS.class, deSerializer);
	}

	public static RNDAccessPatterns create(String indexFile, String dataFile, Dictionary dict) throws IOException {
		return new RNDAccessPatterns(indexFile, dataFile, dict);
	}
}
