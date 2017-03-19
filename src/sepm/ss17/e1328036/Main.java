package sepm.ss17.e1328036;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sepm.ss17.e1328036.dao.BoxDAO;
import sepm.ss17.e1328036.dao.impl.BoxDAOImpl;
import sepm.ss17.e1328036.entities.Box;
import sepm.ss17.e1328036.util.DatabaseUtil;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        DatabaseUtil.openConnection();
        DatabaseUtil.closeConnection();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
