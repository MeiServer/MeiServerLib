package r3qu13m.mei.lib;

import java.util.logging.Logger;

public class MeiLogger {

	public static Logger getLogger(final String name) {
		return Logger.getLogger(name);
	}

	public static Logger getLogger() {
		return MeiLogger.getLogger("Mei");
	}

	static {
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS [%4$s]: %5$s%6$s%n");
	}
}
