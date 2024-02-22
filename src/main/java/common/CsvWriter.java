package common;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {
	
	public static void writeLiteral(String fileName, List<String> lines) {
		writeLiteral(fileName, "", lines);
	}
	
	public static void writeLiteral(String fileName, String header, List<String> lines) {
		try (FileWriter csv = new FileWriter(fileName)) {
			if(!header.isEmpty())
				csv.append(header + "\n");
				
			for (String line : lines)
				csv.append(line + "\n");
			
		} catch (IOException e) {
			Logger.getInstance().error("Error writing file");
			Logger.getInstance().error(e.getLocalizedMessage());
		}
	}
}
