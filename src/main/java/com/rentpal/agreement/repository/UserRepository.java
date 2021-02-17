package com.rentpal.agreement.repository;

/*
 * @author frank
 * @created 15 Dec,2020 - 12:01 AM
 */

import com.rentpal.agreement.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT id FROM #{#entityName} WHERE email=:email")
    Long getUserId(String email);
}
