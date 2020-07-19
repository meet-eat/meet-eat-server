package meet_eat.server.repository;

import meet_eat.data.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {

}
