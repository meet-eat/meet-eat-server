package meet_eat.server.repository;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {

    public Iterable<Offer> findByCreator(User creator);
}
