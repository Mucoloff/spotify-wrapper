package net.echo.registry;

public enum EndpointRegistry {

    CURRENTLY_PLAYING("/me/player/currently-playing"),
    PLAY("/me/player/play"),
    PAUSE("/me/player/pause"),
    SKIP_NEXT("/me/player/next"),
    SKIP_PREVIOUS("/me/player/previous"),
    QUEUE("/me/player/queue");

    public static final String BASE_URL = "https://api.spotify.com/v1";
    private final String endpoint;

    EndpointRegistry(String endpoint) {
        this.endpoint = BASE_URL + endpoint;
    }

    public String getUrl() {
        return endpoint;
    }

    public static String getUrl(EndpointRegistry endpoint) {
        return BASE_URL + endpoint.endpoint;
    }
}