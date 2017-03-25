package sepm.ss17.e1328036.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sepm.ss17.e1328036.dto.Box;
import sepm.ss17.e1328036.service.Service;
import sepm.ss17.e1328036.service.SimpleService;


import java.util.List;
import java.util.Observable;

/**
 * Created by evgen on 23.03.2017.
 */
public class BoxViewController {

    private Service service = new SimpleService();

    @FXML
    private TableView<Box> boxTable;

    @FXML
    private TableColumn<Box, Float> size;

    public BoxViewController() {

    }

    public void initialize() {
        size.setCellValueFactory(new PropertyValueFactory<Box, Float>("size"));
    }

    public void fillTableWithBoxes() {
    }

}
