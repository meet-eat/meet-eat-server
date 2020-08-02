package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Service
public abstract class EntityService<T extends Entity<U>, U extends Serializable, K extends MongoRepository<T, U>> {

    private final K repository;

    protected EntityService(K repository) {
        this.repository = repository;
    }

    public boolean exists(U identifier) {
        return Objects.nonNull(identifier) && repository.existsById(identifier);
    }

    public Iterable<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> get(U identifier) {
        return repository.findById(Objects.requireNonNull(identifier));
    }

    public T post(T entity) {
        Objects.requireNonNull(entity);
        if (existsPostConflict(entity)) {
            throw new EntityConflictException();
        }
        return repository.insert(entity);
    }

    public T put(T entity) {
        Objects.requireNonNull(entity);
        if (existsPutConflict(entity)) {
            throw new EntityConflictException();
        }
        return repository.save(entity);
    }

    public void delete(T entity) {
        repository.delete(Objects.requireNonNull(entity));
    }

    public void delete(U identifier) {
        repository.deleteById(Objects.requireNonNull(identifier));
    }

    public K getRepository() {
        return repository;
    }

    public boolean existsPostConflict(T entity) {
        return exists(entity.getIdentifier());
    }

    public boolean existsPutConflict(T entity) {
        return false;
    }
}
