package de.vontrsotorff.strava.domain;

import de.westnordost.osmapi.map.data.Node;

public record Path(Node node, double distance, Path parent) {
}
