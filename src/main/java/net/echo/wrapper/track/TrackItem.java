package net.echo.wrapper.track;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import net.echo.wrapper.Album;
import net.echo.wrapper.Artist;
import net.echo.wrapper.Image;

@Getter
@ToString
public class TrackItem {

    @SerializedName("name")
    private String trackName;

    @SerializedName("artists")
    private Artist[] artists;

    @SerializedName("album")
    private Album album;

    @SerializedName("duration_ms")
    private int durationMs;

    @SerializedName("uri")
    private String uri;


    public Image[] getImages() {
        if (album != null) {
            return album.getImages();
        }

        return null;
    }

}
