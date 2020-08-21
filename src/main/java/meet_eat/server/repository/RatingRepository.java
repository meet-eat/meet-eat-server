package meet_eat.server.repository;

import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.user.User;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Rating} instances.
 */
@Repository
public interface RatingRepository extends EntityRelationRepository<Rating, User, User, String> {

}
