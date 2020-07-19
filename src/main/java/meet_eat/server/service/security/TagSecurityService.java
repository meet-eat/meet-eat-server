package meet_eat.server.service.security;

import meet_eat.data.entity.Tag;

public class TagSecurityService extends SecurityService<Tag> {

    @Override
    public Tag anonymiseEntity(Tag entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public boolean isLegalEntityOperation(Tag entity) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
