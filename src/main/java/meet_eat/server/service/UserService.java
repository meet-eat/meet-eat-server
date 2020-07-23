package meet_eat.server.service;

import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends EntityService<User, String, UserRepository> {

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public Optional<User> getByEmail(Email email) {
        return getRepository().findOneByEmail(email);
    }
}
