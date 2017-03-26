package sepm.ss17.e1328036.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sepm.ss17.e1328036.dto.Box;
import sepm.ss17.e1328036.service.Service;
import sepm.ss17.e1328036.service.ServiceException;
import sepm.ss17.e1328036.service.SimpleService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by evgen on 26.03.2017.
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

    private Desktop desktop = Desktop.getDesktop();

    private String imagePath = "";

    private Service service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                service.addBox(new Box(0, Main.getFloat(size), Main.getInteger(sawdust), Main.getInteger(straw), hasWindow, Main.getFloat(price), imagePath, false));
            } catch (ServiceException e) {
                Main.showAlert("Error", "Problem while saving the new box.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void onBrowseClicked() {
        File file = fileChooser.showOpenDialog((Stage)cancelButton.getScene().getWindow());
        if (file != null) {
            imagePath = file.getAbsolutePath();
        }
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Main.showAlert("Error", "Problem occurred while opening the file.", "Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onCancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
