package meet_eat.server.service.security;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Token;
import meet_eat.server.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public abstract class SecurityService<T extends Entity> {

    private final TokenService tokenService;

    protected SecurityService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public abstract boolean isLegalGet(Token authenticationToken);

    public abstract boolean isLegalPost(T entity, Token authenticationToken);

    public abstract boolean isLegalPut(T entity, Token authenticationToken);

    public abstract boolean isLegalDelete(T entity, Token authenticationToken);

    public boolean isValidAuthentication(Token authenticationToken) {
        return tokenService.isValidToken(authenticationToken);
    }

    public TokenService getTokenService() {
        return tokenService;
    }
}
