package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("name")
    private String trackName;

    @SerializedName("artists")
    private Artist[] artists;

    @SerializedName("album")
    private Album album;

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        if (artists != null && artists.length > 0) {
            return artists[0].getName();
        }
        return "";
    }

    public String getImageUrl() {
        if (album != null && album.getImages() != null && album.getImages().length > 0) {
            return album.getImages()[0].getUrl();
        }
        return "";
    }
}