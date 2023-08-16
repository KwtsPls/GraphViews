package gr.uoa.di.entities.graph.regular.abstractions;

public interface GraphConstructor<N extends AbstractionForNode<N,T>,T extends AbstractionForTriple<N,T>,G extends AbstractionForGraph<N,T,G>> {

	G getGraphQuery();
	
	void addTripleFromJObjects(Object subject, Object predicate, Object object);
	
	public T addTripleFromInt(int subjectId, int predicateId, int objectId);

}
