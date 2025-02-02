package net.echo.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.echo.registry.EndpointRegistry;
import net.echo.web.SpotifyWebInterface;
import net.echo.wrapper.Queue;
import net.echo.wrapper.Track;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SpotifyClient {

    private static final Gson GSON = new Gson();

    private String accessToken;

    public SpotifyClient() {
    }

    public SpotifyClient(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public CompletableFuture<Track> getCurrentTrackAsync() {
        CompletableFuture<String> response = SpotifyWebInterface.get(accessToken, EndpointRegistry.CURRENTLY_PLAYING);

        return response.thenApply(s -> {
            JsonObject json = GSON.fromJson(s, JsonObject.class);

            return GSON.fromJson(json.get("item").getAsJsonObject(), Track.class);
        });
    }

    public Track getCurrentTrack() {
        return getCurrentTrackAsync().join();
    }

    public CompletableFuture<Queue> getQueue() {
        CompletableFuture<String> response = SpotifyWebInterface.get(accessToken, EndpointRegistry.QUEUE);

        return response.thenApply(s -> GSON.fromJson(s, Queue.class));
    }

    public CompletableFuture<Boolean> play() {
        CompletableFuture<String> response = SpotifyWebInterface.get(accessToken, EndpointRegistry.PLAY);

        return response.thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> pause() {
        CompletableFuture<String> response = SpotifyWebInterface.get(accessToken, EndpointRegistry.PAUSE);

        return response.thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> skipNext() {
        CompletableFuture<String> response = SpotifyWebInterface.get(accessToken, EndpointRegistry.SKIP_NEXT);

        return response.thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> skipPrevious() {
        CompletableFuture<String> response = SpotifyWebInterface.get(accessToken, EndpointRegistry.SKIP_PREVIOUS);

        return response.thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> addToQueue(String trackUri) {
        String url = EndpointRegistry.QUEUE.getUrl() + "?uri=" + trackUri;
        CompletableFuture<String> response = SpotifyWebInterface.get(accessToken, url);

        return response.thenApply(String::isEmpty);
    }
}
