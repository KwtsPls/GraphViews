package main.java.gr.uoa.di.entities.viewSelection.materialization;

import java.util.HashSet;
import java.util.Set;

import gr.uoa.di.entities.trie.TrieMetadata;
import main.java.gr.uoa.di.entities.viewSelection.queryRewriting.ViewForRewriting;

public class MetadataForMaterialization implements TrieMetadata<ViewForRewriting> {

	private Set<ViewForRewriting> viewMap = new HashSet<>();

	@Override
	public void updateOn(ViewForRewriting view) {
		viewMap.add(view);
	}

	public Set<ViewForRewriting> getSetOfRewritings() {
		return viewMap;
	}

	@Override
	public String toString() {
		return viewMap.toString();
	}

}
