package r3qu13m.mei.lib;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.DiscordApi;
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
				.defaultScope("identify messages.read guilds").callback("http://example.com/callback")
				.userAgent("MeiServerLib").build(DiscordApi.instance());
		this.token = null;
	}

	public Discord(final OAuth2AccessToken token) {
		this();
		this.setAccessToken(token);
	}

	public String getAuthorizationURL() {
		return this.service.getAuthorizationUrl();
	}

	public void authorize(final String authorizationCode) {
		try {
			this.setAccessToken(this.service.getAccessToken(authorizationCode));
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public void setAccessToken(final OAuth2AccessToken token) {
		try {
			this.token = this.service.refreshAccessToken(token.getRefreshToken());
		} catch (IOException | InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean hasValidAccessToken() {
		return this.token != null;
	}

	public Response get(final String path) {
		final OAuthRequest req = new OAuthRequest(Verb.GET, path);

		this.service.signRequest(this.token, req);

		try {
			return this.service.execute(req);
		} catch (InterruptedException | ExecutionException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Response post(final String path, final Map<String, String> params) {
		final OAuthRequest req = new OAuthRequest(Verb.POST, path);
		for (final Entry<String, String> pair : params.entrySet()) {
			req.addBodyParameter(pair.getKey(), pair.getValue());
		}

		this.service.signRequest(this.token, req);

		try {
			return this.service.execute(req);
		} catch (InterruptedException | ExecutionException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
