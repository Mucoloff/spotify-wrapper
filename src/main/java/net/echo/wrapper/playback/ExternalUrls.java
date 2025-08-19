package net.echo.wrapper.playback;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExternalUrls {

    @SerializedName("spotify")
    private String spotify;

}
