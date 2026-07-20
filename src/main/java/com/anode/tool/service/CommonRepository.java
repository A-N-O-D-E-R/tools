package com.anode.tool.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic repository interface for data access operations.
 */
public interface CommonRepository<T,ID extends Serializable> {

	/**
	 * Gets an entity by ID.
	 * @param id the entity ID
	 * @return optional containing the entity if found
	 */
	Optional<T> get(ID id);

	/**
	 * Saves a new entity.
	 * @param entity the entity to save
	 * @return the saved entity
	 */
	<S extends T> S save(S entity);

	/**
	 * Saves or updates an entity.
	 * @param entity the entity to save or update
	 * @return the saved or updated entity
	 */
	<S extends T> S saveOrUpdate(S entity);

	/**
	 * Updates an existing entity.
	 * @param entity the entity to update
	 * @return the updated entity
	 */
	<S extends T> S update(S entity);

	/**
	 * Saves a collection of entities.
	 * @param objects the entities to save
	 */
	<S extends T> void saveCollection(Collection<S> objects);

	/**
	 * Saves or updates a collection of entities.
	 * @param objects the entities to save or update
	 */
	<S extends T> void saveOrUpdateCollection(Collection<S> objects);

	/**
	 * Deletes an entity by ID.
	 * @param id the entity ID
	 */
	void delete(ID id);

	/**
	 * Gets all entities.
	 * @return list of all entities
	 */
	<S extends T> List<S> getAll();

	/**
	 * Gets a unique entity by a key-value pair.
	 * @param uniqueKeyName the field name
	 * @param uniqueKeyValue the field value
	 * @return the entity if found
	 */
	<S extends T> S getUniqueItem(String uniqueKeyName, String uniqueKeyValue);

	/**
	 * Gets an entity with a pessimistic write lock.
	 * Must be used within a transaction that releases the lock quickly.
	 * @param id the entity ID
	 * @return the locked entity if found
	 */
	<S extends T> S getLocked(ID id);

}
