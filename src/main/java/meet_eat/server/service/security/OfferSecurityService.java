package meet_eat.server.service.security;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Role;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class OfferSecurityService extends SecurityService<Offer> {

    @Autowired
    protected OfferSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        return isValidAuthentication(authenticationToken);
    }

    @Override
    public boolean isLegalPost(Offer entity, Token authenticationToken) {
        boolean isEntityCreator = authenticationToken.getUser().getIdentifier().equals(entity.getCreator().getIdentifier());
        return isValidAuthentication(authenticationToken) && isEntityCreator;
    }

    @Override
    public boolean isLegalPut(Offer entity, Token authenticationToken) {
        boolean isAdmin = authenticationToken.getUser().getRole().equals(Role.ADMIN);
        boolean isEntityCreator = authenticationToken.getUser().getIdentifier().equals(entity.getCreator().getIdentifier());

        return isValidAuthentication(authenticationToken) && (isAdmin || isEntityCreator);
    }

    @Override
    public boolean isLegalDelete(Offer entity, Token authenticationToken) {
        return isLegalPut(entity, authenticationToken);
    }
}
