package gr.uoa.di.views4Neo.interfaceAdapters;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import gr.uoa.di.entities.dictionary.Variable;

public class CypherNode {
	String varName;
	List<String> labels = new ArrayList<>(2);
	HashMap<String, Object> properties2Values = new HashMap<>(2);

	CypherNode(URI uri, String varName) {
		properties2Values.put("uri", uri.toString());
		this.varName = varName;
	}

	CypherNode(Variable var) {
		this.varName = var.toString().substring(1);
	}

	CypherNode(String varName){
		this.varName=varName;
	}

	CypherNode(String property, String value, String varName){
		properties2Values.put(property,value);
		this.varName = varName;
	}

	CypherNode(String property, Number value, String varName){
		properties2Values.put(property,value);
		this.varName = varName;
	}

	CypherNode(String value, String varName) {
		properties2Values.put("value", value);
		this.varName = varName;
	}

	CypherNode(Number value, String varName) {
		properties2Values.put("value", value);
		this.varName = varName;
	}

	public void addLabel(String label) {
		labels.add(label);
	}

	@Override
	public String toString() {
		StringBuffer output = new StringBuffer("(").append(varName);
		StringJoiner joiner = new StringJoiner(",");
		labels.forEach(x -> joiner.add(":`" + x + "`"));
		output.append(joiner.toString());
		if (!properties2Values.isEmpty()) {
			output.append(" {");
			StringJoiner joiner2 = new StringJoiner(",");
			properties2Values.forEach((property, value) -> {
				joiner2.add(property + ":\"" + value + "\"");
			});
			output.append(joiner2);
			output.append('}');
		}
		output.append(")");
		return output.toString();
	}

}
