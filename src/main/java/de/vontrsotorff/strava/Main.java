package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;
import mil.nga.sf.geojson.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    record CoordinateAndDistance(Coordinate coordinate, double distance){}
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        var known = Set.of(
                new CoordinateAndDistance(
                      new Coordinate(
                              50.7, 7.0
                      ),
                        191
                )
        );
        final var instance = PossibleStartPointsService.getInstance();
        var points = known.stream()
                .map(coordinateAndDistance -> instance.getPossibleStartingPoints(coordinateAndDistance.coordinate, coordinateAndDistance.distance))
                .flatMap(Collection::stream)
                .map(point -> new Position(point.point().lon(), point.point().lat()))
                .map(mil.nga.sf.geojson.Point::new)
                .map(Feature::new)
                .collect(Collectors.toSet());
        var collection = new FeatureCollection(points);
        System.out.println(FeatureConverter.toStringValue(collection));
    }
}
