package meet_eat.server.service;

import meet_eat.data.Report;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.rating.Rating;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public final class AnonymizationService {

    private AnonymizationService() {
    }

    public static Tag anonymize(Tag tag) {
        return tag;
    }

    public static User anonymize(User user) {
        Collection<Report> reports = user.getReports().stream().map(x -> anonymize(x)).collect(Collectors.toList());
        Collection<Rating> ratings = user.getRatings().stream().map(x -> anonymize(x)).collect(Collectors.toList());
        Set<User> subscriptions = user.getSubscriptions().stream().map(x -> anonymize(x)).collect(Collectors.toSet());
        Set<Offer> bookmarks = user.getBookmarks().stream().map(x -> anonymize(x)).collect(Collectors.toSet());
        Password password = anonymize(user.getPassword());
        return new User(user.getIdentifier(), reports, ratings, subscriptions, user.getSettings(),
                bookmarks, user.getRole(), user.getEmail(), password, user.getBirthDay(), user.getName(),
                user.getPhoneNumber(), user.getDescription(), user.isVerified(), user.getOfferPredicates(),
                user.getOfferComparator(), user.getLocalizable());
    }

    public static Offer anonymize(Offer offer) {
        Collection<Report> reports = offer.getReports().stream().map(x -> anonymize(x)).collect(Collectors.toList());
        User creator = anonymize(offer.getCreator());
        Set<User> participants = offer.getParticipants().stream().map(x -> anonymize(x)).collect(Collectors.toSet());
        Set<Tag> tags = offer.getTags().stream().map(x -> anonymize(x)).collect(Collectors.toSet());
        return new Offer(offer.getIdentifier(), reports, creator, participants, tags, offer.getName(),
                offer.getDescription(), offer.getPrice(), offer.getMaxParticipants(), offer.getDateTime(),
                offer.getLocation());
    }

    public static Token anonymize(Token token) {
        User user = anonymize(token.getUser());
        return new Token(token.getIdentifier(), user, token.getValue());
    }

    private static Rating anonymize(Rating rating) {
        User reviewer = anonymize(rating.getReviewer());
        return new Rating(rating.getBasis(), rating.getValue(), reviewer);
    }

    private static Password anonymize(Password password) {
        return null;
    }

    private static Report anonymize(Report report) {
        User reporter = anonymize(report.getReporter());
        Report anonymizedReport = new Report(reporter, report.getMessage());
        anonymizedReport.setProcessed(report.isProcessed());
        return anonymizedReport;
    }
}
