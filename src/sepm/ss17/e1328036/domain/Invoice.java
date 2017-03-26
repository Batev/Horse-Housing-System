package sepm.ss17.e1328036.domain;

import java.sql.Date;

/**
 * @author Evgeni Batev
 * Entity class for the invoices.
 */
public class Invoice {

    private int brid;
    private int bid;
    private int rid;
    private String clientName;
    private String horseName;
    private Date dateFrom;
    private Date dateTo;

    public Invoice(int brid, int bid, int rid, String clientName, String horseName, Date dateFrom, Date dateTo) {
        this.brid = brid;
        this.bid = bid;
        this.rid = rid;
        this.clientName = clientName;
        this.horseName = horseName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public int getBrid() {
        return brid;
    }

    public void setBrid(int brid) {
        this.brid = brid;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
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
        return "Client name: " + clientName + ", Horse name: " + horseName + ", From: " + dateFrom + " to " + dateTo + ".\n";
    }
}
