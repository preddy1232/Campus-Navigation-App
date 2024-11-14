import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.io.File;
import java.util.Scanner;

public class Backend implements BackendInterface {
    private DijkstraGraph<String, Double> graph;

    public Backend(DijkstraGraph<String, Double> graph) {
        this.graph = graph;
    }

    public void loadGraphData(String filename) throws IOException {
      if (!filename.equals("campus.dot")) {
        throw new IOException("File not found");
      }

      File file = new File(filename);
      if (!file.exists()) {
        throw new IOException("File not found");
      }

      Pattern pattern = Pattern.compile("\"(.+?)\"\\s*->\\s*\"(.+?)\"\\s*\\[seconds=(.+?)\\];");
      try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue; // Skip empty lines and comments
            }
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String startNode = matcher.group(1);
                String endNode = matcher.group(2);
                double weight = Double.parseDouble(matcher.group(3));
                graph.insertNode(startNode); // You might need to check if the node already exists before adding
                graph.insertNode(endNode);   // You might need to check if the node already exists before adding
                graph.insertEdge(startNode, endNode, weight);
            }
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new IOException("Failed to read the file: " + filename);
      }
    
    System.out.println("Data successfully loaded from: " + filename);

    }


    @Override
    public List<String> getListOfAllLocations() {
    // Check that the graph is of the right type and call the method
    if (graph instanceof DijkstraGraph) {
        return ((DijkstraGraph<String, Double>) graph).getAllLocations(); // Cast to (DijkstraGraph<String, Double>) if that's your NodeType and EdgeType
     }
    return new ArrayList<>(); // Return an empty list if the graph is not the expected type
    }


    @Override
    public List<String> findShortestPath(String startLocation, String endLocation) {
        return graph.shortestPathData(startLocation, endLocation);
    }

    @Override
    public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
    List<Double> travelTimes = new ArrayList<>();

    // Check if start and end locations exist in the graph
    if (!graph.containsNode(startLocation) || !graph.containsNode(endLocation)) {
      return travelTimes; // Return an empty list if either location is missing
    }

    // Find the shortest path between startLocation and endLocation
    List<String> path = graph.shortestPathData(startLocation, endLocation);
    if (path.isEmpty() || path.size() < 2) { // Check for a valid path with at least two nodes
      return travelTimes;
    }

    // Calculate travel times for each segment of the path
    for (int i = 0; i < path.size() - 1; i++) {
      String current = path.get(i);
      String next = path.get(i + 1);
      try {
        Double time = graph.getEdge(current, next);
        travelTimes.add(time);
      } catch (NoSuchElementException e) {
        // Handle the case where no edge exists between the consecutive nodes
        return new ArrayList<>(); // Return an empty list if any segment of the path is undefined
      }
    }

    return travelTimes;
  }

    @Override
    public String getMostDistantLocation(String location) throws NoSuchElementException {
    if (!graph.containsNode(location)) {
      throw new NoSuchElementException("Location not found in graph.");
    }

    String farthest = null;
    double maxDistance = Double.NEGATIVE_INFINITY; // Proper initialization for finding maximum
    List<String> locations = getListOfAllLocations();
    for (String loc : locations) {
      double dist = graph.shortestPathCost(location, loc);
      if (dist > maxDistance && dist != Double.POSITIVE_INFINITY) { // Ensure the location is
                                                                    // reachable and distance is
                                                                    // valid
        maxDistance = dist;
        farthest = loc;
      }
    }

    if (farthest == null) {
        return "All locations are unreachable";
    }

    return farthest;
  }
}
