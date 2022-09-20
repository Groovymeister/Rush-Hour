package puzzles.common.solver;

import java.util.Collection;

/**
 * An interface to be used by Configuration classes for puzzle solving
 *
 * @author John West (jrw2936)
 */
public interface Configuration {
    boolean isSolution();
    Collection<Configuration> getNeighbors();
    boolean equals(Object other);
    int hashCode();
    String toString();
}