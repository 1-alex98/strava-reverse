package de.vontrsotorff.strava.domain;


public record Coordinate(double lat, double lon) {
    @Override
    public String toString() {
        return lat+", "+lon;
    }
}
