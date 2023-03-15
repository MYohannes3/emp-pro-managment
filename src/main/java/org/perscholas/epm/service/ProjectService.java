package org.perscholas.epm.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.perscholas.epm.dao.EmployeeRepo;
import org.perscholas.epm.dao.ManagerRepo;
import org.perscholas.epm.dao.ProjectRepo;
import org.perscholas.epm.model.Employee;
import org.perscholas.epm.model.Manager;
import org.perscholas.epm.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class ProjectService {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ManagerRepo ManagerRepo;
    @Autowired
    private EmployeeRepo EmployeeRepo;

    public void projectServiceImpl(ProjectRepo projectDao, ManagerRepo ManagerRepo, EmployeeRepo EmployeeRepo){
        this.projectRepo = projectDao;
        this.ManagerRepo = ManagerRepo;
        this.EmployeeRepo = EmployeeRepo;
    }

    public Project loadProjectById(int projectId){
        return projectRepo.findById(projectId).orElseThrow(() ->new EntityNotFoundException("project with id" +projectId+"not Found"));
    }


    public Project createProject(String projectName, String projectDuration, String projectDescription, int managerId) {
        Manager manager = ManagerRepo.findById(managerId).orElseThrow(() -> new EntityNotFoundException("Manager with id" + managerId + " Not Found"));
        return projectRepo.save(new Project(projectName, projectDuration, projectDescription, manager));
    }


    public Project createOrUpdateProject(Project project) {

        return projectRepo.save(project);
    }


    public List<Project> findProjectsByProjectName(String keyword) {
        return projectRepo.findProjectsByProjectNameContains(keyword);
    }

    public void assignEmployeeToProject(int projectId, int employeeId) {
        Employee employee = EmployeeRepo.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " Not Found"));
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Course with id " + projectId + " Not Found"));
        project.assignEmployeeToProject(employee);
    }


    public List<Project> fetchAll() {
        return projectRepo.findAll();
    }

    public List<Project> fetchProjectForEmployee(int employeeId) {
        return null;
    }

    public List<Project> fetchProjectsForEmployee(int employeeId) {
        return projectRepo.getProjectsByEmployeeId(employeeId);
    }

    public void removeProject(int projectId) {

        projectRepo.deleteById(projectId);
    }

}
