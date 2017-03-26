package sepm.ss17.e1328036.dao;

import sepm.ss17.e1328036.domain.Box;

import java.util.List;

/**
 * @author Evgeni Batev
 * Provides access to the box table in the database with some functionality.
 */
public interface BoxDAO {

    /**
     * Adds a new box to the database.
     * @param box The new box to be saved.
     * @throws DAOException On error throws exception.
     */
    void save(Box box) throws DAOException;

    /**
     * Deletes a box from the database.
     * @param bid The Id of the box to be deleted.
     * @throws DAOException On error throws exception.
     */
    void delete(int bid) throws DAOException;

    /**
     * Updates the price of a box.
     * @param bid The Id of the box to be updated.
     * @param newPrice The new price of the box.
     * @throws DAOException On error throws exception.
     */
    void updatePrice(int bid, float newPrice) throws DAOException;

    /**
     * Updates the size of the box.
     * @param bid The Id of the box to be updated.
     * @param newSize The new size of the box.
     * @throws DAOException On error throws exception.
     */
    void updateSize(int bid, float newSize) throws DAOException;

    /**
     * Gets all the boxes.
     * @return Returns list with all boxes.
     * @throws DAOException On error throws exception.
     */
    List<Box> getAll() throws DAOException;

    /**
     * Gets all boxes and deleted too.
     * @return Returns list with all boxes.
     * @throws DAOException On error throws exception.
     */
    List<Box> getAllWithDeleted() throws DAOException;

    /**
     * Gets boxes in price interval.
     * @param priceFrom Smallest price.
     * @param priceTo Biggest price.
     * @return List of all boxes that match the search.
     * @throws DAOException On error throws exception.
     */
    List<Box> getByPrice(float priceFrom, float priceTo) throws DAOException;

    /**
     * Gets boxes in price interval.
     * @param sizeFrom Smallest price.
     * @param sizeTo Biggest price.
     * @return List of all boxes that match the search.
     * @throws DAOException On error throws exception.
     */
    List<Box> getBySize(float sizeFrom, float sizeTo) throws DAOException;

    /**
     * Gets boxes with exact id.
     * @param bid Id of the box.
     * @return List with the boxes.
     * @throws DAOException On error throws exception.
     */
    List<Box> getById(int bid) throws DAOException;
}
