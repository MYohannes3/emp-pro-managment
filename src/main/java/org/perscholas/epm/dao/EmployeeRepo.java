package org.perscholas.epm.dao;


import org.perscholas.epm.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
    public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
        @Query(value = "select e from Employee as e where e.firstName like %:name% or e.lastName like %:name%")
        List<Employee> findEmployeesByName(@Param("name") String name);

        @Query(value = "select e from Employee as e where e.user.email=:email")
        Employee findEmployeeByEmail(@Param("email") String email);

}
