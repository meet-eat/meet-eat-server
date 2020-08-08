package meet_eat.server.service;

import meet_eat.data.entity.Subscription;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SubscriptionService extends EntityService<Subscription, String, SubscriptionRepository> {

    private final UserService userService;

    /**
     * Constructs a new instance of {@link SubscriptionService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public SubscriptionService(SubscriptionRepository repository, UserService userService) {
        super(repository);
        this.userService = userService;
    }

    public Iterable<Subscription> getBySourceUser(User sourceUser) {
        return getRepository().findBySourceUser(sourceUser);
    }

    public Optional<Iterable<Subscription>> getBySourceUser(String sourceUserIdentifier) {
        Optional<User> optionalUser = userService.get(sourceUserIdentifier);
        return optionalUser.map(this::getBySourceUser);
    }

    public void deleteByUser(User sourceUser) {
        Objects.requireNonNull(sourceUser);
        getRepository().deleteBySourceUserOrTargetUser(sourceUser, sourceUser);
    }

    public void deleteByUser(String sourceUserId) {
        Optional<User> optionalCreator = userService.get(sourceUserId);
        optionalCreator.ifPresent(this::deleteByUser);
    }

    @Override
    public boolean existsPostConflict(Subscription entity) {
        return existsPair(entity) && super.existsPostConflict(entity);
    }

    private boolean existsPair(Subscription subscription) {
        Optional<Subscription> optionalSubscription = getRepository().findBySourceUserAndTargetUser(subscription.getSourceUser(), subscription.getTargetUser());
        return optionalSubscription.isPresent();
    }
}
