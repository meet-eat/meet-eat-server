package meet_eat.server.service;

import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingValue;
import meet_eat.data.entity.user.User;

public class RatingServiceTest extends EntityRelationServiceTest<RatingService, Rating, User, User, String> {

    @Override
    protected User getSourceEntity() {
        return getBasicUser();
    }

    @Override
    protected User getTargetEntity() {
        return getBasicUser();
    }

    @Override
    protected Rating createDistinctTestEntity(User source, User target) {
        return Rating.createHostRating(source, getValidOffer(target), RatingValue.POINTS_3);
    }
}
