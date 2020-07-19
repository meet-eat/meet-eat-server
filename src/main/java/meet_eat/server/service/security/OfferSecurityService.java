package meet_eat.server.service.security;

import meet_eat.data.entity.Offer;
import org.springframework.stereotype.Service;

@Service
public class OfferSecurityService extends SecurityService<Offer> {

    @Override
    public Offer anonymiseEntity(Offer entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalEntityOperation(Offer entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
