package puzzles.jam.solver;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.Car;
import puzzles.jam.model.Coordinates;
import puzzles.jam.model.JamConfig;
import java.util.*;
import java.io.*;

/**
 * Main class that drives the program to complete the Jam BFS puzzle
 *
 * @author John West (jrw2936)
 */
public class Jam {

    /**
     * Drives the program to complete the Jam BFS puzzle.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }
        else {
            String filename = args[0];
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String[] dimensions = br.readLine().split(" ");
            int numCars = Integer.parseInt(br.readLine());
            ArrayList<Car> cars = new ArrayList<>();
            for (int i = 0; i < numCars; i++) {
                String nextLine = br.readLine();
                if (nextLine != null) {
                    String[] temp = nextLine.split(" ");
                    cars.add(new Car(temp[0].charAt(0),
                            new Coordinates(Integer.parseInt(temp[1]), Integer.parseInt(temp[2])),
                            new Coordinates(Integer.parseInt(temp[3]), Integer.parseInt(temp[4]))));
                }
            }

            JamConfig config = new JamConfig(Integer.parseInt(dimensions[0]),
                    Integer.parseInt(dimensions[1]),
                    numCars,
                    cars);

            Collection<Configuration> path = Solver.solve(config);

            System.out.println("Total Configurations: " + Solver.totalConfigs);
            System.out.println("Unique Configurations: " + Solver.uniqueConfigs);
            System.out.println("File: " + filename);
            System.out.println(config);
            if (path != null){
                ArrayList<Configuration> finalPath = new ArrayList<>(path);
                for (int i = 0; i < path.size(); i++){
                    System.out.println("Step " + i + ": " + System.lineSeparator() + finalPath.get(i).toString());
                }
            }
            else {
                System.out.println("No path was found...");
            }
        }
    }
}