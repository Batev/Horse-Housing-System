package sepm.ss17.e1328036.service;

import sepm.ss17.e1328036.domain.Box;
import sepm.ss17.e1328036.domain.Invoice;
import sepm.ss17.e1328036.domain.Reservation;

import java.sql.Date;
import java.util.List;

/**
 * @author Evgeni Batev
 * Service for the business logic.
 */
public interface Service {

    /**
     * Gets all boxes.
     * @return List of all boxes.
     * @throws ServiceException On error throws exception.
     */
    List<Box> getAllBoxes() throws ServiceException;

    /**
     * Gets all boxes with deleted.
     * @return List of all boxes with deleted.
     * @throws ServiceException On error throws exception.
     */
    List<Box> getAllBoxesWithDeleted() throws ServiceException;

    /**
     * Gets boxes filtered by price.
     * @param priceFrom Min price value.
     * @param priceTo Max price value.
     * @return List of boxes matching the search.
     * @throws ServiceException On error throws exception.
     */
    List<Box> getBoxesByPrice(float priceFrom, float priceTo) throws ServiceException;

    /**
     * Gets boxes filtered by size.
     * @param sizeFrom Min size value.
     * @param sizeTo Max size value.
     * @return List of boxes matching the search.
     * @throws ServiceException On error throws exception.
     */
    List<Box> getBoxesBySize(float sizeFrom, float sizeTo) throws ServiceException;

    /**
     * Gets boxes filtered by id.
     * @param bid The id of the box.
     * @return List of boxes matching the search.
     * @throws ServiceException On error throws exception.
     */
    List<Box> getBoxesById(int bid) throws ServiceException;

    /**
     * Updates box size.
     * @param bid Box Id.
     * @param newSize The new size value.
     * @throws ServiceException On error throws exception.
     */
    void updateBoxSize(int bid, float newSize) throws ServiceException;

    /**
     * Updates box price.
     * @param bid Box Id.
     * @param newPrice The new price value.
     * @throws ServiceException On error throws exception.
     */
    void updateBoxPrice(int bid, float newPrice) throws ServiceException;

    /**
     * Deletes a box.
     * @param bid Box to be deleted by id.
     * @throws ServiceException On error throws exception.
     */
    void deleteBox(int bid) throws ServiceException;

    /**
     * Adds box.
     * @param box The box to be added.
     * @throws ServiceException On error throws exception.
     */
    void addBox(Box box) throws ServiceException;

    /**
     * Gets all reservations from the database.
     * @return List of reservations matching the search.
     * @throws ServiceException On error throws exception.
     */
    List<Reservation> getAllReservations() throws ServiceException;

    /**
     * Gets reservations filtered by Id.
     * @param rid The Id of the reservation.
     * @return List of reservations matching the search.
     * @throws ServiceException On error throws exception.
     */
    List<Reservation> getReservationsById(int rid) throws ServiceException;

    /**
     * Gets reservations filtered by date.
     * @param from date from
     * @param to date to
     * @return List of reservations matching the search.
     * @throws ServiceException On error throws exception.
     */
    List<Reservation> getReservationsByDate(Date from, Date to) throws ServiceException;

    /**
     * Adds a new reservation and saves it.
     * @param reservation The new reservation
     * @throws ServiceException On error throws exception.
     */
    void addReservation(Reservation reservation) throws ServiceException;

    /**
     * Gets the next id of the reservations.
     * @return The next id.
     * @throws ServiceException On error throws exception.
     */
    int getNextId() throws ServiceException;

    /**
     * Deletes a reservation from the database.
     * @param reservation The reservation to be deleted.
     * @throws ServiceException On error throws exception.
     */
    void deleteReservation(Invoice reservation) throws ServiceException;

    /**
     * Updates reservation end date.
     * @param bid The id.
     * @param dateFrom The from date.
     * @param dateTo The to date.
     * @param newEndDate The new end date.
     * @throws ServiceException On error throws exception.
     */
    void updateReservationEndDate(int bid, Date dateFrom, Date dateTo, Date newEndDate) throws ServiceException;
}