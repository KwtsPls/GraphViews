package gr.uoa.di.entities.graph.regular.abstractions;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.function.BiConsumer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.dictionary.Variable;
import gr.uoa.di.entities.helpStructures.tuples.IntTriple;

public class _GraphSerializer<N extends AbstractionForNode<N, T>, T extends AbstractionForTriple<N, T>, G extends AbstractionForGraph<N, T, G>>
		implements GraphSerializer<G> {
	
	BiConsumer<IntTriple,T> consumer = null;
	
	public _GraphSerializer() {
		
	}
	
	public _GraphSerializer(BiConsumer<IntTriple,T> consumer) {
		this.consumer = consumer;
	}

	@Override
	public JsonElement serialize(G graph, Type typeOfSrc, JsonSerializationContext context) {
		JsonArray arrayGraph = new JsonArray();
		BiMap<N, Integer> varNodesToIds =  HashBiMap.create();
		Iterator<Variable> iter = Dictionary.getVarIdIterator();
		// Adding Head
		graph.forEach(triple -> {
			JsonArray tripleArray = new JsonArray();
			int subjId = getNodeId(varNodesToIds,iter,triple.getSubject());
			int predId = triple.getPredicateLabel();
			int objId = getNodeId(varNodesToIds,iter,triple.getObject());
			tripleArray.add(subjId);
			tripleArray.add(predId);
			tripleArray.add(objId);
			arrayGraph.add(tripleArray);
			if(consumer!=null) 
				consumer.accept(IntTriple.of(subjId,predId,objId), triple);			
		});
		return arrayGraph;                  
	}

	private int getNodeId(BiMap<N, Integer> varNodesToIds, Iterator<Variable> iter, N node) {
		if(node.isConstant())
			return node.getLabel();
		else {			
			int nodeId = node.isLabeledVariable()?varNodesToIds.computeIfAbsent(node, x->x.getLabel()) : varNodesToIds.computeIfAbsent(node, x->iter.next().getId());
			return nodeId;
		}
	}

	
}
