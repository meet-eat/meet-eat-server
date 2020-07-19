package meet_eat.server.service;

import meet_eat.data.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService extends EntityService<Tag, String> {

    @Autowired
    public TagService(MongoRepository<Tag, String> tagRepository) {
        super(tagRepository);
    }
}
