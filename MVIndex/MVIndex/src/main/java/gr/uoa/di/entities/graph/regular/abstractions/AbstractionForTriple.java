package gr.uoa.di.entities.graph.regular.abstractions;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.graph.Printable;
import gr.uoa.di.entities.graph.regular.term.Term;

public abstract class AbstractionForTriple<N extends AbstractionForNode<N, T>, T extends AbstractionForTriple<N, T>>
		implements Triple {
	private N subject;
	private Term predicate;
	private N object;
	private boolean isExamined = false;
	private int sqlId = -1;

	private static int tripleCounter = 0;
	private int id;

	public AbstractionForTriple(AbstractionForNode<N, T> subject, Term predicate, AbstractionForNode<N, T> object) {
		this.subject = subject == null ? null : subject.getThis();
		this.predicate = predicate;
		this.object = object == null ? null : object.getThis();
		id = tripleCounter++;
	}

	public int getId() {
		return id;
	}

	@Override
	public N getSubject() {
		return subject;
	}

	public abstract T getThis();

	@Override
	public Term getPredicate() {
		return predicate;
	}

	@Override
	public N getObject() {
		return object;
	}

	@Override
	public String toString() {
		return toCompactString();
	}

	@Override
	public boolean isVisited() {
		return isExamined;
	}

	@Override
	public void visit() {
		isExamined = true;
	}

	@Override
	public int getPredicateLabel() {
		return predicate.getLabel();
	}

	@Override
	public int getEdgeId() {
		return sqlId;
	}

	@Override
	public int getVarCount() {
		return (Dictionary.isConstant(subject) ? 0 : 1) + (Dictionary.isConstant(object) ? 0 : 1)
				+ (Dictionary.isConstant(predicate) ? 0 : 1);
	}

	@Override
	public void resetForSerialization() {
		isExamined = false;
	}

	@Override
	public Set<N> getVariables() {
		Set<N> out = new HashSet<N>(2);
		if (Dictionary.isVariable(subject)) {
			out.add(subject);
		}
		if (Dictionary.isVariable(object)) {
			out.add(object);
		}
		return out;
	}	
	
	@Override
	public String print(Function<Printable,String> function) {
		return new StringBuilder("(").append(function.apply(subject)).append(',').append(
				function.apply(predicate))
				.append(',').append(function.apply(object)).append(')').toString();
	}

}
