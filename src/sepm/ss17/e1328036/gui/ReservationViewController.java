package sepm.ss17.e1328036.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Created by evgen on 26.03.2017.
 */
public class ReservationViewController implements Initializable {

    private Service service;

    @FXML
    private TableView<Invoice> reservationTable;

    @FXML
    private TableColumn<Invoice, Integer> rid, boxId;

    @FXML
    private TableColumn<Invoice, String> clientName, horseName;

    @FXML
    private TableColumn<Invoice, Date> from, to;

    @FXML
    private RadioButton idRadio, dateRadio;

    @FXML
    private TextField idField;

    @FXML
    private DatePicker dateFrom, dateTo;

    ToggleGroup toggleGroup;

    String currentRadioButtonValue;

    private static int count = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new SimpleService();
        reservationTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        initializeButtons();

        rid.setCellValueFactory(new PropertyValueFactory<Invoice, Integer>("rid"));
        boxId.setCellValueFactory(new PropertyValueFactory<Invoice, Integer>("bid"));
        clientName.setCellValueFactory(new PropertyValueFactory<Invoice, String>("clientName"));
        horseName.setCellValueFactory(new PropertyValueFactory<Invoice, String>("horseName"));
        from.setCellValueFactory(new PropertyValueFactory<Invoice, Date>("dateFrom"));
        to.setCellValueFactory(new PropertyValueFactory<Invoice, Date>("dateTo"));

        showAll();
    }

    @FXML
    public void onSearchClicked() {

        switch (currentRadioButtonValue) {
            case "dateRadio":
                searchByDate();
                break;
            case "idRadio":
                searchById();
                break;
        }
    }

    @FXML
    public void onShowAllClicked() {
        showAll();
    }

    @FXML
    public void onAddClicked() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddReservationView.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 400, 300);
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

        showAll();
    }

    @FXML
    public void onDeleteClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete...");
        alert.setHeaderText("Delete selected boxes.");
        alert.setContentText("Are you sure, that you want to delete the selected boxes?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ObservableList<Invoice> invoices = reservationTable.getSelectionModel().getSelectedItems();

            for (Invoice invoice :
                    invoices) {
                try {
                    service.deleteReservation(invoice);
                } catch (ServiceException e) {
                    Main.showAlert("Error", "Problem on delete.", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            showAll();
        }
    }

    @FXML
    public void onGetHorseClicked() {
        ObservableList<Invoice> invoices = reservationTable.getSelectionModel().getSelectedItems();
        Invoice invoice;
        if(invoices.size() > 1) {
            Main.showAlert("Error", "Select problem.", "You have selected too many boxes. You must select exactly one box.", Alert.AlertType.ERROR);
        }
        else if(invoices.isEmpty()) {
            Main.showAlert("Error", "Select problem.", "You have selected too little boxes. You must select exactly one box.", Alert.AlertType.ERROR);
        }
        else {
            LocalDate localDate = LocalDate.now();
            Date newDate = Date.valueOf(localDate);
            invoice = invoices.get(0);
            try {
                service.updateReservationEndDate(invoice.getBid(), invoice.getDateFrom(), invoice.getDateTo(), newDate);
            } catch (ServiceException e) {
                Main.showAlert("Error", "Problem while updating the end date.", e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
            showAll();
            generateInvoice(reservationTable.getSelectionModel().getSelectedItems());
        }
    }

    private void generateInvoice(ObservableList<Invoice> selectedItems) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invoice " + count++);
        alert.setHeaderText("Information about the selected reservations: ");
        String msg = "";
        List<Box> boxList = new LinkedList<>();
        float priceAll = 0f;

        try {
            boxList = service.getAllBoxesWithDeleted();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        for (Invoice invoice:
             selectedItems) {
            float price = 0f;
            for (Box box:
                 boxList) {
                if (box.getBid() == invoice.getBid()) {
                    price = box.getPrice();
                }
            }

            long days = getDifferenceDays(invoice.getDateFrom(), invoice.getDateTo());
            msg += "Box with ID: " + invoice.getBid() + " was reserved by " + invoice.getClientName() + " for the horse " + invoice.getHorseName() + " for " + days + " days.\n";
            msg += "Total cost: " + days*price + "€.\n\n";
            priceAll += days*price;
        }

        msg += "Total cost for all reservations: " + priceAll;

        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void onInvoiceClicked() {
        generateInvoice(reservationTable.getSelectionModel().getSelectedItems());
    }

    private long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private void searchById() {
        String id = idField.getText();

        if (Main.isInteger(id)) {
            try {
                showList(service.getReservationsById(Main.getInteger(id)));
            } catch (ServiceException e) {
                Main.showAlert("Error", "Invalid input.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else {
            Main.showAlert("Error", "Invalid input.", "Invalid input in the id search field.", Alert.AlertType.ERROR);
        }
    }

    private void searchByDate() {

        LocalDate date1 = dateFrom.getValue();
        LocalDate date2 = dateTo.getValue();

        if (date1 != null && date2 != null) {
            Date dateF = Date.valueOf(date1);
            Date dateT = Date.valueOf(date2);

            try {
                showList(service.getReservationsByDate(dateF, dateT));
            } catch (ServiceException e) {
                Main.showAlert("Error", "Invalid input.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else {
            Main.showAlert("Error", "Invalid input.", "Please select both Date from and Date to.", Alert.AlertType.ERROR);
        }

    }

    private void showAll() {
        List<Invoice> invoices = new LinkedList<>();
        try {
            service.getAllReservations().forEach(elem -> invoices.addAll(elem.getInvoices()));
            reservationTable.getItems().setAll(invoices);
        } catch (ServiceException e) {
            Main.showAlert("Error", "Invalid input.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showList(List<Reservation> reservations) {
        List<Invoice> invoices = new LinkedList<>();
        reservations.forEach(elem -> invoices.addAll(elem.getInvoices()));
        reservationTable.getItems().setAll(invoices);
    }

    private void initializeButtons() {
        toggleGroup = new ToggleGroup();
        dateRadio.setToggleGroup(toggleGroup);
        idRadio.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {

                RadioButton button = (RadioButton)t1.getToggleGroup().getSelectedToggle();
                currentRadioButtonValue = button.getId();

            }
        });
    }
}
