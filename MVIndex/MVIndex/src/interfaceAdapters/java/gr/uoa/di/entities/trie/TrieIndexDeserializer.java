package gr.uoa.di.entities.trie;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;





public class TrieIndexDeserializer<T,M extends TrieMetadata<T>> implements JsonDeserializer<TrieIndex<T, M>>{

	@Override
	public TrieIndex<T, M> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Type type = new TypeToken<TrieIndex<T, M>>() {
		}.getType();
		return context.deserialize(json, type);
	}

}
