package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntityService<T extends Entity, U> {

    private final MongoRepository<T, U> entityRepository;

    @Autowired
    public EntityService(MongoRepository<T, U> entityRepository) {
        this.entityRepository = entityRepository;
    }

    public Iterable<T> getAll() {
        return entityRepository.findAll();
    }

    public Optional<T> get(U identifier) {
        return entityRepository.findById(identifier);
    }

    public T post(T entity) {
        return entityRepository.insert(entity);
    }

    public T put(T entity) {
        return entityRepository.save(entity);
    }

    public void delete(T entity) {
        entityRepository.delete(entity);
    }

    public void delete(U identifier) {
        entityRepository.deleteById(identifier);
    }
}
