package sepm.ss17.e1328036.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sepm.ss17.e1328036.domain.Box;
import sepm.ss17.e1328036.domain.Invoice;
import sepm.ss17.e1328036.domain.Reservation;
import sepm.ss17.e1328036.service.Service;
import sepm.ss17.e1328036.service.ServiceException;
import sepm.ss17.e1328036.service.SimpleService;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Evgeni Batev
 * Controller for the reservation add interface.
 */
public class AddReservationViewController implements Initializable {

    @FXML
    private ListView<Integer> boxListView;

    @FXML
    private TextField clientNameField;

    @FXML
    private Button cancelButton;

    private Service service;

    static Integer currentId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new SimpleService();
        List<Integer> bids = new LinkedList<>();
        currentId = 0;
        try {
            List<Box> list = service.getAllBoxes();
            list.forEach(elem -> bids.add(elem.getBid()));
        } catch (ServiceException e) {
            Main.showAlert("Error", "Problem while generating the available boxes.", e.getMessage(), Alert.AlertType.ERROR);
        }

        boxListView.getItems().addAll(bids);
        boxListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void onSubmitClicked() {
        String clientNameText = clientNameField.getText();
        List<Invoice> invoices = new LinkedList<>();
        Reservation reservation = null;
        try {
            reservation = new Reservation(service.getNextId(),false,null);
        } catch (ServiceException e) {
            Main.showAlert("Error", "Invalid id.", e.getMessage(), Alert.AlertType.ERROR);
        }

        ObservableList<Integer> selectedIds;
        selectedIds = boxListView.getSelectionModel().getSelectedItems();

        if (clientNameText.isEmpty()) {
            Main.showAlert("Error", "Invalid client name input.", "Please enter a client name.", Alert.AlertType.ERROR);
        }
        else if (selectedIds.isEmpty()) {
            Main.showAlert("Error", "Invalid number of boxes.", "Please select at least one box that to reserve.", Alert.AlertType.ERROR);
        }
        else {
            for (Integer id :
                    selectedIds) {
                currentId = id;
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("AddReservationSubmitView.fxml"));
                Scene scene = null;

                try {
                    scene = new Scene(fxmlLoader.load(), 400, 350);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setResizable(false);
                stage.alwaysOnTopProperty();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Add reservation...");
                stage.setScene(scene);
                stage.showAndWait();

                if (AddReservationSubmitViewController.cancellationRequested) {
                    onCancelClicked();
                    break;
                }

                invoices.add(new Invoice(0, id, reservation.getRid(), clientNameText, AddReservationSubmitViewController.horseName, AddReservationSubmitViewController.from, AddReservationSubmitViewController.to));
            }

            reservation.setInvoices(invoices);

            try {
                service.addReservation(reservation);
            } catch (ServiceException e) {
                Main.showAlert("Error", "Save reservation error.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void onCancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
