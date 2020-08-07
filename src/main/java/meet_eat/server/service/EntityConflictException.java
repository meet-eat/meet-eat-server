package meet_eat.server.service;

/**
 * Represents an exception indicating a conflict between {@link meet_eat.data.entity.Entity} instances.
 */
public class EntityConflictException extends IllegalArgumentException {

    /**
     * Constructs a new instance of {@link EntityConflictException}.
     */
    public EntityConflictException() {
        super();
    }

    /**
     * Constructs a new instance of {@link EntityConflictException} with a given message.
     *
     * @param message the message of the exception
     */
    public EntityConflictException(String message) {
        super(message);
    }
}
