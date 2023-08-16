package main.java.gr.uoa.di.entities.viewSelection._steps;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gr.uoa.di.entities.dictionary.Dictionary;
import main.java.gr.uoa.di.entities.cypherViews.*;
import main.java.gr.uoa.di.entities.graph.PatternVS;
import main.java.gr.uoa.di.entities.viewSelection.edgeRewriting.TripleMap;
import main.java.gr.uoa.di.entities.viewSelection.hierarchy.PatternHierarchy;
import org.apache.jena.base.Sys;
import org.apache.log4j.BasicConfigurator;
import useCases.java.gr.uoa.di.usecases.constants.experiments.ConstantForExperiments;
import useCases.java.gr.uoa.di.usecases.constants.materialization.ConstantsSharable;

public class Step3_IndexSelection {

	static int BENEFIT_THRESHOLD = 256;
	static int STORAGE_LIMIT = 10000;
	static char del=6;

	public static <C extends ConstantsSharable & ConstantForExperiments> void selectViews(C constants,
																						  double tolerancePercentage) throws IOException {
		Dictionary dict = constants.deserializeDictionary();
		PatternHierarchy hierarchy = constants.deserializeHierarchy(dict);

		//Create a converter to convert patterns to cypher queries
		PatternToQueryConverter converter = new PatternToQueryConverter();

		//Class object to calculate the benefit of a given query
		CypherBenefitModel bnftModel = new CypherBenefitModel("bolt://localhost:7687", "neo4j", "equator-alpine-mobile-panic-lorenzo-1219");
		FileWriter fw = new FileWriter("tmp/index_256_1000.csv");
		fw.write("id"+del+"edge"+del+"delete"+del+"mappings"+del+"benefit\n");

		//Create a list with the Knapsack items to solve the problem
		List<KnapsackItem> Items = new ArrayList<KnapsackItem>();

		hierarchy.forEachPatternContainedPatterns((pattern, containments) -> {

			//Create the query equivalent to the given patterns
			CypherQuery query  = converter.convert(pattern);
			double benefit = bnftModel.benefit2(query);
			query.initView(bnftModel.getIndexEdge(query.toString()));


			//Select the index candidates to be materialized
			if(benefit>BENEFIT_THRESHOLD && query.getReplacedEdge()!=null) {

				containments.forEachRemaining(pair -> {
					PatternVS containedPattern = pair.getLeft();
					List<TripleMap> mappings = pair.getRight();

					//Candidate index
					if (containedPattern.equals(pattern) && (int)bnftModel.getEstimatedResult(query.toString())!=0
							&& (int)bnftModel.getEstimatedResult(query.toString())<STORAGE_LIMIT
							&& (int)bnftModel.getEstimatedResult(query.toString())<4000) {
						KnapsackItem item = new KnapsackItem(benefit,(int)bnftModel.getEstimatedResult(query.toString()),query,mappings);
						Items.add(item);
					}

				});
			}
		});

		KnapsackSolver solver = new KnapsackSolver();
		List<KnapsackItem> selectedIndexes = solver.solve(Items,STORAGE_LIMIT,Items.size());


		//Materialize the selected indices
		for(KnapsackItem i:selectedIndexes) {
			CypherQuery query = i.getQuery();
			List<TripleMap> mappings = i.getMappings();
			double benefit = i.getBenefit();

			System.out.println("\n" + query +"\n");
			System.out.println("Benefit :" + benefit+"\n");

			CypherIndex index = new CypherIndex(query.getViewName(), query.getReplacedEdge(),
					query.viewDeletionQuery(), mappings, benefit);


			//Create the index
			Long result = bnftModel.materializeIndex(query.viewCreationQuery());

			if (result != 0) {
				System.out.println("Implementing " + query.getViewName());
				System.out.println("------------------------------------");

				try {
					fw.write(index + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		fw.close();
	}

}
