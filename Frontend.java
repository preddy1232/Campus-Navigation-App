import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.scene.control.Alert;


/**
 * Frontend class that implements the FrontendInterface and extends Application
 * to create the GUI for the project.
 */
public class Frontend extends Application implements FrontendInterface {

    private static Backend back; // backend object
    private boolean hideTimes = true; // boolean to hide travel times
    private VBox pathListDisplay;

    /**
     * Sets the backend object.
     * 
     * @param back backend object
     */
    public static void setBackend(Backend back) {
        Frontend.back = back;
    }

    /**
     * Starts the GUI.
     * 
     * @param stage stage object
     */
    public void start(Stage stage) {
        Pane root = new Pane();
        createAllControls(root);
        try {
           back.loadGraphData("campus.dot");
        } catch (IOException e) {
            System.out.println("Error loading graph data: " + e.getMessage());
          // Optionally, use a dialog to inform the user that loading failed
    }

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Project 2: Dijkstra's Algorithm");
        stage.show();
    }

    /**
     * Creates all the controls for the GUI.
     * 
     * @param parent parent pane
     */
    public void createAllControls(Pane parent) {
        createShortestPathControls(parent);
        createPathListDisplay(parent);
        createAdditionalFeatureControls(parent);
        createAboutAndQuitControls(parent);
    }

    /**
     * Creates the controls for the shortest path.
     * 
     * @param parent parent pane
     */
    public void createShortestPathControls(Pane parent) {
        Label src = new Label("Path Start Selector:");
        TextField srcBox = new TextField("");
        srcBox.setId("srcBox");
        srcBox.setLayoutX(150);
        srcBox.setLayoutY(16);
        src.setId("src");
        src.setLayoutX(32);
        src.setLayoutY(16);
        parent.getChildren().add(src);
        parent.getChildren().add(srcBox);

        Label dst = new Label("Path End Selector:");
        TextField dstBox = new TextField("");
        dstBox.setId("dstBox");
        dstBox.setLayoutX(150);
        dstBox.setLayoutY(48);
        dst.setId("dst");
        dst.setLayoutX(32);
        dst.setLayoutY(48);
        parent.getChildren().add(dst);
        parent.getChildren().add(dstBox);

        Button find = new Button("Submit/Find");
        find.setId("find");
        find.setLayoutX(32);
        find.setLayoutY(80);
        parent.getChildren().add(find);

        find.setOnAction(actionEvent -> {
        String start = srcBox.getText();
        String end = dstBox.getText();
        // Clear previous path and time labels
        parent.getChildren().removeIf(node -> node instanceof Label && (node.getId().equals("path") || node.getId().equals("times")));

        // Display path
        Label path = new Label(back.findShortestPath(start, end).toString());
        path.setId("path");
        path.setLayoutX(32);
        path.setLayoutY(152);
        parent.getChildren().add(path);

        // Optionally calculate and display travel times in minutes
        if (!hideTimes) {
          List<Double> timesList = back.getTravelTimesOnPath(start, end); // Assuming this returns a List of times in seconds
          double totalSeconds = timesList.stream().mapToDouble(Double::doubleValue).sum();
          double totalMinutes = totalSeconds / 60.0; // Convert seconds to minutes

          // Format the total minutes to two decimal places
          String formattedMinutes = String.format("%.2f minutes", totalMinutes);

          Label times = new Label(formattedMinutes);
          times.setId("times");
          times.setLayoutX(32);
          times.setLayoutY(200);
          parent.getChildren().add(times);
      }
         });
    }

    /**
     * Creates the path list display.
     * 
     * @param parent parent pane
     */
    public void createPathListDisplay(Pane parent) {
      pathListDisplay = new VBox();
      pathListDisplay.setId("pathListDisplay");
      pathListDisplay.setLayoutX(32);
      pathListDisplay.setLayoutY(152);
      parent.getChildren().add(pathListDisplay);

      Label times = new Label("");
      times.setId("timesDisplay");
      times.setLayoutX(32);
      times.setLayoutY(200);
      times.setVisible(!hideTimes);  // Set visibility based on the current state of hideTimes
      parent.getChildren().add(times);
    }

    /**
     * Creates the additional feature controls.
     * 
     * @param parent parent pane
     */
    public void createAdditionalFeatureControls(Pane parent) {
        createFurthestDestinationControls(parent);
        createTravelTimesBox(parent);
    }

    /**
     * Creates the furthest destination controls.
     * 
     * @param parent parent pane
     */
    public void createFurthestDestinationControls(Pane parent) {
       Label furthest = new Label("Furthest Destination Selector:");
       TextField furthestBox = new TextField();
       furthestBox.setId("furthestBox");
       furthestBox.setLayoutX(550);
       furthestBox.setLayoutY(16);
       furthest.setId("furthest");
       furthest.setLayoutX(500);
       furthest.setLayoutY(16);
       parent.getChildren().add(furthest);
       parent.getChildren().add(furthestBox);

       Button findFurthest = new Button("Find Furthest");
       findFurthest.setId("findFurthest");
       findFurthest.setLayoutX(500);
       findFurthest.setLayoutY(48);
       parent.getChildren().add(findFurthest);

       Label furthestFromLabel = new Label();
       furthestFromLabel.setId("furthestFrom");
       furthestFromLabel.setLayoutX(500);
       furthestFromLabel.setLayoutY(80);
       parent.getChildren().add(furthestFromLabel);

       findFurthest.setOnAction(actionEvent -> {
        try {
            String mostDistantLocation = back.getMostDistantLocation(furthestBox.getText());
            furthestFromLabel.setText("Most Distant Location: " + mostDistantLocation);
        } catch (NoSuchElementException e) {
            furthestFromLabel.setText(e.getMessage());
        }
      });
     }
    /**
     * Creates the travel times box.
     * 
     * @param parent parent pane
     */
    public void createTravelTimesBox(Pane parent) {
        // implements backends getTravelTimesOnPath method
        CheckBox travelTimes = new CheckBox("Travel Times");
        travelTimes.setId("travelTimes");
        travelTimes.setLayoutX(200);
        travelTimes.setLayoutY(80);
        parent.getChildren().add(travelTimes);
        travelTimes.setOnAction(actionEvent -> {
        hideTimes = !hideTimes;
        });
    }

    /**
     * Creates the about and quit controls.
     * 
     * @param parent parent pane
     */
    public void createAboutAndQuitControls(Pane parent) {
        Button about = new Button("About");
        about.setId("about");
        about.setLayoutX(32);
        about.setLayoutY(560);
        parent.getChildren().add(about);
        about.setOnAction(event -> {
            Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
            aboutAlert.setTitle("About");
            aboutAlert.setHeaderText("Path Finding Application");
            aboutAlert.setContentText("This application helps users find the shortest path " +
                    "between locations on a map. It calculates the walking times between the " +
                    "locations and displays the results.");

            aboutAlert.showAndWait();
        });

        // exits the program
        Button quit = new Button("Quit");
        quit.setId("quit");
        quit.setLayoutX(96);
        quit.setLayoutY(560);
        parent.getChildren().add(quit);
        quit.setOnAction(actionEvent -> System.exit(0));
    }

}
