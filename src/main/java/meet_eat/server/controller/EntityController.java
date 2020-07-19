package meet_eat.server.controller;

import meet_eat.data.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class EntityController<T extends Entity, U, K extends EntityService<T, U>> {

    @Autowired
    private final K entityService;

    @Autowired
    private final SecurityService<T> securityService;

    protected EntityController(K entityService, SecurityService<T> securityService) {
        this.entityService = entityService;
        this.securityService = securityService;
    }
}
