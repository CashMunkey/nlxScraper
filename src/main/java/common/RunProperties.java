package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class RunProperties {

    private static RunProperties INSTANCE;
	private static String filePath = "src/main/resources/run_settings.properties";
	
	private FileInputStream fileReader;
	private String jobTitle;
	private String location;
	private LocalDate dateFilter;
	private String outPath;
    
	private RunProperties() {
		Properties properties = new Properties();
		
		try {
			fileReader = new FileInputStream(filePath);
			properties.load(fileReader);
		} catch (IOException e) {
			System.out.println("Error: properties file not found in " + System.getProperty("user.dir"));
			System.out.println(e.getLocalizedMessage());
			System.exit(-1);
		}
		
		jobTitle = properties.getProperty("jobDescription");
		location = properties.getProperty("location");
		dateFilter = setDateFilter(properties.getProperty("dateFilter"));
		outPath = properties.getProperty("outputPath");
	}
	
    public synchronized static RunProperties getInstance() {
        if(INSTANCE == null) {
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
    	return outPath;
    }
    
    public synchronized void close() {
    	if(INSTANCE != null) {
	    	INSTANCE = null;
	    	
	    	try {
	    		fileReader.close();
	    	} catch (Exception e) {
	    		System.out.println("Tried to close properties stream but was already closed");
	    		System.out.println(e.getLocalizedMessage());
	    	}
    	}
    }
    
	private LocalDate setDateFilter(String date) {
		if(date.isEmpty() || date.isBlank()) {
			return LocalDate.now().minusYears(1);
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			return LocalDate.parse(date, formatter);
		}
	}
}
