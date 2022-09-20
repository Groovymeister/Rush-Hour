package puzzles.common.solver;

import java.util.*;

/**
 * Class used for solving puzzles using a breadth-first-search algorithm.
 *
 * @author RIT CS Dept: Produced BFS algorithm
 * @author John West (jrw2936@rit.edu): Modified for use with configuration objects.
 */
public class Solver{
    public static int totalConfigs;
    public static int uniqueConfigs;

    /**
     * @param config Initial configuration of the puzzle.
     * @return The shortest path to the intended solution.
     */
    public static Collection<Configuration> solve(Configuration config) {
        Queue<Configuration> queue = new LinkedList<>();
        List<Configuration> total = new ArrayList<>();
        queue.add(config);
        total.add(config);
        HashMap<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(config, null);
        while (!queue.isEmpty() && !queue.peek().isSolution()) {
            Configuration current = queue.remove();
            for (Configuration neighbor : current.getNeighbors()) {
                total.add(neighbor);
                if (!predecessors.containsKey(neighbor)) {
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        totalConfigs = total.size();
        uniqueConfigs = predecessors.size();
        if (queue.isEmpty()){
            return null;
        }
        else {
            List< Configuration > path = new LinkedList<>();
            Configuration temp = queue.remove();
            path.add( 0, temp);
            Configuration node = predecessors.get(temp);
            while (node != null) {
                path.add( 0, node);
                node = predecessors.get(node);
            }
            return path;
        }
    }
}
