package com.anode.tool.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public interface CommonService {

    public void saveOrUpdate(Object object);

    public void save(Object object);

    public void update(Object object);

    public void saveCollection(Collection objects);

    public void saveOrUpdateCollection(Collection objects);

    public void delete(Serializable id);

    public <T> T get(Class<T> objectClass, Serializable id);

    public <T> List<T> getAll(Class<T> type);

    public <T> T getUniqueItem(Class<T> type, String uniqueKeyName, String uniqueKeyValue);

    /**
     * Recupere l'objet en faisant un lock pessimiste sur la ligne en base (ie
     * select for update)<br> ! A faire dans une transaction qui libere l'objet
     * rapidement
     */
    public <T> T getLocked(Class<T> objectClass, Serializable id);

    /**
	 * Replace every id of the object by a new one given by the id factory. This is not a java clone equivalent !
	 * For instance to make an object transient, use a TransientIdFactory
	 * @return a map with key as the old id and value the new id.
	 */
    public Map<Serializable, Serializable> makeClone(Object object, IdFactory idFactory) ;
    
    public Serializable getMinimalId(Comparator<Serializable> comparator) ;

    /**
     * The method used to increment the value of a counter
     *
     * @param key the key for the counter
     * @return the incremented value of the counter
     */
    public long incrCounter(String key);
}
