package sepm.ss17.e1328036.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1328036.dao.DAOException;
import sepm.ss17.e1328036.dao.ReservationDAO;
import sepm.ss17.e1328036.dto.Invoice;
import sepm.ss17.e1328036.dto.Reservation;
import sepm.ss17.e1328036.util.DatabaseUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by evgen on 19.03.2017.
 */
public class ReservationDAOImpl implements ReservationDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReservationDAOImpl.class);

    private final String saveStatementReservation = "INSERT INTO Reservations VALUES (DEFAULT, DEFAULT)";
    private final String getReservationIdStatement = "SELECT TOP 1 rid FROM Reservations r ORDER BY r.rid DESC";
    private final String saveStatement = "INSERT INTO BoxReservations (brid, bid, rid, client_name, horse_name, date_from, date_to) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private final String deleteStatement = "UPDATE Reservations r SET r.is_deleted = TRUE WHERE r.rid = ?";
    private final String checkAvailabilityStatement = "SELECT * FROM Reservations r WHERE r.date_to > ?";
    private final String selectAllStatement = "SELECT * FROM Reservations r JOIN Boxreservations br on br.rid = r.rid";
    private final String updateDateStatement = "UPDATE BoxReservations br SET br.date_to = ? WHERE br.bid = ? AND br.date_from = ? AND br.date_to = ?";


    @Override
    public void create(Reservation reservation) {
        int rid = 0;

        try {
            PreparedStatement preparedStatement1 = DatabaseUtil.getConnection().prepareStatement(saveStatementReservation);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement2 = DatabaseUtil.getConnection().prepareStatement(getReservationIdStatement);
            ResultSet rs = preparedStatement2.executeQuery();

            rid = rs.getInt(1);

            preparedStatement2.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement3 = DatabaseUtil.getConnection().prepareStatement(saveStatement);

            for (Invoice invoice:
                 reservation.getInvoices()) {

                preparedStatement3.setInt(1, invoice.getBid());
                preparedStatement3.setInt(2, rid);
                preparedStatement3.setString(3, invoice.getClientName());
                preparedStatement3.setString(4, invoice.getHorseName());
                preparedStatement3.setDate(5, invoice.getDateFrom());
                preparedStatement3.setDate(6, invoice.getDateTo());
                preparedStatement3.executeUpdate();
            }

            preparedStatement3.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reservation reservation) throws DAOException {

        try {
            PreparedStatement preparedStatement1 = DatabaseUtil.getConnection().prepareStatement(checkAvailabilityStatement);
            preparedStatement1.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));

            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                throw new DAOException("There is a reservation, that is currently active.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement preparedStatement2 = DatabaseUtil.getConnection().prepareStatement(deleteStatement);

            preparedStatement2.setInt(1, reservation.getRid());
            preparedStatement2.executeUpdate();

            preparedStatement2.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reservation> getAll() {
        return getReservations(false, null, null);
    }

    @Override
    public List<Reservation> getByDate(Date startDate, Date endDate) {
        return getReservations(true, startDate, endDate);
    }

    @Override
    public void updateEndDate(int bid, Date dateFrom, Date dateTo, Date newEndDate) {
        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(updateDateStatement);
            preparedStatement.setDate(1, newEndDate);
            preparedStatement.setInt(2, bid);
            preparedStatement.setDate(3, dateFrom);
            preparedStatement.setDate(4, dateTo);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Reservation> getReservations(boolean isFiltered, Date dateFrom, Date dateTo) {

        List<Reservation> reservations = new LinkedList<>();
        List<Invoice> invoices = new LinkedList<>();
        int oldId = 0;

        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(selectAllStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int currentId = resultSet.getInt(1);

                if (oldId != currentId) {
                    if (!invoices.isEmpty()) {
                        reservations.add(new Reservation(oldId, false, invoices));
                    }
                    invoices = new LinkedList<>();
                    oldId = currentId;
                }
                if (!(isFiltered && (dateFrom.compareTo(resultSet.getDate(8)) > 0
                        || dateTo.compareTo(resultSet.getDate(9)) < 0))) {
                    invoices.add(new Invoice(resultSet.getInt(3),
                            resultSet.getInt(4),
                            resultSet.getInt(5),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            resultSet.getDate(8),
                            resultSet.getDate(9)));
                }
            }

            reservations.add(new Reservation(oldId, false, invoices));

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }
}
