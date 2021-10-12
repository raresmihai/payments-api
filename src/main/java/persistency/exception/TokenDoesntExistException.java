package persistency.exception;

public class TokenDoesntExistException extends RepositoryException {
    public TokenDoesntExistException(String message) {
        super(message);
    }
}
