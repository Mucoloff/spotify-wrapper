import com.sun.net.httpserver.HttpServer;
import net.echo.oauth.SpotifyOAuth;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SpotifyOAuthTest {

    private final String callback = "http://localhost:8888/callback";
    private final String clientId = "<YOUR-CLIENT-ID>";
    private final String clientSecret = "<YOUR-CLIENT-SECRET>";
    private final SpotifyOAuth spotifyOAuth = new SpotifyOAuth(callback, clientId, clientSecret);

    @Test
    public void testGetOAuth() throws IOException, InterruptedException {
        startServer();

        String url = spotifyOAuth.getAuthorizeUrl("user-read-playback-state");
        System.out.println("Follow this url: " + url);

        while (true) {
            Thread.sleep(500);
        }
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

        server.createContext("/callback", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String code = query.split("code=")[1];

            System.out.println("Authorization code: " + code);

            String response = "Authorization completed, you can now close your browser.";
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

            spotifyOAuth.getAccessToken(code)
                    .thenAccept(token -> System.out.println("Auth Token: " + token))
                    .exceptionally(e -> {
                        e.printStackTrace(System.err);
                        return null;
                    });
        });

        System.out.println("Server listening...");
        server.start();
    }
}
