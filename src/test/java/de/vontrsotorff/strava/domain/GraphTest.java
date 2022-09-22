package de.vontrsotorff.strava.domain;

import de.vontrsotorff.strava.CoordinateUtils;
import de.vontrsotorff.strava.GraphCreationService;
import de.vontrsotorff.strava.PossibleStartPointsService;
import de.westnordost.osmapi.map.data.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static de.vontrsotorff.strava.TestUtils.mockNode;
import static de.vontrsotorff.strava.TestUtils.mockWay;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GraphTest {

    @Test
    void getNearestNodesWithAnExampleOfTwoWays() {
        Graph graph = new Graph();
        var node11= mockNode(new Coordinate(50.8469522288313, 7.022282492316446));
        var node12= mockNode(new Coordinate(50.84689002425202, 7.027378958353663));
        var node13= mockNode(new Coordinate(50.84652924816051, 7.034423519780646));
        var node21= mockNode(new Coordinate(50.84373426893931, 7.027221242639238));
        var node22= mockNode(new Coordinate(50.84476547949343, 7.027300043553715));
        var node23= mockNode(new Coordinate(50.844292867674056, 7.034344256061944));
        var target= new Coordinate(50.84672833285967, 7.030783163365415);
        graph.addNode(node11);
        graph.addNode(node12);
        graph.addNode(node13);
        graph.addNode(node21);
        graph.addNode(node22);
        graph.addNode(node23);
        Way closestWay = mockWay(node11, node12, node13);
        graph.addWay(closestWay);
        graph.addWay(mockWay(node21, node22, node23));
        graph.finalizeCreation();
        Graph.WayWithDistance nearestNodes = graph.getNearestNodes(target);
        assertEquals(nearestNodes.wayWithNodes().way(), closestWay);
        assertTrue(nearestNodes.nodesMinDist().contains(node12));
        assertTrue(nearestNodes.nodesMinDist().contains(node13));
        assertTrue(nearestNodes.distance()<=10);
    }

    @Test
    void testDistanceWithNoWayPointsResultsInMaxValue() {
        Graph.MinDist distance = new Graph().distance(new Coordinate(0, 0), new WayWithNodes(null, List.of()));
        assertEquals(distance.dist(), Double.MAX_VALUE);
    }

    @Test
    void testDistanceWithThreeWaypointsResultsInCorrectResult() {
        Node nodeClose1 = mockNode(new Coordinate(50.84478802689815, 7.0272706173484725));
        Node nodeClose2 = mockNode(new Coordinate(50.844293325839104, 7.0343302588182075));
        Graph.MinDist distance = new Graph()
                .distance(new Coordinate(50.84484220509153, 7.030382026292729),
                        new WayWithNodes(null,
                                List.of(
                                        mockNode(new Coordinate(50.842823402338304, 7.026390912382546)),
                                        nodeClose1,
                                        nodeClose2
                                )));
        assertTrue(distance.dist()>= 20 && distance.dist()<=40);
        assertEquals(2, distance.nodes().size());
        assertTrue(distance.nodes().contains(nodeClose1));
        assertTrue(distance.nodes().contains(nodeClose2));
    }

    @Test
    public void testGetConnectedNodesWithoutStartAndSecondNode(){
        Graph graph = new Graph();
        var node11= mockNode(new Coordinate(50.8469522288313, 7.022282492316446));
        var node21= mockNode(new Coordinate(50.8469522289913, 7.122282492316446));
        var node12= mockNode(new Coordinate(50.84689002425202, 7.027378958353663));
        var node13= mockNode(new Coordinate(50.84652924816051, 7.034423519780646));
        graph.addNode(node11);
        graph.addNode(node12);
        graph.addNode(node13);
        graph.addNode(node21);
        Way way = mockWay(node11, node12, node13);
        graph.addWay(way);
        graph.addWay(mockWay(node11, node21));
        graph.finalizeCreation();

        final var connectedNodesWithoutStartAndSecondNode = graph.getConnectedNodesWithoutStartAndSecondNode(node12, Set.of(node11));
        assertEquals(1, connectedNodesWithoutStartAndSecondNode.size());
        assertTrue(connectedNodesWithoutStartAndSecondNode.contains(node13));
        final var connectedNodesWithoutStartAndSecondNode2 = graph.getConnectedNodesWithoutStartAndSecondNode(node11, null);
        assertEquals(2, connectedNodesWithoutStartAndSecondNode2.size());
        assertTrue(connectedNodesWithoutStartAndSecondNode2.contains(node12));
        assertTrue(connectedNodesWithoutStartAndSecondNode2.contains(node21));
    }

    @Test
    public void testGetNearestNodeWithExample(){
        final var graph = GraphCreationService.getInstance()
                .createGraph(new Coordinate(50.65412, 7.08089));
        assertTrue(graph.getWays().stream()
                .anyMatch(wayWithNodes -> wayWithNodes.way().getId() == 89321181));
        var nodes = graph
                .getNearestNodes(new Coordinate(50.65412, 7.08089));

        assertEquals(89321181, nodes.wayWithNodes().way().getId());
    }

    @Test
    public void testGetNearestNodeWithExample2(){
        final var graph = GraphCreationService.getInstance()
                .createGraph(new Coordinate(50.67129, 7.06906));
        assertTrue(graph.getWays().stream()
                .anyMatch(wayWithNodes -> wayWithNodes.way().getId() == 89321181));
        var nodes = graph
                .getNearestNodes(new Coordinate(50.67129, 7.06906));

        assertEquals(9885860, nodes.wayWithNodes().way().getId());
    }

}