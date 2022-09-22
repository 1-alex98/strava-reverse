package de.vontrsotorff.strava.domain;

import de.westnordost.osmapi.map.data.Node;
import de.westnordost.osmapi.map.data.Way;

import java.util.List;

public record NodeWithWay(Node node, List<WayWithNodes> wayWithNodes) {
}
