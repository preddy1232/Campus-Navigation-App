// === CS400 File Header Information ===
// Name: Prithvi Reddy
// Email: pdreddy@wisc.edu
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Lecture 3 Florian
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

    /**
    * Constructor that sets the map that the graph uses.
    */
    public DijkstraGraph() {
     super(new HashtableMap<>());
    }

   @Override
    public List<NodeType> getAllLocations() {
      // Additional logic specific to DijkstraGraph
      // For example, filtering or processing locations based on Dijkstra-specific criteria
      List<NodeType> locations = super.getAllLocations();
      // Modify or process locations list as required
      return locations;
   }
  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException("Start or end node not found");
    }

    PriorityQueue<SearchNode> priorityQueue = new PriorityQueue<>();
    MapADT<NodeType, SearchNode> visitedNodes = new PlaceholderMap<>();

    // Initialized the start node in the priority queue
    priorityQueue.add(new SearchNode(nodes.get(start), 0, null));

    while (!priorityQueue.isEmpty()) {
      SearchNode currentNode = priorityQueue.poll();

      // If the node has been visited with a cheaper path, I skip it
      if (visitedNodes.containsKey(currentNode.node.data)) {
        continue;
      }
      visitedNodes.put(currentNode.node.data, currentNode);
      if (currentNode.node.data.equals(end)) {
        return currentNode;
      }

      for (BaseGraph<NodeType, EdgeType>.Edge edge : currentNode.node.edgesLeaving) {
        NodeType successorData = edge.successor.data;
        double edgeWeight = edge.data.doubleValue();
        double newCost = currentNode.cost + edgeWeight;

        // If the successor has not been visited or I found a cheaper path to the successor
        if (!visitedNodes.containsKey(successorData)
            || newCost < visitedNodes.get(successorData).cost) {
          // Added the successor to the priority queue
          priorityQueue.add(new SearchNode(edge.successor, newCost, currentNode));
        }
      }
    }
    throw new NoSuchElementException("No path found from " + start + " to " + end);
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    SearchNode endNode;
    try {
      endNode = computeShortestPath(start, end);
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("No path exists from " + start + " to " + end);
    }

    // If endNode is null, it means computeShortestPath didn't find a path but didn't throw an
    // exception
    if (endNode == null) {
      throw new NoSuchElementException("No path could be computed from " + start + " to " + end);
    }

    LinkedList<NodeType> path = new LinkedList<>();
    for (SearchNode current = endNode; current != null; current = current.predecessor) {
      path.addFirst(current.node.data);
    }

    return path;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path from the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
   public double shortestPathCost(NodeType start, NodeType end) {
      try {
          SearchNode endNode = computeShortestPath(start, end);
          if (endNode == null || endNode.cost == Double.POSITIVE_INFINITY) {
              return Double.POSITIVE_INFINITY; // Indicating no path exists
          }
        return endNode.cost;
      } catch (Exception e) {
          return Double.POSITIVE_INFINITY; // Gracefully handle errors by returning a special value
      }
   }
  /**
   * Test the shortest path from node A to G as traced in class using Dijkstra's algorithm. The
   * expected path is A -> B -> C -> G with a total cost of 7.
   */
  @Test
  void testShortestPathFromAtoG() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertEdge("A", "B", 1);
    graph.insertEdge("B", "C", 4);
    graph.insertEdge("D", "B", 1);
    graph.insertEdge("C", "D", 3);
    graph.insertEdge("E", "C", 2);
    graph.insertEdge("C", "G", 2);
    graph.insertEdge("D", "F", 5);
    graph.insertEdge("E", "G", 4);
    graph.insertEdge("A", "D", 3);
    graph.insertEdge("A", "F", 5);
    graph.insertEdge("F", "E", 4);
    graph.insertEdge("G", "F", 2);
    graph.insertEdge("G", "B", 5);
    graph.insertEdge("E", "D", 3);


    String start = "A";
    String end = "G";

    List<String> expectedPath = List.of("A", "B", "C", "G");
    double expectedCost = 7;
    List<String> actualPath = graph.shortestPathData(start, end);
    double actualCost = graph.shortestPathCost(start, end);

    Assertions.assertEquals(expectedPath, actualPath);
    Assertions.assertEquals(expectedCost, actualCost);
  }

  /**
   * Test the shortest path on the same graph for a different start and end node. This test assumes
   * a different path than the one provided, such as from A to E.
   */
  @Test
  void testShortestPathWithDifferentStartEnd() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertEdge("A", "B", 1);
    graph.insertEdge("B", "C", 4);
    graph.insertEdge("D", "B", 1);
    graph.insertEdge("C", "D", 3);
    graph.insertEdge("E", "C", 2);
    graph.insertEdge("C", "G", 2);
    graph.insertEdge("D", "F", 5);
    graph.insertEdge("E", "G", 4);
    graph.insertEdge("A", "D", 3);
    graph.insertEdge("A", "F", 5);
    graph.insertEdge("F", "E", 4);
    graph.insertEdge("G", "F", 2);
    graph.insertEdge("G", "B", 5);
    graph.insertEdge("E", "D", 3);



    String start = "C";
    String end = "F";

    List<String> expectedPath = List.of("C", "G", "F");
    double expectedCost = 4; // The sum of the costs on the path C -> G -> F

    List<String> actualPath = graph.shortestPathData(start, end);
    double actualCost = graph.shortestPathCost(start, end);

    Assertions.assertEquals(expectedPath, actualPath);
    Assertions.assertEquals(expectedCost, actualCost);
  }

  /**
   * Test the behavior of the implementation when searching for a path between nodes where no path
   * exists. For example, from node F to node A.
   */
  @Test
  void testNoPathScenario() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertEdge("A", "B", 1);
    graph.insertEdge("B", "C", 4);
    graph.insertEdge("D", "B", 1);
    graph.insertEdge("C", "D", 3);
    graph.insertEdge("E", "C", 2);
    graph.insertEdge("C", "G", 2);
    graph.insertEdge("D", "F", 5);
    graph.insertEdge("E", "G", 4);
    graph.insertEdge("A", "D", 3);
    graph.insertEdge("A", "F", 5);
    graph.insertEdge("F", "E", 4);
    graph.insertEdge("G", "F", 2);
    graph.insertEdge("G", "B", 5);
    graph.insertEdge("E", "D", 3);


    String start = "F";
    String end = "A";

    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathData(start, end));
    Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost(start, end));
  }
}
