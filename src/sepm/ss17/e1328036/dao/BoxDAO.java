package sepm.ss17.e1328036.dao;

import sepm.ss17.e1328036.domain.Box;

import java.util.List;

/**
 * Created by evgen on 18.03.2017.
 */
public interface BoxDAO {

    void save(Box box) throws DAOException;
    void delete(int bid) throws DAOException;
    void updatePrice(int bid, float newPrice) throws DAOException;
    void updateSize(int bid, float newSize) throws DAOException;
    List<Box> getAll() throws DAOException;
    List<Box> getAllWithDeleted() throws DAOException;
    List<Box> getByPrice(float priceFrom, float priceTo) throws DAOException;
    List<Box> getBySize(float sizeFrom, float sizeTo) throws DAOException;
    List<Box> getById(int bid) throws DAOException;
}
