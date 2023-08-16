package main.java.gr.uoa.di.entities.viewSelection.materialization;

import gr.uoa.di.entities.trie.TrieIndex;
import main.java.gr.uoa.di.entities.viewSelection.queryRewriting.ViewForRewriting;

public class TrieIndexForMaterialization extends TrieIndex<ViewForRewriting, MetadataForMaterialization> {

	@Override
	public MetadataForMaterialization createMetadataInstance() {
		return new MetadataForMaterialization();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
