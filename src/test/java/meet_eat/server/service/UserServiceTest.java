package meet_eat.server.service;

import com.google.common.collect.Sets;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import org.assertj.core.util.Streams;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class UserServiceTest extends EntityServiceTest<UserService, User, String> {

    private static int userCount = 0;
    private static int offerCount = 0;
    private static boolean isOfferRepoInitialized = false;

    @Autowired
    OfferService offerService;

    @Before
    public void prepareOfferRepository() {
        offerService.getRepository().deleteAll();
    }

    @Override
    protected User createDistinctTestEntity() {
        Email email = new Email("noreply" + userCount + ".meet.eat@example.com");
        Password validPassword = Password.createHashedPassword("ABCDEFGhijkl1234!");
        User user = new User(email, validPassword, LocalDate.of(1990, Month.JANUARY, 1),
                "User" + userCount, "12345" + userCount, "Description" + userCount, true);
        userCount++;
        return user;
    }

    private Offer createDistinctRepoOffer(User creator) {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JULY, 30, 12, 32);
        Localizable location = new CityLocation("Karlsruhe");
        Set<Tag> tags = new HashSet<>();
        Offer offer = new Offer(creator, tags, "Offer " + offerCount++,
                "Spaghetti. Mhmmm.", 4.99, 3, dateTime, location);
        return offerService.post(offer);
    }
}
