package meet_eat.server.service;

import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Subscription subscriptions} and their state
 * persistence.
 */
@Service
public class SubscriptionService extends EntityService<Subscription, String, SubscriptionRepository> {

    private final UserService userService;

    /**
     * Constructs a new instance of {@link SubscriptionService}.
     *
     * @param repository  the repository used for persistence operations
     * @param userService the service used for operations on and with {@link User} entities
     */
    @Lazy
    @Autowired
    public SubscriptionService(SubscriptionRepository repository, UserService userService) {
        super(repository);
        this.userService = userService;
    }

    /**
     * Gets all {@link Subscription subscriptions} containing a specific {@link User source user}.
     *
     * @param sourceUser the source user of the returned subscriptions
     * @return subscriptions containing a specific {@link User source user}
     */
    public Iterable<Subscription> getBySourceUser(User sourceUser) {
        return getRepository().findBySource(sourceUser);
    }

    /**
     * Gets all {@link Subscription subscriptions} containing a specific {@link User source user} identified by
     * identifier.
     *
     * @param sourceUserIdentifier the source user's identifier
     * @return subscriptions containing a specific {@link User source user}
     */
    public Optional<Iterable<Subscription>> getBySourceUser(String sourceUserIdentifier) {
        Optional<User> optionalUser = userService.get(sourceUserIdentifier);
        return optionalUser.map(this::getBySourceUser);
    }

    /**
     * Deletes all {@link Subscription subscriptions} containing a specific {@link User user}.
     *
     * @param user the user contained by the subscriptions to be deleted
     */
    public void deleteByUser(User user) {
        Objects.requireNonNull(user);
        getRepository().deleteBySourceOrTarget(user, user);
    }

    /**
     * Deletes all {@link Subscription subscriptions} containing a specific {@link User user}.
     *
     * @param userId the source user's identifier
     */
    public void deleteByUser(String userId) {
        Optional<User> optionalCreator = userService.get(userId);
        optionalCreator.ifPresent(this::deleteByUser);
    }

    @Override
    public boolean existsPostConflict(Subscription entity) {
        return existsPair(entity) || super.existsPostConflict(entity);
    }

    /**
     * Signalizes whether a given source and target {@link User} pair derived from a given
     * {@link Subscription subscription} already exists within the repository.
     *
     * @param subscription the subscription the user pair gets derived from
     * @return True if the given user pair already exists, false otherwise.
     */
    private boolean existsPair(Subscription subscription) {
        Optional<Subscription> optionalSubscription = getRepository().findBySourceAndTarget(subscription.getSource(), subscription.getTarget());
        return optionalSubscription.isPresent();
    }
}
