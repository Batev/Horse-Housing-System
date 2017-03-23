package sepm.ss17.e1328036.dao.impl;

import sepm.ss17.e1328036.dao.BoxDAO;
import sepm.ss17.e1328036.dto.Box;
import sepm.ss17.e1328036.util.DatabaseUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by evgen on 18.03.2017.
 */
public class BoxDAOImpl implements BoxDAO{

    private final String saveStatement = "INSERT INTO Boxes (bid, size, sawdust_amount, straw_amaount, has_window, price, is_deleted) VALUES (DEFAULT,?,?,?,?,?, DEFAULT)";
    private final String searchStatement = "SELECT * FROM Boxes b WHERE b.bid = ?";
    private final String deleteStatement = "UPDATE Boxes SET is_deleted = true WHERE bid = ?";
    private final String updatePriceStatement = "UPDATE Boxes SET price = ? WHERE bid = ? AND is_deleted = false";
    private final String saveImages = "INSERT INTO Images (iid, bid, image) VALUES (DEFAULT,?,?)";
    private final String getImages = "SELECT image FROM images i WHERE i.bid = ?";

    @Override
    public void save(Box box) {
        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(saveStatement);

            preparedStatement.setFloat(1, box.getSize());
            preparedStatement.setInt(2, box.getSawdust());
            preparedStatement.setInt(3, box.getStraw());
            preparedStatement.setBoolean(4, box.hasWindow());
            preparedStatement.setFloat(5, box.getPrice());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Box search(int bid) {
        Box box = null;

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(searchStatement);
            preparedStatement.setInt(1, bid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (!resultSet.getBoolean(7)) {
                    box = new Box(resultSet.getFloat(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getBoolean(5), resultSet.getFloat(6));
                }
            }
            else {
                throw new IllegalArgumentException("No box with bid: " + bid);
            }

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return box;
    }

    @Override
    public void delete(int bid) {
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
    public void updatePrice(int bid, float newPrice) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(updatePriceStatement);
            preparedStatement.setFloat(1, newPrice);
            preparedStatement.setInt(2, bid);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addImages(int bid, List<FileInputStream> images) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(saveImages);

            for (FileInputStream image:
                 images) {
                preparedStatement.setInt(1, bid);
                preparedStatement.setBinaryStream(2, image);
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<InputStream> getImages(int bid) {
        List<InputStream> images = new LinkedList<>();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(getImages);
            preparedStatement.setInt(1,bid);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                images.add(resultSet.getBinaryStream(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return images.size() == 0 ? null : images;
    }
}
