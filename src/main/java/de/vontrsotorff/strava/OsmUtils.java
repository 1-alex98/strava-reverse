package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;
import de.westnordost.osmapi.map.data.LatLon;

public class OsmUtils {

    public static Coordinate toCoordinate(LatLon position) {
        return new Coordinate(position.getLatitude(), position.getLongitude());
    }
}
