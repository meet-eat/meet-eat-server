package meet_eat.server.service;

public class EntityConflictException extends IllegalArgumentException {

    public EntityConflictException() {
        super();
    }

    public EntityConflictException(String message) {
        super(message);
    }
}
