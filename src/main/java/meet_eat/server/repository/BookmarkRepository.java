package meet_eat.server.repository;

import meet_eat.data.entity.Bookmark;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Represents a repository managing persistence of {@link Bookmark} instances.
 */
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

    /**
     * Finds and returns all {@link Bookmark bookmarks} containing a specific {@link User user}.
     *
     * @param user the user of the searched bookmarks
     * @return all bookmarks containing a specific user
     */
    public Iterable<Bookmark> findByUser(User user);

    /**
     * Finds and returns a {@link Bookmark bookmark} containing a specific {@link User user} and {@link Offer offer}.
     *
     * @param user  the user of the searched bookmark
     * @param offer the offer of the searched bookmark
     * @return a bookmark containing a specific user and offer
     */
    public Optional<Bookmark> findByUserAndOffer(User user, Offer offer);

    /**
     * Signalizes whether a {@link Bookmark bookmark} containing a specific {@link User user} and {@link Offer offer}
     * exists or not.
     *
     * @param user  the user of the searched bookmark
     * @param offer the offer of the searched bookmark
     * @return True if a bookmark with the given user and offer exists, false otherwise.
     */
    public boolean existsByUserAndOffer(User user, Offer offer);

    /**
     * Deletes all {@link Bookmark bookmarks} containing a specific {@link User user}.
     *
     * @param user the user of the bookmarks to be deleted
     */
    public void deleteByUser(User user);

    /**
     * Deletes all {@link Bookmark bookmarks} containing a specific {@link Offer offer}.
     *
     * @param offer the offer of the bookmarks to be deleted
     */
    public void deleteByOffer(Offer offer);
}
