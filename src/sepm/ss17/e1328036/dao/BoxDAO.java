package sepm.ss17.e1328036.dao;

import sepm.ss17.e1328036.dto.Box;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by evgen on 18.03.2017.
 */
public interface BoxDAO {

    void save(Box box) throws DAOException;
    Box search(int bid) throws DAOException;
    void delete(int bid) throws DAOException;
    void updatePrice(int bid, float newPrice) throws DAOException;
    List<Box> getAll() throws DAOException;
    List<Box> getByDate(float priceFrom, Float priceTo) throws DAOException;
}
