package sepm.ss17.e1328036.service;

/**
 * @author Evgeni Batev
 * Exception thrown by the service.
 */
public class ServiceException extends Exception {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }
}
