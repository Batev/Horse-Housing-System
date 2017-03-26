package sepm.ss17.e1328036.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Evgeni Batev
 * Controller for the reservation add interface.
 */
public class AddReservationSubmitViewController implements Initializable {

    @FXML
    private Label idLabel;

    @FXML
    private TextField horseNameField;

    @FXML
    private DatePicker dateFrom, dateTo;

    @FXML
    private Button cancelButton, okButton;

    static boolean cancellationRequested;

    static String horseName;

    static Date from,to;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancellationRequested = false;
        idLabel.setText(AddReservationViewController.currentId + "");
    }

    @FXML
    public void onOkClicked() {
        horseName = horseNameField.getText();
        LocalDate date1 = dateFrom.getValue();
        LocalDate date2 = dateTo.getValue();

        if (horseName.isEmpty()) {
            Main.showAlert("Error", "Invalid input.", "Horse name field must not be empty.", Alert.AlertType.ERROR);
        }
        else {
            if (date1 != null && date2 != null) {
                from = Date.valueOf(date1);
                to = Date.valueOf(date2);
            } else {
                Main.showAlert("Error", "Invalid input.", "Please select both Date from and Date to.", Alert.AlertType.ERROR);
            }
        }

        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onCancelClicked() {
        cancellationRequested = true;
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
