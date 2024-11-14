import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FrontendPlaceholder extends Application implements FrontendInterface {
  
  private static BackendPlaceholder back;

  public static void setBackend(BackendPlaceholder back) {
    FrontendPlaceholder.back = back;
  }

  public void start(Stage stage) {
    Pane root = new Pane();

    createAllControls(root);

    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.setTitle("P2: Prototype");
    stage.show();
  }

  public void createAllControls(Pane parent) {
    createShortestPathControls(parent);
    createPathListDisplay(parent);
    createAdditionalFeatureControls(parent);
    createAboutAndQuitControls(parent);
  }

  public void createShortestPathControls(Pane parent) {
    Label src = new Label("Path Start Selector:  Memorial Union");
    src.setLayoutX(32);
    src.setLayoutY(16);
    parent.getChildren().add(src);

    Label dst = new Label("Path End Selector: Computer Science");
    dst.setLayoutX(32);
    dst.setLayoutY(48);
    parent.getChildren().add(dst);

    Button find = new Button("Submit/Find Button");
    find.setLayoutX(32);
    find.setLayoutY(80);
    parent.getChildren().add(find);
  }

  public void createPathListDisplay(Pane parent) {
    Label path =
        new Label(
            "Results List: \n\tMemorial Union\n\tSciene Hall\n\tPyschology\n\tComputer Science" +
            "\n\n" +
            "Results List (with walking times):\n\tMemorial Union\n\t-(30sec)->Science Hall\n\t-(170sec)->Psychology\n\t-(45sec)->Computer Science\n\tTotal time: 4.08min"
            );
    path.setLayoutX(32);
    path.setLayoutY(112);
    parent.getChildren().add(path);
  }

  public void createAdditionalFeatureControls(Pane parent) {
    this.createTravelTimesBox(parent);
    this.createFurthestDestinationControls(parent);
  }

  public void createTravelTimesBox(Pane parent) {
    CheckBox showTimesBox = new CheckBox("Show Walking Times");
    showTimesBox.setLayoutX(200);
    showTimesBox.setLayoutY(80);
    parent.getChildren().add(showTimesBox);
  }

  public void createFurthestDestinationControls(Pane parent) {
    Label locationSelector = new Label("Location Selector:  Memorial Union");
    locationSelector.setLayoutX(500);
    locationSelector.setLayoutY(16);
    parent.getChildren().add(locationSelector);
    Button furthestFromButton = new Button("Find Most Distant Location");
    furthestFromButton.setLayoutX(500);
    furthestFromButton.setLayoutY(48);
    parent.getChildren().add(furthestFromButton);
    Label furthestFromLabel = new Label("Most Distance Location:  Union South");
    furthestFromLabel.setLayoutX(500);
    furthestFromLabel.setLayoutY(80);
    parent.getChildren().add(furthestFromLabel);
  }

  public void createAboutAndQuitControls(Pane parent) {
    Button about = new Button("About");
    about.setLayoutX(32);
    about.setLayoutY(560);
    parent.getChildren().add(about);

    Button quit = new Button("Quit");
    quit.setLayoutX(96);
    quit.setLayoutY(560);
    parent.getChildren().add(quit);
  }
}
