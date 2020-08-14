package meet_eat.server.service;

import meet_eat.data.entity.Bookmark;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Bookmark bookmarks} and their state
 * persistence.
 */
@Service
public class BookmarkService extends EntityService<Bookmark, String, BookmarkRepository> {

    private final UserService userService;
    private final OfferService offerService;

    /**
     * Constructs a new instance of {@link BookmarkService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public BookmarkService(BookmarkRepository repository, UserService userService, OfferService offerService) {
        super(repository);
        this.userService = userService;
        this.offerService = offerService;
    }

    /**
     * Gets all {@link Bookmark bookmarks} containing a specific {@link User user}.
     *
     * @param user the user of the bookmarks to be returned
     * @return all bookmarks containing a specific {@link User user}
     */
    public Iterable<Bookmark> getByUser(User user) {
        return getRepository().findByUser(Objects.requireNonNull(user));
    }

    /**
     * Gets all {@link Bookmark bookmarks} containing a specific {@link User user} identified by
     * identifier.
     *
     * @param userIdentifier the user's identifier
     * @return all bookmarks containing a specific {@link User user}
     */
    public Optional<Iterable<Bookmark>> getByUser(String userIdentifier) {
        Optional<User> optionalCreator = userService.get(userIdentifier);
        return optionalCreator.map(this::getByUser);
    }

    /**
     * Deletes all {@link Bookmark bookmarks} containing a specific {@link Offer offer}.
     *
     * @param offer the offer of the bookmarks to be deleted
     */
    public void deleteByOffer(Offer offer) {
        getRepository().deleteByOffer(Objects.requireNonNull(offer));
    }

    /**
     * Deletes all {@link Bookmark bookmarks} containing a specific {@link Offer offer}.
     *
     * @param offerIdentifier the offer's identifier
     */
    public void deleteByOffer(String offerIdentifier) {
        Optional<Offer> optionalCreator = offerService.get(offerIdentifier);
        optionalCreator.ifPresent(this::deleteByOffer);
    }

    /**
     * Deletes all {@link Bookmark bookmarks} containing a specific {@link User user}.
     *
     * @param user the user of the bookmarks to be deleted
     */
    public void deleteByUser(User user) {
        getRepository().deleteByUser(Objects.requireNonNull(user));
    }

    /**
     * Deletes all {@link Bookmark bookmarks} containing a specific {@link User user}.
     *
     * @param userIdentifier the user's identifier
     */
    public void deleteByUser(String userIdentifier) {
        Optional<User> optionalCreator = userService.get(userIdentifier);
        optionalCreator.ifPresent(this::deleteByUser);
    }

    @Override
    public boolean existsPostConflict(Bookmark entity) {
        return getRepository().existsByUserAndOffer(entity.getUser(), entity.getOffer())
                || super.existsPostConflict(entity);
    }
}
