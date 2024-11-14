import java.util.List;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import org.testfx.framework.junit5.ApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class FrontendDeveloperTests extends ApplicationTest {

    @BeforeEach
    public void setup() throws Exception {
        // Set up the backend and launch the frontend application
        Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));
        ApplicationTest.launch(Frontend.class);
    }

    @Test
    public void testLabelsCreatedAndExist() {
        // Test if the labels are created and have the correct text
        Frontend frontend = new Frontend();
        Label label = lookup("#src").query();
        Assertions.assertEquals("Path Start Selector:", label.getText());
        Label labelDestination = lookup("#dst").query();
        Assertions.assertEquals("Path End Selector:", labelDestination.getText());
        Button button = lookup("#find").query();
        Assertions.assertEquals("Submit/Find", button.getText());
    }

    @Test
    public void testPathListDisplay() {
        // Test if the path list is displayed correctly
        Frontend frontend = new Frontend();
        Button button = lookup("#find").query();
        clickOn("#srcBox").write("Memorial Union");
        clickOn("#dstBox").write("Computer Science");
        clickOn(button);
        List<String> locations = Arrays.asList("Union South", "Computer Sciences and Statistics",
                "Atmospheric, Oceanic and Space Sciences");
        Label label = lookup("#path").query();
        Assertions.assertEquals(
                locations.toString(), label.getText());
    }

    @Test
    public void testShowTimes() {
        // Test if the travel times are shown correctly
        Frontend frontend = new Frontend();
        Button button = lookup("#find").query();
        clickOn("#srcBox").write("Memorial Union");
        clickOn("#dstBox").write("Computer Science");
        CheckBox checkbox = lookup("#travelTimes").query();
        Assertions.assertFalse(checkbox.isSelected());
        clickOn(checkbox);
        Assertions.assertTrue(checkbox.isSelected());
        clickOn(button);
        List<String> locations = Arrays.asList("Union South", "Computer Sciences and Statistics",
                "Atmospheric, Oceanic and Space Sciences");
        List<String> times = Arrays.asList("176.0", "80.0");
        Label label = lookup("#path").query();
        Label timesLabel = lookup("#times").query();
        Assertions.assertEquals(
                locations.toString(), label.getText());
        Assertions.assertEquals(
                times.toString(), timesLabel.getText());
    }

    @Test
    public void testFurthestDestinationControls() {
        // Test the furthest destination controls
        Frontend frontend = new Frontend();
        Button button = lookup("#findFurthest").query();
        clickOn("#furthestBox").write("Memorial Union");
        clickOn(button);
        Label label = lookup("#furthestFrom").query();
        Assertions.assertEquals("Most Distance Location: Atmospheric, Oceanic and Space Sciences", label.getText());
    }

}
