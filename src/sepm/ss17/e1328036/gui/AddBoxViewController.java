package sepm.ss17.e1328036.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sepm.ss17.e1328036.domain.Box;
import sepm.ss17.e1328036.service.Service;
import sepm.ss17.e1328036.service.ServiceException;
import sepm.ss17.e1328036.service.SimpleService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Evgeni Batev
 * Controller for the box add interface.
 */
public class AddBoxViewController implements Initializable {

    @FXML
    private TextField sizeField, sawdustField, strawField, priceField;

    @FXML
    private RadioButton trueRadio, falseRadio;

    @FXML
    private Button browseButton, addButton, cancelButton;

    private ToggleGroup toggleGroup;

    private String currentRadioButtonValue;

    private final FileChooser fileChooser = new FileChooser();

    private Service service;

    private String path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        path = null;
        service = new SimpleService();
        toggleGroup = new ToggleGroup();
        trueRadio.setToggleGroup(toggleGroup);
        falseRadio.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {

                RadioButton button = (RadioButton)t1.getToggleGroup().getSelectedToggle();
                currentRadioButtonValue = button.getId();
            }
        });
    }

    @FXML
    public void onAddClicked() {
        saveData();

        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }

    private void saveData() {
        String size = sizeField.getText();
        String sawdust = sawdustField.getText();
        String straw = strawField.getText();
        String price = priceField.getText();
        boolean hasWindow = false;

        if(sawdust.isEmpty()) {
            sawdust = "0";
        }

        if(straw.isEmpty()) {
            straw = "0";
        }

        switch (currentRadioButtonValue) {
            case "trueRadio":
                hasWindow = true;
                break;
            case "falseRadio":
                hasWindow = false;
                break;
        }

        boolean onError = false;

        if(!Main.isFloat(size)) {
            onError = true;
            Main.showAlert("Error", "Invalid input.", "Invalid input in the size field.", Alert.AlertType.ERROR);
        }

        if(!Main.isFloat(price)) {
            onError = true;
            Main.showAlert("Error", "Invalid input.", "Invalid input in the price field.", Alert.AlertType.ERROR);
        }

        if(!Main.isInteger(sawdust)) {
            onError = true;
            Main.showAlert("Error", "Invalid input.", "Invalid input in the sawdust field.", Alert.AlertType.ERROR);
        }

        if(!Main.isInteger(straw)) {
            onError = true;
            Main.showAlert("Error", "Invalid input.", "Invalid input in the straw field.", Alert.AlertType.ERROR);
        }

        if (!onError) {
            try {
                path = path == null ? "file:src\\sepm\\ss17\\e1328036\\images\\NoBox.png" : path;
                service.addBox(new Box(0, Main.getFloat(size), Main.getInteger(sawdust), Main.getInteger(straw), hasWindow, Main.getFloat(price), path, false));
            } catch (ServiceException e) {
                Main.showAlert("Error", "Problem while saving the new box.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void onBrowseClicked() {

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle("Choose a photo:");

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");

        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            String filePath = file.getAbsolutePath();
            String folderPath = new File(".").getAbsolutePath();
            path = filePath.substring(folderPath.length() - 1);
            path = "file:" + path;
        }
    }

    @FXML
    public void onCancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
