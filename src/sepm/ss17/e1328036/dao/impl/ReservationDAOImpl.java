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
    private final String selectAllStatement = "SELECT * FROM Reservations r JOIN Boxreservations br on br.rid = r.rid";
    private final String updateDateStatement = "UPDATE BoxReservations br SET br.date_to = ? WHERE br.bid = ? AND br.date_from = ? AND br.date_to = ?";
    private final String selectByIdStatement = "SELECT * FROM Reservations r JOIN Boxreservations br on br.rid = r.rid WHERE r.rid = ?";
    private final String deleteInvoiceStatement = "DELETE FROM BoxReservations br WHERE br.brid = ?";


    @Override
    public void create(Reservation reservation) throws DAOException {

        try {
            PreparedStatement preparedStatement1 = DatabaseUtil.getConnection().prepareStatement(saveStatementReservation);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        } catch (SQLException e) {
            throw new DAOException("Problem occurred while trying to create a new reservation.");
        }

        try {
            PreparedStatement preparedStatement3 = DatabaseUtil.getConnection().prepareStatement(saveStatement);

            for (Invoice invoice:
                 reservation.getInvoices()) {

                preparedStatement3.setInt(1, invoice.getBid());
                preparedStatement3.setInt(2, invoice.getRid());
                preparedStatement3.setString(3, invoice.getClientName());
                preparedStatement3.setString(4, invoice.getHorseName());
                preparedStatement3.setDate(5, invoice.getDateFrom());
                preparedStatement3.setDate(6, invoice.getDateTo());
                preparedStatement3.executeUpdate();
            }

            preparedStatement3.close();

        } catch (SQLException e) {
            throw new DAOException("Problem occurred while trying to save a new invoice.");
        }
    }

    public int getNextId() throws DAOException {
        int rid = 0;

        try {
            PreparedStatement preparedStatement2 = DatabaseUtil.getConnection().prepareStatement(getReservationIdStatement);
            ResultSet rs = preparedStatement2.executeQuery();

            if (rs.next()) {
                rid = rs.getInt("rid");
            }

            preparedStatement2.close();
            rs.close();
        } catch (SQLException e) {
            throw new DAOException("Problem occurred while trying to get the next id.");
        }
        return rid + 1;
    }

    @Override
    public void delete(Invoice reservation) throws DAOException {

        if (!checkActive(reservation.getBrid(), false)) {
            try {
                PreparedStatement preparedStatement2 = DatabaseUtil.getConnection().prepareStatement(deleteInvoiceStatement);

                preparedStatement2.setInt(1, reservation.getBrid());
                preparedStatement2.executeUpdate();

                preparedStatement2.close();

            } catch (SQLException e) {
                throw new DAOException("Problem while deleting an invoice occurred.");
            }
        }
        else {
            throw new DAOException("The reservation has an active reserved box and cannot be deleted.");
        }
    }

    static boolean checkActive(int id, boolean isBox) {
        String statement = isBox ? "SELECT * FROM BoxReservations r WHERE r.date_to > ? AND r.bid = ?" : "SELECT * FROM BoxReservations r WHERE r.date_to > ? AND r.brid = ?";

        try {
            PreparedStatement preparedStatement1 = DatabaseUtil.getConnection().prepareStatement(statement);
            preparedStatement1.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
            preparedStatement1.setInt(2, id);

            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Reservation> getAll() throws DAOException {
        return getReservations(false, null, null);
    }

    @Override
    public List<Reservation> getById(int rid) throws DAOException {

        PreparedStatement preparedStatement = null;
        List<Reservation> reservations = new LinkedList<>();
        List<Invoice> invoices = new LinkedList<>();
        int oldId = 0;

        try {
            preparedStatement = DatabaseUtil.getConnection().prepareStatement(selectByIdStatement);
            preparedStatement.setInt(1, rid);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                int currentId = rs.getInt(1);

                if (oldId != currentId) {
                    if (!invoices.isEmpty()) {
                        reservations.add(new Reservation(oldId, false, invoices));
                    }
                    invoices = new LinkedList<>();
                    oldId = currentId;
                }
                invoices.add(new Invoice(rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getDate(8),
                        rs.getDate(9)));
            }

            reservations.add(new Reservation(oldId, false, invoices));
            rs.close();
            preparedStatement.close();

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return reservations;
    }

    @Override
    public List<Reservation> getByDate(Date startDate, Date endDate) throws DAOException {
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

    private List<Reservation> getReservations(boolean isFiltered, Date dateFrom, Date dateTo) throws DAOException {

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
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return reservations;
    }
}
