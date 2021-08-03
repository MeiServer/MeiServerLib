package r3qu13m.mei.lib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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

	public static void downloadFile(final URL url, final File destination) throws IOException {
		final URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", "MeiServerLauncher (https://example.com/callback, 1.0)");
		conn.connect();

		final InputStream is = new BufferedInputStream(conn.getInputStream());
		final OutputStream os = new FileOutputStream(destination);
		final byte buf[] = new byte[1024];
		while (true) {
			final int readCount = is.read(buf, 0, 1024);
			if (readCount == -1) {
				break;
			}
			os.write(buf, 0, readCount);
		}
		os.flush();
		is.close();
		os.close();
	}
}
