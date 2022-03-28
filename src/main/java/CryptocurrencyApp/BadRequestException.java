package CryptocurrencyApp;

/**
 * This exception is thrown when an invalid or erroneous HTTP request has been made
 */
public class BadRequestException
        extends RuntimeException {
    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }
}
