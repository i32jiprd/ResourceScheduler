package scheduler.Util;

import org.apache.log4j.Logger;

public class Utilities {

	public static final int PROCESSING_DELAY = 1000; // Aprox. 1second

	public static final int WAITING_DELAY = 100;

	public static Logger log = Logger.getLogger("ResourceScheduler");

	public static String LINE_SEPARATOR = System.getProperty("line.separator");

}
