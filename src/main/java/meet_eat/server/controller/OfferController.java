package meet_eat.server.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import meet_eat.data.EndpointPath;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.User;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.server.service.OfferService;
import meet_eat.server.service.security.OfferSecurityService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class OfferController extends EntityController<Offer, String, OfferService> {

    protected static final String REQUEST_PARAM_OWNER = "owner";
    protected static final String REQUEST_PARAM_SUBSCRIBER = "subscriber";
    private static final String URI_PATH_SEGMENT_PARTICIPANTS = "/participants";

    @Autowired
    public OfferController(OfferService offerService, OfferSecurityService offerSecurityService) {
        super(offerService, offerSecurityService);
    }

    // GET

    @GetMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Offer> getOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGet(identifier, token);
    }

    @GetMapping(EndpointPath.OFFERS)
    public ResponseEntity<Iterable<Offer>> getAllOffers(
            @RequestParam(value = REQUEST_PARAM_OWNER, required = false) String creatorIdentifier,
            @RequestParam(value = REQUEST_PARAM_SUBSCRIBER, required = false) String subscriberIdentifier,
            @RequestHeader(value = RequestHeaderField.PREDICATES, required = false) OfferPredicate[] predicates,
            @RequestHeader(value = RequestHeaderField.COMPARATORS, required = false) OfferComparator comparator,
            @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {

        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get all offers (by certain creator if given).
        Iterable<Offer> offers;
        if (Objects.nonNull(creatorIdentifier) || Objects.nonNull(subscriberIdentifier)) {
            // Avoid duplicates for creator and subscriber by using a set.
            Set<Offer> offerSet = new HashSet<>();

            // Get all offers of a certain identified creator
            if (Objects.nonNull(creatorIdentifier)) {
                Optional<Iterable<Offer>> optionalOffers = getEntityService().getByCreatorId(creatorIdentifier);
                if (optionalOffers.isEmpty()) {
                    // Indicating that the given creatorId does not exist in the user repository.
                    // Therefore, no resource could be found.
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                offerSet.addAll(Lists.newLinkedList(optionalOffers.get()));
            }

            // Get all offers of users subscribed by the identified "subscriber" user
            if (Objects.nonNull(subscriberIdentifier)) {
                Optional<Iterable<Offer>> optionalOffers = getEntityService().getBySubscriberIdentifier(subscriberIdentifier);
                if (optionalOffers.isEmpty()) {
                    // Analogous to the non-existence of a creator.
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                offerSet.addAll(Lists.newLinkedList(optionalOffers.get()));
            }

            // Write offers back to iterable
            offers = offerSet;
        } else {
            offers = getEntityService().getAll();
        }

        // Filter the offers with given predicates.
        if (Objects.nonNull(predicates)) {
            offers = filterOffers(offers, predicates);
        }

        // Sort the offers with a given comparator
        if (Objects.nonNull(comparator)) {
            List<Offer> offerList = Lists.newArrayList(offers);
            offerList.sort(comparator);
            offers = offerList;
        }

        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    // POST

    @PostMapping(EndpointPath.OFFERS)
    public ResponseEntity<Offer> postOffer(@RequestBody Offer offer,
                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(offer, token);
    }

    @PostMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER + URI_PATH_SEGMENT_PARTICIPANTS)
    public ResponseEntity<Offer> postParticipant(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                                 @RequestBody User participant,
                                                 @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        // Check if request is authenticated correctly
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (Objects.isNull(participant.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (!getSecurityService().isValidAuthentication(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (!token.getUser().getIdentifier().equals(participant.getIdentifier())) {
            // Avoid adding of other users with foreign token.
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Fetch the identified offer
        Optional<Offer> optionalOffer = getEntityService().get(identifier);
        if (optionalOffer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Offer offer = optionalOffer.get();

        // Add participant to offer if there is space left and put it into the repository.
        if (offer.getParticipants().size() >= offer.getMaxParticipants()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        offer.addParticipant(participant);
        return new ResponseEntity<>(getEntityService().put(offer), HttpStatus.CREATED);
    }

    // PUT

    @PutMapping(EndpointPath.OFFERS)
    public ResponseEntity<Offer> putOffer(@RequestBody Offer offer,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(null, offer, token);
    }

    @PutMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Offer> putOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                          @RequestBody Offer offer,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(identifier, offer, token);
    }

    // DELETE

    @DeleteMapping(EndpointPath.OFFERS)
    public ResponseEntity<Void> deleteOffer(@RequestBody Offer offer,
                                            @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(offer, token);
    }

    @DeleteMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Void> deleteOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                            @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(identifier, token);
    }

    @DeleteMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER + URI_PATH_SEGMENT_PARTICIPANTS)
    public ResponseEntity<Offer> deleteParticipant(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                                 @RequestBody User participant,
                                                 @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        // Check if request is authenticated correctly
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isValidAuthentication(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (!token.getUser().getIdentifier().equals(participant.getIdentifier())) {
            // Avoid remove of other users with foreign token.
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Fetch the identified offer
        Optional<Offer> optionalOffer = getEntityService().get(identifier);
        if (optionalOffer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Offer offer = optionalOffer.get();

        // Remove participant if existent and write back
        boolean foundAndDeleted = offer.getParticipants().removeIf(x -> x.getIdentifier().equals(participant.getIdentifier()));
        if (!foundAndDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(getEntityService().put(offer), HttpStatus.OK);
    }

    private static Iterable<Offer> filterOffers(Iterable<Offer> offers, OfferPredicate... predicates) {
        Stream<Offer> offerStream = Streams.stream(offers);
        for (OfferPredicate predicate : predicates) {
            offerStream = offerStream.filter(predicate);
        }
        return offerStream.collect(Collectors.toList());
    }
}
