package meet_eat.server.service.security;

import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Role;
import meet_eat.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class TagSecurityService extends SecurityService<Tag> {

    @Autowired
    protected TagSecurityService(TokenService tokenService) {
        super(tokenService);
    }

    @Override
    public boolean isLegalGet(Token authenticationToken) {
        return isValidAuthentication(authenticationToken);
    }

    @Override
    public boolean isLegalPost(Tag entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken) && authenticationToken.getUser().getRole().equals(Role.ADMIN);
    }

    @Override
    public boolean isLegalPut(Tag entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken) && authenticationToken.getUser().getRole().equals(Role.ADMIN);
    }

    @Override
    public boolean isLegalDelete(Tag entity, Token authenticationToken) {
        return isValidAuthentication(authenticationToken) && authenticationToken.getUser().getRole().equals(Role.ADMIN);
    }
}
