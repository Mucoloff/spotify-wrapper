package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Image {

    @SerializedName("url")
    private String url;

    @Override
    public String toString() {
        return url;
    }
}