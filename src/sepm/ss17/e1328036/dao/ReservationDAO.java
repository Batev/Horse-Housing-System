package sepm.ss17.e1328036.dao;

import sepm.ss17.e1328036.dto.Invoice;
import sepm.ss17.e1328036.dto.Reservation;

import java.sql.Date;
import java.util.List;

/**
 * Created by evgen on 18.03.2017.
 */
public interface ReservationDAO {

    void create(Reservation reservation) throws DAOException;

    void delete(Invoice reservation) throws DAOException;

    List<Reservation> getAll() throws DAOException;

    List<Reservation> getById(int rid) throws DAOException;

    List<Reservation> getByDate(Date startDate, Date endDate) throws DAOException;

    void updateEndDate(int bid, Date dateFrom, Date dateTo, Date newEndDate);

    int getNextId() throws  DAOException;
}
