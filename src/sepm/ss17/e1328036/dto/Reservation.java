package sepm.ss17.e1328036.dto;

import java.sql.Date;

/**
 * Created by evgen on 18.03.2017.
 */
public class Reservation {
    private int bid;
    private String clientName;
    private String horseName;
    private Date dateFrom;
    private Date dateTo;

    public Reservation(int bid, String clientName, String horseName, Date dateFrom, Date dateTo) {
        this.bid = bid;
        this.clientName = clientName;
        this.horseName = horseName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Client: " + clientName + ", Horse: " + horseName + ", From: " + dateFrom.toString() + " to " + dateTo.toString() + ".";
    }
}
