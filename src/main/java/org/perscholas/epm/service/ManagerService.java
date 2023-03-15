package org.perscholas.epm.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.perscholas.epm.dao.ManagerRepo;
import org.perscholas.epm.model.Manager;
import org.perscholas.epm.model.Project;
import org.perscholas.epm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class ManagerService  {
    @Autowired
    ManagerRepo ManagerRepo;
    @Autowired
    ProjectService projectService;
    @Autowired
    UserService userService;
@Autowired
    public ManagerService(ManagerRepo ManagerRepo, ProjectService projectService, UserService userService) {
        this.ManagerRepo = ManagerRepo;
        this.projectService = projectService;
        this.userService = userService;
    }

    public List<Manager> findManagersByName(String name) {
        return ManagerRepo.findManagersByName(name);
    }

    public Manager loadManagerByEmail(String email) {

        return ManagerRepo.findManagerByEmail(email);
    }

    public Manager createManager(String firstName, String lastName, String summary, String email, String password) {
        User user = userService.createUser(email, password);
        userService.assignRoleToUser(email, "Manager");
        return ManagerRepo.save(new Manager(firstName, lastName, summary, user));
    }
    public Manager updateManager(Manager manager) {

        return ManagerRepo.save(manager);
    }
    public Manager loadManagerById(Integer managerId) {
        return ManagerRepo.findById(managerId).orElseThrow(()->new EntityNotFoundException("manager with id" + managerId+ "not found"));
    }

    public List<Manager> fetchManagers() {

        return ManagerRepo.findAll();
    }
    public void removeManager(int managerId) {
    Manager manager=loadManagerById(managerId);
    for(Project project : manager.getProjects()){
        projectService.removeProject(project.getProjectId());
    }
    ManagerRepo.deleteById(managerId);
    }

}
