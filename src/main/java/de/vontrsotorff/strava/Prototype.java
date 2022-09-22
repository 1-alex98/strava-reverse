package de.vontrsotorff.strava;

import de.westnordost.osmapi.OsmConnection;
import de.westnordost.osmapi.map.data.BoundingBox;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Relation;
import de.westnordost.osmapi.map.data.Way;
import de.westnordost.osmapi.map.handler.MapDataHandler;
import de.westnordost.osmapi.overpass.OverpassMapDataApi;

public class Prototype {
    public static void main(String[] args) {
        OsmConnection connection = new OsmConnection("https://maps.mail.ru/osm/tools/overpass/api/interpreter", "my user agent");
        OverpassMapDataApi overpass = new OverpassMapDataApi(connection);
        overpass.queryElements(
                """
                            nwr(50.74193365346485, 7.119239781643016,50.74840917157416, 7.121654295849223);
                            out meta;
                        """,
                new MapDataHandler() {
                    @Override
                    public void handle(BoundingBox bounds) {
                        System.out.println(bounds);
                    }

                    @Override
                    public void handle(Node node) {
                        System.out.println(node);
                    }

                    @Override
                    public void handle(Way way) {
                        System.out.println(way);
                    }

                    @Override
                    public void handle(Relation relation) {
                        System.out.println(relation);
                    }
                }
        );
    }


}
