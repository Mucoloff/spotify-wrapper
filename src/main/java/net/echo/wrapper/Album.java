package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;

public class Album {

    @SerializedName("images")
    private Image[] images;

    public Image[] getImages() {
        return images;
    }
}
