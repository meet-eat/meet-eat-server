package meet_eat.server.controller;

import meet_eat.data.entity.Token;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController extends EntityController<Token, String, TokenService> {

}
