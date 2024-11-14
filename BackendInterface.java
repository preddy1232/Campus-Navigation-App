import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface BackendInterface {

  /*
   * Implementing classes should support the constructor below.
   * @param graph object to sture the backend's graph data
   */
  // public BackendInterface(GraphADT<String,Double> graph);

  /**
   * Loads graph data from a dot file.
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was a problem reading in the specified file
   */
  public void loadGraphData(String filename) throws IOException;

  /**
   * Returns a list of all locations (nodes) available on the backend's graph.
   * @return list of all location names
   */
  public List<String> getListOfAllLocations();

  /**
   * Return the sequence of locations along the shortest path from startLocation to endLocation, or
   * en empty list if no such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or
   *         an empty list if no such path exists
   */
  public List<String> findShortestPath(String startLocation, String endLocation);

  /**
   * Return the walking times in seconds between each two nodes on the shortest path from startLocation
   * to endLocation, or an empty list of no such path exists.
   * @param startLocation the start location of the path
   * @param endLocation the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   *         startLocation to endLocation, or an empty list if no such path exists
   */
  public List<Double> getTravelTimesOnPath(String startLocation, String endLocation);

  /**
   * Return the most distant location from startLocation that is reachable in the graph.
   * @param startLocation the location to find the most distant location for
   * @return the location that is most distant (has the longest overall walking time)
   * @throws NoSuchElementException if startLocation does not exist
   */
  public String getMostDistantLocation(String startLocation) throws NoSuchElementException;

}
