package com.anode.tool.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommonRepository<T,ID extends Serializable> {

    Optional<T> get(ID id);
    <S extends T> S save(S entity);
    <S extends T> S saveOrUpdate(S entity);
    <S extends T> S update(S entity);
    <S extends T> void saveCollection(Collection<S> objects);
    <S extends T> void saveOrUpdateCollection(Collection<S> objects);
    void delete(ID id);
    <S extends T> List<S> getAll();
    <S extends T> S getUniqueItem(String uniqueKeyName, String uniqueKeyValue);

    /**
     * Recupere l'objet en faisant un lock pessimiste sur la ligne en base (ie
     * select for update)<br> ! A faire dans une transaction qui libere l'objet
     * rapidement
     */
    <S extends T> S  getLocked(ID id);

}
