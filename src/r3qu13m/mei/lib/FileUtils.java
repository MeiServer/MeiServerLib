package r3qu13m.mei.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

public class FileUtils {
	private static String computeHash(final File target) {
		try {
			return DigestUtils.sha1Hex(new FileInputStream(target));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
