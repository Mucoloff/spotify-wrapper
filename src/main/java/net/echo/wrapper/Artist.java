package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}