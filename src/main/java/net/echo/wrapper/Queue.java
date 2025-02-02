package net.echo.wrapper;

import com.google.gson.annotations.SerializedName;

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