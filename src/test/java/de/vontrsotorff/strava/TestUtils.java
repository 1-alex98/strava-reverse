package de.vontrsotorff.strava;

import de.vontrsotorff.strava.domain.Coordinate;
import de.westnordost.osmapi.map.data.Element;
import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.OsmLatLon;
import de.westnordost.osmapi.map.data.Way;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static Node mockNode(Coordinate coordinate){
        Node mock = mock(Node.class);
        when(mock.getPosition()).thenReturn(new OsmLatLon(coordinate.lat(), coordinate.lon()));
        var id= new Random().nextLong();
        when(mock.getId()).thenReturn(id);
        return mock;
    }

    public static Way mockWay(Node... node) {
        Way mock = mock(Way.class);
        var list = Arrays.stream(node).map(Element::getId).collect(Collectors.toList());
        when(mock.getNodeIds()).thenReturn(
                list
        );
        return mock;
    }
}
