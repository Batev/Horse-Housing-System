package sepm.ss17.e1328036.dao;

import sepm.ss17.e1328036.dto.Reservation;

import java.sql.Date;
import java.util.List;

/**
 * Created by evgen on 18.03.2017.
 */
public interface ReservationDAO {

    void create(Reservation reservation);

    void delete(Reservation reservation);

    List<Reservation> getAll();

    List<Reservation> getByDate(Date startDate, Date endDate);

    void updateEndDate(Reservation reservation, Date endDate);
}
