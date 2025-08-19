package net.echo.client;

import com.google.gson.Gson;
import net.echo.registry.EndpointRegistry;
import net.echo.web.SpotifyWebInterface;
import net.echo.wrapper.Queue;
import net.echo.wrapper.playback.Playback;
import net.echo.wrapper.track.Track;

import java.util.concurrent.CompletableFuture;

public class SpotifyClient {

    public static final Gson GSON = new Gson();

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
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.CURRENTLY_PLAYING, "", "");

        return response.thenApply(s -> GSON.fromJson(s, Track.class));
    }

    public Track getCurrentTrack() {
        return getCurrentTrackAsync().join();
    }

    public CompletableFuture<Playback> getPlayBack() {
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.PLAYBACK, "", "");

        return response.thenApply(s -> GSON.fromJson(s, Playback.class));
    }

    public CompletableFuture<Queue> getQueue() {
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.QUEUE, "", "");

        return response.thenApply(s -> GSON.fromJson(s, Queue.class));
    }

    public CompletableFuture<Boolean> play(String deviceId, String body) {
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.PLAY, "?device_id=" + deviceId, body);

        return response.thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> pause(String deviceId) {
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.PAUSE, "?device_id=" + deviceId, "");

        return response.thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> skipNext(String deviceId) {
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.SKIP_NEXT, "?device_id=" + deviceId, "");

        return response.thenApply(s -> {
            System.out.println("skipNext: " + s);
            return s;
        }).thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> skipPrevious(String deviceId) {
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.SKIP_PREVIOUS, "?device_id=" + deviceId, "");

        return response.thenApply(String::isEmpty);
    }

    public CompletableFuture<Boolean> addToQueue(String deviceId, String trackUri) {
        CompletableFuture<String> response = SpotifyWebInterface.request(accessToken, EndpointRegistry.ADD_QUEUE, "?uri=%s&device_id=%s".formatted(trackUri.replace(":", "%3A"), deviceId), "");

        return response.thenApply(s -> {
            System.out.println("addToQueue: " + s);
            return s;
        }).thenApply(String::isEmpty);
    }
}
