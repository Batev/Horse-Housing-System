package sepm.ss17.e1328036.domain;

import java.util.List;

/**
 * Created by evgen on 18.03.2017.
 */
public class Reservation {
    private int rid;
    private boolean is_deleted;
    private List<Invoice> invoices;

    public Reservation(int rid, boolean is_deleted, List<Invoice> invoices) {
        this.rid = rid;
        this.is_deleted = is_deleted;
        this.invoices = invoices;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public String toString() {
        return "RID: " + rid + ", with Invoices: \n" + invoices;
    }
}