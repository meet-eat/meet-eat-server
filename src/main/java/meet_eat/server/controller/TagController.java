package meet_eat.server.controller;

import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.server.service.TagService;
import meet_eat.server.service.security.TagSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController extends EntityController<Tag, String, TagService> {

    @Autowired
    public TagController(TagService tagService, TagSecurityService tagSecurityService) {
        super(tagService, tagSecurityService);
    }

    // GET

    @GetMapping(EndpointPath.TAGS)
    public ResponseEntity<Iterable<Tag>> getAllTags(@RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGetAll(token);
    }

    @GetMapping(EndpointPath.TAGS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Tag> getTag(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                      @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleGet(identifier, token);
    }

    // POST

    @PostMapping(EndpointPath.TAGS)
    public ResponseEntity<Tag> postTag(@RequestBody Tag tag,
                                       @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePost(tag, token);
    }

    // PUT

    @PutMapping(EndpointPath.TAGS)
    public ResponseEntity<Tag> putTag(@RequestBody Tag tag,
                                      @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(null, tag, token);
    }

    @PutMapping(EndpointPath.TAGS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Tag> putTag(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                      @RequestBody Tag tag,
                                      @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handlePut(identifier, tag, token);
    }

    // DELETE

    @DeleteMapping(EndpointPath.TAGS)
    public ResponseEntity<Void> deleteTag(@RequestBody Tag tag,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(tag, token);
    }

    @DeleteMapping(EndpointPath.TAGS + URI_PATH_SEGMENT_IDENTIFIER)
    public ResponseEntity<Void> deleteTag(@PathVariable(value = PATH_VARIABLE_IDENTIFIER) String identifier,
                                          @RequestHeader(value = RequestHeaderField.TOKEN, required = false) Token token) {
        return handleDelete(identifier, token);
    }
}
