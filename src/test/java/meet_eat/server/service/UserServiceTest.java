package meet_eat.server.service;

import static org.junit.Assert.*;

import com.google.common.collect.Iterables;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class UserServiceTest extends EntityServiceTest<UserService, User, String> {

    private static int userCount = 0;
    private static int offerCount = 0;

    @Autowired
    OfferService offerService;

    @Autowired
    TokenService tokenService;

    @Before
    public void prepareForeignRepositories() {
        offerService.getRepository().deleteAll();
        offerCount = 0;
        tokenService.getRepository().deleteAll();
    }

    @Test(expected = NullPointerException.class)
    public void testExistsPutConflictNull() {
        // Execution
        getEntityService().existsPutConflict(null);
    }

    @Test
    public void testExistsPutConflictSameUser() {
        // Test data
        User user = createDistinctTestEntity();

        // Execution
        User postedUser = getEntityService().post(user);

        // Assertions
        assertFalse(getEntityService().existsPutConflict(postedUser));
    }

    @Test
    public void testExistsPutConflictSameEmail() {
        // Test data
        Email email = new Email("valid.meet.eat@example.com");
        User userFst = createDistinctUser(email);
        User userSnd = createDistinctTestEntity();

        // Execution
        getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        postedUserSnd.setEmail(email);

        // Assertions
        assertTrue(getEntityService().existsPutConflict(postedUserSnd));
    }

    @Test
    public void testExistsPutConflictNoConflict() {
        // Test data
        Email email = new Email("valid.meet.eat@example.com");
        User userFst = createDistinctUser(email);
        User userSnd = createDistinctTestEntity();

        // Execution
        getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);

        // Assertions
        assertFalse(getEntityService().existsPutConflict(postedUserSnd));
    }

    @Test(expected = EntityConflictException.class)
    public void testPutWithEmailConflict() {
        // Test data
        Email email = new Email("valid.meet.eat@example.com");
        User userFst = createDistinctUser(email);
        User userSnd = createDistinctTestEntity();

        // Execution
        getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        postedUserSnd.setEmail(email);
        getEntityService().put(postedUserSnd);
    }

    @Test
    public void testPutUnmodifiedPassword() {
        // Test data
        User user = createDistinctTestEntity();

        // Execution
        User postedUser = getEntityService().post(user);
        Password postedPassword = postedUser.getPassword();
        User putUser = getEntityService().put(postedUser);

        // Assertions
        assertEquals(postedPassword, putUser.getPassword());
    }

    @Test
    public void testPutModifiedPassword() {
        // Test data
        User user = createDistinctTestEntity();
        Password newPassword = Password.createHashedPassword("New:ABCDEFGhijklmn123!");

        // Execution
        User postedUser = getEntityService().post(user);
        postedUser.setPassword(newPassword);
        User putUser = getEntityService().put(postedUser);

        // Assertions
        assertNotEquals(newPassword, putUser.getPassword());
        assertNotNull(putUser.getPassword().getIterations());
        assertNotNull(putUser.getPassword().getSalt());
        assertNotNull(putUser.getPassword().getHash());
    }

    @Test(expected = EntityConflictException.class)
    public void testPostWithEmailConflict() {
        // Test data
        Email email = new Email("valid.meet.eat@example.com");
        User userFst = createDistinctUser(email);
        User userSnd = createDistinctUser(email);

        // Execution
        getEntityService().post(userFst);
        getEntityService().post(userSnd);
    }

    @Test
    public void testPostPasswordDerivation() {
        // Test data
        User user = createDistinctTestEntity();
        Password password = user.getPassword();

        // Execution
        User postedUser = getEntityService().post(user);

        // Assertions
        assertNotEquals(password, postedUser.getPassword());
        assertNotNull(postedUser.getPassword().getIterations());
        assertNotNull(postedUser.getPassword().getSalt());
        assertNotNull(postedUser.getPassword().getHash());
    }

    @Test
    public void testDeleteByEntityCascadingOffers() {
        // Test data
        User userFst = createDistinctTestEntity();
        User userSnd = createDistinctTestEntity();

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Offer offerFst = createDistinctRepoOffer(postedUserFst);
        Offer offerSnd = createDistinctRepoOffer(postedUserFst);
        Offer offerTrd = createDistinctRepoOffer(postedUserSnd);
        Offer offerFth = createDistinctRepoOffer(postedUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(offerService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst);

        // Assertions
        Iterable<Offer> offers = offerService.getAll();
        assertEquals(2, Iterables.size(offers));
        assertFalse(Iterables.contains(offers, offerFst));
        assertFalse(Iterables.contains(offers, offerSnd));
        assertTrue(Iterables.contains(offers, offerTrd));
        assertTrue(Iterables.contains(offers, offerFth));
    }

    @Test
    public void testDeleteByIdentifierCascadingOffers() {
        // Test data
        User userFst = createDistinctTestEntity();
        User userSnd = createDistinctTestEntity();

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Offer offerFst = createDistinctRepoOffer(postedUserFst);
        Offer offerSnd = createDistinctRepoOffer(postedUserFst);
        Offer offerTrd = createDistinctRepoOffer(postedUserSnd);
        Offer offerFth = createDistinctRepoOffer(postedUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(offerService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst.getIdentifier());

        // Assertions
        Iterable<Offer> offers = offerService.getAll();
        assertEquals(2, Iterables.size(offers));
        assertFalse(Iterables.contains(offers, offerFst));
        assertFalse(Iterables.contains(offers, offerSnd));
        assertTrue(Iterables.contains(offers, offerTrd));
        assertTrue(Iterables.contains(offers, offerFth));
    }

    @Test
    public void testDeleteByEntityCascadingTokens() {
        // Test data
        User userFst = createDistinctTestEntity();
        LoginCredential loginCredentialUserFst = new LoginCredential(userFst.getEmail(), userFst.getPassword());
        User userSnd = createDistinctTestEntity();
        LoginCredential loginCredentialUserSnd = new LoginCredential(userSnd.getEmail(), userSnd.getPassword());

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Token tokenFst = tokenService.createToken(loginCredentialUserFst);
        Token tokenSnd = tokenService.createToken(loginCredentialUserFst);
        Token tokenTrd = tokenService.createToken(loginCredentialUserSnd);
        Token tokenFth = tokenService.createToken(loginCredentialUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(tokenService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst);

        // Assertions
        Iterable<Token> tokens = tokenService.getAll();
        assertEquals(2, Iterables.size(tokens));
        assertFalse(Iterables.contains(tokens, tokenFst));
        assertFalse(Iterables.contains(tokens, tokenSnd));
        assertTrue(Iterables.contains(tokens, tokenTrd));
        assertTrue(Iterables.contains(tokens, tokenFth));
    }

    @Test
    public void testDeleteByIdentifierCascadingTokens() {
        // Test data
        User userFst = createDistinctTestEntity();
        LoginCredential loginCredentialUserFst = new LoginCredential(userFst.getEmail(), userFst.getPassword());
        User userSnd = createDistinctTestEntity();
        LoginCredential loginCredentialUserSnd = new LoginCredential(userSnd.getEmail(), userSnd.getPassword());

        // Execution: Pre-Deletion
        User postedUserFst = getEntityService().post(userFst);
        User postedUserSnd = getEntityService().post(userSnd);
        Token tokenFst = tokenService.createToken(loginCredentialUserFst);
        Token tokenSnd = tokenService.createToken(loginCredentialUserFst);
        Token tokenTrd = tokenService.createToken(loginCredentialUserSnd);
        Token tokenFth = tokenService.createToken(loginCredentialUserSnd);

        // Assertions: Pre-Deletion
        assertEquals(4, Iterables.size(tokenService.getAll()));

        // Execution
        getEntityService().delete(postedUserFst.getIdentifier());

        // Assertions
        Iterable<Token> tokens = tokenService.getAll();
        assertEquals(2, Iterables.size(tokens));
        assertFalse(Iterables.contains(tokens, tokenFst));
        assertFalse(Iterables.contains(tokens, tokenSnd));
        assertTrue(Iterables.contains(tokens, tokenTrd));
        assertTrue(Iterables.contains(tokens, tokenFth));
    }

    @Test(expected = NullPointerException.class)
    public void testGetByEmailNull() {
        // Execution
        getEntityService().getByEmail(null);
    }

    @Test
    public void testGetByEmail() {
        // Test data
        Email email = new Email("valid.meet.eat@example.com");
        User user = createDistinctUser(email);

        // Execution
        User postedUser = getEntityService().post(user);
        User gotUser = getEntityService().getByEmail(email).orElseThrow();

        // Assertions
        assertEquals(postedUser, gotUser);
    }

    @Override
    protected User createDistinctTestEntity() {
        Email email = new Email("noreply" + userCount + ".meet.eat@example.com");
        return createDistinctUser(email);
    }

    private User createDistinctUser(Email email) {
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
