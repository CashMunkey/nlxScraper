package common;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CsvWriter {

	private FileWriter csv;
	
	public CsvWriter(String fileName) throws IOException {
		csv = new FileWriter(fileName + ".csv");
		csv.flush();
	}
	
	public CsvWriter(String fileName, String... headers) throws IOException {
		this(fileName);
		writeHeaders(headers);
	}
	
	public void writeHeaders(String... headers) throws IOException {
		StringBuilder headerString = new StringBuilder();		
		for(int i = 0; i < headers.length - 1; i++) {
			headerString.append(headers[i] + ",");
		}
		headerString.append(headers[headers.length-1] + "\n");
		
		csv.append(headerString);
	}
	
	public void mapToCsv(Map<?, ?> data, String... filters) throws IOException {
		for(Object e : data.keySet()) {
			String row = String.format("%s,%s", e.toString().replace(",", ""), data.get(e));
			
			for(String filter : filters) {
				row = row.replace(filter, "");
			}
			
			csv.append(row + "\n");
		}
	}
	
	public void close() throws IOException {
		csv.close();
	}
}
