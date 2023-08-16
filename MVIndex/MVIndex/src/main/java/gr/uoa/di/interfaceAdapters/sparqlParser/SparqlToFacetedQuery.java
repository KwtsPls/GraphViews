package gr.uoa.di.interfaceAdapters.sparqlParser;

import java.net.URI;
//import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Node_Literal;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.core.Var;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForGraph;
import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForNode;
import gr.uoa.di.entities.graph.regular.abstractions.AbstractionForTriple;
import gr.uoa.di.entities.graph.regular.abstractions.GraphConstructor;
import gr.uoa.di.entities.graph.regular.abstractions.GraphFactory;

/**
 * This class is used to transform a query in SPARQL format, to a Faceted Query.
 *
 */
public interface SparqlToFacetedQuery<G extends AbstractionForGraph<?, ?, G>> {

	public LinkedList<G> getGraphQuery(String queryString, Dictionary dictionary);

	public static <N extends AbstractionForNode<N, T>, T extends AbstractionForTriple<N, T>, G extends AbstractionForGraph<N, T, G>> SparqlToFacetedQuery<G> create(
			GraphFactory<N, T, G> factory) {
		return new HiddenClass<N, T, G>(factory);
	}

	public static class HiddenClass<N extends AbstractionForNode<N, T>, T extends AbstractionForTriple<N, T>, G extends AbstractionForGraph<N, T, G>>
			implements SparqlToFacetedQuery<G> {

		GraphFactory<N, T, G> factory;

		public HiddenClass(GraphFactory<N, T, G> factory2) {
			factory = factory2;
		}

		/**
		 * This function transforms the string of a SPARQL query (possibly containing
		 * unions etc.) to a list of Faceted Queries.
		 * 
		 * @param queryString the string of the SPARQL query.
		 * @param dictionary  the dictionary that will make the transformation.
		 * @return The list of Faceted queries that are contained in the initial
		 *         queryString. E.g. for a union of conjunctive queries, each of them
		 *         will be returned to the list.
		 */

		@Override
		public LinkedList<G> getGraphQuery(String queryString, Dictionary dictionary) {
			LinkedList<G> output = new LinkedList<G>();
			List<Set<TriplePath>> uCQs = getCQs(queryString);
			for (Set<TriplePath> cq : uCQs) {
				output.add(getCQAsInternalQuery(cq, dictionary));
			}
			return output;
		}

		private List<Set<TriplePath>> getCQs(String queryString) {
			try {
				Query query = new Query();
				QueryFactory.parse(query, queryString, (String) null, Syntax.defaultQuerySyntax);
				return GetConjunctiveQueries.getUCQ(query);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new LinkedList<Set<TriplePath>>();
		}

		private G getCQAsInternalQuery(Set<TriplePath> cq, Dictionary dictionary) {
			GraphConstructor<N, T, G> graphConstructor = factory.getGraphConstructor(dictionary);

			if (cq.size() == 0)
				return null;
			for (TriplePath triplePath : cq) {
				Node s = triplePath.getSubject();
				Node r = triplePath.getPredicate();
				Node o = triplePath.getObject();

				if (s == null || r == null || o == null || (!s.isURI() && !s.isVariable())
						|| (!o.isURI() && !o.isVariable() && !o.isLiteral()) || (!r.isURI())) {
					return null;
				} else {
					
					graphConstructor.addTripleFromJObjects(getSimplified(s), getSimplified(r), getSimplified(o));
				}
			}
			G query = graphConstructor.getGraphQuery();
			return query;
		}
		
		public Object getSimplified(Node node) {
			switch(node.getClass().getSimpleName()) {
				case "Var":
					return node.toString();
				case "Node_URI":					
					return URI.create(((Node_URI)node).getURI());
				case "Node_Literal":
					
					
					Node_Literal nodeLiteral = (Node_Literal)node;
					switch(nodeLiteral.getLiteralDatatypeURI()){
						case "http://www.w3.org/2001/XMLSchema#string":
							return nodeLiteral.getLiteralLexicalForm();
						case "http://www.w3.org/1999/02/22-rdf-syntax-ns#langString":
							return nodeLiteral.getLiteralLexicalForm();
						default:
							System.err.println("The literal type "+node.getClass()+" is not parsed correctly");
							System.exit(1);
					}
					
				default:     
					System.err.println("The class of node "+node.getClass()+" is not parsed correctly");
					System.exit(1);
			}
			return null;
		}

	}

}
