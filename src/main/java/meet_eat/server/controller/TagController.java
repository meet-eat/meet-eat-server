package meet_eat.server.controller;

import meet_eat.data.entity.Tag;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController extends EntityController<Tag, String, EntityService<Tag, String>> {

    @Autowired
    public TagController(EntityService<Tag, String> entityService, SecurityService<Tag> securityService) {
        super(entityService, securityService);
    }
}
