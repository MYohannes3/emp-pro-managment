package org.perscholas.epm.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.perscholas.epm.model.Manager;
import org.perscholas.epm.model.Project;
import org.perscholas.epm.model.User;
import org.perscholas.epm.service.ManagerService;
import org.perscholas.epm.service.ProjectService;
import org.perscholas.epm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.cglib.SpringCglibInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Controller
@RequestMapping(value = "/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    UserService userService;
    @Autowired
    ManagerService managerService;
    public ProjectController(ProjectService projectService, UserService userService, ManagerService managerService){

        this.projectService = projectService;
        this.userService = userService;
        this.managerService = managerService;
    }

    @GetMapping(value = "/index")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String projects(Model model, @RequestParam(name = "keyword", defaultValue = "")String keyword){
        List<Project> projects = projectService.findProjectsByProjectName(keyword);
        model.addAttribute("listProjects", projects);
        model.addAttribute("keyword", keyword);
        return "projects";
    }

    @GetMapping(value = "/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProject(int projectId, String keyword){
        projectService.removeProject(projectId);
        return "redirect:/projects/index?keyword="+keyword;
    }

    @GetMapping(value = "/updateProject")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public String updateProject(Model model, int projectId, Principal principal){
        // we used  principal so that we can get the loged in users name

        if(userService.doesCurrentUserHasRole("MANAGER")){
            Manager manager=managerService.loadManagerByEmail(principal.getName());
            model.addAttribute("CurrentManager", manager);
        }
        Project project = projectService.loadProjectById(projectId);
        List<Manager> managers = managerService.fetchManagers();

        model.addAttribute("project", project);
        model.addAttribute("listManagers", managers);
        return "updateProject";
    }


    @GetMapping(value = "/createProject")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public String formProjects(Model model, Principal principal){
        if (userService.doesCurrentUserHasRole("MANAGER")){
            Manager manager = managerService.loadManagerByEmail(principal.getName());
            model.addAttribute("CurrentManager", manager);
        }
        List<Manager> managers =managerService.fetchManagers();
        model.addAttribute("listManagers", managers);
        model.addAttribute("project", new Project());
        return "createProject";
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public String save(Project project){
        projectService.createOrUpdateProject(project);
      return userService.doesCurrentUserHasRole("MANAGER") ? "redirect:/projects/managerProjects": "redirect:/projects/index";

    }

    @GetMapping(value = "/employeeProjects")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public String projectsForCurrentEmployee(Model model, Principal principal){
        User user = userService.loadUserByEmail(principal.getName());
        List<Project> assignedProjects = projectService.fetchProjectsForEmployee(user.getEmployee().getEmployeeId());
        List<Project> otherProjects = new ArrayList<>();
        for (Project project : projectService.fetchAll()) {
            if (!assignedProjects.contains(project)) {
                otherProjects.add(project);
            }
        }
        model.addAttribute("otherProjects", otherProjects);
        model.addAttribute("listProjects", assignedProjects);
        model.addAttribute("firstName", user.getEmployee().getFirstName());
        return "employeeProjects";
    }

    @GetMapping(value = "/assignProject")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public String assignCurrentEmployeeToProject(int projectId, Principal principal){
        User user = userService.loadUserByEmail(principal.getName());
        projectService.assignEmployeeToProject(projectId, user.getEmployee().getEmployeeId());
        return "redirect:/projects/employeeProjects";
    }

    @GetMapping(value = "/managerProjects")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public String projectsForCurrentManager(Model model, Principal principal){
        User user= userService.loadUserByEmail(principal.getName());
        Manager manager = managerService.loadManagerById(user.getManager().getManagerId());
        model.addAttribute("listProjects", manager.getProjects());
        model.addAttribute("firstName", manager.getFirstName());
        return "managerProjects";
    }

    @GetMapping(value ="/manager")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String projectsByManagerId(Model model, int managerId) {
        Manager manager =managerService.loadManagerById(managerId);
        model.addAttribute("listProjects", manager.getProjects());
        model.addAttribute("firstName", manager.getFirstName());
        return "Projects/index";
    }
}
