package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.User;

public class ParticipationServiceTest extends EntityRelationServiceTest<ParticipationService, Participation, User, Offer, String> {

    @Override
    protected User getSourceEntity() {
        return getBasicUser();
    }

    @Override
    protected Offer getTargetEntity() {
        return getValidOffer(getBasicUser());
    }

    @Override
    protected Participation createDistinctTestEntity(User source, Offer target) {
        return new Participation(source, target);
    }
}
