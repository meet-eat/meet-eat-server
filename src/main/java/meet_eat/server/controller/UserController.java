package meet_eat.server.controller;

import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.UserService;
import meet_eat.server.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Objects;

@RestController
public class UserController extends EntityController<User, String, UserService> {

    private static final String PATH_VARIABLE_EMAIL = "email";
    private static final String URI_PATH_SEGMENT_EMAIL = "/{" + PATH_VARIABLE_EMAIL + "}";

    @Autowired
    public UserController(UserService userService, UserSecurityService userSecurityService) {
        super(userService, userSecurityService);
    }

    // GET

    @GetMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<User> getUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                        @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGet(identifier, token);
    }

    // POST

    @PostMapping(EndpointPath.USERS)
    public ResponseEntity<User> postUser(@RequestBody User user,
                                         @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(user, token);
    }

    @Override
    protected ResponseEntity<User> handlePost(User entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!getSecurityService().isLegalPost(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (getEntityService().exists(entity.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User postedUser = getEntityService().post(entity);
        return new ResponseEntity<>(postedUser, HttpStatus.CREATED);
    }

    @PostMapping(EndpointPath.USERS + URI_PATH_SEGMENT_EMAIL)
    public ResponseEntity<User> postPasswordReset(@PathVariable(value = PATH_VARIABLE_EMAIL) String emailAddress) {
        // No errors are sent to the caller in order to avoid brute force searches identifying valid email addresses.
        if (Email.isLegalEmailAddress(emailAddress)) {
            getEntityService().resetPassword(emailAddress);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // PUT

    @PutMapping(EndpointPath.USERS)
    public ResponseEntity<User> putUser(@RequestBody User user,
                                        @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(null, user, token);
    }

    @PutMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<User> putUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                        @RequestBody User user,
                                        @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(identifier, user, token);
    }

    // DELETE

    @DeleteMapping(EndpointPath.USERS)
    public ResponseEntity<Void> deleteUser(@RequestBody User user,
                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(user, token);
    }

    @DeleteMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Void> deleteUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(identifier, token);
    }
}
