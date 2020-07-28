package meet_eat.server.service;

import meet_eat.data.entity.Tag;
import meet_eat.server.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TagService extends EntityService<Tag, String, TagRepository> {

    @Lazy
    @Autowired
    public TagService(TagRepository tagRepository) {
        super(tagRepository);
    }
}
