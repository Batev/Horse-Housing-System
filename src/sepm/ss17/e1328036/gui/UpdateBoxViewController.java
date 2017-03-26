package sepm.ss17.e1328036.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sepm.ss17.e1328036.domain.Box;
import sepm.ss17.e1328036.service.Service;
import sepm.ss17.e1328036.service.ServiceException;
import sepm.ss17.e1328036.service.SimpleService;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by evgen on 25.03.2017.
 */
public class UpdateBoxViewController implements Initializable {

    @FXML
    private Label idLabel;

    @FXML
    private RadioButton sizeRadio, priceRadio;

    @FXML
    private TextField sizeText, priceText;

    @FXML
    private Button cancelButton, okButton;

    private ToggleGroup toggleGroup;

    private String currentRadioButtonValue;

    private Service service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new SimpleService();
        initializeButtons();

        Box box = BoxViewController.currentBox;
        idLabel.setText(box.getBid() + "");
    }

    private void initializeButtons() {
        toggleGroup = new ToggleGroup();
        sizeRadio.setToggleGroup(toggleGroup);
        priceRadio.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {

                RadioButton button = (RadioButton)t1.getToggleGroup().getSelectedToggle();
                currentRadioButtonValue = button.getId();

            }
        });
    }

    @FXML
    public void onOkClicked() {
        switch (currentRadioButtonValue) {
            case "sizeRadio":
                executeFloatUpdate(true);
                break;
            case "priceRadio":
                executeFloatUpdate(false);
                break;
        }
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    private void executeFloatUpdate(boolean isSize) {
        String text = null;
        text = isSize ? sizeText.getText() : priceText.getText();

        if (Main.isFloat(text)) {
            try {
                if(isSize) {
                    service.updateBoxSize(BoxViewController.currentBox.getBid(), Main.getFloat(text));
                }
                else {
                service.updateBoxPrice(BoxViewController.currentBox.getBid(), Main.getFloat(text));
                }
            } catch (ServiceException e) {
                Main.showAlert("Error", "Service exception.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else {
            Main.showAlert("Error", "Invalid input.", "Invalid input in the size field.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onCancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
