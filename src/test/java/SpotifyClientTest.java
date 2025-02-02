import net.echo.client.SpotifyClient;
import net.echo.wrapper.Track;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SpotifyClientTest {

    private final String accessToken = "<YOUR-ACCESS-TOKEN>";
    private final SpotifyClient spotifyClient = new SpotifyClient(accessToken);

    @Test
    public void testGetCurrentlyPlaying() throws IOException {
        Track track = spotifyClient.getCurrentTrack();

        System.out.println(track.getTrackName());
        System.out.println(track.getArtistName());
        System.out.println(track.getImageUrl());
    }
}