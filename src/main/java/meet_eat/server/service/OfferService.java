package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class OfferService extends EntityService<Offer, String, OfferRepository> {

    private final UserService userService;

    @Lazy
    @Autowired
    public OfferService(OfferRepository offerRepository, UserService userService) {
        super(offerRepository);
        this.userService = userService;
    }

    public Optional<Iterable<Offer>> getByCreatorId(String creatorId) {
        Optional<User> optionalCreator = userService.get(creatorId);
        return optionalCreator.map(creator -> getRepository().findByCreator(creator));
    }

    public void deleteByCreator(User creator) {
        getRepository().deleteByCreator(Objects.requireNonNull(creator));
    }

    public void deleteByCreator(String creatorId) {
        Optional<User> optionalCreator = userService.get(creatorId);
        optionalCreator.ifPresent(this::deleteByCreator);
    }
}
