package meet_eat.server.service.security;

import meet_eat.data.entity.user.User;

public class UserSecurityService extends SecurityService<User> {

    @Override
    public User anonymiseEntity(User entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalEntityOperation(User entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
