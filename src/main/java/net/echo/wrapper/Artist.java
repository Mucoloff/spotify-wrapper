package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
public class Artist {

    @SerializedName("name")
    private String name;

    @Override
    public String toString() {
        return name;
    }
}