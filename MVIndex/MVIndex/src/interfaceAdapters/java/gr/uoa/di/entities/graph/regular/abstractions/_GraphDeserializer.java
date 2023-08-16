package gr.uoa.di.entities.graph.regular.abstractions;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.helpStructures.tuples.IntTriple;

public class _GraphDeserializer<N extends AbstractionForNode<N, T>, T extends AbstractionForTriple<N, T>, G extends AbstractionForGraph<N, T, G>>
		implements GraphDeSerializer<G> {
	GraphFactory<N, T, G> factory;
	Dictionary dict;
	BiConsumer<IntTriple,T> consumer = null;

	public _GraphDeserializer(GraphFactory<N, T, G> factory, Dictionary dict) {
		this.factory = factory;
		this.dict = dict;
	}
	
	public _GraphDeserializer(GraphFactory<N, T, G> factory, Dictionary dict, BiConsumer<IntTriple,T> consumer) {
		this.factory = factory;
		this.dict = dict;
		this.consumer = consumer;
	}

	@Override
	public G deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		GraphConstructor<N, T, G> constructor = factory.getGraphConstructor(dict);
		json.getAsJsonArray().forEach(tripleOfIds -> {
			JsonArray array = tripleOfIds.getAsJsonArray();
			int subjectId = array.get(0).getAsInt();
			int predicateId = array.get(1).getAsInt();
			int objectId = array.get(2).getAsInt();
			T triple = constructor.addTripleFromInt(subjectId, predicateId, objectId);
			if(consumer!=null)
				consumer.accept(IntTriple.of(subjectId,predicateId,objectId), triple);
		});
		return constructor.getGraphQuery();
	}
	
}
