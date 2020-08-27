package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OfferServiceTest extends EntityServiceTest<OfferService, Offer, String> {

    private static boolean isTagRepoInitialized = false;

    @Autowired
    private TagService tagService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private ParticipationService participationService;

    @Before
    public void prepareTagRepository() {
        if (!isTagRepoInitialized) {
            Stream<Tag> tags = Stream.of(new Tag("Vegan"), new Tag("Vegetarian"), new Tag("Gluten free"));
            tagService.getRepository().deleteAll();
            tags.forEach(tagService::post);
            isTagRepoInitialized = true;
        }
    }

    //#region @Test getByCreatorId

    @Test(expected = NullPointerException.class)
    public void testGetByCreatorIdNull() {
        // Execution
        getEntityService().getByCreatorId(null);
    }

    @Test
    public void testGetByCreatorId() {
        // Test data
        User creator = getBasicUserPersistent();
        User otherCreator = getBasicUserPersistent();
        Offer offerFst = getOfferPersistent(creator);
        Offer offerSnd = getOfferPersistent(creator);
        Offer offerTrd = getOfferPersistent(otherCreator);

        // Execution
        Iterable<Offer> gotOffers = getEntityService().getByCreatorId(creator.getIdentifier()).orElseThrow();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(2, Iterables.size(gotOffers));
        assertTrue(Iterables.contains(gotOffers, offerFst));
        assertTrue(Iterables.contains(gotOffers, offerSnd));
        assertFalse(Iterables.contains(gotOffers, offerTrd));
    }

    //#endregion

    //#region @Test deleteByCreator

    @Test(expected = NullPointerException.class)
    public void testDeleteByCreatorEntityNull() {
        // Execution
        getEntityService().deleteByCreator((User) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByCreatorIdentifierNull() {
        // Execution
        getEntityService().deleteByCreator((String) null);
    }

    @Test
    public void testDeleteByCreatorEntity() {
        // Test data
        User creator = getBasicUserPersistent();
        User otherCreator = getBasicUserPersistent();
        Offer offerFst = getOfferPersistent(creator);
        Offer offerSnd = getOfferPersistent(creator);
        Offer offerTrd = getOfferPersistent(otherCreator);

        // Execution
        getEntityService().deleteByCreator(creator);
        Iterable<Offer> gotOffers = getEntityService().getAll();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(1, Iterables.size(gotOffers));
        assertFalse(Iterables.contains(gotOffers, offerFst));
        assertFalse(Iterables.contains(gotOffers, offerSnd));
        assertTrue(Iterables.contains(gotOffers, offerTrd));
    }

    @Test
    public void testDeleteByCreatorIdentifier() {
        // Test data
        User creator = getBasicUserPersistent();
        User otherCreator = getBasicUserPersistent();
        Offer offerFst = getOfferPersistent(creator);
        Offer offerSnd = getOfferPersistent(creator);
        Offer offerTrd = getOfferPersistent(otherCreator);

        // Execution
        getEntityService().deleteByCreator(creator.getIdentifier());
        Iterable<Offer> gotOffers = getEntityService().getAll();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(1, Iterables.size(gotOffers));
        assertFalse(Iterables.contains(gotOffers, offerFst));
        assertFalse(Iterables.contains(gotOffers, offerSnd));
        assertTrue(Iterables.contains(gotOffers, offerTrd));
    }

    //#endregion

    //#region @Test existsPutConflict

    @Test(expected = NullPointerException.class)
    public void testExistsPutConflictNull() {
        // Execution
        getEntityService().existsPutConflict(null);
    }

    // TODO Finish implementation
    @Ignore
    @Test
    public void testExistsPutConflictParticipantAmount() {
        // Test data
        Offer offer = getEntityService().post(createDistinctTestEntity());

        // Execution
        getEntityService().existsPutConflict(null);
    }

    //#endregion

    @Override
    protected Offer createDistinctTestEntity() {
        return getOfferTransient(getBasicUserPersistent());
    }
}
