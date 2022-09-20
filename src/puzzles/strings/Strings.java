package puzzles.strings;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Main class that drives the program to complete the Strings BFS puzzle
 *
 * @author John West (jrw2936)
 */
public class Strings{

    /**
     * Drives the program to complete the Strings BFS puzzle and displays all steps, as well as the completed BFS path.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        }
        else {
            String start = args[0];
            String end = args[args.length-1];
            StringsConfig config = new StringsConfig(start, end);
            System.out.println("Start: " + start + ", End: " + end);
            Collection<Configuration> path = Solver.solve(config);
            if (path != null){
                ArrayList<Configuration> finalPath = new ArrayList<>(path);
                for (int i = 0; i < path.size(); i++){
                    System.out.println("Step " + i + ": " + finalPath.get(i).toString());
                }
                System.out.println(System.lineSeparator() + "Path : " + finalPath);
            }
            else {
                System.out.println("No path was found...");
            }

            }
        }
    }
