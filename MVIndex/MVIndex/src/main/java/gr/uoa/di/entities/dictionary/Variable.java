package gr.uoa.di.entities.dictionary;

import java.util.Objects;

public class Variable {
	
	private int id;
	private String varString;
	
	static public Variable create(int id) {
		return new Variable(id);
	}
	
	static public Variable create(String varString) {
		return new Variable(varString);
	}
	
	
	Variable(String varString) {
		this.varString = varString;
		id = Dictionary.variableLabel;
	}
	
	Variable(int id) {
		this.id = id;
		varString=new StringBuffer("?x").append(id-Dictionary.firstVarId).toString();
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		return id == other.id;
	}

	
	
	
	@Override
	public String toString() {
		return varString;
	}

	public int getId() {
		return id;
	}

}
