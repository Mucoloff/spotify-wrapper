package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;
import net.echo.wrapper.track.Track;

public class Queue {

    @SerializedName("queue")
    private Track[] queue;

    public Track[] getQueue() {
        return queue;
    }

    public void setQueue(Track[] queue) {
        this.queue = queue;
    }
}