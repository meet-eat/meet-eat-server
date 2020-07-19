package meet_eat.server.repository;

import meet_eat.data.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String> {

}
