package r3qu13m.mei.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.DiscordApi;
import com.github.scribejava.core.builder.ScopeBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

public class Discord {
	private static String CLIENT_ID = "862898876662677514";
	private static String CLIENT_SECRET = "UEh3o-i17k1W8VAbg84QHP5EJccR2g69";

	private final OAuth20Service service;
	private OAuth2AccessToken token;

	public Discord() {
		this.service = new ServiceBuilder(Discord.CLIENT_ID).apiSecret(Discord.CLIENT_SECRET)
				.defaultScope(new ScopeBuilder("identify")).callback("http://example.com/callback")
				.userAgent("MeiServerLib").build(DiscordApi.instance());
		this.token = null;
	}

	public String getAuthorizationURL() {
		return this.service.getAuthorizationUrl();
	}

	public void authorize(final String authorizationCode) {
		try {
			this.setRefreshToken(this.service.getAccessToken(authorizationCode).getRefreshToken());
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public void setRefreshToken(final String refreshToken) {
		try {
			this.token = this.service.refreshAccessToken(refreshToken);
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public String getAccessToken() {
		return this.token.getAccessToken();
	}

	public String getRefreshToken() {
		return this.token.getRefreshToken();
	}

	public boolean hasValidAccessToken() {
		if (this.token == null) {
			return false;
		}
		try {
			Response res = this.get("/users/@me");
			return res.getCode() == 200;
		} catch (InterruptedException | ExecutionException | IOException e) {
			return false;
		}
	}

	public Response get(final String path) throws InterruptedException, ExecutionException, IOException {
		final OAuthRequest req = new OAuthRequest(Verb.GET, "https://discordapp.com/api/v9" + path);

		this.service.signRequest(this.token, req);

		return this.service.execute(req);
	}

	public String getUsingToken(final String path, String token) throws IOException {
		HttpURLConnection con = (HttpURLConnection) new URL("https://discordapp.com/api/v9" + path).openConnection();
		con.addRequestProperty("Authorization", token);
		con.setRequestProperty("User-Agent", "MeiServerLauncher (https://example.com/callback, 1.0)");
		con.setRequestProperty("Content-Type", "application/json");
		// con.setRequestMethod("GET");
		con.connect();

		if (con.getResponseCode() != 200) {
			return null;
		}

		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		while (br.ready()) {
			sb.append(br.readLine());
		}

		br.close();

		return sb.toString();
	}

	public void clearToken() {
		this.token = null;
	}
}
