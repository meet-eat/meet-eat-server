package meet_eat.server.repository;

import meet_eat.data.entity.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {

}
