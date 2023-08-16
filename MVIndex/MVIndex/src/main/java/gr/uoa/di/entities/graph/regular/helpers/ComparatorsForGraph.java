package gr.uoa.di.entities.graph.regular.helpers;

import java.util.Comparator;

import gr.uoa.di.entities.graph.regular.abstractions.Triple;
import gr.uoa.di.entities.graph.regular.term.Term;

public class ComparatorsForGraph {
	
	private static Comparator<Term> termComparator = new Comparator<Term>() {
		@Override
		public int compare(Term t1, Term t2) {			
			return Integer.compare(t1.getLabel(), t2.getLabel());
		}

	};
	
	public static Comparator<Triple> _tripleComparator = new Comparator<Triple>() {
		@Override
		public int compare(Triple e1, Triple e2) {
			int comp = termComparator.compare(e1.getPredicate(), e2.getPredicate());
			if (comp != 0)
				return comp;
			if (e1.getSubject() == e2.getSubject())
				return termComparator.compare(e1.getObject(), e2.getObject());
			return termComparator.compare(e1.getSubject(), e2.getSubject());
		}
	};

	public static Comparator<Triple> tripleComparator = new Comparator<Triple>() {
		@Override
		public int compare(Triple e1, Triple e2) {
			int comp = termComparator.compare(e1.getPredicate(), e2.getPredicate());
			if (comp != 0)
				return comp;
			if (e1.getSubject() == e2.getSubject())
				return termComparator.compare(e1.getObject(), e2.getObject());
			return termComparator.compare(e1.getSubject(), e2.getSubject());
		}
	};

	static Comparator<Triple> tripleComparatorWithNulls = new Comparator<Triple>() {
		@Override
		public int compare(Triple e1, Triple e2) {
			return termComparator.compare(e1.getPredicate(), e2.getPredicate());
		}
	};
	
}
