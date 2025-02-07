package net.echo.oauth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.echo.web.SpotifyWebInterface;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import static net.echo.client.SpotifyClient.GSON;

public class SpotifyOAuth {

    private final String redirectUrl;
    private final String clientId;
    private final String clientSecret;

    public SpotifyOAuth(String redirectUrl, String clientId, String clientSecret) {
        this.redirectUrl = redirectUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public CompletableFuture<AuthToken> getAccessToken(String code) {
        CompletableFuture<AuthToken> future = new CompletableFuture<>();

        String endpoint = "https://accounts.spotify.com/api/token";
        String encodedAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", redirectUrl)
                .build();

        Request request = new Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .addHeader("Authorization", "Basic " + encodedAuth)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        SpotifyWebInterface.CLIENT.newCall(request).enqueue(new ResponseCallback(future));

        return future;
    }

    public CompletableFuture<AuthToken> refreshAccessToken(String refreshToken) {
        CompletableFuture<AuthToken> future = new CompletableFuture<>();

        String endpoint = "https://accounts.spotify.com/api/token";
        String encodedAuth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .build();

        Request request = new Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .addHeader("Authorization", "Basic " + encodedAuth)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        SpotifyWebInterface.CLIENT.newCall(request).enqueue(new ResponseCallback(future));

        return future;
    }

    public String getAuthorizeUrl(String scope) {
        return "https://accounts.spotify.com/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public record ResponseCallback(CompletableFuture<AuthToken> future) implements Callback {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            future.completeExceptionally(e);
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            ResponseBody body = response.body();

            if (body == null) {
                future.completeExceptionally(new IOException("Response body is null"));
                return;
            }

            String responseBody = body.string();
            body.close();

            if (!response.isSuccessful()) {
                future.completeExceptionally(new IOException("Unexpected code " + response));
                return;
            }

            AuthToken token = GSON.fromJson(responseBody, AuthToken.class);

            if (token.getAccessToken() != null) {
                future.complete(token);
            } else {
                future.completeExceptionally(new IOException("Access token not found in response"));
            }
        }
    }
}
