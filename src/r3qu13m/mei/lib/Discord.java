package r3qu13m.mei.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Discord {
	private String token;

	public Discord() {
		this.token = null;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

	public boolean hasValidToken() {
		if (this.token == null) {
			return false;
		}
		try {
			final Optional<String> res = this.get("/users/@me");
			return res.isPresent();
		} catch (InterruptedException | ExecutionException | IOException e) {
			return false;
		}
	}

	public Optional<String> get(final String path) throws InterruptedException, ExecutionException, IOException {
		return this.get(path, 5);
	}

	public Optional<String> get(final String path, final int ttl)
			throws InterruptedException, ExecutionException, IOException {
		final HttpURLConnection con = (HttpURLConnection) new URL("https://discordapp.com/api/v9" + path)
				.openConnection();
		con.addRequestProperty("Authorization", this.token);
		con.setRequestProperty("User-Agent", "MeiServerLauncher (https://example.com/callback, 1.0)");
		con.setRequestProperty("Content-Type", "application/json");
		// con.setRequestMethod("GET");
		con.connect();

		if (con.getResponseCode() != 200) {
			return Optional.empty();
		}

		final InputStream is = con.getInputStream();
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		
		while (true) {
			String buf = br.readLine();
			if (buf == null) {
				break;
			}
			sb.append(buf);
		}

		br.close();
		final String s = sb.toString();

		if (s.length() == 0) {
			final Logger log = MeiLogger.getLogger();
			log.warning("Failed to access to discord");
			if (ttl == 1) {
				return Optional.empty();
			}
			log.warning(String.format("Retrying...(remain %d)", ttl - 1));
			Thread.sleep(1000);
			return this.get(path, ttl - 1);
		}

		return Optional.of(s);
	}

	public void clearToken() {
		this.token = null;
	}
}
