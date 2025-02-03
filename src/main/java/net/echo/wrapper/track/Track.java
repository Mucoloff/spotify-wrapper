package net.echo.wrapper.track;

import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("repeat_state")
    private String repeat;

    @SerializedName("shuffle_state")
    private boolean shuffle;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("progress_ms")
    private Integer progress;

    @SerializedName("is_playing")
    private boolean isPlaying;

    @SerializedName("item")
    private TrackItem item;

    public String getRepeat() {
        return repeat;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Integer getProgress() {
        return progress;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public TrackItem getItem() {
        return item;
    }
}
