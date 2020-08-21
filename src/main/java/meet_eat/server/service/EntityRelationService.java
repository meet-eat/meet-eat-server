package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.relation.EntityRelation;
import meet_eat.server.repository.EntityRelationRepository;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link EntityRelation entity relations} and their state
 * persistence.
 *
 * @param <K>  the type of the {@link EntityRelation entity relation}
 * @param <T>  the type of the {@link Entity source entity} within the relation
 * @param <S>  the type of the {@link Entity target entity} within the relation
 * @param <UK> the type of the identifier of the {@link EntityRelation entity relation}
 * @param <UT> the type of the identifier of the {@link Entity source entity}
 * @param <US> the type of the identifier of the {@link Entity target entity}
 * @param <R>  the type of the {@link EntityRelation entity relation} {@link EntityRelationRepository repository}
 * @param <ST> the type of the {@link Entity source entity} {@link EntityService service}
 * @param <SS> the type of the {@link Entity target entity} {@link EntityService service}
 */
public abstract class EntityRelationService<K extends EntityRelation<T, S, UK>,
        T extends Entity<UT>, S extends Entity<US>,
        UK extends Serializable, UT extends Serializable, US extends Serializable,
        R extends EntityRelationRepository<K, T, S, UK>,
        ST extends EntityService<T, UT, ?>, SS extends EntityService<S, US, ?>>
        extends EntityService<K, UK, R> {

    private final ST sourceService;
    private final SS targetService;

    /**
     * Constructs a new instance of {@link EntityRelationService}.
     *
     * @param repository    the repository used for persistence operations
     * @param sourceService the service used for operations on and with {@link Entity source entities}
     * @param targetService the service used for operations on and with {@link Entity target entities}
     */
    protected EntityRelationService(R repository, ST sourceService, SS targetService) {
        super(repository);
        this.sourceService = sourceService;
        this.targetService = targetService;
    }

    /**
     * Gets all {@link EntityRelation relations} containing a specific {@link Entity source entity}.
     *
     * @param source the source entity of the relations to be returned
     * @return all relations containing a specific source entity
     */
    public Iterable<K> getBySource(T source) {
        return getRepository().findBySource(Objects.requireNonNull(source));
    }

    /**
     * Gets all {@link EntityRelation relations} containing a specific {@link Entity target entity}.
     *
     * @param target the target entity of the relations to be returned
     * @return all relations containing a specific target entity
     */
    public Iterable<K> getByTarget(S target) {
        return getRepository().findByTarget(Objects.requireNonNull(target));
    }

    /**
     * Deletes all {@link EntityRelation relations} containing a specific {@link Entity source entity}.
     *
     * @param source the source entity of the relations to be deleted
     */
    public void deleteBySource(T source) {
        getRepository().deleteBySource(Objects.requireNonNull(source));
    }

    /**
     * Deletes all {@link EntityRelation relations} containing a specific {@link Entity target entity}.
     *
     * @param target the target entity of the relations to be deleted
     */
    public void deleteByTarget(S target) {
        getRepository().deleteByTarget(Objects.requireNonNull(target));
    }

    /**
     * Deletes all {@link EntityRelation relations} containing a specific {@link Entity source entity}.
     *
     * @param sourceIdentifier the identifier of the source entity of the relations to be deleted
     */
    public void deleteBySourceIdentifier(UT sourceIdentifier) {
        Optional<T> optionalSource = sourceService.get(Objects.requireNonNull(sourceIdentifier));
        optionalSource.ifPresent(this::deleteBySource);
    }

    /**
     * Deletes all {@link EntityRelation relations} containing a specific {@link Entity target entity}.
     *
     * @param targetIdentifier the identifier of the target entity of the relations to be deleted
     */
    public void deleteByTargetIdentifier(US targetIdentifier) {
        Optional<S> optionalTarget = targetService.get(Objects.requireNonNull(targetIdentifier));
        optionalTarget.ifPresent(this::deleteByTarget);
    }

    /**
     * Deletes a {@link EntityRelation relation} containing a specific {@link Entity source entity} and
     * {@link Entity target entity}.
     *
     * @param source the source entity of the relation to be deleted
     * @param target the target entity of the relation to be deleted
     */
    public void deleteBySourceAndTarget(T source, S target) {
        getRepository().deleteBySourceAndTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    /**
     * Deletes a {@link EntityRelation relation} containing a specific {@link Entity source entity} or
     * {@link Entity target entity}.
     *
     * @param source the source entity of the relation to be deleted
     * @param target the target entity of the relation to be deleted
     */
    public void deleteBySourceOrTarget(T source, S target) {
        getRepository().deleteBySourceOrTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    /**
     * Returns whether a {@link EntityRelation relation} containing a specific {@link Entity source entity} and
     * {@link Entity target entity} exists.
     *
     * @param source the source entity of the relation
     * @param target the target entity of the relation
     * @return True if the relation exists, false otherwise.
     */
    public boolean existsBySourceAndTarget(T source, S target) {
        return getRepository().existsBySourceAndTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    /**
     * Gets the {@link EntityService} used for working on and with {@link Entity source entities}.
     *
     * @return the source entity service
     */
    public ST getSourceService() {
        return sourceService;
    }

    /**
     * Gets the {@link EntityService} used for working on and with {@link Entity target entities}.
     *
     * @return the target entity service
     */
    public SS getTargetService() {
        return targetService;
    }
}
