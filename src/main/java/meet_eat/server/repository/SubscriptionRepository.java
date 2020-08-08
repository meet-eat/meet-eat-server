package meet_eat.server.repository;

import meet_eat.data.entity.Subscription;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Iterable<Subscription> findBySourceUser(User user);

    Iterable<Subscription> findByTargetUser(User user);

    Optional<Subscription> findBySourceUserAndTargetUser(User sourceUser, User targetUser);

    void deleteBySourceUser(User sourceUser);

    void deleteBySourceUserOrTargetUser(User sourceUser, User targetUser);
}
