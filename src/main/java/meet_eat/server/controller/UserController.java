package meet_eat.server.controller;

import meet_eat.data.entity.user.User;
import meet_eat.server.service.UserService;
import meet_eat.server.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends EntityController<User, String, UserService> {

    @Autowired
    public UserController(UserService userService, UserSecurityService userSecurityService) {
        super(userService, userSecurityService);
    }
}
