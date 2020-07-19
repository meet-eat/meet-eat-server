package meet_eat.server.repository;

import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
