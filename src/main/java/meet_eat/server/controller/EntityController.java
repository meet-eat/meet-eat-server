package meet_eat.server.controller;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Token;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
public abstract class EntityController<T extends Entity<U>, U, K extends EntityService<T, U, ? extends MongoRepository<T, U>>> {

    private final K entityService;
    private final SecurityService<T> securityService;

    protected EntityController(K entityService, SecurityService<T> securityService) {
        this.entityService = entityService;
        this.securityService = securityService;
    }

    protected ResponseEntity<T> handleGet(U identifier, Token token) {
        if (Objects.isNull(identifier)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<T> optionalEntity = getEntityService().get(identifier);
        if (optionalEntity.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalEntity.get(), HttpStatus.OK);
    }

    protected ResponseEntity<Iterable<T>> handleGetAll(Token token) {
        if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalGet(token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Iterable<T> entities = getEntityService().getAll();
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    protected ResponseEntity<T> handlePost(T entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalPost(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (getEntityService().exists(entity.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        T postedTag = getEntityService().post(entity);
        return new ResponseEntity<>(postedTag, HttpStatus.CREATED);
    }

    protected ResponseEntity<T> handlePut(T entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalPut(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (!getEntityService().exists(entity.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        T puttedTag = getEntityService().put(entity);
        return new ResponseEntity<>(puttedTag, HttpStatus.OK);
    }

    protected ResponseEntity<Void> handleDelete(T entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalDelete(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (!getEntityService().exists(entity.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        getEntityService().delete(entity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    protected ResponseEntity<Void> handleDelete(U identifier, Token token) {
        Optional<T> optionalEntity = getEntityService().get(identifier);
        if (optionalEntity.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return handleDelete(optionalEntity.get(), token);
    }

    public K getEntityService() {
        return entityService;
    }

    public SecurityService<T> getSecurityService() {
        return securityService;
    }
}
