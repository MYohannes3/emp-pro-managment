package org.perscholas.epm.dao;

import org.perscholas.epm.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectRepo extends JpaRepository<Project, Integer> {

    List<Project> findProjectsByProjectNameContains(String keyword);

    @Query(value = "select * from projects as p where p.project_id in (select w.project_id from works_on as w where w.employee_id =:employeeId)",nativeQuery = true)
    List<Project> getProjectsByEmployeeId(@Param("employeeId") int employeeId);
}
