package puzzles.crossing;

import puzzles.common.solver.*;

import java.util.*;

/**
 * Main class that drives the program to complete the Crossing BFS puzzle
 *
 * @author John West (jrw2936)
 */
public class Crossing extends Solver{

    /**
     * Drives the program to complete the Crossing BFS puzzle and displays all steps.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Crossing pups wolves"));
        } else {
            int pups = Integer.parseInt(args[0]);
            int wolves = Integer.parseInt(args[args.length-1]);
            CrossingConfig config = new CrossingConfig(pups, wolves, 0, 0, "left");
            System.out.println("Pups: " + pups + " Wolves: " + wolves);
            Collection<Configuration> path = Solver.solve(config);
            if (path != null){
                ArrayList<Configuration> finalPath = new ArrayList<>(path);
                for (int i = 0; i < path.size(); i++){
                    System.out.println("Step " + i + ": " + finalPath.get(i).toString());
                }
            }
            else {
                System.out.println("No path was found...");
            }
        }
    }
}
