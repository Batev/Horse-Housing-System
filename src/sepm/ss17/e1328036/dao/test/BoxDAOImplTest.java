package sepm.ss17.e1328036.dao.test;

import org.junit.After;
import org.junit.Before;
import sepm.ss17.e1328036.dao.BoxDAO;
import sepm.ss17.e1328036.util.DatabaseUtil;
import java.sql.SQLException;


/**
 * Created by evgen on 20.03.2017.
 */
public class BoxDAOImplTest extends AbstractBoxDAOTest {

    @Before
    public void setUp() throws SQLException {
        DatabaseUtil.openConnection();
        DatabaseUtil.getConnection().setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseUtil.getConnection().rollback();
        DatabaseUtil.closeConnection();
    }
}
