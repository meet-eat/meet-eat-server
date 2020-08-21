package meet_eat.server.service;

import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Represents a service class providing functionality to manage {@link Rating ratings} and their state
 * persistence.
 */
@Service
public class RatingService extends EntityRelationService<Rating, User, User, String, RatingRepository> {

    /**
     * Constructs a new instance of {@link RatingService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public RatingService(RatingRepository repository) {
        super(repository);
    }
}
