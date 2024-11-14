import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GraphPlaceholder implements GraphADT<String, Double> {

  private final List<String> nodes;

  public GraphPlaceholder() {
    nodes = new ArrayList<>();
    nodes.add("Union South");
    nodes.add("Computer Sciences and Statistics");
    nodes.add("Atmospheric, Oceanic and Space Sciences");
  }

  @Override
  public boolean insertNode(String data) {
    return false; // Static graph; modifications are not supported
  }

  @Override
  public boolean removeNode(String data) {
    return false; // Static graph; modifications are not supported
  }

  @Override
  public boolean containsNode(String data) {
    return nodes.contains(data);
  }

  @Override
  public int getNodeCount() {
    return nodes.size();
  }

  @Override
  public boolean insertEdge(String pred, String succ, Double weight) {
    return false; // Static graph; modifications are not supported
  }

  @Override
  public boolean removeEdge(String pred, String succ) {
    return false; // Static graph; modifications are not supported
  }

  @Override
  public boolean containsEdge(String pred, String succ) {
    return (pred.equals("Union South") && succ.equals("Computer Sciences and Statistics"))
        || (pred.equals("Computer Sciences and Statistics")
            && succ.equals("Atmospheric, Oceanic and Space Sciences"));
  }

  @Override
  public Double getEdge(String pred, String succ) {
    if (pred.equals("Union South") && succ.equals("Computer Sciences and Statistics")) {
      return 176.0;
    } else if (pred.equals("Computer Sciences and Statistics")
        && succ.equals("Atmospheric, Oceanic and Space Sciences")) {
      return 127.2;
    } else {
      throw new NoSuchElementException();
    }
  }

  @Override
  public int getEdgeCount() {
    return 2; // There are exactly two edges defined
  }

  @Override
  public List<String> shortestPathData(String start, String end) {
    if (start.equals("Union South") && end.equals("Atmospheric, Oceanic and Space Sciences")) {
      return nodes; // Returns the direct path for this specific case
    }
    return new ArrayList<>(); // No valid path for other cases
  }

  @Override
  public double shortestPathCost(String start, String end) {
    if (start.equals("Union South") && end.equals("Atmospheric, Oceanic and Space Sciences")) {
      return 176.0 + 127.2; // Sum of the defined edge weights
    }
    return Double.POSITIVE_INFINITY; // No valid path found
  }

  public List<String> getAllLocations() {
    return new ArrayList<>(nodes); // Helper method to list all nodes
  }
}
