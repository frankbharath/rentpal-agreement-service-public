package com.rentpal.agreement.repository;

/*
 * @author frank
 * @created 19 Dec,2020 - 11:17 PM
 */

import com.rentpal.agreement.model.Tenant;
import com.rentpal.agreement.model.Unit;
import org.springframework.data.repository.CrudRepository;

public interface TenantRepository extends CrudRepository<Tenant, Long> {
    boolean existsByFirstNameAndLastNameAndEmailAndUnit(String firstName, String lastName, String email, Unit unit);
}
