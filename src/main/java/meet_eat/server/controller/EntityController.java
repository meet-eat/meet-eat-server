package meet_eat.server.controller;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Token;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.server.HeaderPropertyEditor;
import meet_eat.server.service.EntityService;
import meet_eat.server.service.security.SecurityService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@RestController
public abstract class EntityController<T extends Entity<U>, U extends Serializable, K extends EntityService<T, U, ? extends MongoRepository<T, U>>> {

    protected static final String PATH_VARIABLE_IDENTIFIER = "identifier";
    protected static final String URI_PATH_SEGMENT_IDENTIFIER = "/{" + PATH_VARIABLE_IDENTIFIER + "}";

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
        } else if (getEntityService().existsPostConflict(entity)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        T postedEntity = getEntityService().post(entity);
        return new ResponseEntity<>(postedEntity, HttpStatus.CREATED);
    }

    protected ResponseEntity<T> handlePut(U identifier, T entity, Token token) {
        if (Objects.isNull(entity)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if ((Objects.nonNull(identifier) && !identifier.equals(entity.getIdentifier()))
                || getEntityService().existsPutConflict(entity)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (Objects.isNull(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!getSecurityService().isLegalPut(entity, token)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (!getEntityService().exists(entity.getIdentifier())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        T puttedEntity = getEntityService().put(entity);
        return new ResponseEntity<>(puttedEntity, HttpStatus.OK);
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Token.class, new HeaderPropertyEditor(Token.class));
        binder.registerCustomEditor(OfferPredicate.class, new HeaderPropertyEditor(OfferPredicate.class));
    }
}
