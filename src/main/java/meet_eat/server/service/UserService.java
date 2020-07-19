package meet_eat.server.service;

import meet_eat.data.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends EntityService<User, String> {

    @Autowired
    public UserService(MongoRepository<User, String> userRepository) {
        super(userRepository);
    }
}
