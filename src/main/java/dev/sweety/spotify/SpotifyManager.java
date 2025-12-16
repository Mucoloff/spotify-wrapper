package dev.sweety.spotify;

import com.sun.net.httpserver.HttpServer;
import dev.sweety.spotify.auth.AuthToken;
import dev.sweety.spotify.auth.SpotifyOAuth;
import dev.sweety.spotify.client.SpotifyClient;
import dev.sweety.spotify.util.StopWatch;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SpotifyManager {

    private final int port;

    public SpotifyManager(String callback, int port, String clientId, String clientSecret) {
        this.port = port;
        this.oAuth = new SpotifyOAuth(callback, clientId, clientSecret);
    }

    private final SpotifyOAuth oAuth;
    private final SpotifyClient client = new SpotifyClient();

    private final StopWatch lastUpdate = new StopWatch();

    @Setter
    @Getter
    private String refreshToken;

    @Setter
    private Consumer<URL> open = url -> {
    };

    @Setter
    private BiConsumer<String, Object[]> info = (format, args) -> {
    };

    @Setter
    private BiConsumer<String, Object[]> err = (format, args) -> {
    };

    private int expire = 5 * 60 * 1000;

    public SpotifyClient client() {
        // If the last update was 5 minutes ago, we renew the token
        if (lastUpdate.hasPassed(expire) || !authenticated()) this.refreshAuth();
        return client;
    }

    public String setRefreshToken() {
        return refreshToken;
    }

    public boolean authenticated() {
        return client.getAccessToken() != null;
    }

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public void startServer(BiConsumer<HttpServer, String> codeCallback) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/callback", exchange -> {
                String query = exchange.getRequestURI().getQuery();
                String code = query.split("code=")[1];

                String response = loadResource("callback/spotify.html");

                byte[] bytes = response.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }

                codeCallback.accept(server, code);
            });

            info("Server listening...");
            server.start();
            isRunning.set(true);
        } catch (Exception e) {
            err("exception caught: ", e);
        }
    }

    public void refreshAuth() {
        this.lastUpdate.reset();

        Consumer<String> auth = token -> this.oAuth.refreshAccessToken(token).thenAccept(authToken -> {
            String refreshToken = authToken.getRefreshToken();

            if (!isNull(refreshToken)) this.refreshToken = refreshToken;

            this.client.setAccessToken(authToken.getAccessToken());
            this.lastUpdate.reset();
        });

        if (!isNull(this.refreshToken)) auth.accept(this.refreshToken);
        else doAuth().thenAccept(auth);
    }

    public CompletableFuture<String> doAuth() {

        CompletableFuture<String> future = new CompletableFuture<>();

        BiConsumer<HttpServer, String> callback = (server, code) ->
                this.oAuth.getAccessToken(code)
                        .thenAccept(token -> {
                            AuthToken newToken = this.oAuth.refreshAccessToken(token.getRefreshToken()).join();

                            if (!isNull(token.getRefreshToken())) this.refreshToken = token.getRefreshToken();

                            this.expire = newToken.getExpiresIn();
                            this.lastUpdate.reset();
                            this.client.setAccessToken(newToken.getAccessToken());

                            future.complete(newToken.getAccessToken());
                            server.stop(5);
                            isRunning.set(false);
                        })
                        .exceptionally(e -> {
                            err("exception caught: ", e);
                            return null;
                        });

        startServer(callback);

        String url = this.oAuth.getAuthorizeUrl("user-read-playback-state user-read-email");

        try {
            open.accept(new URL(url));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return future;
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    private void info(String format, Object... args) {
        info.accept(format, args);
    }

    private void err(String format, Object... args) {
        err.accept(format, args);
    }

    @SneakyThrows
    private static String loadResource(String path) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
             Scanner scanner = new Scanner(in == null ? SpotifyManager.class.getResourceAsStream(path) : in, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    private static <T> boolean isNull(T t) {
        if (t == null) return true;
        if (t instanceof CharSequence c && c.isEmpty()) return true;
        return false;
    }
}
