package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferService extends EntityService<Offer, String> {

    @Autowired
    public OfferService(MongoRepository<Offer, String> entityRepository) {
        super(entityRepository);
    }
}
