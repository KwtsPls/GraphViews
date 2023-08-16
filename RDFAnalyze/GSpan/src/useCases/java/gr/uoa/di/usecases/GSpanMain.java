package useCases.java.gr.uoa.di.usecases;

import java.io.File;
import java.io.IOException;

import gr.uoa.di.interfaceAdapters.debug.MyDebug;
import main.java.gr.uoa.di.entities.viewSelection._steps.*;
import useCases.java.gr.uoa.di.usecases.constants.dbpedia.DBPediaPrimordialConstants;
import useCases.java.gr.uoa.di.usecases.constants.experiments.ConstantForExperiments;
import useCases.java.gr.uoa.di.usecases.constants.materialization.Constants;

class GSpanMain { // NO_UCD (unused code)

	static double tolerancePercentage = 0.1;
	///////////////

	public static void main(String[] args) throws Exception, IOException {
		for (int minSup : new int[] { 1000 }) {
			viewSelection(DBPediaPrimordialConstants.create("tmp/", minSup));

		}
	}

	static <C extends Constants & ConstantForExperiments> void viewSelection(C constants)
			throws Exception, IOException {
		//
//		System.gc();

		long start;
		long end;

		System.out.println("0st Step: Creating DBPedia Statistics");
		MyDebug.printAndLog(() -> "0st Step: Creating DBPedia Statistics");
		start = System.currentTimeMillis();
		Step_Preprocessing.createLabelStatistics(constants);
		end = System.currentTimeMillis();
		System.out.println("Time required for Preprocessing :" + (end - start) +"\n");
		//

		MyDebug.printAndLog(() -> "0st Step: Create Test Set");
		Step_TestSet_Creation.create(constants, 10000);
		System.out.println("1st Step: Transforming Graph to gspan form and mining frequent patterns");
		MyDebug.printAndLog(() -> "1st Step: Transforming Graph to gspan form and mining frequent patterns");
		start = System.currentTimeMillis();
		Step1_Mining.mineFrequentPatterns(constants);
		end = System.currentTimeMillis();
		System.out.println("Time required for Mining :" + (end - start)+"\n");
		//

		System.out.println("2nd Step: Transform frequent patterns, create hierarchy, and clean non-benefiting patterns");
		MyDebug.printAndLog(
				() -> "2nd Step: Transform frequent patterns, create hierarchy, and clean non-benefiting patterns");
		start = System.currentTimeMillis();
		Step2_CleaningAndHierarchyCreation.cleanPatternsAndCreateHierarchy(constants, tolerancePercentage);
		end = System.currentTimeMillis();
		System.out.println("Time required for Cleaning :" + (end - start)+"\n");
		//

		System.out.println("3nd Step: Select the indexes to be implemented based on the cost model");
		MyDebug.printAndLog(
				() -> "3nd Step: Select the indexes to be implemented based on the cost model");
		start = System.currentTimeMillis();
		Step3_IndexSelection.selectViews(constants, tolerancePercentage);
		end = System.currentTimeMillis();
		System.out.println("Time required for Index selection :" + (end - start)+"\n");

		//Step4_ViewCleaning.cleanIndexes();
	}

	static void deleteDirectory(String dir) {
		File directory = new File(dir);
		File[] files = directory.listFiles();
		for (File file : files)
			file.delete();
	}

}
