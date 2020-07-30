package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Entity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class EntityServiceTest<T extends EntityService<S, U, ?>, S extends Entity<U>, U> {

    @Autowired
    private T entityService;

    @Before
    public void prepareTestEnvironment() {
        entityService.getRepository().deleteAll();
    }

    @After
    public void cleanUpTestEnvironment() {
        entityService.getRepository().deleteAll();
    }

    @Test(expected = NullPointerException.class)
    public void testPostNull() {
        // Execution
        entityService.post(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNull() {
        // Execution
        entityService.get(null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNull() {
        // Execution
        entityService.put(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByNullIdentifier() {
        // Execution
        entityService.delete((U) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByNullEntity() {
        // Execution
        entityService.delete((S) null);
    }

    @Test
    public void testExistsNull() {
        // Assertions
        assertFalse(entityService.exists(null));
    }

    @Test
    public void testGetAllEmptyRepository() {
        // Assertions: Pre-Execution
        assertEquals(0L, entityService.getRepository().count());

        // Execution
        Iterable<S> entities = entityService.getAll();

        // Assertions: Post-Execution
        assertTrue(Iterables.isEmpty(entities));
    }

    @Test
    public void testPost() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);

        // Assertions
        assertNotNull(postedEntity);
        assertNotNull(postedEntity.getIdentifier());
    }

    @Test
    public void testPostAndGet() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        Optional<S> optionalGotEntity = getEntityService().get(postedEntity.getIdentifier());

        // Assertions
        assertNotNull(optionalGotEntity);
        assertTrue(optionalGotEntity.isPresent());
        assertEquals(postedEntity, optionalGotEntity.get());
    }

    @Test
    public void testPostAndGetAll() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        Iterable<S> gotEntities = getEntityService().getAll();

        // Assertions
        assertNotNull(gotEntities);
        assertFalse(Iterables.isEmpty(gotEntities));
        assertTrue(Iterables.contains(gotEntities, postedEntity));
    }

    @Test
    public void testPostMultipleAndGetAll() {
        // Test data
        S entityFst = createDistinctTestEntity();
        S entitySnd = createDistinctTestEntity();
        S entityTrd = createDistinctTestEntity();
        int numberOfPosts = 3;

        // Execution
        S postedEntityFst = entityService.post(entityFst);
        S postedEntitySnd = entityService.post(entitySnd);
        S postedEntityTrd = entityService.post(entityTrd);
        Iterable<S> gotEntities = getEntityService().getAll();

        // Assertions
        assertNotNull(gotEntities);
        assertFalse(Iterables.isEmpty(gotEntities));
        assertEquals(numberOfPosts, Iterables.size(gotEntities));
        assertTrue(Iterables.contains(gotEntities, postedEntityFst));
        assertTrue(Iterables.contains(gotEntities, postedEntitySnd));
        assertTrue(Iterables.contains(gotEntities, postedEntityTrd));
    }

    @Test
    public void testPostAndDeleteByEntity() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        entityService.delete(postedEntity);

        // Assertions
        assertFalse(entityService.getRepository().existsById(postedEntity.getIdentifier()));
    }

    @Test
    public void testPostAndDeleteById() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        entityService.delete(postedEntity.getIdentifier());

        // Assertions
        assertFalse(entityService.getRepository().existsById(postedEntity.getIdentifier()));
    }

    @Test
    public void testPostAndExistsTrue() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);

        // Assertions
        assertTrue(entityService.getRepository().existsById(postedEntity.getIdentifier()));
        assertTrue(entityService.exists(postedEntity.getIdentifier()));
    }

    @Test
    public void testPostAndDeleteExistsFalse() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        entityService.delete(postedEntity);

        // Assertions
        assertFalse(entityService.getRepository().existsById(postedEntity.getIdentifier()));
        assertFalse(entityService.exists(postedEntity.getIdentifier()));
    }

    @Test
    public void testPutWithEqualEntities() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        S putEntity = entityService.put(postedEntity);

        // Assertions
        assertNotNull(putEntity);
        assertNotNull(putEntity.getIdentifier());
        assertEquals(postedEntity, putEntity);
    }

    @Test
    public void testPutIdempotence() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        S putEntity = entityService.put(postedEntity);
        S twicePutEntity = entityService.put(putEntity);

        // Assertions
        assertNotNull(twicePutEntity);
        assertNotNull(twicePutEntity.getIdentifier());
        assertEquals(putEntity, twicePutEntity);
    }

    @Test
    public void testDeleteIdempotence() {
        // Test data
        S entity = createDistinctTestEntity();

        // Execution
        S postedEntity = entityService.post(entity);
        entityService.delete(postedEntity);
        entityService.delete(postedEntity);

        // Assertions
        assertFalse(entityService.getRepository().existsById(postedEntity.getIdentifier()));
        assertFalse(entityService.exists(postedEntity.getIdentifier()));
    }

    protected T getEntityService() {
        return entityService;
    }

    /**
     * Creates and returns a new distinct entity.
     * Guarantees that returned entities do not conflict with each other.
     * Guarantees that multiple calls always return entities that are not equal according to the equals implementation.
     *
     * @return A new distinct entity.
     */
    protected abstract S createDistinctTestEntity();
}
