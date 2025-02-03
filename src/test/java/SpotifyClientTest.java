import net.echo.client.SpotifyClient;
import net.echo.wrapper.track.Track;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpotifyClientTest {

    private final String accessToken = "<YOUR-ACCESS-TOKEN>";
    private final SpotifyClient spotifyClient = new SpotifyClient(accessToken);

    @Test
    public void testGetCurrentlyPlaying() throws IOException {
        Track track = spotifyClient.getCurrentTrack();

        assertNotNull(track);
        System.out.println(track.getItem().getTrackName());
        System.out.println(Arrays.toString(track.getItem().getArtists()));
        System.out.println(Arrays.toString(track.getItem().getImages()));
    }
}