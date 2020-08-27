package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.user.User;

public class BookmarkServiceTest extends EntityRelationServiceTest<BookmarkService, Bookmark, User, Offer, String> {

    @Override
    protected User getSourceEntity() {
        return getBasicUser();
    }

    @Override
    protected Offer getTargetEntity() {
        return getValidOffer(getBasicUser());
    }

    @Override
    protected Bookmark createDistinctTestEntity(User source, Offer target) {
        return new Bookmark(source, target);
    }
}
