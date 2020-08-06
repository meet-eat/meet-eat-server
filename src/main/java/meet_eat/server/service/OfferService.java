package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
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

    public Optional<Iterable<Offer>> getBySubscriberIdentifier(String subscriberIdentifier) {
        // Get "subscriber" user or return empty if not present.
        Optional<User> optionalSubscriber = userService.get(subscriberIdentifier);
        if (optionalSubscriber.isPresent()) {
            Iterable<Offer> offers = new LinkedList<>();

            // Get the offers of every subscribed user.
            for (User subscribedUser : optionalSubscriber.get().getSubscriptions()) {
                Optional<Iterable<Offer>> optionalOffersBySubscribed = getByCreatorId(subscribedUser.getIdentifier());

                // Add offers of subscribed user, continue if not present respectively.
                if (optionalOffersBySubscribed.isPresent()) {
                    Iterable<Offer> offersBySubscribed = optionalOffersBySubscribed.get();
                    offers = Iterables.concat(offers, offersBySubscribed);
                }
            }
            return Optional.of(offers);
        }
        return Optional.empty();
    }
}
