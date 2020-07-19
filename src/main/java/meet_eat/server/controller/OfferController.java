package meet_eat.server.controller;

import meet_eat.data.entity.Offer;
import meet_eat.server.service.OfferService;
import meet_eat.server.service.security.OfferSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfferController extends EntityController<Offer, String, OfferService> {

    @Autowired
    public OfferController(OfferService offerService, OfferSecurityService offerSecurityService) {
        super(offerService, offerSecurityService);
    }
}
