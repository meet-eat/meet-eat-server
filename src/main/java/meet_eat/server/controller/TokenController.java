package meet_eat.server.controller;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Token;
import meet_eat.server.service.TokenService;
import meet_eat.server.service.security.TokenSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class TokenController extends EntityController<Token, String, TokenService> {

    @Autowired
    public TokenController(TokenService tokenService, TokenSecurityService tokenSecurityService) {
        super(tokenService, tokenSecurityService);
    }

    @PostMapping(EndpointPath.LOGIN)
    public ResponseEntity<Token> login(@RequestBody(required = true) LoginCredential loginCredential) {
        if (!getEntityService().isValidLoginCredential(loginCredential)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Token token = getEntityService().createToken(loginCredential);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping(EndpointPath.LOGOUT)
    public ResponseEntity<Void> logout(@RequestBody(required = true) Token token) {
        getEntityService().delete(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
