package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.server.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService extends EntityService<Offer, String, OfferRepository> {

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        super(offerRepository);
    }
}
