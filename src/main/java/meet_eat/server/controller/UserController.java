package meet_eat.server.controller;

import meet_eat.data.entity.user.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends EntityController<User, String, EntityService<User, String>> {

}
