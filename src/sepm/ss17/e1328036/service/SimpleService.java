package sepm.ss17.e1328036.service;

import sepm.ss17.e1328036.dao.BoxDAO;
import sepm.ss17.e1328036.dao.DAOException;
import sepm.ss17.e1328036.dao.ReservationDAO;
import sepm.ss17.e1328036.dao.impl.BoxDAOImpl;
import sepm.ss17.e1328036.dao.impl.ReservationDAOImpl;
import sepm.ss17.e1328036.domain.Box;
import sepm.ss17.e1328036.domain.Invoice;
import sepm.ss17.e1328036.domain.Reservation;

import java.sql.Date;
import java.util.List;

/**
 * Created by evgen on 23.03.2017.
 */
public class SimpleService implements Service {

    private BoxDAO boxDAO;
    private ReservationDAO reservationDAO;

    public SimpleService() {
        boxDAO = new BoxDAOImpl();
        reservationDAO = new ReservationDAOImpl();
    }

    @Override
    public List<Box> getAllBoxes() throws ServiceException {
        try {
            return boxDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Box> getAllBoxesWithDeleted() throws ServiceException {
        try {
            return boxDAO.getAllWithDeleted();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Box> getBoxesByPrice(float priceFrom, float priceTo) throws ServiceException {

        if (priceFrom >= priceTo) {
            throw new ServiceException("Price from should be greater than price to in the search fields.");
        }

        try {
            return boxDAO.getByPrice(priceFrom, priceTo);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Box> getBoxesBySize(float sizeFrom, float sizeTo) throws ServiceException {

        if (sizeFrom >= sizeTo) {
            throw new ServiceException("Size from should be greater than size to in the search fields.");
        }

        try {
            return boxDAO.getBySize(sizeFrom, sizeTo);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Box> getBoxesById(int bid) throws ServiceException {

        if (bid < 0) {
            throw new ServiceException("Bid cannot be negative.");
        }

        try {
            return boxDAO.getById(bid);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateBoxSize(int bid, float newSize) throws ServiceException {

        if (newSize <= 0) {
            throw new ServiceException("The size must be a positive number.");
        }

        try {
            boxDAO.updateSize(bid, newSize);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateBoxPrice(int bid, float newPrice) throws ServiceException {

        if (newPrice <= 0) {
            throw new ServiceException("The price must be a positive number.");
        }

        try {
            boxDAO.updatePrice(bid, newPrice);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteBox(int bid) throws ServiceException {

        try {
            boxDAO.delete(bid);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addBox(Box box) throws ServiceException {
        if (box.getSize() < 0 || box.getSawdust() < 0 || box.getStraw() < 0 || box.getPrice() < 0) {
            throw new ServiceException("All values must be positive.");
        }

        try {
            boxDAO.save(box);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Reservation> getAllReservations() throws ServiceException {
        try {
            return reservationDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Problem while listing all reservations.");
        }
    }

    @Override
    public List<Reservation> getReservationsById(int rid) throws ServiceException {

        if (rid < 0) {
            throw new ServiceException("Reservation id must be a positive number.");
        }
        try {
            return reservationDAO.getById(rid);
        } catch (DAOException e) {
            throw new ServiceException("Problem while listing reservations by id.");
        }
    }

    @Override
    public List<Reservation> getReservationsByDate(Date from, Date to) throws ServiceException {
        if (from.after(to)) {
            throw new ServiceException("Date from must be earlier than Date to.");
        }
        try {
            return reservationDAO.getByDate(from, to);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public int getNextId() throws ServiceException {
        try {
            return reservationDAO.getNextId();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteReservation(Invoice reservation) throws ServiceException {
        if(reservation == null) {
            throw new ServiceException("Reservation must not be null.");
        }

        try {
            reservationDAO.delete(reservation);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateReservationEndDate(int bid, Date dateFrom, Date dateTo, Date newEndDate) throws ServiceException {
        if (bid < 0) {
            throw new ServiceException("Box ID must be a positive number.");
        }
        else if (dateFrom.after(dateTo)) {
            throw new ServiceException("Date to must be greater than Date from.");
        }
        else if (dateFrom.after(newEndDate)) {
            throw new ServiceException("The reservation has not stared yet.");
        }
        else if (dateTo.before(newEndDate)) {
            throw new ServiceException("Cannot change the Date to of an inactive reservation.");
        }

        reservationDAO.updateEndDate(bid, dateFrom, dateTo, newEndDate);
    }

    @Override
    public void addReservation(Reservation reservation) throws ServiceException {
        if (reservation == null) {
            throw new ServiceException("Reservation must not be null.");
        }
        try {
            reservationDAO.create(reservation);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
