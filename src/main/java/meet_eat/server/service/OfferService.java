package meet_eat.server.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a service class providing functionality to manage {@link Offer offers} and their state persistence.
 */
@Service
public class OfferService extends EntityService<Offer, String, OfferRepository> {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final BookmarkService bookmarkService;
    private final ParticipationService participationService;
    private final ReportService reportService;
    private final RatingService ratingService;

    /**
     * Constructs a new instance of {@link OfferService}.
     *
     * @param offerRepository     the repository used for persistence operations
     * @param userService         the service used for operations on and with {@link User} entities
     * @param subscriptionService the service used for operations on and with {@link Subscription} entities
     */
    @Lazy
    @Autowired
    public OfferService(OfferRepository offerRepository, UserService userService, SubscriptionService subscriptionService,
                        BookmarkService bookmarkService, ParticipationService participationService,
                        ReportService reportService, RatingService ratingService) {
        super(offerRepository);
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.bookmarkService = bookmarkService;
        this.participationService = participationService;
        this.reportService = reportService;
        this.ratingService = ratingService;
    }

    /**
     * Gets {@link Offer offers} from the repository identified by their {@link User creator's} identifier.
     *
     * @param creatorId the identifier of the offer's creator
     * @return offers of an identified creator
     */
    public Optional<Iterable<Offer>> getByCreatorId(String creatorId) {
        Optional<User> optionalCreator = userService.get(creatorId);
        return optionalCreator.map(creator -> getRepository().findByCreator(creator));
    }

    @Override
    public void delete(Offer entity) {
        Objects.requireNonNull(entity);

        // Cascading deletion of relation entities
        bookmarkService.deleteByTarget(entity);
        participationService.deleteByTarget(entity);
        reportService.deleteByTarget(entity);
        ratingService.deleteByOffer(entity);

        super.delete(entity);
    }

    @Override
    public void delete(String identifier) {
        Objects.requireNonNull(identifier);

        // Cascading deletion of relation entities
        Optional<Offer> optionalOffer = get(identifier);
        optionalOffer.ifPresent(bookmarkService::deleteByTarget);
        optionalOffer.ifPresent(participationService::deleteByTarget);
        optionalOffer.ifPresent(reportService::deleteByTarget);
        optionalOffer.ifPresent(ratingService::deleteByOffer);

        super.delete(identifier);
    }

    /**
     * Deletes {@link Offer offers} from the repository identified by their {@link User creator}.
     *
     * @param creator the creator of the offers to be deleted
     */
    public void deleteByCreator(User creator) {
        getRepository().deleteByCreator(Objects.requireNonNull(creator));
    }

    /**
     * Deletes {@link Offer offers} from the repository identified by their {@link User creator's} identifier.
     *
     * @param creatorId the creator's identifier of the offers to be deleted
     */
    public void deleteByCreator(String creatorId) {
        Optional<User> optionalCreator = userService.get(creatorId);
        optionalCreator.ifPresent(this::deleteByCreator);
    }

    /**
     * Gets {@link Offer offers} from the repository which were created by subscribed {@link User users} of a given user.
     *
     * @param subscriberIdentifier the identifier of the subscriber
     * @return offers of the subscribed users of an identified subscriber
     */
    public Optional<Iterable<Offer>> getBySubscriberIdentifier(String subscriberIdentifier) {
        // Get "subscriber" user or return empty if not present.
        Optional<User> optionalSubscriber = userService.get(subscriberIdentifier);
        if (optionalSubscriber.isPresent()) {
            Iterable<Offer> offers = new LinkedList<>();

            // Get the subscription for every subscribed user.
            Iterable<Subscription> subscriptions = subscriptionService.getBySource(optionalSubscriber.get());

            // Stream the subscribed users
            List<User> subscribedUsers = Streams.stream(subscriptions)
                    .map(Subscription::getTarget)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Get the offers of the subscribed users
            for (User subscribedUser : subscribedUsers) {
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
