package net.echo.wrapper.device;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Devices {
    @SerializedName("devices")
    private Device[] devices;

}
