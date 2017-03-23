package sepm.ss17.e1328036.dao.impl;

import sepm.ss17.e1328036.dao.ReservationDAO;
import sepm.ss17.e1328036.dto.Reservation;
import sepm.ss17.e1328036.util.DatabaseUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by evgen on 19.03.2017.
 */
public class ReservationDAOImpl implements ReservationDAO {

    private final String saveStatement = "INSERT INTO Reservations (bid, client_name, horse_name, date_from, date_to) VALUES (?, ?, ?, ?, ?)";
    private final String deleteStatement = "DELETE FROM Reservations r WHERE r.bid = ? AND r.date_from = ? AND r.date_to = ?";
    private final String selectAllStatement = "SELECT * FROM Reservations";
    private final String updateDateStatement = "UPDATE Reservations r SET date_to = ? WHERE r.bid = ? AND r.date_from = ? AND r.date_to = ?";


    @Override
    public void create(Reservation reservation) {
        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(saveStatement);

            preparedStatement.setInt(1, reservation.getBid());
            preparedStatement.setString(2, reservation.getClientName());
            preparedStatement.setString(3, reservation.getHorseName());
            preparedStatement.setDate(4, reservation.getDateFrom());
            preparedStatement.setDate(5, reservation.getDateTo());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Reservation reservation) {
        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(deleteStatement);

            preparedStatement.setInt(1, reservation.getBid());
            preparedStatement.setDate(2, reservation.getDateFrom());
            preparedStatement.setDate(3, reservation.getDateTo());

            preparedStatement.executeUpdate();
            preparedStatement.close();

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
    public void updateEndDate(Reservation reservation, Date endDate) {
        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(updateDateStatement);
            preparedStatement.setDate(1, endDate);
            preparedStatement.setInt(2, reservation.getBid());
            preparedStatement.setDate(3, reservation.getDateFrom());
            preparedStatement.setDate(4, reservation.getDateTo());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Reservation> getReservations(boolean isFiltered, Date dateFrom, Date dateTo) {
        List<Reservation> reservations = new LinkedList<>();

        try {
            PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(selectAllStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (!(isFiltered && (dateFrom.compareTo(resultSet.getDate(4)) > 0
                        || dateTo.compareTo(resultSet.getDate(5)) < 0))) {

                    reservations.add(new Reservation(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDate(4),
                            resultSet.getDate(5)));
                }
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }
}
