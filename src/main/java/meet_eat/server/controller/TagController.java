package meet_eat.server.controller;

import meet_eat.data.entity.Tag;
import meet_eat.server.service.TagService;
import meet_eat.server.service.security.TagSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController extends EntityController<Tag, String, TagService> {

    @Autowired
    public TagController(TagService tagService, TagSecurityService tagSecurityService) {
        super(tagService, tagSecurityService);
    }
}
