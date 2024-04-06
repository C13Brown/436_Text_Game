package objectAdventure.core.Utilities;
// $Id: GameMapUtils.java 326 2023-10-19 14:07:09Z aconover $

import java.io.*;
import java.util.*;

/**
 * Mostly, experimental Utility class for working with the game map.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
final class GameMapUtils {
    private GameMapUtils() {
    }

    /**
     * EXPERIMENT: Idea in progress...
     * Read the contents of a file containing room connections.
     * <p>
     * The file should contain a list of room numbers and the connections for each room.
     * <p>
     * Each room connection line is of the form:
     * roomNumber northConnection eastConnection southConnection westConnection upConnection downConnection
     *
     * @param fileName The name of the file to read.
     * @return A Map<Integer, List<Integer>> containing the room connections.
     */
    public static Map<Integer, List<Integer>> readRoomConnectionFile(String fileName) {
        Map<Integer, List<Integer>> roomConnections = new TreeMap<>();

        //AJCTODO: Clean this up so it reads one line at time, adding the connections as it goes.
        try (Scanner s = new Scanner(new File(fileName))) {
            while (s.hasNextInt()) {
                int roomNumber = s.nextInt();
                List<Integer> connections = new ArrayList<>();

                // Add the connections for the room.
                for (int i = 0; i < 6; i++) {
                    connections.add(s.nextInt());
                }

                // Add the room to the map.
                roomConnections.put(roomNumber, connections);
            }

            return roomConnections;

        } catch (FileNotFoundException e) {
            // display error message
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * EXPERIMENT: Idea in progress...
     * Generate a dot file for the given graph.
     *
     * @param graph    The graph to generate the dot file for.
     * @param filename The name of the file to write the dot file to.
     */
    public static void generateDotFile(Map<Integer, ? extends List<Integer>> graph, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);

            writer.write("digraph GameMap {\n");

            // Write out nodes
            for (int node : graph.keySet()) {
                writer.write("\t" + node + ";\n");
            }

            // Write out edges
            for (int node : graph.keySet()) {
                List<Integer> neighbors = graph.get(node);
                for (int neighbor : neighbors) {
                    writer.write("\t" + node + " -> " + neighbor + ";\n");
                }
            }

            writer.write("}");

            writer.close();
        } catch (IOException e) {
            // display error message
            System.err.println("Error writing to file " + filename + ": " + e.getMessage());
        }
    }

    // Just a test of dumping the map as a .DOT file.
//        public static void main(String[] args) {
//            GameMapUtils.generateDotFile(new GameMap().map, "map.dot");
//        }

}
