package com.rentpal.agreement.repository;

/*
 * @author frank
 * @created 15 Dec,2020 - 1:20 AM
 */

import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {

    boolean existsByPropertyNameAndUser(String propertyName, User user);

    boolean existsByPropertyNameAndIdNotAndUser(String propertyName, Long id, User user);

    boolean existsByIdAndUser(Long id, User user);

    Optional<Property> findByIdAndUser(Long id, User user);

    List<Property> findByIdInAndUser(List<Long> propertyIds, User user);

    long countByUser(User user);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM #{#entityName} WHERE user_id=:userid AND propertysearch_tsv @@ to_tsquery(:searchQuery)")
    long countByUserAndTSV(Long userid, String searchQuery);

    List<Property> findByUser(User user, Pageable paging);

    @Query(nativeQuery = true, value = "SELECT * FROM #{#entityName} WHERE user_id=:userid AND propertysearch_tsv @@ to_tsquery(:searchQuery)")
    List<Property> findByUserAndTSV(Long userid, String searchQuery, Pageable paging);

    void deleteByIdAndUser(Long id, User user);


}
