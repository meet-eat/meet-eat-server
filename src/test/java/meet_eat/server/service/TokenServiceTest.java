package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TokenServiceTest extends EntityServiceTest<TokenService, Token, String> {

    private static final Password PASSWORD_VALID = Password.createHashedPassword("ABCDEFGhijkl1234!");
    private static final Localizable LOCALIZABLE_VALID = new SphericalLocation(new SphericalPosition(0, 0));
    private static final List<User> USERS = Arrays.asList(
            new User(new Email("noreply1.meet.eat@gmail.com"), PASSWORD_VALID, LocalDate.of(1990, Month.JANUARY, 1), "User 1", "12345", "Description1", true, LOCALIZABLE_VALID),
            new User(new Email("noreply2.meet.eat@gmail.com"), PASSWORD_VALID, LocalDate.of(1980, Month.JANUARY, 1), "User 2", "67890", "Description2", false, LOCALIZABLE_VALID),
            new User(new Email("noreply3.meet.eat@gmail.com"), PASSWORD_VALID, LocalDate.of(1982, Month.FEBRUARY, 12), "User 3", "10293", "Description3", false, LOCALIZABLE_VALID)
    );
    private static int tokenCount = 0;
    private static boolean isClassInitialized = false;

    @Autowired
    private UserService userService;

    @Before
    public void prepareUserRepository() {
        if (!isClassInitialized) {
            userService.getRepository().deleteAll();
            USERS.forEach(userService::post);
            isClassInitialized = true;
        }
    }

    //#region @Test createToken

    @Test
    public void testCreateToken() {
        // Test data
        User user = getRepoUserShuffled();
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), PASSWORD_VALID);

        // Execution
        Token token = getEntityService().createToken(loginCredential);

        // Assertions
        assertNotNull(token);
        assertNotNull(token.getIdentifier());
        assertNotNull(token.getUser());
        assertNotNull(token.getValue());
        assertTrue(getEntityService().getRepository().existsById(token.getIdentifier()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTokenUnknownEmail() {
        // Test data
        LoginCredential loginCredential = new LoginCredential(new Email("moritz@gstuer.com"), PASSWORD_VALID);

        // Execution
        getEntityService().createToken(loginCredential);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTokenWrongPassword() {
        // Test data
        User user = getRepoUserShuffled();
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), Password.createHashedPassword("Invalid!PASSWORD123"));

        // Execution
        getEntityService().createToken(loginCredential);
    }


    @Test(expected = NullPointerException.class)
    public void testCreateTokenNull() {
        // Execution
        Token token = getEntityService().createToken(null);
    }

    //#endregion

    //#region @Test isValidLoginCredential

    @Test
    public void testIsValidLoginCredential() {
        // Test data
        User user = getRepoUserShuffled();
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), PASSWORD_VALID);

        // Assertions
        assertTrue(getEntityService().isValidLoginCredential(loginCredential));
    }

    @Test
    public void testIsValidLoginCredentialWithWrongPassword() {
        // Test data
        User user = getRepoUserShuffled();
        Password wrongPassword = Password.createHashedPassword("abcdefgAb123!");
        LoginCredential loginCredential = new LoginCredential(user.getEmail(), wrongPassword);

        // Assertions
        assertFalse(getEntityService().isValidLoginCredential(loginCredential));
    }

    @Test
    public void testIsValidLoginCredentialWithUnknownEmail() {
        // Test data
        LoginCredential loginCredential = new LoginCredential(new Email("unknown@example.com"), PASSWORD_VALID);

        // Assertions
        assertFalse(getEntityService().isValidLoginCredential(loginCredential));
    }

    @Test
    public void testIsValidLoginCredentialNull() {
        // Assertions
        assertFalse(getEntityService().isValidLoginCredential(null));
    }

    //#endregion

    //#region @Test isValidToken

    @Test
    public void testIsValidToken() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());

        // Assertions
        assertTrue(getEntityService().isValidToken(token));
    }

    @Test
    public void testIsValidTokenDeleted() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());
        getEntityService().delete(token);

        // Assertions
        assertFalse(getEntityService().isValidToken(token));
    }

    @Test
    public void testIsValidTokenNull() {
        // Assertions
        assertFalse(getEntityService().isValidToken(null));
    }

    @Test
    public void testIsValidTokenWithoutIdentifier() {
        // Execution
        Token token = createDistinctTestEntity();

        // Assertions
        assertFalse(getEntityService().isValidToken(token));
    }

    @Test
    public void testIsValidTokenModifiedValue() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());
        Token modifiedToken = new Token(token.getIdentifier(), token.getUser(), token.getValue() + "TestModify");

        // Assertions
        assertTrue(getEntityService().isValidToken(token));
        assertFalse(getEntityService().isValidToken(modifiedToken));
    }

    @Test
    public void testIsValidTokenModifiedUser() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());
        User otherUser = getRepoUserShuffled();
        while (otherUser.equals(token.getUser())) {
            otherUser = getRepoUserShuffled();
        }
        Token modifiedToken = new Token(token.getIdentifier(), otherUser, token.getValue());

        // Assertions
        assertTrue(getEntityService().isValidToken(token));
        assertFalse(getEntityService().isValidToken(modifiedToken));
    }

    //#endregion

    //#region @Test deleteByUser

    @Test
    public void testDeleteByUserEntity() {
        // Test data
        User user = getRepoUserShuffled();
        Token tokenFst = new Token(user, "ABC");
        Token tokenSnd = new Token(user, "EFG");

        // Execution
        Token postedTokenFst = getEntityService().post(tokenFst);
        Token postedTokenSnd = getEntityService().post(tokenSnd);
        getEntityService().deleteByUser(user);

        // Assertions: Post-Deletion
        assertTrue(Iterables.isEmpty(getEntityService().getAll()));
        assertFalse(getEntityService().exists(postedTokenFst.getIdentifier()));
        assertFalse(getEntityService().exists(postedTokenSnd.getIdentifier()));
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByUserEntityNull() {
        // Execution
        getEntityService().deleteByUser((User) null);
    }

    @Test
    public void testDeleteByUserIdentifier() {
        // Test data
        User user = getRepoUserShuffled();
        Token tokenFst = new Token(user, "ABC");
        Token tokenSnd = new Token(user, "EFG");

        // Execution
        Token postedTokenFst = getEntityService().post(tokenFst);
        Token postedTokenSnd = getEntityService().post(tokenSnd);
        getEntityService().deleteByUser(user.getIdentifier());

        // Assertions: Post-Deletion
        assertTrue(Iterables.isEmpty(getEntityService().getAll()));
        assertFalse(getEntityService().exists(postedTokenFst.getIdentifier()));
        assertFalse(getEntityService().exists(postedTokenSnd.getIdentifier()));
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByUserIdentifierNull() {
        // Execution
        getEntityService().deleteByUser((String) null);
    }

    //#endregion

    @Override
    protected Token createDistinctTestEntity() {
        User user = getRepoUserShuffled();
        return new Token(user, String.valueOf(tokenCount++));
    }

    private User getRepoUserShuffled() {
        List<User> users = userService.getRepository().findAll();
        Collections.shuffle(users);
        return users.stream().findFirst().orElseThrow(IllegalStateException::new);
    }
}
