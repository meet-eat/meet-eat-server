package meet_eat.server.controller;

import meet_eat.data.entity.Token;
import meet_eat.server.service.TokenService;
import meet_eat.server.service.security.TokenSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController extends EntityController<Token, String, TokenService> {

    @Autowired
    public TokenController(TokenService tokenService, TokenSecurityService tokenSecurityService) {
        super(tokenService, tokenSecurityService);
    }
}
