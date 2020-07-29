package meet_eat.server.service;

import static org.junit.Assert.*;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TokenServiceTest extends EntityServiceTest<TokenService, Token, String> {

    private static final Password PASSWORD_VALID = Password.createHashedPassword("ABCDEFGhijkl1234!");
    private static final List<User> USERS = Arrays.asList(
            new User(new Email("noreply1.meet.eat@gmail.com"), PASSWORD_VALID, LocalDate.of(1990, Month.JANUARY, 1), "User 1", "12345", "Description1", true),
            new User(new Email("noreply2.meet.eat@gmail.com"), PASSWORD_VALID, LocalDate.of(1980, Month.JANUARY, 1), "User 2", "67890", "Description2", false),
            new User(new Email("noreply3.meet.eat@gmail.com"), PASSWORD_VALID, LocalDate.of(1982, Month.FEBRUARY, 12), "User 3", "10293", "Description3", false)
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
    public void testIsValidToken() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());

        // Assertions
        assertTrue(getEntityService().isValidToken(token));

    }

    @Test
    public void testIsInvalidWhenModifyingToken() {
        // Execution
        Token token = getEntityService().post(createDistinctTestEntity());
        Token modifiedToken = new Token(token.getIdentifier(), token.getUser(), token.getValue() + "TestModify");

        // Assertions
        assertTrue(getEntityService().isValidToken(token));
        assertFalse(getEntityService().isValidToken(modifiedToken));

    }

    @Override
    protected Token createDistinctTestEntity() {
        User user = getRepoUserShuffled();
        System.out.println(user.getIdentifier());
        return new Token(user, String.valueOf(tokenCount++));
    }

    private User getRepoUserShuffled() {
        List<User> users = userService.getRepository().findAll();
        Collections.shuffle(users);
        return users.stream().findFirst().orElseThrow(IllegalStateException::new);
    }
}
