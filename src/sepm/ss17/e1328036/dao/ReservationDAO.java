package sepm.ss17.e1328036.dao;

import sepm.ss17.e1328036.domain.Invoice;
import sepm.ss17.e1328036.domain.Reservation;

import java.sql.Date;
import java.util.List;

/**
 * @author Evgeni Batev
 * Provides access to the reservation table in the database with some functionality.
 */
public interface ReservationDAO {

    /**
     * Adds a new reservation.
     * @param reservation The new reservation.
     * @throws DAOException On error throws exception.
     */
    void create(Reservation reservation) throws DAOException;

    /**
     * Deletes a reservation from the database.
     * @param reservation The reservation to be deleted.
     * @throws DAOException On error throws exception.
     */
    void delete(Invoice reservation) throws DAOException;

    /**
     * Gets all reservations.
     * @return List with all reservations.
     * @throws DAOException On error throws exception.
     */
    List<Reservation> getAll() throws DAOException;

    /**
     * Gets reservations by id.
     * @param rid The id of the reservations.
     * @return List of the reservations matching the search.
     * @throws DAOException On error throws exception.
     */
    List<Reservation> getById(int rid) throws DAOException;

    /**
     * Gets reservations by date.
     * @param startDate Starting date.
     * @param endDate Ending date.
     * @return List of the reservations matching the search.
     * @throws DAOException On error throws exception.
     */
    List<Reservation> getByDate(Date startDate, Date endDate) throws DAOException;

    /**
     * Updates the end date of a reservation.
     * @param bid The Id of the reservation.
     * @param dateFrom The starting date of the reservation.
     * @param dateTo The end date of the reservation.
     * @param newEndDate The new end date of the reservation.
     */
    void updateEndDate(int bid, Date dateFrom, Date dateTo, Date newEndDate);

    /**
     * Gets the next id for a reservation.
     * @return The id.
     * @throws DAOException On error throws exception.
     */
    int getNextId() throws  DAOException;
}
