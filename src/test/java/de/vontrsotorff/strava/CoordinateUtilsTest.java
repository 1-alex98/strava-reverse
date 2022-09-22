package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateUtilsTest {

    @Test
    public void testDistanceFunction(){
        double distance = CoordinateUtils.distance(
                new Coordinate(50.74643592354311, 7.116554520408042),
                new Coordinate(50.93468700325154, 7.040839515125521)
        );
        assertTrue(distance<= 26.6*1000&& distance>= 20*1000);
    }

    @Test
    public void testShortestDistanceFunction(){
        double distance = CoordinateUtils.shortestDistance(
                new Coordinate(50.8394458258644, 7.018436142828564),
                new Coordinate(50.83821934765045, 7.033031736277756),
                new Coordinate(50.84172592171909, 7.025436007615989)
        );
        assertTrue(distance<= 315&& distance>= 310);
    }

}