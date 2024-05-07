package dev.app.artist.api.exception;

/**
 * @author Anish Panthi
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
      super("Resource not found");
    }

    public NotFoundException(String message) {
      super(message);
    }
}
