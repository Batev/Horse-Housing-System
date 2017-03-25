package sepm.ss17.e1328036.dao.impl;

import sepm.ss17.e1328036.dao.BoxDAO;
import sepm.ss17.e1328036.dao.DAOException;
import sepm.ss17.e1328036.dto.Box;
import sepm.ss17.e1328036.util.DatabaseUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
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
    private final String searchStatement = "SELECT * FROM Boxes b WHERE b.bid = ?";
    private final String deleteStatement = "UPDATE Boxes SET is_deleted = true WHERE bid = ?";
    private final String updatePriceStatement = "UPDATE Boxes SET price = ? WHERE bid = ? AND is_deleted = false";
    private final String selectAllStatement = "SELECT * FROM Boxes";
    private final String selectByPriceStatement = "SELECT * FROM Boxes b WHERE b.price BETWEEN ? AND ?";

    @Override
    public void save(Box box) throws DAOException {
        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(saveStatement);

            preparedStatement.setFloat(1, box.getSize());
            preparedStatement.setInt(2, box.getSawdust());
            preparedStatement.setInt(3, box.getStraw());
            preparedStatement.setBoolean(4, box.hasWindow());
            preparedStatement.setFloat(5, box.getPrice());
            preparedStatement.setString(6, box.getImage());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public Box search(int bid) throws DAOException {

        Box box = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(searchStatement);
            preparedStatement.setInt(1, bid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (!resultSet.getBoolean(8)) {
                    box = new Box(resultSet.getInt(1), resultSet.getFloat(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getBoolean(5), resultSet.getFloat(6), resultSet.getString(7), resultSet.getBoolean(8));
                }
            }
            else {
                throw new DAOException("No box with bid: " + bid);
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return box;
    }

    @Override
    public void delete(int bid) throws DAOException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(deleteStatement);
            preparedStatement.setInt(1, bid);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePrice(int bid, float newPrice) throws DAOException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(updatePriceStatement);
            preparedStatement.setFloat(1, newPrice);
            preparedStatement.setInt(2, bid);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<Box> getAll() throws DAOException {
        return get(false, 0f, 0f);
    }

    @Override
    public List<Box> getByDate(float priceFrom, Float priceTo) throws DAOException {
        return get(true, priceFrom, priceTo);
    }

    private List<Box> get(boolean isFilteredByDate, float priceFrom, Float priceTo) throws DAOException {
        List<Box> boxList = new LinkedList<>();

        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(isFilteredByDate ? selectByPriceStatement : selectAllStatement);

            if(isFilteredByDate) {
                preparedStatement.setFloat(1, priceFrom);
                preparedStatement.setFloat(2, priceTo);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                boxList.add(new Box(resultSet.getInt(1), resultSet.getFloat(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getBoolean(5), resultSet.getFloat(6), resultSet.getString(7), resultSet.getBoolean(8)));
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return boxList.size() == 0 ? null : boxList;
    }
}