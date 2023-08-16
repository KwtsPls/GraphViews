package gr.uoa.di.views4Neo.interfaceAdapters;

import java.net.URI;

public class CypherEdge {
	CypherNode subject;
	CypherNode object;
	URI uri;

	public CypherEdge(CypherNode subject, URI uri, CypherNode object) {
		super();
		this.subject = subject;
		this.object = object;
		this.uri = uri;
	}

	@Override
	public String toString() {
		return new StringBuilder(subject.toString()).append("-[:`").append(uri).append("`]->").append(object)
				.toString();
	}

}
