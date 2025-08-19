package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.echo.wrapper.track.Track;

@Setter
@Getter
@ToString
public class Queue {

    @SerializedName("queue")
    private Track[] queue;

}