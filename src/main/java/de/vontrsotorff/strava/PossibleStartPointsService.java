package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;
import de.vontrsotorff.strava.domain.Graph;
import de.vontrsotorff.strava.domain.Path;
import de.westnordost.osmapi.map.data.Node;
import mil.nga.sf.geojson.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.vontrsotorff.strava.OsmUtils.toCoordinate;

public class PossibleStartPointsService {
    private static final PossibleStartPointsService instance= new PossibleStartPointsService();

    public static PossibleStartPointsService getInstance() {
        return instance;
    }

    private PossibleStartPointsService(){}

    public Set<Point> getPossibleStartingPoints(Coordinate coordinate, double distanceInMeters){
        Graph graph = GraphCreationService.getInstance()
                .createGraph(coordinate);
        Graph.WayWithDistance nearestNodes = graph.getNearestNodes(coordinate);
        return getWayPoint(distanceInMeters, nearestNodes.nodesMinDist(), coordinate, graph);
    }

    public String getPossibleStartingPointsAsGeoJson(Coordinate coordinate, double distanceInMeters){
        final var possibleStartingPoints = getPossibleStartingPoints(coordinate, distanceInMeters);
        final var points = possibleStartingPoints.stream()
                .map(point -> new Position(point.point().lon(), point.point().lat()))
                .map(mil.nga.sf.geojson.Point::new)
                .map(Feature::new)
                .collect(Collectors.toSet());
        var collection = new FeatureCollection(points);
        return FeatureConverter.toStringValue(collection);
    }

    record PathAndPoints(Set<Path> paths, Set<Point> points){}

    private Set<Point> getWayPoint(double maxDistance, Set<Node> nodesMinDist, Coordinate coordinate, Graph graph) {
        var initialPaths = nodesMinDist.stream()
                .map(node -> getInitPath(coordinate, node, maxDistance))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        var initialPoints = nodesMinDist.stream()
                .map(node -> getInitPoints(coordinate, node, maxDistance))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(coordinatePoint -> new Point(coordinatePoint, null))
                .collect(Collectors.toSet());
        var pathsAndPoints = new PathAndPoints(initialPaths, initialPoints);
        while (!pathsAndPoints.paths().isEmpty()){
            pathsAndPoints = getNewPath(pathsAndPoints, maxDistance, graph);
        }
        return pathsAndPoints.points();
    }

    record PathAndConnectedNodes(Path path, Set<Node> connectedNodes){}

    public record Point(Coordinate point, Path parent){}
    record PathOrPoint(Path path, Point point){}
    private PathAndPoints getNewPath(PathAndPoints pathAndPoints, double maxDistance, Graph graph) {
        var pathsAndPointsSet = pathAndPoints.paths().stream()
                .map(path ->
                        new PathAndConnectedNodes(
                                path,
                                graph.getConnectedNodesWithoutStartAndSecondNode(
                                        path.node(),
                                        Optional.ofNullable(path.parent())
                                                .map(this::getAllPreviousNodes)
                                                .orElse(pathAndPoints.paths().stream()
                                                        .map(Path::node).collect(Collectors.toSet()))
                                )
                        )
                )
                .map(pathAndConnectedNodes -> deriveNewPathOrPoints(pathAndConnectedNodes, maxDistance))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        final var newPoints = pathsAndPointsSet.stream().filter(ele -> ele.point != null).map(PathOrPoint::point).collect(Collectors.toSet());
        newPoints.addAll(pathAndPoints.points());
        return new PathAndPoints(
                pathsAndPointsSet.stream().filter(ele -> ele.path != null).map(PathOrPoint::path).collect(Collectors.toSet()),
                newPoints
        );
    }

    private Set<Node> getAllPreviousNodes(final Path parent) {
        if(parent == null) return Set.of();
        var set = new HashSet<Node>();
        set.add(parent.node());
        set.addAll(getAllPreviousNodes(parent.parent()));
        return set;
    }

    private Set<PathOrPoint> deriveNewPathOrPoints(PathAndConnectedNodes pathAndConnectedNodes, double maxDistance) {
        return pathAndConnectedNodes.connectedNodes().stream()
                .map(node -> {
                    var nodeCoordinate = toCoordinate(node.getPosition());
                    var originCoordinate = toCoordinate(pathAndConnectedNodes.path().node().getPosition());
                    final var distance = CoordinateUtils.distance(originCoordinate, nodeCoordinate);
                    final var distanceAlready = pathAndConnectedNodes.path().distance();
                    var overallDistance = distanceAlready + distance;
                    if(overallDistance >= maxDistance){
                        return new PathOrPoint(
                                null,
                                new Point(
                                        getPoint(originCoordinate, nodeCoordinate, maxDistance-distanceAlready, distance),
                                        pathAndConnectedNodes.path()
                                )
                        );
                    }
                    return new PathOrPoint(
                            new Path(node, overallDistance, pathAndConnectedNodes.path()),
                            null
                    );
                })
                .collect(Collectors.toSet());

    }

    Optional<Coordinate> getInitPoints(Coordinate coordinate, Node node, double maxDistance) {
        var distance = CoordinateUtils.distance(toCoordinate(node.getPosition()), coordinate);
        if(maxDistance >= distance) return Optional.empty();
        return Optional.of(getPoint(coordinate, toCoordinate(node.getPosition()), maxDistance, distance));
    }

    @NotNull
    private static Coordinate getPoint(Coordinate coordinate, Coordinate nodeCoordinate, double maxDistance, double distance) {
        var percentOfWay = maxDistance / distance;
        var latDifference = nodeCoordinate.lat() - coordinate.lat();
        var lonDifference = nodeCoordinate.lon() - coordinate.lon();
        return new Coordinate(coordinate.lat() + percentOfWay * latDifference, coordinate.lon() + percentOfWay * lonDifference);
    }

    private static Optional<Path> getInitPath(Coordinate coordinate, Node node, double maxDistance) {
        double distance = CoordinateUtils.distance(toCoordinate(node.getPosition()), coordinate);
        if(maxDistance < distance) return Optional.empty();
        return Optional.of(new Path(node, distance, null));
    }
}
