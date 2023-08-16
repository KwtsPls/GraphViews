package gr.uoa.di.entities.graph;

import java.util.function.BiFunction;
import gr.uoa.di.entities.dictionary.Dictionary;

public interface DictionaryPrintable {
	
	default String toIdString() {
		return print(null,(dict2,p)->p.toIdString());
	}
	
	default String toCompactString(Dictionary dict) {
		return print(dict,(dict2,p)->p.toCompactString(dict2));
	}
	
	default String toFullString(Dictionary dict) {
		return print(dict,(dict2,p)->p.toFullString(dict2));
	}
	
	String print(Dictionary dict,BiFunction<Dictionary,DictionaryPrintable,String> function);
	
}
