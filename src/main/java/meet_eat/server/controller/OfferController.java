package meet_eat.server.controller;

import meet_eat.data.entity.Offer;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfferController extends EntityController<Offer, String, EntityService<Offer, String>> {

    @Autowired
    public OfferController(EntityService<Offer, String> entityService, SecurityService<Offer> securityService) {
        super(entityService, securityService);
    }
}
