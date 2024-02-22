package common;

import org.slf4j.LoggerFactory;

public class Logger {

	private static org.slf4j.Logger INSTANCE;
	
	public static synchronized org.slf4j.Logger getInstance() {
		if (INSTANCE == null) {
			INSTANCE = LoggerFactory.getLogger(Logger.class);
		}

		return INSTANCE;
	}
}
