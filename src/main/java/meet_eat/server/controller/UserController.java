package meet_eat.server.controller;

import meet_eat.data.entity.user.User;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends EntityController<User, String, EntityService<User, String>> {

    @Autowired
    public UserController(EntityService<User, String> entityService, SecurityService<User> securityService) {
        super(entityService, securityService);
    }
}
