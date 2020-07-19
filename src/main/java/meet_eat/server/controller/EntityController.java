package meet_eat.server.controller;

import meet_eat.data.entity.Entity;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class EntityController<T extends Entity, U, K extends EntityService<T, U>> {

    private final K entityService;
    private final SecurityService<T> securityService;

    protected EntityController(K entityService, SecurityService<T> securityService) {
        this.entityService = entityService;
        this.securityService = securityService;
    }
}
