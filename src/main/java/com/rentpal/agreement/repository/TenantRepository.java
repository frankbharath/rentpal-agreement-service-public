package com.rentpal.agreement.repository;

import com.rentpal.agreement.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author frank
 * @created 19 Dec,2020 - 11:17 PM
 */
public interface TenantRepository extends CrudRepository<Tenant, Long> {
    boolean existsByFirstNameAndLastNameAndEmailAndUnit(String firstName, String lastName, String email, Unit unit);

    List<Tenant> findAllByUser(User user, Pageable paging);

    long countByUser(User user);

    void deleteByIdAndUser(Long id, User user);

    @Query(nativeQuery = true, value = "select * from #{#entityName} where movein > date_trunc('month', now()) - interval '12 month' and movein<date_trunc('month', now()) and user_id=:userId")
    List<Tenant> findAllByMoveinBetweenAndUser(Long userId);

    @Query(nativeQuery = true, value ="select info.totalRent as totalRent, info.propertyName, info.propertyId\n" +
            "from (select property_name as propertyName, property.id as propertyId, \n" +
            "sum(unit.rent * (extract(year from age(now(), movein)) * 12 +\n" +
            "extract(month from age(now(), movein)))) as totalRent \n" +
            "from tenant inner join unit on tenant.unit_id=unit.id \n" +
            "inner join property on property.id=unit.property_id \n" +
            "where movein > date_trunc('month', now()) - interval '12 month' \n" +
            "and movein<date_trunc('month', now()) and property.user_id=:userId group by property.id) " +
            "as info order by totalRent DESC limit 5 offset 0")
    List<PropertyTenantRentInfo> findPropertyRentInfo(Long userId);

    @Query(nativeQuery = true, value="select first_name as firstName, last_name as lastName, email as email, \n" +
            "movein + interval '1 month' * ((extract(year from age(now(), movein)) * 12 +\n" +
            "extract(month from age(now(), movein))) + 1) as nextPayment\n" +
            "from tenant where moveout>now() and user_id=:userId order by nextPayment")
    List<UpcomingPayments> findUpcomingPayments(Long userId);
}
