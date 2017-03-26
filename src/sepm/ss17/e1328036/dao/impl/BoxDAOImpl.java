package sepm.ss17.e1328036.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1328036.dao.BoxDAO;
import sepm.ss17.e1328036.dao.DAOException;
import sepm.ss17.e1328036.dao.test.BoxDAOImplTest;
import sepm.ss17.e1328036.domain.Box;
import sepm.ss17.e1328036.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by evgen on 18.03.2017.
*/
public class BoxDAOImpl implements BoxDAO{

    private final String saveStatement = "INSERT INTO Boxes (bid, size, sawdust_amount, straw_amaount, has_window, price, image, is_deleted) VALUES (DEFAULT,?,?,?,?,?,?,DEFAULT)";
    private final String deleteStatement = "UPDATE Boxes SET is_deleted = true WHERE bid = ?";
    private final String updatePriceStatement = "UPDATE Boxes b SET b.price = ? WHERE b.bid = ? AND b.is_deleted = false";
    private final String updateSizeStatement = "UPDATE Boxes b SET b.size = ? WHERE b.bid = ? AND b.is_deleted = false";
    private final String selectAllStatement = "SELECT * FROM Boxes";
    private final String selectByPriceStatement = "SELECT * FROM Boxes b WHERE b.price BETWEEN ? AND ?";
    private final String selectBySizeStatement = "SELECT * FROM Boxes b WHERE b.size BETWEEN ? AND ?";
    private final String selectByIdStatement = "SELECT * FROM Boxes b WHERE b.bid = ?";

    private static final Logger logger = LoggerFactory.getLogger(BoxDAOImplTest.class);

    @Override
    public void save(Box box) throws DAOException {

        logger.info("Saving new box in the database:" + box);
        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(saveStatement);

            preparedStatement.setFloat(1, box.getSize());
            preparedStatement.setInt(2, box.getSawdust());
            preparedStatement.setInt(3, box.getStraw());
            preparedStatement.setBoolean(4, box.getHasWindow());
            preparedStatement.setFloat(5, box.getPrice());
            preparedStatement.setString(6, box.getImage());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            logger.error("Save: " + e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void delete(int bid) throws DAOException {
        logger.info("Deleting box from the database:" + bid);
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(deleteStatement);
            preparedStatement.setInt(1, bid);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new DAOException();
        }
    }

    @Override
    public void updatePrice(int bid, float newPrice) throws DAOException {
        logger.info("Updating box price: " + bid);
        update(false, bid, newPrice);
    }

    @Override
    public void updateSize(int bid, float newSize) throws DAOException {
        logger.info("Updating box size: " + bid);
        update(true, bid, newSize);
    }

    private void update(boolean isSize, int bid, float newValue) throws DAOException {
        PreparedStatement preparedStatement = null;

        if(!ReservationDAOImpl.checkActive(bid, true)) {
            try {
                preparedStatement = DatabaseUtil.getConnection().prepareStatement(isSize ? updateSizeStatement : updatePriceStatement);
                preparedStatement.setFloat(1, newValue);
                preparedStatement.setInt(2, bid);

                preparedStatement.executeUpdate();
                preparedStatement.close();

            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        else {

            throw new DAOException("The box that you are trying to update is currently reserved and cannot be updated.");
        }
    }

    @Override
    public List<Box> getAll() throws DAOException {
        return get(false, 0f, 0f, false, false);
    }

    @Override
    public List<Box> getBySize(float sizeFrom, float sizeTo) throws DAOException {
        return get(true, sizeFrom, sizeTo, true, false);
    }

    @Override
    public List<Box> getById(int bid) throws DAOException {
        return get(true, bid, 0f, false, true);
    }

    @Override
    public List<Box> getByPrice(float priceFrom, float priceTo) throws DAOException {
        return get(true, priceFrom, priceTo, false, false);
    }

    @Override
    public List<Box> getAllWithDeleted() throws DAOException {
        logger.info("Getting all boxes + deleted.");
        List<Box> boxList = new LinkedList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(selectAllStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                boxList.add(new Box(resultSet.getInt(1), resultSet.getFloat(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getBoolean(5), resultSet.getFloat(6), resultSet.getString(7), resultSet.getBoolean(8)));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new DAOException("");
        }
        return boxList;
    }

    private List<Box> get(boolean isFiltered, float from, float to, boolean isSize, boolean isId) throws DAOException {
        logger.info("Getting boxes either filtered or not.");
        List<Box> boxList = new LinkedList<>();

        try {

            PreparedStatement preparedStatement = null;
            String statement = null;

            if (isFiltered) {
                if (!isId) {
                    statement = isSize ? selectBySizeStatement : selectByPriceStatement;
                    preparedStatement = DatabaseUtil.getConnection().prepareStatement(statement);
                    preparedStatement.setFloat(1, from);
                    preparedStatement.setFloat(2, to);
                }
                else {
                    statement = selectByIdStatement;
                    preparedStatement = DatabaseUtil.getConnection().prepareStatement(statement);
                    preparedStatement.setInt(1, (int)from);
                }
            }
            else {
                statement = selectAllStatement;
                preparedStatement = DatabaseUtil.getConnection().prepareStatement(statement);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (!resultSet.getBoolean("is_deleted")) {
                    boxList.add(new Box(resultSet.getInt(1), resultSet.getFloat(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getBoolean(5), resultSet.getFloat(6), resultSet.getString(7), resultSet.getBoolean(8)));
                }
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return boxList;
    }
}