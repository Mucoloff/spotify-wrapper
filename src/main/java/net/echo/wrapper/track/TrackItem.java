package net.echo.wrapper.track;

import com.google.gson.annotations.SerializedName;
import net.echo.wrapper.Album;
import net.echo.wrapper.Artist;
import net.echo.wrapper.Image;

public class TrackItem {

    @SerializedName("name")
    private String trackName;

    @SerializedName("artists")
    private Artist[] artists;

    @SerializedName("album")
    private Album album;

    @SerializedName("duration_ms")
    public int durationMs;

    public String getTrackName() {
        return trackName;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public Image[] getImages() {
        if (album != null) {
            return album.getImages();
        }

        return null;
    }

    public int getDurationMs() {
        return durationMs;
    }
}
