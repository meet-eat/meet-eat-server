package meet_eat.server.repository;

import meet_eat.data.entity.Subscription;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Represents a repository managing persistence of {@link Subscription} instances.
 */
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    /**
     * Finds and returns all {@link Subscription subscriptions} by their {@link User source user}.
     *
     * @param user the source user of the searched subscriptions
     * @return all subscriptions containing a specific source user
     */
    Iterable<Subscription> findBySourceUser(User user);

    /**
     * Finds and returns all {@link Subscription subscriptions} by their {@link User target user}.
     *
     * @param user the target user of the searched subscriptions
     * @return all subscriptions containing a specific target user
     */
    Iterable<Subscription> findByTargetUser(User user);

    /**
     * Finds and returns a {@link Subscription} containing a given pair of {@link User source and target user}.
     *
     * @param sourceUser the source user of the searched subscription
     * @param targetUser the target user of the searched subscription
     * @return a subscription containing a specific user pair
     */
    Optional<Subscription> findBySourceUserAndTargetUser(User sourceUser, User targetUser);

    /**
     * Deletes all {@link Subscription subscriptions} by their {@link User source user}.
     *
     * @param sourceUser the source user of the subscriptions to be deleted
     */
    void deleteBySourceUser(User sourceUser);

    /**
     * Deletes all {@link Subscription subscriptions} by their {@link User source user} or {@link User target user}.
     *
     * @param sourceUser the source user of the subscriptions to be deleted
     * @param targetUser the target user of the subscriptions to be deleted
     */
    void deleteBySourceUserOrTargetUser(User sourceUser, User targetUser);
}
