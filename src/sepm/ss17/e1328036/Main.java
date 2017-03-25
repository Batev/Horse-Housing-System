package sepm.ss17.e1328036;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sepm.ss17.e1328036.dao.BoxDAO;
import sepm.ss17.e1328036.dao.ReservationDAO;

import sepm.ss17.e1328036.dao.impl.ReservationDAOImpl;
import sepm.ss17.e1328036.dto.Reservation;
import sepm.ss17.e1328036.util.DatabaseUtil;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ui/sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 600, 360);
        primaryStage.setScene(scene);
        primaryStage.show();

        DatabaseUtil.openConnection();



        DatabaseUtil.closeConnection();

    }

    private static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, (month-1));
        calendar.set(Calendar.DAY_OF_MONTH, day);
        java.util.Date dateUtil = calendar.getTime();

        return new Date(dateUtil.getTime());
    }
    public static void main(String[] args) {
        launch(args);
    }
}