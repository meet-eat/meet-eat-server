package meet_eat.server.controller;

import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
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

import java.util.Objects;
import java.util.Optional;

@RestController
public class OfferController extends EntityController<Offer, String, OfferService> {

    protected static final String REQUEST_PARAM_OWNER = "owner";

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
    public ResponseEntity<Iterable<Offer>> getOffersByCreator(@RequestParam(value = REQUEST_PARAM_OWNER) String creatorIdentifier,
                                                              @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Iterable<Offer>> optionalOffers = getEntityService().getByCreatorId(creatorIdentifier);
        if (optionalOffers.isEmpty()) {
            // Indicating that the given creatorId does not exist in the user repository.
            // Therefore, no resource could be found.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalOffers.get(), HttpStatus.OK);
    }

    // POST

    @PostMapping(EndpointPath.OFFERS)
    public ResponseEntity<Offer> postOffer(@RequestBody Offer offer,
                                           @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(offer, token);
    }

    // PUT

    @PutMapping(EndpointPath.OFFERS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Offer> putOffer(@PathVariable(value = PATH_VARIABLE_IDENTIFIER, required = false) String identifier,
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
}
