package com.rentpal.agreement.repository;

/*
 * @author frank
 * @created 15 Dec,2020 - 9:47 PM
 */


import com.rentpal.agreement.model.Property;
import com.rentpal.agreement.model.Unit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends CrudRepository<Unit, Long> {
    boolean existsByDoorNumberAndFloorNumberAndProperty(String doorNo, Integer floorNo, Property property);

    boolean existsByIdAndProperty(Long id, Property property);

    boolean existsByDoorNumberAndFloorNumberAndPropertyAndIdNot(String doorNo, Integer floorNo, Property property, Long id);

    List<Unit> getByProperty(Property property);

    Optional<Unit> getByPropertyAndId(Property property, Long id);

    void deleteByIdAndProperty(Long id, Property property);
}
