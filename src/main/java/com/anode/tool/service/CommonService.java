package com.anode.tool.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Generic data access service providing CRUD operations and persistence management.
 *
 * <p>The {@code CommonService} interface defines a pluggable persistence layer for the workflow
 * engine, allowing integration with various storage backends (databases, in-memory stores, etc.).
 * It provides standard CRUD operations, collection management, pessimistic locking, object cloning,
 * and atomic counter operations.
 *
 * <h2>Core Operations</h2>
 * The service supports the following categories of operations:
 * <ul>
 *   <li><b>Single Object Persistence</b> - {@link #save}, {@link #update}, {@link #saveOrUpdate}</li>
 *   <li><b>Collection Operations</b> - {@link #saveCollection}, {@link #saveOrUpdateCollection}</li>
 *   <li><b>Retrieval</b> - {@link #get}, {@link #getAll}, {@link #getUniqueItem}</li>
 *   <li><b>Deletion</b> - {@link #delete}</li>
 *   <li><b>Concurrency Control</b> - {@link #getLocked} for pessimistic locking</li>
 *   <li><b>Cloning</b> - {@link #makeClone} for creating deep copies with new IDs</li>
 *   <li><b>Counters</b> - {@link #incrCounter} for atomic incrementing</li>
 * </ul>
 *
 * <h2>Example Implementation with JPA</h2>
 * <pre>{@code
 * @Service
 * public class JpaCommonService implements CommonService {
 *     @PersistenceContext
 *     private EntityManager entityManager;
 *
 *     @Override
 *     @Transactional
 *     public void saveOrUpdate(Serializable id, Object object) {
 *         if (id == null || get(object.getClass(), id) == null) {
 *             entityManager.persist(object);
 *         } else {
 *             entityManager.merge(object);
 *         }
 *     }
 *
 *     @Override
 *     @Transactional(readOnly = true)
 *     public <T> T get(Class<T> objectClass, Serializable id) {
 *         return entityManager.find(objectClass, id);
 *     }
 *
 *     @Override
 *     @Transactional(readOnly = true)
 *     public <T> T getLocked(Class<T> objectClass, Serializable id) {
 *         return entityManager.find(objectClass, id, LockModeType.PESSIMISTIC_WRITE);
 *     }
 * }
 * }</pre>
 *
 * <h2>Usage in Workflow Engine</h2>
 * The workflow engine uses this service to persist workflow state:
 * <pre>{@code
 * public class WorkflowPersistenceService {
 *     private CommonService commonService;
 *
 *     public void persistWorkflowInstance(WorkflowInfo workflowInfo) {
 *         commonService.saveOrUpdate(workflowInfo.getCaseId(), workflowInfo);
 *     }
 *
 *     public WorkflowInfo loadWorkflowInstance(String caseId) {
 *         return commonService.get(WorkflowInfo.class, caseId);
 *     }
 *
 *     public WorkflowInfo lockWorkflowInstance(String caseId) {
 *         // Pessimistic lock for concurrent access control
 *         return commonService.getLocked(WorkflowInfo.class, caseId);
 *     }
 * }
 * }</pre>
 *
 * <h2>Thread Safety and Transactions</h2>
 * <ul>
 *   <li>Implementations must be thread-safe for concurrent workflow execution</li>
 *   <li>Methods should participate in existing transactions or create new ones</li>
 *   <li>Use {@link #getLocked} within short-lived transactions to avoid deadlocks</li>
 *   <li>Read-only operations should be marked as such for optimization</li>
 * </ul>
 *
 * <h2>Storage Backend Flexibility</h2>
 * Implementations can use various storage mechanisms:
 * <ul>
 *   <li><b>JPA/Hibernate</b> - Relational database persistence</li>
 *   <li><b>NoSQL</b> - MongoDB, Cassandra, DynamoDB</li>
 *   <li><b>In-Memory</b> - ConcurrentHashMap for testing or transient workflows</li>
 *   <li><b>Cache</b> - Redis, Hazelcast for distributed caching</li>
 * </ul>
 *
 * @see IdFactory
 * @since 0.0.1
 */
public interface CommonService {

    /**
     * Saves a new object or updates an existing one based on whether it exists in storage.
     *
     * <p>This method provides upsert semantics - if an object with the given ID exists,
     * it will be updated; otherwise, a new object will be created. This is the most commonly
     * used persistence method in the workflow engine.
     *
     * <p>Example: Save or update a workflow instance
     * <pre>{@code
     * WorkflowInfo workflowInfo = new WorkflowInfo();
     * workflowInfo.setCaseId("ORDER-123");
     * workflowInfo.setStatus(WorkflowStatus.IN_PROGRESS);
     * commonService.saveOrUpdate("ORDER-123", workflowInfo);
     * }</pre>
     *
     * @param id the unique identifier for the object; null for new objects with generated IDs
     * @param object the object to persist; must not be null
     * @throws IllegalArgumentException if object is null
     */
    public void saveOrUpdate(Serializable id, Object object);

    /**
     * Saves a new object to storage.
     *
     * <p>This method should only be used for creating new entities. If an object with the
     * same ID already exists, the behavior is implementation-dependent (may throw an exception
     * or be silently ignored). For idempotent operations, use {@link #saveOrUpdate} instead.
     *
     * <p>Example: Save a new workflow definition
     * <pre>{@code
     * WorkflowDefinition definition = new WorkflowDefinition();
     * definition.setName("orderProcessing");
     * definition.setVersion("1.0");
     * commonService.save("orderProcessing", definition);
     * }</pre>
     *
     * @param id the unique identifier for the new object; null for auto-generated IDs
     * @param object the object to save; must not be null
     * @throws IllegalArgumentException if object is null
     */
    public void save(Serializable id, Object object);

    /**
     * Updates an existing object in storage.
     *
     * <p>This method should only be used for updating existing entities. If the object
     * does not exist, the behavior is implementation-dependent (may throw an exception
     * or create a new object). For idempotent operations, use {@link #saveOrUpdate} instead.
     *
     * <p>Example: Update workflow status
     * <pre>{@code
     * WorkflowInfo workflowInfo = commonService.get(WorkflowInfo.class, "ORDER-123");
     * workflowInfo.setPendWorkBasket("APPROVAL_QUEUE");
     * commonService.update("ORDER-123", workflowInfo);
     * }</pre>
     *
     * @param id the unique identifier of the object to update; must not be null
     * @param object the updated object; must not be null
     * @throws IllegalArgumentException if id or object is null
     */
    public void update(Serializable id, Object object);

    /**
     * Saves a collection of new objects in a single batch operation.
     *
     * <p>This method is optimized for bulk inserts and should be used when saving multiple
     * new entities at once. It may perform significantly better than calling {@link #save}
     * repeatedly due to batching optimizations.
     *
     * <p>Example: Save multiple workflow steps
     * <pre>{@code
     * List<Step> steps = Arrays.asList(
     *     new Step("validateOrder", StepType.TASK),
     *     new Step("checkCredit", StepType.TASK),
     *     new Step("approveOrder", StepType.TASK)
     * );
     * commonService.saveCollection(steps);
     * }</pre>
     *
     * @param objects the collection of objects to save; must not be null or empty
     * @throws IllegalArgumentException if objects is null or empty
     */
    public void saveCollection(Collection objects);

    /**
     * Saves or updates a collection of objects in a single batch operation.
     *
     * <p>This method provides bulk upsert semantics - objects will be created if they don't
     * exist or updated if they do. This is the most efficient way to persist multiple objects
     * when you don't know which ones already exist in storage.
     *
     * <p>Example: Save or update workflow variables
     * <pre>{@code
     * List<WorkflowVariable> variables = Arrays.asList(
     *     new WorkflowVariable("orderId", "ORD-001"),
     *     new WorkflowVariable("amount", "1500.00"),
     *     new WorkflowVariable("status", "PENDING")
     * );
     * commonService.saveOrUpdateCollection(variables);
     * }</pre>
     *
     * @param objects the collection of objects to persist; must not be null or empty
     * @throws IllegalArgumentException if objects is null or empty
     */
    public void saveOrUpdateCollection(Collection objects);

    /**
     * Deletes an object from storage by its unique identifier.
     *
     * <p>After deletion, attempts to retrieve the object using {@link #get} should return null.
     * If the object doesn't exist, the behavior is implementation-dependent (may be silently
     * ignored or throw an exception).
     *
     * <p>Example: Delete a workflow instance
     * <pre>{@code
     * commonService.delete("ORDER-123");
     * }</pre>
     *
     * @param id the unique identifier of the object to delete; must not be null
     * @throws IllegalArgumentException if id is null
     */
    public void delete(Serializable id);

    /**
     * Retrieves an object from storage by its unique identifier and type.
     *
     * <p>This is the primary method for loading objects by their ID. Returns null if no
     * object with the given ID exists. The returned object is detached from any persistence
     * context and can be safely modified without affecting stored state unless explicitly
     * saved.
     *
     * <p>Example: Retrieve a workflow instance by case ID
     * <pre>{@code
     * WorkflowInfo workflowInfo = commonService.get(WorkflowInfo.class, "ORDER-123");
     * }</pre>
     *
     * @param <T> the type of object to retrieve
     * @param objectClass the class of the object to retrieve; must not be null
     * @param id the unique identifier of the object; must not be null
     * @return the object with the given ID, or null if not found
     * @throws IllegalArgumentException if objectClass or id is null
     */
    public <T> T get(Class<T> objectClass, Serializable id);

    /**
     * Retrieves all objects of a given type from storage.
     *
     * <p>This method loads all instances of the specified class. Use with caution on large
     * datasets as it may cause memory issues. For large result sets, consider implementing
     * pagination or streaming APIs in your storage layer.
     *
     * <p>Example: Load all workflow definitions
     * <pre>{@code
     * List<WorkflowDefinition> allDefinitions =
     *     commonService.getAll(WorkflowDefinition.class);
     * }</pre>
     *
     * @param <T> the type of objects to retrieve
     * @param type the class of objects to retrieve; must not be null
     * @return a list of all objects of the given type; empty list if none exist
     * @throws IllegalArgumentException if type is null
     */
    public <T> List<T> getAll(Class<T> type);

    /**
     * Retrieves a single object by a unique key-value pair.
     *
     * <p>This method queries for an object where a specific field (uniqueKeyName) matches
     * a given value. It should only be used when the key is guaranteed to be unique. If
     * multiple objects match, the behavior is implementation-dependent (may return first
     * match, throw exception, or be undefined).
     *
     * <p>Example: Find workflow definition by name
     * <pre>{@code
     * WorkflowDefinition definition = commonService.getUniqueItem(
     *     WorkflowDefinition.class,
     *     "name",
     *     "orderProcessing"
     * );
     * }</pre>
     *
     * @param <T> the type of object to retrieve
     * @param type the class of the object to retrieve; must not be null
     * @param uniqueKeyName the name of the unique field to query; must not be null
     * @param uniqueKeyValue the value to match; must not be null
     * @return the unique object matching the key-value pair, or null if not found
     * @throws IllegalArgumentException if any parameter is null
     */
    public <T> T getUniqueItem(Class<T> type, String uniqueKeyName, String uniqueKeyValue);

    /**
     * Retrieves an object with a pessimistic write lock for concurrent modification control.
     *
     * <p>This method acquires a database-level lock on the object row, preventing other
     * transactions from reading or modifying it until the current transaction completes.
     * This is essential for preventing race conditions when multiple workflow instances
     * or threads attempt to modify the same object concurrently.
     *
     * <p><b>IMPORTANT:</b> This method MUST be used within a transaction that releases
     * the lock quickly to avoid blocking other operations and potential deadlocks.
     *
     * <p>Lock semantics: Equivalent to SQL {@code SELECT ... FOR UPDATE}. Lock is held
     * until transaction commits or rolls back.
     *
     * @param <T> the type of object to retrieve
     * @param objectClass the class of the object to retrieve; must not be null
     * @param id the unique identifier of the object to lock; must not be null
     * @return the locked object, or null if not found
     * @throws IllegalArgumentException if objectClass or id is null
     */
    public <T> T getLocked(Class<T> objectClass, Serializable id);

    /**
     * Creates a deep copy of an object with new identifiers assigned by the provided factory.
     *
     * <p>This method performs a specialized cloning operation that replaces all object identifiers
     * (primary keys, foreign keys, etc.) with new values generated by the {@link IdFactory}. This
     * is NOT equivalent to Java's {@code Object.clone()} - it performs ID substitution while
     * copying the object graph.
     *
     * <p>Common use cases include creating a new workflow instance from an existing template,
     * duplicating a workflow definition with new identifiers, or making an object transient.
     *
     * <p>Example: Clone a workflow definition with new IDs
     * <pre>{@code
     * WorkflowDefinition original = commonService.get(
     *     WorkflowDefinition.class,
     *     "orderProcessing"
     * );
     * IdFactory uuidFactory = new UUIDIdFactory();
     * Map<Serializable, Serializable> idMapping =
     *     commonService.makeClone(original, uuidFactory);
     * }</pre>
     *
     * @param object the object to clone; must not be null
     * @param idFactory the factory responsible for generating new identifiers; must not be null
     * @return a map where keys are old IDs and values are new IDs assigned during cloning
     * @throws IllegalArgumentException if object or idFactory is null
     *
     * @see IdFactory
     */
    public Map<Serializable, Serializable> makeClone(Object object, IdFactory idFactory);

    /**
     * Retrieves the minimal (smallest) identifier across all stored objects.
     *
     * <p>This method scans all object identifiers and returns the minimum value according
     * to the provided comparator. This can be useful for finding the oldest record, the
     * lowest priority item, or any other minimum value based on custom comparison logic.
     *
     * <p>Example: Find the smallest numeric ID
     * <pre>{@code
     * Comparator<Serializable> numericComparator =
     *     (id1, id2) -> Integer.compare((Integer) id1, (Integer) id2);
     * Serializable minId = commonService.getMinimalId(numericComparator);
     * }</pre>
     *
     * @param comparator the comparator to determine minimum value; must not be null
     * @return the minimal identifier, or null if no objects exist
     * @throws IllegalArgumentException if comparator is null
     */
    public Serializable getMinimalId(Comparator<Serializable> comparator);

    /**
     * Atomically increments a named counter and returns the new value.
     *
     * <p>This method provides thread-safe, atomic counter incrementation suitable for
     * generating sequential identifiers, tracking metrics, or implementing distributed
     * sequences. The counter is automatically created if it doesn't exist, starting from 1.
     *
     * <p>Use cases include:
     * <ul>
     *   <li>Generating sequential case IDs for workflows</li>
     *   <li>Tracking total number of workflow executions</li>
     *   <li>Implementing business sequence numbers</li>
     *   <li>Counting events or operations</li>
     * </ul>
     *
     * <p>Example: Generate sequential order numbers
     * <pre>{@code
     * long sequence = commonService.incrCounter("orderNumber");
     * String orderNumber = String.format("ORD-%06d", sequence);
     * }</pre>
     *
     * <p>This operation is guaranteed to be atomic and thread-safe. Multiple concurrent calls
     * with the same key will receive unique, sequential values without gaps or duplicates.
     *
     * @param key the unique name of the counter; must not be null or empty
     * @return the incremented value (1 for first call, 2 for second, etc.)
     * @throws IllegalArgumentException if key is null or empty
     */
    public long incrCounter(String key);
}
