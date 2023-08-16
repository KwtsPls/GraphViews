package main.java.gr.uoa.di.entities.gspan.gspanImplementation;

import main.java.gr.uoa.di.entities.gspan.gspanImplementation.implementation.AlgoGSPAN;

import java.io.File;
import java.io.IOException;

public class GSpanWeightedExecutor {

	public static void run(File input, File output, int support, int minNodeNumber, int maxNodeNumber) {
		AlgoGSPAN algo = new AlgoGSPAN();
		try {
			algo.runAlgorithm(input.toString(), output.toString(), support, false, false, 8, true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
