package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.relation.EntityRelation;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class EntityRelationServiceTest<V extends EntityRelationService<K, T, S, U, ?>, K extends EntityRelation<T, S, U>, T extends Entity<?>, S extends Entity<?>, U extends Serializable> extends EntityServiceTest<V, K, U> {

    private static final String PASSWORD_VALID_VALUE = "AbcdefgTest1234!?";

    private static int userCount = 0;
    private static int offerCount = 0;

    @Autowired
    private UserService userService;
    @Autowired
    private OfferService offerService;

    @Before
    public void prepareUserAndOfferRepository() {
        userService.getRepository().deleteAll();
        offerService.getRepository().deleteAll();
    }

    //#region @Test getBySource

    @Test(expected = NullPointerException.class)
    public void testGetBySourceNull() {
        // Assertions
        getEntityService().getBySource(null);
    }

    @Test
    public void testGetBySourceEmpty() {
        // Test data
        T source = getSourceEntity();

        // Execution
        Iterable<K> relations = getEntityService().getBySource(source);

        // Assertions
        assertNotNull(relations);
        assertEquals(0, Iterables.size(relations));
    }

    @Test
    public void testGetBySourceSingleRelation() {
        // Test data
        T source = getSourceEntity();
        K relation = getRelationEntity(source, getTargetEntity());

        // Execution
        Iterable<K> relations = getEntityService().getBySource(source);

        // Assertions
        assertNotNull(relations);
        assertEquals(1, Iterables.size(relations));
        assertTrue(Iterables.contains(relations, relation));
    }

    @Test
    public void testGetBySourceMultipleRelations() {
        // Test data
        T source = getSourceEntity();
        K relationFst = getRelationEntity(source, getTargetEntity());
        K relationSnd = getRelationEntity(source, getTargetEntity());
        K foreignRelation = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Execution
        Iterable<K> relations = getEntityService().getBySource(source);

        // Assertions
        assertNotNull(relations);
        assertEquals(2, Iterables.size(relations));
        assertTrue(Iterables.contains(relations, relationFst));
        assertTrue(Iterables.contains(relations, relationSnd));
        assertFalse(Iterables.contains(relations, foreignRelation));
    }

    //#endregion

    //#region @Test getByTarget

    @Test(expected = NullPointerException.class)
    public void testGetByTargetNull() {
        // Assertions
        getEntityService().getByTarget(null);
    }

    @Test
    public void testGetByTargetEmpty() {
        // Test data
        S target = getTargetEntity();

        // Execution
        Iterable<K> relations = getEntityService().getByTarget(target);

        // Assertions
        assertNotNull(relations);
        assertEquals(0, Iterables.size(relations));
    }

    @Test
    public void testGetByTargetSingleRelation() {
        // Test data
        S target = getTargetEntity();
        K relation = getRelationEntity(getSourceEntity(), target);

        // Execution
        Iterable<K> relations = getEntityService().getByTarget(target);

        // Assertions
        assertNotNull(relations);
        assertEquals(1, Iterables.size(relations));
        assertTrue(Iterables.contains(relations, relation));
    }

    @Test
    public void testGetByTargetMultipleRelations() {
        // Test data
        S target = getTargetEntity();
        K relationFst = getRelationEntity(getSourceEntity(), target);
        K relationSnd = getRelationEntity(getSourceEntity(), target);
        K foreignRelation = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Execution
        Iterable<K> relations = getEntityService().getByTarget(target);

        // Assertions
        assertNotNull(relations);
        assertEquals(2, Iterables.size(relations));
        assertTrue(Iterables.contains(relations, relationFst));
        assertTrue(Iterables.contains(relations, relationSnd));
        assertFalse(Iterables.contains(relations, foreignRelation));
    }

    //#endregion

    //#region @Test countBySource

    @Test(expected = NullPointerException.class)
    public void testCountBySourceNull() {
        // Assertions
        getEntityService().countBySource(null);
    }

    @Test
    public void testCountBySourceEmpty() {
        // Test data
        T source = getSourceEntity();

        // Execution
        long amount = getEntityService().countBySource(source);

        // Assertions
        assertEquals(0, amount);
    }

    @Test
    public void testCountBySourceSingleRelation() {
        // Test data
        T source = getSourceEntity();
        K relation = getRelationEntity(source, getTargetEntity());

        // Execution
        long amount = getEntityService().countBySource(source);

        // Assertions
        assertEquals(1, amount);
    }

    @Test
    public void testCountBySourceMultipleRelations() {
        // Test data
        T source = getSourceEntity();
        K relationFst = getRelationEntity(source, getTargetEntity());
        K relationSnd = getRelationEntity(source, getTargetEntity());
        K foreignRelation = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Execution
        long amount = getEntityService().countBySource(source);

        // Assertions
        assertEquals(2, amount);
    }

    //#endregion

    //#region @Test countByTarget

    @Test(expected = NullPointerException.class)
    public void testCountByTargetNull() {
        // Assertions
        getEntityService().countByTarget(null);
    }

    @Test
    public void testCountByTargetEmpty() {
        // Test data
        S target = getTargetEntity();

        // Execution
        long amount = getEntityService().countByTarget(target);

        // Assertions
        assertEquals(0, amount);
    }

    @Test
    public void testCountByTargetSingleRelation() {
        // Test data
        S target = getTargetEntity();
        K relation = getRelationEntity(getSourceEntity(), target);

        // Execution
        long amount = getEntityService().countByTarget(target);

        // Assertions
        assertEquals(1, amount);
    }

    @Test
    public void testCountByTargetMultipleRelations() {
        // Test data
        S target = getTargetEntity();
        K relationFst = getRelationEntity(getSourceEntity(), target);
        K relationSnd = getRelationEntity(getSourceEntity(), target);
        K foreignRelation = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Execution
        long amount = getEntityService().countByTarget(target);

        // Assertions
        assertEquals(2, amount);
    }

    //#endregion

    //#region @Test deleteBySource

    @Test(expected = NullPointerException.class)
    public void testDeleteBySourceNull() {
        // Assertions
        getEntityService().deleteBySource(null);
    }

    @Test
    public void testDeleteBySourceEmpty() {
        // Test data
        T source = getSourceEntity();

        // Execution
        getEntityService().deleteBySource(source);

        // Assertions
        assertEquals(0, getEntityService().countBySource(source));
        assertEquals(0, Iterables.size(getEntityService().getBySource(source)));
    }

    @Test
    public void testDeleteBySourceSingleRelation() {
        // Test data
        T source = getSourceEntity();
        K relation = getRelationEntity(source, getTargetEntity());

        // Assertions: Pre-Deletion
        assertEquals(1, getEntityService().countBySource(source));
        assertEquals(1, Iterables.size(getEntityService().getBySource(source)));

        // Execution: Deletion
        getEntityService().deleteBySource(source);

        // Assertions: Post-Deletion
        assertEquals(0, getEntityService().countBySource(source));
        assertEquals(0, Iterables.size(getEntityService().getBySource(source)));
    }

    @Test
    public void testDeleteBySourceMultipleRelations() {
        // Test data
        T source = getSourceEntity();
        K relationFst = getRelationEntity(source, getTargetEntity());
        K relationSnd = getRelationEntity(source, getTargetEntity());
        K foreignRelation = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Assertions: Pre-Deletion
        assertEquals(2, getEntityService().countBySource(source));
        assertEquals(2, Iterables.size(getEntityService().getBySource(source)));

        // Execution: Deletion
        getEntityService().deleteBySource(source);

        // Assertions: Post-Deletion
        assertEquals(0, getEntityService().countBySource(source));
        assertEquals(0, Iterables.size(getEntityService().getBySource(source)));
    }

    //#endregion

    //#region @Test deleteByTarget

    @Test(expected = NullPointerException.class)
    public void testDeleteByTargetNull() {
        // Assertions
        getEntityService().deleteByTarget(null);
    }

    @Test
    public void testDeleteByTargetEmpty() {
        // Test data
        S target = getTargetEntity();

        // Execution
        getEntityService().deleteByTarget(target);

        // Assertions
        assertEquals(0, getEntityService().countByTarget(target));
        assertEquals(0, Iterables.size(getEntityService().getByTarget(target)));
    }

    @Test
    public void testDeleteByTargetSingleRelation() {
        // Test data
        S target = getTargetEntity();
        K relation = getRelationEntity(getSourceEntity(), target);

        // Assertions: Pre-Deletion
        assertEquals(1, getEntityService().countByTarget(target));
        assertEquals(1, Iterables.size(getEntityService().getByTarget(target)));

        // Execution: Deletion
        getEntityService().deleteByTarget(target);

        // Assertions: Post-Deletion
        assertEquals(0, getEntityService().countByTarget(target));
        assertEquals(0, Iterables.size(getEntityService().getByTarget(target)));
    }

    @Test
    public void testDeleteByTargetMultipleRelations() {
        // Test data
        S target = getTargetEntity();
        K relationFst = getRelationEntity(getSourceEntity(), target);
        K relationSnd = getRelationEntity(getSourceEntity(), target);
        K foreignRelation = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Assertions: Pre-Deletion
        assertEquals(2, getEntityService().countByTarget(target));
        assertEquals(2, Iterables.size(getEntityService().getByTarget(target)));

        // Execution: Deletion
        getEntityService().deleteByTarget(target);

        // Assertions: Post-Deletion
        assertEquals(0, getEntityService().countByTarget(target));
        assertEquals(0, Iterables.size(getEntityService().getByTarget(target)));
    }

    //#endregion

    //#region @Test deleteBySourceAndTarget

    @Test(expected = NullPointerException.class)
    public void testDeleteBySourceAndTargetNullSource() {
        // Assertions
        getEntityService().deleteBySourceAndTarget(null, getTargetEntity());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteBySourceAndTargetNullTarget() {
        // Assertions
        getEntityService().deleteBySourceAndTarget(getSourceEntity(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteBySourceAndTargetNullSourceAndNullTarget() {
        // Assertions
        getEntityService().deleteBySourceAndTarget(null, null);
    }

    @Test
    public void testDeleteBySourceAndTargetEmpty() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();

        // Execution
        getEntityService().deleteBySourceAndTarget(source, target);

        // Assertions
        assertFalse(getEntityService().existsBySourceAndTarget(source, target));
    }

    @Test
    public void testDeleteBySourceAndTargetSingleRelation() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();
        K relation = getRelationEntity(source, target);

        // Assertions: Pre-Deletion
        assertTrue(getEntityService().existsBySourceAndTarget(source, target));
        assertTrue(getEntityService().exists(relation.getIdentifier()));

        // Execution: Deletion
        getEntityService().deleteBySourceAndTarget(source, target);

        // Assertions: Post-Deletion
        assertFalse(getEntityService().existsBySourceAndTarget(source, target));
        assertFalse(getEntityService().exists(relation.getIdentifier()));
    }

    @Test
    public void testDeleteBySourceAndTargetMultipleRelations() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();
        K relation = getRelationEntity(source, target);
        K foreignRelationFst = getRelationEntity(source, getTargetEntity());
        K foreignRelationSnd = getRelationEntity(getSourceEntity(), target);
        K foreignRelationTrd = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Assertions: Pre-Deletion
        assertTrue(getEntityService().existsBySourceAndTarget(source, target));
        assertTrue(getEntityService().exists(relation.getIdentifier()));

        // Execution: Deletion
        getEntityService().deleteBySourceAndTarget(source, target);

        // Assertions: Post-Deletion
        assertFalse(getEntityService().existsBySourceAndTarget(source, target));
        assertFalse(getEntityService().exists(relation.getIdentifier()));
        assertTrue(getEntityService().exists(foreignRelationFst.getIdentifier()));
        assertTrue(getEntityService().exists(foreignRelationSnd.getIdentifier()));
        assertTrue(getEntityService().exists(foreignRelationTrd.getIdentifier()));
    }

    //#endregion

    //#region @Test deleteBySourceOrTarget

    @Test(expected = NullPointerException.class)
    public void testDeleteBySourceOrTargetNullSource() {
        // Assertions
        getEntityService().deleteBySourceOrTarget(null, getTargetEntity());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteBySourcePrTargetNullTarget() {
        // Assertions
        getEntityService().deleteBySourceOrTarget(getSourceEntity(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteBySourceOrTargetNullSourceAndNullTarget() {
        // Assertions
        getEntityService().deleteBySourceOrTarget(null, null);
    }

    @Test
    public void testDeleteBySourceOrTargetEmpty() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();

        // Execution
        getEntityService().deleteBySourceOrTarget(source, target);

        // Assertions
        assertEquals(0, getEntityService().countBySource(source));
        assertEquals(0, getEntityService().countByTarget(target));
    }

    @Test
    public void testDeleteBySourceOrTargetSingleRelation() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();
        K relation = getRelationEntity(source, target);

        // Assertions: Pre-Deletion
        assertEquals(1, getEntityService().countBySource(source));
        assertEquals(1, getEntityService().countByTarget(target));
        assertTrue(getEntityService().exists(relation.getIdentifier()));

        // Execution: Deletion
        getEntityService().deleteBySourceOrTarget(source, target);

        // Assertions: Post-Deletion
        assertEquals(0, getEntityService().countBySource(source));
        assertEquals(0, getEntityService().countByTarget(target));
        assertFalse(getEntityService().exists(relation.getIdentifier()));
    }

    @Test
    public void testDeleteBySourceOrTargetMultipleRelations() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();
        K relation = getRelationEntity(source, target);
        K foreignRelationFst = getRelationEntity(source, getTargetEntity());
        K foreignRelationSnd = getRelationEntity(getSourceEntity(), target);
        K foreignRelationTrd = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Assertions: Pre-Deletion
        assertEquals(2, getEntityService().countBySource(source));
        assertEquals(2, getEntityService().countByTarget(target));
        assertTrue(getEntityService().exists(relation.getIdentifier()));
        assertTrue(getEntityService().exists(foreignRelationFst.getIdentifier()));
        assertTrue(getEntityService().exists(foreignRelationSnd.getIdentifier()));
        assertTrue(getEntityService().exists(foreignRelationTrd.getIdentifier()));

        // Execution: Deletion
        getEntityService().deleteBySourceOrTarget(source, target);

        // Assertions: Post-Deletion
        assertEquals(0, getEntityService().countBySource(source));
        assertEquals(0, getEntityService().countByTarget(target));
        assertFalse(getEntityService().exists(relation.getIdentifier()));
        assertFalse(getEntityService().exists(foreignRelationFst.getIdentifier()));
        assertFalse(getEntityService().exists(foreignRelationSnd.getIdentifier()));
        assertTrue(getEntityService().exists(foreignRelationTrd.getIdentifier()));
    }

    //#endregion

    //#region @Test existsBySourceAndTarget

    @Test(expected = NullPointerException.class)
    public void testExistsBySourceAndTargetNullSource() {
        // Assertions
        getEntityService().existsBySourceAndTarget(null, getTargetEntity());
    }

    @Test(expected = NullPointerException.class)
    public void testExistsBySourceAndTargetNullTarget() {
        // Assertions
        getEntityService().existsBySourceAndTarget(getSourceEntity(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testExistsBySourceAndTargetNullSourceAndNullTarget() {
        // Assertions
        getEntityService().existsBySourceAndTarget(null, null);
    }

    @Test
    public void testExistsBySourceAndTargetEmpty() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();

        // Assertions
        assertFalse(getEntityService().existsBySourceAndTarget(source, target));
    }

    @Test
    public void testExistsBySourceAndTargetSingleRelation() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();
        K relation = getRelationEntity(source, target);

        // Assertions
        assertTrue(getEntityService().existsBySourceAndTarget(source, target));
    }

    @Test
    public void testExistsBySourceAndTargetMultipleRelations() {
        // Test data
        T source = getSourceEntity();
        S target = getTargetEntity();
        K relation = getRelationEntity(source, target);
        K foreignRelationFst = getRelationEntity(source, getTargetEntity());
        K foreignRelationSnd = getRelationEntity(getSourceEntity(), target);
        K foreignRelationTrd = getRelationEntity(getSourceEntity(), getTargetEntity());

        // Assertions
        assertTrue(getEntityService().existsBySourceAndTarget(source, target));
    }

    //#endregion

    //#region Test environment utility
    protected abstract T getSourceEntity();

    protected abstract S getTargetEntity();

    protected K getRelationEntity(T source, S target) {
        return getEntityService().post(createDistinctTestEntity(source, target));
    }

    protected abstract K createDistinctTestEntity(T source, S target);

    @Override
    protected K createDistinctTestEntity() {
        return createDistinctTestEntity(getSourceEntity(), getTargetEntity());
    }

    protected Offer getValidOffer(User creator) {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JULY, 30, 12, 32);
        Localizable location = new CityLocation("Karlsruhe");
        Set<Tag> tags = new HashSet<>();
        Offer offer = new Offer(creator, tags, "Offer " + offerCount++,
                "Spaghetti. Mhmmm.", 4.99, 3, dateTime, location);
        return offerService.post(offer);
    }

    protected User getBasicUser() {
        return createUser(Role.USER);
    }

    protected User getModeratorUser() {
        return createUser(Role.MODERATOR);
    }

    protected User getAdminUser() {
        return createUser(Role.ADMIN);
    }

    private User createUser(Role role) {
        Email email = new Email("noreply" + userCount + ".meet.eat@example.com");
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        Localizable validLocalizable = new SphericalLocation(new SphericalPosition(0, 0));
        User user = new User(email, password, LocalDate.EPOCH, "User" + userCount, "12345" + userCount,
                "Description" + userCount, true, validLocalizable);
        user.setRole(role);
        userCount++;
        return userService.post(user);
    }
    //#endregion
}
