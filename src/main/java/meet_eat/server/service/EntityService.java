package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class EntityService<T extends Entity, U, K extends MongoRepository<T, U>> {

    private final K repository;

    protected EntityService(K repository) {
        this.repository = repository;
    }

    public Iterable<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> get(U identifier) {
        return repository.findById(identifier);
    }

    public T post(T entity) {
        return repository.insert(entity);
    }

    public T put(T entity) {
        return repository.save(entity);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void delete(U identifier) {
        repository.deleteById(identifier);
    }

    public K getRepository() {
        return repository;
    }
}
