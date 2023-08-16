package useCases.java.gr.uoa.di.usecases.constants.experiments;

import interfaceAdapters.java.gr.uoa.di.interfaceAdapters.gateways.csv.CsvWriter;

import java.io.IOException;


public interface ConstantForExperiments {

	CsvWriter getCsvWriter() throws IOException;

	CsvWriter getCsvWriter(boolean append) throws IOException;

	String getCsvFile();

	boolean existsCsvFile();

}
