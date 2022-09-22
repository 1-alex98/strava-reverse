package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;
import de.vontrsotorff.strava.domain.Graph;
import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import de.westnordost.osmapi.map.handler.MapDataHandler;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;

public class GraphCreationService {
    private static final GraphCreationService instance= new GraphCreationService();

    public static GraphCreationService getInstance() {
        return instance;
    }

    private GraphCreationService(){}

    public Graph createGraph(Coordinate coordinate){
        Graph graph = new Graph();
        overpassQuery(coordinate, graph);
        return graph;
    }

    private static void overpassQuery(Coordinate coordinate, Graph graph) {
        OsmConnection connection = new OsmConnection("https://maps.mail.ru/osm/tools/overpass/api/interpreter", "my user agent");
        OverpassMapDataApi overpass = new OverpassMapDataApi(connection);
        overpass.queryElements(
                """
                    (node(%f, %f, %f, %f);way(%f, %f, %f, %f)[highway];);
                    out meta;
                """.formatted(
                        coordinate.lat()-0.01, coordinate.lon()-0.01, coordinate.lat()+0.01,coordinate.lon()+0.01,
                        coordinate.lat()-0.01, coordinate.lon()-0.01, coordinate.lat()+0.01,coordinate.lon()+0.01
                        ),
                new MapDataHandler() {
                    @Override
                    public void handle(BoundingBox bounds) {}

                    @Override
                    public void handle(Node node) {
                        graph.addNode(node);
                    }

                    @Override
                    public void handle(Way way) {
                        graph.addWay(way);
                    }

                    @Override
                    public void handle(Relation relation) {}
                }
        );
        graph.finalizeCreation();
    }
}
