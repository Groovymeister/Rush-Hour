package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Configuration class used to solve the Strings puzzle.
 *
 * @author John West (jrw2936@rit.edu)
 */
public class StringsConfig implements Configuration {
    private final String start;
    private final String end;

    /**
     * Constructor for string configurations.
     *
     * @param start Starting string
     * @param end Ending string
     */
    public StringsConfig(String start, String end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the starting string value.
     *
     * @return The starting string value.
     */
    @Override
    public String toString() {
        return this.start;
    }

    /**
     * Returns the string hashcode of the starting string value.
     *
     * @return The string hashcode of the starting string value.
     */
    @Override
    public int hashCode(){
        return this.start.hashCode();
    }

    /**
     * Returns boolean containing whether the solution is achieved.
     *
     * @return Whether the start value of the configuration is the ending value.
     */
    @Override
    public boolean isSolution() {
        return this.start.equals(end);
    }

    /**
     * Determines whether two StringConfigs are the same
     *
     * @param other The other StringConfig
     * @return Whether the configs are the same
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof StringsConfig otherConfig) {
            result = this.start.equals(otherConfig.start) && this.end.equals(otherConfig.end);
        }
        return result;
    }

    /**
     * Returns a Hashset of valid neighbors for the given starting value of the configuration.
     *
     * @return A HashSet of neighbors of the starting string value.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        HashSet<Configuration> neighbors = new HashSet<>();
        for (int i = 0; i < start.length(); i++) {
            char ch = start.charAt(i);
            char firstNeighbor = (char) (ch + 1);
            char secondNeighbor = (char) (ch - 1);
            if (ch == 'A') {
                secondNeighbor = 'Z';
            }
            if (ch == 'Z') {
                firstNeighbor = 'A';
            }
            if (i >= 1) {
                neighbors.add(new StringsConfig((start.substring(0, i) + firstNeighbor + start.substring(i + 1)), end));
                neighbors.add(new StringsConfig((start.substring(0, i) + secondNeighbor + start.substring(i + 1)), end));
            }
            else {
                neighbors.add(new StringsConfig((firstNeighbor + start.substring(1)), end));
                neighbors.add(new StringsConfig((secondNeighbor + start.substring(1)), end));
            }
        }
        return neighbors;
    }
}
