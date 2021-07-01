package r3qu13m.mei.lib;

import java.util.logging.Logger;

public class MeiLogger {
	
	public static Logger getLogger(final String name) {
		return Logger.getLogger(name);
	}
	
	public static Logger getLogger() {
		return MeiLogger.getLogger("Mei");
	}

}
