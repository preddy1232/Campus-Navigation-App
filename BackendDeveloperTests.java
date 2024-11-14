import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.testfx.framework.junit5.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import org.testfx.framework.junit5.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;




public class BackendDeveloperTests extends ApplicationTest{

  @Test
  public void testLoadGraphDataWithNonexistentFile() {
      // Create an instance of GraphADT and Backend for this test
      DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
      Backend backend = new Backend(graph);

      // Define the filename for the test
      String filename = "nonexistent.dot";

      try {
          // Attempt to load from a non-existent file
          backend.loadGraphData(filename);
      } catch (IOException e) {
          // If caught, assert something or do operations to indicate the test has passed
          Assertions.assertTrue(e.getMessage().contains("File not found"), "The error message should indicate the file was not found.");
      }
  }

    @Test
    public void testGetListOfAllLocations() {
        // Create an instance of the graph and populate it with the given data
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        // Initialize Backend with this graph
        Backend backend = new Backend(graph);
        try {
          backend.loadGraphData("campus.dot"); // Assuming this loads your actual graph data
         } catch (IOException e) {
           System.out.println("Error loading graph data: " + e.getMessage());
          // Optionally handle the exception (e.g., log it or ignore if the test can proceed without data)
       }

        // Retrieve locations from the backend
        List<String> locations = backend.getListOfAllLocations();

        // Assert that these specific locations are present in the list
        Assertions.assertTrue(locations.contains("Memorial Union"));
        Assertions.assertTrue(locations.contains("Science Hall"));
        Assertions.assertTrue(locations.contains("Brat Stand"));
        Assertions.assertTrue(locations.contains("Helen C White Hall"));
        Assertions.assertTrue(locations.contains("Union South"));
        Assertions.assertTrue(locations.contains("D.C. Smith Greenhouse"));
        Assertions.assertTrue(locations.contains("Radio Hall"));
        Assertions.assertTrue(locations.contains("Robert M. Lafollette School of Public Affairs"));
        Assertions.assertTrue(locations.contains("Carillon Tower"));
        Assertions.assertTrue(locations.contains("Arthur D. Hasler Laboratory of Limnology"));
    }

  @Test
  public void testFindShortestPath() {
    // Test to verify that findShortestPath returns the correct path between two locations.
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    Backend backend = new Backend(graph);
    
    // Attempt to load the graph data and handle IOException
    try {
        backend.loadGraphData("campus.dot"); // Assuming this loads your actual graph data
    } catch (IOException e) {
        System.out.println("Error loading graph data: " + e.getMessage());
        // Optionally handle the exception (e.g., log it or ignore if the test can proceed without data)
    }

    // Expected path from Bascom Hall to Brat Stand via Education Building and Radio Hall
    List<String> expectedPath = Arrays.asList("Bascom Hall", "Education Building", "Radio Hall", "Brat Stand");

    // Perform the test
    Assertions.assertEquals(expectedPath, backend.findShortestPath("Bascom Hall", "Brat Stand"),
        "The shortest path from Bascom Hall to Brat Stand does not match the expected.");
  }

  @Test
  public void testGetTravelTimesOnPath() {
    // Test to verify that getTravelTimesOnPath returns the correct list of travel times between
    // locations on the path.
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    Backend backend = new Backend(graph);
    try {
        backend.loadGraphData("campus.dot"); // Assuming this loads your actual graph data
    } catch (IOException e) {
        System.out.println("Error loading graph data: " + e.getMessage());
        // Optionally handle the exception (e.g., log it or ignore if the test can proceed without data)
    }

    List<Double> expectedTimes = Arrays.asList(145.1);
    Assertions.assertEquals(expectedTimes,
        backend.getTravelTimesOnPath("Union South", "Memorial Arch"));
  }

  @Test
  public void testGetMostDistantLocationSuccess() {
    // Test to verify that getMostDistantLocation returns the correct most distant location.
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    Backend backend = new Backend(graph);
    try {
        backend.loadGraphData("campus.dot"); // Assuming this loads your actual graph data
    } catch (IOException e) {
        System.out.println("Error loading graph data: " + e.getMessage());
        // Optionally handle the exception (e.g., log it or ignore if the test can proceed without data)
    }

    String expectedLocation = "Smith Residence Hall";
    Assertions.assertEquals(expectedLocation, backend.getMostDistantLocation("Union South"));
  }


  @BeforeEach
  public void setup() throws Exception {
   Backend backend = new Backend(new DijkstraGraph<String, Double>());
   try {
    backend.loadGraphData("campus.dot");
    Frontend.setBackend(backend);
    } catch(IOException e) {

     Assertions.fail("IOException thrown: " + e.getMessage());
    }
   ApplicationTest.launch(Frontend.class);
  }
 
  @Test
  public void testIntegrationShortestPathCorrectness() {
      // Simulate selecting two locations in the frontend and verify backend processes to find the shortest path correctly
      clickOn("#srcBox").write("Memorial Union");
      clickOn("#dstBox").write("Education Building");
      clickOn("#find"); // Correct ID for the find path button
      // Verify the shortest path is displayed correctly
      verifyThat("#pathListDisplay", hasText("[Memorial Union, Radio Hall, Education Building]"));
  }
  @Test
  public void testIntegrationDisplayTravelTimesAccuracy() {
   // This test checks if the frontend correctly displays travel times when enabled
      clickOn("#srcBox").write("Memorial Union");
      clickOn("#dstBox").write("Education Building");
      clickOn("#travelTimes"); // Correct ID for the checkbox for showing times
      clickOn("#find"); // Correct ID for the find path button
      // Verify that the walking times are displayed correctly
      verifyThat("#pathListDisplay", hasText("4.83 minutes"));
  }

  @Test
    public void testCreateShortestPathControls() {

        // Checking for labels
        Label label1 = lookup("#labelID1").query();
        Assertions.assertEquals("Path Start Selector: ", label1.getText());
        Label label2 = lookup("#labelID2").query();
        Assertions.assertEquals("Path End Selector: ", label2.getText());

        // Checking for the text field of the starting point
        clickOn("#textFieldID1");
        write("Memorial Union");
        TextField startingPointTextField = lookup("#textFieldID1").query();
        Platform.runLater(() -> startingPointTextField.requestFocus());
        Assertions.assertEquals("Memorial Union", startingPointTextField.getText());

        //  Checking for the text field of the destination point
        clickOn("#destinationField1");
        TextField destinationField = lookup("#destinationField1").query();
        Platform.runLater(() -> destinationField.requestFocus());
        write("Union South");
        Assertions.assertEquals("Union South", destinationField.getText());


    }
   /**
     * This test verifies if about and quit controls are created correctly.
     */
    @Test
    public void testCreateAboutAndQuitControls() {

        // Checking for the about button
        clickOn("#aboutButtonID");
        Button button = lookup("#aboutButtonID").queryButton();
        Platform.runLater(() -> button.requestFocus());
        Assertions.assertEquals("About",button.getText());

        // Checking for the quit button
        clickOn("#quitButtonID");
        Button button1 = lookup("#quitButtonID").queryButton();
        Platform.runLater(() -> button1.requestFocus());
        Assertions.assertEquals("Quit",button1.getText());


    }


}
