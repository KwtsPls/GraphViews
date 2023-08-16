package gr.uoa.di.entities.dictionary;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Node_Literal;

import com.google.common.collect.BiMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonDeserializer;

import gr.uoa.di.interfaceAdapters.controllers.dataVault.object.ObjectVault;

public class DictionaryVault extends ObjectVault<Dictionary> {

	public DictionaryVault(String dataFile) throws IOException {
		initialize(dataFile, Dictionary.class);
	}

	@Override
	public void addSerializersDeserializers() {
		Type type = new TypeToken<BiMap<Object, Integer>>() {
		}.getType();
		//
		builder.registerTypeAdapter(type, new DictionarySerializer());
		builder.registerTypeAdapter(type, new DictionaryDeserializer());
		//
		builder.registerTypeAdapter(Dictionary.class, new JsonDeserializer<Dictionary>() {

			@Override
			public _InMemoryDictionary deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				return context.deserialize(json, _InMemoryDictionary.class);
			}
		});
	}

	private static class DictionarySerializer implements JsonSerializer<BiMap<Object, Integer>> {
		@Override
		public JsonElement serialize(BiMap<Object, Integer> src, Type typeOfSrc, JsonSerializationContext context) {
			JsonArray array = new JsonArray();
			src.forEach((x, y) -> {
				try {
					String classString = x.getClass().getName();

					switch (classString) {
					case "gr.uoa.di.entities.dictionary._Mark": {
						break;
					}
					case "gr.uoa.di.entities.dictionary._DictVar": {
						break;
					}
					case "org.apache.jena.graph.Node_Literal": {
						Node_Literal nodeLiteral = ((Node_Literal) x);
						String uri = nodeLiteral.getLiteralDatatype().getURI();
						JsonElement element = getJsonForLiteral(context, uri.split("#")[1],
								nodeLiteral.getLiteralLexicalForm(), nodeLiteral.getLiteralLanguage(), y);
						array.add(element);
						break;
					}
					case "org.apache.jena.graph.Node_URI": {
						JsonElement element = getJsonForURI(context, "Node_URI", x.toString(), y);
						array.add(element);
						break;
					}
					default: {
						System.err.println("Objects of type: " + classString + " are ignored");
						break;
					}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			return array;
		}

		private static JsonElement getJsonForURI(JsonSerializationContext context, String type, String objectString,
				int id) throws IOException {
			JsonArray array = new JsonArray();
			array.add(type);
			array.add(id);
			array.add(objectString);
			return array;
		}

		private static JsonElement getJsonForLiteral(JsonSerializationContext context, String type, String objectString,
				String language, int id) throws IOException {
			JsonArray array = new JsonArray();
			array.add(type);
			array.add(id);
			array.add(objectString);
			array.add(language);
			return array;
		}
	}

	private static class DictionaryDeserializer implements JsonDeserializer<BiMap<Object, Integer>> {

		@Override
		public BiMap<Object, Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			_InMemoryDictionary tmp = new _InMemoryDictionary();
			BiMap<Object, Integer> biMap = tmp.entries;
			json.getAsJsonArray().forEach(entry -> {
				JsonArray entryTuple = entry.getAsJsonArray();
				String type = entryTuple.get(0).getAsString();
				int value = entryTuple.get(1).getAsInt();
				Node key = null;
				RDFDatatype dt = null;
				switch (type) {
				case "Node_URI":
					key = NodeFactory.createURI(entryTuple.get(2).getAsString());
					biMap.put(key, value);
					break;
				case "langString":
					dt = NodeFactory.getType("http://www.w3.org/1999/02/22-rdf-syntax-ns#langString");
					key = NodeFactory.createLiteral(entryTuple.get(2).getAsString(), entryTuple.get(3).getAsString(), dt);
					biMap.put(key, value);
					break;
				case "string":
					dt = NodeFactory.getType("http://www.w3.org/2001/XMLSchema#string");
					key = NodeFactory.createLiteralByValue(entryTuple.get(2).getAsString(), dt);
					biMap.put(key, value);
					break;
				default:
					System.err.println(type + " type is not supported");
					break;
				}
			});
			return biMap;
		}

	}

}
