import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class BackendPlaceholder implements BackendInterface {

  public BackendPlaceholder(GraphADT<String,Double> graph) { }

  public void loadGraphData(String filename) throws IOException {}

  public List<String> getListOfAllLocations() {
    return Arrays.asList("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
  }

  public List<String> findShortestPath(String startLocation, String endLocation) {
    return Arrays.asList("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
  }

  public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
    return Arrays.asList(176.0, 80.0);
  }

  public String getMostDistantLocation(String location) throws NoSuchElementException {
    return "Atmospheric, Oceanic and Space Sciences";
  }

}
