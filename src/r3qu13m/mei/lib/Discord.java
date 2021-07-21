package r3qu13m.mei.lib;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.DiscordApi;
import com.github.scribejava.core.builder.ScopeBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class Discord {
	private static String CLIENT_ID = "862898876662677514";
	private static String CLIENT_SECRET = "UEh3o-i17k1W8VAbg84QHP5EJccR2g69";
	private static String READ_BOT_TOKEN = "ODY3Mjc5ODEwNjk5MDY3NDEy.YPezRA.zM9ECZg0xsJQ9vwqZho8wXW5RdM";

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

	public JsonRootNode getUsingBotToken(final String path) throws IOException, InvalidSyntaxException {
		HttpURLConnection con = (HttpURLConnection) new URL("https://discordapp.com/api/v9" + path).openConnection();
		con.addRequestProperty("Authorization", "Bot " + READ_BOT_TOKEN);
		con.setRequestProperty("Content-Type", "application/json");
		con.connect();

		if (con.getResponseCode() != 200) {
			return null;
		}

		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		JsonRootNode node = new JdomParser().parse(br);
		br.close();

		return node;
	}

	public void clearToken() {
		this.token = null;
	}
}
