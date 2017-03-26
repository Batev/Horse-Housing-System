package sepm.ss17.e1328036.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import sepm.ss17.e1328036.util.DatabaseUtil;

import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // establish connection to the database.
        DatabaseUtil.openConnection();

        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Wendy");
        Scene scene = new Scene(root, 610, 530);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // close connection to database
        primaryStage.setOnCloseRequest(e -> DatabaseUtil.closeConnection());
    }

    private static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, (month-1));
        calendar.set(Calendar.DAY_OF_MONTH, day);
        java.util.Date dateUtil = calendar.getTime();

        return new Date(dateUtil.getTime());
    }

    public static Optional showAlert(String title, String header, String content, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }

    public static int getInteger(String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return i;
        } catch(NullPointerException e) {
            return i;
        }

        return i;
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }

    public static float getFloat(String s) {
        float f = 0f;
        try {
            f = Float.parseFloat(s);
        } catch(NumberFormatException e) {
            return f;
        } catch(NullPointerException e) {
            return f;
        }

        return f;
    }

    public static void main(String[] args) {
        launch(args);
    }
}