package sepm.ss17.e1328036.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by evgen on 18.03.2017.
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
        }
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
