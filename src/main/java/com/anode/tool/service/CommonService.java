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
     * <h3>Example Usage</h3>
     * <pre>{@code
     * WorkflowInfo workflowInfo = new WorkflowInfo();
     * workflowInfo.setCaseId("ORDER-123");
     * workflowInfo.setStatus(WorkflowStatus.IN_PROGRESS);
     *
     * // First call saves new instance
     * commonService.saveOrUpdate("ORDER-123", workflowInfo);
     *
     * // Subsequent calls update existing instance
     * workflowInfo.setStatus(WorkflowStatus.COMPLETED);
     * commonService.saveOrUpdate("ORDER-123", workflowInfo);
     * }</pre>
     *
     * @param id the unique identifier for the object; null for new objects with generated IDs
     * @param object the object to persist; must not be null
     * @throws IllegalArgumentException if object is null
     * @throws PersistenceException if the operation fails
     */
    public void saveOrUpdate(Serializable id, Object object);

    /**
     * Saves a new object to storage.
     *
     * <p>This method should only be used for creating new entities. If an object with the
     * same ID already exists, the behavior is implementation-dependent (may throw an exception
     * or be silently ignored). For idempotent operations, use {@link #saveOrUpdate} instead.
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * WorkflowDefinition definition = new WorkflowDefinition();
     * definition.setName("orderProcessing");
     * definition.setVersion("1.0");
     *
     * commonService.save("orderProcessing", definition);
     * }</pre>
     *
     * @param id the unique identifier for the new object; null for auto-generated IDs
     * @param object the object to save; must not be null
     * @throws IllegalArgumentException if object is null
     * @throws DuplicateKeyException if an object with this ID already exists (implementation-dependent)
     * @throws PersistenceException if the save operation fails
     */
    public void save(Serializable id, Object object);

    /**
     * Updates an existing object in storage.
     *
     * <p>This method should only be used for updating existing entities. If the object
     * does not exist, the behavior is implementation-dependent (may throw an exception
     * or create a new object). For idempotent operations, use {@link #saveOrUpdate} instead.
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * WorkflowInfo workflowInfo = commonService.get(WorkflowInfo.class, "ORDER-123");
     * workflowInfo.setPendWorkBasket("APPROVAL_QUEUE");
     *
     * commonService.update("ORDER-123", workflowInfo);
     * }</pre>
     *
     * @param id the unique identifier of the object to update; must not be null
     * @param object the updated object; must not be null
     * @throws IllegalArgumentException if id or object is null
     * @throws EntityNotFoundException if no object with this ID exists (implementation-dependent)
     * @throws PersistenceException if the update operation fails
     */
    public void update(Serializable id, Object object);

    /**
     * Saves a collection of new objects in a single batch operation.
     *
     * <p>This method is optimized for bulk inserts and should be used when saving multiple
     * new entities at once. It may perform significantly better than calling {@link #save}
     * repeatedly due to batching optimizations.
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * List<Step> steps = Arrays.asList(
     *     new Step("validateOrder", StepType.TASK),
     *     new Step("checkCredit", StepType.TASK),
     *     new Step("approveOrder", StepType.TASK)
     * );
     *
     * commonService.saveCollection(steps);
     * }</pre>
     *
     * @param objects the collection of objects to save; must not be null or empty
     * @throws IllegalArgumentException if objects is null or empty
     * @throws PersistenceException if the batch save operation fails
     */
    public void saveCollection(Collection objects);

    /**
     * Saves or updates a collection of objects in a single batch operation.
     *
     * <p>This method provides bulk upsert semantics - objects will be created if they don't
     * exist or updated if they do. This is the most efficient way to persist multiple objects
     * when you don't know which ones already exist in storage.
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * List<WorkflowVariable> variables = Arrays.asList(
     *     new WorkflowVariable("orderId", "ORD-001"),
     *     new WorkflowVariable("amount", "1500.00"),
     *     new WorkflowVariable("status", "PENDING")
     * );
     *
     * commonService.saveOrUpdateCollection(variables);
     * }</pre>
     *
     * @param objects the collection of objects to persist; must not be null or empty
     * @throws IllegalArgumentException if objects is null or empty
     * @throws PersistenceException if the batch operation fails
     */
    public void saveOrUpdateCollection(Collection objects);

    /**
     * Deletes an object from storage by its unique identifier.
     *
     * <p>After deletion, attempts to retrieve the object using {@link #get} should return null.
     * If the object doesn't exist, the behavior is implementation-dependent (may be silently
     * ignored or throw an exception).
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * // Delete completed workflow instance
     * commonService.delete("ORDER-123");
     *
     * // Verify deletion
     * WorkflowInfo deleted = commonService.get(WorkflowInfo.class, "ORDER-123");
     * assert deleted == null;
     * }</pre>
     *
     * @param id the unique identifier of the object to delete; must not be null
     * @throws IllegalArgumentException if id is null
     * @throws PersistenceException if the delete operation fails
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
     * <h3>Example Usage</h3>
     * <pre>{@code
     * // Retrieve workflow instance by case ID
     * WorkflowInfo workflowInfo = commonService.get(WorkflowInfo.class, "ORDER-123");
     *
     * if (workflowInfo != null) {
     *     String status = workflowInfo.getStatus();
     *     WorkflowVariables vars = workflowInfo.getWorkflowVariables();
     * }
     *
     * // Retrieve workflow definition
     * WorkflowDefinition definition = commonService.get(
     *     WorkflowDefinition.class,
     *     "orderProcessing"
     * );
     * }</pre>
     *
     * @param <T> the type of object to retrieve
     * @param objectClass the class of the object to retrieve; must not be null
     * @param id the unique identifier of the object; must not be null
     * @return the object with the given ID, or null if not found
     * @throws IllegalArgumentException if objectClass or id is null
     * @throws PersistenceException if the retrieval operation fails
     */
    public <T> T get(Class<T> objectClass, Serializable id);

    /**
     * Retrieves all objects of a given type from storage.
     *
     * <p>This method loads all instances of the specified class. Use with caution on large
     * datasets as it may cause memory issues. For large result sets, consider implementing
     * pagination or streaming APIs in your storage layer.
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * // Load all workflow definitions
     * List<WorkflowDefinition> allDefinitions =
     *     commonService.getAll(WorkflowDefinition.class);
     *
     * for (WorkflowDefinition def : allDefinitions) {
     *     System.out.println("Workflow: " + def.getName());
     * }
     *
     * // Load all active workflow instances (warning: may be large)
     * List<WorkflowInfo> allInstances = commonService.getAll(WorkflowInfo.class);
     * }</pre>
     *
     * @param <T> the type of objects to retrieve
     * @param type the class of objects to retrieve; must not be null
     * @return a list of all objects of the given type; empty list if none exist
     * @throws IllegalArgumentException if type is null
     * @throws PersistenceException if the retrieval operation fails
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
     * <h3>Example Usage</h3>
     * <pre>{@code
     * // Find workflow definition by name (assuming name is unique)
     * WorkflowDefinition definition = commonService.getUniqueItem(
     *     WorkflowDefinition.class,
     *     "name",
     *     "orderProcessing"
     * );
     *
     * // Find workflow instance by business key
     * WorkflowInfo workflowInfo = commonService.getUniqueItem(
     *     WorkflowInfo.class,
     *     "businessKey",
     *     "PO-2024-001"
     * );
     *
     * if (workflowInfo == null) {
     *     // No workflow found with this business key
     * }
     * }</pre>
     *
     * @param <T> the type of object to retrieve
     * @param type the class of the object to retrieve; must not be null
     * @param uniqueKeyName the name of the unique field to query; must not be null
     * @param uniqueKeyValue the value to match; must not be null
     * @return the unique object matching the key-value pair, or null if not found
     * @throws IllegalArgumentException if any parameter is null
     * @throws NonUniqueResultException if multiple objects match (implementation-dependent)
     * @throws PersistenceException if the query operation fails
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
     * the lock quickly to avoid blocking other operations and potential deadlocks. The
     * transaction should be as short as possible, performing only the necessary read-modify-write
     * operation.
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * @Transactional
     * public void incrementWorkflowCounter(String caseId) {
     *     // Acquire pessimistic lock to prevent concurrent updates
     *     WorkflowInfo workflowInfo = commonService.getLocked(
     *         WorkflowInfo.class,
     *         caseId
     *     );
     *
     *     // Safely modify the object
     *     int counter = workflowInfo.getRetryCount();
     *     workflowInfo.setRetryCount(counter + 1);
     *
     *     // Update and release lock at transaction commit
     *     commonService.update(caseId, workflowInfo);
     * }
     *
     * // Example: Prevent double-processing of work items
     * @Transactional
     * public boolean claimWorkItem(String workItemId, String userId) {
     *     WorkItem item = commonService.getLocked(WorkItem.class, workItemId);
     *
     *     if (item.getAssignee() != null) {
     *         return false; // Already claimed
     *     }
     *
     *     item.setAssignee(userId);
     *     item.setClaimedAt(Instant.now());
     *     commonService.update(workItemId, item);
     *     return true;
     * }
     * }</pre>
     *
     * <h3>Lock Semantics</h3>
     * <ul>
     *   <li>Equivalent to SQL {@code SELECT ... FOR UPDATE}</li>
     *   <li>Other transactions attempting to lock the same row will block</li>
     *   <li>Lock is held until transaction commits or rolls back</li>
     *   <li>May cause deadlocks if locks are acquired in inconsistent order</li>
     * </ul>
     *
     * @param <T> the type of object to retrieve
     * @param objectClass the class of the object to retrieve; must not be null
     * @param id the unique identifier of the object to lock; must not be null
     * @return the locked object, or null if not found
     * @throws IllegalArgumentException if objectClass or id is null
     * @throws TransactionRequiredException if called outside a transaction context
     * @throws LockTimeoutException if lock cannot be acquired within timeout period
     * @throws PersistenceException if the locking operation fails
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
     * <p>Common use cases include:
     * <ul>
     *   <li>Creating a new workflow instance from an existing template</li>
     *   <li>Duplicating a workflow definition with new identifiers</li>
     *   <li>Making an object transient (detached from persistence) using a {@code TransientIdFactory}</li>
     *   <li>Copying object graphs while maintaining referential integrity</li>
     * </ul>
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * // Clone a workflow definition with new IDs
     * WorkflowDefinition original = commonService.get(
     *     WorkflowDefinition.class,
     *     "orderProcessing"
     * );
     *
     * // Create factory that generates new UUIDs
     * IdFactory uuidFactory = new UUIDIdFactory();
     *
     * // Clone with new IDs
     * Map<Serializable, Serializable> idMapping =
     *     commonService.makeClone(original, uuidFactory);
     *
     * // idMapping contains: {oldId1 -> newId1, oldId2 -> newId2, ...}
     * for (Map.Entry<Serializable, Serializable> entry : idMapping.entrySet()) {
     *     System.out.println("Old ID: " + entry.getKey() +
     *                       " -> New ID: " + entry.getValue());
     * }
     *
     * // Make object transient (detach from database)
     * IdFactory transientFactory = new TransientIdFactory();
     * commonService.makeClone(workflowInfo, transientFactory);
     * }</pre>
     *
     * <h3>ID Mapping</h3>
     * The returned map contains all ID substitutions performed during cloning:
     * <ul>
     *   <li>Key: Original identifier</li>
     *   <li>Value: New identifier assigned by the factory</li>
     * </ul>
     * This mapping can be used to update external references or track the cloning operation.
     *
     * @param object the object to clone; must not be null
     * @param idFactory the factory responsible for generating new identifiers; must not be null
     * @return a map where keys are old IDs and values are new IDs assigned during cloning
     * @throws IllegalArgumentException if object or idFactory is null
     * @throws PersistenceException if the cloning operation fails
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
     * <h3>Example Usage</h3>
     * <pre>{@code
     * // Find the smallest numeric ID
     * Comparator<Serializable> numericComparator =
     *     (id1, id2) -> Integer.compare((Integer) id1, (Integer) id2);
     *
     * Serializable minId = commonService.getMinimalId(numericComparator);
     * System.out.println("Smallest ID: " + minId);
     *
     * // Find the oldest timestamp-based ID
     * Comparator<Serializable> timestampComparator =
     *     (id1, id2) -> Long.compare((Long) id1, (Long) id2);
     *
     * Serializable oldestId = commonService.getMinimalId(timestampComparator);
     * }</pre>
     *
     * @param comparator the comparator to determine minimum value; must not be null
     * @return the minimal identifier, or null if no objects exist
     * @throws IllegalArgumentException if comparator is null
     * @throws PersistenceException if the operation fails
     */
    public Serializable getMinimalId(Comparator<Serializable> comparator);

    /**
     * Atomically increments a named counter and returns the new value.
     *
     * <p>This method provides thread-safe, atomic counter incrementation suitable for
     * generating sequential identifiers, tracking metrics, or implementing distributed
     * sequences. The counter is automatically created if it doesn't exist, starting from 1.
     *
     * <h3>Use Cases</h3>
     * <ul>
     *   <li>Generating sequential case IDs for workflows</li>
     *   <li>Tracking total number of workflow executions</li>
     *   <li>Implementing business sequence numbers</li>
     *   <li>Counting events or operations</li>
     * </ul>
     *
     * <h3>Example Usage</h3>
     * <pre>{@code
     * // Generate sequential order numbers
     * public String createOrderNumber() {
     *     long sequence = commonService.incrCounter("orderNumber");
     *     return String.format("ORD-%06d", sequence);
     * }
     * // First call:  ORD-000001
     * // Second call: ORD-000002
     * // Third call:  ORD-000003
     *
     * // Track workflow execution count
     * public void startWorkflow(String caseId) {
     *     long executionCount = commonService.incrCounter("totalWorkflows");
     *     log.info("Starting workflow {} (total executions: {})",
     *         caseId, executionCount);
     * }
     *
     * // Per-workflow-type counters
     * public String generateCaseId(String workflowType) {
     *     String counterKey = "caseId." + workflowType;
     *     long sequence = commonService.incrCounter(counterKey);
     *     return workflowType + "-" + sequence;
     * }
     * }</pre>
     *
     * <h3>Thread Safety</h3>
     * This operation is guaranteed to be atomic and thread-safe. Multiple concurrent calls
     * with the same key will receive unique, sequential values without gaps or duplicates.
     *
     * @param key the unique name of the counter; must not be null or empty
     * @return the incremented value (1 for first call, 2 for second, etc.)
     * @throws IllegalArgumentException if key is null or empty
     * @throws PersistenceException if the increment operation fails
     */
    public long incrCounter(String key);
}
