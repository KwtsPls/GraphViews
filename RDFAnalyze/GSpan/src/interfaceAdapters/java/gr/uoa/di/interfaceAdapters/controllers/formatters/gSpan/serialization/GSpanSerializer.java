package interfaceAdapters.java.gr.uoa.di.interfaceAdapters.controllers.formatters.gSpan.serialization;

import main.java.gr.uoa.di.entities.graph.PatternVS;

import java.io.IOException;
import java.util.Iterator;

public interface GSpanSerializer {

	static GSpanSerializer create(String filename) {
		return new _GSpanSerializer(filename);
	}

	void serializeCollection(Iterator<PatternVS> valueIterator) throws IOException;

}
