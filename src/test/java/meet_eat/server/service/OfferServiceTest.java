package meet_eat.server.service;

import static org.junit.Assert.*;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import org.assertj.core.util.Streams;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;
import java.util.stream.Stream;

public class OfferServiceTest extends EntityServiceTest<OfferService, Offer, String> {

    private static int offerCount = 0;
    private static boolean isUserRepoInitialized = false;
    private static boolean isTagRepoInitialized = false;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Before
    public void prepareUserRepository() {
        if (!isUserRepoInitialized) {
            Password validPassword = Password.createHashedPassword("ABCDEFGhijkl1234!");
            Localizable validLocalizable = new SphericalLocation(new SphericalPosition(0, 0));
            userService.getRepository().deleteAll();
            for (int i = 0; i < 5; i++) {
                User user = new User(new Email("noreply" + i + ".meet.eat@gmail.com"),
                        validPassword, LocalDate.of(1990, Month.JANUARY, 1),
                        "User" + i, "12345" + i, "Description" + i, true, validLocalizable);
                userService.post(user);
            }
            isUserRepoInitialized = true;
        }
    }

    @Before
    public void prepareTagRepository() {
        if (!isTagRepoInitialized) {
            Stream<Tag> tags = Stream.of(new Tag("Vegan"), new Tag("Vegetarian"), new Tag("Gluten free"));
            tagService.getRepository().deleteAll();
            tags.forEach(tagService::post);
            isTagRepoInitialized = true;
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetByCreatorIdNull() {
        // Execution
        getEntityService().getByCreatorId(null);
    }

    @Test
    public void testGetByCreatorId() {
        // Test data
        User creator = Streams.stream(userService.getAll()).findAny().orElseThrow();
        User otherCreator = Streams.stream(userService.getAll()).filter(x -> !x.equals(creator)).findAny().orElseThrow();

        // Execution
        Offer offerFst = getEntityService().post(createDistinctOffer(creator));
        Offer offerSnd = getEntityService().post(createDistinctOffer(creator));
        Offer offerTrd = getEntityService().post(createDistinctOffer(otherCreator));
        Iterable<Offer> gotOffers = getEntityService().getByCreatorId(creator.getIdentifier()).orElseThrow();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(2, Iterables.size(gotOffers));
        assertTrue(Iterables.contains(gotOffers, offerFst));
        assertTrue(Iterables.contains(gotOffers, offerSnd));
        assertFalse(Iterables.contains(gotOffers, offerTrd));
    }

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
        User creator = Streams.stream(userService.getAll()).findAny().orElseThrow();
        User otherCreator = Streams.stream(userService.getAll()).filter(x -> !x.equals(creator)).findAny().orElseThrow();

        // Execution
        Offer offerFst = getEntityService().post(createDistinctOffer(creator));
        Offer offerSnd = getEntityService().post(createDistinctOffer(creator));
        Offer offerTrd = getEntityService().post(createDistinctOffer(otherCreator));
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
        User creator = Streams.stream(userService.getAll()).findAny().orElseThrow();
        User otherCreator = Streams.stream(userService.getAll()).filter(x -> !x.equals(creator)).findAny().orElseThrow();

        // Execution
        Offer offerFst = getEntityService().post(createDistinctOffer(creator));
        Offer offerSnd = getEntityService().post(createDistinctOffer(creator));
        Offer offerTrd = getEntityService().post(createDistinctOffer(otherCreator));
        getEntityService().deleteByCreator(creator.getIdentifier());
        Iterable<Offer> gotOffers = getEntityService().getAll();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(1, Iterables.size(gotOffers));
        assertFalse(Iterables.contains(gotOffers, offerFst));
        assertFalse(Iterables.contains(gotOffers, offerSnd));
        assertTrue(Iterables.contains(gotOffers, offerTrd));
    }

    @Override
    protected Offer createDistinctTestEntity() {
        User creator = Streams.stream(userService.getAll()).findAny().orElseThrow();
        return createDistinctOffer(creator);
    }

    private Offer createDistinctOffer(User creator) {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JULY, 30, 12, 32);
        Localizable location = new CityLocation("Karlsruhe");
        Set<Tag> tags = Sets.newHashSet(tagService.getAll());
        Offer offer = new Offer(creator, tags, "Offer " + offerCount++,
                "Spaghetti. Mhmmm.", 4.99, 3, dateTime, location);
        Streams.stream(userService.getAll()).filter(x -> !x.equals(creator)).forEach(offer::addParticipant);
        return offer;
    }
}
