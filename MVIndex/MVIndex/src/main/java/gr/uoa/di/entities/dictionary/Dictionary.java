package gr.uoa.di.entities.dictionary;

import java.util.Iterator;

import gr.uoa.di.entities.graph.regular.term.Term;

/**
 * A dictionary is used to map constants to corresponding int values. The
 * dictionary maps constants, i.e. URIs and Strings to int values that are used
 * as identifiers.
 */
public interface Dictionary {
	
	
	
	/**
	 * @return Creates a new Dictionary.
	 */
	public static Dictionary create() {
		_InMemoryDictionary output = new _InMemoryDictionary();
		return output;
	}
	
	static final int firstVarId = Integer.MAX_VALUE - 100;
	static final int lastVarId = Integer.MAX_VALUE - 10;
	static final int variableLabel = lastVarId + 1;
	static final int directionLabel = lastVarId + 2;
	static final int isALabel = lastVarId+3;	
	static final int invEdgeMark =lastVarId+4;
	static final int endEdgeMark =lastVarId+5;
	static final int unexaminedConstant = lastVarId+6;
	
	
	/**
	 * @param id The identifier that is used instead of a constant value.
	 * @return The corresponding constant value.
	 */
	public Object getContantOfId(int id);

	/**
	 * @param constant It takes as input the constant value. If the constant value
	 *                 is contained in the dictionary, it returns its corresponding
	 *                 id value. If not, it assigns to the constant a new id value.
	 * @return
	 * @throws Exception 
	 */
	public Integer getIdOfConstant(Object constant) throws Exception;

	public static boolean isVariable(int i) {
		return firstVarId<=i && i<=variableLabel;
	}
	
	public static boolean isLabeledVariable(int i) {
		return firstVarId<=i && i<variableLabel;
	}
	
	public static boolean isVariable(Term term) {
		return isVariable(term.getLabel());
	}

	public static boolean isConstant(int i) {
		return !(firstVarId<=i && i<=Integer.MAX_VALUE);
	}
	
	public static boolean isConstant(Term term) {
		return isConstant(term.getLabel());
	}
	
	public void setStage(Stage stage);

	public static Iterator<Variable> getVarIdIterator() {		
		return _DictionaryHelper.getVarIdIterator();
	}
		
}
