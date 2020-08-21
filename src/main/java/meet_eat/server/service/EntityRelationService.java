package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.relation.EntityRelation;
import meet_eat.server.repository.EntityRelationRepository;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public abstract class EntityRelationService<K extends EntityRelation<T, S, UK>,
        T extends Entity<UT>, S extends Entity<US>,
        UK extends Serializable, UT extends Serializable, US extends Serializable,
        R extends EntityRelationRepository<K, T, S, UK>,
        ST extends EntityService<T, UT, ?>, SS extends EntityService<S, US, ?>>
        extends EntityService<K, UK, R> {

    private final ST sourceService;
    private final SS targetService;

    protected EntityRelationService(R repository, ST sourceService, SS targetService) {
        super(repository);
        this.sourceService = sourceService;
        this.targetService = targetService;
    }

    public Iterable<K> getBySource(T source) {
        return getRepository().findBySource(Objects.requireNonNull(source));
    }

    public Iterable<K> getByTarget(S target) {
        return getRepository().findByTarget(Objects.requireNonNull(target));
    }

    public void deleteBySource(T source) {
        getRepository().deleteBySource(Objects.requireNonNull(source));
    }

    public void deleteByTarget(S target) {
        getRepository().deleteByTarget(Objects.requireNonNull(target));
    }

    public void deleteBySourceIdentifier(UT sourceIdentifier) {
        Optional<T> optionalSource = sourceService.get(Objects.requireNonNull(sourceIdentifier));
        optionalSource.ifPresent(this::deleteBySource);
    }

    public void deleteByTargetIdentifier(US targetIdentifier) {
        Optional<S> optionalTarget = targetService.get(Objects.requireNonNull(targetIdentifier));
        optionalTarget.ifPresent(this::deleteByTarget);
    }

    public void deleteBySourceAndTarget(T source, S target) {
        getRepository().deleteBySourceAndTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    public void deleteBySourceOrTarget(T source, S target) {
        getRepository().deleteBySourceOrTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    public boolean existsBySourceAndTarget(T source, S target) {
        return getRepository().existsBySourceAndTarget(Objects.requireNonNull(source), Objects.requireNonNull(target));
    }

    public ST getSourceService() {
        return sourceService;
    }

    public SS getTargetService() {
        return targetService;
    }
}
