package sepm.ss17.e1328036.dao.test;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory;

import sepm.ss17.e1328036.util.DatabaseUtil;
import java.sql.SQLException;


/**
 * @author Evgeni Batev
 * Tests for the DAOs.
 */
public class BoxDAOImplTest extends AbstractBoxDAOTest {

    private static final Logger logger = LoggerFactory.getLogger(BoxDAOImplTest.class);

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
