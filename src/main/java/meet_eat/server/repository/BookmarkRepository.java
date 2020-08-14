package meet_eat.server.repository;

import meet_eat.data.entity.Bookmark;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Represents a repository managing persistence of {@link Bookmark} instances.
 */
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

}
