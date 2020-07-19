package meet_eat.server.controller;

import meet_eat.data.entity.Offer;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfferController extends EntityController<Offer, String, EntityService<Offer, String>> {

}
