package org.perscholas.epm.dao;

import org.perscholas.epm.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ManagerRepo extends JpaRepository<Manager, Integer> {
    @Query(value = "select m from Manager as m where m.firstName like %:name% or m.lastName like %:name%")
    List<Manager> findManagersByName(@Param("name") String name);
    @Query(value = "select m from Manager as m where m.user.email=:email")
    Manager findManagerByEmail(@Param("email") String email);
}
