package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class RunProperties {

	private static RunProperties INSTANCE;
	private static String filePath = "src/main/resources/run_settings.properties";
	private static final String relativePath = "target/";

	private FileInputStream fileReader;
	private String jobTitle;
	private String location;
	private LocalDate dateFilter;
	private String outPath;
	private String[] keywords;

	private RunProperties() {
		Properties properties = new Properties();

		try {
			fileReader = new FileInputStream(filePath);
			properties.load(fileReader);
		} catch (IOException e) {
			Logger.getInstance().error("Properties file not found in %s", System.getProperty("user.dir"));
			Logger.getInstance().error(e.getLocalizedMessage());
			System.exit(-1);
		}

		jobTitle = properties.getProperty("jobDescription");
		location = properties.getProperty("location");
		dateFilter = setDateFilter(properties.getProperty("dateFilter"));
		outPath = properties.getProperty("outputPath");

		String rawKeys = properties.getProperty("keywords");
		rawKeys = rawKeys.replace(" ", "").toLowerCase();
		keywords = rawKeys.split(",");
	}

	public static synchronized RunProperties getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RunProperties();
		}

		return INSTANCE;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getLocation() {
		return location;
	}

	public LocalDate getDateFilter() {
		return dateFilter;
	}

	public String getOutputPath() {
		return relativePath + outPath;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public synchronized void close() {
		if (INSTANCE != null) {
			INSTANCE = null;

			try {
				fileReader.close();
			} catch (Exception e) {
				Logger.getInstance().error("Tried to close properties stream but was already closed");
				Logger.getInstance().error(e.getLocalizedMessage());
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(String.format("Searching for %s jobs in %s; ", jobTitle, location));
		s.append("Excluding listings before " + dateFilter.toString());
		s.append(String.format("\nSee %s for results", outPath));
		
		return s.toString();
	}

	private LocalDate setDateFilter(String date) {
		if (date.isEmpty() || date.isBlank()) {
			return LocalDate.now().minusYears(1);
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			return LocalDate.parse(date, formatter);
		}
	}
}
