package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.vontrsotorff.strava.TestUtils.mockNode;
import static org.junit.jupiter.api.Assertions.*;

class PossibleStartPointsServiceTest {

    @Test
    public void testGetInitialPointsWithExample(){
        Optional<Coordinate> initPoints = PossibleStartPointsService.getInstance()
                .getInitPoints(
                        new Coordinate(50.85402967972876, 7.046775665553244),
                        mockNode(new Coordinate(50.85735812411724, 7.051582406985005)),
                        200
                );
        assertTrue(initPoints.isPresent());
        assertTrue(initPoints.get().lat()<= 50.8553286041016+0.0001 && initPoints.get().lat()>=50.8553286041016-0.0001);
        assertTrue(initPoints.get().lon()<= 7.048672550242326+0.0001 && initPoints.get().lon()>=7.048672550242326-0.0001);
        assertTrue(CoordinateUtils.distance(new Coordinate(50.85533875207779, 7.048672550700627), initPoints.get())<=5);
    }

    @Test
    public void testGetPossibleStartingPointsWithExample(){
        var result = PossibleStartPointsService.getInstance()
                .getPossibleStartingPoints(
                        new Coordinate(50.65412, 7.08089),
                        200
                );
        assertEquals(2, result.size());
        var firstExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.65585 +0.0001
                        && coordinate.lat() >= 50.65585 -0.0001
                        && coordinate.lon() <= 7.08017 + 0.0001
                        && coordinate.lon() >= 7.08017 - 0.0001
        );
        assertTrue(firstExpectedCoordinate);
        var secondExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.65238 +0.0001
                        && coordinate.lat() >= 50.65238 -0.0001
                        && coordinate.lon() <= 7.08161 + 0.0001
                        && coordinate.lon() >= 7.08161 - 0.0001
        );
        assertTrue(secondExpectedCoordinate);
    }

    @Test
    public void testGetPossibleStartingPointsWithExampleTwo(){
        var result = PossibleStartPointsService.getInstance()
                .getPossibleStartingPoints(
                        new Coordinate(50.67129, 7.06906),
                        200
                );
        assertEquals(9, result.size());
        var firstExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.66989 +0.0001
                        && coordinate.lat() >= 50.66989 -0.0001
                        && coordinate.lon() <= 7.06900 + 0.0001
                        && coordinate.lon() >= 7.06900 - 0.0001
        );
        assertTrue(firstExpectedCoordinate);
        var secondExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.67001+0.0001
                        && coordinate.lat() >= 50.67001 -0.0001
                        && coordinate.lon() <= 7.07073 + 0.0001
                        && coordinate.lon() >= 7.07073 - 0.0001
        );
        assertTrue(secondExpectedCoordinate);
        var threeExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.67005+0.0001
                        && coordinate.lat() >= 50.67005 -0.0001
                        && coordinate.lon() <= 7.07110 + 0.0001
                        && coordinate.lon() >= 7.07110 - 0.0001
        );
        assertTrue(threeExpectedCoordinate);
        var fourExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.66986+0.0001
                        && coordinate.lat() >= 50.66986 -0.0001
                        && coordinate.lon() <= 7.06772 + 0.0001
                        && coordinate.lon() >= 7.06772 - 0.0001
        );
        assertTrue(fourExpectedCoordinate);
        var fiveExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.67003+0.0001
                        && coordinate.lat() >= 50.67003 -0.0001
                        && coordinate.lon() <= 7.06721 + 0.0001
                        && coordinate.lon() >= 7.06721 - 0.0001
        );
        assertTrue(fiveExpectedCoordinate);
        var sixExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.67267+0.0001
                        && coordinate.lat() >= 50.67267 -0.0001
                        && coordinate.lon() <= 7.06742 + 0.0001
                        && coordinate.lon() >= 7.06742 - 0.0001
        );
        assertTrue(sixExpectedCoordinate);
        var sevenExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.67248+0.0001
                        && coordinate.lat() >= 50.67248 -0.0001
                        && coordinate.lon() <= 7.06717 + 0.0001
                        && coordinate.lon() >= 7.06717 - 0.0001
        );
        assertTrue(sevenExpectedCoordinate);
        var eightExpectedCoordinate = result.stream()
                .map(PossibleStartPointsService.Point::point)
                .anyMatch(
                coordinate ->
                        coordinate.lat() <=50.67273+0.0001
                        && coordinate.lat() >= 50.67273 -0.0001
                        && coordinate.lon() <= 7.06837 + 0.0001
                        && coordinate.lon() >= 7.06837 - 0.0001
        );
        assertTrue(eightExpectedCoordinate);
    }

}