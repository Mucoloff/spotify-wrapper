package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Album {

    @SerializedName("images")
    private Image[] images;


}
