package gr.uoa.di.entities.graph.regular.term;

import java.util.Iterator;

import gr.uoa.di.entities.dictionary.Variable;
import gr.uoa.di.entities.graph.Printable;

public interface Term extends Comparable<Term>, Printable{
	
	public static Term getVarTerm(Object var) {
		return new _Term(Variable.create(var.toString()));
	}
	
	public static Term getConstantTerm(int objectID,Object var) {
		return new _Term(var,objectID,true);
	}
	
	public boolean isVariable();
	
	public boolean isLabeledVariable();
	
	public boolean isConstant();
	
	public int getLabel();
	
	public Object getItem();
	
	public Term getTerm();
	
	public void setVarEnumeration2(Iterator<Variable> iter);


	@Override
	public default int compareTo(Term o) {
		if (this.isVariable() && o.isVariable())
			return this.toString().compareTo(o.toString());
		return Integer.compare(this.getLabel(), o.getLabel());
	}

	public void setLabel(int nodeId);

}
