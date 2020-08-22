package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Participation participations} and their state
 * persistence.
 */
@Service
public class ParticipationService extends EntityRelationService<Participation, User, Offer, String, ParticipationRepository> {

    private final OfferService offerService;

    /**
     * Constructs a new instance of {@link ParticipationService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public ParticipationService(ParticipationRepository repository, OfferService offerService) {
        super(repository);
        this.offerService = offerService;
    }

    public Optional<Iterable<Participation>> getByOfferIdentifier(String offerIdentifier) {
        Optional<Offer> optionalOffer = offerService.get(offerIdentifier);
        return optionalOffer.map(this::getByTarget);
    }
}
