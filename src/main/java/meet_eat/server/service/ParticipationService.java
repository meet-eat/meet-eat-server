package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Represents a service class providing functionality to manage {@link Participation participations} and their state
 * persistence.
 */
@Service
public class ParticipationService extends EntityRelationService<Participation, User, Offer, String, ParticipationRepository> {

    /**
     * Constructs a new instance of {@link ParticipationService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public ParticipationService(ParticipationRepository repository) {
        super(repository);
    }
}
