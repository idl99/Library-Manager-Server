package exceptions;


/**
 * Custom Exception to mark exceptions that arise when trying to insert Library Items after library have reached
 * maximum capacity for those given Library Items.
 */
public class MaximumCapacityException extends Exception{

    public MaximumCapacityException(String message) {
        super(message);
    }

}
