package sepm.ss17.e1328036.gui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sepm.ss17.e1328036.domain.Box;
import sepm.ss17.e1328036.service.Service;
import sepm.ss17.e1328036.service.ServiceException;
import sepm.ss17.e1328036.service.SimpleService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by evgen on 23.03.2017.
 */
public class BoxViewController implements Initializable {

    private Service service;

    @FXML
    private TableView<Box> boxTable;

    @FXML
    private TableColumn<Box, Integer> bid;
    @FXML
    private TableColumn<Box, Float> size;
    @FXML
    private TableColumn<Box, Integer> sawdust;
    @FXML
    private TableColumn<Box, Integer> straw;
    @FXML
    private TableColumn<Box, Boolean> windows;
    @FXML
    private TableColumn<Box, Float> price;
    @FXML
    private TableColumn<Box, ImageView> picture;
    @FXML
    private RadioButton idButton;
    @FXML
    private RadioButton priceButton;
    @FXML
    private RadioButton sizeButton;

    @FXML
    private TextField idValueField, priceFromField, priceToField, sizeFromField, sizeToField;

    private ToggleGroup toggleGroup;

    private String currentRadioButtonValue;

    static Box currentBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentBox = null;
        this.initializeService();
        this.initializeButtons();

        bid.setCellValueFactory(new PropertyValueFactory<Box, Integer>("bid"));
        size.setCellValueFactory(new PropertyValueFactory<Box, Float>("size"));
        sawdust.setCellValueFactory(new PropertyValueFactory<Box, Integer>("sawdust"));
        straw.setCellValueFactory(new PropertyValueFactory<Box, Integer>("straw"));
        windows.setCellValueFactory(new PropertyValueFactory<Box, Boolean>("hasWindow"));
        price.setCellValueFactory(new PropertyValueFactory<Box, Float>("price"));
        picture.setCellValueFactory(e -> new SimpleObjectProperty<>(new ImageView(new Image(e.getValue().getImage(), 70, 70, false ,true))));
        picture.setPrefWidth(75);

        showAll();

    }

    private void initializeService() {
        service = new SimpleService();
    }

    private void initializeButtons() {
        this.toggleGroup = new ToggleGroup();
        idButton.setToggleGroup(toggleGroup);
        priceButton.setToggleGroup(toggleGroup);
        sizeButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {

                RadioButton button = (RadioButton)t1.getToggleGroup().getSelectedToggle();
                currentRadioButtonValue = button.getId();

            }
        });
    }

    @FXML
    public void onSearchClicked() {
        switch (currentRadioButtonValue) {
            case "priceButton":
                executeFloatSearch(false);
                break;
            case "idButton":
                executeIdSearch();
                break;
            case "sizeButton":
                executeFloatSearch(true);
                break;
        }
    }

    @FXML
    public void onUpdateClicked() {
        setCurrentBox();

        if (currentBox != null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("UpdateBoxView.fxml"));
            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load(), 300, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.alwaysOnTopProperty();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update");
            stage.setScene(scene);
            stage.showAndWait();

            showAll();
        }
        else {
            Main.showAlert("Error", "No row selected.", "You must select a row in order to edit it.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onAddClicked() {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddBoxView.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 400, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.alwaysOnTopProperty();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add box...");
        stage.setScene(scene);
        stage.showAndWait();

        showAll();
    }

    @FXML
    public void onDeleteClicked() {
        setCurrentBox();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete...");
        alert.setHeaderText("Delete selected box.");
        alert.setContentText("Are you sure, that you want to delete a box with id: " + currentBox.getBid() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            if (currentBox != null) {

                try {
                    service.deleteBox(currentBox.getBid());
                } catch (ServiceException e) {
                    Main.showAlert("Error", "Problem while deleting box with id: " + currentBox.getBid(), e.getMessage(), Alert.AlertType.ERROR);
                }
                showAll();
            } else {
                Main.showAlert("Error", "No row selected.", "You must select a row in order to delete it.", Alert.AlertType.ERROR);
            }
        }
    }

    public void onShowAllClicked() {
        showAll();
    }

    private void showAll() {
        try {
            boxTable.getItems().setAll(service.getAllBoxes());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentBox() {
        currentBox = boxTable.getSelectionModel().getSelectedItem();
    }

    private void executeFloatSearch(boolean isSize) {
        String text1;
        String text2;

        if (!isSize) {
            text1 = priceFromField.getText();
            text2 = priceToField.getText();
        }
        else {
            text1 = sizeFromField.getText();
            text2 = sizeToField.getText();
        }

        if (Main.isFloat(text1) && Main.isFloat(text2)) {
            try {
                List<Box> boxList = isSize ? service.getBoxesBySize(Main.getFloat(text1), Main.getFloat(text2))
                                        : service.getBoxesByPrice(Main.getFloat(text1), Main.getFloat(text2));

                boxTable.getItems().setAll(boxList);
            }
            catch (ServiceException e) {
                Main.showAlert("Error", "Service exception.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else {
            String temp = isSize ? "size" : "price";
            Main.showAlert("Error", "Invalid input.", "Invalid input in the " + temp + " search field.", Alert.AlertType.ERROR);
        }
    }

    private void executeIdSearch() {
        String text1 = idValueField.getText();

        if (Main.isInteger(text1)) {
            List<Box> boxList = null;
            try {
                boxList = service.getBoxesById(Main.getInteger(text1));
            } catch (ServiceException e) {
                Main.showAlert("Error", "Service exception.", e.getMessage(), Alert.AlertType.ERROR);
            }
            boxTable.getItems().setAll(boxList);
        }
        else {
            Main.showAlert("Error", "Invalid input.", "Invalid input in the box id search field.", Alert.AlertType.ERROR);
        }
    }
}
