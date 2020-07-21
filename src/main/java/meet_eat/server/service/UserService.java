package meet_eat.server.service;

import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends EntityService<User, String, UserRepository> {

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public User getUserByEmail(Email email) {
        return getRepository().findOneByEmail(email);
    }
}
