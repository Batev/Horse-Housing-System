package sepm.ss17.e1328036.dao.test;

import org.junit.Assert;
import org.junit.Test;
import sepm.ss17.e1328036.dao.BoxDAO;

/**
 * Created by evgen on 20.03.2017.
 */
public abstract class AbstractBoxDAOTest {

    protected BoxDAO boxDAO;

    @Test(expected = NullPointerException.class)
    public void saveNullThrowsException() {
        boxDAO.save(null);
    }

    @Test(expected = NullPointerException.class)
    public void searchInvalidBidThrowsException() {
        boxDAO.search(-1);
    }

}
