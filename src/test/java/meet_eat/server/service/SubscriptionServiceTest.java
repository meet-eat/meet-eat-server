package meet_eat.server.service;

import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;

public class SubscriptionServiceTest extends EntityRelationServiceTest<SubscriptionService, Subscription, User, User, String> {

    @Override
    protected User getSourceEntity() {
        return getBasicUser();
    }

    @Override
    protected User getTargetEntity() {
        return getBasicUser();
    }

    @Override
    protected Subscription createDistinctTestEntity(User source, User target) {
        return new Subscription(source, target);
    }
}
