package r3qu13m.mei.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

public class FileUtils {
	public static String computeHash(final File target) {
		try {
			return FileUtils.computeHash(new FileInputStream(target));
		} catch (final FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static String computeHash(final InputStream is) {
		try {
			return DigestUtils.sha1Hex(is);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
