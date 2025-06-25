package com.anode.tool.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public interface CommonService {

    public Object get(Class objectClass, Serializable id) ;       
    
    public List getAll(Class objectClass) ;

    public void store(Object bean);

    public void storeAll(Collection beans);
    
    public void delete(Object object) ;  
    
    /**
     * allows to set a specific lock implementation
     * Every operations are protected by a lock. Default implementation is not locking.
     * @param lock
     */
    public void setLock(Lock lock);
    
    public Lock getLock() ;
    
    /**
	 * Replace every id of the object by a new one given by the id factory. This is not a java clone equivalent !
	 * For instance to make an object transient, use a TransientIdFactory
	 * @return a map with key as the old id and value the new id.
	 */
    public Map<Serializable, Serializable> makeClone(Object object, IdFactory idFactory) ;
    
    public Serializable getMinimalId(Comparator<Serializable> comparator) ;
}
