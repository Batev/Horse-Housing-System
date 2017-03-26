package sepm.ss17.e1328036.util;

import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Evgeni Batev
 * Class for initializing the database connection.
 */
public class DatabaseUtil {

    private static Connection con = null;

    public static Connection getConnection(){
        return con;
    }

    public static void openConnection() {
        if (con == null) {
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                con = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //fillDatabase();
        }
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void fillDatabase() {
        try {
            RunScript.execute(con, new FileReader("E:\\G3no\\TU Wien\\Software Engineering und Projektmanagement\\Einzelbeispiel\\Wendy\\src\\sepm\\ss17\\e1328036\\util\\Create.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
