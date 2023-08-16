package gr.uoa.di.views4Neo.interfaceAdapters;

import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gr.uoa.di.entities.dictionary.Dictionary;
import gr.uoa.di.entities.dictionary.Variable;
import gr.uoa.di.entities.graph.regular.abstractions.Graph;
import gr.uoa.di.entities.graph.regular.abstractions.Node;
import gr.uoa.di.entities.graph.regular.abstractions.Triple;
import gr.uoa.di.entities.graph.regular.implementations.BasicGraph;
import gr.uoa.di.entities.graph.regular.implementations.BasicGraphFactory;
import gr.uoa.di.interfaceAdapters.iterators.dbPedia.DBPediaGraphQueryIterator;
import gr.uoa.di.interfaceAdapters.workloads.JenaGraphQueryIterator;
import org.apache.jena.atlas.lib.Pair;
import org.apache.jena.base.Sys;
import org.checkerframework.checker.units.qual.C;

public class CyperQueryTranslator {

	public static void main(String[] args) throws IOException {
		//noExecution();
		long start = System.currentTimeMillis();
		executeQueries();
		long end = System.currentTimeMillis();
		System.out.println("Query execution: " + (end - start)+"\n");
	}

	static void noExecution() throws IOException{

		Dictionary dict = Dictionary.create();
		List<CypherView> views = initializeViews(1);
		FileWriter replaced = new FileWriter("outputs/replaced.txt");
		FileWriter rewritten = new FileWriter("outputs/rewritten.txt");
		FileWriter workload = new FileWriter("outputs/output.txt");
		FileWriter sample = new FileWriter("outputs/sample.txt");
		try (JenaGraphQueryIterator<BasicGraph> iter = DBPediaGraphQueryIterator.create(dict,
				BasicGraphFactory.create());) {
			while (iter.hasNext()) {
				BasicGraph query = iter.next();
				if (query == null)
					continue;
				CyperQueryTranslator cypherQuery = new CyperQueryTranslator(query);
				workload.write(cypherQuery+"\n");
				String rewrittenQuery = rewriteQuery(cypherQuery,views,0);
				if(rewrittenQuery!=null){
					try {
						replaced.write(cypherQuery.toString().replaceAll("�d\\?","var")+"\n");
						rewritten.write(rewrittenQuery.replaceAll("�d\\?","var")+"\n");
						sample.write(rewrittenQuery.replaceAll("�d\\?","var")+"\n");
					}
					catch (IOException e){
						System.out.println("Bad Input");
					}

				}
				else{
					sample.write(cypherQuery.toString().replaceAll("�d\\?","var")+"\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		replaced.close();
		rewritten.close();
		workload.close();
		sample.close();
	}


	//Method to execute the queries and install the indices with lazy materialization
	static void executeQueries() throws IOException{
		try {
			File myObj = new File("outputs/sample.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println("\n"+data+"\n");
				connection.runQuery(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}


	List<String> cypherPatterns = new LinkedList<>();
	List<NaiveTriple> triplesList = new ArrayList<>();
	LinkedHashSet<String> headVars = new LinkedHashSet<>();
	HashMap<Node, CypherNode> node2CypherNode = new HashMap<>();
	HashMap<String,String> varMap = new HashMap<>();
	HashMap<String,String> nameMap = new HashMap<>();
	static Neo4jConnection connection = new Neo4jConnection("bolt://localhost:7687", "neo4j", "equator-alpine-mobile-panic-lorenzo-1219");
	int varCounter;
	int contantCounter;

	CyperQueryTranslator(Graph graph) {
		var iter = graph.getEdgeIterator();
		while (iter.hasNext()) {
			Triple triple = iter.next();
			cypherPatterns.add(tripleTranslate(triple));
			triplesList.add(tripleToPattern(triple));
		}
	}

	static int isMaterialized(final List<CypherView> list, final String name){
		for(CypherView view:list){
			if(Objects.equals(view.getViewName(), name)){
				return view.getMaterialized();
			}
		}
		return 0;
	}

	static void updateMaterialized(final List<CypherView> list, final String name){
		for(CypherView view:list){
			if(Objects.equals(view.getViewName(), name)){
				view.setMaterialized(1);
				return;
			}
		}
	}

	//Method to perform basic query rewriting
	public static String rewriteQuery(CyperQueryTranslator cypherQuery,List<CypherView> views,int execute){
		String rewrite=null;
		String replacedEdge=null;
		String viewName=null;
		Double maxBenefit=0.0;
		for(CypherView view:views){

			int contained = view.isContained(cypherQuery.triplesList);
			if(contained==1){
				if(view.getBenefit()>maxBenefit){
					maxBenefit = view.getBenefit();
					viewName = view.getViewName();
					replacedEdge = view.getEdge();
				}
			}
		}

		if(replacedEdge!=null) {
				Matcher m = Pattern.compile("\\((.*?)\\)").matcher(replacedEdge);
				List<String> vars = new ArrayList<>();
				while (m.find()) {
					vars.add(m.group(1));
				}
				String var1 = vars.get(0);
				String var2 = vars.get(1);

				Matcher m1 = Pattern.compile("\\[(.*?)\\]").matcher(replacedEdge);
				String edge = null;
				while (m1.find()) {
					edge = m1.group(1).substring(1);
				}

				var1 = "(" + cypherQuery.nameMap.get(var1) + ")";
				if (var2.charAt(0) == 'c')
					var2 = "(c";
				else
					var2 = "(" + cypherQuery.nameMap.get(var2) + ")";

				replacedEdge = var1 + "-[:" + edge + "]->" + var2;
				String viewEdge = var1 + "-[:" + viewName + "]->" + var2;

				//Check if index is materialized or not
				rewrite = cypherQuery.toString().replace(replacedEdge, viewEdge).replaceAll("�d\\?", "var");
				if (isMaterialized(views, viewName) == 0) {
					//Materialize index and set is as online
					updateMaterialized(views, viewName);
					rewrite = cypherQuery.toString(viewEdge).replaceAll("�d\\?", "var");
				}
		}


		return rewrite;
	}


	String tripleTranslate(Triple triple) {
		Object predicate = triple.getPredicate().getItem();
		CypherNode subject = translate(triple.getSubject());
		if (URI.class.isInstance(predicate)) {
			URI uri = (URI) predicate;
			if (uri.toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
				if (Variable.class.isInstance(triple.getObject().getItem())) {
					headVars.add("labels(" + subject.varName + ")");
				} else {
					subject.addLabel(triple.getObject().toString());
				}
				return subject.toString();
			} else {
				CypherNode object = node2CypherNode.computeIfAbsent(triple.getObject(), x -> translate(x));
				CypherEdge edge = new CypherEdge(subject, uri, object);
				return edge.toString();
			}
		} else {
			System.err.println("Not Supported");
			System.exit(1);
		}
		return null;
	}

	/*
	 *
	 * MATCH (a:Person {name:'Jim'})-[:KNOWS]->(b)-[:KNOWS]->(c),(a)-[:KNOWS]->(c)
	 * RETURN b, c
	 */
	CypherNode translate(Node node) {
		Object object = node.getItem();
		switch (object.getClass().getSimpleName()) {
			case "String":
				return new CypherNode((String) object, "c" + (contantCounter++));
			case "Number":
				return new CypherNode((Number) object, "c" + (contantCounter++));
			case "URI":
				return new CypherNode((URI) object, "c" + (contantCounter++));
			case "Variable": {
				CypherNode cypherNode = new CypherNode((Variable) object);
				headVars.add(cypherNode.varName);
				return cypherNode;
			}
			default:
				System.err.println("Not Supported");
				return null;
		}
	}


	NaiveTriple tripleToPattern(Triple triple){
		Pair<String,String> subject = viewTranslate(triple.getSubject());
		String subjectValue = subject.getLeft();
		String subjectType = subject.getRight();
		Object predicate = triple.getPredicate().getItem();
		Pair<String,String> object = viewTranslate(triple.getObject());
		String objectValue = object.getLeft();
		String objectType = object.getRight();

		String s = (mapVariableName(subjectValue,subjectType)==1?varMap.get(subjectValue):"\""+subjectValue+"\"");
		String p = "`"+predicate.toString()+"`";
		String o = (mapVariableName(objectValue,objectType)==1?varMap.get(objectValue):"\""+objectValue+"\"");

		return new NaiveTriple(s,p,o);
	}

	int mapVariableName(String name,String type){
		if(Objects.equals(type, "Var")){
			String value = varMap.get(name);
			if(value==null){
				varMap.put(name,"x"+varCounter);
				nameMap.put("x"+varCounter,name);
				varCounter++;
			}
			return 1;
		}
		else
			return 0;
	}


	Pair<String,String> viewTranslate(Node node){
		Object object = node.getItem();
		switch (object.getClass().getSimpleName()) {
			case "String":
				return new Pair<>((String) object,"String");
			case "Number":
				return new Pair<>((String) object,"Number");
			case "URI":
				return new Pair<>((String) object.toString(),"URI");
			case "Variable": {
				CypherNode cypherNode = new CypherNode((Variable) object);
				return new Pair<>(cypherNode.varName,"Var");
			}
			default:
				System.err.println("Not Supported");
				return null;
		}
	}



	//Method to initialize the views from th csv file
	public static List<CypherView> initializeViews(int isMaterialized){
		List<CypherView> views = new ArrayList<>();
		String path = "resources/index_256_1000.csv";
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			int start=0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if(start==0) start++;
				else{
					CypherView view = new CypherView(data,isMaterialized);
					views.add(view);
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return views;
	}

	// TODO Change Buffer to builder
	@Override
	public String toString() {
		StringBuffer output = new StringBuffer("MATCH ");
		output.append(String.join(",", cypherPatterns));
		output.append(" return ");
		output.append(String.join(",", headVars));
		return output.toString();
	}

	public String toString(String view) {
		StringBuffer output = new StringBuffer("MATCH ");
		output.append(String.join(",", cypherPatterns));
		output.append(" MERGE ");
		output.append(view);
		output.append(" RETURN ");
		output.append(String.join(",", headVars));
		return output.toString();
	}

}