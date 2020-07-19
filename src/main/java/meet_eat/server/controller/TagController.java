package meet_eat.server.controller;

import meet_eat.data.entity.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController extends EntityController<Tag, String, EntityService<Tag, String>> {

}
