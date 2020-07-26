package meet_eat.server.repository;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    public void deleteByUser(User user);
}
