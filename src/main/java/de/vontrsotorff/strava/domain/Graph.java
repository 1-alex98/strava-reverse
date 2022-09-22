package de.vontrsotorff.strava.domain;

import de.vontrsotorff.strava.CoordinateUtils;
import de.westnordost.osmapi.map.data.LatLon;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Way;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static de.vontrsotorff.strava.OsmUtils.toCoordinate;

@Data
public class Graph {
    private final List<WayWithNodes> ways = new ArrayList<>();
    private List<NodeWithWay> nodeWithWays;
    private final Map<Long, Node> nodes = new HashMap<>();


    public void addWay(Way way) {
        final var nodesForWay = getNodesForWay(way);
        ways.add(new WayWithNodes(way, nodesForWay));
    }

    private List<Node> getNodesForWay(Way way) {
        return way.getNodeIds().stream()
                .map(nodes::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public void finalizeCreation(){
        nodeWithWays = nodes.values().stream()
                .map(node -> new NodeWithWay(node, getWaysForNode(node)))
                .collect(Collectors.toList());
    }

    private List<WayWithNodes> getWaysForNode(Node node) {
        return ways.stream()
                .filter(wayWithNodes -> wayWithNodes.nodes().contains(node))
                .collect(Collectors.toList());
    }

    public Set<Node> getConnectedNodesWithoutStartAndSecondNode(Node node, @Nullable Set<Node> without) {
        var nodeWithWay = nodeWithWays.stream()
                .filter(nodeWithWayCandidate -> nodeWithWayCandidate.node().getId() == node.getId())
                .findFirst()
                .orElseThrow();
        var result = nodeWithWay.wayWithNodes().stream()
                .map(wayWithNodes -> getAdjacent(node, wayWithNodes.nodes()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        if(without != null){
            result.removeAll(without);
        }
        return result;
    }

    private Set<Node> getAdjacent(Node node, List<Node> nodes) {
        final var index = nodes.indexOf(node);
        var result = new HashSet<Node>();
        if(index!= 0){
            result.add(nodes.get(index-1));
        }
        if(index != nodes.size()-1){
            result.add(nodes.get(index+1));
        }
        return result;
    }

    public record WayWithDistance(WayWithNodes wayWithNodes, double distance, Set<Node> nodesMinDist){}

    public WayWithDistance getNearestNodes(Coordinate coordinate) {
        return ways.stream()
                .map(wayWithNodes -> {
                    var distance = distance(coordinate, wayWithNodes);
                    return new WayWithDistance(wayWithNodes, distance.dist, distance.nodes);
                })
                .min(Comparator.comparing(WayWithDistance::distance))
                .orElseThrow();
    }

    record MinDist(double dist, Set<Node> nodes){}
    MinDist distance(Coordinate coordinate, WayWithNodes wayWithNodes) {
        MinDist minDist;
        var iter = wayWithNodes.nodes().iterator();
        var previous = iter.hasNext() ? iter.next() : null;
        if(previous == null){
            return new MinDist(Double.MAX_VALUE, null);
        }
        minDist = new MinDist(
                CoordinateUtils.distance(coordinate, toCoordinate(previous.getPosition())),
                Set.of(previous)
                );
        while (iter.hasNext()) {
            final var current = iter.next();
            MinDist distance = distance(coordinate, previous, current);
            if(distance.dist < minDist.dist) {
                minDist = distance;
            }
            previous = current;
        }
        var distLast = CoordinateUtils.distance(coordinate, toCoordinate(previous.getPosition()));
        if (distLast<= minDist.dist) {
            minDist = new MinDist(
                    distLast,
                    Set.of(previous)
            );
        }
        return minDist;
    }

    private MinDist distance(Coordinate coordinate, Node previous, Node current) {
        var aCoordinate = toCoordinate(previous.getPosition());
        var bCoordinate = toCoordinate(current.getPosition());
        double dist = CoordinateUtils.shortestDistance(aCoordinate, bCoordinate, coordinate);
        return new MinDist(dist, Set.of(previous, current));
    }


}
