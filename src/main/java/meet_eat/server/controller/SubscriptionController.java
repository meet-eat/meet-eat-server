package meet_eat.server.controller;

import com.google.common.collect.Streams;
import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Subscription;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.SubscriptionService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
public class SubscriptionController extends EntityController<Subscription, String, SubscriptionService> {

    @Lazy
    @Autowired
    public SubscriptionController(SubscriptionService entityService, SecurityService<Subscription> securityService) {
        super(entityService, securityService);
    }

    // GET

    @GetMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.SUBSCRIPTIONS)
    public ResponseEntity<Iterable<Subscription>> getSubscriptionsByUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                                         @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        // Check if authentication is valid
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isValidAuthentication(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get the subscriptions of the user
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUser(userIdentifier);
        if (optionalSubscriptions.isEmpty()) {
            System.out.println("No subscriptions found for " + userIdentifier);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalSubscriptions.get(), HttpStatus.OK);
    }

    // Post

    @PostMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.SUBSCRIPTIONS)
    public ResponseEntity<Subscription> postSubscription(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                         @RequestBody Subscription subscription,
                                                         @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (!userIdentifier.equals(subscription.getSourceUser().getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return handlePost(subscription, token);
    }

    // Delete

    @DeleteMapping(EndpointPath.USERS + URI_PATH_SEGMENT_IDENTIFIER + EndpointPath.SUBSCRIPTIONS)
    public ResponseEntity<Void> deleteSubscriptionBySubscribedUser(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String userIdentifier,
                                                                   @RequestBody User subscribedUser,
                                                                   @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        // Get subscriptions of identified user
        Optional<Iterable<Subscription>> optionalSubscriptions = getEntityService().getBySourceUser(userIdentifier);
        if (optionalSubscriptions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Get the subscription to be deleted
        Optional<Subscription> optionalSubscription = Streams.stream(optionalSubscriptions.get())
                .filter(x -> x.getTargetUser().equals(subscribedUser))
                .findFirst();

        // Delete if user's subscriptions found and return
        if (optionalSubscription.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return handleDelete(optionalSubscription.get(), token);
    }
}
