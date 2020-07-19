package meet_eat.server.service.security;

import meet_eat.data.entity.Token;

public class TokenSecurityService extends SecurityService<Token> {

    @Override
    public Token anonymiseEntity(Token entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalEntityOperation(Token entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
