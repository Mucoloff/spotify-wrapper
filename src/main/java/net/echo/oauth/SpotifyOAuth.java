package net.echo.oauth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.echo.web.SpotifyWebInterface;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SpotifyOAuth {

    private final String redirectUrl;
    private final String clientId;
    private final String clientSecret;

    public SpotifyOAuth(String redirectUrl, String clientId, String clientSecret) {
        this.redirectUrl = redirectUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public CompletableFuture<String> getAccessToken(String code) {
        CompletableFuture<String> future = new CompletableFuture<>();

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

    public String getAuthorizeUrl(String scope) {
        return "https://accounts.spotify.com/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&scope=" + scope;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public record ResponseCallback(CompletableFuture<String> future) implements Callback {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            future.completeExceptionally(e);
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if (!response.isSuccessful()) {
                future.completeExceptionally(new IOException("Unexpected code " + response));
                return;
            }

            String responseBody = Objects.requireNonNull(response.body()).string();

            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
            String accessToken = json.has("access_token") ? json.get("access_token").getAsString() : null;

            response.body().close();

            if (accessToken != null) {
                future.complete(accessToken);
            } else {
                future.completeExceptionally(new IOException("Access token not found in response"));
            }
        }
    }
}
